/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwddts1/q4bCGDA4M3dH8C2PEEMnfDqqdF4ZhcSc
1BeEnCZR6DAWCqpnKz3Auiiy9Kih54SB6tFwGDQYjqsGeeDTNlRATcSAJbvvqEQztfeuGjQF
KUD/3DkSA7WxvXeK5D5+ilerTevQPwnB3Wl7yO78Ih9z7h+myVfdsKbFQiQhYw==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2011
 * =====================================
 *
 */
package com.huawei.unibi.molap.schema.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name NSE V3R7C00 
 * Module Name : MOLAP
 * Author :C00900810
 * Created Date :24-Jun-2013
 * FileName : AggregateTable.java
 * Class Description : 
 * Version 1.0
 */
public class AggregateTable
{
    
    /**
     * aggregateTableName
     */
    private String aggregateTableName;
    
    /**
     * aggLevels
     */
    private String[] aggLevels;
    
    /**
     * aggLevels
     */
    private String[] aggLevelsActualName;
    
    /**
     * actualAggLevels
     */
    private String[] actualAggLevels;

    /**
     * aggMeasure
     */
    private String[] aggMeasure;
    
    /**
     * aggregator
     */
    private String[] aggregator;
    
    /**
     * Agg Namesssss
     */
    private String[] aggNames;
    
    /**
     * aggColuName
     */
    private String[] aggColuName;
    
    /**
     * aggregateClass
     */
    private String[] aggregateClass;

	//private String tableNameForAggr;
	
	private List<AggregateTable> dependentAggTables=new ArrayList<AggregateTable>(10);
    

	public List<AggregateTable> getDependentAggTables() {
		return dependentAggTables;
	}
	public void setDependentAggTables(List<AggregateTable> dependentAggTables) {
		this.dependentAggTables = dependentAggTables;
	}
	/**
     * @return
     */
    public String getAggregateTableName()
    {
        return aggregateTableName;
    }
    /**
     * @param aggregateTableName
     */
    public void setAggregateTableName(String aggregateTableName)
    {
        this.aggregateTableName = aggregateTableName;
    }
    /**
     * @return
     */
    public String[] getAggLevels()
    {
        return aggLevels;
    }
    /**
     * @param aggLevels
     */
    public void setAggLevels(String[] aggLevels)
    {
        this.aggLevels = aggLevels;
    }
    /**
     * @return
     */
    public String[] getAggMeasure()
    {
        return aggMeasure;
    }
    /**
     * @param aggMeasure
     */
    public void setAggMeasure(String[] aggMeasure)
    {
        this.aggMeasure = aggMeasure;
    }
    /**
     * @return
     */
    public String[] getAggregator()
    {
        return aggregator;
    }
    /**
     * @param aggregator
     */
    public void setAggregator(String[] aggregator)
    {
        this.aggregator = aggregator;
    }

    /**
     * 
     * @return Returns the actualAggLevels.
     * 
     */
    public String[] getActualAggLevels()
    {
        return actualAggLevels;
    }

    /**
     * 
     * @param actualAggLevels The actualAggLevels to set.
     * 
     */
    public void setActualAggLevels(String[] actualAggLevels)
    {
        this.actualAggLevels = actualAggLevels;
    }
    /**
     * 
     * @return Returns the aggNames.
     * 
     */
    public String[] getAggNames()
    {
        return aggNames;
    }
    /**
     * 
     * @param aggNames The aggNames to set.
     * 
     */
    public void setAggNames(String[] aggNames)
    {
        this.aggNames = aggNames;
    }
    /**
     * @return the aggregateClass
     */
    public String[] getAggregateClass()
    {
        return aggregateClass;
    }
    /**
     * @param aggregateClass the aggregateClass to set
     */
    public void setAggregateClass(String[] aggregateClass)
    {
        this.aggregateClass = aggregateClass;
    }
    /**
     * @return the aggLevelsActualName
     */
    public String[] getAggLevelsActualName()
    {
        return aggLevelsActualName;
    }
    /**
     * @param aggLevelsActualName the aggLevelsActualName to set
     */
    public void setAggLevelsActualName(String[] aggLevelsActualName)
    {
        this.aggLevelsActualName = aggLevelsActualName;
    }
	/**
	 * @return the aggColuName
	 */
	public String[] getAggColuName() {
		return aggColuName;
	}
	/**
	 * @param aggColuName the aggColuName to set
	 */
	public void setAggColuName(String[] aggColuName) {
		this.aggColuName = aggColuName;
	}
	/*public void setTableNameForAggregate(String tableNameForAggr) {
		this.tableNameForAggr=tableNameForAggr;
		
	}*/

}
