/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwedLwWEET5JCCp2J65j3EiB2PJ4ohyqaGEDuXyJ
TTt3d2t/qYElqRMUl23tjFy8c9uoUqxZ+HuzSbcpDEeNrMsFv3Fi/N+a60pslR/0SxKt51ow
iKtAxQIiVZvoTNzTvgLLxOpiCQP26udd2/4eg1uW/T1Dw/Z+dQgbNwETGJ90XA==*/
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
package com.huawei.unibi.molap.engine.executer.pagination;

import java.util.List;
import java.util.Map;

import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;
import com.huawei.unibi.molap.engine.datastorage.InMemoryCube;
import com.huawei.unibi.molap.engine.directinterface.impl.MeasureSortModel;
import com.huawei.unibi.molap.engine.executer.impl.topn.TopNModel.MolapTopNType;
import com.huawei.unibi.molap.engine.executer.pagination.lru.LRUCacheKey;
import com.huawei.unibi.molap.engine.filters.measurefilter.GroupMeasureFilterModel;
import com.huawei.unibi.molap.engine.filters.metadata.InMemFilterModel;
import com.huawei.unibi.molap.keygenerator.KeyGenerator;
import com.huawei.unibi.molap.metadata.CalculatedMeasure;
import com.huawei.unibi.molap.metadata.MolapMetadata.Dimension;
import com.huawei.unibi.molap.metadata.MolapMetadata.Measure;
import com.huawei.unibi.molap.filter.MolapFilterInfo;

/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap Engine
 * Author R00900208
 * Created Date :21-May-2013 6:42:29 PM 
 * FileName :PaginationModel.java
 * Class Description : This class is responsible for holding pagination info
 * Version 1.0
 */
public class PaginationModel
{

    /**
     * keySize
     */
    private int keySize;
    
    /**
     * outLocation
     */
    private String outLocation; 
    
    /**
     * queryId
     */
    private String queryId;
    
    /**
     * fileBufferSize
     */
    private int fileBufferSize;
    
    /**
     * measureAggregators
     */
    private MeasureAggregator[] measureAggregators;
    
    /**
     * blockSize
     */
    private int blockSize;
    
    /**
     * groupMaskedBytes
     */
    private byte[] groupMaskedBytes;
    
    /**
     * maskedBytes
     */
    private byte[] maskedBytes;
    
    /**
     * topNdimIndexes
     */
    private int[] topNdimIndexes; 
    
    /**
     * topMeasureIndex
     */
    private int topMeasureIndex; 
    
    /**
     * topNCount
     */
    private int topNCount;
    
    /**
     * topNType
     */
    private MolapTopNType topNType;
    
    /**
     * aggName
     */
    private String aggName;
    
    /**
     * avgMsrIndex
     */
    private int avgMsrIndex; 
    
    /**
     * countMsrIndex
     */
    private int countMsrIndex;

    /**
     * msrConstraints
     */
    private GroupMeasureFilterModel[] msrConstraints;
    
    /**
     * msrConstraints
     */
    private GroupMeasureFilterModel[] msrConstraintsAfterTopN;
    
    /**
     * msrSortModel
     */
    private MeasureSortModel msrSortModel;
    
    /**
     * ResultSizeHolder
     */
    private LRUCacheKey holder;
    
    /**
     * total rowCount
     */
    private int rowCount;
	
	/**
     * holderSize
     */
    private int holderSize;
    
    /**
     * dimensionSortOrder
     */
    private byte[] dimensionSortOrder;
    
    /**
     * dimensionCompareIndex
     */
    private int[][] maskedByteRangeForSorting;
    
    /**
     * dimensionMasks
     */
    private byte[][] dimensionMasks;
    
    /**
     * maxKey
     */
    private byte[] maxKey;
    
    /**
     * maskedByteRange
     */
    private int[] maskedByteRange;
    
    /**
     * slices
     */
    private List<InMemoryCube> slices;
    
    /**
     * queryDims
     */
    private Dimension [] queryDims;
    
    /**
     * recordHolderType
     */
    private String recordHolderType;
    
    /**
     * keyGenerator
     */
    private KeyGenerator keyGenerator;
    
    /**
     * actualMaskByteRanges
     */
    private int[]  actualMaskByteRanges;
    
    /**
     * queryMsrs
     */
    private Measure[] queryMsrs;
    
    /**
     * calculatedMeasures
     */
    private CalculatedMeasure[] calculatedMeasures;
    
    /**
     * Pagination is enabled or not.
     */
    private boolean paginationEnabled;
    
    /**
     * Whether top cunt on calculated measure.
     */
    private boolean topCountOnCalcMeasure;
    
    /**
     * topNBytePos
     */
    private int[] topNMaskedBytesPos;
    
    /**
     * bytePos
     */
    private int[] topNGroupMaskedBytesPos;
    
    /**
     * measureIndexToRead
     */
    private int[] measureIndexToRead;
    
    /**
     * topNonColumn
     */
    private boolean topNOnColumn;
    
    
    private InMemFilterModel filterModelAfterTopN; 
    
    private int limit = -1;
    
    /**
     * Filter Constraints
     */
    private Map<Dimension, MolapFilterInfo> constraintsAfterTopN;

    private byte[] msrFilterMaskedBytes;

    private int[] msrFilterMaskedBytesPos;
    
    /**
     * @return the keySize
     */
    public int getKeySize()
    {
        return keySize;
    }

    /**
     * @param keySize the keySize to set
     */
    public void setKeySize(int keySize)
    {
        this.keySize = keySize;
    }

    /**
     * @return the outLocation
     */
    public String getOutLocation()
    {
        return outLocation;
    }

    /**
     * @param outLocation the outLocation to set
     */
    public void setOutLocation(String outLocation)
    {
        this.outLocation = outLocation;
    }

    /**
     * @return the queryId
     */
    public String getQueryId()
    {
        return queryId;
    }

    /**
     * @param queryId the queryId to set
     */
    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }

    /**
     * @return the fileBufferSize
     */
    public int getFileBufferSize()
    {
        return fileBufferSize;
    }

    /**
     * @param fileBufferSize the fileBufferSize to set
     */
    public void setFileBufferSize(int fileBufferSize)
    {
        this.fileBufferSize = fileBufferSize;
    }

    /**
     * @return the measureAggregators
     */
    public MeasureAggregator[] getMeasureAggregators()
    {
        return measureAggregators;
    }

    /**
     * @param measureAggregators the measureAggregators to set
     */
    public void setMeasureAggregators(MeasureAggregator[] measureAggregators)
    {
        this.measureAggregators = measureAggregators;
    }

    /**
     * @return the blockSize
     */
    public int getBlockSize()
    {
        return blockSize;
    }

    /**
     * @param blockSize the blockSize to set
     */
    public void setBlockSize(int blockSize)
    {
        this.blockSize = blockSize;
    }

    /**
     * @return the groupMaskedBytes
     */
    public byte[] getGroupMaskedBytes()
    {
        return groupMaskedBytes;
    }

    /**
     * @param groupMaskedBytes the groupMaskedBytes to set
     */
    public void setGroupMaskedBytes(byte[] groupMaskedBytes)
    {
        this.groupMaskedBytes = groupMaskedBytes;
    }

    /**
     * @return the maskedBytes
     */
    public byte[] getMaskedBytes()
    {
        return maskedBytes;
    }

    /**
     * @param maskedBytes the maskedBytes to set
     */
    public void setMaskedBytes(byte[] maskedBytes)
    {
        this.maskedBytes = maskedBytes;
    }
    
    /**
     * @param maskedBytes the maskedBytes to set
     */
    public void setMaskedBytesForMeasureFilter(byte[] msrFilterMaskedBytes)
    {
        this.msrFilterMaskedBytes = msrFilterMaskedBytes;
    }
    
    /**
     * @return the maskedBytes
     */
    public byte[] getMaskedBytesForMeasureFilter()
    {
        return msrFilterMaskedBytes;
    }

    /**
     * @return the topNdimIndexes
     */
    public int[] getTopNdimIndexes()
    {
        return topNdimIndexes;
    }

    /**
     * @param topNdimIndexes the topNdimIndexes to set
     */
    public void setTopNdimIndexes(int[] topNdimIndexes)
    {
        this.topNdimIndexes = topNdimIndexes;
    }

    /**
     * @return the topMeasureIndex
     */
    public int getTopMeasureIndex()
    {
        return topMeasureIndex;
    }

    /**
     * @param topMeasureIndex the topMeasureIndex to set
     */
    public void setTopMeasureIndex(int topMeasureIndex)
    {
        this.topMeasureIndex = topMeasureIndex;
    }

    /**
     * @return the topNCount
     */
    public int getTopNCount()
    {
        return topNCount;
    }

    /**
     * @param topNCount the topNCount to set
     */
    public void setTopNCount(int topNCount)
    {
        this.topNCount = topNCount;
    }

    /**
     * @return the topNType
     */
    public MolapTopNType getTopNType()
    {
        return topNType;
    }

    /**
     * @param topNType the topNType to set
     */
    public void setTopNType(MolapTopNType topNType)
    {
        this.topNType = topNType;
    }

    /**
     * @return the aggName
     */
    public String getAggName()
    {
        return aggName;
    }

    /**
     * @param aggName the aggName to set
     */
    public void setAggName(String aggName)
    {
        this.aggName = aggName;
    }

    /**
     * @return the avgMsrIndex
     */
    public int getAvgMsrIndex()
    {
        return avgMsrIndex;
    }

    /**
     * @param avgMsrIndex the avgMsrIndex to set
     */
    public void setAvgMsrIndex(int avgMsrIndex)
    {
        this.avgMsrIndex = avgMsrIndex;
    }

    /**
     * @return the countMsrIndex
     */
    public int getCountMsrIndex()
    {
        return countMsrIndex;
    }

    /**
     * @return the rowCount
     */
    public int getRowCount()
    {
        return rowCount;
    }

    /**
     * @param rowCount the rowCount to set
     */
    public void setRowCount(int rowCount)
    {
        this.rowCount = rowCount;
    }

    /**
     * @param countMsrIndex the countMsrIndex to set
     */
    public void setCountMsrIndex(int countMsrIndex)
    {
        this.countMsrIndex = countMsrIndex;
    }

  
    /**
     * Get MeasureSortModel
     * 
     * @return msrSortModel
     */
    public MeasureSortModel getMsrSortModel()
    {
        return msrSortModel;
    }

    /**
     * Set MeasureSortModel
     * 
     * @param msrSortModel
     */
    public void setMsrSortModel(MeasureSortModel msrSortModel)
    {
        this.msrSortModel = msrSortModel;
    }

    /**
     * @return the holder
     */
    public LRUCacheKey getHolder()
    {
        return holder;
    }

    /**
     * @param holder the holder to set
     */
    public void setHolder(LRUCacheKey holder)
    {
        this.holder = holder;
    }
	
    /**
     * get Dimension Sort Order
     * @return the dimensionSortOrder
     */
    public byte[] getDimensionSortOrder()
    {
        return dimensionSortOrder;
    }

    /**
     * the dimension Sort Order to set
     * @param dimensionSortOrder the dimensionSortOrder to set
     */
    public void setDimensionSortOrder(byte[] dimensionSortOrder)
    {
        this.dimensionSortOrder = dimensionSortOrder;
    }
    
    /**
     * holde rSize
     * @return the holderSize
     */
    public int getHolderSize()
    {
        return holderSize;
    }

    /**
     * the holderSize to set
     * @param holderSize the holderSize to set
     */
    public void setHolderSize(int holderSize)
    {
        this.holderSize = holderSize;
    }

    /**
     * get Dimension Masks
     * @return the dimensionMasks
     */
    public byte[][] getDimensionMasks()
    {
        return dimensionMasks;
    }

    /**
     * the dimensionMasks to set
     * @param dimensionMasks 
     */
    public void setDimensionMasks(byte[][] dimensionMasks)
    {
        this.dimensionMasks = dimensionMasks;
    }

    /**
     * @return the maxKey
     */
    public byte[] getMaxKey()
    {
        return maxKey;
    }

    /**
     * @param maxKey the maxKey to set
     */
    public void setMaxKey(byte[] maxKey)
    {
        this.maxKey = maxKey;
    }

    /**
     * @return the maskedByteRange
     */
    public int[] getMaskedByteRange()
    {
        return maskedByteRange;
    }

    /**
     * @param maskedByteRange the maskedByteRange to set
     */
    public void setMaskedByteRange(int[] maskedByteRange)
    {
        this.maskedByteRange = maskedByteRange;
    }

    /**
     * @return the maskedByteRangeForSorting
     */
    public int[][] getMaskedByteRangeForSorting()
    {
        return maskedByteRangeForSorting;
    }

    /**
     * @param maskedByteRangeForSorting the maskedByteRangeForSorting to set
     */
    public void setMaskedByteRangeForSorting(int[][] maskedByteRangeForSorting)
    {
        this.maskedByteRangeForSorting = maskedByteRangeForSorting;
    }

    /**
     * @return the slices
     */
    public List<InMemoryCube> getSlices()
    {
        return slices;
    }

    /**
     * @param slices the slices to set
     */
    public void setSlices(List<InMemoryCube> slices)
    {
        this.slices = slices;
    }

    /**
     * @return the queryDims
     */
    public Dimension[] getQueryDims()
    {
        return queryDims;
    }

    /**
     * @param queryDims the queryDims to set
     */
    public void setQueryDims(Dimension[] queryDims)
    {
        this.queryDims = queryDims;
    }

    /**
     * @return the recordHolderType
     */
    public String getRecordHolderType()
    {
        return recordHolderType;
    }

    /**
     * @param recordHolderType the recordHolderType to set
     */
    public void setRecordHolderType(String recordHolderType)
    {
        this.recordHolderType = recordHolderType;
    }

    /**
     * @return the keyGenerator
     */
    public KeyGenerator getKeyGenerator()
    {
        return keyGenerator;
    }

    /**
     * @param keyGenerator the keyGenerator to set
     */
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }

    /**
     * @return the actualMaskByteRanges
     */
    public int[] getActualMaskByteRanges()
    {
        return actualMaskByteRanges;
    }

    /**
     * @param actualMaskByteRanges the actualMaskByteRanges to set
     */
    public void setActualMaskByteRanges(int[] actualMaskByteRanges)
    {
        this.actualMaskByteRanges = actualMaskByteRanges;
    }

    /**
     * @return the queryMsrs
     */
    public Measure[] getQueryMsrs()
    {
        return queryMsrs;
    }

    /**
     * @param queryMsrs the queryMsrs to set
     */
    public void setQueryMsrs(Measure[] queryMsrs)
    {
        this.queryMsrs = queryMsrs;
    }

    /**
     * @return the calculatedMeasures
     */
    public CalculatedMeasure[] getCalculatedMeasures()
    {
        return calculatedMeasures;
    }

    /**
     * @param calculatedMeasures the calculatedMeasures to set
     */
    public void setCalculatedMeasures(CalculatedMeasure[] calculatedMeasures)
    {
        this.calculatedMeasures = calculatedMeasures;
    }

    /**
     * @return the paginationEnabled
     */
    public boolean isPaginationEnabled()
    {
        return paginationEnabled;
    }

    /**
     * @param paginationEnabled the paginationEnabled to set
     */
    public void setPaginationEnabled(boolean paginationEnabled)
    {
        this.paginationEnabled = paginationEnabled;
    }

    /**
     * @return the topCountOnCalcMeasure
     */
    public boolean isTopCountOnCalcMeasure()
    {
        return topCountOnCalcMeasure;
    }

    /**
     * @param topCountOnCalcMeasure the topCountOnCalcMeasure to set
     */
    public void setTopCountOnCalcMeasure(boolean topCountOnCalcMeasure)
    {
        this.topCountOnCalcMeasure = topCountOnCalcMeasure;
    }

    /**
     * @return the msrConstraints
     */
    public GroupMeasureFilterModel[] getMsrConstraints()
    {
        return msrConstraints;
    }

    /**
     * @param msrConstraints the msrConstraints to set
     */
    public void setMsrConstraints(GroupMeasureFilterModel[] msrConstraints)
    {
        this.msrConstraints = msrConstraints;
    }

    /**
     * @return the topNMaskedBytesPos
     */
    public int[] getTopNMaskedBytesPos()
    {
        return topNMaskedBytesPos;
    }

    /**
     * @param topNMaskedBytesPos the topNMaskedBytesPos to set
     */
    public void setTopNMaskedBytesPos(int[] topNMaskedBytesPos)
    {
        this.topNMaskedBytesPos = topNMaskedBytesPos;
    }
    
    /**
     * @param topNMaskedBytesPos the topNMaskedBytesPos to set
     */
    public void setMsrFilterMaskedBytesPos(int[] msrFilterMaskedBytesPos)
    {
        this.msrFilterMaskedBytesPos = msrFilterMaskedBytesPos;
    }
    
    public int[]  getMsrFilterMaskedBytesPos()
    {
        return  msrFilterMaskedBytesPos;
    }


    /**
     * @return the topNGroupMaskedBytesPos
     */
    public int[] getTopNGroupMaskedBytesPos()
    {
        return topNGroupMaskedBytesPos;
    }

    /**
     * @param topNGroupMaskedBytesPos the topNGroupMaskedBytesPos to set
     */
    public void setTopNGroupMaskedBytesPos(int[] topNGroupMaskedBytesPos)
    {
        this.topNGroupMaskedBytesPos = topNGroupMaskedBytesPos;
    }

    /**
     * @return the measureIndexToRead
     */
    public int[] getMeasureIndexToRead()
    {
        return measureIndexToRead;
    }

    /**
     * @param measureIndexToRead the measureIndexToRead to set
     */
    public void setMeasureIndexToRead(int[] measureIndexToRead)
    {
        this.measureIndexToRead = measureIndexToRead;
    }

    /**
     * @return the topNOnColumn
     */
    public boolean isTopNOnColumn()
    {
        return topNOnColumn;
    }

    /**
     * @param topNOnColumn the topNOnColumn to set
     */
    public void setTopNOnColumn(boolean topNOnColumn)
    {
        this.topNOnColumn = topNOnColumn;
    }

    /**
     * @return the filterModelAfterTopN
     */
    public InMemFilterModel getFilterModelAfterTopN()
    {
        return filterModelAfterTopN;
    }

    /**
     * @param filterModelAfterTopN the filterModelAfterTopN to set
     */
    public void setFilterModelAfterTopN(InMemFilterModel filterModelAfterTopN)
    {
        this.filterModelAfterTopN = filterModelAfterTopN;
    }

    /**
     * @return the msrConstraintsAfterTopN
     */
    public GroupMeasureFilterModel[] getMsrConstraintsAfterTopN()
    {
        return msrConstraintsAfterTopN;
    }

    /**
     * @param msrConstraintsAfterTopN the msrConstraintsAfterTopN to set
     */
    public void setMsrConstraintsAfterTopN(GroupMeasureFilterModel[] msrConstraintsAfterTopN)
    {
        this.msrConstraintsAfterTopN = msrConstraintsAfterTopN;
    }

    /**
     * @return the constraintsAfterTopN
     */
    public Map<Dimension, MolapFilterInfo> getConstraintsAfterTopN()
    {
        return constraintsAfterTopN;
    }

    /**
     * @param constraintsAfterTopN the constraintsAfterTopN to set
     */
    public void setConstraintsAfterTopN(Map<Dimension, MolapFilterInfo> constraintsAfterTopN)
    {
        this.constraintsAfterTopN = constraintsAfterTopN;
    }

    /**
     * @return the limit
     */
    public int getLimit()
    {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(int limit)
    {
        this.limit = limit;
    }
}
