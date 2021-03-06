/**
 * Project Name  : Carbon 
 * Module Name   : MOLAP spark interface
 * Author    : R00903928
 * Created Date  : 22-Sep-2015
 * FileName   : DeleteLoadFolders.java
 * Description   : for physical deletion of load folders.
 * Class Version  : 1.0
 */
package com.huawei.datasight.molap.load;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.huawei.datasight.molap.core.load.LoadMetadataDetails;
import com.huawei.datasight.molap.spark.util.LoadMetadataUtil;
import com.huawei.datasight.molap.spark.util.MolapSparkInterFaceLogEvent;
import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFile;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFileFilter;
import com.huawei.unibi.molap.datastorage.store.impl.FileFactory;
import com.huawei.unibi.molap.util.MolapCoreLogEvent;
import com.huawei.unibi.molap.util.MolapProperties;

/**
 * 
 * Project Name : Carbon Module Name : MOLAP Data Processor Author : R00903928
 * Created Date : 24-Sep-2015 FileName : DeleteLoadFolders.java Description :
 * Class Version : 1.0
 */
public final class DeleteLoadFolders
{

    private static final LogService LOGGER = LogServiceFactory
            .getLogService(DeleteLoadFolders.class.getName());

    private DeleteLoadFolders()
    {
    	
    }
    /**
     * 
     * @param path
     * @param loadModel
     */
    public static boolean deleteLoadFoldersFromFileSystem(
            MolapLoadModel loadModel, int partitionCount, String storeLocation,
            boolean isForceDelete, int currentRestructNumber,LoadMetadataDetails[] details)
    {
        String path = null;
        List<LoadMetadataDetails> deletedLoads = new ArrayList<LoadMetadataDetails>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        
        boolean isDeleted = false;
        
        if(details!=null&&details.length != 0)
        {
            for(LoadMetadataDetails oneLoad : details)
            {
                if(checkIfLoadCanBeDeleted(oneLoad, isForceDelete))
                {
                    boolean deletionStatus = false;

                    for(int partitionId = 0;partitionId < partitionCount;partitionId++)
                    {
                        // check load folder in each restructure folder
                        for(int restructureFolderNum = 0;restructureFolderNum <= currentRestructNumber;restructureFolderNum++)
                        {
                            MolapFile[] aggFiles = LoadMetadataUtil
                                    .getAggregateTableList(loadModel,
                                            storeLocation, partitionId,
                                            restructureFolderNum);
                            deleteAggLoadFolders(
                                    aggFiles,
                                    MolapCommonConstants.LOAD_FOLDER
                                            + oneLoad.getLoadName());
                            path = LoadMetadataUtil.createLoadFolderPath(
                                    loadModel, storeLocation, partitionId,
                                    restructureFolderNum);
                            String loadFolderPath = "";
                            // deleting merged load folder
                            if(oneLoad.getMergedLoadName() != null)
                            {
                                loadFolderPath = path
                                        + MolapCommonConstants.FILE_SEPARATOR
                                        + MolapCommonConstants.LOAD_FOLDER
                                        + oneLoad.getMergedLoadName();
                                deletionStatus = physicalFactAndMeasureMetadataDeletion(loadFolderPath);
                            }
                            else
                            {
                                loadFolderPath = path
                                        + MolapCommonConstants.FILE_SEPARATOR
                                        + MolapCommonConstants.LOAD_FOLDER
                                        + oneLoad.getLoadName();
                                deletionStatus = physicalFactAndMeasureMetadataDeletion(loadFolderPath);
                            }
                            if(deletionStatus)
                            {
                                cleanDeletedFactFile(loadFolderPath);
                                factFileRenaming(loadFolderPath);
                                // if deletion status is True then there is no
                                // need to traverse all the RS folders.
                                break;
                            }
                        }

                    }
                    if(deletionStatus)
                    {
                        isDeleted = true;
                        oneLoad.setVisibility("false");
                        deletedLoads.add(oneLoad);
                        LOGGER.info(
                                MolapSparkInterFaceLogEvent.UNIBI_MOLAP_SPARK_INTERFACE_MSG,
                                " Deleted the load " + oneLoad.getLoadName());
                    }
                }
            }
        }

        return isDeleted;
    }

    /**
     * 
     * @param aggFiles
     * @param loadName
     */
    public static void deleteAggLoadFolders(MolapFile[] aggFiles, String loadName) 
    {
    	for(MolapFile file : aggFiles)
    	{
    		deleteLoadFolderFromEachAgg(file,loadName);
    	}
		
	}

    /**
     * 
     * @param file
     * @param loadName
     */
	private static void deleteLoadFolderFromEachAgg(MolapFile file,
			final String loadName) 
    {
        MolapFile[] loadFolders = file.listFiles(new MolapFileFilter()
        {

            @Override
            public boolean accept(MolapFile file)
            {
                if(file.getName().equalsIgnoreCase(loadName))
                {
                    return true;
                }
                return false;
            }
        });

        for(MolapFile loadFolder : loadFolders)
        {
            MolapFile[] files = loadFolder.listFiles();
            // deleting individual files
            if(files != null)
            {
                for(MolapFile eachFile : files)
                {
                    if(!eachFile.delete())
                    {
                        LOGGER.warn(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                                "Unable to delete the file as per delete command "
                                        + loadFolder.getAbsolutePath());
                    }
                }
            }

        }

    }

	/**
     * @param path
     */
    private static boolean physicalFactAndMeasureMetadataDeletion(String path)
    {
        
        boolean status = false;
        try
        {
            if(FileFactory.isFileExist(path, FileFactory.getFileType(path)))
            {
                MolapFile file = FileFactory.getMolapFile(path,
                        FileFactory.getFileType(path));
                MolapFile[] filesToBeDeleted = file
                        .listFiles(new MolapFileFilter()
                        {

                            @Override
                            public boolean accept(MolapFile file)
                            {
                            	
                            	return (file.getName().endsWith(
                                        MolapCommonConstants.FACT_FILE_EXT)
                                        || file.getName()
                                                .endsWith(
                                                      MolapCommonConstants.MEASUREMETADATA_FILE_EXT));
                          /*      if(file.getName().endsWith(
                                        MolapCommonConstants.FACT_FILE_EXT)
                                        || file.getName()
                                                .endsWith(
                                                        MolapCommonConstants.MEASUREMETADATA_FILE_EXT))
                                {
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }*/
                            }
                        });
                
                // if there are no fact and msr metadata files present then no need to keep entry in metadata.
                if(filesToBeDeleted.length == 0)
                {
                    status = true;
                }
                else
                {

                    for(MolapFile eachFile : filesToBeDeleted)
                    {
                        if(!eachFile.delete())
                        {

                            LOGGER.warn(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                                    "Unable to delete the file as per delete command "
                                            + eachFile.getAbsolutePath());
                            status = false;
                        }
                        else
                        {
                            status = true;
                        }
                    }
                }
            }
            else
            {
                status =  false;
            }
        }
        catch(IOException e)
        {
            LOGGER.warn(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                    "Unable to delete the file as per delete command " + path);
        }
        
        return status;

    }

    /**
     * @param oneLoad
     * @return
     */
    private static boolean checkIfLoadCanBeDeleted(LoadMetadataDetails oneLoad,
            boolean isForceDelete)
    {
        if(MolapCommonConstants.MARKED_FOR_DELETE.equalsIgnoreCase(oneLoad.getLoadStatus())&&oneLoad.getVisibility().equalsIgnoreCase("true"))
        {
            if(isForceDelete)
            {
                return true;
            }
            String deletionTime = oneLoad.getDeletionTimestamp();
            SimpleDateFormat parser = new SimpleDateFormat(
                    MolapCommonConstants.MOLAP_TIMESTAMP);
            Date deletionDate = null;
            String date = null;
            Date currentTimeStamp = null;
            try
            {
                deletionDate = parser.parse(deletionTime);
                date = MolapLoaderUtil.readCurrentTime();
                currentTimeStamp = parser.parse(date);
            }
            catch(ParseException e)
            {
                return false;
            }

            long difference = currentTimeStamp.getTime()
                    - deletionDate.getTime();

            long minutesElapsed = (difference / (1000 * 60));
            
            int maxTime; 
            try
            {
                maxTime = Integer.parseInt(MolapProperties.getInstance().
                        getProperty(MolapCommonConstants.MAX_QUERY_EXECUTION_TIME));
            }
            catch(NumberFormatException e)
            {
               maxTime = MolapCommonConstants.DEFAULT_MAX_QUERY_EXECUTION_TIME;
            }
            if(minutesElapsed > maxTime)
            {
                return true;
            }

        }

        return false;
    }

    /**
     * @param loadFolderPath
     */
    private static void factFileRenaming(String loadFolderPath)
    {

        FileFactory.FileType fileType = FileFactory.getFileType(loadFolderPath);
        try
        {
            if(FileFactory.isFileExist(loadFolderPath, fileType))
            {
                MolapFile loadFolder = FileFactory.getMolapFile(loadFolderPath,
                        fileType);

                MolapFile [] listFiles = loadFolder.listFiles(new MolapFileFilter()
                {

                    @Override
                    public boolean accept(MolapFile file)
                    {
                    	return (file.getName().endsWith(
                                '_' + MolapCommonConstants.FACT_FILE_UPDATED));
                    }
                });
                
                for(MolapFile file : listFiles)
                {
                    if(!file.renameTo(file.getName().substring(0, file.getName().length()-MolapCommonConstants.FACT_FILE_UPDATED.length())))
                    {
                        LOGGER.warn(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                                "could not rename the updated fact file.");
                    }
                }
                
            }
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                    "exception" + e.getMessage());
        }

    }
    
    /**
     * deletes the fact file which is marked as _deleted
     * @param loadFolderPath
     */
    private static void cleanDeletedFactFile(String loadFolderPath)
    {
        FileFactory.FileType fileType = FileFactory.getFileType(loadFolderPath);
        try
        {
            if(FileFactory.isFileExist(loadFolderPath, fileType))
            {
                MolapFile loadFolder = FileFactory.getMolapFile(loadFolderPath,
                        fileType);

                MolapFile [] listFiles = loadFolder.listFiles(new MolapFileFilter()
                {

                    @Override
                    public boolean accept(MolapFile file)
                    {
                        return (file.getName().endsWith(
                                MolapCommonConstants.FACT_DELETE_EXTENSION));
                    }
                });
                
                for(MolapFile file : listFiles)
                {
                    if(!file.delete())
                    {
                        LOGGER.warn(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                                "could not delete the marked fact file.");
                    }
                }
                
            }
        }
        catch(IOException e)
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG,
                    "exception" + e.getMessage());
        }
    }

}
