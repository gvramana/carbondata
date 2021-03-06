/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcfJtSMNYgnOYiEQwbS13nxM8hk/dmbY4B4u+tG
aRAl/g0Ye9xqmTJk/cYaqtL2LaYz8T8UXb6O/7qVJZ5/8R64H9a+l43HxSY5vkobopPvlBqZ
pV+sWATiYYreipZE6LcCYErfAEBTS79ZPtCWwREOQWTC/X1CY4ZAPqdbZAFXlQ==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
*/

package com.huawei.unibi.molap.surrogatekeysgenerator.csvbased;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

import com.huawei.datasight.molap.datatypes.ArrayDataType;
import com.huawei.datasight.molap.datatypes.GenericDataType;
import com.huawei.datasight.molap.datatypes.PrimitiveDataType;
import com.huawei.datasight.molap.datatypes.StructDataType;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.schema.metadata.HierarchiesInfo;
import com.huawei.unibi.molap.util.MolapDataProcessorUtil;

/**
 * Project Name NSE V3R7C00 
 * Module Name : 
 * Author V00900840
 * Created Date :11-Jul-2013 5:17:46 PM
 * FileName : MolapCSVBasedSeqGenMeta.java
 * Class Description :
 * Version 1.0
 */
public class MolapCSVBasedSeqGenMeta extends BaseStepMeta implements StepMetaInterface
{



    /**
     * pkg
     */
    private static Class<?> pkg = MolapCSVBasedSeqGenMeta.class; // for i18n
                                                             // purposes
    
    /**
     * molapdim
     */
    private String molapdim;

    /**
     * molapProps
     */
    private String molapProps;

    /**
     * molapmsr
     */
    private String molapmsr;

    /**
     * molaphier
     */
    private String molaphier;
    
    /**
     * molapMeasureNames
     */
    private String molapMeasureNames;

    /**
     * molaphierColumn
     */
    private String molaphierColumn;

    /**
     * molapTime
     */
    private String molapTime;
    private String  highCardinalityDims;

//    /**
//     * storeLocation
//     */
//    private String storeLocation;

    /**
     * molapJNDI
     */
//    private String molapJNDI;

    /**
     * molapSchema
     */
    private String molapSchema;

    /**
     * batchSize
     */
    private int batchSize = 10000;

    /**
     * isAggregate
     */
    private boolean isAggregate;

    /**
     * generateDimFiles
     */
    private boolean generateDimFiles;

    /**
     * storeType
     */
    private String storeType;

    /**
     * metaHeirSQLQuery
     */
    private String metaHeirSQLQuery;

    /**
     * molapMetaHier
     */
    private String molapMetaHier;
    
    /**
     * Foreign key and respective hierarchy String
     */
    private String foreignKeyHierarchyString;
    
    /**
     * Foreign key and respective hierarchy Map
     */
    protected Map<String, String> foreignKeyHierarchyMap;
    
    /**
     * heirNadDimsLensString
     */
    private String heirNadDimsLensString;
    
    /**
     * measureDataType
     */
    private String measureDataType;
    
    /**
     * measureSurrogateRequired
     */
    private Map<String, Boolean> measureSurrogateRequired;
    
    /**
     * hier name
     */
    protected String[] hierNames;
    
    private String heirKeySize;
    
    /**
     * checkPointFileExits
     */
    private String checkPointFileExits;
    
    private String complexDelimiterLevel1;
    
	private String complexDelimiterLevel2;

    /**
     * dims
     */
    protected int[] dims;
 
    /**
     * dims
     */
    protected Map<String, GenericDataType> complexTypes = new HashMap<String, GenericDataType>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
    
    private String complexTypeString;

    public Map<String, GenericDataType> getComplexTypes() {
		return complexTypes;
	}

	public void setComplexTypes(Map<String, GenericDataType> complexTypes) {
		this.complexTypes = complexTypes;
	}
	
	public String getComplexDelimiterLevel1() {
		return complexDelimiterLevel1;
	}

	public void setComplexDelimiterLevel1(String complexDelimiterLevel1) {
		this.complexDelimiterLevel1 = complexDelimiterLevel1;
	}

	public String getComplexDelimiterLevel2() {
		return complexDelimiterLevel2;
	}

	public void setComplexDelimiterLevel2(String complexDelimiterLevel2) {
		this.complexDelimiterLevel2 = complexDelimiterLevel2;
	}
	
	public String getComplexTypeString() {
		return complexTypeString;
	}

	public void setComplexTypeString(String complexTypeString) {
		this.complexTypeString = complexTypeString;
	}

	/**
     * dimLens
     */
    protected int[] dimLens;

    /**
     * msrs
     */
    protected int[] msrs;

    /**
     * timehierName
     */
    protected String timehierName;

    /**
     * hirches
     */
    protected Map<String, int[]> hirches;
    
    /**
     * Hierarchy Column Name map
     */
    protected Map<String,String[]> hierColumnMap;
    
    /**
     * Primary Key String
     */
    private String primaryKeysString;
    
    /**
     * foreign key Column name string
     */
    private String forgienKeyPrimayKeyString;
    
    /**
     * Primary Key Map.
     */
    private Map<String, Boolean> primaryKeyMap;
    
    /**
     * timeFormat
     */
    protected SimpleDateFormat timeFormat;
    
    /**
     * 
     */
    private Map<String, String> hierDimTableMap;

    /**
     * timeIndex
     */
//    protected int timeIndex = -1;

    /**
     * timeDimeIndex
     */
    protected int timeDimeIndex = -1;

    /**
     * timeLevels
     */
    protected String[] timeLevels = new String[0];

    /**
     * timeOrdinalCols
     */
    protected String[] timeOrdinalCols = new String[0];

    /**
     * timeOrdinalIndices
     */
    protected int[] timeOrdinalIndices = new int[0];

    /**
     * dimColNames
     */
    protected String[] dimColNames;
    
    /**
     * 
     */
    protected String[] highCardCols;

    /**
     * propColumns
     */
    private List<String>[] propColumns;

    /**
     * propTypes
     */
    private List<String>[] propTypes;

    /**
     * propIndxs
     */
    private int[][] propIndxs;

    /**
     * metahierVoList
     */
    private List<HierarchiesInfo> metahierVoList;
    
    /**
     * dimesionTableNames
     */
    private String dimesionTableNames;
    
    /**
     * dimTableArray
     */
    private String[] dimTableArray;
    
    /**
     * tableName
     */
    private String tableName;
    
    /**
     * measureColumn
     */
    protected String[] measureColumn;
    
    /**
     * msrMapping
     */
    protected int[] msrMapping;
    
    /**
     * dimMapping
     */
    protected int[] dimMapping;
    
    /**
     * MOdified Dimension
     */
    private String[] modifiedDimension;
    
    /**
     * actualDimNames
     */
    private String actualDimNames; 
    
    private String normHiers;
    /**
     * dims
     */
    protected boolean[] dimPresent;
    
    protected int normLength;
    
    
    protected List<String> normHierList;
    
    /**
     * actualDimArrays
     */
    protected String[] actualDimArray;
	
    /**
     * msrAggregatorString
     */
    private String msrAggregatorString;
    
    /**
     * mrsAggregators
     */
    protected String []msrAggregators;
    
    /**
     * columnAndTableName_ColumnMapForAgg
     */
    protected Map<String,String> columnAndTableNameColumnMapForAggMap; 
    
    /**
     * columnAndTableName_ColumnMapForAggString
     */
    private String columnAndTableNameColumnMapForAggString;
    
    private String connectionURL;
    
    private String driverClass;
    
    private String userName;
    
    private String password;
    
    /**
     * denormColumNames
     */
    private String denormColumNames;
    
    /**
     * denormColumnList
     */
    protected List<String> denormColumnList;
    
    /**
     * cubeName
     */
    private String cubeName;
    
    /**
     * schemaName
     */
    private String schemaName;
    /**
     * Member Mapping
     */
    protected int[]memberMapping;
    
    protected Map<String, String> foreignKeyPrimaryKeyMap;
    
    private int currentRestructNumber;

    public void setMolapMetaHier(String molapMetaHier)
    {
        this.molapMetaHier = molapMetaHier;
    }
    
    public String getMolapMetaHier()
    {
        return molapMetaHier;
    }
    
    public String getMetaHeirSQLQueries()
    {
        return metaHeirSQLQuery;
    }

    public void setMetaMetaHeirSQLQueries(String metaHeirSQLQuery)
    {
        this.metaHeirSQLQuery = metaHeirSQLQuery;
    }
   
    /**
     * 
     * @param isInitialLoad The isInitialLoad to set.
     * 
     */
    public void setAggregate(boolean isAggregate)
    {
        this.isAggregate = isAggregate;
    }
    
    /**
     * 
     * @return Returns the isInitialLoad.
     * 
     */
    public boolean isAggregate()
    {
        return isAggregate;
    }

    
    public int getBatchSize()
    {
        return batchSize;
    }

    public void setBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
    }

    
    /**
     * 
     * @return Returns the storeType.
     * 
     */
    public String getStoreType()
    {
        return storeType;
    }

    /**
     * 
     * @param storeType The storeType to set.
     * 
     */
    public void setStoreType(String storeType)
    {
        this.storeType = storeType;
    }

    /**
     * @return the molapSchema
     */
    public String getMolapSchema() {
        return molapSchema;
    }

    /**
     * @param molapSchema the molapSchema to set
     */
    public void setMolapSchema(String molapSchema) {
        this.molapSchema = molapSchema;
    }

//    /**
//     * @return the molapCon
//     */
//    public String getMolapJNDIName() {
//        return molapJNDI;
//    }
//
//    /**
//     * @param molapCon the molapCon to set
//     */
//    public void setMolapJNDIName(String jndiName) {
//        this.molapJNDI = jndiName;
//    }


    
    public List<HierarchiesInfo> getMetahierVoList()
    {
        return metahierVoList;
    }

    public void setMetahierVoList(List<HierarchiesInfo> metahierVoList)
    {
        this.metahierVoList = metahierVoList;
    }

    /**
     * @return the molapTime
     */
    public String getMolapTime() {
        return molapTime;
    }

    /**
     * @param molapTime the molapTime to set
     */
    public void setMolapTime(String molapTime) {
        this.molapTime = molapTime;
    }

  
    public MolapCSVBasedSeqGenMeta()
    {
        super(); 
    }
    
    // getters and setters for the step settings
    
    public void setMolapProps(String molapProps)
    {
        this.molapProps = molapProps;
    }
    
    public String getMolapProps()
    {
        return molapProps;
    }    

    public String getMolapmsr() 
    {
        return molapmsr;
    }

    public void setMolapmsr(String molapmsr) 
    {
        this.molapmsr = molapmsr;
    }
    
    public String getMolapdim() 
    {
        return molapdim;
    }

    public void setMolapdim(String molapdim) 
    {
        this.molapdim = molapdim;
    }


    public String getMolapHier() {
        return molaphier;
    }

    public void setMolaphier(String molaphier) {
        this.molaphier = molaphier;
    }
    
    
    /**
	 * @return the connectionURL
	 */
	public String getConnectionURL() {
		return connectionURL;
	}

	/**
	 * @param connectionURL the connectionURL to set
	 */
	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	/**
	 * @return the driverClass
	 */
	public String getDriverClass() {
		return driverClass;
	}
	
	//TODO SIMIAN	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param driverClass the driverClass to set
	 */
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
     * 
     * @return Returns the generateDimFiles.
     * 
     */
    public boolean isGenerateDimFiles()
    {
        return generateDimFiles;
    }

    /**
     * 
     * @param generateDimFiles The generateDimFiles to set.
     * 
     */
    public void setGenerateDimFiles(boolean generateDimFiles)
    {
        this.generateDimFiles = generateDimFiles;
    }

    /**
     * set sensible defaults for a new step
     * 
     * @see org.pentaho.di.trans.step.StepMetaInterface#setDefault()
     */
    public void setDefault() {
        molapProps="";
        molapdim = "";
        molapmsr = "";
        molaphier = "";
        molapTime="";
        //
//        molapJNDI = "";
        driverClass = "";
        connectionURL = "";
        userName = "";
        password = "";
        molapSchema = "";
        storeType = "";
        isAggregate=false;
        metaHeirSQLQuery="";
        molapMetaHier="";
        dimesionTableNames="";
        highCardinalityDims="";
        tableName="";
        molaphierColumn="";
        foreignKeyHierarchyString="";
        complexTypeString="";
        complexDelimiterLevel1 = "";
        complexDelimiterLevel2 = "";
        primaryKeysString="";
        molapMeasureNames = "";
        actualDimNames = "";
        normHiers = "";
		msrAggregatorString = "";
		heirKeySize="";
		heirNadDimsLensString="";
		measureDataType="";
		columnAndTableNameColumnMapForAggString="";
		checkPointFileExits="";
		denormColumNames="";
		currentRestructNumber = -1;
        //
    }   
    
    // helper method to allocate the arrays
    public void allocate(int nrkeys){
        
        
    }

   /* public Object clone() {
        
        // field by field copy is default
        MolapCSVBasedSeqGenMeta retval = (MolapCSVBasedSeqGenMeta) super.clone();
        
        // add proper deep copy for the collections
//      int nrKeys   = keyField.length;

    
        return retval;
    }   */
    
    /**
     * @see org.pentaho.di.trans.step.BaseStepMeta#getXML()
     */
    public String getXML() throws KettleValueException {
        //
        StringBuffer retval = new StringBuffer(150);
        //
        retval.append("    ").append(XMLHandler.addTagValue("molapProps", molapProps));
        retval.append("    ").append(XMLHandler.addTagValue("dim", molapdim));
        retval.append("    ").append(XMLHandler.addTagValue("msr", molapmsr));
        retval.append("    ").append(XMLHandler.addTagValue("hier", molaphier));
        retval.append("    ").append(XMLHandler.addTagValue("time", molapTime));
//        retval.append("    ").append(XMLHandler.addTagValue("con", molapJNDI));
        retval.append("    ").append(XMLHandler.addTagValue("driverClass", driverClass));
        retval.append("    ").append(XMLHandler.addTagValue("connectionURL", connectionURL));
        retval.append("    ").append(XMLHandler.addTagValue("userName", userName));
        retval.append("    ").append(XMLHandler.addTagValue("password", password));
        //
        retval.append("    ").append(XMLHandler.addTagValue("batchSize", batchSize));
        retval.append("    ").append(XMLHandler.addTagValue("genDimFiles", generateDimFiles));
        retval.append("    ").append(XMLHandler.addTagValue("isAggregate", isAggregate));
        retval.append("    ").append(XMLHandler.addTagValue("storeType", storeType));
        retval.append("    ").append(XMLHandler.addTagValue("metadataFilePath", metaHeirSQLQuery));
        retval.append("    ").append(XMLHandler.addTagValue("molapMetaHier", molapMetaHier));
        retval.append("    ").append(XMLHandler.addTagValue("foreignKeyHierarchyString", foreignKeyHierarchyString));
        retval.append("    ").append(XMLHandler.addTagValue("complexTypeString", complexTypeString));
        retval.append("    ").append(XMLHandler.addTagValue("complexDelimiterLevel1", complexDelimiterLevel1));
        retval.append("    ").append(XMLHandler.addTagValue("complexDelimiterLevel2", complexDelimiterLevel2));
        retval.append("    ").append(XMLHandler.addTagValue("primaryKeysString", primaryKeysString));
        retval.append("    ").append(XMLHandler.addTagValue("molapMeasureNames", molapMeasureNames));
        retval.append("    ").append(XMLHandler.addTagValue("actualDimNames", actualDimNames));
        retval.append("    ").append(XMLHandler.addTagValue("msrAggregatorString", msrAggregatorString));
        
        
        retval.append("    ").append(XMLHandler.addTagValue("dimHierReleation", dimesionTableNames));
        retval.append("    ").append(XMLHandler.addTagValue("dimHighCardinality", highCardinalityDims));
        retval.append("    ").append(XMLHandler.addTagValue("factOrAggTable", tableName));
        retval.append("    ").append(XMLHandler.addTagValue("molaphierColumn", molaphierColumn));
        retval.append("    ").append(XMLHandler.addTagValue("normHiers", normHiers));
        retval.append("    ").append(XMLHandler.addTagValue("heirKeySize", heirKeySize));
        
        retval.append("    ").append(XMLHandler.addTagValue("forgienKeyPrimayKeyString", forgienKeyPrimayKeyString));
        retval.append("    ").append(XMLHandler.addTagValue("heirNadDimsLensString", heirNadDimsLensString));
        retval.append("    ").append(XMLHandler.addTagValue("measureDataType", measureDataType));
        retval.append("    ").append(XMLHandler.addTagValue("columnAndTableName_ColumnMapForAggString", columnAndTableNameColumnMapForAggString));
		retval.append("    ").append(XMLHandler.addTagValue("schemaName", schemaName));
        retval.append("    ").append(XMLHandler.addTagValue("cubeName", cubeName));
        retval.append("    ").append(XMLHandler.addTagValue("checkPointFileExits", checkPointFileExits));
        retval.append("    ").append(XMLHandler.addTagValue("denormColumNames", denormColumNames));
        retval.append("    ").append(XMLHandler.addTagValue("currentRestructNumber", currentRestructNumber));
        
        
        
        //
        return retval.toString();
    }
    
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException 
    {

        try {
            
            molapProps = XMLHandler.getTagValue(stepnode, "molapProps");
            molapdim = XMLHandler.getTagValue(stepnode, "dim");
            molapmsr = XMLHandler.getTagValue(stepnode, "msr");
            molaphier = XMLHandler.getTagValue(stepnode, "hier");
            molapTime = XMLHandler.getTagValue(stepnode, "time");
//            molapJNDI = XMLHandler.getTagValue(stepnode, "con");
            driverClass = XMLHandler.getTagValue(stepnode, "driverClass");
            connectionURL = XMLHandler.getTagValue(stepnode, "connectionURL");
            userName = XMLHandler.getTagValue(stepnode, "userName");
            password = XMLHandler.getTagValue(stepnode, "password");
            molapMetaHier = XMLHandler.getTagValue(stepnode, "molapMetaHier");
            molaphierColumn = XMLHandler.getTagValue(stepnode, "molaphierColumn");
            foreignKeyHierarchyString = XMLHandler.getTagValue(stepnode, "foreignKeyHierarchyString");
            complexTypeString = XMLHandler.getTagValue(stepnode, "complexTypeString");
            complexDelimiterLevel1 = XMLHandler.getTagValue(stepnode, "complexDelimiterLevel1");
            complexDelimiterLevel2 = XMLHandler.getTagValue(stepnode, "complexDelimiterLevel2");
            primaryKeysString = XMLHandler.getTagValue(stepnode, "primaryKeysString");
            molapMeasureNames = XMLHandler.getTagValue(stepnode, "molapMeasureNames");
            actualDimNames = XMLHandler.getTagValue(stepnode, "actualDimNames");
            normHiers = XMLHandler.getTagValue(stepnode, "normHiers");
            msrAggregatorString = XMLHandler.getTagValue(stepnode, "msrAggregatorString");
            heirKeySize = XMLHandler.getTagValue(stepnode, "heirKeySize");
            forgienKeyPrimayKeyString = XMLHandler.getTagValue(stepnode, "forgienKeyPrimayKeyString");
            heirNadDimsLensString = XMLHandler.getTagValue(stepnode, "heirNadDimsLensString");
            measureDataType = XMLHandler.getTagValue(stepnode, "measureDataType");
            columnAndTableNameColumnMapForAggString=XMLHandler.getTagValue(stepnode, "columnAndTableName_ColumnMapForAggString");
            dimesionTableNames = XMLHandler.getTagValue(stepnode, "dimHierReleation");
            highCardinalityDims=XMLHandler.getTagValue(stepnode, "dimHighCardinality");
            tableName = XMLHandler.getTagValue(stepnode, "factOrAggTable");
			cubeName = XMLHandler.getTagValue(stepnode, "cubeName");
            schemaName = XMLHandler.getTagValue(stepnode, "schemaName");
            checkPointFileExits = XMLHandler.getTagValue(stepnode, "checkPointFileExits");
            denormColumNames = XMLHandler.getTagValue(stepnode, "denormColumNames");
            currentRestructNumber = Integer.parseInt(XMLHandler.getTagValue(stepnode, "currentRestructNumber"));

            String batchConfig = XMLHandler.getTagValue(stepnode, "batchSize");
            
            if(batchConfig!=null)
            {
                batchSize =  Integer.parseInt(batchConfig);
            }
            
            String dimeFileConfig = XMLHandler.getTagValue(stepnode, "genDimFiles");
            if(dimeFileConfig!=null)
            {
                generateDimFiles =  Boolean.parseBoolean(dimeFileConfig);
            }
            
            storeType = XMLHandler.getTagValue(stepnode, "storeType");
            metaHeirSQLQuery = XMLHandler.getTagValue(stepnode, "metadataFilePath");
            
            isAggregate = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "isAggregate"));
            
            
            int nrKeys = XMLHandler.countNodes(stepnode, "lookup"); 
            allocate(nrKeys);
//          intialize();
            
        } catch (Exception e) {
            throw new KettleXMLException("Template Plugin Unable to read step info from XML node", e);
        }

    }   
    
    public void initialize() throws KettleException
    {
        if(null != complexTypeString)
        {
            complexTypes = getComplexTypesMap(complexTypeString);
        }
        updateDimensions(molapdim,molapmsr,highCardinalityDims);
        
        hirches = getHierarichies(molaphier);

        hierColumnMap = getHierarchiesColumnMap(molaphierColumn);
        
        foreignKeyHierarchyMap = getForeignKeyHierMap(foreignKeyHierarchyString);

        
        primaryKeyMap = updatePrimaryKeyMap(primaryKeysString);
        
        foreignKeyPrimaryKeyMap = getForeignKeyColumnNameMap(forgienKeyPrimayKeyString);
        
        actualDimArray = getActualDimensionArray(actualDimNames);
        
        normHierList = getNormHierList(normHiers);
        
        //update non time dimension properties
        updateDimProperties();
        
        //update the meta Hierarichies list 
        getMetaHierarichies(molapMetaHier);
        
        updateMetaHierarichiesWithQueries(metaHeirSQLQuery);
        
        updateMeasureAggregator(msrAggregatorString);
        
        measureSurrogateRequired = getMeasureDatatypeMap(measureDataType);
        
        updateHierDimTableMap(dimesionTableNames);
        
        if(isAggregate)
        {
        	columnAndTableNameColumnMapForAggMap = new HashMap<String, String>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
            String[] split = columnAndTableNameColumnMapForAggString.split(MolapCommonConstants.HASH_SPC_CHARACTER);
            for(int i = 0;i < split.length;i++)
            {
                String[] split2 = split[i].split(MolapCommonConstants.HYPHEN_SPC_CHARACTER);
                columnAndTableNameColumnMapForAggMap.put(split2[0], split2[1]);
            }
        }
        
        updateDenormColunList(denormColumNames);
        
        
    }
    
    /**
     * 
     * @param denormColumNames2
     * 
     */
    private void updateDenormColunList(String denormColumNames)
    {
        //
        if(null==denormColumNames || "".equals(denormColumNames))
        {
            denormColumnList = new ArrayList<String>(1);
            return;
        }
        
        String[] columnNames = denormColumNames.split(MolapCommonConstants.HASH_SPC_CHARACTER);
        
        if(null == denormColumnList)
        {
            denormColumnList = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        }
        
        for(String colName : columnNames)
        {
            denormColumnList.add(colName);
        }
    }

    /**
     * 
     * @param dimesionTableNames2
     * 
     */
    private void updateHierDimTableMap(String dimesionTableNames)
    {
        //
        if(null==dimesionTableNames || "".equals(dimesionTableNames))
        {
            return;
        }
        
        String[] hierTableName = dimesionTableNames.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);
        
        if(null == hierDimTableMap)
        {
            hierDimTableMap = new HashMap<String, String>(hierTableName.length);
        }
        if(null == dimTableArray)
        {
            dimTableArray = new String[hierTableName.length];
        }
        int i=0;
        for(String hierTable : hierTableName)
        {
            String[] hierAndTable = hierTable.split(MolapCommonConstants.COLON_SPC_CHARACTER);
            hierDimTableMap.put(hierAndTable[0], hierAndTable[1]);
            dimTableArray[i++] = hierAndTable[1];
        }
    }

    /**
     * 
     * @param measureDataType2
     * @return
     * 
     */
    private Map<String, Boolean> getMeasureDatatypeMap(String measureDataType)
    {
        if(measureDataType == null
                || "".equals(measureDataType))
        {
            return new HashMap<String, Boolean>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        }
        Map<String, Boolean> resultMap = new HashMap<String, Boolean>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);

        String[] measures = measureDataType.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);

        for(int i = 0;i < measures.length;i++)
        {
            String[] measureValue = measures[i].split(MolapCommonConstants.COLON_SPC_CHARACTER);
            resultMap.put(measureValue[0], Boolean.valueOf(measureValue[1]));
        }
        return resultMap;
    }

    /**
     * 
     * @param msrAggregatorString2
     * 
     */
    private void updateMeasureAggregator(String msrAggregatorString)
    {
        String[] split = msrAggregatorString.split(MolapCommonConstants.SEMICOLON_SPC_CHARACTER);
        msrAggregators = new String[split.length];
        System.arraycopy(split, 0, msrAggregators, 0, split.length);
    }

    /**
     * 
     * @param actualDimNames2
     * @return
     * 
     */
    private String[] getActualDimensionArray(String actualDimNames)
    {
        if(actualDimNames == null
                || "".equals(actualDimNames))
        {
            return new String[0];
        }

       return actualDimNames.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);
        
       
    }
    
    private List<String> getNormHierList(String normHier)
    {
        List<String> hierList = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        if(null!=normHier && normHier.length()!=0)
        {
            String[] split = normHier.split(MolapCommonConstants.COMA_SPC_CHARACTER);

            for(int i = 0;i < split.length;i++)
            {
                hierList.add(split[i]);
            }
        }
        return hierList;
    }
    
    /**
     * 
     * @param foreignKeyColumnNameString2
     * @return
     * 
     */
    private Map<String, String> getForeignKeyColumnNameMap(
            String foreignKeyColumnNameString)
    {
        if(foreignKeyColumnNameString == null
                || "".equals(foreignKeyColumnNameString))
        {
            return new HashMap<String, String>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        }
        Map<String, String> map = new HashMap<String, String>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);

        String[] foreignKeys = foreignKeyColumnNameString.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);

        for(int i = 0;i < foreignKeys.length;i++)
        {
            String[] foreignHierArray = foreignKeys[i].split(MolapCommonConstants.COLON_SPC_CHARACTER);
            String hiers = map.get(foreignHierArray[0]);
            
            if(null == hiers)
            {
                map.put(foreignHierArray[0], foreignHierArray[1]);
            }
            else
            {
               map.put(foreignHierArray[0], hiers + MolapCommonConstants.COMA_SPC_CHARACTER + foreignHierArray[1]);
            }
        }
        return map;
    }

    /**
     * 
     * @param primaryKeysString2
     * @return
     * 
     */
    private Map<String, Boolean> updatePrimaryKeyMap(String primaryKeysString)
    {
        if(primaryKeysString == null
                || "".equals(primaryKeysString))
        {
            return new HashMap<String, Boolean>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        }
        Map<String, Boolean> resultMap = new HashMap<String, Boolean>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);

        String[] primaryKeys = primaryKeysString.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);

        for(int i = 0;i < primaryKeys.length;i++)
        {
            resultMap.put(primaryKeys[i], true);
        }
        return resultMap;
    }

    public void updateHierMappings(RowMetaInterface metaInterface)
    {
//        hierNames = new String[primaryKeyColumnMap.size()];
        List<String> actualHierList = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        for(int j=0;j<metaInterface.size();j++)
        {
            String foreignKey = metaInterface.getValueMeta(j).getName();
            String actualHier = foreignKeyHierarchyMap.get(foreignKey);
            if(null != actualHier)
            {
                if(actualHier.indexOf(MolapCommonConstants.COMA_SPC_CHARACTER) > -1)
                {
                    String[] splitHier = actualHier.split(MolapCommonConstants.COMA_SPC_CHARACTER);
                    for(String hier : splitHier)
                    {
                        actualHierList.add(hier);
                    }
                }
                else
                {
                    actualHierList.add(actualHier);
                }
//                hierNames[k++] = actualHier;
            }
        }
        
        hierNames = new String[actualHierList.size()];
        hierNames = actualHierList.toArray(new String[actualHierList.size()]);
    }

    
    private Map<String,GenericDataType> getComplexTypesMap(String complexTypeString)
    {
    	Map<String,GenericDataType> complexTypesMap = new LinkedHashMap<String,GenericDataType>();
    	String[] hierarchies = complexTypeString.split(MolapCommonConstants.SEMICOLON_SPC_CHARACTER);
        for(int i = 0;i < hierarchies.length;i++)
        {
            String[] levels = hierarchies[i].split(MolapCommonConstants.HASH_SPC_CHARACTER);
            String[] levelInfo = levels[0].split(MolapCommonConstants.COLON_SPC_CHARACTER);
			GenericDataType g = levelInfo[1].equals("Array")?
						new ArrayDataType(levelInfo[0], ""):new StructDataType(levelInfo[0], "");
			complexTypesMap.put(levelInfo[0], g);
            for(int j = 1;j < levels.length;j++)
            {
            	levelInfo = levels[j].split(MolapCommonConstants.COLON_SPC_CHARACTER);
				switch(levelInfo[1])
				{
					case "Array" : 
						g.addChildren(new ArrayDataType(levelInfo[0], levelInfo[2]));
						break;
					case "Struct" : 
						g.addChildren(new StructDataType(levelInfo[0], levelInfo[2]));
						break;
					default :
						g.addChildren(new PrimitiveDataType(levelInfo[0], levelInfo[2]));
				}
            }
        }
        return complexTypesMap;
    }
    /**
     * 
     * @param foreignKeyHierarchyString2
     * @return
     * 
     */
    private Map<String, String> getForeignKeyHierMap(
            String foreignKeyHierarchyString)
    {
        if(foreignKeyHierarchyString == null
                || "".equals(foreignKeyHierarchyString))
        {
            return new HashMap<String, String>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        }
        Map<String, String> map = new HashMap<String, String>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);

        String[] hies = foreignKeyHierarchyString.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);

        for(int i = 0;i < hies.length;i++)
        {
            String[] foreignHierArray = hies[i].split(MolapCommonConstants.COLON_SPC_CHARACTER);
            String hiers = map.get(foreignHierArray[0]);
            
            if(null == hiers)
            {
                map.put(foreignHierArray[0], foreignHierArray[1]);
            }
            else
            {
               map.put(foreignHierArray[0], hiers + MolapCommonConstants.COMA_SPC_CHARACTER + foreignHierArray[1]);
            }

        }
        return map;
    }

    /**
     * 
     * @param molaphierColumn
     * @return
     * 
     */
    private Map<String, String[]> getHierarchiesColumnMap(
            String molaphierColumn)
    {
        if(molaphierColumn == null || "".equals(molaphierColumn))
        {
            return new HashMap<String, String[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        }
        Map<String, String[]> map = new HashMap<String, String[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        
        String[] hies = molaphierColumn.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);
        
        for (int i = 0; i < hies.length; i++) 
        {
            String hie = hies[i];
            
            String hierName = hie.substring(0, hie.indexOf(MolapCommonConstants.COLON_SPC_CHARACTER));
            
            String[] columnArray = getStringArray(hie.substring(hie.indexOf(MolapCommonConstants.COLON_SPC_CHARACTER)+MolapCommonConstants.COLON_SPC_CHARACTER.length(),hie.length()));
            map.put(hierName, columnArray);
        }
        return map;
    }
    
    private String[] getStringArray(String columnNames)
    {
        String [] splitedColumnNames = columnNames.split(MolapCommonConstants.COMA_SPC_CHARACTER);
        String[] columns = new String[splitedColumnNames.length];
        
//        for (int i = 0; i < columns.length; i++) 
//        {
//            columns[i] = splitedColumnNames[i];
//        }
        System.arraycopy(splitedColumnNames, 0, columns, 0, columns.length);
        return columns;
    }

    private void getMetaHierarichies(String molapMetaHier)
    {
        //
        if(null==molapMetaHier || "".equals(molapMetaHier))
        {
            return;
        }
        String[] metaHier = molapMetaHier.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);
        metahierVoList = new ArrayList<HierarchiesInfo>(metaHier.length);
        Map<String, String[]> columnPropsMap = new HashMap<String, String[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        for(int i = 0;i < metaHier.length;i++)
        {
            //
            HierarchiesInfo hierarichiesVo = new HierarchiesInfo();
            String[] isTimeDim = metaHier[i].split(MolapCommonConstants.HASH_SPC_CHARACTER);
            
            String[] timeDimCols = isTimeDim[1].split(MolapCommonConstants.SEMICOLON_SPC_CHARACTER);
            
            if(null != timeDimCols && "true".equalsIgnoreCase(timeDimCols[0]))
            {
                Map<String, String> levelTypeColumnMap = new HashMap<String, String>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
                
                if(timeDimCols.length > 1 && !timeDimCols[1].isEmpty())
                {
                    String[] levelTypeColumns = timeDimCols[1].split(MolapCommonConstants.STAR_SPC_CHARACTER);
                    
                    for(String levelsColumns : levelTypeColumns)
                    {
                        String[] split = levelsColumns.split(MolapCommonConstants.COMA_SPC_CHARACTER);
                        levelTypeColumnMap.put(split[0], split[1]);
                    }
                    
                }
                hierarichiesVo.setLevelTypeColumnMap(levelTypeColumnMap);
                hierarichiesVo.setTimeDimension(true);
            }
            

            String[] split = isTimeDim[0].split(MolapCommonConstants.COLON_SPC_CHARACTER);
            String[] columnNames = new String[split.length - 1];
            int[] columnIndex = new int[split.length - 1];
            hierarichiesVo.setHierarichieName(split[0]);
            if(null!=hirches.get(split[0]))
            {
                hierarichiesVo.setLoadToHierarichiTable(true);
            }
            int index = 0;
            for(int j = 1;j < split.length;j++)
            {
                //
                String[] columnAndPropertyNames = split[j].split(MolapCommonConstants.COMA_SPC_CHARACTER);//CHECKSTYLE:OFF    Approval No:Approval-323
                columnNames[index] = columnAndPropertyNames[0];//CHECKSTYLE:ON
                columnIndex[index] = getColumnIndex(columnNames[index]);
                String[] properties = new String[columnAndPropertyNames.length - 1];
//                int propertyIndex = 0;
//                for(int k = 1;k < columnAndPropertyNames.length;k++)
//                {
//                    //
//                    properties[propertyIndex] = columnAndPropertyNames[k];
//                    propertyIndex++;
//                }
                System.arraycopy(columnAndPropertyNames, 1, properties, 0, columnAndPropertyNames.length - 1);
                if(null == columnPropsMap.get(columnNames[index]))
                {
                    columnPropsMap.put(columnNames[index], properties);
                }
                index++;
            }
            hierarichiesVo.setColumnIndex(columnIndex);
            hierarichiesVo.setColumnNames(columnNames);
            hierarichiesVo.setColumnPropMap(columnPropsMap);
            metahierVoList.add(hierarichiesVo);
        }
    }
    
    private void updateMetaHierarichiesWithQueries(String molapLocation)
    {
        //
        if(null==molapLocation)
        {
            return;
        }
        String[] hierWithQueries = molapLocation.split(MolapCommonConstants.HASH_SPC_CHARACTER);
        //
        for(String hierarchyWithQuery : hierWithQueries)
        {
            String[] hierQueryStrings = hierarchyWithQuery.split(MolapCommonConstants.COLON_SPC_CHARACTER);

            Iterator<HierarchiesInfo> iterator = metahierVoList.iterator();
            while(iterator.hasNext())
            {
                //
                HierarchiesInfo next = iterator.next();
                if(hierQueryStrings[0].equalsIgnoreCase(next.getHierarichieName()))
                {   
                    next.setQuery(hierQueryStrings[1]);
                    break;
                }

            }
        }
        
    }
    
    private int getColumnIndex(String colNames)
    {
        for(int j = 0;j < dimColNames.length;j++)
        {
            if(dimColNames[j].equalsIgnoreCase(colNames))
            {
                return j;
            }
        }
        return -1;
    }

    /**
     *  Parse the properties string.
     *  
     *  Level Entries separated by '&'
     *  Level and prop details separated by ':'
     *  Property column name and index separated by ','
     *   
     *  Level:p1,index1:p2,index2&Level2....   
     *  
     */
    private void updateDimProperties()
    {  
        Map<String, int[]> indices = new HashMap<String, int[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        Map<String, String[]> columns = new HashMap<String, String[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        Map<String, String[]> dbTypes = new HashMap<String, String[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        
        if(molapProps!=null && !"".equals(molapProps))
        {
            String[] entries = molapProps.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);
            for(int i=0; i<entries.length; i++)
            {
                String[] levelEntry = entries[i].split(MolapCommonConstants.COLON_SPC_CHARACTER);
                String dimColumnName = levelEntry[0];
                int[] pIndices = new int[levelEntry.length-1];
                String[] cols = new String[levelEntry.length-1];
                String[] dbType = new String[levelEntry.length-1];
                for(int j=1; j< levelEntry.length; j++)
                {
                    String[] propEntry = levelEntry[j].split(MolapCommonConstants.COMA_SPC_CHARACTER);
                    pIndices[j-1] = Integer.parseInt(propEntry[1]);
                    
                    //CHECKSTYLE:OFF    Approval No:Approval-324,325
                    cols[j-1]  =  propEntry[0];
                    dbType[j-1]  =  propEntry[2];
                }//CHECKSTYLE:ON
                
                indices.put(dimColumnName, pIndices);
                columns.put(dimColumnName, cols);
                dbTypes.put(dimColumnName, dbType);
            }
        }
        
        if(indices.isEmpty())
        {
            return;
        }
        
        propColumns = new List[dimColNames.length];
        propTypes = new List[dimColNames.length];
        propIndxs = new int[dimColNames.length][];
        
        
        //Fill the property details based on the map created 
        for(int i=0; i< dimColNames.length; i++)
        {
            //Properties present or not
            if(indices.containsKey(dimColNames[i]))
            {
                propColumns[i] =  Arrays.asList(columns.get(dimColNames[i]));
                propTypes[i] =  Arrays.asList(dbTypes.get(dimColNames[i]));
                propIndxs[i] = indices.get(dimColNames[i]);
            }
            else
            {
                propColumns[i] =  new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
                propTypes[i] =  new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
                propIndxs[i] = new int[0];
            }
        }
    }

    private Map<String, int[]> getHierarichies(String ds)
    {
        if(ds == null || "".equals(ds))
        {
            return new HashMap<String, int[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        }
        Map<String, int[]> map = new HashMap<String, int[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        
        String[] hies = ds.split(MolapCommonConstants.AMPERSAND_SPC_CHARACTER);
        
        for (int i = 0; i < hies.length; i++) 
        {
            String hie = hies[i];
            
            String name = hie.substring(0, hie.indexOf(MolapCommonConstants.COLON_SPC_CHARACTER));
            
            int[] a = getIntArray(hie.substring(hie.indexOf(MolapCommonConstants.COLON_SPC_CHARACTER)+MolapCommonConstants.COLON_SPC_CHARACTER.length(),hie.length()));
            map.put(name, a);
        }
        return map;
    }

    private int[] getIntArray(String ds)
    {
        
        String [] sp = ds.split(MolapCommonConstants.COMA_SPC_CHARACTER);
        int[] a = new int[sp.length];
        
        for (int i = 0; i < a.length; i++) 
        {
            a[i] = Integer.parseInt(sp[i]);
        }
        return a;
        
    }
    

    private void updateDimensions(String ds,String msr,String highCardinalityDims)
    {
        String [] sp = null;
        if(null != ds)
        {
            sp = ds.split(MolapCommonConstants.COMA_SPC_CHARACTER);
        }
        else
        {
            sp = new String[0];
        }
        int[] dimsLocal = new int[sp.length];
        int[] lens = new int[sp.length];
        List<String> list = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        dimPresent = new boolean[sp.length];
            
        for (int i = 0; i < dimsLocal.length; i++) 
        {
            String[] dim = sp[i].split(MolapCommonConstants.COLON_SPC_CHARACTER);
            list.add(dim[0]);
            dimsLocal[i] = Integer.parseInt(dim[1]);
            lens[i] = Integer.parseInt(dim[2]);
            
            if("Y".equals(dim[3]))
            {
                dimPresent[i] = true;
                normLength++;
            }
        }
        dims = dimsLocal;
        dimLens = lens;
//      columns.put(DIMENSIONS, list);
        dimColNames = list.toArray(new String[list.size()]);
        highCardCols= new String[0];
        //for high card dims
        if(null != highCardinalityDims)
        {
            String[] highCard = highCardinalityDims
                    .split(MolapCommonConstants.COMA_SPC_CHARACTER);
            int[] highCardDimsLocal = new int[highCard.length];
            List<String> list1 = new ArrayList<String>(
                    MolapCommonConstants.CONSTANT_SIZE_TEN);

            for(int i = 0;i < highCardDimsLocal.length;i++)
            {
                String[] dim = highCard[i]
                        .split(MolapCommonConstants.COLON_SPC_CHARACTER);
                list1.add(dim[0]);
                highCardDimsLocal[i] = Integer.parseInt(dim[1]);
                Integer.parseInt(dim[2]);

            }
            highCardCols = list1.toArray(new String[list1.size()]);
        }
        String [] sm = msr.split(MolapCommonConstants.COMA_SPC_CHARACTER);
        int[] m = new int[sm.length];
        Set<String> mlist = new LinkedHashSet<String>();
        for (int i = 0; i < m.length; i++) 
        {
            String[] ms = sm[i].split(MolapCommonConstants.COLON_SPC_CHARACTER);
            mlist.add(ms[0]);
            m[i] = Integer.parseInt(ms[1]);
        }
        msrs = m;
        measureColumn = mlist.toArray(new String[mlist.size()]);
//      columns.put(MEASURES, mlist);
    }


    public void readRep(Repository rep, ObjectId idStep, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
        try
        {
            //
            molapProps = rep.getStepAttributeString(idStep, "molapProps");
            molapmsr = rep.getStepAttributeString(idStep, "msr");
            molapdim = rep.getStepAttributeString(idStep, "dim");
            molaphier = rep.getStepAttributeString(idStep, "hier");
            molapTime = rep.getStepAttributeString(idStep, "time"); 
            //
//            molapJNDI = rep.getStepAttributeString(id_step, "con");
            driverClass = rep.getStepAttributeString(idStep, "driverClass");
            connectionURL =rep.getStepAttributeString(idStep, "connectionURL");
            userName = rep.getStepAttributeString(idStep, "userName");
            password = rep.getStepAttributeString(idStep, "password");
            isAggregate = rep.getStepAttributeBoolean(idStep, "isAggregate");
            metaHeirSQLQuery =  rep.getStepAttributeString(idStep, "metadataFilePath");
            molapMetaHier =  rep.getStepAttributeString(idStep, "molapMetaHier");
            molaphierColumn =  rep.getStepAttributeString(idStep, "molaphierColumn");
//            primaryKeyColumnNamesString =  rep.getStepAttributeString(id_step, "primaryKeyColumnNamesString");
            foreignKeyHierarchyString =  rep.getStepAttributeString(idStep, "foreignKeyHierarchyString");
            primaryKeysString =  rep.getStepAttributeString(idStep, "primaryKeysString");
//            foreignKeyColumnNameString =  rep.getStepAttributeString(id_step, "foreignKeyColumnNameString");
            molapMeasureNames =  rep.getStepAttributeString(idStep, "molapMeasureNames");
            actualDimNames =  rep.getStepAttributeString(idStep, "actualDimNames");
            msrAggregatorString =  rep.getStepAttributeString(idStep, "msrAggregatorString");
            
            dimesionTableNames = rep.getStepAttributeString(idStep, "dimHierReleation");
            highCardinalityDims=rep.getStepAttributeString(idStep, "dimHighCardinality");
            normHiers = rep.getStepAttributeString(idStep, "normHiers");
            tableName = rep.getStepAttributeString(idStep, "factOrAggTable");
            batchSize = Integer.parseInt(rep.getStepAttributeString(idStep,
                    "batchSize"));
            heirKeySize = rep.getStepAttributeString(idStep, "heirKeySize");
            forgienKeyPrimayKeyString = rep.getStepAttributeString(idStep, "forgienKeyPrimayKeyString");
            heirNadDimsLensString = rep.getStepAttributeString(idStep, "heirNadDimsLensString");
            measureDataType = rep.getStepAttributeString(idStep, "measureDataType");
            columnAndTableNameColumnMapForAggString=rep.getStepAttributeString(idStep, "columnAndTableName_ColumnMapForAggString");
			schemaName=rep.getStepAttributeString(idStep, "schemaName");
            
            cubeName=rep.getStepAttributeString(idStep, "cubeName");
            checkPointFileExits = rep.getStepAttributeString(idStep, "checkPointFileExits");
            denormColumNames = rep.getStepAttributeString(idStep, "denormColumNames");
            currentRestructNumber = (int)rep.getStepAttributeInteger(idStep, "currentRestructNumber");
            //
            int nrKeys   = rep.countNrStepAttributes(idStep, "lookup_keyfield");
            allocate(nrKeys);
            //
        }
        catch(Exception e)
        {
            throw new KettleException(BaseMessages.getString(pkg,
                    "MolapStep.Exception.UnexpectedErrorInReadingStepInfo"), e);
        }
    }

    public void saveRep(Repository rep, ObjectId idTransformation, ObjectId idStep) throws KettleException
    {
        //
        try
        {
            rep.saveStepAttribute(idTransformation, idStep, "dim", molapdim); 
            rep.saveStepAttribute(idTransformation, idStep, "molapProps", molapProps);
            rep.saveStepAttribute(idTransformation, idStep, "msr", molapmsr); 
            rep.saveStepAttribute(idTransformation, idStep, "hier", molaphier);
            rep.saveStepAttribute(idTransformation, idStep, "molaphierColumn", molaphierColumn);
            rep.saveStepAttribute(idTransformation, idStep, "columnAndTableName_ColumnMapForAggString", columnAndTableNameColumnMapForAggString);
            rep.saveStepAttribute(idTransformation, idStep, "time", molapTime);
            //
//            rep.saveStepAttribute(id_transformation, id_step, "con", molapJNDI);
            rep.saveStepAttribute(idTransformation, idStep, "driverClass", driverClass);
            rep.saveStepAttribute(idTransformation, idStep, "connectionURL", connectionURL);
            rep.saveStepAttribute(idTransformation, idStep, "userName", userName);
            rep.saveStepAttribute(idTransformation, idStep, "password", password);
            rep.saveStepAttribute(idTransformation, idStep, "isInitialLoad", isAggregate);
            rep.saveStepAttribute(idTransformation, idStep, "metadataFilePath", metaHeirSQLQuery);
            rep.saveStepAttribute(idTransformation, idStep, "molapMetaHier", molapMetaHier);
            rep.saveStepAttribute(idTransformation, idStep, "batchSize", batchSize);
            rep.saveStepAttribute(idTransformation, idStep, "dimHierReleation", dimesionTableNames);
            rep.saveStepAttribute(idTransformation, idStep, "dimHighCardinality", highCardinalityDims);
//            rep.saveStepAttribute(id_transformation, id_step, "primaryKeyColumnNamesString", primaryKeyColumnNamesString);
            rep.saveStepAttribute(idTransformation, idStep, "foreignKeyHierarchyString", foreignKeyHierarchyString);
            rep.saveStepAttribute(idTransformation, idStep, "primaryKeysString", primaryKeysString);
//            rep.saveStepAttribute(id_transformation, id_step, "foreignKeyColumnNameString", foreignKeyColumnNameString);
            rep.saveStepAttribute(idTransformation, idStep, "molapMeasureNames", molapMeasureNames);
            rep.saveStepAttribute(idTransformation, idStep, "actualDimNames", actualDimNames);
            rep.saveStepAttribute(idTransformation, idStep, "normHiers", normHiers);
            rep.saveStepAttribute(idTransformation, idStep, "msrAggregatorString", msrAggregatorString);
            rep.saveStepAttribute(idTransformation, idStep, "heirKeySize", heirKeySize);
            rep.saveStepAttribute(idTransformation, idStep, "forgienKeyPrimayKeyString", forgienKeyPrimayKeyString);
            //
            rep.saveStepAttribute(idTransformation, idStep, "factOrAggTable", tableName);
            rep.saveStepAttribute(idTransformation, idStep, "heirNadDimsLensString", heirNadDimsLensString);
            rep.saveStepAttribute(idTransformation, idStep, "measureDataType", measureDataType);
			rep.saveStepAttribute(idTransformation, idStep, "schemaName", schemaName);
            rep.saveStepAttribute(idTransformation, idStep, "cubeName", cubeName);
            rep.saveStepAttribute(idTransformation, idStep, "checkPointFileExits", checkPointFileExits);
            rep.saveStepAttribute(idTransformation, idStep, "denormColumNames", denormColumNames);
            rep.saveStepAttribute(idTransformation, idStep, "currentRestructNumber", currentRestructNumber);
        }
        catch(Exception e)
        {
            throw new KettleException(BaseMessages.getString(pkg,
                    "MolapStep.Exception.UnableToSaveStepInfoToRepository")
                    + idStep, e);
        }
    }   
    


    public void check(List<CheckResultInterface> remarks, TransMeta transmeta,
            StepMeta stepMeta, RowMetaInterface prev, String[] input,
            String[] output, RowMetaInterface info)
    {
//        CheckResult cr;
//
//        // See if we have input streams leading to this step!
//        if (input.length > 0) {
//            cr = new CheckResult(
//                    CheckResult.TYPE_RESULT_OK,
//                    BaseMessages
//                            .getString(pkg,
//                                    "MolapStep.Check.StepIsReceivingInfoFromOtherSteps"),
//                    stepMeta);
//            remarks.add(cr);
//        } else {
//            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
//                    BaseMessages.getString(pkg,
//                            "MolapStep.Check.NoInputReceivedFromOtherSteps"),
//                    stepMeta);
//            remarks.add(cr);
//        }   
//        
//        // also check that each expected key fields are acually coming
//        if (prev!=null && prev.size()>0)
//        {
//            String error_message = ""; 
//            boolean error_found = false;
//            
//            if (error_found)
//            {
//                cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepMeta);
//            }
//            else
//            {
//                cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK,
//                        BaseMessages.getString(pkg,
//                                "MolapStep.Check.AllFieldsFoundInInput"),
//                        stepMeta);
//            }
//            remarks.add(cr);
//        }
//        else
//        {
//            String error_message = BaseMessages.getString(pkg,
//                    "MolapStep.Check.CouldNotReadFromPreviousSteps") + Const.CR;
//            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, error_message, stepMeta);
//            remarks.add(cr);
//        }       
    	MolapDataProcessorUtil.check(pkg, remarks, stepMeta, prev, input);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new MolapCSVBasedSeqGenStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new MolapCSVBasedSeqGenData();
    }

    public List<String>[] getPropertiesColumns()
    {
        return propColumns;
    }

    public int[][] getPropertiesIndices()
    {
        return propIndxs;
    }

    /**
     * 
     * @return Returns the propTypes.
     * 
     */
    public List<String>[] getPropTypes()
    {
        return propTypes;
    }

    /**
     * 
     * @return Returns the dimHierReleation.
     * 
     */
    public String getTableNames()
    {
        return dimesionTableNames;
    }

    /**
     * 
     * @param dimHierReleation The dimHierReleation to set.
     * 
     */
    public void setTableNames(String dimHierReleation)
    {
        this.dimesionTableNames = dimHierReleation;
    }

    /**
     * 
     * @return Returns the tableName.
     * 
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * 
     * @param tableName The tableName to set.
     * 
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    /**
     * 
     * @return Returns the modifiedDimension.
     * 
     */
    public String[] getModifiedDimension()
    {
        return modifiedDimension;
    }

    /**
     * 
     * @param modifiedDimension The modifiedDimension to set.
     * 
     */
    public void setModifiedDimension(String[] modifiedDimension)
    {
        this.modifiedDimension = modifiedDimension;
    }

    /**
     * 
     * @param molaphierColumn The molaphierColumn to set.
     * 
     */
    public void setMolaphierColumn(String molaphierColumn)
    {
        this.molaphierColumn = molaphierColumn;
    }

    /**
     * 
     * @param primaryKeyColumnNamesString The primaryKeyColumnNamesString to set.
     * 
     */
//    public void setPrimaryKeyColumnNamesString(String primaryKeyColumnNamesString)
//    {
//        this.primaryKeyColumnNamesString = primaryKeyColumnNamesString;
//    }

    /**
     * 
     * @param foreignKeyHierarchyString The foreignKeyHierarchyString to set.
     * 
     */
    public void setForeignKeyHierarchyString(String foreignKeyHierarchyString)
    {
        this.foreignKeyHierarchyString = foreignKeyHierarchyString;
    }

    /**
     * 
     * @param primaryKeysString The primaryKeysString to set.
     * 
     */
    public void setPrimaryKeysString(String primaryKeysString)
    {
        this.primaryKeysString = primaryKeysString;
    }

    /**
     * 
     * @return Returns the primaryKeyMap.
     * 
     */
    public Map<String, Boolean> getPrimaryKeyMap()
    {
        return primaryKeyMap;
    }

    /**
     * 
     * @param foreignKeyColumnNameString The foreignKeyColumnNameString to set.
     * 
     */
//    public void setForeignKeyColumnNameString(String foreignKeyColumnNameString)
//    {
//        this.foreignKeyColumnNameString = foreignKeyColumnNameString;
//    }

    /**
     * @return the molapMeasureNames
     */
    public String getMolapMeasureNames()
    {
        return molapMeasureNames;
    }

    /**
     * @param molapMeasureNames
     *            the molapMeasureNames to set
     */
    public void setMolapMeasureNames(String molapMeasureNames)
    {
        this.molapMeasureNames = molapMeasureNames;
    }

    /**
     * 
     * @return Returns the actualDimNames.
     * 
     */
    public String getActualDimNames()
    {
        return actualDimNames;
    }

    /**
     * 
     * @param actualDimNames The actualDimNames to set.
     * 
     */
    public void setActualDimNames(String actualDimNames)
    {
        this.actualDimNames = actualDimNames;
    }

    public String getNormHiers()
    {
        return normHiers;
    }

    public void setNormHiers(String normHiers)
    {
        this.normHiers = normHiers;
    }
	
	/**
     * 
     * @return Returns the msrAggregatorString.
     * 
     */
    public String getMsrAggregatorString()
    {
        return msrAggregatorString;
    }

    /**
     * 
     * @param msrAggregatorString The msrAggregatorString to set.
     * 
     */
    public void setMsrAggregatorString(String msrAggregatorString)
    {
        this.msrAggregatorString = msrAggregatorString;
    }

    public String getHeirKeySize()
    {
        return heirKeySize;
    }

    public void setHeirKeySize(String heirKeySize)
    {
        this.heirKeySize = heirKeySize;
    }

    public String getForgienKeyPrimayKeyString()
    {
        return forgienKeyPrimayKeyString;
    }

    public void setForgienKeyPrimayKeyString(String forgienKeyPrimayKeyString)
    {
        this.forgienKeyPrimayKeyString = forgienKeyPrimayKeyString;
    }

    /**
     * 
     * @return Returns the heirNadDimsLensString.
     * 
     */
    public String getHeirNadDimsLensString()
    {
        return heirNadDimsLensString;
    }

    /**
     * 
     * @param heirNadDimsLensString The heirNadDimsLensString to set.
     * 
     */
    public void setHeirNadDimsLensString(String heirNadDimsLensString)
    {
        this.heirNadDimsLensString = heirNadDimsLensString;
    }

    /**
     * 
     * @return Returns the measureDataType.
     * 
     */
    public String getMeasureDataType()
    {
        return measureDataType;
    }

    /**
     * 
     * @param measureDataType The measureDataType to set.
     * 
     */
    public void setMeasureDataType(String measureDataType)
    {
        this.measureDataType = measureDataType;
    }

    /**
     * 
     * @return Returns the measureSurrogateRequired.
     * 
     */
    public Map<String, Boolean> getMeasureSurrogateRequired()
    {
        return measureSurrogateRequired;
    }

    /**
     * 
     * @param measureSurrogateRequired The measureSurrogateRequired to set.
     * 
     */
    public void setMeasureSurrogateRequired(
            Map<String, Boolean> measureSurrogateRequired)
    {
        this.measureSurrogateRequired = measureSurrogateRequired;
    }

    /**
     * 
     * @return Returns the hierDimTableMap.
     * 
     */
    public Map<String, String> getHierDimTableMap()
    {
        return hierDimTableMap;
    }

    /**
     * 
     * @return Returns the dimTableArray.
     * 
     */
    public String[] getDimTableArray()
    {
        return dimTableArray;
    }

    public String getColumnAndTableNameColumnMapForAggString() {
		return columnAndTableNameColumnMapForAggString;
	}

	public void setColumnAndTableNameColumnMapForAggString(
			String columnAndTableNameColumnMapForAggString) {
		this.columnAndTableNameColumnMapForAggString = columnAndTableNameColumnMapForAggString;
	}

	/**
     * @return the columnAndTableName_ColumnMapForAggString
     *//*
    public String getColumnAndTableName_ColumnMapForAggString()
    {
        return columnAndTableName_ColumnMapForAggString;
    }

    *//**
     * @param columnAndTableName_ColumnMapForAggString the columnAndTableName_ColumnMapForAggString to set
     *//*
    public void setColumnAndTableName_ColumnMapForAggString(
            String columnAndTableName_ColumnMapForAggString)
    {
        this.columnAndTableName_ColumnMapForAggString = columnAndTableName_ColumnMapForAggString;
    }*/

    public String getCheckPointFileExits()
    {
        return checkPointFileExits;
    }

    public void setCheckPointFileExits(String checkPointFileExits)
    {
        this.checkPointFileExits = checkPointFileExits;
    }
	
	 /**
     * @return the cubeName
     */
    public String getCubeName()
    {
        return cubeName;
    }

    /**
     * @return the schemaName
     */
    public String getSchemaName()
    {
        return schemaName;
    }

    /**
     * @param cubeName the cubeName to set
     */
    public void setCubeName(String cubeName)
    {
        this.cubeName = cubeName;
    }

    /**
     * @param schemaName the schemaName to set
     */
    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }

    /**
     * 
     * @return Returns the denormColumNames.
     * 
     */
    public String getDenormColumNames()
    {
        return denormColumNames;
    }

    /**
     * 
     * @param denormColumNames The denormColumNames to set.
     * 
     */
    public void setDenormColumNames(String denormColumNames)
    {
        this.denormColumNames = denormColumNames;
    }

    /**
     * @return the currentRestructNumber
     */
    public int getCurrentRestructNumber()
    {
        return currentRestructNumber;
    }

    /**
     * @param currentRestructNum the currentRestructNumber to set
     */
    public void setCurrentRestructNumber(int currentRestructNum)
    {
        this.currentRestructNumber = currentRestructNum;
    }
    /**
     * 
     * @return
     */
    public String  getHighCardinalityDims() {
		return highCardinalityDims;
	}

    /**
     * 
     * @param highCardinalityDims
     */
	public void setHighCardinalityDims(String highCardinalityDims) {
		this.highCardinalityDims = highCardinalityDims;
	}
}

