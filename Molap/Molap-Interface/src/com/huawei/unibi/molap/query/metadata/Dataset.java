/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 1997
 * =====================================
 *
 */
package com.huawei.unibi.molap.query.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author K00900207
 *
 */
public class Dataset
{
    
    public static final String DATA_SET_NAME = "DATA_SET_NAME";
    
    
    public static final String DP_EXTENSION = ".dpxanalyzer";
    public static final String DATA_SET_EXTENSION = ".csv";
    
    
    /**
     * SQL connection parameters
     */
    public static final String DB_CONNECTION_NAME = "DB_CONNECTION_NAME";
    public static final String DRIVER_CLASS = "DRIVER_CLASS";
    public static final String DB_URL = "DB_URL";
    public static final String DB_USER_NAME = "DB_USER_NAME";
    public static final String DB_PASSWORD = "DB_PASSWORD";
    
    
    
    /**
     * Type of the data set from the supported enumeration 
     */
    private DatasetType type;
    
    /**
     * Name 
     */
    private String name;
    
    /**
     * Columns represented in   
     */
    private List<Column> columns;
    
    private Map<String, Object> properties = new HashMap<String, Object>(16);
    
    public Dataset(DatasetType type, String title, List<Column> columns)
    {
        super();
        this.type = type;
        this.name = title;
        this.columns = columns;
    }
    
    public Object getProperty(String propertyName)
    {
        return properties.get(propertyName);
    }
    
    public void setProperty(String propertyName, Object value)
    {
        properties.put(propertyName, value);
    }

    public static enum DatasetType
    {
        LOCAL_FILE,
        REPORT_DATA_EXPORT,
        REPORT_LINK_EXPORT,
        DB_SQL;
    }
    
    
    public static class Column
    {
        private String name;
//        private String type;
//        private String description;
        
        public Column(String name, String type, String description)
        {
            this.name = name;
//            this.type = type;
//            this.description = description;
        }

        /*public String getName()
        {
            return name;
        }*/

        /*public String getType()
        {
            return type;
        }*/
        
        /*public String getDescription()
        {
            return description;
        }*/
        
        @Override
        public String toString()
        {
            return name;
        }
        
    }
    public DatasetType getType()
    {
        return type;
    }
    
    public void setType(DatasetType type)
    {
        this.type = type;
    }
    
    public String getTitle()
    {
        return name;
    }
    
    public void setTitle(String title)
    {
        this.name = title;
    }
    
    public List<Column> getColumns()
    {
        return columns;
    }
    
    public void setColumns(List<Column> columns)
    {
        this.columns = columns;
    }
    
    @Override
    public String toString()
    {
        return "<DS: Name= "+ name +", Columns= " + columns.toString() + '>';
    }
  
}
