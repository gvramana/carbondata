/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2014
 * =====================================
 *
 */
package com.huawei.unibi.molap.factreader.step;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eigenbase.xom.Parser;
import org.eigenbase.xom.XOMUtil;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;


//import com.huawei.datasight.molap.core.load.LoadMetadataDetails;
import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.engine.aggregator.CustomMolapAggregateExpression;
//import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFile;
//import com.huawei.unibi.molap.datastorage.store.impl.FileFactory.FileType;
import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;
import com.huawei.unibi.molap.engine.aggregator.dimension.DimensionAggregatorInfo;
import com.huawei.unibi.molap.engine.cache.QueryExecutorUtil;
import com.huawei.unibi.molap.engine.datastorage.InMemoryCube;
import com.huawei.unibi.molap.engine.datastorage.InMemoryCubeStore;
import com.huawei.unibi.molap.engine.executer.SliceExecuter;
import com.huawei.unibi.molap.engine.executer.exception.QueryExecutionException;
import com.huawei.unibi.molap.engine.executer.impl.ColumnarParallelSliceExecutor;
import com.huawei.unibi.molap.engine.executer.impl.RestructureHolder;
import com.huawei.unibi.molap.engine.executer.impl.RestructureUtil;
import com.huawei.unibi.molap.engine.executer.pagination.impl.QueryResult;
import com.huawei.unibi.molap.engine.executer.pagination.impl.QueryResult.QueryResultIterator;
import com.huawei.unibi.molap.engine.schema.metadata.SliceExecutionInfo;
import com.huawei.unibi.molap.engine.util.QueryExecutorUtility;
import com.huawei.unibi.molap.engine.wrappers.ByteArrayWrapper;
import com.huawei.unibi.molap.iterator.MolapIterator;
import com.huawei.unibi.molap.keygenerator.KeyGenException;
import com.huawei.unibi.molap.keygenerator.KeyGenerator;
import com.huawei.unibi.molap.keygenerator.columnar.impl.MultiDimKeyVarLengthEquiSplitGenerator;
import com.huawei.unibi.molap.keygenerator.columnar.impl.MultiDimKeyVarLengthVariableSplitGenerator;
import com.huawei.unibi.molap.keygenerator.factory.KeyGeneratorFactory;
import com.huawei.unibi.molap.metadata.MolapMetadata;
import com.huawei.unibi.molap.metadata.MolapMetadata.Cube;
import com.huawei.unibi.molap.metadata.MolapMetadata.Dimension;
import com.huawei.unibi.molap.metadata.MolapMetadata.Measure;
import com.huawei.unibi.molap.metadata.SliceMetaData;
import com.huawei.unibi.molap.olap.MolapDef;
import com.huawei.unibi.molap.olap.MolapDef.AggLevel;
import com.huawei.unibi.molap.olap.MolapDef.AggMeasure;
import com.huawei.unibi.molap.olap.MolapDef.Schema;
import com.huawei.unibi.molap.util.MolapDataProcessorLogEvent;
import com.huawei.unibi.molap.util.MolapProperties;
import com.huawei.unibi.molap.util.MolapSchemaParser;
import com.huawei.unibi.molap.util.RemoveDictionaryUtil;
//import com.huawei.unibi.molap.util.MolapSliceAndFiles;
import com.huawei.unibi.molap.util.MolapUtil;

/**
 * Project Name NSE V3R8C10 
 * Module Name : MOLAP Data Processor
 * Author :k00900841 
 * Created Date:10-Aug-2014
 * FileName : MolapFactReaderStep.java
 * Class Description : Step class to read the fact table data
 * Class Version 1.0
 */
public class MolapFactReaderStep extends BaseStep implements StepInterface
{

    /**
     * LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(MolapFactReaderStep.class.getName());

    /**
     * BYTE ENCODING
     */
    private static final String BYTE_ENCODING = "ISO-8859-1";
    
    /**
     * lock
     */
    private static final Object LOCK = new Object();

    /**
     * meta
     */
    private MolapFactReaderMeta meta;

    /**
     * data
     */
    private MolapFactReaderData data;

    /**
     * writeCounter
     */
    private long writeCounter;

    /**
     * logCounter
     */
    private int logCounter;
    
    /**
     * executorService
     */
    private ExecutorService executorService;
    
    /**
     * threadStatusObserver
     */
    private ThreadStatusObserver threadStatusObserver;
    
    /**
     * readCopies
     */
    private int readCopies;
    
    /**
     * aggregate dimensions array
     */
    private AggLevel[] aggLevels;
    
    /**
     * aggregate measures array
     */
    private AggMeasure[] aggMeasures;
    
    private Dimension[] currentQueryDims;
    
    private Measure[] currentQueryMeasures;
    
    private String[] aggTypes;
    
    List<DimensionAggregatorInfo> dimAggInfo;
    
    /**
     * this is used to store the mapping of high card dims along with agg types.
     */
    private boolean[] isHighCardinality;
    
    /**
     * 
     * MolapFactReaderStep Constructor to initialize the step
     * 
     * @param stepMeta
     * @param stepDataInterface
     * @param copyNr
     * @param transMeta
     * @param trans
     * 
     */
    public MolapFactReaderStep(StepMeta stepMeta,
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
            if(first)
            {
                LOGGER.info(
                        MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                        "File Based fact reader will be used For "+ meta.getTableName());
                meta = (MolapFactReaderMeta)smi;
                data = (MolapFactReaderData)sdi;
                data.outputRowMeta = new RowMeta();
                first = false;
                meta.initialize();
                this.logCounter = Integer
                        .parseInt(MolapCommonConstants.DATA_LOAD_LOG_COUNTER_DEFAULT_COUNTER);
                setStepOutputInterface(meta.getAggType());
                try
                {
                    readCopies = Integer.parseInt(MolapProperties.getInstance()
                            .getProperty(MolapCommonConstants.NUM_CORES_LOADING,
                                    MolapCommonConstants.DEFAULT_NUMBER_CORES));
                }
                catch (NumberFormatException e)
                {
                    LOGGER.error(
                            MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                            "Invalid Value for: "
                                    + MolapCommonConstants.NUM_CORES_LOADING
                                    + "Default Value: "
                                    + MolapCommonConstants.DEFAULT_NUMBER_CORES
                                    + " will be used");
                    readCopies= Integer.parseInt(MolapCommonConstants.DEFAULT_NUMBER_CORES);
                }
                this.executorService = Executors.newFixedThreadPool(readCopies);
                this.threadStatusObserver= new ThreadStatusObserver();
                initAggTable();
            }
            Callable<Void> c= new DataProcessor();
            this.executorService.submit(c);
            this.executorService.shutdown();
            this.executorService.awaitTermination(1, TimeUnit.DAYS);
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "Record Procerssed For table: " + meta.getTableName());
            String logMessage = "Summary: Molap Fact Reader Step: Read: "
                    + 0 + ": Write: " + writeCounter;
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    logMessage);
        }
        catch(Exception ex)
        {
            LOGGER.error(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, ex);
            throw new RuntimeException(ex);
        }
        setOutputDone();
        return false;
    }
    
    private Schema parseStringToSchema(String schema) throws Exception 
    {
        Parser xmlParser = XOMUtil.createDefaultParser();
        ByteArrayInputStream baoi = new ByteArrayInputStream(schema.getBytes(Charset.defaultCharset()));
        return new MolapDef.Schema(xmlParser.parse(baoi));
    }
    
    private void initAggTable() throws Exception
    {
    	MolapDef.Schema schema = parseStringToSchema(meta.getSchema());
    	MolapDef.Cube cube = MolapSchemaParser.getMondrianCube(schema, meta.getCubeName());
    	MolapDef.Table table = (MolapDef.Table)cube.fact;
    	MolapDef.AggTable[] aggTables = table.aggTables;
        int numberOfAggregates = aggTables.length;
        for(int i=0; i<numberOfAggregates; i++)
        {
        	String aggTableName = ((MolapDef.AggName)aggTables[i]).getNameAttribute();
        	if(aggTableName.equals(meta.getAggregateTableName()))
        	{
        		aggLevels = aggTables[i].getAggLevels();
        		aggMeasures = aggTables[i].getAggMeasures();
        		break;
        	}
        }
    }
    
    /**
     * Thread class to which will be used to read the fact data 
     * @author K00900841
     *
     */
    private final class DataProcessor implements Callable<Void>
    {
        public Void call() throws Exception
        {
            try
            {
                Object[] next = null;
                synchronized(LOCK)
                {
                    InMemoryCubeStore inMemoryCubeInstance = InMemoryCubeStore
                            .getInstance();
                    String cubeUniqueName = meta.getSchemaName() + "_"
                            + meta.getCubeName();
                    Cube cube = MolapMetadata.getInstance().getCube(
                            cubeUniqueName);
                    if(null == cube)
                    {
                        LOGGER.info(
                                MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                                "cube not loaded: "
                                        + meta.getTableName());
                        return null;
                    }
                    List<InMemoryCube> activeSlices = inMemoryCubeInstance
                            .getActiveSlices(cubeUniqueName);
                    List<SliceExecutionInfo> infos = new ArrayList<SliceExecutionInfo>(
                            MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
                    String loadName = getLoadName();
                    dimAggInfo = new ArrayList<DimensionAggregatorInfo>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
                    initQueryDims(cube);
                    initQueryMsrs(cube);
                    aggTypes = getAggTypes();
                    SliceMetaData sliceMataData = null;
                    SliceExecutionInfo latestSliceInfo = null;
                    InMemoryCube requiredSlice = null;
                    // for each slice we need create a slice info which will be
                    // used to
                    // execute the query
                    int currentSliceIndex = -1;
                    for(InMemoryCube slice : activeSlices)
                    {
                        // get the slice metadata for each slice
                        currentSliceIndex++;
                        if(null != slice.getDataCache(cube.getFactTableName()))
                        {
                            sliceMataData = slice.getRsStore()
                                    .getSliceMetaCache(cube.getFactTableName());
                        }
                        else
                        {
                            continue;
                        }
                        if(loadName.equals(slice.getLoadName()))
                        {
                            requiredSlice = slice;
                            break;
                        }
                    }
                    if(null == requiredSlice)
                    {
                        LOGGER.info(
                                MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                                "No sctive slices for the request load folder : "
                                        + loadName);
                        return null;
                    }
                    latestSliceInfo = getSliceExecutionInfo(cube, activeSlices, requiredSlice,
                            sliceMataData, currentSliceIndex);
                    if(null == latestSliceInfo)
                    {
                        LOGGER.info(
                                MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                                "No slice execution info object created: "
                                        + meta.getTableName());
                        return null;
                    }
                    infos.add(latestSliceInfo);
                    SliceExecuter executor = new ColumnarParallelSliceExecutor();
                    MolapIterator<QueryResult> resultIterator = executor
                            .executeSlices(infos,null);
                    QueryResult queryResult = resultIterator.next();
                    QueryResultIterator queryResultIterator = queryResult
                            .iterator();
                    ByteArrayWrapper key = null;
                    MeasureAggregator[] value = null;
                    int count = 0;
                    int j = 0;
                    while(queryResultIterator.hasNext())
                    {
                        key = queryResultIterator.getKey();
                        value = queryResultIterator.getValue();
                        count++;
                        // length incremented by 2 (1 for mdkey and 1 for count)
                        next = new Object[4];
                        next[j++] = value;
                        next[j++] = Double.valueOf(count);
                        // converting high card dims into single byte buffer [].
                        byte[] highCardByteArr = null;
                                
                        if(null != key.getDirectSurrogateKeyList())     
                        {
                            highCardByteArr = RemoveDictionaryUtil.convertListByteArrToSingleArr(key.getDirectSurrogateKeyList());
                        }
                        next[j++] = highCardByteArr;
                        next[j] = key.getMaskedKey();
                        putRow(data.outputRowMeta, next);
                        writeCounter++;
                        if(writeCounter % logCounter == 0)
                        {
                            LOGGER.info(
                                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                                    "Record Procerssed For table: "
                                            + meta.getTableName());
                            String logMessage = "Molap Fact Reader Step: Read: "
                                    + 0 + ": Write: " + writeCounter;
                            LOGGER.info(
                                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                                    logMessage);
                        }
                        j = 0;
                    }
                }
            }
            catch(Exception e)
            {
                LOGGER.error(
                        MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                        e, "");
                threadStatusObserver.notifyFailed(e);
                throw e;
            }
            return null;
        }
    }
    
    /**
     * 
     * @return
     * 
     */
    private String getLoadName()
    {
        String location = meta.getFactStoreLocation();
        String loadName = location.substring(location
                .lastIndexOf(MolapCommonConstants.LOAD_FOLDER));
        return loadName;
    }
    
    private Dimension[] getSelectedQueryDimensions(String[] dims,
            Dimension[] currentDimTables, String tableName)
    {
        List<Dimension> selectedQueryDimensions = new ArrayList<Dimension>(
                MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        for(int i = 0;i < dims.length;i++)
        {
            for(int j = 0;j < currentDimTables.length;j++)
            {
                if(dims[i].equals(tableName + '_'
                        + currentDimTables[j].getName()))
                {
                    selectedQueryDimensions.add(currentDimTables[j]);
                    break;
                }
            }
        }
        return selectedQueryDimensions
                .toArray(new Dimension[selectedQueryDimensions.size()]);
    }
    
    private SliceExecutionInfo getSliceExecutionInfo(Cube cube, List<InMemoryCube> activeSlices, InMemoryCube slice,
            SliceMetaData sliceMataData, int currentSliceIndex) throws KeyGenException, QueryExecutionException
    {
		RestructureHolder holder = new RestructureHolder();
		KeyGenerator globalKeyGenerator = null;
        Dimension[] queryDimensions = getSelectedQueryDimensions(
                sliceMataData.getDimensions(), currentQueryDims,
                cube.getFactTableName());
		globalKeyGenerator = KeyGeneratorFactory.getKeyGenerator(meta
                .getGlobalDimLens());
		if (!globalKeyGenerator.equals(slice
				.getKeyGenerator(cube.getFactTableName()))) {
			holder.updateRequired = true;
		}

		holder.metaData = sliceMataData;
		int[] measureOrdinal = new int[currentQueryMeasures.length];
		boolean[] msrExists = new boolean[currentQueryMeasures.length];
		double[] newMsrsDftVal = new double[currentQueryMeasures.length];
		RestructureUtil.updateMeasureInfo(sliceMataData, currentQueryMeasures,
				measureOrdinal, msrExists, newMsrsDftVal);
    	SliceExecutionInfo info = new SliceExecutionInfo();
    	info.setIsMeasureExistis(msrExists);
    	info.setMsrDefaultValue(newMsrsDftVal);
        double[] sliceUniqueValues = null;
        boolean isCustomMeasure = false;
        sliceUniqueValues = slice.getDataCache(cube.getFactTableName()).getUniqueValue();
        char[] type = slice.getDataCache(cube.getFactTableName()).getType();
        for(int i = 0;i < type.length;i++)
        {
            if(type[i] == 'c')
            {
                isCustomMeasure = true;
            }
        }
        info.setCustomMeasure(isCustomMeasure);
        info.setTableName(cube.getFactTableName());
        info.setKeyGenerator(slice.getKeyGenerator(cube.getFactTableName()));
        holder.setKeyGenerator(slice.getKeyGenerator(cube.getFactTableName()));
        info.setQueryDimensions(queryDimensions);
        info.setMeasureOrdinal(measureOrdinal);
        info.setCubeName(meta.getCubeName());
        info.setSchemaName(meta.getSchemaName());
        info.setQueryId(System.currentTimeMillis() + "");
        info.setDetailQuery(false);
        int[] maskByteRanges = QueryExecutorUtil.getMaskedByte(queryDimensions,globalKeyGenerator);
        info.setMaskedKeyByteSize(maskByteRanges.length);
        int[] maskedBytesLocal = new int[slice.getKeyGenerator(cube.getFactTableName()).getKeySizeInBytes()];
        QueryExecutorUtil.updateMaskedKeyRanges(maskedBytesLocal, maskByteRanges);
        holder.maskedByteRanges = maskedBytesLocal;
        // creating a masked key
        int[] maskedBytes = new int[globalKeyGenerator.getKeySizeInBytes()];
        // update the masked byte
        QueryExecutorUtil.updateMaskedKeyRanges(maskedBytes, maskByteRanges);
        info.setMaskedBytePositions(maskedBytes);
        
     // get the mask byte range based on dimension present in the query
        maskByteRanges = QueryExecutorUtil.getMaskedByte(currentQueryDims,
                globalKeyGenerator);

        // update the masked byte
        QueryExecutorUtil.updateMaskedKeyRanges(maskedBytes, maskByteRanges);
        info.setActalMaskedByteRanges(maskByteRanges);
        info.setMaskedBytePositions(maskedBytes);
        info.setActualMaskedKeyByteSize(maskByteRanges.length);
        info.setActualMaskedKeyByteSize(maskByteRanges.length);
        List<Dimension> dimList = cube.getDimensions(cube.getFactTableName());
        Dimension[] dimemsions = null;
        if(dimList != null)
        {
            dimemsions = dimList.toArray(new Dimension[dimList.size()]);
        }
        info.setActualMaxKeyBasedOnDimensions(QueryExecutorUtil.getMaxKeyBasedOnDimensions(currentQueryDims,
                globalKeyGenerator, dimemsions));
        info.setActualKeyGenerator(globalKeyGenerator);
        info.setRestructureHolder(holder);
        info.setSlice(slice);
        info.setSlices(activeSlices);
        info.setAvgIndexes(new ArrayList<Integer>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE));
        info.setCountMsrsIndex(-1);
        info.setDimensionSortOrder(new byte[0]);
        info.setUniqueValues(sliceUniqueValues);
        info.setOriginalDims(queryDimensions);
        int[][] maskedByteRangeForSorting = QueryExecutorUtility.getMaskedByteRangeForSorting(queryDimensions,
                globalKeyGenerator, maskByteRanges);
        info.setMaskedByteRangeForSorting(maskedByteRangeForSorting);
        info.setDimensionMaskKeys(QueryExecutorUtility.getMaksedKeyForSorting(queryDimensions,
                globalKeyGenerator, maskedByteRangeForSorting, maskByteRanges));
        info.setColumnarSplitter(new MultiDimKeyVarLengthVariableSplitGenerator(MolapUtil.getDimensionBitLength(slice.getHybridStoreModel().getHybridCardinality(),slice.getHybridStoreModel().getDimensionPartitioner()),slice.getHybridStoreModel().getColumnSplit()));
        if(slice.getHybridStoreModel().isHybridStore())
        {
        	 info.setQueryDimOrdinal(QueryExecutorUtility.getSelectedDimensionStoreIndex(queryDimensions,info.getHybridStoreMeta()));
        }
        else
        {
        	info.setQueryDimOrdinal(QueryExecutorUtility.getSelectedDimnesionIndex(queryDimensions));
        }
        
        List<Dimension> customDim = new ArrayList<Dimension>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        info.setAllSelectedDimensions(QueryExecutorUtility.getAllSelectedDiemnsion(queryDimensions,dimAggInfo,customDim));
        info.setLimit(-1);
        info.setTotalNumerOfDimColumns(cube.getDimensions(cube.getFactTableName()).size());
        info.setTotalNumberOfMeasuresInTable(cube.getMeasures(cube.getFactTableName()).size());
        
        long [] startKey = new long[cube.getDimensions(meta.getTableName()).size()];
        long [] endKey = meta.getDimLens();
        info.setStartKey(startKey);
        info.setEndKey(endKey);
        info.setNumberOfRecordsInMemory(Integer.MAX_VALUE);
        info.setOutLocation(MolapProperties.getInstance().getProperty(
                MolapCommonConstants.STORE_LOCATION_HDFS, MolapCommonConstants.STORE_LOCATION_DEFAULT_VAL));
        info.setDimAggInfo(dimAggInfo);
        info.setCurrentSliceIndex(currentSliceIndex);
        int measureStartIndex = aggTypes.length - currentQueryMeasures.length;
        // check for is aggregate table here as second parameter below is isAggTable boolean (table from which data has to be read)
        double[] msrMinValue = QueryExecutorUtility.getMinValueOfSlices(cube.getFactTableName(),
                false, activeSlices);
        double[] queryMsrMinValue = new double[aggTypes.length]; 
        for(int i = 0;i < currentQueryMeasures.length;i++)
        {
        	queryMsrMinValue[measureStartIndex + i] = msrMinValue[currentQueryMeasures[i].getOrdinal()];
        }
        info.setMsrMinValue(queryMsrMinValue);
        info.setAggType(aggTypes);
        info.setMeasureStartIndex(measureStartIndex);
        List<CustomMolapAggregateExpression> expression = new ArrayList<CustomMolapAggregateExpression>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        info.setCustomExpressions(expression);
        return info;
    }
    
    /**
     * @param cube
     * @return
     */
    private void initQueryDims(Cube cube)
    {
        currentQueryDims = new Dimension[aggLevels.length];
        int i = 0;
        for(AggLevel aggLevel : aggLevels)
        {
            currentQueryDims[i++] = cube.getDimension(aggLevel.column, cube.getFactTableName());
        }
    }

    /**
     * @return
     */
    private String[] getAggTypes()
    {
        String[] aggTypes = new String[aggMeasures.length];
        // initializing high card mapping.
        isHighCardinality = new boolean[aggTypes.length];

           // executerProperties.a
        for(int i = 0;i < aggMeasures.length;i++)
        {
            if(null!=dimAggInfo)
            {
                for(DimensionAggregatorInfo dimAgg : dimAggInfo)
                {
                    // checking if the dimension aggregate is high cardinality or not.
                    if(aggMeasures[i].column.equals(dimAgg.getColumnName())
                            && dimAgg.getDim().isHighCardinalityDim())
                    {
                        isHighCardinality[i] = true;
                    }
                }
            }
            aggTypes[i] = aggMeasures[i].aggregator;
        }
        return aggTypes;
    }
    
    /**
     * @param cube
     * 
     */
    private void initQueryMsrs(Cube cube)
    {
        int count = 0;
        int[] matchedIndexes = new int[aggMeasures.length];
        Measure[] measure = new Measure[aggMeasures.length];
        for(int i = 0;i < aggMeasures.length;i++)
        {
            measure[i] = cube.getMeasure(meta.getTableName(),
                    aggMeasures[i].column);
            if(null != measure[i])
            {
                measure[i].setAggName(aggMeasures[i].aggregator);
                matchedIndexes[count++] = i;
            }
            else
            {
                Dimension dimension = cube.getDimension(aggMeasures[i].column,cube.getFactTableName());
                DimensionAggregatorInfo info = new DimensionAggregatorInfo();
                List<String> aggList = new ArrayList<String>(1);
                aggList.add(aggMeasures[i].aggregator);
                info.setAggList(aggList);
                if(dimension!=null){
                info.setColumnName(dimension.getColName());
                info.setDim(dimension);
                }
                dimAggInfo.add(info);
            }
        }
        currentQueryMeasures = new Measure[count];
        for(int i = 0;i < count;i++)
        {
            currentQueryMeasures[i] = measure[matchedIndexes[i]];
        }
    }
    /**
     * Observer class for thread execution 
     * In case of any failure we need stop all the running thread 
     * @author k00900841
     *
     */
    private class ThreadStatusObserver
    {
        /**
         * Below method will be called if any thread fails during execution
         * @param exception
         * @throws Exception
         */
        public void notifyFailed(Throwable exception) throws Exception
        {
            executorService.shutdownNow();
            LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, exception);
            throw new Exception(exception);
        }
    }

    
    /**
     * This method will be used for setting the output interface. Output
     * interface is how this step will process the row to next step
     * 
     * @param dimLens
     *            number of dimensions
     * 
     */
    private void setStepOutputInterface(String[] aggType)
    {
        int size=1 + this.meta.getMeasureCount();
        ValueMetaInterface[] out = new ValueMetaInterface[size];
        int l = 0;
        final String measureConst = "measure";
        ValueMetaInterface valueMetaInterface = null;
        ValueMetaInterface storageMetaInterface = null;
        int measureCount=meta.getMeasureCount();
        for(int i = 0;i < measureCount;i++)
        {
            if(aggType[i].charAt(0)=='n')
            {
                valueMetaInterface = new ValueMeta(measureConst + i,
                        ValueMetaInterface.TYPE_NUMBER,
                        ValueMetaInterface.STORAGE_TYPE_NORMAL);
                storageMetaInterface=(new ValueMeta(measureConst
                        + i, ValueMetaInterface.TYPE_NUMBER,
                        ValueMetaInterface.STORAGE_TYPE_NORMAL));
                valueMetaInterface.setStorageMetadata(storageMetaInterface);
                
            }
            else 
            {
                valueMetaInterface = new ValueMeta(measureConst + i,
                        ValueMetaInterface.TYPE_BINARY,
                        ValueMetaInterface.STORAGE_TYPE_BINARY_STRING);
                storageMetaInterface=(new ValueMeta(measureConst
                        + i, ValueMetaInterface.TYPE_STRING,
                        ValueMetaInterface.STORAGE_TYPE_NORMAL));
                valueMetaInterface.setStringEncoding(BYTE_ENCODING);
                valueMetaInterface.setStorageMetadata(storageMetaInterface);
                valueMetaInterface.getStorageMetadata().setStringEncoding(
                        BYTE_ENCODING);
                valueMetaInterface.setStorageMetadata(storageMetaInterface);
            }
            out[l++] = valueMetaInterface;
        }
        valueMetaInterface = new ValueMeta("id",
                ValueMetaInterface.TYPE_BINARY,
                ValueMetaInterface.STORAGE_TYPE_BINARY_STRING);
        valueMetaInterface.setStorageMetadata((new ValueMeta("id",
                ValueMetaInterface.TYPE_STRING,
                ValueMetaInterface.STORAGE_TYPE_NORMAL)));
        valueMetaInterface.setStringEncoding(BYTE_ENCODING);
        valueMetaInterface.getStorageMetadata().setStringEncoding(
                BYTE_ENCODING);
        out[l++] = valueMetaInterface;
        data.outputRowMeta.setValueMetaList(Arrays.asList(out));
    }

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
        meta = (MolapFactReaderMeta)smi;
        data = (MolapFactReaderData)sdi;
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
        meta = (MolapFactReaderMeta)smi;
        data = (MolapFactReaderData)sdi;
        super.dispose(smi, sdi);
    }

}
