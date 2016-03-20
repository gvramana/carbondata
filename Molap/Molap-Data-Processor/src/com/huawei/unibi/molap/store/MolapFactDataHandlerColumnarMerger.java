package com.huawei.unibi.molap.store;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.datastorage.store.NodeMeasureDataStore;
import com.huawei.unibi.molap.datastorage.store.columnar.BlockIndexerStorageForInt;
import com.huawei.unibi.molap.datastorage.store.columnar.IndexStorage;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionModel;
import com.huawei.unibi.molap.datastorage.store.dataholder.MolapWriteDataHolder;
import com.huawei.unibi.molap.datastorage.store.impl.FileFactory;
import com.huawei.unibi.molap.datastorage.util.StoreFactory;
import com.huawei.unibi.molap.file.manager.composite.FileData;
import com.huawei.unibi.molap.file.manager.composite.IFileManagerComposite;
import com.huawei.unibi.molap.file.manager.composite.LoadFolderData;
import com.huawei.unibi.molap.groupby.MolapAutoAggGroupBy;
import com.huawei.unibi.molap.groupby.MolapAutoAggGroupByExtended;
import com.huawei.unibi.molap.groupby.exception.MolapGroupByException;
import com.huawei.unibi.molap.keygenerator.columnar.ColumnarSplitter;
import com.huawei.unibi.molap.keygenerator.columnar.impl.MultiDimKeyVarLengthEquiSplitGenerator;
import com.huawei.unibi.molap.schema.metadata.MolapColumnarFactMergerInfo;
import com.huawei.unibi.molap.store.writer.MolapFactDataWriter;
import com.huawei.unibi.molap.store.writer.MolapFactDataWriterImpl;
import com.huawei.unibi.molap.store.writer.MolapFactDataWriterImplForIntIndex;
import com.huawei.unibi.molap.store.writer.MolapFactDataWriterImplForIntIndexAndAggBlock;
import com.huawei.unibi.molap.store.writer.MolapFactDataWriterImplForIntIndexAndAggBlockCompressed;
import com.huawei.unibi.molap.store.writer.exception.MolapDataWriterException;
import com.huawei.unibi.molap.util.MolapDataProcessorLogEvent;
import com.huawei.unibi.molap.util.MolapProperties;
import com.huawei.unibi.molap.util.MolapUtil;
import com.huawei.unibi.molap.util.ValueCompressionUtil;

public class MolapFactDataHandlerColumnarMerger implements MolapFactHandler
{

    /**
     * LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(MolapFactDataHandlerColumnar.class.getName());

    /**
     * data writer
     */
    private MolapFactDataWriter dataWriter;
    
    private String destLocation;

    /**
     * File manager
     */
    private IFileManagerComposite fileManager;

    /**
     * total number of entries in leaf node
     */
    private int entryCount;

    /**
     * startkey of each node
     */
    private byte[] startKey;

    /**
     * end key of each node
     */
    private byte[] endKey;

    /**
     * ValueCompressionModel
     */
    private ValueCompressionModel compressionModel;

    /**
     * data store which will hold the measure data
     */
    private NodeMeasureDataStore dataStore;

    /**
     * uniqueValue
     */
    private double[] uniqueValue;

    /**
     * leaf node size
     */
    private int leafNodeSize;

    /**
     * groupBy
     */
    private MolapAutoAggGroupBy groupBy;

    /**
     * MolapWriteDataHolder
     */
    private MolapWriteDataHolder[] dataHolder;

    /**
     * otherMeasureIndex
     */
    private int[] otherMeasureIndex;

    /**
     * customMeasureIndex
     */
    private int[] customMeasureIndex;

    /**
     * keyGenerator
     */
    private ColumnarSplitter columnarSplitter;

    /**
     * keyBlockHolder
     */
    private MolapKeyBlockHolder[] keyBlockHolder;

    private boolean isIntBasedIndexer;

    private boolean[] aggKeyBlock;

    private boolean isAggKeyBlock;

    private long processedDataCount;

    private boolean isCompressedKeyBlock;

    private ExecutorService writerExecutorService;

    private int numberOfColumns;

    private Object lock = new Object();

    private MolapWriteDataHolder keyDataHolder;

    private MolapColumnarFactMergerInfo molapFactDataMergerInfo;
    
    private int currentRestructNumber;
    
    
    //TODO SIMIAN
    /**
     * 
     * @param molapFactDataMergerInfo
     */
    public MolapFactDataHandlerColumnarMerger(
            MolapColumnarFactMergerInfo molapFactDataMergerInfo, int currentRestructNum)
    {
        this.molapFactDataMergerInfo=molapFactDataMergerInfo;
        this.aggKeyBlock = new boolean[molapFactDataMergerInfo.getDimLens().length];
        this.currentRestructNumber = currentRestructNum;
        isIntBasedIndexer = Boolean
                .parseBoolean(MolapCommonConstants.IS_INT_BASED_INDEXER_DEFAULTVALUE);

        this.isAggKeyBlock = Boolean
                .parseBoolean(MolapCommonConstants.AGGREAGATE_COLUMNAR_KEY_BLOCK_DEFAULTVALUE);
        if(isAggKeyBlock)
        {
            int highCardinalityVal = Integer
                    .parseInt(MolapProperties 
                            .getInstance()
                            .getProperty(
                                    MolapCommonConstants.HIGH_CARDINALITY_VALUE,
                                    MolapCommonConstants.HIGH_CARDINALITY_VALUE_DEFAULTVALUE));
            for(int i = 0;i < molapFactDataMergerInfo.getDimLens().length;i++)
            {
                if(molapFactDataMergerInfo.getDimLens()[i] < highCardinalityVal)
                {
                    this.aggKeyBlock[i] = true;
                }
            }
        }

        isCompressedKeyBlock = Boolean
                .parseBoolean(MolapCommonConstants.IS_COMPRESSED_KEYBLOCK_DEFAULTVALUE);

        LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "Initializing writer executers");
        writerExecutorService = Executors.newFixedThreadPool(3);

    }

    /**
     * This method will be used to get and update the step properties which will
     * required to run this step
     * 
     * @param totalRowLength
     *            total number of records in reacords
     * @param mdkeyLength
     *            lenght of mdkey
     * @throws MolapDataWriterException
     * 
     */
    public void initialise() throws MolapDataWriterException
    {
        fileManager = new LoadFolderData();
        fileManager.setName(new File(molapFactDataMergerInfo
                .getDestinationLocation()).getName());
        if(!molapFactDataMergerInfo.isGroupByEnabled())
        {
            try
            {
                setWritingConfiguration(molapFactDataMergerInfo
                        .getMdkeyLength());
            }
            catch(MolapDataWriterException e)
            {
                throw e;
            }
        }
        else
        {
            if(!molapFactDataMergerInfo.isMergingRequestForCustomAgg())
            {
                this.groupBy = new MolapAutoAggGroupBy(
                        molapFactDataMergerInfo.getAggregators(),
                        molapFactDataMergerInfo.getAggregatorClass(),
                        molapFactDataMergerInfo.getSchemaName(),
                        molapFactDataMergerInfo.getCubeName(),
                        molapFactDataMergerInfo.getTableName(), 
                        null, 
                        MolapCommonConstants.MERGER_FOLDER_EXT+MolapCommonConstants.FILE_INPROGRESS_STATUS, 
                        currentRestructNumber);
            }

            else
            {
                this.groupBy = new MolapAutoAggGroupByExtended(
                        molapFactDataMergerInfo.getAggregators(),
                        molapFactDataMergerInfo.getAggregatorClass(),
                        molapFactDataMergerInfo.getSchemaName(),
                        molapFactDataMergerInfo.getCubeName(),
                        molapFactDataMergerInfo.getTableName(), 
                        null,
                        MolapCommonConstants.MERGER_FOLDER_EXT+MolapCommonConstants.FILE_INPROGRESS_STATUS, 
                        currentRestructNumber);
            }
        }

    }

    /**
     * This method will add mdkey and measure values to store
     * 
     * @param row
     * @throws MolapDataWriterException
     * 
     */
    public void addDataToStore(Object[] row) throws MolapDataWriterException
    {
        if(molapFactDataMergerInfo.isGroupByEnabled())
        {
            try
            {
                groupBy.add(row);
            }
            catch(MolapGroupByException e)
            {
                throw new MolapDataWriterException("Problem in doing groupBy",
                        e);
            }
        }
        else
        {
            addToStore(row);
        }
    }

    /**
     * below method will be used to add row to store
     * 
     * @param row
     * @throws MolapDataWriterException
     */
    private void addToStore(Object[] row) throws MolapDataWriterException
    {
        byte[] mdkey = (byte[])row[molapFactDataMergerInfo.getMeasureCount()];
        byte[] b = null;
        if(this.entryCount == 0)
        {
            this.startKey = mdkey;
        }
        this.endKey = mdkey;
        // add to key store
        keyDataHolder.setWritableByteArrayValueByIndex(entryCount, mdkey);

        // CHECKSTYLE:OFF Approval No:Approval-351
        for(int i = 0;i < otherMeasureIndex.length;i++)
        {
            if(null == row[otherMeasureIndex[i]])
            {
                dataHolder[otherMeasureIndex[i]].setWritableDoubleValueByIndex(
                        entryCount, uniqueValue[otherMeasureIndex[i]]);
            }
            else
            {
                dataHolder[otherMeasureIndex[i]].setWritableDoubleValueByIndex(
                        entryCount, (Double)row[otherMeasureIndex[i]]);
            }
        }
        for(int i = 0;i < customMeasureIndex.length;i++)
        {
            b = (byte[])row[customMeasureIndex[i]];
            dataHolder[customMeasureIndex[i]].setWritableByteArrayValueByIndex(
                    entryCount, b);
        }
        // CHECKSTYLE:ON
        this.entryCount++;
        // if entry count reaches to leaf node size then we are ready to
        // write
        // this to leaf node file and update the intermediate files
        if(this.entryCount == this.leafNodeSize)
        {
            // byte[][][] data = new byte[numberOfColumns][][];
            // for(int i = 0;i < keyBlockHolder.length;i++)
            // {
            // data[i]=keyBlockHolder[i].getKeyBlock().clone();
            // }
            byte[][] byteArrayValues = keyDataHolder.getByteArrayValues()
                    .clone();
            byte[][] writableMeasureDataArray = this.dataStore
                    .getWritableMeasureDataArray(dataHolder).clone();
            int entryCountLocal = entryCount;
            byte[] startKeyLocal = startKey;
            byte[] endKeyLocal = endKey;
            startKey = new byte[molapFactDataMergerInfo.getMdkeyLength()];
            endKey = new byte[molapFactDataMergerInfo.getMdkeyLength()];
            writerExecutorService.submit(new DataWriterThread(byteArrayValues,
                    writableMeasureDataArray, entryCountLocal, startKeyLocal,
                    endKeyLocal));
            // writeDataToFile(data,writableMeasureDataArray,entryCount,startKey,endKey);
            // set the entry count to zero
            processedDataCount += entryCount;
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "*******************************************Number Of records processed: "
                            + processedDataCount);
            this.entryCount = 0;
            resetKeyBlockHolder();
            initialisedataHolder();
            keyDataHolder.reset();
        }
    }

    private void writeDataToFile(byte[][] data, byte[][] dataHolderLocal,
            int entryCountLocal, byte[] startkeyLocal, byte[] endKeyLocal)
            throws MolapDataWriterException
    {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<IndexStorage>> submit = new ArrayList<Future<IndexStorage>>(
                numberOfColumns);
        byte[][][] columnsData = new byte[numberOfColumns][data.length][];
        for(int i = 0;i < data.length;i++)
        {
            byte[][] splitKey = columnarSplitter.splitKey(data[i]);
            for(int j = 0;j < splitKey.length;j++)
            {
                columnsData[j][i] = splitKey[j];
            }
        }
        for(int i = 0;i < numberOfColumns;i++)
        {
            submit.add(executorService.submit(new BlockSortThread(i,
                    columnsData[i])));
        }
        executorService.shutdown();
        try
        {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        }
        catch(InterruptedException e)
        {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,"error in executorService/awaitTermination ", e, e.getMessage());
            
        }
        IndexStorage[] blockStorage = new IndexStorage[numberOfColumns];
        try
        {
            for(int i = 0;i < blockStorage.length;i++)
            {
                blockStorage[i] = submit.get(i).get();
            }
        }
        catch(Exception e)
        {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,"error in  populating  blockstorage array ", e, e.getMessage());
            
        }
        synchronized(lock)
        {
            this.dataWriter.writeDataToFile(blockStorage, dataHolderLocal,
                    entryCountLocal, startkeyLocal, endKeyLocal);
        }
    }

    private final class DataWriterThread implements Callable<IndexStorage>
    {
        private byte[][] data;

        private byte[][] dataHolderLocal;

        private int entryCountLocal;

        private byte[] startkeyLocal;

        private byte[] endKeyLocal;

        private DataWriterThread(byte[][] data, byte[][] dataHolderLocal,
                int entryCountLocal, byte[] startKey, byte[] endKey)
        {
            this.data = data;
            this.entryCountLocal = entryCountLocal;
            this.startkeyLocal = startKey;
            this.endKeyLocal = endKey;
            this.dataHolderLocal = dataHolderLocal;
        }

        @Override
        public IndexStorage call() throws Exception
        {
            writeDataToFile(this.data, dataHolderLocal, entryCountLocal,
                    startkeyLocal, endKeyLocal);
            return null;
        }

    }

    private final class BlockSortThread implements Callable<IndexStorage>
    {
        private int index;

        private byte[][] data;

        private BlockSortThread(int index, byte[][] data)
        {
            this.index = index;
            this.data = data;
        }

        @Override
        public IndexStorage call() throws Exception
        {
            return new BlockIndexerStorageForInt(this.data,
                    aggKeyBlock[this.index], true,false,false);

        }

    }

    /**
     * below method will be used to finish the data handler
     * 
     * @throws MolapDataWriterException
     */
    public void finish() throws MolapDataWriterException
    {
        if(molapFactDataMergerInfo.isGroupByEnabled())
        {
            try
            {
                this.groupBy.initiateReading(
                        molapFactDataMergerInfo.getDestinationLocation(),
                        molapFactDataMergerInfo.getTableName());
                setWritingConfiguration(molapFactDataMergerInfo
                        .getMdkeyLength());
                // CHECKSTYLE:OFF Approval No:Approval-V3R8C00_018
                Object[] row = null;
                while(this.groupBy.hasNext())
                { // CHECKSTYLE:ON
                    row = this.groupBy.next();
                    addToStore(row);
                }
            }
            catch(MolapGroupByException e)
            {
                throw new MolapDataWriterException(
                        "Problem while doing the groupby", e);
            }
            finally
            {
                try
                {
                    this.groupBy.finish();
                }
                catch(MolapGroupByException e)
                {
                    LOGGER.error(
                            MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                            "Problem in group by finish");
                }
            }
        }
        // / still some data is present in stores if entryCount is more
        // than 0
        if(this.entryCount > 0)
        {
            byte[][] data = keyDataHolder.getByteArrayValues();
            byte[][][] columnsData = new byte[numberOfColumns][data.length][];
            for(int i = 0;i < data.length;i++)
            {
                byte[][] splitKey = columnarSplitter.splitKey(data[i]);
                for(int j = 0;j < splitKey.length;j++)
                {
                    columnsData[j][i] = splitKey[j];
                }
            }
            ExecutorService executorService = Executors.newFixedThreadPool(7);
            List<Future<IndexStorage>> submit = new ArrayList<Future<IndexStorage>>(
                    numberOfColumns);
            for(int i = 0;i < numberOfColumns;i++)
            {
                submit.add(executorService.submit(new BlockSortThread(i,
                        columnsData[i])));
            }
            executorService.shutdown();
            try
            {
                executorService.awaitTermination(1, TimeUnit.DAYS);
            }
            catch(InterruptedException e)
            {
                // TODO Auto-generated catch block
//                e.printStackTrace();
                LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,"error in  executorService.awaitTermination ", e, e.getMessage());
                
            }
            IndexStorage[] blockStorage = new IndexStorage[numberOfColumns];
            try
            {
                for(int i = 0;i < blockStorage.length;i++)
                {
                    blockStorage[i] = submit.get(i).get();
                }
            }
            catch(Exception e)
            {
                // TODO Auto-generated catch block
//                e.printStackTrace();
                LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,"error while populating blockStorage array ", e, e.getMessage());
                
            }

            writerExecutorService.shutdown();
            try
            {
                writerExecutorService.awaitTermination(1, TimeUnit.DAYS);
            }
            catch(InterruptedException e)
            {
                // TODO Auto-generated catch block
//                e.printStackTrace();
                LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,"error in  writerexecutorService/awaitTermination ", e, e.getMessage());
            }
            this.dataWriter.writeDataToFile(blockStorage,
                    this.dataStore.getWritableMeasureDataArray(dataHolder),
                    this.entryCount, this.startKey, this.endKey);

            processedDataCount += entryCount;
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "*******************************************Number Of records processed: "
                            + processedDataCount);
            this.dataWriter.writeleafMetaDataToFile();
        }
        else if(null != this.dataWriter
                && this.dataWriter.getLeafMetadataSize() > 0)
        {
            this.dataWriter.writeleafMetaDataToFile();
        }
    }

    /**
     * below method will be used to close the handler
     */
    public void closeHandler()
    {
        if(null != this.dataWriter)
        {
            // close all the open stream for both the files
            this.dataWriter.closeWriter();
            destLocation =this.dataWriter.getTempStoreLocation();
            int size = fileManager.size();
            FileData fileData = null;
            String storePathval = null;
            String inProgFileName = null;
            String changedFileName = null;
            File currentFile = null;
            File destFile = null;
            for(int i = 0;i < size;i++) 
            {
                fileData = (FileData)fileManager.get(i);

                if(null!=destLocation)
                {
                    currentFile = new File(destLocation);
                     destLocation=destLocation.substring(0,destLocation.indexOf(".inprogress"));
                     destFile = new File(destLocation);
                }
                else
                {
                	storePathval = fileData.getStorePath();
                    inProgFileName = fileData.getFileName();
                    changedFileName = inProgFileName.substring(0,
                            inProgFileName.lastIndexOf('.'));
                    currentFile = new File(storePathval + File.separator
                            + inProgFileName);
                  
                    destFile = new File(storePathval + File.separator
                            + changedFileName);
                }

                if(!currentFile.renameTo(destFile))
                {
                    LOGGER.info(
                            MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                            "Problem while renaming the file");
                }
                
                fileData.setName(changedFileName);
            }
        }
        if(null != groupBy)
        {
            try
            {
                this.groupBy.finish(); 
            }
            catch(MolapGroupByException exception)
            {
                LOGGER.info(
                        MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                        "Problem while closing the groupby file");
            }
        }
        this.keyBlockHolder = null;
        this.dataStore = null;
        this.dataWriter = null;
        this.groupBy = null;
    }

    /**
     * Below method will be to configure fact file writing configuration
     * 
     * @param instance
     * @throws MolapDataWriterException
     */
    private void setWritingConfiguration(int mdkeySize)
            throws MolapDataWriterException
    {
        String measureMetaDataFileLocation = molapFactDataMergerInfo
                .getDestinationLocation()
                + MolapCommonConstants.MEASURE_METADATA_FILE_NAME
                + molapFactDataMergerInfo.getTableName()
                + MolapCommonConstants.MEASUREMETADATA_FILE_EXT;
        // get the compression model
        // this will used max, min and decimal point value present in the
        // and the measure count to get the compression for each measure
        this.compressionModel = ValueCompressionUtil.getValueCompressionModel(
                measureMetaDataFileLocation,
                molapFactDataMergerInfo.getMeasureCount());
        this.uniqueValue = compressionModel.getUniqueValue();
        // get leaf node size
        this.leafNodeSize = Integer.parseInt(MolapProperties.getInstance()
                .getProperty(MolapCommonConstants.LEAFNODE_SIZE,
                        MolapCommonConstants.LEAFNODE_SIZE_DEFAULT_VAL));

        LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "************* Leaf Node Size: " + leafNodeSize);

        int dimSet = Integer
                .parseInt(MolapCommonConstants.DIMENSION_SPLIT_VALUE_IN_COLUMNAR_DEFAULTVALUE);
        this.columnarSplitter = new MultiDimKeyVarLengthEquiSplitGenerator(
                MolapUtil.getIncrementedCardinalityFullyFilled(molapFactDataMergerInfo
                        .getDimLens().clone()), (byte)dimSet);

        this.keyBlockHolder = new MolapKeyBlockHolder[this.columnarSplitter
                .getBlockKeySize().length];

        for(int i = 0;i < keyBlockHolder.length;i++)
        {
            this.keyBlockHolder[i] = new MolapKeyBlockHolder(leafNodeSize);
            this.keyBlockHolder[i].resetCounter();
        }

        numberOfColumns = keyBlockHolder.length;

        // create data store
        this.dataStore = StoreFactory.createDataStore(compressionModel);
        // agg type
        char[] type = compressionModel.getType();
        List<Integer> otherMeasureIndexList = new ArrayList<Integer>(
                MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        List<Integer> customMeasureIndexList = new ArrayList<Integer>(
                MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        for(int i = 0;i < type.length;i++)
        {
            if(type[i] != 'c')
            {
                otherMeasureIndexList.add(i);
            }
            else
            {
                customMeasureIndexList.add(i);
            }
        }
        otherMeasureIndex = new int[otherMeasureIndexList.size()];
        customMeasureIndex = new int[customMeasureIndexList.size()];
        for(int i = 0;i < otherMeasureIndex.length;i++)
        {
            otherMeasureIndex[i] = otherMeasureIndexList.get(i);
        }
        for(int i = 0;i < customMeasureIndex.length;i++)
        {
            customMeasureIndex[i] = customMeasureIndexList.get(i);
        }

        this.dataHolder = new MolapWriteDataHolder[molapFactDataMergerInfo
                .getMeasureCount()];
        for(int i = 0;i < otherMeasureIndex.length;i++)
        {
            this.dataHolder[otherMeasureIndex[i]] = new MolapWriteDataHolder();
            this.dataHolder[otherMeasureIndex[i]]
                    .initialiseDoubleValues(this.leafNodeSize);
        }
        for(int i = 0;i < customMeasureIndex.length;i++)
        {
            this.dataHolder[customMeasureIndex[i]] = new MolapWriteDataHolder();
            this.dataHolder[customMeasureIndex[i]]
                    .initialiseByteArrayValues(leafNodeSize);
        }

        keyDataHolder = new MolapWriteDataHolder();
        keyDataHolder.initialiseByteArrayValues(leafNodeSize);
        initialisedataHolder();
        // create data writer instance
        // this.dataWriter = new MolapFactDataWriterImpl(this.storeLocation,
        // this.measureCount, this.mdkeyLength, this.tableName,true,fileManager,
        // this.columnarSplitter.getBlockKeySize());

        this.dataWriter = getFactDataWriter(
                molapFactDataMergerInfo.getDestinationLocation(),
                molapFactDataMergerInfo.getMeasureCount(),
                molapFactDataMergerInfo.getMdkeyLength(),
                molapFactDataMergerInfo.getTableName(), true, fileManager,
                this.columnarSplitter.getBlockKeySize(),molapFactDataMergerInfo.isUpdateFact());
        // initialize the channel;
        this.dataWriter.initializeWriter();

    }

    private void resetKeyBlockHolder()
    {
        for(int i = 0;i < keyBlockHolder.length;i++)
        {
            this.keyBlockHolder[i].resetCounter();
        }
    }

    private void initialisedataHolder()
    {
        for(int i = 0;i < this.dataHolder.length;i++)
        {
            this.dataHolder[i].reset();
        }
    }

    private MolapFactDataWriter<?> getFactDataWriter(String storeLocation,
            int measureCount, int mdKeyLength, String tableName,
            boolean isNodeHolder, IFileManagerComposite fileManager,
            int[] keyBlockSize, boolean isUpdateFact)
    {

        if(isCompressedKeyBlock && isIntBasedIndexer && isAggKeyBlock)
        {
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "**************************Compressed key block and aggregated and int");
            return new MolapFactDataWriterImplForIntIndexAndAggBlockCompressed(
                    storeLocation, measureCount, mdKeyLength, tableName,
                    isNodeHolder, fileManager, keyBlockSize, aggKeyBlock,
                    molapFactDataMergerInfo.getDimLens(),isUpdateFact);
        }
        else if(isIntBasedIndexer && isAggKeyBlock)
        {
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "*************************************aggregated and int");
            return new MolapFactDataWriterImplForIntIndexAndAggBlock(
                    storeLocation, measureCount, mdKeyLength, tableName,
                    isNodeHolder, fileManager, keyBlockSize, aggKeyBlock,isUpdateFact);
        }
        else if(isIntBasedIndexer)
        {
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "************************************************int");
            return new MolapFactDataWriterImplForIntIndex(storeLocation,
                    measureCount, mdKeyLength, tableName, isNodeHolder,
                    fileManager, keyBlockSize,isUpdateFact);
        }
        else
        {
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "************************************************short");
            return new MolapFactDataWriterImpl(storeLocation, measureCount,
                    mdKeyLength, tableName, isNodeHolder, fileManager,
                    keyBlockSize,isUpdateFact);
        }
    }

	public void copyToHDFS(String loadPath) throws MolapDataWriterException  {
		
		   Path path = new Path(loadPath);
	       FileSystem fs;
		try {
			fs = path.getFileSystem(FileFactory.getConfiguration());
			 fs.copyFromLocalFile(true, true, new Path(destLocation), new Path(loadPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new MolapDataWriterException(e.getLocalizedMessage());
		}
	      
	}
}
