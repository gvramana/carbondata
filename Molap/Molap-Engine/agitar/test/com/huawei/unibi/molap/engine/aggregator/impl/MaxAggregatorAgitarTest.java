/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 5:00:01 PM
 * Time to generate: 00:13.615 seconds
 *
 */

package com.huawei.unibi.molap.engine.aggregator.impl;

import com.agitar.lib.junit.AgitarTestCase;

public class MaxAggregatorAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return MaxAggregator.class;
    }
    
    public void testConstructor() throws Throwable {
        MaxAggregator maxAggregator = new MaxAggregator();
        assertEquals("maxAggregator.getValue()", Double.MIN_VALUE, maxAggregator.getValue(), 1.0E-6);
    }
    
    public void testAgg() throws Throwable {
        MaxAggregator maxAggregator = new MaxAggregator();
        byte[] key = new byte[3];
        maxAggregator.agg(0.0, key, 100, 1000);
        assertEquals("maxAggregator.getValue()", Double.MIN_VALUE, maxAggregator.getValue(), 1.0E-6);
    }
    
    public void testAgg1() throws Throwable {
        MaxAggregator maxAggregator = new MaxAggregator();
        byte[] key = new byte[3];
        maxAggregator.agg(100.0, key, 100, 1000);
        assertEquals("maxAggregator.getValue()", 100.0, maxAggregator.getValue(), 1.0E-6);
    }
    
    public void testAgg2() throws Throwable {
        MaxAggregator maxAggregator = new MaxAggregator();
        maxAggregator.agg(100.0, 1000.0);
        assertEquals("maxAggregator.getValue()", 100.0, maxAggregator.getValue(), 1.0E-6);
    }
    
    public void testGetValue() throws Throwable {
        double result = new MaxAggregator().getValue();
        assertEquals("result", Double.MIN_VALUE, result, 1.0E-6);
    }
    
    public void testGetValueObject() throws Throwable {
        MaxAggregator maxAggregator = new MaxAggregator();
        maxAggregator.agg(100.0, 1000.0);
        Double result = (Double) maxAggregator.getValueObject();
        assertEquals("result", 100.0, result.doubleValue(), 1.0E-6);
    }
    
    public void testGetValueObject1() throws Throwable {
        Double result = (Double) new MaxAggregator().getValueObject();
        assertEquals("result", Double.MIN_VALUE, result.doubleValue(), 1.0E-6);
    }
    
    public void testMerge() throws Throwable {
        MaxAggregator maxAggregator = new MaxAggregator();
        maxAggregator.merge(new MaxAggregator());
        assertEquals("maxAggregator.getValue()", Double.MIN_VALUE, maxAggregator.getValue(), 1.0E-6);
    }
    
    public void testMergeThrowsClassCastException() throws Throwable {
        MaxAggregator maxAggregator = new MaxAggregator();
        try {
            maxAggregator.merge(new SumAggregator());
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException ex) {
            assertEquals("ex.getClass()", ClassCastException.class, ex.getClass());
            assertThrownBy(MaxAggregator.class, ex);
            assertEquals("maxAggregator.getValue()", Double.MIN_VALUE, maxAggregator.getValue(), 1.0E-6);
        }
    }
    
    public void testMergeThrowsNullPointerException() throws Throwable {
        MaxAggregator maxAggregator = new MaxAggregator();
        try {
            maxAggregator.merge(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(MaxAggregator.class, ex);
            assertEquals("maxAggregator.getValue()", Double.MIN_VALUE, maxAggregator.getValue(), 1.0E-6);
        }
    }
}

