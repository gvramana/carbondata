/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbweRARwUrjYxPx0CUk3mVB7mxOcZSaagKrMQNlhB
QO/t7OcIaYg265R56kYWVXPJrEsTVdhxHhXCZ2W1G14UYLmBIhNTKLoTrfmwjw/1s2Ew3hYD
MbX+EnAJXL1TSlMcG7SoZ1z36248sdJifaD1rZhP0zCwZhV+GNpjMl9kL+LrFw==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
 *
 */
package com.huawei.unibi.molap.engine.executer.impl.measure.sort;

import java.io.File;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.collections.comparators.ComparatorChain;

import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;
import com.huawei.unibi.molap.engine.directinterface.impl.MeasureSortModel;
import com.huawei.unibi.molap.engine.executer.Tuple;
import com.huawei.unibi.molap.engine.executer.groupby.GroupByHolder;
import com.huawei.unibi.molap.engine.executer.impl.comparator.MaksedByteComparatorForDFCH;
import com.huawei.unibi.molap.engine.executer.impl.comparator.MaksedByteComparatorForTuple;
import com.huawei.unibi.molap.engine.executer.impl.comparator.MeasureComparatorDFCH;
import com.huawei.unibi.molap.engine.executer.impl.comparator.MeasureComparatorTuple;
import com.huawei.unibi.molap.engine.executer.pagination.DataProcessor;
import com.huawei.unibi.molap.engine.executer.pagination.PaginationModel;
import com.huawei.unibi.molap.engine.executer.pagination.exception.MolapPaginationException;
import com.huawei.unibi.molap.engine.executer.pagination.impl.DataFileMerger;
import com.huawei.unibi.molap.engine.executer.pagination.impl.DataFileWriter;

/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap Engine
 * Author K00900841
 * Created Date :21-May-2013 6:42:29 PM 
 * FileName :DataFileWriter.java
 * Class Description : Processor class to sort the data based on measures 
 * Version 1.0
 */
public class MeasureSortProcessor implements DataProcessor
{
    /**
     * dataProcessor
     */
    private DataProcessor dataProcessor;
    
    /**
     * msrSortModel
     */
    private MeasureSortModel msrSortModel;
    
    /**
     * maskedByteRangeForsorting
     */
    private int[][] maskedByteRangeForSorting;
    
    /**
     * dataHeap
     */
    private AbstractQueue<Tuple> dataHeap;
    
    /**
     * holderSize
     */
    private int holderSize;
    
    /**
     * entryCount
     */
    private int entryCount;
    
    /**
     * dimensionSortOrder
     */
    private byte[] dimensionSortOrder;
    
    /**
     * dataFileWriter
     */
    private DataFileWriter dataFileWriter;
    
    /**
     * queryId
     */
    private String queryId;
    
    /**
     * outLocation
     */
    private String outLocation;
    
    /**
     * dimensionMasks
     */
    private byte[][] dimensionMasks;
    
    /**
     * comparatorChain
     */
    private Comparator comparatorChain; 
    
    /**
     * model
     */
    private PaginationModel model;
    
    /**
     * interFileLocation
     */
    private String interFileLocation;
    
    /**
     * paginationEnabled
     */
    private boolean paginationEnabled;
    /**
     * MeasureFilterProcessor Constructor
     * 
     * @param dataProcessor
     */
    public MeasureSortProcessor(DataProcessor dataProcessor)
    {
        this.dataProcessor=dataProcessor;
    }
    
    /**
     * Below method will be used to initialize the Processor  
     * @throws MolapPaginationException 
     */
    @Override
    public void initModel(PaginationModel model) throws MolapPaginationException
    {
        this.model=model;
        this.msrSortModel=model.getMsrSortModel();
        this.maskedByteRangeForSorting = model.getMaskedByteRangeForSorting();
        this.holderSize=model.getHolderSize();
        this.dimensionSortOrder=model.getDimensionSortOrder();
        this.dimensionMasks=model.getDimensionMasks();
        this.queryId=model.getQueryId();
        this.outLocation=model.getOutLocation();
        createComparatorChain();
        createHeap();
        this.interFileLocation = this.outLocation + File.separator + queryId+File.separator+MolapCommonConstants.MEASURE_SORT_FOLDER;
        this.paginationEnabled = model.isPaginationEnabled(); 
        dataProcessor.initModel(model);
        
    }

    /**
     * Below method will be used to process the data  
     * 
     * @param key
     * @param measures
     */
    @Override
    public void processRow(byte[] key, MeasureAggregator[] measures) throws MolapPaginationException
    {
        addRow(key, measures);
    }

    /**
     * below method will be used to add row to heap, heap size reach holder size
     * it will sort the data based dimension and measures(dimension for keep
     * heir) and write data to file
     * 
     * @param key
     * @param measures
     * @throws MolapPaginationException
     */
    private void addRow(byte[] key, MeasureAggregator[] measures) throws MolapPaginationException
    {
        if((this.entryCount==this.holderSize) && paginationEnabled)
        {
            writeData();
        }
        Tuple tuple = new Tuple();
        tuple.setKey(key);
        tuple.setMeasures(measures);
        this.dataHeap.add(tuple);
        this.entryCount++;
    }

    /**
     * Below method will be used to write sorted data to file 
     * @throws MolapPaginationException
     */
    private void writeData() throws MolapPaginationException
    {
        model.setRecordHolderType(MolapCommonConstants.HEAP);
        dataFileWriter = new DataFileWriter(dataHeap,model,MolapCommonConstants.HEAP,interFileLocation);
        try
        {
            dataFileWriter.call();
        }
        catch(Exception e)
        {
            throw new MolapPaginationException(e);
        }
        this.entryCount=0;
        createHeap();
    }
    
    /**
     * Below method will be used to create chain comparator for sorting 
     */
    private void createComparatorChain()
    {
        List compratorList = new ArrayList(MolapCommonConstants.CONSTANT_SIZE_TEN);
        MaksedByteComparatorForTuple keyComparator= null;
        if(this.msrSortModel.getSortOrder() < 2)
        {
            for(int i = 0;i < this.maskedByteRangeForSorting.length-1;i++)
            {
                keyComparator= new MaksedByteComparatorForTuple(this.maskedByteRangeForSorting[i], this.dimensionSortOrder[i],this.dimensionMasks[i]);
                compratorList.add(keyComparator);
            }
        }
        MeasureComparatorTuple measureComparator = new MeasureComparatorTuple(msrSortModel.getMeasureIndex(), this.msrSortModel.getSortOrder());
        compratorList.add(measureComparator);
        this.comparatorChain = new ComparatorChain(compratorList);
    }
    
    /**
     * Below method will be used to create the heap holder 
     */
    private void createHeap()
    {
        this.dataHeap = new PriorityQueue<Tuple>(this.holderSize,this.comparatorChain);     
    }
    
    /**
     * Below method will be call to finish the processor
     * @throws MolapPaginationException
     */
    @Override
    public void finish() throws MolapPaginationException
    {
        if(paginationEnabled)
        {
            writeData();
            List compratorList = new ArrayList(MolapCommonConstants.CONSTANT_SIZE_TEN);
            MaksedByteComparatorForDFCH keyComparator= null;
            if(this.msrSortModel.getSortOrder() < 2)
            {
                for(int i = 0;i < this.maskedByteRangeForSorting.length-1;i++)
                {
                    keyComparator= new MaksedByteComparatorForDFCH(this.maskedByteRangeForSorting[i], this.dimensionSortOrder[i],this.dimensionMasks[i]);
                    compratorList.add(keyComparator);
                }
            }
            MeasureComparatorDFCH measureComparator = new MeasureComparatorDFCH(msrSortModel.getMeasureIndex(), this.msrSortModel.getSortOrder());
            compratorList.add(measureComparator);
            ComparatorChain comparatorChain = new ComparatorChain(compratorList);
            DataFileMerger dataFileMerger = new DataFileMerger(model, comparatorChain,dataProcessor,interFileLocation);
            try
            {
                dataFileMerger.call();
            }
            catch(Exception e)
            {
                throw new MolapPaginationException(e);
            }
        }
        else
        {
            //CHECKSTYLE:OFF    Approval No:Approval-V3R8C00_003
            int size = dataHeap.size();
            if(msrSortModel.isBreakHeir())
            {
                List compratorList = new ArrayList();
                MaksedByteComparatorForTuple keyComparator = null;
                for(int i = 0;i < this.maskedByteRangeForSorting.length - 1;i++)
                {
                    keyComparator = new MaksedByteComparatorForTuple(this.maskedByteRangeForSorting[i],
                            this.dimensionSortOrder[i], this.dimensionMasks[i]);
                    compratorList.add(keyComparator);
                }
                MeasureComparatorTuple measureComparator = new MeasureComparatorTuple(msrSortModel.getMeasureIndex(),
                        this.msrSortModel.getSortOrder());
                compratorList.add(measureComparator);
                ComparatorChain comparatorChain = new ComparatorChain(compratorList);

                List<Tuple> tupleList = new ArrayList<Tuple>(dataHeap);

                Collections.sort(tupleList, comparatorChain);

                for(Tuple tuple : tupleList)
                {
                    dataProcessor.processRow(tuple.getKey(), tuple.getMeasures());
                }
             // CHECKSTYLE:ON
            }
            else
            {
                for(int j = 0;j < size;j++)
                {
                    Tuple tuple = dataHeap.poll();
                    dataProcessor.processRow(tuple.getKey(), tuple.getMeasures());
                }
            }

            dataProcessor.finish();
        }
    }

    @Override
    public void processGroup(GroupByHolder groupByHolder)
    {
        // No need to implement any thing.
        
    }
}
