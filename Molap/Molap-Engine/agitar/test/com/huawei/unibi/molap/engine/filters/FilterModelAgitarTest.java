/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 4:52:56 PM
 * Time to generate: 00:13.225 seconds
 *
 */

package com.huawei.unibi.molap.engine.filters;

import com.agitar.lib.junit.AgitarTestCase;

public class FilterModelAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return FilterModel.class;
    }
    
    public void testConstructor() throws Throwable {
        byte[][] maxKey = new byte[0][];
        byte[][][] filter = new byte[1][][];
        FilterModel filterModel = new FilterModel(filter, maxKey, 100);
        assertEquals("filterModel.getMaxSize()", 100, filterModel.getMaxSize());
        assertSame("filterModel.getMaxKey()", maxKey, filterModel.getMaxKey());
        assertSame("filterModel.getFilter()", filter, filterModel.getFilter());
    }
    
    public void testSetFilter() throws Throwable {
        byte[][] maxKey = new byte[0][];
        byte[][][] filter = new byte[1][][];
        FilterModel filterModel = new FilterModel(filter, maxKey, 100);
        byte[][][] filter2 = new byte[0][][];
        filterModel.setFilter(filter2);
        assertSame("filterModel.getFilter()", filter2, filterModel.getFilter());
    }
    
    public void testSetMaxKey() throws Throwable {
        byte[][] maxKey = new byte[0][];
        byte[][][] filter = new byte[1][][];
        FilterModel filterModel = new FilterModel(filter, maxKey, 100);
        byte[][] maxKey2 = new byte[1][];
        filterModel.setMaxKey(maxKey2);
        assertSame("filterModel.getMaxKey()", maxKey2, filterModel.getMaxKey());
        assertNull("filterModel.getMaxKey()[0]", filterModel.getMaxKey()[0]);
    }
    
    public void testSetMaxSize() throws Throwable {
        byte[][] maxKey = new byte[0][];
        byte[][][] filter = new byte[1][][];
        FilterModel filterModel = new FilterModel(filter, maxKey, 100);
        filterModel.setMaxSize(1000);
        assertEquals("filterModel.getMaxSize()", 1000, filterModel.getMaxSize());
    }
}

