/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcfJtSMNYgnOYiEQwbS13nxM8hk/dmbY4B4u+tG
aRAl/vCV2kS4VcG1dPcw5GcA5fecggBzpS94g9dq/DH63LSiXniPgfrN1XJq2h0W8Kee16RB
fTKn1+5FtumiLr1ZUYwho/HcAFqDY5sPNGhWOm1iuwYkeDJl7U5O0wg1dze8+w==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
 */
package com.huawei.unibi.molap.store;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.csvreader.checkpoint.CheckPointHanlder;
import com.huawei.unibi.molap.metadata.SliceMetaData;
import com.huawei.unibi.molap.store.writer.exception.MolapDataWriterException;
import com.huawei.unibi.molap.threadbasedmerger.consumer.ConsumerThread;
import com.huawei.unibi.molap.threadbasedmerger.container.Container;
import com.huawei.unibi.molap.threadbasedmerger.producer.ProducerThread;
import com.huawei.unibi.molap.util.MolapDataProcessorLogEvent;
import com.huawei.unibi.molap.util.MolapDataProcessorUtil;
import com.huawei.unibi.molap.util.MolapProperties;
import com.huawei.unibi.molap.util.MolapUtil;
import com.huawei.unibi.molap.util.MolapUtilException;



/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap Data Processor 
 * Author K00900841
 * Created Date :21-May-2013 6:42:29 PM 
 * FileName : MolapDataWriterStep.java
 * Class Description : This class is responsible for writing the incoming data
 * to molap structure 
 * Version 1.0
 */
public class MolapDataWriterStep extends BaseStep implements StepInterface
{

    /**
     * LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(MolapDataWriterStep.class.getName());

    /**
     * molap data writer step data class
     */
    private MolapDataWriterStepData data;

    /**
     * molap data writer step meta
     */
    private MolapDataWriterStepMeta meta;

    /**
     * tabel name
     */
    private String tableName;

    /**
     * measure count
     */
    private int measureCount;

    /**
     * index of mdkey in incoming rows
     */
    private int mdKeyIndex;

    /**
     * temp file location
     */
    private String tempFileLocation;

    /**
     * number of consumer thread
     */
    private int numberOfConsumerThreads;

    /**
     * number of producer thread
     */
    private int numberOfProducerThreads;

    /**
     * buffer size
     */
    private int bufferSize;

    /**
     * mdkey lenght
     */
    private int mdkeyLength;
    
    /**
     * boolean to check whether producer and cosumer based sorting is enabled 
     */
    private boolean isPAndCSorting;
    
    /**
     * dataHandler
     */
    private MolapFactHandler dataHandler;
    
    /**
     * isEmptyLoad
     */
    private boolean isEmptyLoad;
    
    /**
     * type
     */
    private char[] type;
    
    private String[] aggregators;
    
    /**
     * 
     * MolapDataWriterStep Constructor to initialize the step
     * 
     * @param stepMeta
     * @param stepDataInterface
     * @param copyNr
     * @param transMeta
     * @param trans
     * 
     */
    public MolapDataWriterStep(StepMeta stepMeta,
            StepDataInterface stepDataInterface, int copyNr,
            TransMeta transMeta, Trans trans)
    {
        super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

    /**
     * Perform the equivalent of processing one row. Typically this means
     * reading a row from input (getRow()) and passing a row to output
     * (putRow)).
     * 
     * @param smi
     *            The steps metadata to work with
     * @param sdi
     *            The steps temporary working data to work with (database
     *            connections, result sets, caches, temporary variables, etc.)
     * @return false if no more rows can be processed or an error occurred.
     * @throws KettleException
     */
    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
            throws KettleException
    {

        try
        {
            meta = (MolapDataWriterStepMeta)smi;

            // molap data writer step data
            data = (MolapDataWriterStepData)sdi;
            // get row from previous step, blocks when needed!
            Object[] row = getRow();
            if(first)
            {

                first = false;
                // // molap data writer step meta
                if(null != getInputRowMeta())
                {
                    this.data.outputRowMeta = (RowMetaInterface)getInputRowMeta()
                            .clone();
                    this.meta.getFields(data.outputRowMeta, getStepname(),
                            null, null, this);
                }
                // set step configuration
                setStepConfiguration();
            }
            // if row is null then we will start final merging and writing the fact file 
            if(null == row)
            {
                if(!isEmptyLoad)
                {
                    try
                    {
                        startBTreeCreation();
                    }
                    finally
                    {
                        this.dataHandler.closeHandler();
                    }
                }
                putRow(data.outputRowMeta, new Object[0]);
                setOutputDone();
                return false;
            }
            // if row is null then there is no more incoming data

        }
        catch(Exception ex)
        {
            LOGGER.error(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, ex);
            throw new RuntimeException(ex);
        }
        putRow(data.outputRowMeta, new Object[0]);
        return true;
    }

    /**
     * below method will be used to write the fact file 
     * @throws KettleException
     * @throws MolapDataWriterException 
     */
    private void startBTreeCreation() throws MolapDataWriterException
    {
        try
        {
            if(isPAndCSorting)
            {
                startPAndCFinalMerge();
            }
            else
            {
                startSingleThreadFinalMerge();
            }
            this.dataHandler.finish();
        }
        catch(MolapDataWriterException e)
        {
            LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, e);
            throw e;
        }
        finally
        {
            this.dataHandler.closeHandler();
        }

    }


    /**
     * This method will be used to get and update the step properties which will
     * required to run this step
     * 
     * @param totalRowLength
     *            total number of records in reacords
     * @param mdkeyLength
     *            lenght of mdkey
     * @throws KettleException
     * @throws MolapUtilException 
     * 
     */
    private void setStepConfiguration() throws KettleException
    {
        MolapProperties instance = MolapProperties.getInstance();
        // get the table name
        this.tableName = meta.getTabelName();
        String inputStoreLocation = meta.getSchemaName() + File.separator
                + meta.getCubeName();
        // get the base store location
        String tempLocationKey = meta.getSchemaName()+'_'+meta.getCubeName();
        String baseStorelocation = instance.getProperty(
                tempLocationKey,
                MolapCommonConstants.STORE_LOCATION_DEFAULT_VAL)
                + File.separator + inputStoreLocation;
        int restructFolderNumber = meta.getCurrentRestructNumber()/*MolapUtil
                .checkAndReturnNextRestructFolderNumber(baseStorelocation,"RS_")*/;
        if(restructFolderNumber<0)
        {
            isEmptyLoad=true;
            return;
        }
        
        baseStorelocation = baseStorelocation + File.separator
                + MolapCommonConstants.RESTRUCTRE_FOLDER + restructFolderNumber
                + File.separator + this.tableName;
        
        // get the current folder sequence
        int counter = MolapUtil
                .checkAndReturnCurrentLoadFolderNumber(baseStorelocation);
        if(counter<0)
        {
            isEmptyLoad=true;
            return;
        }
        File file = new File(baseStorelocation);
        // get the store location
        String storeLocation = file.getAbsolutePath() + File.separator
                + MolapCommonConstants.LOAD_FOLDER + counter+MolapCommonConstants.FILE_INPROGRESS_STATUS;
        
        SliceMetaData sliceMetaData = null;
        try
        {
            sliceMetaData = MolapUtil.readSliceMetadata(new File(
                    baseStorelocation), restructFolderNumber);
        }
        catch(MolapUtilException e1)
        {
            throw new KettleException(
                    "Problem while reading the slice metadata", e1);
        }
        this.mdkeyLength = meta.getMdkeyLength();
        String[] measures = sliceMetaData.getMeasures();
        this.aggregators = sliceMetaData.getMeasuresAggregator();
        this.measureCount = measures.length;
        int highCardIndex = measureCount;
        // incremented the index of mdkey by 1 so that the earlier one is high card index.
        this.mdKeyIndex = highCardIndex + 1;
        bufferSize=MolapCommonConstants.MOLAP_PREFETCH_BUFFERSIZE;
        
        try
        {
            numberOfConsumerThreads = Integer.parseInt(instance.getProperty(
                    "molap.sort.number.of.cosumer.thread", "2"));
        }
        catch (NumberFormatException e)
        {
            numberOfConsumerThreads=2;
        }
        
        try
        {
            numberOfProducerThreads = Integer.parseInt(instance.getProperty(
                    "molap.sort.number.of.producer.thread", "4"));
        }
        catch (NumberFormatException e)
        {
            numberOfProducerThreads=4;
        }
        updateSortTempFileLocation(instance, meta.getSchemaName(),
                meta.getCubeName());
        isPAndCSorting = Boolean
                .parseBoolean(instance
                        .getProperty(
                                MolapCommonConstants.IS_PRODUCERCONSUMER_BASED_SORTING,
                                MolapCommonConstants.PRODUCERCONSUMER_BASED_SORTING_ENABLED_DEFAULTVALUE));
        String [] aggType = sliceMetaData.getMeasuresAggregator();

        type = new char[measureCount];
        Arrays.fill(type, 'c'); 
        type[measureCount - 1] = 'n';
//        boolean isGroupByInSort = Boolean
//                .parseBoolean(MolapProperties
//                        .getInstance()
//                        .getProperty(
//                                MolapCommonConstants.MOLAP_IS_GROUPBY_IN_SORT,
//                                MolapCommonConstants.MOLAP_IS_GROUPBY_IN_SORT_DEFAULTVALUE));
//        
////        isGroupByInSort = updateStepConfForGroupByInSort(aggType, isGroupByInSort);
        boolean isByteArrayInMeasure=true;
        
//        if(meta.isGroupByEnabled()
//                && (isGroupByInSort || meta.isUpdateMemberRequest()))
//        {
//            aggType = MolapDataProcessorUtil.getUpdatedAggregator(meta
//                    .getAggregators());
//            for(int i = 0;i < aggType.length;i++)
//            {
//                if(aggType[i].equals(MolapCommonConstants.DISTINCT_COUNT)
//                        || aggType[i].equals(MolapCommonConstants.AVERAGE))
//                {
//                    type[i] = 'c';
//                    isByteArrayInMeasure = true;
//                }
//            }
//        }
//        
//        isByteArrayInMeasure = updateStepConfForByteArrInMeasure(aggType, isByteArrayInMeasure);
        
        String levelCardinalityFilePath = storeLocation + File.separator + 
				MolapCommonConstants.LEVEL_METADATA_FILE + meta.getTabelName() + ".metadata";
        int[] dimLens;
        try
        {
        dimLens = MolapUtil.getCardinalityFromLevelMetadataFile(levelCardinalityFilePath);
        }
        catch(MolapUtilException e1)
        {
        	throw new KettleException(
        			"Problem while reading the cardinality from level metadata file", e1);
        }
        
        updateFactHandler(isByteArrayInMeasure, aggType, dimLens, storeLocation);
        try
        {
            dataHandler.initialise();
        }
        catch(MolapDataWriterException e)
        {
            throw new KettleException(e);
        }
    }

   /* private boolean updateStepConfForByteArrInMeasure(String[] aggType, boolean isByteArrayInMeasure)
    {
        if(meta.isGroupByEnabled() && meta.isUpdateMemberRequest())
        {
            for(int i = 0;i < aggType.length;i++)
            {
                if(aggType[i].equals(MolapCommonConstants.CUSTOM))
                {
                    type[i] = 'c';
                    isByteArrayInMeasure = true;
                }
            }
        }
        return isByteArrayInMeasure;
    }*/

   /* private boolean updateStepConfForGroupByInSort(String[] aggType, boolean isGroupByInSort)
    {
        if(meta.isGroupByEnabled()
                && (isGroupByInSort || meta.isUpdateMemberRequest()))
        {
            for(int i = 0;i < aggType.length;i++)
            {
                if(aggType[i].equals(MolapCommonConstants.CUSTOM))
                {
                    isGroupByInSort = false;
                    break;
                }
            }
        }
        return isGroupByInSort;
    }*/
    
    /**
     * Initialize and do work where other steps need to wait for...
     * 
     * @param smi
     *            The metadata to work with
     * @param sdi
     *            The data to initialize
     * @return step initialize or not
     */
    public boolean init(StepMetaInterface smi, StepDataInterface sdi)
    {
        meta = (MolapDataWriterStepMeta)smi;
        data = (MolapDataWriterStepData)sdi;
        return super.init(smi, sdi);
    }

    /**
     * Dispose of this step: close files, empty logs, etc.
     * 
     * @param smi
     *            The metadata to work with
     * @param sdi
     *            The data to dispose of
     */
    public void dispose(StepMetaInterface smi, StepDataInterface sdi)
    {
        meta = (MolapDataWriterStepMeta)smi;
        data = (MolapDataWriterStepData)sdi;
        super.dispose(smi, sdi);
        try
        {
            // if sort by step is not null then delete temp files and folder
            if(!CheckPointHanlder.IS_CHECK_POINT_NEEDED || meta.isGroupByEnabled())
            {
                if(null!=tempFileLocation)
                {
                    MolapUtil.deleteFoldersAndFiles(tempFileLocation);
                }
            }
        }
        catch(MolapUtilException e)
        {
            LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, e,"Problem while deleting the temp files");
        }
        this.meta = null;
        this.data = null;
    }

    /**
     * This will be used to get the sort temo location
     * 
     * @param storeLocation
     * @param instance
     */
    private void updateSortTempFileLocation(MolapProperties molapProperties,
            String schemaName, String cubeName)
    {
        // get the base location
        String tempLocationKey = meta.getSchemaName()+'_'+meta.getCubeName();
        String baseLocation = molapProperties.getProperty(
                tempLocationKey,
                MolapCommonConstants.STORE_LOCATION_DEFAULT_VAL);
        // get the temp file location
        this.tempFileLocation = baseLocation + File.separator + schemaName
                + File.separator + cubeName + File.separator
                + MolapCommonConstants.SORT_TEMP_FILE_LOCATION + File.separator
                + this.tableName;
        LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "temp file location" + this.tempFileLocation);
    }

    /**
     * below method will be used for single thread based merging 
     * @throws MolapDataWriterException
     */
    private void startSingleThreadFinalMerge() throws MolapDataWriterException
    {
        SingleThreadFinalMerger finalMergerThread = new SingleThreadFinalMerger(
                tempFileLocation, tableName, mdkeyLength, measureCount,
                mdKeyIndex, meta.isFactMdKeyInInputRow(),
                meta.getFactMdkeyLength(), type, this.aggregators,this.meta.gethighCardCount());
        finalMergerThread.startFinalMerge();
        int recordCounter=0;
      //CHECKSTYLE:OFF    Approval No:Approval-V3R8C00_018
        while(finalMergerThread.hasNext())
        {
            dataHandler.addDataToStore(finalMergerThread.next());
            recordCounter++;
        }
        LOGGER.info(
                MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "************************************************ Total number of records processed"
                        + recordCounter);
        finalMergerThread.clear();
      //CHECKSTYLE:ON
    }
    
    /**
     * below method will be used to producer consumer based sorting 
     */
    private void startPAndCFinalMerge()
    {
        ExecutorService executorService = null;
        File file = new File(tempFileLocation);
        File[] tempFiles = file.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.getName().startsWith(tableName);
            }
        });

        if(null == tempFiles || tempFiles.length < 1)
        {
            return;
        }
        int fileBufferSize = 64 * MolapCommonConstants.BYTE_TO_KB_CONVERSION_FACTOR;

        int numberOfFilesPerThreads = tempFiles.length
                / (numberOfConsumerThreads * numberOfProducerThreads);

        int leftOver = tempFiles.length
                % (numberOfConsumerThreads * numberOfProducerThreads);

        if(numberOfFilesPerThreads == 0 && leftOver > 0)
        {
            numberOfConsumerThreads = 1;
            numberOfProducerThreads = 1;
        }

        int totalNumberOfThreads = (numberOfConsumerThreads * numberOfProducerThreads)
                + numberOfConsumerThreads + 1;
        
        LOGGER.info(
                MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "******************************************************Total Number of Threads: "
                        + totalNumberOfThreads);

        executorService = Executors.newFixedThreadPool(totalNumberOfThreads);

        File[][] filesPerEachThread = new File[numberOfConsumerThreads
                * numberOfProducerThreads][];

        int counter = 0;
        if(numberOfFilesPerThreads > 0)
        {
            for(int i = 0;i < filesPerEachThread.length;i++)
            {
                filesPerEachThread[i] = new File[numberOfFilesPerThreads];
                System.arraycopy(tempFiles, counter, filesPerEachThread[i], 0,
                        numberOfFilesPerThreads);
                counter += numberOfFilesPerThreads;
            }
        }
        if(leftOver > 0 && numberOfFilesPerThreads > 0)
        {
            int i = 0;
            while(true)
            {
               //CHECKSTYLE:OFF    Approval No:Approval-019
                File[] temp = new File[filesPerEachThread[i].length + 1];
                System.arraycopy(filesPerEachThread[i], 0, temp, 0,
                        filesPerEachThread[i].length);
                temp[temp.length - 1] = tempFiles[counter++];
                filesPerEachThread[i] = temp;
                if(counter >= tempFiles.length)
                {
                    break;
                }

                if(counter < tempFiles.length && i >= filesPerEachThread.length)
                {
                    i = 0;
                }
                i++;
              //CHECKSTYLE:ON
            }
        }
        if(leftOver > 0 && numberOfFilesPerThreads <= 0)
        {
            filesPerEachThread = new File[numberOfConsumerThreads
                    * numberOfProducerThreads][];

            filesPerEachThread[0] = new File[leftOver];
            System.arraycopy(tempFiles, 0, filesPerEachThread[0], 0,
                    tempFiles.length);
        }

        for(int i = 0;i < filesPerEachThread.length;i++)
        {
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "********************************************Number of Files for Producer: "
                            + filesPerEachThread[i].length);
        }

        startPAndC(executorService, fileBufferSize, filesPerEachThread);
    }

    private void startPAndC(ExecutorService executorService,
            int fileBufferSize, File[][] filesPerEachThread)
    {
        List<Container> consumerContainerList = new ArrayList<Container>(numberOfConsumerThreads);
        for(int i = 0;i < numberOfConsumerThreads;i++)
        {
            Container container = new Container();
            consumerContainerList.add(container);
        }
        List<List<Container>> producerContainersList = new ArrayList<List<Container>>(numberOfConsumerThreads);
        for(int i = 0;i < numberOfConsumerThreads;i++)
        {
            List<Container> list = new ArrayList<Container>(numberOfProducerThreads);
            for(int j = 0;j < numberOfProducerThreads;j++)
            {
                Container container = new Container();
                list.add(container);
            }
            producerContainersList.add(list);
        }
        int index = 0;
        for(int i = 0;i < numberOfConsumerThreads;i++)
        {
            ConsumerThread c = new ConsumerThread(
                    producerContainersList.get(i), bufferSize,
                    consumerContainerList.get(i), i, this.mdKeyIndex);
            List<Container> list1 = producerContainersList.get(i);
            for(int j = 0;j < numberOfProducerThreads;j++)
            {
                LOGGER.info(
                        MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                        "*******************************************Submitted Producer Thread "+ j);
                executorService.submit(new ProducerThread(
                        filesPerEachThread[index], fileBufferSize, bufferSize,
                        this.measureCount, this.mdkeyLength, list1.get(j),
                        index, meta.isFactMdKeyInInputRow(), meta
                                .getFactMdkeyLength(),type));
                index++;
            }

            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "*******************************************Submitted consumer thread");
            executorService.submit(c);
        }
        ProducerCosumerFinalMergerThread finalMergerThread = new ProducerCosumerFinalMergerThread(
                dataHandler, this.measureCount, this.mdKeyIndex,
                consumerContainerList);
        executorService.submit(finalMergerThread);
        LOGGER.info(
                MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "********************************************** Submitted all the task to executer");
        executorService.shutdown();
        try
        {
            executorService.awaitTermination(3, TimeUnit.HOURS);
        }
        catch(InterruptedException e)
        {
            LOGGER.error(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    e);
        }
    }
    
    private void updateFactHandler(boolean isByteArrayInMeasure, String[] aggType, int[] dimLens, String storeLocation)
    {
        boolean isColumnar=Boolean.parseBoolean(MolapCommonConstants.IS_COLUMNAR_STORAGE_DEFAULTVALUE);
        
        if(isColumnar)
        {
            dataHandler = new MolapFactDataHandlerColumnar(meta.getSchemaName(),
                    meta.getCubeName(), this.tableName, meta.isGroupByEnabled(),
                    measureCount, mdkeyLength, mdKeyIndex, aggType,
                    meta.getAggregatorClass(), storeLocation,
                    MolapDataProcessorUtil.getDimLens(meta.getFactDimLensString()),
                    isByteArrayInMeasure, meta.isUpdateMemberRequest(),dimLens,meta.getFactLevels(),meta.getAggregateLevels(), true, meta.getCurrentRestructNumber(),this.meta.gethighCardCount());
        }
        else
        {
              dataHandler = new MolapFactDataHandler(meta.getSchemaName(),
                meta.getCubeName(), this.tableName, meta.isGroupByEnabled(),
                measureCount, mdkeyLength, mdKeyIndex, aggType,
                meta.getAggregatorClass(), storeLocation,
                MolapDataProcessorUtil.getDimLens(meta.getFactDimLensString()),
                isByteArrayInMeasure, meta.isUpdateMemberRequest(),dimLens, meta.getFactLevels(),meta.getAggregateLevels(), true, meta.getCurrentRestructNumber());
        }
        
    }
}
