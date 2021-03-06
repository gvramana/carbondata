/**
 * Project Name: NSE V300R008C10 UniBI
 * FileName : UpdateOlapSchemaCommadHandler.java
 *
 * =============================Copyright Notice ==============================
 *  This file contains proprietary information of Huawei Technologies Co. Ltd. 
 *  Copying or reproduction without prior written approval is prohibited. 
 *  Copyright (c) 2015;
 * ============================================================================
 */
package com.huawei.unibi.molap.engine.directinterface.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.unibi.molap.engine.filters.metadata.ContentMatchFilterInfo;
import com.huawei.unibi.molap.metadata.MolapMetadata.Cube;
import com.huawei.unibi.molap.metadata.MolapMetadata.Dimension;
import com.huawei.unibi.molap.query.metadata.MolapDimensionLevelFilter;
import com.huawei.unibi.molap.filter.MolapFilterInfo;
/**
 * 
 * Module Name : MOLAP 
 * Author M00903915
 * Created Date :05-Jan-2015 8:05:45 PM
 * 
 * Class Description : This class is used to get the filter applied at the each dimension level.
 * Version 1.0
 */
public class MolapFilterWrapper
{
    /**
     * 
     */
    private List<Wrapper> wrapperList;
    
    /**
     * deafault consstructor to inititialize the cunstructer.
     */
    public MolapFilterWrapper()
    {
        this.wrapperList= new ArrayList<Wrapper>(10);
    }
    
    /**
     * the method is used to construct the wrapperList 
     * @Author M00903915
     * @Description : addDimensionAndFilter
     * @param dim instance of dimension
     * @param filterValue instance of MolapDimensionLevelFilter
     */
    public void addDimensionAndFilter(Dimension dim, MolapDimensionLevelFilter filterValue)
    {
       Wrapper wrapper = new Wrapper();
       wrapper.dimension=dim;
       wrapper.filter=filterValue;
       this.wrapperList.add(wrapper);
    }

    /**
     * 
     * @param cube 
     * @param b 
     * @Author M00903915
     * @Description : getFilters
     * @return The method returns the Map of the filters for each dimension level
     * dimensionLevel is the key and the fileter is the value.
     */
    public Map<Dimension, MolapFilterInfo> getFilters(boolean isAnalyzerQuery, Cube cube)
    {
        Collections.sort(this.wrapperList, new WrapperComparator());
        Map<Dimension, MolapFilterInfo> constraints = new HashMap<Dimension, MolapFilterInfo>(10);

        int[][] rangeIndex = new int[this.wrapperList.size()][2];
        int sum = 0;
        for(int i = 0;i < rangeIndex.length;i++)
        {
            rangeIndex[i][0] = sum;
            sum += this.wrapperList.get(i).filter.getIncludeFilter().size()-sum;
            rangeIndex[i][1] = sum;
        }
        int k = 0;
        List<String> filterStringList = null;
        MolapFilterInfo molapFilterInfo = null;
        while(k < rangeIndex.length)
        {
            filterStringList = new ArrayList<String>(10);
            if(isAnalyzerQuery)
            {
                addIncludeFiltersForAnalyzerQuery(rangeIndex, k, filterStringList);
            }
            else
            {
                addIncludeFiltersForDirectAPIQuery(rangeIndex, k, filterStringList);
            }
            
            if(filterStringList.size()>0)
            {
                molapFilterInfo = getMolapFilterInfo(this.wrapperList.get(k).dimension, this.wrapperList.get(k).filter, cube);
                molapFilterInfo.addAllIncludedMembers(filterStringList);
                constraints.put(this.wrapperList.get(k).dimension, molapFilterInfo);
            }
            k++;
        }

        rangeIndex = new int[this.wrapperList.size()][2];
        sum = 0;
        for(int i = 0;i < rangeIndex.length;i++)
        {
            rangeIndex[i][0] = sum;
            sum += this.wrapperList.get(i).filter.getExcludeFilter().size()-sum;
            rangeIndex[i][1] = sum;
        }
        k = 0;
        while(k < rangeIndex.length)
        {
            filterStringList = new ArrayList<String>(10);
            if(isAnalyzerQuery){
                addExcludeFiltersForAnalyzerQuery(rangeIndex, k, filterStringList);     
            }
            else
            {
                addExcludeFiltersForDirectAPIQuery(rangeIndex, k, filterStringList);
            }
            
            if(filterStringList.size()>0)
            {
                molapFilterInfo = constraints.get(this.wrapperList.get(k).dimension);
                if(null == molapFilterInfo)
                {
                    molapFilterInfo = getMolapFilterInfo(this.wrapperList.get(k).dimension, this.wrapperList.get(k).filter, cube);
                }
                molapFilterInfo.addAllExcludedMembers(filterStringList);
                constraints.put(this.wrapperList.get(k).dimension, molapFilterInfo);
            }
            k++;
        }
        Wrapper wrapper = null;
        if(this.wrapperList.size()>0)
        {
            int size= this.wrapperList.size();
            for(int i = 0;i <size;i++)
            {
                wrapper = this.wrapperList.get(i);
                if((wrapper.filter.getContainsFilter().size() > 0 || wrapper.filter.getDoesNotContainsFilter().size() > 0)
                        && (wrapper.filter.getExcludeFilter().size() < 1 && wrapper.filter.getIncludeFilter().size() < 1))
                {
                    constraints.put(wrapper.dimension, getMolapFilterInfoForSingleFilter(wrapper.filter));
                }
            }
        }
        return constraints;
    }

    private void addExcludeFiltersForAnalyzerQuery(int[][] rangeIndex, int k, List<String> filterStringList)
    {
        StringBuilder builder=null;
        for(int j = rangeIndex[k][0];j < rangeIndex[k][1];j++)
        {
            builder = new StringBuilder();
            for(int i = this.wrapperList.size() - 1;i > k;i--)
            {
                builder.append('[');
                builder.append(this.wrapperList.get(i).filter.getExcludeFilter().get(j));
                builder.append(']');
                builder.append('.');
            }
            builder.append('[');
            builder.append(this.wrapperList.get(k).filter.getExcludeFilter().get(j));
            builder.append(']');
            filterStringList.add(builder.toString());
        }
    }
    
    private void addExcludeFiltersForDirectAPIQuery(int[][] rangeIndex, int k, List<String> filterStringList)
    {
        StringBuilder builder=null;
        for(int j = rangeIndex[k][0];j < rangeIndex[k][1];j++)
        {
            builder = new StringBuilder();
            for(int i = this.wrapperList.size() - 1;i > k;i--)
            {
          
                builder.append(this.wrapperList.get(i).filter.getExcludeFilter().get(j));
                builder.append('.');
            }
         
            builder.append(this.wrapperList.get(k).filter.getExcludeFilter().get(j));
            filterStringList.add(builder.toString());
        }
    }

    private void addIncludeFiltersForAnalyzerQuery(int[][] rangeIndex, int k, List<String> filterStringList)
    {
        StringBuilder builder=null;
        for(int j = rangeIndex[k][0];j < rangeIndex[k][1];j++)
        {
            builder = new StringBuilder();
            for(int i = this.wrapperList.size() - 1;i > k;i--)
            {
                builder.append('[');
                builder.append(this.wrapperList.get(i).filter.getIncludeFilter().get(j));
                builder.append(']');
                builder.append('.');
            }
            builder.append('[');
            builder.append(this.wrapperList.get(k).filter.getIncludeFilter().get(j));
            builder.append(']');
            filterStringList.add(builder.toString());
        }
    }
    
    private void addIncludeFiltersForDirectAPIQuery(int[][] rangeIndex, int k, List<String> filterStringList)
    {
        StringBuilder builder=null;
        for(int j = rangeIndex[k][0];j < rangeIndex[k][1];j++)
        {
            builder = new StringBuilder();
            for(int i = this.wrapperList.size() - 1;i > k;i--)
            {

                builder.append(this.wrapperList.get(i).filter.getIncludeFilter().get(j));

                builder.append('.');
            }

            builder.append(this.wrapperList.get(k).filter.getIncludeFilter().get(j));

            filterStringList.add(builder.toString());
        }
    }
    
   

   /**
    * 
    * @param dimension 
 * @Author M00903915
    * @Description : getMolapFilterInfo
    * @param dimLevelFilter instance of MolapDimensionLevelFilter
 * @param cube 
    * @return return the instance of MolapaFileterInfo
    */
    private MolapFilterInfo getMolapFilterInfo(Dimension dimension, MolapDimensionLevelFilter dimLevelFilter, Cube cube)
    {
        
        MolapFilterInfo filterInfo = null;
        if(dimLevelFilter.getContainsFilter().size() > 0 || dimLevelFilter.getDoesNotContainsFilter().size() > 0)
        {
            filterInfo = new ContentMatchFilterInfo();
        }
        else
        {
            filterInfo = new MolapFilterInfo();
        }
        
//        for(Object object : dimLevelFilter.getIncludeFilter())
//        {
//            filterInfo.addIncludedMembers(object.toString());
//        }
//        
//        for(Object object : dimLevelFilter.getExcludeFilter())
//        {
//            filterInfo.addExcludedMembers(object.toString());
//        }
        
        if(filterInfo instanceof ContentMatchFilterInfo)
        {
            ContentMatchFilterInfo info = new ContentMatchFilterInfo();
            List<String> contains = new ArrayList<String>(10);
            // CHECKSTYLE:OFF
            // Already Approved - Refer approval IDs: V3R8C00_003 :
            // Checkstyle_Approval.xlsx
            // at location: /03.SW Folder/Approval Record/Static Check Approvals
            for(String object : dimLevelFilter.getContainsFilter())
            {
           //CHECKSTYLE:ON
                contains.add(object);
            }
            List<String> notContains = new ArrayList<String>(10);
           // CHECKSTYLE:OFF
            // Already Approved - Refer approval IDs: V3R8C00_003 :
            // Checkstyle_Approval.xlsx
            // at location: /03.SW Folder/Approval Record/Static Check Approvals
            for(String object : dimLevelFilter.getDoesNotContainsFilter())
            {
            //CHECKSTYLE:ON  
                notContains.add(object);
            }
            ((ContentMatchFilterInfo)filterInfo).setExcludedContentMatchMembers(notContains);
            ((ContentMatchFilterInfo)filterInfo).setIncludedContentMatchMembers(contains);
            info.setExcludedContentMatchMembers(notContains);
            info.setIncludedContentMatchMembers(contains);
            
          
            List<Dimension> hierarchiesMapping = cube.getHierarchiesMapping(dimension.getDimName()+'_'+dimension.getHierName());
            int indexOf = hierarchiesMapping.indexOf(dimension);
            
            ((ContentMatchFilterInfo)filterInfo).getDimFilterMap().put(indexOf, info);
        }
        
        return filterInfo;
    }
    
    /**
     * 
     * @Author M00903915
     * @Description : getMolapFilterInfo
     * @param dimLevelFilter instance of MolapDimensionLevelFilter
     * @return return the instance of MolapaFileterInfo
     */
     private MolapFilterInfo getMolapFilterInfoForSingleFilter(MolapDimensionLevelFilter dimLevelFilter)
     {
         
         MolapFilterInfo filterInfo = null;
         if(dimLevelFilter.getContainsFilter().size() > 0 || dimLevelFilter.getDoesNotContainsFilter().size() > 0)
         {
             filterInfo = new ContentMatchFilterInfo();
         }
         else
         {
             filterInfo = new MolapFilterInfo();
         }
         // CHECKSTYLE:OFF
         // Already Approved - Refer approval IDs: V3R8C00_003 :
         // Checkstyle_Approval.xlsx
         // at location: /03.SW Folder/Approval Record/Static Check Approvals
         for(Object object : dimLevelFilter.getIncludeFilter())
         {
             filterInfo.addIncludedMembers(object.toString());
         }
         
         for(Object object : dimLevelFilter.getExcludeFilter())
         {//CHECKSTYLE:ON
             filterInfo.addExcludedMembers(object.toString());
         }
         
         if(filterInfo instanceof ContentMatchFilterInfo)
         {
             ContentMatchFilterInfo info = new ContentMatchFilterInfo();
             List<String> contains = new ArrayList<String>(10);
             // CHECKSTYLE:OFF
             // Already Approved - Refer approval IDs: V3R8C00_003 :
             // Checkstyle_Approval.xlsx
             // at location: /03.SW Folder/Approval Record/Static Check Approvals
             for(String str : dimLevelFilter.getContainsFilter())    
             {
            //CHECKSTYLE:ON
                 contains.add(str);
             }
             
             List<String> notContains = new ArrayList<String>(10);
            // CHECKSTYLE:OFF
             // Already Approved - Refer approval IDs: V3R8C00_003 :
             // Checkstyle_Approval.xlsx
             // at location: /03.SW Folder/Approval Record/Static Check Approvals
             for(String object : dimLevelFilter.getDoesNotContainsFilter())
             {
             //CHECKSTYLE:ON  
                 notContains.add(object);
             }
             ((ContentMatchFilterInfo)filterInfo).setExcludedContentMatchMembers(notContains);
             ((ContentMatchFilterInfo)filterInfo).setIncludedContentMatchMembers(contains);
             info.setExcludedContentMatchMembers(notContains);
             info.setIncludedContentMatchMembers(contains);
             ((ContentMatchFilterInfo)filterInfo).getDimFilterMap().put(0, info);
             
         }
         
         return filterInfo;
     }
    /**
     * The comparator class used to sort the List<Wrapper> according to the ordinal value of the dimension.
     * Module Name : 
     * Author M00903915
     * Created Date :05-Jan-2015 8:15:32 PM
     * 
     * Class Description :
     * Version 1.0
     */
    private class WrapperComparator implements Comparator<Wrapper>
    {
        @Override
        public int compare(Wrapper o1, Wrapper o2)
        {
            int cpmValue=o1.dimension.getOrdinal()-o2.dimension.getOrdinal();
            return cpmValue*-1;
        }
        
    }
    /**
     * The class to to wrap the filter with dimension.
     * Module Name : 
     * Author M00903915
     * Created Date :05-Jan-2015 8:17:04 PM
     * 
     * Class Description :
     * Version 1.0
     */
    private class Wrapper
    {
        private Dimension dimension;
        
        private MolapDimensionLevelFilter filter;
    }
}
