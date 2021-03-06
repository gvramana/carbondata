/**
 * 
 */
package com.huawei.unibi.molap.engine.processor.sort;

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
import com.huawei.unibi.molap.engine.executer.impl.comparator.MaksedByteComparatorForDFCH;
import com.huawei.unibi.molap.engine.executer.impl.comparator.MaksedByteComparatorForTuple;
import com.huawei.unibi.molap.engine.executer.impl.comparator.MeasureComparatorDFCH;
import com.huawei.unibi.molap.engine.executer.impl.comparator.MeasureComparatorTuple;
import com.huawei.unibi.molap.engine.executer.pagination.impl.QueryResult;
import com.huawei.unibi.molap.engine.merger.SortedResultFileMerger;
import com.huawei.unibi.molap.engine.processor.DataProcessor;
import com.huawei.unibi.molap.engine.processor.exception.DataProcessorException;
import com.huawei.unibi.molap.engine.schema.metadata.DataProcessorInfo;
import com.huawei.unibi.molap.engine.util.ScannedResultProcessorUtil;
import com.huawei.unibi.molap.engine.wrappers.ByteArrayWrapper;
import com.huawei.unibi.molap.engine.writer.HeapBasedDataFileWriterThread;
import com.huawei.unibi.molap.iterator.MolapIterator;

/**
 * 
 * Project Name  : Carbon 
 * Module Name   : MOLAP Data Processor
 * Author    : R00903928,k00900841
 * Created Date  : 27-Aug-2015
 * FileName   : SortMeasureProcessor.java
 * Description   : This class is responsible for sorting the data based on a measure.
 * Class Version  : 1.0
 */
public class SortMeasureProcessor implements DataProcessor
{

    private DataProcessor dataProcessor;

    private DataProcessorInfo dataProcessorInfo;

    private String interFileLocation;

    /**
     * entryCount
     */
    private int entryCount;

    /**
     * comparatorChain
     */
    private Comparator comparatorChain;

    /**
     * dataHeap
     */
    private AbstractQueue<Tuple> dataHeap;

    private String outLocation;

    private boolean isFileBased;

    private MeasureSortModel measureSortModel;

    public SortMeasureProcessor(DataProcessor dataProcessor, String outLocation, boolean isFilebased,
            MeasureSortModel measureSortModel)
    {
        this.dataProcessor = dataProcessor;
        this.outLocation = outLocation;
        this.isFileBased = isFilebased;
        this.measureSortModel = measureSortModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.huawei.unibi.molap.engine.processor.DataProcessor#initialise(com.
     * huawei.unibi.molap.engine.processor.DataProcessorInfo)
     */
    @Override
    public void initialise(DataProcessorInfo model) throws DataProcessorException
    {
        dataProcessorInfo = model;
        createComparatorChain();
        createHeap();
        this.interFileLocation = outLocation + '/' + model.getQueryId() + '/'
                + MolapCommonConstants.MEASURE_SORT_FOLDER;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.huawei.unibi.molap.engine.processor.DataProcessor#processRow(byte[],
     * com.huawei.unibi.molap.engine.aggregator.MeasureAggregator[])
     */
    @Override
    public void processRow(byte[] key, MeasureAggregator[] value) throws DataProcessorException
    {
        addRow(key, value);
    }
    
    @Override
    public void processRow(ByteArrayWrapper key, MeasureAggregator[] value) throws DataProcessorException
    {
        processRow(key.getMaskedKey(), value);
    }
    /*
     * (non-Javadoc)
     * 
     * @see com.huawei.unibi.molap.engine.processor.DataProcessor#finish()
     */
    @Override
    public void finish() throws DataProcessorException
    {

        if(isFileBased)
        {
            writeData();
            List compratorList = new ArrayList(10);
            MaksedByteComparatorForDFCH keyComparator = null;
            if(measureSortModel.getSortOrder() < 2)
            {
                for(int i = 0;i < dataProcessorInfo.getMaskedByteRangeForSorting().length - 1;i++)
                {
                    if(null== dataProcessorInfo.getMaskedByteRangeForSorting()[i])
                    {
                        continue;
                    }
                    keyComparator = new MaksedByteComparatorForDFCH(
                            dataProcessorInfo.getMaskedByteRangeForSorting()[i],
                            dataProcessorInfo.getDimensionSortOrder()[i], dataProcessorInfo.getDimensionMasks()[i]);
                    compratorList.add(keyComparator);
                }
                
            }
            MeasureComparatorDFCH measureComparator = new MeasureComparatorDFCH(measureSortModel.getMeasureIndex(),
                    measureSortModel.getSortOrder());
            compratorList.add(measureComparator);
            //ComparatorChain comparatorChain = new ComparatorChain(compratorList);
            // DataFileMerger dataFileMerger = new DataFileMerger(model,
            // comparatorChain,dataProcessor,interFileLocation);
            SortedResultFileMerger sortedFileMergerThread = new SortedResultFileMerger(dataProcessor,
                    dataProcessorInfo, ScannedResultProcessorUtil.getFiles(interFileLocation, new String[]{
                            MolapCommonConstants.QUERY_OUT_FILE_EXT, MolapCommonConstants.QUERY_MERGED_FILE_EXT}));
            try
            {
                sortedFileMergerThread.call();
            }
            catch(Exception e)
            {
                throw new DataProcessorException(e);
            }
        }
        else
        {
            // CHECKSTYLE:OFF Approval No:Approval-V3R8C00_003
            int size = dataHeap.size();
            if(measureSortModel.isBreakHeir())
            {
                List compratorList = new ArrayList();
                if(dataProcessorInfo.getDimensionSortOrder().length < 1)
                {
                    MaksedByteComparatorForTuple keyComparator = null;

                    for(int i = 0;i < dataProcessorInfo.getMaskedByteRangeForSorting().length - 1;i++)
                    {
                        if(null== dataProcessorInfo.getMaskedByteRangeForSorting()[i])
                        {
                            continue;
                        }
                        keyComparator = new MaksedByteComparatorForTuple(
                                dataProcessorInfo.getMaskedByteRangeForSorting()[i],
                                dataProcessorInfo.getDimensionSortOrder()[i], dataProcessorInfo.getDimensionMasks()[i]);
                        compratorList.add(keyComparator);
                    }
                    
                }
                MeasureComparatorTuple measureComparator = new MeasureComparatorTuple(
                        measureSortModel.getMeasureIndex(), measureSortModel.getSortOrder());
                compratorList.add(measureComparator);
                ComparatorChain comparatorChain = new ComparatorChain(compratorList);

                List<Tuple> tuples = new ArrayList<Tuple>(dataHeap);

                Collections.sort(tuples, comparatorChain);

                for(Tuple tuple : tuples)
                {
                    dataProcessor.processRow(tuple.getKey(), tuple.getMeasures());
                }
                // CHECKSTYLE:ON
            }
            else
            {
                for(int i = 0;i < size;i++)
                {
                    Tuple tuple = dataHeap.poll();
                    dataProcessor.processRow(tuple.getKey(), tuple.getMeasures());
                }
            }

            dataProcessor.finish();
        }

    }

    /**
     * Below method will be used to create chain comparator for sorting
     */
    private void createComparatorChain()
    {
        List compratorList = new ArrayList(10);
        MaksedByteComparatorForTuple keyComparator = null;
        if(measureSortModel.getSortOrder() < 2)
        {
            for(int i = 0;i < dataProcessorInfo.getMaskedByteRangeForSorting().length - 1;i++)
            {
                if(null==dataProcessorInfo.getMaskedByteRangeForSorting()[i])
                {
                    continue;
                }
                keyComparator = new MaksedByteComparatorForTuple(dataProcessorInfo.getMaskedByteRangeForSorting()[i],
                        dataProcessorInfo.getDimensionSortOrder()[i], dataProcessorInfo.getDimensionMasks()[i]);
                compratorList.add(keyComparator);
            }
        }
        this.comparatorChain = new ComparatorChain(compratorList);
       
        MeasureComparatorTuple measureComparator = new MeasureComparatorTuple(measureSortModel.getMeasureIndex(),
                measureSortModel.getSortOrder());
        compratorList.add(measureComparator);
        
    }

    /**
     * Below method will be used to create the heap holder
     */
    private void createHeap()
    {
        this.dataHeap = new PriorityQueue<Tuple>(dataProcessorInfo.getHolderSize(), this.comparatorChain);
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
    private void addRow(byte[] key, MeasureAggregator[] measures) throws DataProcessorException
    {
        if((this.entryCount == dataProcessorInfo.getHolderSize()) && isFileBased)
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
     * 
     * @throws MolapPaginationException
     */
    private void writeData() throws DataProcessorException
    {
        HeapBasedDataFileWriterThread dataWriter = new HeapBasedDataFileWriterThread(dataHeap, dataProcessorInfo,
                interFileLocation);
        try
        {
            dataWriter.call();
        }
        catch(Exception e)
        {
            throw new DataProcessorException(e);
        }
        this.entryCount = 0;
        createHeap();
    }

    @Override
    public MolapIterator<QueryResult> getQueryResultIterator()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
