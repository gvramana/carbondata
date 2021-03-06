/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcAIRTtLWBkMMN+iqJ62JNQb/MYFaBoemC1VlrU
n+vkOVMc9CMCKDLKxuyZpL3JTwirW4zLhA+EDfYjPl68dd0XmITp3rUgMKRxOBkQ6tMAXLLl
lvADaY3Hpxy78eGT/JtOMVb2ThYBiksM/otbo4Pwdl9z/SSqHDWpk6vuYtBpLA==*/
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
package com.huawei.unibi.molap.engine.directinterface.impl;

import java.util.List;
import java.util.Map;

import com.huawei.unibi.molap.engine.executer.impl.topn.TopNModel;
import com.huawei.unibi.molap.engine.filters.measurefilter.MeasureFilterModel;
import com.huawei.unibi.molap.metadata.CalculatedMeasure;
import com.huawei.unibi.molap.metadata.MolapMetadata;
import com.huawei.unibi.molap.metadata.MolapMetadata.Cube;
import com.huawei.unibi.molap.metadata.MolapMetadata.Dimension;
import com.huawei.unibi.molap.metadata.MolapMetadata.Measure;
import com.huawei.unibi.molap.query.metadata.DSLTransformation;
import com.huawei.unibi.molap.filter.MolapFilterInfo;

/**
 * It is model object for molap query
 * @author R00900208
 *
 */
public class MolapQueryModel
{
    /**
     * cube
     */
    private Cube cube;
    
    /**
     * factTableName
     */
    private String factTableName;
    /**
     * queryDims
     */
    private List<MolapMetadata.Dimension> queryDims;
    /**
     * cube
     */
    private List<Measure> msrs;
    /**
     * cube
     */
    private Map<Measure,MeasureFilterModel[]> msrFilter;
    /**
     * constraints
     */
    private Map<Dimension, MolapFilterInfo> constraints;
    
    /**
     * msrFilterAfterTopN
     */
    private Map<Measure,MeasureFilterModel[]> msrFilterAfterTopN;
    /**
     * constraintsAfterTopN
     */
    private Map<Dimension, MolapFilterInfo> constraintsAfterTopN;
    
    /**
     * dimSortTypes
     */
    private byte[] dimSortTypes;
    /**
     * topNModel
     */
    private TopNModel topNModel;
    /**
     * queryDimsRows
     */
    private List<MolapMetadata.Dimension> queryDimsRows;
    /**
     * queryDimsCols
     */
    private List<MolapMetadata.Dimension> queryDimsCols;
    
    /**
     * dimSortTypes
     */
    private byte[] globalDimSortTypes;
    
    /**
     * queryDimsRows
     */
    private List<MolapMetadata.Dimension> globalQueryDimsRows;
    /**
     * queryDimsCols
     */
    private List<MolapMetadata.Dimension> globalQueryDimsCols;
    
    /**
     * queryDims including the dynamic columns
     */
    private List<MolapMetadata.Dimension> globalQueryDims;
    /**
     * sortModel
     */
    private MeasureSortModel sortModel;
    
    /**
     *  When it set as true then user needs to provide the filters exactly with there parent members.
     */
    private boolean exactLevelsMatch;
    
    /**
     * Whether pagination required or not
     */
    private boolean paginationRequired;
    
    /**
     * Row range for pagination.
     */
    private int[] rowRange;
    
    /**
     * Unique query ID
     */
    private String queryId;
    
    /**
     * Calculated measures
     */
    private List<CalculatedMeasure> calcMsrs;
    
    /**
     * Grand total enabled
     */
    private boolean grandTotalForAllRows;
    
    /**
     * relativefilter
     */
    private boolean relativefilter;

    /**
     * pushTopNInOlap
     */
    private boolean pushTopNInOlap;
    
    /**
     * isAnalyzer
     */
    private boolean isAnalyzer;
    
    /**
     * isSubTotal
     */
    private boolean isSubTotal;
 
    /**
     * isPresent.
     */
    private boolean isPresent;
    private List<DSLTransformation> molapTransformations;

    /**
     * @return the cube
     */
    public Cube getCube()
    {
        return cube;
    }

    /**
     * @param cube the cube to set
     */
    public void setCube(Cube cube)
    {
        this.cube = cube;
    }

    /**
     * @return the factTableName
     */
    public String getFactTableName()
    {
        return factTableName;
    }

    /**
     * @param factTableName the factTableName to set
     */
    public void setFactTableName(String factTableName)
    {
        this.factTableName = factTableName;
    }

    /**
     * @return the queryDims
     */
    public List<MolapMetadata.Dimension> getQueryDims()
    {
        return queryDims;
    }

    /**
     * @param queryDims the queryDims to set
     */
    public void setQueryDims(List<MolapMetadata.Dimension> queryDims)
    {
        this.queryDims = queryDims;
    }

    /**
     * @return the msrs
     */
    public List<Measure> getMsrs()
    {
        return msrs;
    }

    /**
     * @param msrs the msrs to set
     */
    public void setMsrs(List<Measure> msrs)
    {
        this.msrs = msrs;
    }

    /**
     * @return the msrFilter
     */
    public Map<Measure,MeasureFilterModel[]> getMsrFilter()
    {
        return msrFilter;
    }

    /**
     * @param msrFilter the msrFilter to set
     */
    public void setMsrFilter(Map<Measure,MeasureFilterModel[]> msrFilter)
    {
        this.msrFilter = msrFilter;
    }

    /**
     * @return the constraints
     */
    public Map<Dimension, MolapFilterInfo> getConstraints()
    {
        return constraints;
    }

    /**
     * @param constraints the constraints to set
     */
    public void setConstraints(Map<Dimension, MolapFilterInfo> constraints)
    {
        this.constraints = constraints;
    }

    /**
     * @return the dimSortTypes
     */
    public byte[] getDimSortTypes()
    {
        return dimSortTypes;
    }

    /**
     * @param dimSortTypes the dimSortTypes to set
     */
    public void setDimSortTypes(byte[] dimSortTypes)
    {
        this.dimSortTypes = dimSortTypes;
    }

    /**
     * @return the topNModel
     */
    public TopNModel getTopNModel()
    {
        return topNModel;
    }

    /**
     * @param topNModel the topNModel to set
     */
    public void setTopNModel(TopNModel topNModel)
    {
        this.topNModel = topNModel;
    }

    /**
     * @return the queryDimsRows
     */
    public List<MolapMetadata.Dimension> getQueryDimsRows()
    {
        return queryDimsRows;
    }

    /**
     * @param queryDimsRows the queryDimsRows to set
     */
    public void setQueryDimsRows(List<MolapMetadata.Dimension> queryDimsRows)
    {
        this.queryDimsRows = queryDimsRows;
    }

    /**
     * @return the queryDimsCols
     */
    public List<MolapMetadata.Dimension> getQueryDimsCols()
    {
        return queryDimsCols;
    }

    /**
     * @param queryDimsCols the queryDimsCols to set
     */
    public void setQueryDimsCols(List<MolapMetadata.Dimension> queryDimsCols)
    {
        this.queryDimsCols = queryDimsCols;
    }

    /**
     * @return the sortModel
     */
    public MeasureSortModel getSortModel()
    {
        return sortModel;
    }

    /**
     * @param sortModel the sortModel to set
     */
    public void setSortModel(MeasureSortModel sortModel)
    {
        this.sortModel = sortModel;
    }

    /**
     * @return the exactLevelsMatch
     */
    public boolean isExactLevelsMatch()
    {
        return exactLevelsMatch;
    }

    /**
     * @param exactLevelsMatch the exactLevelsMatch to set
     */
    public void setExactLevelsMatch(boolean exactLevelsMatch)
    {
        this.exactLevelsMatch = exactLevelsMatch;
    }

    /**
     * @return the paginationRequired
     */
    public boolean isPaginationRequired()
    {
        return paginationRequired;
    }

    /**
     * @param paginationRequired the paginationRequired to set
     */
    public void setPaginationRequired(boolean paginationRequired)
    {
        this.paginationRequired = paginationRequired;
    }

    /**
     * @return the rowRange
     */
    public int[] getRowRange()
    {
        return rowRange;
    }

    /**
     * @param rowRange the rowRange to set
     */
    public void setRowRange(int[] rowRange)
    {
        this.rowRange = rowRange;
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
     * @return the calcMsrs
     */
    public List<CalculatedMeasure> getCalcMsrs()
    {
        return calcMsrs;
    }

    /**
     * @param calcMsrs the calcMsrs to set
     */
    public void setCalcMsrs(List<CalculatedMeasure> calcMsrs)
    {
        this.calcMsrs = calcMsrs;
    }

    /**
     * @return the msrFilterAfterTopN
     */
    public Map<Measure, MeasureFilterModel[]> getMsrFilterAfterTopN()
    {
        return msrFilterAfterTopN;
    }

    /**
     * @param msrFilterAfterTopN the msrFilterAfterTopN to set
     */
    public void setMsrFilterAfterTopN(Map<Measure, MeasureFilterModel[]> msrFilterAfterTopN)
    {
        this.msrFilterAfterTopN = msrFilterAfterTopN;
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
     * @return the grandTotalForAllRows
     */
    public boolean isGrandTotalForAllRows()
    {
        return grandTotalForAllRows;
    }

    /**
     * @param grandTotalForAllRows the grandTotalForAllRows to set
     */
    public void setGrandTotalForAllRows(boolean grandTotalForAllRows)
    {
        this.grandTotalForAllRows = grandTotalForAllRows;
    }

    /**
     * @return the relativefilter
     */
    public boolean isRelativefilter()
    {
        return relativefilter;
    }

    /**
     * @param relativefilter the relativefilter to set
     */
    public void setRelativefilter(boolean relativefilter)
    {
        this.relativefilter = relativefilter;
    }

    public void pushTopNToOlapEngine(boolean pushTopNInOlap) 
    {
        this.pushTopNInOlap=pushTopNInOlap;
        
    }
    
    public boolean pushTopN() 
    {
       return pushTopNInOlap;
        
    }

    /**
     * @return the isAnalyzer
     */
    public boolean isAnalyzer()
    {
        return isAnalyzer;
    }

    /**
     * @param isAnalyzer the isAnalyzer to set
     */
    public void setAnalyzer(boolean isAnalyzer)
    {
        this.isAnalyzer = isAnalyzer;
    }

    public boolean isSubTotal()
    {
        return isSubTotal;
    }

    public void setSubTotal(boolean isSubTotal)
    {
        this.isSubTotal = isSubTotal;
    }

    /**
     * 
     * @Author s71955
     * @Description : setIsSliceFilterPresent
     * @param isPresent
     */
    public void setIsSliceFilterPresent(boolean isPresent)
    {
        this.isPresent=isPresent;
        
    }
    /**
     * 
     * @Author s71955
     * @Description : isSliceFilterPresent
     * @return
     */
    public boolean isSliceFilterPresent()
    {
        return isPresent;
        
    }
    /**
     * @return the molapTransformations
     */
    public List<DSLTransformation> getMolapTransformations()
    {
        return molapTransformations;
    }

    /**
     * @param molapTransformations the molapTransformations to set
     */
    public void setMolapTransformations(List<DSLTransformation> molapTransformations)
    {
        this.molapTransformations = molapTransformations;
    }

    /**
     * 
     * @return Returns the globalDimSortTypes.
     * 
     */
    public byte[] getGlobalDimSortTypes()
    {
        return globalDimSortTypes;
    }

    /**
     * 
     * @param globalDimSortTypes The globalDimSortTypes to set.
     * 
     */
    public void setGlobalDimSortTypes(byte[] globalDimSortTypes)
    {
        this.globalDimSortTypes = globalDimSortTypes;
    }

    /**
     * 
     * @return Returns the globalQueryDimsRows.
     * 
     */
    public List<MolapMetadata.Dimension> getGlobalQueryDimsRows()
    {
        return globalQueryDimsRows;
    }

    /**
     * 
     * @param globalQueryDimsRows The globalQueryDimsRows to set.
     * 
     */
    public void setGlobalQueryDimsRows(List<MolapMetadata.Dimension> globalQueryDimsRows)
    {
        this.globalQueryDimsRows = globalQueryDimsRows;
    }

    /**
     * 
     * @return Returns the globalQueryDimsCols.
     * 
     */
    public List<MolapMetadata.Dimension> getGlobalQueryDimsCols()
    {
        return globalQueryDimsCols;
    }

    /**
     * 
     * @param globalQueryDimsCols The globalQueryDimsCols to set.
     * 
     */
    public void setGlobalQueryDimsCols(List<MolapMetadata.Dimension> globalQueryDimsCols)
    {
        this.globalQueryDimsCols = globalQueryDimsCols;
    }

    /**
     * 
     * @return Returns the globalQueryDims.
     * 
     */
    public List<MolapMetadata.Dimension> getGlobalQueryDims()
    {
        return globalQueryDims;
    }

    /**
     * 
     * @param globalQueryDims The globalQueryDims to set.
     * 
     */
    public void setGlobalQueryDims(List<MolapMetadata.Dimension> globalQueryDims)
    {
        this.globalQueryDims = globalQueryDims;
    }

}
