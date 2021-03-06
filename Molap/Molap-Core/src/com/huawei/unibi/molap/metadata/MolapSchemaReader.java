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
package com.huawei.unibi.molap.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.olap.MolapDef;
import com.huawei.unibi.molap.olap.MolapDef.Cube;
import com.huawei.unibi.molap.olap.MolapDef.CubeDimension;
import com.huawei.unibi.molap.olap.MolapDef.Dimension;
import com.huawei.unibi.molap.olap.MolapDef.DimensionUsage;
import com.huawei.unibi.molap.olap.MolapDef.Hierarchy;
import com.huawei.unibi.molap.olap.MolapDef.Level;
import com.huawei.unibi.molap.olap.MolapDef.Measure;
import com.huawei.unibi.molap.olap.MolapDef.Property;
import com.huawei.unibi.molap.olap.MolapDef.RelationOrJoin;
import com.huawei.unibi.molap.olap.MolapDef.Schema;
import com.huawei.unibi.molap.olap.MolapDef.Table;



/**
 * Project Name NSE V3R7C00 
 * Module Name : MOLAP
 * Author :C00900810
 * Created Date :24-Jun-2013
 * FileName : MolapSchemaParser.java
 * Class Description : 
 * Version 1.0
 */
public final class MolapSchemaReader
{
    /**
     * 
     */
    private static final String QUOTES = "\"";
     private MolapSchemaReader()
     {
         
     }

    /**
     * 
     * @param schema
     * @return
     */
    public static Cube[] getMondrianCubes(Schema schema)
    {
        return schema.cubes;
    }
    
    /**
     * getMondrianCube
     * @param schema
     * @param cubeName
     * @return Cube
     */
    public static Cube getMondrianCube(Schema schema,String cubeName)
    {
        Cube[] cubes = schema.cubes;
        for(Cube cube : cubes)    
        {
            String cubeUniqueName = schema.name+'_'+cube.name;
            if(cubeUniqueName.equals(cubeName))
            {
                return cube;
            }
        }
		return null;
    }

    /**
     * @param dimensions
     * @return
     */
    public static String getDimensionSQLQueries(CubeDimension[]dimensions)
    {
        //
        List<String> queryList = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        Property[] properties = null;
        for(CubeDimension dim : dimensions)
        {
            //
            Dimension dimension = (Dimension)dim;
                    StringBuilder query;
            for(Hierarchy hierarchy : dimension.hierarchies)
            {
                
               // String tableName = hierarchy.relation.toString();
                RelationOrJoin relation = hierarchy.relation;
                String tableName = relation==null?dimension.name:((Table)hierarchy.relation).name;
                if(null == hierarchy.name)
                {
                    query = new StringBuilder(tableName + '_'  + dimension.name + ':');
                }
                else
                {
                    query = new StringBuilder(tableName + '_'  + hierarchy.name + ':');
                }
                //
                query.append("SELECT ");
                query.append(hierarchy.levels[0].column);
                //
                if(hasOrdinalColumn(hierarchy.levels[0]))
                {
                    query.append(',' + hierarchy.levels[0].ordinalColumn);
                }
                if(null!=hierarchy.levels[0].nameColumn)
                {
                    query.append(',' + hierarchy.levels[0].nameColumn);
                }
                //
                properties = hierarchy.levels[0].properties;
                if(properties.length > 1)
                {
                    for(int j = 0;j < properties.length;j++)
                    {
                        query.append(',' + properties[j].column);
                    }
                }
                //
                for(int i = 1;i < hierarchy.levels.length;i++)
                {
                    query.append(',' + hierarchy.levels[i].column);
                    if(hasOrdinalColumn(hierarchy.levels[0]))
                    {
                        query.append(',' + hierarchy.levels[0].ordinalColumn);
                    }
                    if(null!=hierarchy.levels[i].nameColumn)
                    {
                        query.append(',' + hierarchy.levels[i].nameColumn);
                    }
                    properties = hierarchy.levels[i].properties;
                    if(properties.length > 1)
                    {
                        for(int j = 0;j < properties.length;j++)
                        {
                            query.append(',' + properties[j].column);
                        }
                    }
                }
                //
                if (null != hierarchy.relation)
                {
                    query.append(" FROM " + hierarchy.relation.toString());
                }
                queryList.add(query.toString());
            }
        }
        StringBuilder finalQuryString = new StringBuilder();

        for(int i = 0;i < queryList.size() - 1;i++)
        {
            finalQuryString.append(queryList.get(i));
            finalQuryString.append("#");
        }
        finalQuryString.append(queryList.get(queryList.size() - 1));
        return finalQuryString.toString();
    }
    
    
    /**
     * @param dimensions
     * @param measures
     * @param factTableName
     * @param isQuotesRequired 
     * @param schemaInfo 
     * @return
     */
    public static String getTableInputSQLQuery(CubeDimension[] dimensions,
            Measure[] measures, String factTableName, boolean isQuotesRequired)
    {
        StringBuilder query = new StringBuilder("SELECT ");
        // List<Hierarchy> hierarchyList = new
        // ArrayList<MolapDef.Hierarchy>();
        getQueryForDimension(dimensions, query, factTableName, isQuotesRequired);
        if(!isQuotesRequired)
        {
            getPropetiesQuerypart(dimensions, query, factTableName);
        }
        else
        {
            getPropetiesQuerypartWithQuotes(dimensions, query, factTableName);
        }
        for(int i = 0;i < measures.length;i++)
        {
            query.append(System.getProperty("line.separator"));
            // query.append("\n");
            if(isQuotesRequired)
            {
                query.append(',' + QUOTES + factTableName + QUOTES + '.' + QUOTES
                        + measures[i].column + QUOTES);
            }
            else
            {
                query.append(',' + factTableName + '.' 
                        + measures[i].column);
            }
        }
        query.append(System.getProperty("line.separator"));
        // query.append("\n");
        if(isQuotesRequired)
        {
         query.append(" FROM " + QUOTES + factTableName + QUOTES + ' ');
        }
        else
        {
            query.append(" FROM " + factTableName + ' ');
        }

        for(CubeDimension cubeDimension : dimensions)
        {
            Dimension dim = (Dimension)cubeDimension;
            Hierarchy[] hierarchies = dim.hierarchies;
            for(Hierarchy hierarchy : hierarchies)
            {
                query.append(System.getProperty("line.separator"));
                // query.append("\n");
                // String tableName = hierarchy.relation.toString();
                RelationOrJoin relation = hierarchy.relation;
                String hierarchyTable = relation == null ? factTableName
                        : ((Table)hierarchy.relation).name;
                if(isQuotesRequired)
                {
                    query.append(" INNER JOIN " + QUOTES + hierarchyTable + QUOTES
                            + " ON ");
                    String primaryKey = hierarchy.primaryKey;
                    query.append(QUOTES + hierarchyTable + QUOTES + '.' + QUOTES
                            + primaryKey + QUOTES);
                    query.append('=' + QUOTES + factTableName + QUOTES + '.'
                            + QUOTES + dim.foreignKey + QUOTES);
                }
                else
                {
                    query.append(" INNER JOIN " + hierarchyTable + " ON ");
                    String primaryKey = hierarchy.primaryKey;
                    query.append(hierarchyTable + '.' 
                            + primaryKey);
                    query.append('=' + factTableName + '.'
                            + dim.foreignKey);
                }
            }
        }

        return query.toString();
    }

    /**
     * @param aggDim
     * @param measures
     * @param factTableName
     * @param isQuotesRequired 
     * @param schemaInfo 
     * @return
     */
    public static String getTableInputSQLQueryForAGG(String [] aggDim, String[] measures, String factTableName, boolean isQuotesRequired)
    {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(System.getProperty("line.separator"));
        //query.append("\n");
        for(int i = 0;i < aggDim.length;i++)
        {
            if(isQuotesRequired)
            {
                query.append(QUOTES + aggDim[i] + QUOTES);
            }
            else
            {
                query.append(aggDim[i]);
            }
            query.append(",");
            query.append(System.getProperty("line.separator"));
            //query.append("\n");
        }
        
        for(int i = 0;i < measures.length-1;i++)
        {
            if(isQuotesRequired)
            {
                query.append(QUOTES + measures[i] + QUOTES);
            }
            else
            {
                query.append(measures[i]);
            }
            query.append(",");
            query.append(System.getProperty("line.separator"));
            //query.append("\n");
        }
        if(isQuotesRequired)
        {
            query.append(QUOTES + measures[measures.length-1] + QUOTES);
            query.append(System.getProperty("line.separator"));
            query.append(" FROM " + QUOTES + factTableName + QUOTES);
        }
        else
        {
            query.append(measures[measures.length-1]);
            query.append(System.getProperty("line.separator"));
            query.append(" FROM " + factTableName);
        }
        return query.toString();
    }

    private static void getPropetiesQuerypart(CubeDimension[] dimensions,
            StringBuilder query, String factTableName)
    {
        Property[] properties;
        for(CubeDimension cDim : dimensions)
        {
            Dimension dim = (Dimension)cDim;
            for(Hierarchy hierarchy : dim.hierarchies)
            {
                // String tableName = hierarchy.relation.toString();
                RelationOrJoin relation = hierarchy.relation;
                String hierarchyTable = relation == null ? factTableName
                        : ((Table)hierarchy.relation).name;
                if(hasOrdinalColumn(hierarchy.levels[0]))
                {
                    query.append(System.getProperty("line.separator"));
                    // query.append("\n");
                    query.append(',' + hierarchyTable + '.'
                            + hierarchy.levels[0].ordinalColumn);
                }
                if(null != hierarchy.levels[0].nameColumn)
                {
                    query.append(System.getProperty("line.separator"));
                    // query.append("\n");
                    query.append(',' + hierarchyTable + '.'
                            + hierarchy.levels[0].nameColumn);
                }
                properties = hierarchy.levels[0].properties;
                if(properties.length > 0)
                {
                    for(int j = 0;j < properties.length;j++)
                    {
                        query.append(System.getProperty("line.separator"));
                        // query.append("\n");
                        query.append(',' + hierarchyTable + '.'
                                + properties[j].column);
                    }
                }
                for(int i = 1;i < hierarchy.levels.length;i++)
                {
                    if(hasOrdinalColumn(hierarchy.levels[i]))
                    {
                        query.append(System.getProperty("line.separator"));
                        // query.append("\n");
                        query.append(',' + hierarchyTable + '.'
                                + hierarchy.levels[i].ordinalColumn);
                    }
                    if(null != hierarchy.levels[i].nameColumn)
                    {
                        query.append(System.getProperty("line.separator"));
                        // query.append("\n");
                        query.append(',' + hierarchyTable + '.'
                                + hierarchy.levels[i].nameColumn);
                    }
                    properties = hierarchy.levels[i].properties;
                    if(properties.length > 0)
                    {
                        for(int j = 0;j < properties.length;j++)
                        {
                            query.append(System.getProperty("line.separator"));
                            // query.append("\n");
                            query.append(',' + hierarchyTable + '.'
                                    + properties[j].column);
                        }
                    }
                }
            }
        }
    }

    private static void getPropetiesQuerypartWithQuotes(CubeDimension[] dimensions,
            StringBuilder query ,String factTableName)
    {
        Property[] properties;
        for(CubeDimension cDim:dimensions)
        {
            Dimension dim = (Dimension)cDim;
            for(Hierarchy hierarchy : dim.hierarchies)
            {
                //String tableName = hierarchy.relation.toString();
                RelationOrJoin relation = hierarchy.relation;
                String hierarchyTable = relation==null?factTableName:((Table)hierarchy.relation).name;
                if(hasOrdinalColumn(hierarchy.levels[0]))
                {
                    query.append(System.getProperty("line.separator"));
                    //query.append("\n");
                        query.append(',' + QUOTES + hierarchyTable + QUOTES + '.'
                            + QUOTES + hierarchy.levels[0].ordinalColumn + QUOTES);
                }
                if(null!=hierarchy.levels[0].nameColumn)
                {
                    query.append(System.getProperty("line.separator"));
                    //query.append("\n");
                        query.append(',' + QUOTES + hierarchyTable + QUOTES + '.'
                                + QUOTES + hierarchy.levels[0].nameColumn + QUOTES);
                }
                properties = hierarchy.levels[0].properties;
                if(properties.length > 1)
                {
                    for(int j = 0;j < properties.length;j++)
                    {
                        query.append(System.getProperty("line.separator"));
                        //query.append("\n");
                         query.append(',' + QUOTES + hierarchyTable + QUOTES +  '.'
                                + QUOTES + properties[j].column + QUOTES);
                    }
                }
                for(int i = 1;i < hierarchy.levels.length;i++)
                {
                    if(hasOrdinalColumn(hierarchy.levels[i]))
                    {
                        query.append(System.getProperty("line.separator"));
                        //query.append("\n");
                        query.append(',' + QUOTES + hierarchyTable + QUOTES + '.'
                                + QUOTES + hierarchy.levels[i].ordinalColumn + QUOTES);
                    }
                    if(null!=hierarchy.levels[i].nameColumn)
                    {
                        query.append(System.getProperty("line.separator"));
                        //query.append("\n");
                            query.append(',' + QUOTES + hierarchyTable + QUOTES + '.'
                                + QUOTES + hierarchy.levels[i].nameColumn + QUOTES);
                    }
                    properties = hierarchy.levels[i].properties;
                    if(properties.length > 1)
                    {
                        for(int j = 0;j < properties.length;j++)
                        {
                            query.append(System.getProperty("line.separator"));
                            //query.append("\n");
                                query.append(',' + QUOTES + hierarchyTable + QUOTES + '.'
                                    + QUOTES + properties[j].column + QUOTES);
                        }
                    }
                }
            }
        }
    }


    private static int getQueryForDimension(CubeDimension[] dimensions,
            StringBuilder query,String factTableName, boolean isQuotesRequired)
    {
        //
        int counter=0;
        for(CubeDimension cDim:dimensions)
        {
            //
            Dimension dim = (Dimension)cDim;
            for(Hierarchy hierarchy : dim.hierarchies)
            {
                query.append(System.getProperty("line.separator"));
                // query.append("\n");
                //String tableName = hierarchy.relation.toString();
                RelationOrJoin relation = hierarchy.relation;
                String hierarchyTable = relation==null?factTableName:((Table)hierarchy.relation).name;
                
                if(counter==0)
                {
                    if(isQuotesRequired)
                    {
                        query.append(QUOTES + hierarchyTable + QUOTES + '.' + QUOTES + hierarchy.levels[0].column + QUOTES);
                    }
                    else
                    {
                        query.append(hierarchyTable + '.' + hierarchy.levels[0].column);
                    }
                }
                else
                {
                    if(isQuotesRequired)
                    {
                        query.append(',' + QUOTES+hierarchyTable + QUOTES + '.'
                            + QUOTES + hierarchy.levels[0].column + QUOTES);
                    }
                    else
                    {
                        query.append(',' + hierarchyTable + '.'
                                + hierarchy.levels[0].column);
                    }
                }
                //
                counter++;
                for (int i =1; i<hierarchy.levels.length;i++)
                {
                    query.append(System.getProperty("line.separator"));
                    //query.append("\n");
                    if(isQuotesRequired)
                    {
                        query.append(',' + QUOTES + hierarchyTable + QUOTES + '.'
                                + QUOTES + hierarchy.levels[i].column + QUOTES);
                    }
                    else
                    {
                        query.append(',' + hierarchyTable + '.'
                                + hierarchy.levels[i].column);
                    }
                }
            }
        }
        return counter;
    }
    /**
     * Get dimension string from a array of CubeDimension,which can be shared
     * CubeDimension within schema or in a cube.
     * 
     * @param dimensions
     * @return
     * 
     */
    public static int getDimensionString(CubeDimension[] dimensions, StringBuilder dimString, int counter)
    {
        for(CubeDimension cDimension : dimensions)
        {
            Dimension dimension = (Dimension)cDimension;

            for(Hierarchy hierarchy : dimension.hierarchies)
            {
                RelationOrJoin relation = hierarchy.relation;
                String tableName = relation==null?dimension.name:((Table)hierarchy.relation).name;
                //String tableName = hierarchy.relation.toString();
                int i = hierarchy.levels.length;

                for(Level level : hierarchy.levels)
                {
                    
                    dimString.append(tableName + '_' + level.column + ':'
                            + counter
                            + ':' + level.levelCardinality);
					counter++;
                    if(i > 1)
                    {
                        dimString.append(",");

                    }
                    i--;

                }
                dimString.append(",");
            }
            
//            if(!"TimeDimension".equals(dimension.type))
//            {
//                for(Hierarchy hierarchy : dimension.hierarchies)
//                {
//                    int i = hierarchy.levels.length;
//
//                    for(Level level : hierarchy.levels)
//                    {
//                        dimString.append(level.column + ":" + counter++
//                                + ":" + level.levelCardinality);
//                        if(i > 1)
//                        {
//                            dimString.append(",");
//
//                        }
//                        i--;
//
//                    }
//                    dimString.append(",");
//                }
//            }
//            else
//            {
//                dimString.append(dimension.foreignKey).append(":")
//                        .append(counter++) 
//                        .append(",");
//            }
        }

       /* String dimstr = dimString.toString();
        if(dimstr.length() > 0 && dimstr.charAt(dimstr.length()-1) == ',')
        {
            dimstr = dimstr.substring(0, dimstr.length() - 1);
        }*/

        return counter;
    }
    
   /**
 * @param dimensions
 * @param dimString
 * @param counter
 * @param dimCardinalities
 * @return
 */
public static int getDimensionStringForAgg(String[] dimensions, StringBuilder dimString, int counter,Map<String, String> dimCardinalities)
    {
        //
       int len=dimensions.length;
        for(int i = 0;i < len-1;i++)
        {
            dimString.append(dimensions[i]);
            dimString.append(":");
            dimString.append(counter++);
            dimString.append(":");
            dimString.append(dimCardinalities.get(dimensions[i]));
            dimString.append(",");
        }
        //
        dimString.append(dimensions[len-1]);
        dimString.append(":");
        dimString.append(counter++);
        dimString.append(":");
        dimString.append(dimCardinalities.get(dimensions[len-1]));
        return counter;
    }
    
    /**
     * Return mapping of Column name to cardinality 
     */
    public static Map<String, String> getCardinalities(CubeDimension[] dimensions)
    {
        Map<String, String>  cardinalities = new LinkedHashMap<String, String>();
        //
        for(CubeDimension cDimension : dimensions)
        {
            Dimension dimension = (Dimension)cDimension;
            //
            for(Hierarchy hierarchy : dimension.hierarchies)
            {
                //String tableName = hierarchy.relation.toString();
                RelationOrJoin relation = hierarchy.relation;
                String tableName = relation==null?dimension.name:((Table)hierarchy.relation).name;
                for(Level level : hierarchy.levels)
                {
                    cardinalities.put(tableName + '_' + level.column, level.levelCardinality+"");
                    //
                }
            }
        }
        return cardinalities;
    }
    

    /**
     * Get measure string from a array of Measure
     * 
     * @param measures
     * @return
     * 
     */
    public static String getMeasureString(Measure[] measures, int counter)
    {
        StringBuilder measureString = new StringBuilder();
        int i = measures.length;
        for(Measure measure : measures)
        {
            
            measureString.append(measure.column + ':' + counter);
			counter++;
            if(i > 1)
            {
                measureString.append(",");
            }
            i--;

        }
        return measureString.toString();
    }
    
    /**
     * Get measure string from a array of Measure
     * 
     * @param measures
     * @return
     * 
     */
    public static String getMeasureStringForAgg(String[] measures, int counter)
    {
        StringBuilder measureString = new StringBuilder();
        int i = measures.length;
        for(String measure : measures)
        {
            
            measureString.append(measure + ':' + counter);
			counter++;
            if(i > 1)
            {
                measureString.append(",");
            }
            i--;

        }
        return measureString.toString();
    }
    
    /**
     * Get measure string from a array of Measure
     * 
     * @param measures
     * @return
     * 
     */
    public static String[] getMeasures(Measure[] measures)
    {
        String [] measuresStringArray= new String[measures.length];

        for(int i = 0;i < measuresStringArray.length;i++)
        {
            measuresStringArray[i]=measures[i].column;
        }
        return measuresStringArray;
    }
    
    /**
     * Get hierarchy string from dimensions
     * 
     * @param dimensions
     * @return
     * 
     */
    public static String getHierarchyString(CubeDimension[] dimensions)
    {
        StringBuilder hierString = new StringBuilder();
        int hierIndex = -1;
        String hierStr = "";
        int lengthOfLevels = 0;
        int counter=0;

        for(CubeDimension cDimension : dimensions)
        {
            Dimension dimension = (Dimension)cDimension;
            //if we don't care the time dimension,we can use below block.
            //if we do,then we must remove the block.
            //if(!"TimeDimension".equals(dimension.type))
       //     {
                for(Hierarchy hierarchy : dimension.hierarchies)
                {
                    String hName = hierarchy.name;
                if(hName == null || "".equals(hName.trim()))
                    {
                        hName = dimension.name;
                    }
                    //String tableName = hierarchy.relation.toString();
                    RelationOrJoin relation = hierarchy.relation;
                    String tableName = relation==null?dimension.name:((Table)hierarchy.relation).name;
                    
                    lengthOfLevels = hierarchy.levels.length;
                    int hierlength = hierarchy.levels.length;
                    if(hierlength > 1)
                    {
                        StringBuilder localString = new StringBuilder();

                    for(int i = 0;i < hierlength;i++)
                        {
                            if(hierIndex == -1)
                            {
                                localString.append(counter++);
                            }
                            else
                            {
                                localString.append(++hierIndex);
                            }

                            if(lengthOfLevels > 1)
                            {
                                localString.append(",");

                            }
                            lengthOfLevels--;
                        }
                        localString.append("&");
                        hierStr = localString.toString();
                    hierStr = tableName + '_' + hName + ':' + hierStr;
                        hierString.append(hierStr);
                    }
                    else
                    {
                        counter++;
                    }

                }
            }

     //   }

        hierStr = hierString.toString();
        if(hierStr.length() > 0 && hierStr.charAt(hierStr.length()-1) == '&')
        {
            hierStr = hierStr.substring(0, hierStr.length() - 1);
        }
        return hierStr;
    }

    
    /**
     * Get all aggregate tables in a cube
     * 
     * @param cube
     * @return
     *
     */
    public static List<Map<String,String>> getAggTable(Cube cube)
    {
        List<Map<String,String>> aggTableList = new ArrayList<Map<String,String>>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        
        MolapDef.Table factTable = (MolapDef.Table)cube.fact;
        MolapDef.AggTable[] aggTables = factTable.getAggTables();
        
        for(int i = 0;i < aggTables.length;i++)
        {
            Map<String,String> aggrTableMap = new HashMap<String,String>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
            String aggTableName = "Agg";//aggTables[0].name;
            String dimensionString = "";
            String timeString = "";
            String measureString = "";
            
            // MolapDef.AggMeasure[] measures = aggTable.measures;
            // MolapDef.AggLevel[] dimensions = aggTable.levels;
             
            aggrTableMap.put("AggTableName", aggTableName);
            aggrTableMap.put("AggTableDim", dimensionString);
            aggrTableMap.put("AggTableTime", timeString);
            aggrTableMap.put("AggTableMsr", measureString);
            
            aggTableList.add(aggrTableMap);
            
        }
        
        return aggTableList;
    }
    
    /**
     * Get the name of a fact table in a cube
     * 
     * @param cube
     * @return
     *
     */
    public static String getFactTableName(Cube cube)
    {
        MolapDef.Table factTable = (MolapDef.Table)cube.fact;
        return factTable.name;
    }
    
    /**
     * @param measures
     * @return
     */
    public static String getAggTableMeasureString(MolapDef.AggMeasure[] measures)
    {
        StringBuilder measureString = new StringBuilder();

        int i = measures.length;
        for(int j = 0;j < measures.length;j++)
        {
            measureString.append(measures[j].column + ':' + measures[j].name);
            if(i > 1)
            {
                measureString.append(",");
            }
            i--;

        }
        return measureString.toString();
    }



    /**
     * @param cube
     * @return
     */
    public static Map<String,String> getCubeMeasuresAndDataType(Cube cube)
    {
        MolapDef.Measure[] measures = cube.measures;
        int numOfagg = measures.length;
        Map<String,String> measureNameAndDataTypeMap = new LinkedHashMap<String, String>(numOfagg);
        for(int i = 0;i< numOfagg;i++)
        {
            measureNameAndDataTypeMap.put(measures[i].column, measures[i].datatype);
        }
        return measureNameAndDataTypeMap;
    }
    
    /**
     * @param cube
     * @return
     */
    public static List<String[]> getCubeMeasures(Cube cube)
    {
        
        List<String[]> cubeMeasures = new ArrayList<String[]>(3);
        MolapDef.Measure[] measures = cube.measures;
        int numOfagg = measures.length;
        String[] aggregators = new String[numOfagg];
        String[] measureNames = new String[numOfagg];
        String[] measureColumns = new String[numOfagg];
       // String[] measureColumnIndex = new String[numOfagg];
        
        for(int i = 0;i< numOfagg;i++)
        {
            aggregators[i] = measures[i].aggregator;
            measureColumns[i] = measures[i].column;
            measureNames[i] = measures[i].name;
            //measureColumnIndex[i] = measures[i].columnIndex+"";
        }
        
        cubeMeasures.add(measureColumns);
        cubeMeasures.add(measureNames);
        cubeMeasures.add(aggregators);
      //  cubeMeasures.add(measureColumnIndex);
        
        return cubeMeasures;
        
    }
    
    /**
     * @param cube
     * @return
     */
    public static String[] getCubeDimensions(Cube cube)
    {
        String factTable = ((Table)cube.fact).name;
        List<String> list = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        MolapDef.CubeDimension[] dimensions = cube.dimensions;
        for(CubeDimension cDimension : dimensions)
        {
            for(Hierarchy hierarchy : ((Dimension)cDimension).hierarchies)
            {
                list = getTableNames(factTable, hierarchy);
            }
        }
        String[] fields = new String[list.size()];
        fields= list.toArray(fields);
        return fields;
    }
    /**
     * @param factTable
     * @param list
     * @param hierarchy
     */
    private static List<String> getTableNames(String factTable,
            Hierarchy hierarchy)
    {
        List<String> list = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        // String tableName = hierarchy.relation.toString();
        RelationOrJoin relation = hierarchy.relation;
        String tableName = relation == null ? factTable
                : ((Table)hierarchy.relation).name;
        for(Level level : hierarchy.levels)
        {
            list.add(tableName + '_' + level.column);

            if(hasOrdinalColumn(level))
            {
                list.add(tableName + '_' + level.ordinalColumn);
            }
            if(level.nameColumn != null)
            {
                list.add(tableName + '_' + level.nameColumn);
            }
            Property[] properties = level.properties;
            for(int i = 0;i < properties.length;i++)
            {
                list.add(properties[i].name);
            }
        }
        return list;
    }
    
    /**
     * @param cube
     * @return
     */
    public static String[] getDimensions(Cube cube)
    {
        //
        String factTable = ((Table)cube.fact).name;
        List<String> list = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        MolapDef.CubeDimension[] dimensions = cube.dimensions;
        for(CubeDimension cDimension : dimensions)
        {
            //
            for(Hierarchy hierarchy : ((Dimension)cDimension).hierarchies)
            {
               // String tableName = hierarchy.relation.toString();
                RelationOrJoin relation = hierarchy.relation;
                String tableName = relation==null?factTable:((Table)hierarchy.relation).name;
                for(Level level : hierarchy.levels)
                {
                        list.add(tableName + '_' + level.column);
                    //
                }
            }
        }
        String[] fields = new String[list.size()];
        fields= list.toArray(fields);
        return fields;
    }
    
//    /**
//     * Get the max column index from a cube
//     * 
//     * @param cube
//     * @return
//     *
//     */
//    public static int getMaxIndexFromCube(Cube cube)
//    {
//        List<Integer> dimList = new ArrayList<Integer>();
//        List<Integer> msrList = new ArrayList<Integer>();
//
//        MolapDef.CubeDimension[] dimensions = cube.dimensions;
//        for(CubeDimension cDimension : dimensions)
//        {
//            for(Hierarchy hierarchy : ((Dimension)cDimension).hierarchies)
//            {
//                for(Level level : hierarchy.levels)
//                {
//                    if(level.columnIndex != null && !level.columnIndex.equals("-1"))
//                    {
//                        dimList.add(level.columnIndex);
//                    }
//                }
//            }
//        }
//
//        MolapDef.Measure[] measures = cube.measures;
//        for(Measure measure:measures)
//        {
//            msrList.add(measure.columnIndex);
//        }
//        
//        // Firstly,we get the max value of the columnIndex in a cube
//        // If the max value is not same as the number of the dimensions and
//        // measures,
//        // we will use the bigger one.
//        int numOfLevels = dimList.size() + msrList.size();
//        Integer[] orders = new Integer[numOfLevels];
//        System.arraycopy(dimList.toArray(), 0, orders, 0, dimList.toArray().length);
//        System.arraycopy(msrList.toArray(), 0, orders, dimList.toArray().length, msrList.toArray().length);
//        
//        Arrays.sort(orders);
//        int maxOrder = orders[numOfLevels-1]+1;
//        
//        return maxOrder;
//    }
    
 
   /**
 * 
 * @param cube 
 * @param measureName 
 * @return
 * 
 * /
private static List<String[]> getCubeMeasureAggregatorDetails(String aggTableName, Cube cube)
{
    MolapDef.Table table = (MolapDef.Table)cube.fact;
    MolapDef.AggTable[] aggTables = table.aggTables;
    int numberOfAggregates = aggTables.length;

    List<String[]> cubeMeasures = new ArrayList<String[]>(3);
    
    for(int i = 0 ; i < numberOfAggregates ; i++)
    {
        String aggName = ((MolapDef.AggName)aggTables[i]).getNameAttribute();
        if(aggTableName.equals(aggName))
        {
            MolapDef.AggMeasure[] aggMeasures = aggTables[i].measures;
            int numOfAgg = aggMeasures.length;
            MolapDef.Measure[] measures = cube.measures;
            int numOfMsr = measures.length;
            
            String[] aggregators = new String[numOfAgg];
            String[] measureColumns = new String[numOfAgg];
            
            for(int k=0 ; k < numOfAgg ; k++)
            {
                String name = aggMeasures[k].name;
                String[] split = name.split("\\.");
                
                if(null != split && split.length > 1)
                    {
                        String measureName = split[1];
                        measureName = measureName.substring(
                                measureName.indexOf("[") + 1,
                                measureName.indexOf("]")).trim();

                        for(int l = 0;l < numOfMsr;l++)
                        {
                            if(measureName.equalsIgnoreCase(measures[l].name))
                            {
                                measureColumns[k] = aggMeasures[k].column;
                                aggregators[k] = measures[l].aggregator;
                                break;
                            }
                        }
                    }
            }
            cubeMeasures.add(measureColumns);
            cubeMeasures.add(aggregators);
            break;
        }
    }

    return cubeMeasures;
    
}*/
/**
 * 
 * @param levels
 * @param cube 
 * @return
 * 
 * /
private static String[] getLevelsWithTableName(AggLevel[] levels, Cube cube)
{
    int size = levels.length;
    String []resultLevels = new String[size];
    //String dimensionTable = "";
    for(int i=0 ; i< size ; i++)
    {
        String name = levels[i].name;
        String[] split = name.split("\\.");
        // If only one hierachy exists.
        //[dimensionName].[levelName]
        if(split.length == 2)
        {
            resultLevels[i] = getDimensionTable(split[0],split[1],cube);
        }
        // If more than one hierarchy exists in the same Dimension then 
        // [dimensionName].[hierarchyname].[levelName]
        else if(split.length > 2)
        {
            resultLevels[i]=  getDimensionTable(split[0],split[1],split[2],cube);
        }
        
       // resultLevels[i] = dimensionTable + '_' + levels[i].column;       
    }
    
    return resultLevels;
}*/

/**
 * 
 * @param levelName 
 * @param string
 * @param string2
 * @param cube
 * 
 */
/*private static String getDimensionTable(String dimName, String hierName, String levelName, Cube cube)
{
    CubeDimension[] dimensions = cube.dimensions;
    dimName = dimName.substring(dimName.indexOf("[")+1,dimName.indexOf("]")).trim();
    hierName = hierName.substring(hierName.indexOf("[")+1,hierName.indexOf("]")).trim();
    levelName = levelName.substring(levelName.indexOf("[")+1,levelName.indexOf("]")).trim();
    
    for(CubeDimension dim : dimensions)
    {
        Dimension dimension = (Dimension)dim;
        if(dimName.equals(dimension.name))
        {
            for(Hierarchy hierarchy : dimension.hierarchies)
            {
                if(hierName.equals(hierarchy.name))
                {
                    for(Level levels : hierarchy.levels)
                    {
                        if(levelName.equals(levels.name))
                        {
                            RelationOrJoin relation = hierarchy.relation;
                            String tableName = relation == null ? dimension.name
                                    : ((Table)hierarchy.relation).name;
                            return tableName + '_' + levels.column;
                        }
                    }
                }
            }
        }
    }
    
    return "";
}*/

/**
 * 
 * @param levelName 
 * @param string
 * @param cube 
 * 
 */
/*private static String getDimensionTable(String dimName, String levelName, Cube cube)
{
    CubeDimension[] dimensions = cube.dimensions;
    dimName = dimName.substring(dimName.indexOf("[")+1,dimName.indexOf("]")).trim();
    levelName = levelName.substring(levelName.indexOf("[")+1,levelName.indexOf("]")).trim();
    
    for(CubeDimension dim : dimensions)
    {
        Dimension dimension = (Dimension)dim;
        if(dimName.equals(dimension.name))
        {
            for(Hierarchy hierarchy : dimension.hierarchies)
                {
                    for(Level levels : hierarchy.levels)
                    {
                        if(levelName.equals(levels.name))
                        {
                            RelationOrJoin relation = hierarchy.relation;
                            String tableName = relation == null ? dimension.name
                                    : ((Table)hierarchy.relation).name;
                            return tableName + '_' + levels.column;
                        }

                    }
                }
        }
    }
    
    return "";
}*/
    
    /**
     *  Make the properties string.
     *  Level Entries separated by '&'
     *  Level and prop details separated by ':'
     *  Property column name and index separated by ','
     *   
     *  Level:p1,index1:p2,index2&Level2....   
     *  
     */
    public static int getPropertyString(CubeDimension[] dimensions, StringBuilder propString,int cnt)
    {
        for(CubeDimension cDimension : dimensions)
        {
            Dimension dimension = (Dimension)cDimension;
            for(Hierarchy hierarchy : dimension.hierarchies)
            {
                cnt = generatePropertyString(propString, cnt, hierarchy);
            }
        }
         
        //Delete the last & character
       /* String prop = propString.toString();
        if(prop.length() > 0 && prop.charAt(prop.length()-1) == '&')
        {
            prop = prop.substring(0, prop.length() - 1);
        }*/
        return cnt;
    }

    /**
     * @param propString
     * @param counter
     * @param hierarchy
     * @return
     */
    private static int generatePropertyString(StringBuilder propString,
            int counter, Hierarchy hierarchy)
    {
        for(Level level : hierarchy.levels)
        {
            boolean levelAdded = false;

            // First is ordinal column
            if(hasOrdinalColumn(level))
            {
                if(!levelAdded)
                {
                    levelAdded = true;
                    propString.append(level.column);
                }
                propString.append(":");
                propString.append(level.ordinalColumn);
                propString.append(",");
                propString.append(counter++);
                propString.append(",");
                propString.append("integer");
            }

            // Second is name column
            if(level.nameColumn != null && !"".equals(level.nameColumn))
            {
                if(!levelAdded)
                {
                    levelAdded = true;
                    propString.append(level.column);
                }
                propString.append(":");
                propString.append(level.nameColumn);
                propString.append(",");
                propString.append(counter++);
                propString.append(",");
                propString.append("text");

            }

            // Next all properties
            for(Property property : level.properties)
            {
                if(!levelAdded)
                {
                    levelAdded = true;
                    propString.append(level.column);
                }
                propString.append(":");
                propString.append(property.column);
                propString.append(",");
                propString.append(counter++);
                propString.append(",");
                propString.append(MolapMetadata.getDBDataType(property.type,
                        true));
            }
            if(levelAdded)
            {
                propString.append("&");
            }
        }
        return counter;
    }
    
    /**
     * @param dimensions
     * @return
     */
    public static String getMetaHeirString(CubeDimension[] dimensions)
    {
        StringBuilder propString = new StringBuilder();

        for(CubeDimension cDimension : dimensions)
        {
            Dimension dimension = (Dimension)cDimension;
            for(Hierarchy hierarchy : dimension.hierarchies)
            {
                propString.append(perpareMetaHeirString(dimension, hierarchy));
            }
            // }
            int lastIndexOf = propString.lastIndexOf(":");
            propString.deleteCharAt(lastIndexOf);
            propString.append("&");
        }

        // Delete the last & character
        int lastIndexOf = propString.lastIndexOf("&");
        propString.deleteCharAt(lastIndexOf);
        String prop = propString.toString();
        return prop;
    }

    /**
     * @param propString
     * @param dimension
     * @param hierarchy
     */
    private static String perpareMetaHeirString(Dimension dimension,
            Hierarchy hierarchy)
    {
        StringBuilder propString = new StringBuilder();
        // String tableName = hierarchy.relation.toString();
        RelationOrJoin relation = hierarchy.relation;
        String tableName = relation == null ? dimension.name
                : ((Table)hierarchy.relation).name;
        if(hierarchy.name != null)
        {
            propString.append(tableName + '_' + hierarchy.name);
        }
        else
        {
            propString.append(tableName + '_' + dimension.name);
        }
        propString.append(":");
        for(Level level : hierarchy.levels)
        {
            propString.append(tableName + '_' + level.column);

            // First is ordinal column
            if(hasOrdinalColumn(level))
            {
                propString.append(",");
                propString.append(tableName + '_' + level.ordinalColumn);

            }

            // Second is name column
            if(level.nameColumn != null
                    && !"".equals(tableName + '_' + level.nameColumn))
            {
                propString.append(",");
                propString.append(tableName + '_' + level.nameColumn);
            }

            // Next all properties
            for(Property property : level.properties)
            {
                propString.append(",");
                propString.append(tableName + '_' + property.column);
            }
            propString.append(":");
        }
        return propString.toString();
    }
    
    /**
     * Check whether to consider Ordinal column separately if it is configured.   
     */
    private static boolean hasOrdinalColumn(Level level)
    {
        return (null!=level.ordinalColumn && !level.column.equals(level.ordinalColumn));
    }
    
    /**
     * 
     * @param dimensions
     * @return
     * 
     */
    public static String getTableNameString(CubeDimension[] dimensions)
    {
        StringBuffer stringBuffer = new StringBuffer();
        
        for(CubeDimension cDimension : dimensions)
        {
            Dimension dimension = (Dimension)cDimension;

            for(Hierarchy hierarchy : dimension.hierarchies)
            {
                //String tableName = hierarchy.relation.toString();
                RelationOrJoin relation = hierarchy.relation;
                String tableName = relation==null?dimension.name:((Table)hierarchy.relation).name;
                
                stringBuffer.append(tableName);
                stringBuffer.append("&");
            }
        }
        //Delete the last & character
        int lastIndexOf = stringBuffer.lastIndexOf("&");
        stringBuffer.deleteCharAt(lastIndexOf);
        
        return stringBuffer.toString();
    }
    
    /**
     * Extracts the hierarchy from Dimension or Dimension usage(basedon multiple cubes)
     * @param schemaInfo
     * @param cDimension
     * @param hierarchies
     * @return
     */
    public static Hierarchy[] extractHierarchies(Schema schemaInfo,
            CubeDimension cDimension)
    {
        Hierarchy[] hierarchies = null; 
        if(cDimension instanceof Dimension)
        {
            hierarchies = ((Dimension)cDimension).hierarchies;
        }
        else
        if(cDimension instanceof DimensionUsage)
        {
            String sourceDimensionName = ((DimensionUsage)cDimension).source;
            Dimension[] schemaGlobalDimensions = schemaInfo.dimensions;
            for(Dimension dimension : schemaGlobalDimensions)
            {
                if(sourceDimensionName.equals(dimension.name))
                {
                    hierarchies = dimension.hierarchies;
                }
            }
        }
        return hierarchies;
    }
}
