package com.huawei.unibi.molap.engine.querystats;

public final class Preference
{
    private Preference()
    {
        
    }
  //distinct relationship will not be calculated for dimension with cardinality <= this 
    public static final int IGNORE_CARDINALITY = 1;

    /**
     * parameter to decide whether to consider given combination as part of aggregate table
     * if benfit ratio is less than configured than ignore it else consider it
     */
    public static final int BENEFIT_RATIO=10;
    
    //molap configuration properties
    //sample load to consider for sampling
    public static final String AGG_LOAD_COUNT="molap.agg.loadCount";
    //no of partition to consider for sampling
    public static final String AGG_PARTITION_COUNT="molap.agg.partitionCount";
    // no of fact per load to consider for sampling
    public static final String AGG_FACT_COUNT="molap.agg.factCount";
    // no of record per fact to consider for sampling
    public static final String AGG_REC_COUNT="molap.agg.recordCount";
    //maximum aggregate combination suggestion
    public static final int AGG_COMBINATION_SIZE = 100;
    
    //query stats file saved on store
    public static final String QUERYSTATS_FILE_NAME="queryStats";
    
    //directory where all files will be saved
    public static final String AGGREGATE_STORE_DIR="aggsuggestion";
    //distinct relationship for data stats will be saved in this file
    public static final String DATASTATS_DISTINCT_FILE_NAME="distinctData";
    
    //0->dimensions, 1->measures,2->cubename
    public static final String AGGREGATE_TABLE_SCRIPT="CREATE AGGREGATETABLE {0}{1} from cube {2}";
    
    //aggregate combination for data stats will be saved in this file
    public static final String DATA_STATS_FILE_NAME = "dataStats";
    
    //performance goal in sec
    public static final int PERFORMANCE_GOAL=3;
    
    public static final int QUERY_EXPIRY_DAYS=30;
    
    /**
     * is it required to cache data stats suggestion
     */
    public static final String DATA_STATS_SUGG_CACHE="molap.agg.datastats.cache";
    
    public static final String PERFORMANCE_GOAL_KEY="molap.agg.query.performance.goal";
    
    public static final String QUERY_STATS_EXPIRY_DAYS_KEY="molap.agg.querystats.expiryday";
    
    public static final String BENEFIT_RATIO_KEY="molap.agg.benefit.ratio";
}
