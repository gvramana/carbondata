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
package com.huawei.unibi.molap.datastorage.store.compression.type;

import java.nio.ByteBuffer;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.datastorage.store.compression.Compressor;
import com.huawei.unibi.molap.datastorage.store.compression.SnappyCompression;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressonHolder.UnCompressValue;
import com.huawei.unibi.molap.datastorage.store.dataholder.MolapReadDataHolder;
import com.huawei.unibi.molap.util.MolapCoreLogEvent;
import com.huawei.unibi.molap.util.ValueCompressionUtil;
import com.huawei.unibi.molap.util.ValueCompressionUtil.DataType;

/**
 * @author S71955
 */
public class UnCompressMaxMinShort implements UnCompressValue<short[]>
{
    /**
     * shortCompressor.
     */
    private static Compressor<short[]> shortCompressor = SnappyCompression.SnappyShortCompression.INSTANCE;

    /**
     * Attribute for Molap LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory.getLogService(UnCompressMaxMinShort.class.getName());

    /**
     * value.
     */
    private short[] value;

    @Override
    public void setValue(short[] value)
    {
        this.value = value;

    }

//    @Override
//    public double getValue(int index, int decimal, double maxValue)
//    {
//        if(value[index] == 0)
//        {
//            return maxValue;
//        }
//        return maxValue - value[index];
//    }
    //TODO SIMIAN
    @Override
    public UnCompressValue uncompress(DataType dataType)
    {
        return null;
    }

    @Override
    public byte[] getBackArrayData()
    {
        return ValueCompressionUtil.convertToBytes(value);
    }

    //TODO SIMIAN
    @Override
    public UnCompressValue getNew()
    {
        try
        {
            return (UnCompressValue)clone();
        }
        catch(CloneNotSupportedException ex3) 
        {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, ex3, ex3.getMessage());
        }
        return null;
    }

    @Override
    public UnCompressValue compress()
    {

        UnCompressMaxMinByte byte1 = new UnCompressMaxMinByte();
        byte1.setValue(shortCompressor.compress(value));
        return byte1;
    }

   

    @Override
    public void setValueInBytes(byte[] value)
    {
        ByteBuffer buffer = ByteBuffer.wrap(value);
        this.value = ValueCompressionUtil.convertToShortArray(buffer, value.length);
    }

    /**
     * 
     * @see com.huawei.unibi.molap.datastorage.store.compression.ValueCompressonHolder.UnCompressValue#getCompressorObject()
     * 
     */
    @Override
    public UnCompressValue getCompressorObject()
    {
        return new UnCompressMaxMinByte();
    }

    //TODO SIMIAN
    @Override
    public MolapReadDataHolder getValues(int decimal, double maxValue)
    {
        double[] vals = new double[value.length];
        MolapReadDataHolder molapDataHolderObj = new MolapReadDataHolder();
        for(int i = 0;i < vals.length;i++)
        {
            if(value[i] == 0)
            {
                vals[i] = maxValue;
            }
            else
            {
                vals[i] =  maxValue - value[i]; 
            }
            
        }
        molapDataHolderObj.setReadableDoubleValues(vals);
        return molapDataHolderObj;
    }

}
