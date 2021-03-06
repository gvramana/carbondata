/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwedLwWEET5JCCp2J65j3EiB2PJ4ohyqaGEDuXyJ
TTt3d0EsxvHoMkGWmRRmyEz+U3jFefbodoqI8Ek8SEFXdMjJ1wKoRMPneP4jQWJGIdJ6Kwd4
HdTwkaWbZA0QdSLZNpjr74Dk+jSBjEpkxFlhdx2cB5bf5TIifIHq4HxeI85ajQ==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * 
 */
package com.huawei.unibi.molap.engine.executer.pagination.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//import mondrian.olap.ResourceLimitExceededException;
import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;
import com.huawei.unibi.molap.engine.cache.QueryExecutorUtil;
//import com.huawei.unibi.molap.engine.aggregator.util.AggUtil;
import com.huawei.unibi.molap.engine.datastorage.InMemoryCube;
import com.huawei.unibi.molap.engine.datastorage.storeInterfaces.KeyValue;
import com.huawei.unibi.molap.engine.executer.pagination.DataAggregator;
import com.huawei.unibi.molap.engine.executer.pagination.GlobalPaginatedAggregator;
import com.huawei.unibi.molap.engine.scanner.Scanner;
import com.huawei.unibi.molap.engine.schema.metadata.SliceExecutionInfo;
import com.huawei.unibi.molap.engine.util.MolapEngineLogEvent;
import com.huawei.unibi.molap.engine.wrappers.ByteArrayWrapper;
import com.huawei.unibi.molap.keygenerator.KeyGenerator;
import com.huawei.unibi.molap.metadata.MolapMetadata.Measure;
import com.huawei.unibi.molap.util.MolapProperties;

/**
 * It scans the data from store and aggregates the data. 
 * @author R00900208
 *
 */
public class LocalDataAggregatorImpl implements DataAggregator
{

    /**
     * 
     */
    protected byte[] maxKeyBasedOnDim;

    /**
     * 
     */
    protected int[] maskedByteRanges;

    /**
     * 
     */
    protected int maskedKeyByteSize;
    
    
    /**
     * Measures
     */
    protected Measure[] queryMsrs;
    
    /**
     * Key generator
     */
    protected KeyGenerator generator;
    
    /**
     * slice
     */
    protected InMemoryCube slice;
    
    /**
     * Row counter to interrupt;
     */
    protected int rowLimit;
    
    /**
     * Internal row counter;
     */
    protected int counter;
    
    /**
     * Unique values represents null values of measure.
     */
    protected double[] uniqueValues;
    
    
    protected ByteArrayWrapper dimensionsRowWrapper = new ByteArrayWrapper();
    
    
    /**
     * Measures ordinal
     */
    protected int[] measureOrdinal;
    
    /**
     * Data Map 
     */
    protected Map<ByteArrayWrapper, MeasureAggregator[]> data;
    
    /**
     * paginatedAggregator
     */
    protected GlobalPaginatedAggregator paginatedAggregator;
    
    protected SliceExecutionInfo info;
    
    /**
     * msrExists
     */
    protected boolean[] msrExists;
    
    /**
     * msrDft
     */
    protected double[] msrDft;
    
    /**
     * LOGGER.
     */
    private static final LogService LOGGER = LogServiceFactory.getLogService(LocalDataAggregatorImpl.class.getName());
    
    protected boolean interrupted;
    
    /**
     * ID
     */
    protected String id;
    
    protected long rowCount;
    
    protected int[] avgMsrIndexes;
    
    protected int countMsrIndex;
    
    protected boolean aggTable;
    
    protected int[] otherMsrIndexes;
    
    protected int limit;
    
    protected boolean detailQuery;
    
    public LocalDataAggregatorImpl(SliceExecutionInfo info,GlobalPaginatedAggregator paginatedAggregator,int rowLimit, String id)
    {
//        queryMsrs = info.getQueryMsrs();
        measureOrdinal = info.getMeasureOrdinal();
//        maxKeyBasedOnDim = info.getMaxKeyBasedOnDimensions();
//        maskedByteRanges = info.getMaskedByteRanges();
        maskedKeyByteSize = info.getMaskedKeyByteSize();
        this.generator = info.getKeyGenerator();
        this.slice = info.getSlice();
//        this.msrExists = info.getMsrsExists();
//        this.msrDft = info.getNewMeasureDftValue();
        this.uniqueValues = info.getUniqueValues();
        this.avgMsrIndexes = QueryExecutorUtil.convertIntegerListToIntArray(info.getAvgIndexes());
        Arrays.sort(avgMsrIndexes);
        this.countMsrIndex = info.getCountMsrsIndex();
        aggTable = countMsrIndex>-1;
        if(aggTable)
        {
            otherMsrIndexes = getOtherMsrIndexes();
        }
        String mapSize = MolapProperties.getInstance().getProperty("molap.intial.agg.mapsize", "5000");
        int size = Integer.parseInt(mapSize);
        data = new HashMap<ByteArrayWrapper, MeasureAggregator[]>(size);
        this.paginatedAggregator = paginatedAggregator;
        this.info = info;
        this.rowLimit = rowLimit;
        this.id = id;
        this.limit = info.getLimit();
        this.detailQuery = info.isDetailQuery();
        if(detailQuery && limit > 0)
            {
            this.rowLimit = limit;
            }
    }
    
    /* (non-Javadoc)
     * @see com.huawei.unibi.molap.engine.executer.pagination.DataAggregator#aggregate(byte[], com.huawei.unibi.molap.engine.aggregator.MeasureAggregator[])
     */
    @Override
    public void aggregate(Scanner scanner)
    {
        long startTime = System.currentTimeMillis();
        try
        {
            XXHash32 xxHash32 = null;
            boolean useXXHASH = Boolean.valueOf(MolapProperties.getInstance().getProperty("molap.enableXXHash", "false"));
            if(useXXHASH)
            {
                xxHash32 = XXHashFactory.unsafeInstance().hash32();
            }
            dimensionsRowWrapper = new ByteArrayWrapper(xxHash32);
//            LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, "XXXXXXXXXXXXXX  Using XXHash32 ");
            
            
            // for aggregating without decomposition using masking

            int count = 0;
            // Search till end of the data
            while(!scanner.isDone() && !interrupted)
            {
                KeyValue available = scanner.getNext();
                // Set the data into the wrapper
                dimensionsRowWrapper.setData(available.backKeyArray, available.keyOffset, 
                        maxKeyBasedOnDim, maskedByteRanges, maskedKeyByteSize);
        
                // 2) Extract required measures
                MeasureAggregator[] currentMsrRowData = data.get(dimensionsRowWrapper);
        
                if(currentMsrRowData == null)
                {
//                    currentMsrRowData = AggUtil.getAggregators(queryMsrs, aggTable, generator, slice.getCubeUniqueName());
//                    dimensionsRowWrapper.setActualData(available.getBackKeyArray(), available.getKeyOffset(), available.getKeyLength());
                    data.put(dimensionsRowWrapper, currentMsrRowData);
                    dimensionsRowWrapper = new ByteArrayWrapper(xxHash32);
                    counter++;
                    if(counter > rowLimit)
                    {
                        aggregateMsrs(available, currentMsrRowData);
                        rowLimitExceeded();
                        counter = 0;
                        count++;
                        continue;
                    }
                }
                aggregateMsrs(available, currentMsrRowData);
                count++;
            }
            rowCount += data.size();
            
//            Entry[] table;
            if(data.size() > 0) {
                Field field = HashMap.class.getDeclaredField("table");
                field.setAccessible(true);
                Object[] table = (Object[])field.get(data);
                int counter=0;
                for(Object entry : table)
                {
                    if(entry!=null)
                        {
                        counter++;
                        }
                }
                LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, "YYYYYYYYYYY  Using Non Empty entries:  " + counter);
            }
            
            
            
            LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, 
                    "Time taken for scan for range " + id + " : " + (System.currentTimeMillis() - startTime) +" && Scanned Count : "+count +"  && Map Size : "+rowCount);
            
            LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, 
                    "CUBE: " +info.getCubeName()+ ", Time taken for scan : " + (System.currentTimeMillis() - startTime));
            
            
            finish();
        }
        
//        catch (ResourceLimitExceededException e) 
//        {
//            throw e;
//        }
        catch(Exception e)
        {
            LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,e, e.getMessage());
        }

    }

    /**
     * aggregateMsrs
     * @param available
     * @param currentMsrRowData
     */
    protected void aggregateMsrs(KeyValue available, MeasureAggregator[] currentMsrRowData)
    {
        if(aggTable)
        {
            aggregateMsrsForAggTable(available, currentMsrRowData);
            return;
        }
        for(int i = 0;i < queryMsrs.length;i++)
        {
            double value = available.getValue(measureOrdinal[i]);
            if(uniqueValues[measureOrdinal[i]] != value)
            {
                currentMsrRowData[i].agg(value, available.backKeyArray,
                    available.keyOffset, available.keyLength);
            }
        }
    }
    
    private int[] getOtherMsrIndexes()
    {
        int[] indexes = new int[queryMsrs.length-(avgMsrIndexes.length)];
        int k = 0;
        for(int i = 0;i < queryMsrs.length;i++)
        {
            if(Arrays.binarySearch(avgMsrIndexes, i) < 0)
            {
                indexes[k++] = i;
            }
        }
        return indexes;
    }
    
    /**
     * aggregateMsrs
     * @param available
     * @param currentMsrRowData
     */
    protected void aggregateMsrsForAggTable(KeyValue available, MeasureAggregator[] currentMsrRowData)
    {
        double countValue = available.getValue(measureOrdinal[countMsrIndex]);
        
        for(int i = 0;i < avgMsrIndexes.length;i++)
        {
            double value = available.getValue(measureOrdinal[avgMsrIndexes[i]]);
            if(uniqueValues[measureOrdinal[avgMsrIndexes[i]]] != value)
            {
                currentMsrRowData[avgMsrIndexes[i]].agg(value,countValue);
            }
        }
        
        for(int i = 0;i < otherMsrIndexes.length;i++)
        {
            double value = available.getValue(measureOrdinal[otherMsrIndexes[i]]);
            if(uniqueValues[measureOrdinal[otherMsrIndexes[i]]] != value)
            {
                currentMsrRowData[otherMsrIndexes[i]].agg(value, available.backKeyArray,
                    available.keyOffset, available.keyLength);
            }
        }
    }
    
    


    public void rowLimitExceeded() throws Exception
    {
        rowCount += data.size();
        paginatedAggregator.writeToDisk(data,info.getRestructureHolder());
        data = new HashMap<ByteArrayWrapper, MeasureAggregator[]>(16);
        if(detailQuery && limit > 0)
        {
            if(rowCount > limit) 
                {
                interrupt();
                }
        }
    }

    @Override
    public void finish() throws Exception
    {
 
        paginatedAggregator.writeToDisk(data,info.getRestructureHolder());
        
    }

    @Override
    public void interrupt()
    {
        interrupted = true;
    }

}
