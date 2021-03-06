/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwdEVzw1icjfRowqz2DW4XzUpEhhSzBOwVynEHjc
u0090Rrc7a1VMOPW8wLKiuV7AQOBmi8dgq3z5firoRvuYofzEE9kdr1iP5B+XVk2w32H6qx2
MtXldEjXlh0x1cT35GdFEJgN6LvU0/vlJ2jQWHkJYwdv8kUffUA8qPHoHEgapQ==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
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
package com.huawei.unibi.molap.engine.aggregator.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;

/**
 * Project Name NSE V3R7C00
 * 
 * Module Name : Molap Engine
 * 
 * Author K00900841
 * 
 * Created Date :13-May-2013 3:35:33 PM
 * 
 * FileName : CountAggregator.java
 * 
 * Class Description : It will return total count of values
 * 
 * Version 1.0
 */
public class CountAggregator implements MeasureAggregator
{

    /**
     * 
     * serialVersionUID
     * 
     */
    private static final long serialVersionUID = 2678878935295306313L;

    /**
     * aggregate value
     */
    private double aggVal;

    /**
     * Count Aggregate function which update the total count
     * 
     * @param newVal
     *            new value
     * @param key
     *            mdkey
     * @param offset
     *            key offset
     * @param length
     *            length to be considered
     * 
     */
    @Override
    public void agg(double newVal, byte[] key, int offset, int length)
    {
        aggVal++;
    }
    
    /**
     * Count Aggregate function which update the total count
     * 
     * @param newVal
     *            new value
     * @param key
     *            mdkey
     * @param offset
     *            key offset
     * @param length
     *            length to be considered
     * 
     */
    @Override
    public void agg(Object newVal, byte[] key, int offset, int length)
    {
        aggVal++;
    }

    /**
     * Below method will be used to get the value byte array
     */
    @Override
    public byte[] getByteArray()
    {
        ByteBuffer buffer = ByteBuffer.allocate(MolapCommonConstants.DOUBLE_SIZE_IN_BYTE);
        buffer.putDouble(aggVal);
        return buffer.array();
    }

    /**
     * Returns the total count
     * 
     * @return total count
     * 
     */
    @Override
    public double getValue()
    {
        return aggVal;
    }

    /**
     * Merge the total count with the aggregator
     * 
     * @param Aggregator
     *            count aggregator
     * 
     */
    @Override
    public void merge(MeasureAggregator aggregator)
    {
        CountAggregator countAggregator = (CountAggregator)aggregator;
        aggVal += countAggregator.aggVal;
    }

    /**
     * Overloaded Aggregate function will be used for Aggregate tables because
     * aggregate table will have fact_count as a measure. It will update the
     * total count
     * 
     * @param newVal
     *            new value
     * @param factCount
     *            total fact count
     * 
     */
    @Override
    public void agg(double newVal, double factCount)
    {
        agg(newVal, null, 0, 0);
    }

    /**
     * This method return the count value as an object
     * 
     * @return count value as an object
     */

    @Override
    public Object getValueObject()
    {
        return aggVal;
    }

    /**
     * 
     * @see com.huawei.unibi.molap.engine.aggregator.MeasureAggregator#setNewValue(double)
     * 
     */
    @Override
    public void setNewValue(double newValue)
    {
        aggVal+=newValue;
    }

    @Override
    public boolean isFirstTime()
    {
        return false;
    }
    
    @Override
    public void writeData(DataOutput output) throws IOException
    {
        output.writeDouble(aggVal);
        
    }

    @Override
    public void readData(DataInput inPut) throws IOException
    {
        aggVal = inPut.readDouble();
    }

    @Override
    public MeasureAggregator getCopy()
    {
        CountAggregator aggregator = new CountAggregator();
        aggregator.aggVal = aggVal;
        return aggregator;
    }
    
    //we are not comparing the Aggregator values 
   /* public boolean equals(MeasureAggregator msrAggregator){
        return compareTo(msrAggregator)==0;
    }*/
    
    @Override
    public void merge(byte[] value)
    {
        if(0 == value.length)
        {
            return;
        }
        ByteBuffer buffer = ByteBuffer.wrap(value);
        aggVal += buffer.getDouble();
    }
    
    @Override
    public int compareTo(MeasureAggregator obj)
    {
        double val = getValue();
        double otherVal = obj.getValue();
        if(val > otherVal)
        {
            return 1;
        }
        if(val < otherVal)
        {
            return -1;
        }
        return 0;
    }

    @Override
    public MeasureAggregator get()
    {
        return this;
    }
    
    public String toString()
    {
        return aggVal+"";
    }

   
}
