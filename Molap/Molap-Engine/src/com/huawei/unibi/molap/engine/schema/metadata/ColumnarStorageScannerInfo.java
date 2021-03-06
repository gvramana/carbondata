package com.huawei.unibi.molap.engine.schema.metadata;

import com.huawei.unibi.molap.datastorage.store.FileHolder;
import com.huawei.unibi.molap.engine.columnar.aggregator.ColumnarAggregatorInfo;
import com.huawei.unibi.molap.engine.columnar.datastoreblockprocessor.DataStoreBlockProcessor;
import com.huawei.unibi.molap.engine.datastorage.storeInterfaces.DataStoreBlock;
import com.huawei.unibi.molap.engine.executer.impl.RestructureHolder;
import com.huawei.unibi.molap.engine.executer.processor.ScannedResultProcessor;

public class ColumnarStorageScannerInfo
{
    private DataStoreBlock datablock;

    private long totalNumberOfBlocksToScan;

    private DataStoreBlockProcessor blockProcessor;

    private RestructureHolder restructurHolder;

    private ColumnarAggregatorInfo columnarAggregatorInfo;
    
    private boolean isAutoAggregateTableRequest;
    
    private int keySize;
    
    private int dimColumnCount;
    
    private int msrColumnCount;

    private FileHolder fileHolder;
    
    private ScannedResultProcessor scannedResultProcessor;

    private String queryId;

    /**
     * partitionid
     */
    private String partitionId;
    /**
     * @return the datablock
     */
    public DataStoreBlock getDatablock()
    {
        return datablock;
    }

    /**
     * @return the totalNumberOfBlocksToScan
     */
    public long getTotalNumberOfBlocksToScan()
    {
        return totalNumberOfBlocksToScan;
    }

    /**
     * @return the blockProcessor
     */
    public DataStoreBlockProcessor getBlockProcessor()
    {
        return blockProcessor;
    }

    /**
     * @return the restructurHolder
     */
    public RestructureHolder getRestructurHolder()
    {
        return restructurHolder;
    }

    /**
     * @return the columnarAggregatorInfo
     */
    public ColumnarAggregatorInfo getColumnarAggregatorInfo()
    {
        return columnarAggregatorInfo;
    }

    /**
     * @param datablock the datablock to set
     */
    public void setDatablock(DataStoreBlock datablock)
    {
        this.datablock = datablock;
    }

    /**
     * @param totalNumberOfBlocksToScan the totalNumberOfBlocksToScan to set
     */
    public void setTotalNumberOfBlocksToScan(long totalNumberOfBlocksToScan)
    {
        this.totalNumberOfBlocksToScan = totalNumberOfBlocksToScan;
    }

    /**
     * @param blockProcessor the blockProcessor to set
     */
    public void setBlockProcessor(DataStoreBlockProcessor blockProcessor)
    {
        this.blockProcessor = blockProcessor;
    }

    /**
     * @param restructurHolder the restructurHolder to set
     */
    public void setRestructurHolder(RestructureHolder restructurHolder)
    {
        this.restructurHolder = restructurHolder;
    }

    /**
     * @param columnarAggregatorInfo the columnarAggregatorInfo to set
     */
    public void setColumnarAggregatorInfo(ColumnarAggregatorInfo columnarAggregatorInfo)
    {
        this.columnarAggregatorInfo = columnarAggregatorInfo;
    }

    public boolean isAutoAggregateTableRequest()
    {
        return isAutoAggregateTableRequest;
    }

    public void setAutoAggregateTableRequest(boolean isAutoAggregateTableRequest)
    {
        this.isAutoAggregateTableRequest = isAutoAggregateTableRequest;
    }

    public int getKeySize()
    {
        return keySize;
    }

    public void setKeySize(int keySize)
    {
        this.keySize = keySize;
    }

    public int getDimColumnCount()
    {
        return dimColumnCount;
    }

    public void setDimColumnCount(int dimColumnCount)
    {
        this.dimColumnCount = dimColumnCount;
    }

    public int getMsrColumnCount()
    {
        return msrColumnCount;
    }

    public void setMsrColumnCount(int msrColumnCount)
    {
        this.msrColumnCount = msrColumnCount;
    }

    public FileHolder getFileHolder()
    {
        return fileHolder;
    }

    public void setFileHolder(FileHolder fileHolder)
    {
        this.fileHolder = fileHolder;
    }

    public ScannedResultProcessor getScannedResultProcessor()
    {
        return scannedResultProcessor;
    }

    public void setScannedResultProcessor(ScannedResultProcessor scannedResultProcessor)
    {
        this.scannedResultProcessor = scannedResultProcessor;
    }

    public void setQueryId(String queryId)
    {
        this.queryId=queryId;
        
    }
    
    public String getQueryId()
    {
        return queryId;
    }

    public void setPartitionId(String partitionId)
    {
        this.partitionId=partitionId;
        
    }
    public String getPartitionId()
    {
        return partitionId;
    }
}
