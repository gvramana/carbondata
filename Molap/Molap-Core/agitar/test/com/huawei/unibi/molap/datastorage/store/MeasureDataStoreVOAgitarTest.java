/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:30:42 PM
 * Time to generate: 00:16.101 seconds
 *
 */

package com.huawei.unibi.molap.datastorage.store;

import com.agitar.lib.junit.AgitarTestCase;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionModel;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionUtil;
import com.huawei.unibi.molap.datastorage.store.impl.FileHolderImpl;

public class MeasureDataStoreVOAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return MeasureDataStoreVO.class;
    }
    
    public void testConstructor() throws Throwable {
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        assertNull("measureDataStoreVO.getFilePath()", measureDataStoreVO.getFilePath());
    }
    
    public void testSetCompressionModel() throws Throwable {
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testMeasureDataStoreVOMeasureMetaDataFileLocation", 100);
        measureDataStoreVO.setCompressionModel(compressionModel);
        assertSame("measureDataStoreVO.getCompressionModel()", compressionModel, measureDataStoreVO.getCompressionModel());
    }
    
    public void testSetElementSize() throws Throwable {
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        measureDataStoreVO.setElementSize(100);
        assertEquals("measureDataStoreVO.getElementSize()", 100, measureDataStoreVO.getElementSize());
    }
    
    public void testSetFileHolder() throws Throwable {
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        FileHolder fileHolder = new FileHolderImpl();
        measureDataStoreVO.setFileHolder(fileHolder);
        assertSame("measureDataStoreVO.getFileHolder()", fileHolder, measureDataStoreVO.getFileHolder());
    }
    
    public void testSetFilePath() throws Throwable {
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        measureDataStoreVO.setFilePath("testMeasureDataStoreVOFilePath");
        assertEquals("measureDataStoreVO.getFilePath()", "testMeasureDataStoreVOFilePath", measureDataStoreVO.getFilePath());
    }
    
    public void testSetFileStore() throws Throwable {
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        measureDataStoreVO.setFileStore(true);
        assertTrue("measureDataStoreVO.isFileStore()", measureDataStoreVO.isFileStore());
    }
    
    public void testSetLength() throws Throwable {
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        int[] length = new int[0];
        measureDataStoreVO.setLength(length);
        assertSame("measureDataStoreVO.getLength()", length, measureDataStoreVO.getLength());
    }
    
    public void testSetOffset() throws Throwable {
        long[] offset = new long[0];
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        measureDataStoreVO.setOffset(offset);
        assertSame("measureDataStoreVO.getOffset()", offset, measureDataStoreVO.getOffset());
    }
    
    public void testSetTotalSize() throws Throwable {
        MeasureDataStoreVO measureDataStoreVO = new MeasureDataStoreVO();
        measureDataStoreVO.setTotalSize(100);
        assertEquals("measureDataStoreVO.getTotalSize()", 100, measureDataStoreVO.getTotalSize());
    }
}

