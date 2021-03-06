/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwedLwWEET5JCCp2J65j3EiB2PJ4ohyqaGEDuXyJ
TTt3dwU41DMy77+Sd+rIg6o2YLGqoNe5Uyu94p9q/uWqSb1bSf65TOwu9s4lmXsg/Bil6S48
DKWbmyRO60FPLVXxxh+LEPCLbNDWv+suDrEbpNg3guvkNsfJoeOItctfCWKRlw==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * 
 */
package com.huawei.unibi.molap.engine.filters.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.unibi.molap.engine.filters.likefilters.FilterLikeExpressionIntf;
import com.huawei.unibi.molap.filter.MolapFilterInfo;

/**
 * This class is used for content match of level string.
 * 
 * @author R00900208
 *
 */
public class ContentMatchFilterInfo extends MolapFilterInfo
{
    /**
     * excludedContentMatchMembers.
     */
    private List<String> excludedContentMatchMembers;
    /**
     * includedContentMatchMembers.
     */
    private List<String> includedContentMatchMembers;
    
    /**
     * dimFilterMap
     */
    private Map<Integer, ContentMatchFilterInfo> dimFilterMap = new HashMap<Integer, ContentMatchFilterInfo>(16);
    /**
     * Include like filters.
     * Ex: select employee_name,department_name,sum(salary) from employee where employee_name like ("a","b");
     * then "a" and "b" will be the include like filters.
     */
    private List<FilterLikeExpressionIntf> likeFilterExpressions;
    
    /**
     * @return the excludedContentMatchMembers
     */
    public List<String> getExcludedContentMatchMembers()
    {
        return excludedContentMatchMembers;
    }
    /**
     * @param excludedContentMatchMembers the excludedContentMatchMembers to set
     */
    public void setExcludedContentMatchMembers(List<String> excludedContentMatchMembers)
    {
        this.excludedContentMatchMembers = excludedContentMatchMembers;
    }
    /**
     * @return the includedContentMatchMembers
     */
    public List<String> getIncludedContentMatchMembers()
    {
        return includedContentMatchMembers;
    }
    /**
     * @param includedContentMatchMembers the includedContentMatchMembers to set
     */
    public void setIncludedContentMatchMembers(List<String> includedContentMatchMembers)
    {
        this.includedContentMatchMembers = includedContentMatchMembers;
    }
    
    /**
     * @param includeLikeFilter the includeLikeFilter to set
     */
    public void setLikeFilterExpression(List<FilterLikeExpressionIntf> listOfFilterLikeExpressionIntf) 
    {
        this.likeFilterExpressions=listOfFilterLikeExpressionIntf;
    }

    /**
     * @return the excludeLikeFilter
     */
    public List<FilterLikeExpressionIntf> getLikeFilterExpression() 
    {
        return likeFilterExpressions;
    }
    
    /**
     * hashCode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int res = 1; 
        res = prime * res
                + ((excludedMembers == null) ? 0 : excludedMembers.hashCode());
        res = prime * res
                + ((includedMembers == null) ? 0 : includedMembers.hashCode());
        return res;
    }
    
    /**
     * equals
     */
    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof ContentMatchFilterInfo)
        {
            
            if (this == obj)
            {
                return true;
            }
            
            
            ContentMatchFilterInfo other = (ContentMatchFilterInfo) obj;
            
            if (excludedMembers == null) 
            {
                if (other.excludedMembers != null)
                {
                    return false;
                }
            } 
            else if (!excludedMembers.equals(other.excludedMembers))
            {
                return false;
            }
            if (includedMembers == null) 
            {
                if (other.includedMembers != null)
                {
                    return false;
                }
            } 
            else if (!includedMembers.equals(other.includedMembers))
            {
                return false;
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * @return the dimFilterMap
     */
    public Map<Integer, ContentMatchFilterInfo> getDimFilterMap()
    {
        return dimFilterMap;
    }
    /**
     * @param dimFilterMap the dimFilterMap to set
     */
    public void setDimFilterMap(Map<Integer, ContentMatchFilterInfo> dimFilterMap)
    {
        this.dimFilterMap = dimFilterMap;
    }

}
