/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:30:38 PM
 * Time to generate: 00:13.553 seconds
 *
 */

package com.huawei.unibi.molap.metadata;

import com.agitar.lib.junit.AgitarTestCase;

public class LeafNodeInfoAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return LeafNodeInfo.class;
    }
    
    public void testConstructor() throws Throwable {
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        assertNull("leafNodeInfo.getFileName()", leafNodeInfo.getFileName());
    }
    
    public void testSetEndKey() throws Throwable {
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        byte[] endKey = new byte[1];
        leafNodeInfo.setEndKey(endKey);
        assertSame("leafNodeInfo.getEndKey()", endKey, leafNodeInfo.getEndKey());
    }
    
    public void testSetFileName() throws Throwable {
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        leafNodeInfo.setFileName("testLeafNodeInfoFileName");
        assertEquals("leafNodeInfo.getFileName()", "testLeafNodeInfoFileName", leafNodeInfo.getFileName());
    }
    
    public void testSetKeyLength() throws Throwable {
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        leafNodeInfo.setKeyLength(100);
        assertEquals("leafNodeInfo.getKeyLength()", 100, leafNodeInfo.getKeyLength());
    }
    
    public void testSetKeyOffset() throws Throwable {
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        leafNodeInfo.setKeyOffset(100L);
        assertEquals("leafNodeInfo.getKeyOffset()", 100L, leafNodeInfo.getKeyOffset());
    }
    
    public void testSetMeasureLength() throws Throwable {
        int[] measureLength = new int[1];
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        leafNodeInfo.setMeasureLength(measureLength);
        assertSame("leafNodeInfo.getMeasureLength()", measureLength, leafNodeInfo.getMeasureLength());
    }
    
    public void testSetMeasureOffset() throws Throwable {
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        long[] measureOffset = new long[1];
        leafNodeInfo.setMeasureOffset(measureOffset);
        assertSame("leafNodeInfo.getMeasureOffset()", measureOffset, leafNodeInfo.getMeasureOffset());
    }
    
    public void testSetNumberOfKeys() throws Throwable {
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        leafNodeInfo.setNumberOfKeys(100);
        assertEquals("leafNodeInfo.getNumberOfKeys()", 100, leafNodeInfo.getNumberOfKeys());
    }
    
    public void testSetStartKey() throws Throwable {
        LeafNodeInfo leafNodeInfo = new LeafNodeInfo();
        byte[] startKey = new byte[3];
        leafNodeInfo.setStartKey(startKey);
        assertSame("leafNodeInfo.getStartKey()", startKey, leafNodeInfo.getStartKey());
    }
}

