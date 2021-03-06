/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 4:53:37 PM
 * Time to generate: 00:13.319 seconds
 *
 */

package com.huawei.unibi.molap.engine.filters;

import com.agitar.lib.junit.AgitarTestCase;

public class InMemFilterModelAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return InMemFilterModel.class;
    }
    
    public void testConstructor() throws Throwable {
        InMemFilterModel inMemFilterModel = new InMemFilterModel();
        assertEquals("inMemFilterModel.getMaxSize()", 0, inMemFilterModel.getMaxSize());
        assertNull("inMemFilterModel.getFilter()", inMemFilterModel.getFilter());
        assertNull("inMemFilterModel.getMaxKey()", inMemFilterModel.getMaxKey());
    }
    
    public void testConstructor1() throws Throwable {
        byte[][] maxKey = new byte[1][];
        byte[][][] filter = new byte[3][][];
        InMemFilterModel inMemFilterModel = new InMemFilterModel(filter, maxKey, 100);
        assertEquals("inMemFilterModel.getMaxSize()", 100, inMemFilterModel.getMaxSize());
        assertSame("inMemFilterModel.getFilter()", filter, inMemFilterModel.getFilter());
        assertSame("inMemFilterModel.getMaxKey()", maxKey, inMemFilterModel.getMaxKey());
    }
    
    public void testSetColExcludeDimOffset() throws Throwable {
        byte[][][] filter = new byte[0][][];
        byte[][] maxKey = new byte[3][];
        InMemFilterModel inMemFilterModel = new InMemFilterModel(filter, maxKey, 100);
        int[] colExcludeDimOffset = new int[2];
        inMemFilterModel.setColExcludeDimOffset(colExcludeDimOffset);
        assertSame("inMemFilterModel.getColExcludeDimOffset()", colExcludeDimOffset, inMemFilterModel.getColExcludeDimOffset());
    }
    
    public void testSetColIncludeDimOffset() throws Throwable {
        byte[][][] filter = new byte[0][][];
        byte[][] maxKey = new byte[3][];
        InMemFilterModel inMemFilterModel = new InMemFilterModel(filter, maxKey, 100);
        int[] colIncludeDimOffset = new int[1];
        inMemFilterModel.setColIncludeDimOffset(colIncludeDimOffset);
        assertSame("inMemFilterModel.getColIncludeDimOffset()", colIncludeDimOffset, inMemFilterModel.getColIncludeDimOffset());
    }
    
    public void testSetExcludeFilter() throws Throwable {
        byte[][][] filter = new byte[0][][];
        byte[][] maxKey = new byte[3][];
        InMemFilterModel inMemFilterModel = new InMemFilterModel(filter, maxKey, 100);
        byte[][][] excludeFilter = new byte[2][][];
        inMemFilterModel.setExcludeFilter(excludeFilter);
        assertSame("inMemFilterModel.getExcludeFilter()", excludeFilter, inMemFilterModel.getExcludeFilter());
    }
    
    public void testSetExcludePredicateKeys() throws Throwable {
        byte[][][] filter = new byte[0][][];
        byte[][] maxKey = new byte[3][];
        InMemFilterModel inMemFilterModel = new InMemFilterModel(filter, maxKey, 100);
        long[][] excludePredicateKeys = new long[0][];
        inMemFilterModel.setExcludePredicateKeys(excludePredicateKeys);
        assertSame("inMemFilterModel.getExcludePredicateKeys()", excludePredicateKeys, inMemFilterModel.getExcludePredicateKeys());
    }
    
    public void testSetIncludePredicateKeys() throws Throwable {
        byte[][][] filter = new byte[0][][];
        byte[][] maxKey = new byte[3][];
        InMemFilterModel inMemFilterModel = new InMemFilterModel(filter, maxKey, 100);
        long[][] includePredicateKeys = new long[2][];
        inMemFilterModel.setIncludePredicateKeys(includePredicateKeys);
        assertSame("inMemFilterModel.getIncludePredicateKeys()", includePredicateKeys, inMemFilterModel.getIncludePredicateKeys());
    }
}

