/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbweRARwUrjYxPx0CUk3mVB7mxOcZSaagKrMQNlhB
QO/t7N9erIxJIG5+eT8TO/1vHU9DoavJOdkILGcqGDcZVaMslwHx1fOH0Mg3f7I/lU+Qu0la
ySsA1QxJdQkQONVvAZmskUGHhHKjXs06sjYnXJeVS0LzodHem3ab7lXFy9mqNw==*/
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

package com.huawei.unibi.molap.engine.executer.impl;

import java.util.Map;
import java.util.concurrent.Callable;

import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;
import com.huawei.unibi.molap.engine.wrappers.ByteArrayWrapper;

/**
 * Project Name NSE V3R7C00 
 * Module Name : MOLAP
 * Author V00900840 
 * Created Date :13-May-2013 3:19:26 PM 
 * FileName : SliceRangeExecutor.java 
 * Class Description :  This class is responsible for creating scanner based on filter, parallel or 
 *                      no filter tree scanner and then scan the file based on the start and end key,
 *                      and populate the data.
 *                      
 * Version 1.0
 */
public class SliceRangeExecutor implements Callable<Map<ByteArrayWrapper, MeasureAggregator[]>>
{

    @Override
    public Map<ByteArrayWrapper, MeasureAggregator[]> call() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
//    /**
//     * ID
//     */
//    private String id;
//
//    /**
//     * Start key
//     */
//    private long[] startKey;
//
//    /**
//     * End key
//     */
//    private long[] endKey;
//
//    /**
//     * Parallel execution
//     */
//    private boolean executionInParallel;
//
//    /**
//     * Slice execution info
//     */
//    private SliceExecutionInfo info;
//    
//    /**
//     * 
//     */
//    private FileHolder fileHolder;
//    
//    /**
//     * 
//     */
//    private boolean[] msrExists;
//    
//    
//    private DataAggregator aggregator;
//    
//    private GlobalPaginatedAggregator paginatedAggregator;
//    
//    private int rowLimit;
//   
//
//    /**
//     * LOGGER.
//     */
//    private static final LogService LOGGER = LogServiceFactory.getLogService(SliceRangeExecutor.class.getName());
//    
//    public SliceRangeExecutor(long[] startKey, long[] endKey, boolean executionInParallel, SliceExecutionInfo info,GlobalPaginatedAggregator paginatedAggregator,int rowLimit)
//    {
//        this.startKey = startKey;
//        this.endKey = endKey;
//        this.executionInParallel = executionInParallel;
//        id = Arrays.toString(startKey) + " - " + Arrays.toString(endKey);
//        this.info = info;
//        this.fileHolder = FileFactory.getFileHolder(FileFactory.getFileType());
//        this.msrExists = info.getMsrsExists();
//        this.paginatedAggregator = paginatedAggregator;
//        this.rowLimit = rowLimit;
//       
//    }
//
//
//    /**
//     * Execute through thread
//     */
//    @Override
//    public Map<ByteArrayWrapper, MeasureAggregator[]> call() throws Exception
//    {
//       
//        CubeDataStore dataCache = info.getSlice().getDataCache(info.getTableName());

//        Map<ByteArrayWrapper, MeasureAggregator[]> data = new HashMap<ByteArrayWrapper, MeasureAggregator[]>(1000);
//
//        Scanner scanner = null;
//
//        scanner = getTreeScanner(dataCache, fileHolder);
//        
//        if(info.isCustomMeasure())
//        {
//            aggregator = new LocalDataAggregatorForAutoAggregateImpl(info, paginatedAggregator, rowLimit, id);
//        }
//        else if(checkAnyNewMsr())
//        {
//            aggregator = new LocalDataAggregatorRSImpl(info, paginatedAggregator, rowLimit,id);
//        }
//        else if(info.getCalculatedMeasures().length > 0)
//        {
//            aggregator = new LocalDataAggregatorWithCalcMsrImpl(info, paginatedAggregator, rowLimit,id);
//        }
//        else
//        {
//            aggregator = new LocalDataAggregatorImpl(info, paginatedAggregator, rowLimit,id);
//        }
//        aggregator.aggregate(scanner);
//        
//        fileHolder.finish();
//        return data;
//    }
//    
//    private boolean isSmartJumpRequired(long[][] predicateKeys)
//    {
//        int percent = Integer.parseInt(MolapProperties.getInstance().getProperty("molap.smartJump.avoid.percent", "70"));
//      //CHECKSTYLE:OFF    Approval No:Approval-358
//        double percentage = ((double)percent)/100;
//        int len = (int)(predicateKeys.length*percentage);
//      //CHECKSTYLE:ON
//        if(len <= 0)
//        {
//            return true;
//        }
//        for(int i = 0;i < predicateKeys.length;i++)
//        {
//            if(predicateKeys[i] != null)
//            {
//                if(i <= len)
//                {
//                    return true;
//                }
//               if(i >= len)
//               {
//                   return false;
//               }
//            }
//        }
//        return true;
//    }
//    
//    /**
//     * It stops the execution.
//     */
//    public void interrupt()
//    {
//        if(null!=aggregator)
//        {
//        aggregator.interrupt();
//        }
//        LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, "SliceRangeExecutor is interrupted");
//    }
//    
//    
//    /**
//     * Check any measure is new
//     * @return boolean
//     */
//    private boolean checkAnyNewMsr()
//    {
//        boolean isNew = false;
//        for(int i = 0;i < msrExists.length;i++)
//        {
//            if(!msrExists[i])
//            {
//                isNew = true;
//                break;
//            }
//        }
//        return isNew;
//    }
//
//    /**
//     * Return the scanner set with filter if required based on the the query
//     * being executed.
//     * 
//     * @param dataCache
//     * 
//     * @return
//     * @throws KeyGenException
//     * 
//     */
//    private Scanner getTreeScanner(CubeDataStore dataCache, FileHolder fileHolder) throws KeyGenException
//    {
//        Scanner scanner;
//        InMemoryFilter filter = null;
//        byte[] startKeyBytes = info.getKeyGenerator().generateKey(startKey);
//        //
//        KeyValue keyValue = new KeyValue();
//        
//        if(CacheUtil.checkAnyIncludeOrExists(info.getConstraints()))
//        {
//
//            filter = new IncludeOrKeyFilterImpl(info.getFilterModel(), info.getKeyGenerator(), info.getEndKey());
//            scanner = new FilterTreeScanner(startKeyBytes, info.getKeyGenerator().generateKey(endKey),
//                    info.getKeyGenerator(), keyValue, info.getMeasureOrdinal(), fileHolder,false);
//
//            ((FilterTreeScanner)scanner).setFilter(filter);
//        }
//        else if(CacheUtil.checkAnyExcludeExists(info.getConstraints()))
//        {
//            boolean smartJump = false;
//            if(isSmartJumpRequired(info.getFilterModel().getIncludePredicateKeys()) && isSmartJumpRequired(info.getFilterModel().getExcludePredicateKeys()))
//            {
//                smartJump = true;
//            }
//            LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, "Smart Jump is "+smartJump);
//            filter = new IncludeExcludeKeyFilterImpl(info.getFilterModel(), info.getKeyGenerator(), info.getEndKey());
//            scanner = new FilterTreeScanner(startKeyBytes, info.getKeyGenerator().generateKey(endKey),
//                    info.getKeyGenerator(), keyValue, info.getMeasureOrdinal(), fileHolder,smartJump);
//
//            ((FilterTreeScanner)scanner).setFilter(filter);
//        }
//        //
//        else if(CacheUtil.checkAnyIncludeExists(info.getConstraints()))
//        {
//            boolean smartJump = false;
//            if(isSmartJumpRequired(info.getFilterModel().getIncludePredicateKeys()))
//            {
//                smartJump = true;
//            }
//            LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, "Smart Jump is "+smartJump);
//            filter = new KeyFilterImpl(info.getFilterModel(), info.getKeyGenerator(), info.getEndKey());
//
//            scanner = new FilterTreeScanner(startKeyBytes, info.getKeyGenerator().generateKey(endKey),
//                    info.getKeyGenerator(), keyValue, info.getMeasureOrdinal(), fileHolder,smartJump);
//
//            ((FilterTreeScanner)scanner).setFilter(filter);
//        }
//        //
//        else if(executionInParallel)
//        {
//            scanner = new ParallelNonFilterTreeScanner(startKeyBytes, info.getKeyGenerator().generateKey(endKey), info.getKeyGenerator(), dataCache.getData()
//                    .getRangeSplitValue(), keyValue, info.getMeasureOrdinal(), fileHolder);
//        }
//        else
//        {
//            scanner = new NonFilterTreeScanner(startKeyBytes, null, info.getKeyGenerator(), keyValue,
//                    info.getMeasureOrdinal(), fileHolder);
//        }
//        dataCache.initializeScanner(startKeyBytes, scanner);
//        return scanner;
//    }

}
