package com.huawei.unibi.molap.sortandgroupby.sortKey;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.datastorage.store.compression.SnappyCompression.SnappyByteCompression;
import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;
import com.huawei.unibi.molap.sortandgroupby.exception.MolapSortKeyAndGroupByException;
import com.huawei.unibi.molap.util.MolapUtil;

/**
 * Project Name NSE V3R8C10 
 * Module Name : MOLAP Data Processor
 * Author :k00900841 
 * Created Date:10-Aug-2014
 * FileName : MolapCompressedSortTempFileReader.java
 * Class Description : Below class will be used to write the compressed sort temp file 
 * Class Version 1.0
 */
public class MolapCompressedSortTempFileWriter extends AbstractMolapSortTempFileWriter
{

    /**
     * MolapCompressedSortTempFileWriter
     * @param measureCount
     * @param mdkeyIndex
     * @param mdKeyLength
     * @param isFactMdkeyInSort
     * @param factMdkeyLength
     * @param writeFileBufferSize
     */
    public MolapCompressedSortTempFileWriter(int measureCount, int mdkeyIndex, int mdKeyLength,
            boolean isFactMdkeyInSort, int factMdkeyLength,
            int writeFileBufferSize, char[] type)
    {
        super(measureCount, mdkeyIndex, mdKeyLength, isFactMdkeyInSort, factMdkeyLength, writeFileBufferSize,type);
    }

    /**
     * Below method will be used to write the sort temp file
     * @param records
     */
    public void writeSortTempFile(Object[][] records)
            throws MolapSortKeyAndGroupByException
    {
        ByteArrayOutputStream[] blockDataArray = null;
        DataOutputStream[] dataOutputStream = null;
        byte[] completeMdkey = null;
        byte[] completeFactMdkey = null;
        Object[] row= null;
        try
        {
            blockDataArray = new ByteArrayOutputStream[measureCount];
            dataOutputStream = new DataOutputStream[measureCount];
            initializeMeasureBuffers(blockDataArray,dataOutputStream, records.length);
            completeMdkey = new byte[mdKeyLength * records.length];
            if(isFactMdkeyInSort)
            {
                completeFactMdkey = new byte[factMdkeyLength * records.length];
            }
            for(int i = 0;i < records.length;i++)
            {
                row = records[i];
                int aggregatorIndexInRowObject = 0;
                // get row from record holder list
                MeasureAggregator[] aggregator = (MeasureAggregator[])row[aggregatorIndexInRowObject];
                for(int j = 0;j < aggregator.length;j++)
                {
                    byte[] byteArray = aggregator[j].getByteArray();
                    stream.writeInt(byteArray.length);
                    stream.write(byteArray);
                }
                stream.writeDouble((Double)row[mdkeyIndex - 1]);
                System.arraycopy((byte[])row[mdkeyIndex], 0, completeMdkey, i
                        * mdKeyLength, mdKeyLength);
                if(isFactMdkeyInSort)
                {
                    System.arraycopy((byte[])row[row.length - 1], 0,
                            completeFactMdkey, i * factMdkeyLength,
                            factMdkeyLength);
                }
            }

            writeCompressData(records.length, completeMdkey,
                    completeFactMdkey,blockDataArray, isFactMdkeyInSort,
                    writeFileBufferSize);

        }
        catch(IOException e)
        {
            throw new MolapSortKeyAndGroupByException(e);
        }
        finally
        {
            MolapUtil.closeStreams(blockDataArray);
            MolapUtil.closeStreams(dataOutputStream);
        }
    }

    /**
     * Below method will be used to write the compress data to temp file
     * @param recordSize
     * @param mdkey
     * @param factMdkey
     * @param measureData
     * @param isFactMdkeyInSort
     * @param writeFileBufferSize
     * @throws IOException
     */
    private void writeCompressData(int recordSize, byte[] mdkey,
            byte[] factMdkey,  ByteArrayOutputStream[] blockDataArray,
            boolean isFactMdkeyInSort,
            int writeFileBufferSize) throws IOException
    {
        stream.writeInt(recordSize);
        byte[] byteArray=null;
        for(int i = 0;i < blockDataArray.length;i++)
        {
            byteArray = SnappyByteCompression.INSTANCE
                    .compress(blockDataArray[i].toByteArray());
            stream.writeInt(byteArray.length);
            stream.write(byteArray);
        }
        byteArray = SnappyByteCompression.INSTANCE.compress(mdkey);
        stream.writeInt(byteArray.length);
        stream.write(byteArray);
        if(isFactMdkeyInSort)
        {
            byteArray = SnappyByteCompression.INSTANCE.compress(factMdkey);
            stream.writeInt(byteArray.length);
            stream.write(byteArray);
        }
    }

    private void initializeMeasureBuffers(
            ByteArrayOutputStream[] blockDataArray,
            DataOutputStream[] dataOutputStream, int recordSizePerLeaf)
    {
        for(int i = 0;i < blockDataArray.length;i++)
        {
            blockDataArray[i] = new ByteArrayOutputStream(recordSizePerLeaf
                    * MolapCommonConstants.DOUBLE_SIZE_IN_BYTE);
            dataOutputStream[i] = new DataOutputStream(blockDataArray[i]);
        }
    }
}
