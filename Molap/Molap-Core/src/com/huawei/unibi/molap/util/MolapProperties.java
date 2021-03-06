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
package com.huawei.unibi.molap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;

/**
 * 
 * Project Name NSE V3R7C00 Module Name : Molap Commons Author K00900841 Created
 * Date :23-May-2013 5:01:50 PM FileName : MolapProperties.java Class
 * Description : Porperty Reader Utility class for reading molap properties
 * Version 1.0
 */
public final class MolapProperties
{
    /**
     * Attribute for Molap LOGGER.
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(MolapProperties.class.getName());
        
    /**
     * class instance.
     */
    private static final MolapProperties MOLAPPROPERTIESINSTANCE = new MolapProperties();
    


    /**
     * porpeties .
     */
    private Properties molapProperties;

    /**
     * Private constructor this will call load properties method to load all the
     * molap properties in memory.
     * 
     */
    private MolapProperties()
    {
        molapProperties = new Properties();
        loadProperties();
        validateAndLoadDefaultProperties();
    }
    
    /**
     * This method validates the loaded properties and loads default
     * values in case of wrong values.
     * 
     *
     */
    private void validateAndLoadDefaultProperties()
    {
        if(null==molapProperties.getProperty(MolapCommonConstants.STORE_LOCATION))
        {
            molapProperties.setProperty(MolapCommonConstants.STORE_LOCATION,MolapCommonConstants.STORE_LOCATION_DEFAULT_VAL);
        }
        
        if(null==molapProperties.getProperty(MolapCommonConstants.VALUESTORE_TYPE))
        {
            molapProperties.setProperty(MolapCommonConstants.VALUESTORE_TYPE,MolapCommonConstants.VALUESTORE_TYPE_DEFAULT_VAL);
        }
        
        if(null==molapProperties.getProperty(MolapCommonConstants.KEYSTORE_TYPE))
        {
            molapProperties.setProperty(MolapCommonConstants.KEYSTORE_TYPE,MolapCommonConstants.KEYSTORE_TYPE_DEFAULT_VAL);
        }
        
//        validateResultSetSize();
        validateLeafNodeSize();
        validateMaxFileSize();
        validateNumCores();
        validateBatchSize();
        validateSortSize();
        validateCardinalityIncrementValue();
        validateOnlineMergerSize();
        validateOfflineMergerSize();
        validateSortBufferSize();
        validateDataLoadQSize();
        validateDataLoadConcExecSize();
        validateDecimalPointers();
        validateDecimalPointersAgg();
        validateCsvFileSize();
        validateNumberOfCsvFile();
        validateBadRecordsLocation();
        validateBadRecordsEncryption();
    }
    
    private void validateBadRecordsLocation()
    {
    	String badRecordsLocation = molapProperties.getProperty(MolapCommonConstants.MOLAP_BADRECORDS_LOC);
        if(null==badRecordsLocation || badRecordsLocation.length() == 0)
        {
            molapProperties.setProperty(MolapCommonConstants.MOLAP_BADRECORDS_LOC,MolapCommonConstants.MOLAP_BADRECORDS_LOC_DEFAULT_VAL);
        }
    }
    
    private void validateBadRecordsEncryption()
    {
        String badRecordsEncryption = molapProperties.getProperty(MolapCommonConstants.MOLAP_BADRECORDS_ENCRYPTION);
        if(null==badRecordsEncryption || badRecordsEncryption.length() == 0)
        {
            molapProperties.setProperty(MolapCommonConstants.MOLAP_BADRECORDS_ENCRYPTION,MolapCommonConstants.MOLAP_BADRECORDS_ENCRYPTION_DEFAULT_VAL);
        }
    }
    
    private void validateCsvFileSize()
    {
        try
        {
            int csvFileSizeProperty = Integer.parseInt(molapProperties.getProperty(
                    MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE,
                    MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE_DEFAULTVALUE));
            if(csvFileSizeProperty < 1)
            {
                LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Invalid value for "
                        + MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE
                        + "\" Only Positive Integer(greater than zero) is allowed. Using the default value \""
                        + MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE_DEFAULTVALUE);
                
                molapProperties.setProperty(MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE,
                        MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE_DEFAULTVALUE);
            }
        }
        catch(NumberFormatException e)
        {
            LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Invalid value for "
                    + MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE
                    + "\" Only Positive Integer(greater than zero) is allowed. Using the default value \""
                    + MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE_DEFAULTVALUE);
            
            molapProperties.setProperty(MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE,
                    MolapCommonConstants.MOLAP_DATALOAD_VALID_CSVFILE_SIZE_DEFAULTVALUE);
        }
    }
    
    private void validateNumberOfCsvFile()
    {
        try
        {
            int csvFileSizeProperty = Integer.parseInt(molapProperties.getProperty(
                    MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE,
                    MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE_DEFAULTVALUE));
            if(csvFileSizeProperty < 1)
            {
                LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Invalid value for "
                        + MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE
                        + "\" Only Positive Integer(greater than zero) is allowed. Using the default value \""
                        + MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE_DEFAULTVALUE);
                
                molapProperties.setProperty(MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE,
                        MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE_DEFAULTVALUE);
            }
        }
        catch(NumberFormatException e)
        {
            LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Invalid value for "
                    + MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE
                    + "\" Only Positive Integer(greater than zero) is allowed. Using the default value \""
                    + MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE_DEFAULTVALUE);
            
            molapProperties.setProperty(MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE,
                    MolapCommonConstants.MOLAP_DATALOAD_VALID_NUMBAER_OF_CSVFILE_DEFAULTVALUE);
        }
    }

    /**
     * 
     * This method validates the batch size
     *
     */
    private void validateOnlineMergerSize()
    {
        String onlineMergeSize = molapProperties
                .getProperty(MolapCommonConstants.ONLINE_MERGE_FILE_SIZE,MolapCommonConstants.ONLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
        try
        {
           int  offlineMergerSize = Integer.parseInt(onlineMergeSize); 

            if (offlineMergerSize < MolapCommonConstants.ONLINE_MERGE_MIN_VALUE
                    || offlineMergerSize > MolapCommonConstants.ONLINE_MERGE_MAX_VALUE)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The online Merge Size value \""
                                + onlineMergeSize
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.ONLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
                molapProperties.setProperty(MolapCommonConstants.ONLINE_MERGE_FILE_SIZE, MolapCommonConstants.ONLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The online Merge Size value \""
                            + onlineMergeSize
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.ONLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
            molapProperties.setProperty(MolapCommonConstants.ONLINE_MERGE_FILE_SIZE, MolapCommonConstants.ONLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
        }
    }
    
    /**
     * 
     * This method validates the batch size
     *
     */
    private void validateOfflineMergerSize()
    {
        String offLineMergerSize = molapProperties
                .getProperty(MolapCommonConstants.OFFLINE_MERGE_FILE_SIZE,MolapCommonConstants.OFFLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
        try
        {
           int  offLineMergeSize= Integer.parseInt(offLineMergerSize); 

            if (offLineMergeSize < MolapCommonConstants.OFFLINE_MERGE_MIN_VALUE
                    || offLineMergeSize > MolapCommonConstants.OFFLINE_MERGE_MAX_VALUE)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The offline Merge Size value \""
                                + offLineMergerSize
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.OFFLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
                molapProperties.setProperty(MolapCommonConstants.OFFLINE_MERGE_FILE_SIZE, MolapCommonConstants.OFFLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The offline Merge Size value \""
                            + offLineMergerSize
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.OFFLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
            molapProperties.setProperty(MolapCommonConstants.OFFLINE_MERGE_FILE_SIZE, MolapCommonConstants.OFFLINE_MERGE_FILE_SIZE_DEFAULT_VALUE);
        }
    }
    
    /**
     * 
     * This method validates the batch size
     *
     */
    private void validateBatchSize()
    {
        String batchSizeStr = molapProperties
                .getProperty(MolapCommonConstants.BATCH_SIZE,MolapCommonConstants.BATCH_SIZE_DEFAULT_VAL);
        try
        {
           int  batchSize = Integer.parseInt(batchSizeStr);   

            if (batchSize < MolapCommonConstants.BATCH_SIZE_MIN_VAL
                    || batchSize > MolapCommonConstants.BATCH_SIZE_MAX_VAL)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The batch size value \""
                                + batchSizeStr
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.BATCH_SIZE_DEFAULT_VAL);
                molapProperties.setProperty(MolapCommonConstants.BATCH_SIZE, MolapCommonConstants.BATCH_SIZE_DEFAULT_VAL);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The batch size value \""
                            + batchSizeStr
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.BATCH_SIZE_DEFAULT_VAL);
            molapProperties.setProperty(MolapCommonConstants.BATCH_SIZE, MolapCommonConstants.BATCH_SIZE_DEFAULT_VAL);
        }
    }
    
    /**
     * 
     * This method validates the batch size
     *
     */
    private void validateCardinalityIncrementValue()
    {
        String cardinalityIncr = molapProperties.getProperty(MolapCommonConstants.CARDINALITY_INCREMENT_VALUE,
                MolapCommonConstants.CARDINALITY_INCREMENT_VALUE_DEFAULT_VAL);
        try
        {
            int batchSize = Integer.parseInt(cardinalityIncr); 

            if(batchSize < MolapCommonConstants.CARDINALITY_INCREMENT_MIN_VAL
                    || batchSize > MolapCommonConstants.CARDINALITY_INCREMENT_MAX_VAL)
            {
                LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "The batch size value \"" + cardinalityIncr
                        + "\" is invalid. Using the default value \""
                        + MolapCommonConstants.CARDINALITY_INCREMENT_VALUE_DEFAULT_VAL);
                molapProperties.setProperty(MolapCommonConstants.CARDINALITY_INCREMENT_VALUE,
                        MolapCommonConstants.CARDINALITY_INCREMENT_VALUE_DEFAULT_VAL);
            }
        }
        catch(NumberFormatException e)
        {
            LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "The cardinality size value \"" + cardinalityIncr
                    + "\" is invalid. Using the default value \"" + MolapCommonConstants.BATCH_SIZE_DEFAULT_VAL);
            molapProperties.setProperty(MolapCommonConstants.CARDINALITY_INCREMENT_VALUE,
                    MolapCommonConstants.CARDINALITY_INCREMENT_VALUE_DEFAULT_VAL);
        }
    }
    
    /**
     * 
     * This method validates the Leaf node size
     *
     */
    private void validateLeafNodeSize()
    {
        String leafNodeSizeStr = molapProperties
                .getProperty(MolapCommonConstants.LEAFNODE_SIZE,MolapCommonConstants.LEAFNODE_SIZE_DEFAULT_VAL);
        try
        {
           int  leafNodeSize = Integer.parseInt(leafNodeSizeStr); 

            if (leafNodeSize < MolapCommonConstants.LEAFNODE_SIZE_MIN_VAL
                    || leafNodeSize > MolapCommonConstants.LEAFNODE_SIZE_MAX_VAL)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The lefa node size value \""
                                + leafNodeSizeStr
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.LEAFNODE_SIZE_DEFAULT_VAL);
                molapProperties.setProperty(MolapCommonConstants.LEAFNODE_SIZE, MolapCommonConstants.LEAFNODE_SIZE_DEFAULT_VAL);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The lefa node size value \""
                            + leafNodeSizeStr
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.LEAFNODE_SIZE_DEFAULT_VAL);
            molapProperties.setProperty(MolapCommonConstants.LEAFNODE_SIZE, MolapCommonConstants.LEAFNODE_SIZE_DEFAULT_VAL);
        }
    }
    
    /**
     * 
     * This method validates data load queue size
     *
     */
    private void validateDataLoadQSize()
    {
        String dataLoadQSize = molapProperties
                .getProperty(MolapCommonConstants.DATA_LOAD_Q_SIZE,MolapCommonConstants.DATA_LOAD_Q_SIZE_DEFAULT);
        try
        {
           int  dataLoadQSizeInt = Integer.parseInt(dataLoadQSize);

            if (dataLoadQSizeInt < MolapCommonConstants.DATA_LOAD_Q_SIZE_MIN
                    || dataLoadQSizeInt > MolapCommonConstants.DATA_LOAD_Q_SIZE_MAX)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The data load queue size value \""
                                + dataLoadQSize
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.DATA_LOAD_Q_SIZE_DEFAULT);
                molapProperties.setProperty(MolapCommonConstants.DATA_LOAD_Q_SIZE, MolapCommonConstants.DATA_LOAD_Q_SIZE_DEFAULT);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The data load queue size value \""
                            + dataLoadQSize
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.DATA_LOAD_Q_SIZE_DEFAULT);
            molapProperties.setProperty(MolapCommonConstants.DATA_LOAD_Q_SIZE, MolapCommonConstants.DATA_LOAD_Q_SIZE_DEFAULT);
        }
    }
    
    /**
     * 
     * This method validates the data load concurrent exec size
     *
     */
    private void validateDataLoadConcExecSize()
    {
        String dataLoadConcExecSize = molapProperties
                .getProperty(MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE,MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_DEFAULT);
        try
        {
           int  dataLoadConcExecSizeInt = Integer.parseInt(dataLoadConcExecSize);

            if (dataLoadConcExecSizeInt < MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_MIN
                    || dataLoadConcExecSizeInt > MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_MAX)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The data load concurrent exec size value \""
                                + dataLoadConcExecSize
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_DEFAULT);
                molapProperties.setProperty(MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE, MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_DEFAULT);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The data load concurrent exec size value \""
                            + dataLoadConcExecSize
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_DEFAULT);
            molapProperties.setProperty(MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE, MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_DEFAULT);
        }
    }
    
    /**
     * 
     * This method validates the decimal pointers size
     *
     */
    private void validateDecimalPointers()
    {
        String decimalPointers = molapProperties
                .getProperty(MolapCommonConstants.MOLAP_DECIMAL_POINTERS,MolapCommonConstants.MOLAP_DECIMAL_POINTERS_DEFAULT);
        try
        {
           int  decimalPointersInt = Integer.parseInt(decimalPointers);

            if (decimalPointersInt < 0
                    || decimalPointersInt > 15)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The decimal pointers agg \""
                                + decimalPointers
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_DEFAULT);
                molapProperties.setProperty(MolapCommonConstants.MOLAP_DECIMAL_POINTERS, MolapCommonConstants.MOLAP_DECIMAL_POINTERS_DEFAULT);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The decimal pointers agg \""
                            + decimalPointers
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.DATA_LOAD_CONC_EXE_SIZE_DEFAULT);
            molapProperties.setProperty(MolapCommonConstants.MOLAP_DECIMAL_POINTERS, MolapCommonConstants.MOLAP_DECIMAL_POINTERS_DEFAULT);
        }
    }
    
    /**
     * 
     * This method validates the data load concurrent exec size
     *
     */
    private void validateDecimalPointersAgg()
    {
        String decimalPointers = molapProperties
                .getProperty(MolapCommonConstants.MOLAP_DECIMAL_POINTERS_AGG,MolapCommonConstants.MOLAP_DECIMAL_POINTERS_AGG_DEFAULT);
        try
        {
           int  decimalPointersInt = Integer.parseInt(decimalPointers);

            if (decimalPointersInt < 0
                    || decimalPointersInt > 15)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The decimal pointers agg \""
                                + decimalPointers
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.MOLAP_DECIMAL_POINTERS_AGG_DEFAULT);
                molapProperties.setProperty(MolapCommonConstants.MOLAP_DECIMAL_POINTERS_AGG, MolapCommonConstants.MOLAP_DECIMAL_POINTERS_AGG_DEFAULT);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The decimal pointers agg \""
                            + decimalPointers
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.MOLAP_DECIMAL_POINTERS_AGG_DEFAULT);
            molapProperties.setProperty(MolapCommonConstants.MOLAP_DECIMAL_POINTERS_AGG, MolapCommonConstants.MOLAP_DECIMAL_POINTERS_AGG_DEFAULT);
        }
    }
    
    /**
     * 
     * This method validates the maximum number of
     * LeafNodes per file.
     *
     */
    private void validateMaxFileSize()
    {
        String maxFileSizeStr = molapProperties
                .getProperty(MolapCommonConstants.MAX_FILE_SIZE,MolapCommonConstants.MAX_FILE_SIZE_DEFAULT_VAL);
        try
        {
           int  maxFileSize = Integer.parseInt(maxFileSizeStr); 

            if (maxFileSize < MolapCommonConstants.MAX_FILE_SIZE_DEFAULT_VAL_MIN_VAL
                    || maxFileSize > MolapCommonConstants.MAX_FILE_SIZE_DEFAULT_VAL_MAX_VAL)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The max file size value \""
                                + maxFileSizeStr
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.MAX_FILE_SIZE_DEFAULT_VAL);
                molapProperties.setProperty(MolapCommonConstants.MAX_FILE_SIZE, MolapCommonConstants.MAX_FILE_SIZE_DEFAULT_VAL);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The max file size value \""
                            + maxFileSizeStr
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.MAX_FILE_SIZE_DEFAULT_VAL);
                    
            molapProperties.setProperty(MolapCommonConstants.MAX_FILE_SIZE, MolapCommonConstants.MAX_FILE_SIZE_DEFAULT_VAL);
        }
    }
    
    /**
     * 
     * This method validates the number cores specified 
     *
     */
    private void validateNumCores()
    {
        String numCoresStr = molapProperties
                .getProperty(MolapCommonConstants.NUM_CORES,MolapCommonConstants.NUM_CORES_DEFAULT_VAL);
        try
        { 
           int  numCores = Integer.parseInt(numCoresStr); 

            if (numCores < MolapCommonConstants.NUM_CORES_MIN_VAL
                    || numCores > MolapCommonConstants.NUM_CORES_MAX_VAL)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The num Cores  value \""
                                + numCoresStr
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.NUM_CORES_DEFAULT_VAL);
                molapProperties.setProperty(MolapCommonConstants.NUM_CORES, MolapCommonConstants.NUM_CORES_DEFAULT_VAL);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                    "The num Cores  value \""
                            + numCoresStr
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.NUM_CORES_DEFAULT_VAL);
            molapProperties.setProperty(MolapCommonConstants.NUM_CORES, MolapCommonConstants.NUM_CORES_DEFAULT_VAL);
        }
    }
    
    /**
     * 
     * This method validates the sort size
     *
     */
    private void validateSortSize()
    {
        String sortSizeStr = molapProperties
                .getProperty(MolapCommonConstants.SORT_SIZE,MolapCommonConstants.SORT_SIZE_DEFAULT_VAL);
        try
        {
           int  sortSize = Integer.parseInt(sortSizeStr); 

            if(sortSize < MolapCommonConstants.SORT_SIZE_MIN_VAL)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The batch size value \""
                                + sortSizeStr
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.SORT_SIZE_DEFAULT_VAL);
                molapProperties.setProperty(MolapCommonConstants.SORT_SIZE, MolapCommonConstants.SORT_SIZE_DEFAULT_VAL);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                    "The batch size value \""
                            + sortSizeStr
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.SORT_SIZE_DEFAULT_VAL);
            molapProperties.setProperty(MolapCommonConstants.SORT_SIZE, MolapCommonConstants.SORT_SIZE_DEFAULT_VAL);
        }
    }
    
//    /**
//     * 
//     * This method validates the sort size
//     *
//     */
//    private void validateResultSetSize()
//    {
//        String resultSize = molapProperties
//                .getProperty(MolapCommonConstants.MOLAP_RESULT_SIZE_KEY,MolapCommonConstants.MOLAP_RESULT_SIZE_DEFAULT);
//        try
//        {
//           int  result_Size = Integer.parseInt(resultSize);
//
//            if(result_Size < 1)
//            {
//                LOGGER.info(
//                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
//                        "The batch size value \""
//                                + resultSize
//                                + "\" is invalid. Using the default value \""
//                                + MolapCommonConstants.MOLAP_RESULT_SIZE_DEFAULT);
//                molapProperties.setProperty(MolapCommonConstants.MOLAP_RESULT_SIZE_KEY, MolapCommonConstants.MOLAP_RESULT_SIZE_DEFAULT);
//            }
//            
//            
//            if(result_Size > MondrianProperties.instance().ResultLimit.get())
//            {
//                LOGGER.info(
//                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
//                        "The batch size value \""
//                                + resultSize
//                                + "\" is invalid. Using the default value \""
//                                + MolapCommonConstants.MOLAP_RESULT_SIZE_DEFAULT);
//                molapProperties.setProperty(MolapCommonConstants.MOLAP_RESULT_SIZE_KEY, MolapCommonConstants.MOLAP_RESULT_SIZE_DEFAULT);
//            }
//        }
//        catch (NumberFormatException e)
//        {
//            LOGGER.info(
//                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
//                    "The batch size value \""
//                            + resultSize
//                            + "\" is invalid. Using the default value \""
//                            + MolapCommonConstants.MOLAP_RESULT_SIZE_DEFAULT);
//            molapProperties.setProperty(MolapCommonConstants.MOLAP_RESULT_SIZE_KEY, MolapCommonConstants.MOLAP_RESULT_SIZE_DEFAULT);
//        }
//    }
    
    /**
     * 
     * This method validates the sort size
     *
     */
    private void validateSortBufferSize()
    {
        String sortSizeStr = molapProperties
                .getProperty(MolapCommonConstants.SORT_BUFFER_SIZE,MolapCommonConstants.SORT_BUFFER_SIZE_DEFAULT_VALUE);
        try
        {
           int  sortSize = Integer.parseInt(sortSizeStr); 

            if(sortSize < MolapCommonConstants.SORT_BUFFER_SIZE_MIN_VALUE)
            {
                LOGGER.info(
                        MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG ,
                        "The batch size value \""
                                + sortSizeStr
                                + "\" is invalid. Using the default value \""
                                + MolapCommonConstants.SORT_BUFFER_SIZE_DEFAULT_VALUE);
                molapProperties.setProperty(MolapCommonConstants.SORT_BUFFER_SIZE, MolapCommonConstants.SORT_BUFFER_SIZE_DEFAULT_VALUE);
            }
        }
        catch (NumberFormatException e)
        {
            LOGGER.info(
                    MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                    "The batch size value \""
                            + sortSizeStr
                            + "\" is invalid. Using the default value \""
                            + MolapCommonConstants.SORT_BUFFER_SIZE_DEFAULT_VALUE);
            molapProperties.setProperty(MolapCommonConstants.SORT_BUFFER_SIZE, MolapCommonConstants.SORT_BUFFER_SIZE_DEFAULT_VALUE);
        }
    }
    
   

    /**
     * This method will be responsible for get this class instance
     * 
     * @return molap properties instance
     * 
     */
    public static MolapProperties getInstance()
    {
        return MOLAPPROPERTIESINSTANCE;
    }

    /**
     * This method will read all the properties from file and load it into
     * memory
     * 
     * 
     */
    private void loadProperties()
    {
        String property = System.getProperty("molap.properties.filepath");
        if(null==property)
        {
            property=MolapCommonConstants.MOLAP_PROPERTIES_FILE_PATH;
        }
        File file = new File(property);
        LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Property file path: "+ file.getAbsolutePath());
        
        FileInputStream fis=null;
        try
        {
         if(file.exists())
         {
             fis = new FileInputStream(file);
             
             molapProperties.load(fis);
         }
//         else
//         {
//             populate();
//         }
        }
        catch(FileNotFoundException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "The file: "
                    + MolapCommonConstants.MOLAP_PROPERTIES_FILE_PATH + " does not exist");
        }

        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Error while reading the file: "
                    + MolapCommonConstants.MOLAP_PROPERTIES_FILE_PATH);
        }
        finally
        {
            if(null!=fis)
            {
                try
                {
                    fis.close();
                }
                catch(IOException e)
                {
                    LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Error while closing the file stream for file: "
                            + MolapCommonConstants.MOLAP_PROPERTIES_FILE_PATH);
                }
            }
        }
        
        print();
    }
//    /**
//     *  populate().
//     */
//    public void populate() {
//        // Read properties file "mondrian.properties", if it exists. If we have
//        // read the file before, only read it if it is newer.
//        //loadIfStale(propertySource);
//
//        URL url = null;
//        File file = new File("molap.properties");
//        if (file.exists() && file.isFile()) {
//            // Read properties file "mondrian.properties" from PWD, if it
//            // exists.
//            try {
//                url = file.toURI().toURL();
//            } catch (MalformedURLException e) {
//                LOGGER.warn(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
//                    "Mondrian: file '"
//                    + file.getAbsolutePath()
//                    + "' could not be loaded", e);
//            }
//        } else {
//            // Then try load it from classloader
//            url =
//                MondrianPropertiesBase.class.getClassLoader().getResource(
//                        "molap.properties");
//        }
//
//        if (url != null) {
//            load(new UrlPropertySource(url));
//        } else {
//            LOGGER.warn(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
//                "mondrian.properties can't be found under '"
//                + new File(".").getAbsolutePath() + "' or classloader");
//        }
//
//
//    }
//    
//    /**
//     * Tries to load properties from a URL. Does not fail, just prints success
//     * or failure to log.
//     *
//     * @param source Source to read properties from
//     */
//    private void load(final PropertySource source) {
//        InputStream stream = null;
//        try {
//            stream = source.openStream();
//            molapProperties.load(stream);
//
//        } catch (IOException e) {
//            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
//                "Mondrian: error while loading properties "
//                + "from '" + source.getDescription() + '(' + e + ')');
//        }
//        finally
//        {
//            if (null != stream)
//            {
//                try
//                {
//                    stream.close();
//                }
//                catch(IOException e)
//                {
//                    LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
//                            "Failed to close properties "
//                            + "from '" + source.getDescription());
//                }
//            }
//        }
//    }
//    
//    
//    /**
//     * Implementation of {@link PropertySource} which reads from a
//     * {@link java.net.URL}.
//     */
//   private static class UrlPropertySource implements PropertySource {
//        /**
//         * 
//         */
//        private final URL url;
//
//        /**
//         * 
//         */
//        private long lastModified;
//
//        UrlPropertySource(URL url) {
//            this.url = url;
//        }
//
//        private URLConnection getConnection() {
//            try {
//                return url.openConnection();
//            } catch (IOException e) {
//                throw Util.newInternal(
//                    e,
//                    "Error while opening properties file " + url);
//            }
//        }
//
//        /**
//         * openStream.
//         */
//        public InputStream openStream() {
//            try {
//                final URLConnection connection = getConnection();
//                this.lastModified = connection.getLastModified();
//                return connection.getInputStream();
//            } catch (IOException e) {
//                throw Util.newInternal(
//                    e,
//                    "Error while opening properties file " + url);
//            }
//        }
//
//        /**
//         * isStale.
//         */
//        public boolean isStale() {
//            final long lastModified = getConnection().getLastModified();
//            return lastModified > this.lastModified;
//        }
//
//        /**
//         * getDescription.
//         */
//        public String getDescription() {
//            return url.toExternalForm();
//        }
//    }
   
    /**
     * This method will be used to get the properties value
     * 
     * @param key
     * @return properties value
     * 
     */
    public  String getProperty(String key)
    {
        //TODO temporary fix 
        if("molap.leaf.node.size".equals(key))
        {
            return "120000";
        }
        return molapProperties.getProperty(key);
    }

    /**
     * This method will be used to get the properties value if property is not
     * present then it will return tghe default value
     * 
     * @param key
     * @return properties value
     * 
     */
    public String getProperty(String key, String defaultValue)
    {
        String value = getProperty(key);
        if(null == value)
        {
            return defaultValue;
        }
        return value;
    }
    
    public String[] getAllProperties()
    {
        Set<Object> set = molapProperties.keySet();
        String[] allProps = new String[set.size()];
        int i = 0;
        for(Object obj : set)
        {
            allProps[i++] = obj.toString();
        }
        return allProps;
    }

    /**
     * This method will be used to add a new property
     * 
     * @param key
     * @return properties value
     * 
     */
    public void addProperty(String key, String value)
    {
        molapProperties.setProperty(key,value);
     
    }

    /**
     * Validate the restrictions
     * @param actual
     * @param max
     * @param min
     * @param defaultVal
     * @return
     */
    public long validate(long actual, long max,long min,long defaultVal)
    {
        if(actual <= max && actual >= min)
        {
            return actual;
        }
        return defaultVal;
    }
    
    public void print()
    {
        LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,"------Using Molap.properties --------");        
        LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,molapProperties);
//        System.out.println("------Using Molap.properties --------");
//        System.out.println(molapProperties);
    }

}
