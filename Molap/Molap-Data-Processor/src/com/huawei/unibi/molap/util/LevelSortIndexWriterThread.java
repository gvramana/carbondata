package com.huawei.unibi.molap.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.Callable;

import org.apache.commons.codec.binary.Base64;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFile;
import com.huawei.unibi.molap.datastorage.store.impl.FileFactory;
import com.huawei.unibi.molap.engine.datastorage.DataType;
import com.huawei.unibi.molap.engine.util.MemberSortModel;

/**
 * Below class is responsible for creating the level sort index data file
 * 
 * @author K00900841
 *
 */
public class LevelSortIndexWriterThread implements Callable<Void>
{
    
    /**
     * 
     * Comment for <code>LOGGER</code>
     * 
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(LevelSortIndexWriterThread.class.getName());
    
    private String levelFilePath;

    private int minSurrogate;

    private int maxSurrogate;
    
    private DataType memberDataType;

    /**
     * Sort index after members are started
     */
    private int[] sortOrderIndex;

    /**
     * Reverse sort index to retrive the member
     */
    private int[] sortReverseOrderIndex;

    /**
     * LevelSortIndexWriterThread
     * @param levelFilePath
     * @param dataType
     */
    public LevelSortIndexWriterThread(String levelFilePath, String dataType)
    {
        this.levelFilePath = levelFilePath;
        if(dataType.equalsIgnoreCase("Numeric") ||dataType.equalsIgnoreCase("Integer"))
        {//CHECKSTYLE:ON
            memberDataType = DataType.NUMBER;
        }
        else if(dataType.equalsIgnoreCase("Timestamp"))
        {
            memberDataType=DataType.TIMESTAMP;
        }
        else
        {
            memberDataType=DataType.STRING;
        }
    }

    /**
     * call method which will execute the task 
     */
    @Override
    public Void call() throws Exception
    {
        MemberSortModel[] data = getLevelData();
        createSortIndex(data);
        writeUpdatedLevelFile();
        return null;
    }

    /**
     * Create the sort index for the members. It will be useful when sorting
     * using surrogates
     */
    private void createSortIndex(MemberSortModel[] models)
    {
        Arrays.sort(models);
        sortOrderIndex = new int[minSurrogate + models.length];
        sortReverseOrderIndex = new int[maxSurrogate + 1];
        for(int i = 0;i < models.length;i++)

        {
            MemberSortModel memberSortModel = models[i];
            sortOrderIndex[i + minSurrogate] = memberSortModel.getKey();
            sortReverseOrderIndex[memberSortModel.getKey()] = i + minSurrogate;
        }
        models = null;
    }
    
    private MemberSortModel[] getLevelData() throws IOException
    {
        DataInputStream fileChannel = null;
        long currPositionIndex = 0;
        long size = 0;
        ByteBuffer buffer = null;
        
     // CHECKSTYLE:OFF Approval No:Approval-V1R2C10_005
        boolean enableEncoding = Boolean.valueOf(MolapProperties.getInstance()
                .getProperty(MolapCommonConstants.ENABLE_BASE64_ENCODING,
                        MolapCommonConstants.ENABLE_BASE64_ENCODING_DEFAULT));
        // CHECKSTYLE:ON
        try
        {
            fileChannel = FileFactory.getDataInputStream(levelFilePath,
                    FileFactory.getFileType(levelFilePath));
            MolapFile memberFile = FileFactory.getMolapFile(levelFilePath,
                    FileFactory.getFileType(levelFilePath));
            size = memberFile.getSize() - 4;
            long skipSize=size;
            long actualSkipSize=0;
            while(actualSkipSize!=size)
            {
                actualSkipSize+=fileChannel.skip(skipSize);
                skipSize=skipSize-actualSkipSize;
            }
            maxSurrogate = fileChannel.readInt();
        }
        catch(IOException e)
        {
            LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, e,"problem while reading the level file");
            /*return null;*/
            throw e;
        }
        finally
        {
            MolapUtil.closeStreams(fileChannel);
        }
        
        try
        {
            fileChannel = FileFactory.getDataInputStream(levelFilePath,
                    FileFactory.getFileType(levelFilePath));
         // CHECKSTYLE:OFF Approval No:Approval-V1R2C10_005
            buffer = ByteBuffer.allocate((int)size);
         // CHECKSTYLE:ON
            fileChannel.readFully(buffer.array());
            buffer.rewind();
        }
        catch(IOException e)
        {
            LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, e,"problem while reading the level file");
            /*return null;*/
            throw e;
        }
        finally
        {
            MolapUtil.closeStreams(fileChannel);
        }
        minSurrogate = buffer.getInt();
        MemberSortModel[] surogateKeyArrays = new MemberSortModel[maxSurrogate - minSurrogate + 1];
        int surrogateKeyIndex = minSurrogate;
        currPositionIndex += 4;
        int current = 0;
        
        while(currPositionIndex < size)
        {
            int len = buffer.getInt();
            // CHECKSTYLE:OFF Approval No:Approval-V1R2C10_005
            // CHECKSTYLE:ON
            currPositionIndex += 4;
            byte[] rowBytes = new byte[len];
            buffer.get(rowBytes);
            currPositionIndex += len;
            String memberName = null;// CHECKSTYLE:OFF Approval
                                     // No:Approval-361
            if(!memberDataType.equals(DataType.STRING))
            {
                if(enableEncoding)
                {
                    memberName = new String(Base64.decodeBase64(rowBytes),
                            Charset.defaultCharset());
                }
                else
                {
                    memberName = new String(rowBytes, Charset.defaultCharset());
                }
                surogateKeyArrays[current] = new MemberSortModel(surrogateKeyIndex, memberName, null,memberDataType);
            }
            else
            {
                if(enableEncoding)
                {
                    rowBytes=Base64.decodeBase64(rowBytes);
                }
                surogateKeyArrays[current] = new MemberSortModel(surrogateKeyIndex, null, rowBytes,memberDataType);
            }
            surrogateKeyIndex++;
            current++;
        }
        return surogateKeyArrays;
    }

    private void writeUpdatedLevelFile() throws IOException
    {
        DataOutputStream dataOutputStream = null;
        try
        {
        	int lastIndexOf = levelFilePath.lastIndexOf(".level");
        	String path=levelFilePath.substring(0, lastIndexOf);
        	
            dataOutputStream = FileFactory.getDataOutputStream(path
                    + MolapCommonConstants.LEVEL_SORT_INDEX_FILE_EXT,
                    FileFactory.getFileType(path));

            dataOutputStream.writeInt(sortOrderIndex.length);
            for(int i = 0;i < sortOrderIndex.length;i++)
            {
                dataOutputStream.writeInt(sortOrderIndex[i]);
            }
            dataOutputStream.writeInt(sortReverseOrderIndex.length);
            for(int i = 0;i < sortReverseOrderIndex.length;i++)
            {
                dataOutputStream.writeInt(sortReverseOrderIndex[i]);
            }
        }
        catch(IOException e)
        {
            LOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, e,"problem while writing the level sort index file");
            throw e;
        }
        finally
        {
            MolapUtil.closeStreams(dataOutputStream);
        }
    }

}
