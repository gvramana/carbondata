/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwfQVwqh74rUY6n+OZ2pUrkn1TkkvO60rFu08DZa
JnQq9AHq8/lq8pVcHIKN+Su9bC5TDK2gaqPHxaZ8asciCbltvUHdYlGX38Dq7vt75kaf7rSc
ZQrdtFfUauMF6QzTN7i4XqIQvAlIFVA2Uo/yO2h60rOAhlEuKD0HRoAv6JpBfA==*/
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
package com.huawei.unibi.molap.engine.holders;

import java.util.List;

/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap
 * Author K00900841
 * Created Date :13-May-2013 3:35:33 PM
 * FileName : SegmentDataIterator.java
 * Class Description :
 * This class can be used to re index the existing data array to read in
 * different column order. Ex:- For tuple query, data order is
 * dimensions order in query. For Segment query the order as per
 * dimensions in cube.
 * If we re-index the same data with required order, we can reuse the
 * same data array for both purposes.
 * 
 * Version 1.0
 */
public class SegmentResultHolder extends MolapResultHolder
{

    public SegmentResultHolder(List<com.huawei.unibi.molap.olap.SqlStatement.Type> dataTypes)
    {
        super(dataTypes);
        // TODO Auto-generated constructor stub
    }
//    /**
//     * Actual Iterator on data
//     */
//    private MolapResultHolder actual;
//
//    /**
//     * Index mapping for each dimension.
//     */
//    private int[] dimIndexMap;
//
//    /**
//     * Data type
//     */
//    private List<Type> dataTypes;
//
//    /**
//     * 
//     * 
//     * @param actual
//     * @param dimIndexMap
//     *
//     */
//    public SegmentResultHolder(MolapResultHolder actual, int[] dimIndexMap)
//    {
//        super(null);
//
//        this.actual = actual;
//        this.dimIndexMap = dimIndexMap;
//
//        dataTypes = new ArrayList<SqlStatement.Type>(100);
//
//        for(Integer dimension : this.dimIndexMap)
//        {
//            dataTypes.add(actual.getDataType(dimension));
//        }
//    }
//
//    
//    /**
//     * @see com.huawei.unibi.molap.engine.util.MolapResultHolder#getDataType(int)
//     */
//    public Type getDataType(int column)
//    {
//        return dataTypes.get(column);
//    }
//
//    /**
//     * Return the Sql data type,  Wrapped methods to ignore properties.
//     * 
//     * @return Sql data type
//     * 
//     */
//    public List<Type> getDataTypes()
//    {
//        return dataTypes;
//    }
//
//    /**
//     * @see com.huawei.unibi.molap.engine.util.MolapResultHolder#getObject(int)
//     */
//    public Object getObject(int column)
//    {
//        return actual.getObject(dimIndexMap[column - 1] + 1);
//    }
//
//    /**
//     * 
//     * @see com.huawei.unibi.molap.engine.util.MolapResultHolder#isNext()
//     */
//    public boolean isNext()
//    {
//        return actual.isNext();
//    }
//
//    /**
//     * 
//     * @see com.huawei.unibi.molap.engine.util.MolapResultHolder#reset()
//     */
//    public void reset()
//    {
//        actual.reset();
//    }
//
//    /**
//     * 
//     * @see com.huawei.unibi.molap.engine.util.MolapResultHolder#setObject(java.lang
//     *      .Object[][])
//     */
//    public void setObject(Object[][] data)
//    {
//        actual.setObject(data);
//    }
//
//    /**
//     * 
//     * @see com.huawei.unibi.molap.engine.util.MolapResultHolder#getColumnCount()
//     */
//    public int getColumnCount()
//    {
//        return actual.getColumnCount();
//    }
//
//    /**
//     * 
//     * @see com.huawei.unibi.molap.engine.util.MolapResultHolder#getInt(int)
//     */
//    public int getInt(int column)
//    {
//        return actual.getInt(dimIndexMap[column - 1] + 1);
//    }
//
//    /**
//     * 
//     * @see com.huawei.unibi.molap.engine.util.MolapResultHolder#getLong(int)
//     */
//    public long getLong(int column)
//    {
//        return actual.getLong(dimIndexMap[column - 1] + 1);
//    }
//
//    /**
//     * Return the double value for column
//     * 
//     * @param column
//     *          column index
//     * @return double value
//     *          
//     *
//     */
//    public double getDouble(int column)
//    {
//        return actual.getDouble(dimIndexMap[column - 1] + 1);
//    }
//
//    /**
//     * Check whether last value is null or not
//     * 
//     * @return last value was null or not
//     *
//     */
//    public boolean wasNull()
//    {
//        return actual.wasNull();
//    }
//
//    /**
//     * Set the sql data type, Wrapped methods to ignore properties.
//     * 
//     * @param types
//     *            sql data type
//     * 
//     */
//    public void setDataTypes(List<Type> types)
//    {
//        actual.setDataTypes(types);
//    }

}
