/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcfJtSMNYgnOYiEQwbS13nxM8hk/dmbY4B4u+tG
aRAl/h/0feSK+gQDnLiG/Rxa+KkYw1QAh29K3UhDSqor5ZTvSvr6uw8bzVv0TPChMi5H+8lU
YAABPFf8bcJGeefBLsVbOA3FfWqCLulpUNsCHQ1DsQ1OXX5mt4ulc8VxcamD2w==*/
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

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFile;
import com.huawei.unibi.molap.datastorage.store.impl.FileFactory;
import com.huawei.unibi.molap.datastorage.store.impl.FileFactory.FileType;
import com.huawei.unibi.molap.util.MolapDataProcessorLogEvent;
import com.huawei.unibi.molap.util.MolapUtil;

/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap
 * Author V00900840
 * Created Date :06-Aug-2013 1:45:51 AM
 * FileName : BadRecordslogger.java
 * Class Description : This class will log the bad logs into files
 * Version 1.0
 */
public class BadRecordslogger
{

    /**
     * 
     * Comment for <code>LOGGER</code>
     * 
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(BadRecordslogger.class.getName());

    /**
     * File Name
     */
    private String fileName;

    /**
     * Store path
     */
    private String storePath;

    /**
     * Log Strings
     */
    // private StringBuilder logStrings;

    /**
     * FileChannel
     */
    private BufferedWriter bufferedWriter;

    private DataOutputStream outStream;

    /**
     * 
     */
    private MolapFile logFile;

    /**
     * Which holds the key and if any bad rec found to check from API to update
     * the status
     */
    private static Map<String, String> badRecordEntry = new HashMap<String, String>(
            MolapCommonConstants.DEFAULT_COLLECTION_SIZE);

    /**
     * task key which is Schemaname/CubeName/tablename
     */
    private String taskKey;

    // private final Object syncObject =new Object();

    public BadRecordslogger(String key, String fileName, String storePath)
    {
        // Initially no bad rec
        taskKey = key;
        this.fileName = fileName;
        this.storePath = storePath;
    }

    /**
     * @param row
     * @param size
     * @param string
     */
    public void addBadRecordsToBilder(Object[] row, int size, String reason,
            String valueComparer)
    {
        StringBuilder logStrings = new StringBuilder();
        int count = size;
        for(int i = 0;i < size;i++)
        {
            if(null == row[i])
            {
                logStrings.append(row[i]);
            }
            else if(MolapCommonConstants.MEMBER_DEFAULT_VAL.equals(row[i]
                    .toString()))
            {
                logStrings.append(valueComparer);
            }
            else
            {
                logStrings.append(row[i]);
            }
            if(count > 1)
            {
                logStrings.append(" , ");
            }
            count--;
        }

        logStrings.append("----->");
        if(null != reason)
        {
            if(reason.indexOf(MolapCommonConstants.MEMBER_DEFAULT_VAL) > -1)
            {
                logStrings
                        .append(reason.replace(
                                MolapCommonConstants.MEMBER_DEFAULT_VAL,
                                valueComparer));
            }
            else
            {
                logStrings.append(reason);
            }
        }

        writeBadRecordsToFile(logStrings);
    }

    /**
     * 
     */
    private synchronized void writeBadRecordsToFile(StringBuilder logStrings)
    {
        // synchronized (syncObject)
        // {
        String filePath = this.storePath + File.separator + this.fileName
                + MolapCommonConstants.FILE_INPROGRESS_STATUS;
        if(null == logFile)
        {
            logFile = FileFactory.getMolapFile(filePath,
                    FileFactory.getFileType(filePath));
        }
        // }

        try
        {
            if(null == bufferedWriter)
            {
                FileType fileType = FileFactory.getFileType(storePath);
                if(!FileFactory.isFileExist(this.storePath, fileType))
                {
                    // create the folders if not exist
                    // new File(this.storePath).mkdirs();
                    FileFactory.mkdirs(this.storePath, fileType);

                    // create the files
                    // logFile.createNewFile();
                    FileFactory.createNewFile(filePath, fileType);
                }

                outStream = FileFactory.getDataOutputStream(filePath, fileType);

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                        outStream,
                        MolapCommonConstants.MOLAP_DEFAULT_STREAM_ENCODEFORMAT));

            }
            bufferedWriter.write(logStrings.toString());
            bufferedWriter.newLine();
        }
        catch(FileNotFoundException e)
        {
            LOGGER.error(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "Bad Log Files not found");
        }
        catch(IOException e)
        {
            LOGGER.error(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "Error While writing bad log File");
        }
        finally
        {
            // if the Bad record file is created means it partially success
            // if any entry present with key that means its have bad record for
            // that key
            badRecordEntry.put(taskKey, "Partially");
        }
    }

    /**
     * closeStreams void
     */
    public synchronized void closeStreams()
    {
        MolapUtil.closeStreams(bufferedWriter, outStream);
    }

    /**
     * @param key
     *            Schemaname/CubeName/tablename
     * @return return "Partially" and remove from map
     */
    public static String hasBadRecord(String key)
    {
        return badRecordEntry.remove(key);
    }

}

