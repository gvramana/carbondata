/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2014
 * =====================================
 *
 */
package com.huawei.unibi.molap.datastorage.store.dataholder;

/**
 * Project Name NSE V3R8C10 
 * Module Name : MOLAP Data Processor
 * Author :k00900841 
 * Created Date:10-Aug-2014
 * FileName : MolapReadDataHolder.java
 * Class Description : data holder which will be used by all the classes when they want to write the data 
 * Class Version 1.0
 */
public class MolapWriteDataHolder
{
    /**
     * doubleValues
     */
    private double[] doubleValues;
    
    /**
     * byteValues
     */
    private byte[][] byteValues;

    /**
     * byteValues
     */
    private byte[][][] columnByteValues;
    
    /**
     * size
     */
    private int size;
    
    /**
     * totalSize
     */
    private int totalSize;
    
    /**
     * Method to initialise double array
     * @param size
     */
    public void initialiseDoubleValues(int size)
    {
        if(size<1)
        {
            throw new IllegalArgumentException("Invalid array size");
        }
        doubleValues= new double[size];
    }
    
    /**
     * method to initialise byte array
     * @param size
     */
    public void initialiseByteArrayValues(int size)
    {
        if(size<1)
        {
            throw new IllegalArgumentException("Invalid array size");
        }
        
        byteValues= new byte[size][];
        columnByteValues= new byte[size][][];
    }
    /**
     * set double value by index
     * @param index
     * @param value
     */
    public void setWritableDoubleValueByIndex(int index,double value)
    {
        doubleValues[index]=value;
        size++;
    }
    
    /**
     * set byte array value by index
     * @param index
     * @param value
     */
    public void setWritableByteArrayValueByIndex(int index, byte[] value)
    {
        byteValues[index]=value;
        size++;
        if(null != value)
        totalSize+=value.length;
    }

    /**
     * set byte array value by index
     * @param index
     * @param value
     */
    public void setWritableByteArrayValueByIndex(int index, int mdKeyIndex, Object[] columnData)
    {
        int l=0;
        columnByteValues[index] = new byte[columnData.length - (mdKeyIndex+1)][];
        for(int i=mdKeyIndex+1;i<columnData.length;i++)
        {
            columnByteValues[index][l++]=(byte[])columnData[i];
        }
    }
    
    /**
     * Get Writable Double Values
     * @return
     */
    public double[] getWritableDoubleValues()
    {
        if(size<doubleValues.length)
        {
            double[]temp = new double[size];
            System.arraycopy(doubleValues, 0, temp, 0, size);
            doubleValues=temp;
        }
        return doubleValues;
    }
    
    /**
     * Get writable byte array values
     * @return
     */
    public byte[] getWritableByteArrayValues()
    {
        byte[] temp = new byte[totalSize];
        int startIndexToCopy=0;
        for(int i = 0;i < size;i++)
        {
            System.arraycopy(byteValues[i], 0, temp, startIndexToCopy, byteValues[i].length);
            startIndexToCopy+=byteValues[i].length;
        }
        return temp;
    }
    
    public byte[][] getByteArrayValues()
    {
        if(size<byteValues.length)
        {
            byte[][] temp = new byte[size][];
            System.arraycopy(byteValues, 0, temp, 0, size);
            byteValues=temp;
        }
        return byteValues;
    }
    
    public byte[][][] getColumnByteArrayValues()
    {
        if(size<columnByteValues.length)
        {
            byte[][][] temp = new byte[size][][];
            System.arraycopy(columnByteValues, 0, temp, 0, size);
            columnByteValues=temp;
        }
        return columnByteValues;
    }
    
    public void reset()
    {
        size=0;
        totalSize=0;
    }
}
