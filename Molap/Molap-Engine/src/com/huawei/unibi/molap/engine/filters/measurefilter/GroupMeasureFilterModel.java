/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcz8AOhvEHjQfa55oxvUSJWRQCwLl+VwWEHaV7n
0eFj3VSaYQFeEAVxDLHuw9Os9rXpS/bw+bkBeUnzGVJ/CUnxgjWnZRbULjDtDJNF+bqhv0Am
jQeqccwr1Lz7zzdkx3PoKPPQB+ERDD6aA9R+qkUo8BMtfv2OZgWcUqGCfmYstg==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * 
 */
package com.huawei.unibi.molap.engine.filters.measurefilter;

/**
 * It is group filter models used to evaluate the filters in group conditions like AND, OR
 * @author R00900208
 *
 */
public class GroupMeasureFilterModel extends MeasureFilterModel
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1262669317890067272L;

    private MeasureFilterModel[][] filterModels;
    
    private MeasureFilterGroupType filterGroupType;

    /**
     * GroupMeasureFilterModel
     * @param filterModels
     * @param filterGroupType
     */
    public GroupMeasureFilterModel(MeasureFilterModel[][] filterModels,MeasureFilterGroupType filterGroupType)
    {
        this.filterModels = filterModels;
        this.filterGroupType = filterGroupType;
    }


    /**
     * getFilterModels
     * @return the filterModels
     */
    public MeasureFilterModel[][] getFilterModels()
    {
        return filterModels;
    }
    
    
    /**
     * getFilterGroupType
     * @return the filterGroupType
     */
    public MeasureFilterGroupType getFilterGroupType()
    {
        return filterGroupType;
    }


    /**
     * This enum for measure filter group types.
     * @author R00900208
     *
     */
    public static enum MeasureFilterGroupType
    {
        /**
         * Enums declared for Metric filters inorder to process and 
         * and or operarations.
         */
        AND,OR;
    }
    
}
