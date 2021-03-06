/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:29:52 PM
 * Time to generate: 00:22.213 seconds
 *
 */

package com.huawei.unibi.molap.datastorage.store.impl.data.uncompressed;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import com.huawei.unibi.molap.datastorage.store.FileHolder;
import com.huawei.unibi.molap.datastorage.store.MeasureDataWrapper;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionModel;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionUtil;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressonHolder;
import com.huawei.unibi.molap.datastorage.store.compression.type.UnCompressNoneDefault;
import com.huawei.unibi.molap.datastorage.store.impl.CompressedDataMeasureDataWrapper;
import com.huawei.unibi.molap.datastorage.store.impl.FileHolderImpl;
import java.nio.ByteBuffer;

public class DoubleArrayDataInMemoryStoreAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return DoubleArrayDataInMemoryStore.class;
    }
    
    public void testConstructor() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        DoubleArrayDataInMemoryStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000, compressionModel);
        assertSame("doubleArrayDataInMemoryStore.compressionModel", compressionModel, doubleArrayDataInMemoryStore.compressionModel);
        assertEquals("doubleArrayDataInMemoryStore.datastore.length", 1000, doubleArrayDataInMemoryStore.datastore.length);
        assertEquals("doubleArrayDataInMemoryStore.values.length", 100, doubleArrayDataInMemoryStore.values.length);
        assertEquals("doubleArrayDataInMemoryStore.size", 100, doubleArrayDataInMemoryStore.size);
        assertEquals("doubleArrayDataInMemoryStore.elementSize", 1000, ((Number) getPrivateField(doubleArrayDataInMemoryStore, "elementSize")).intValue());
    }
    
    public void testConstructor1() throws Throwable {
        DoubleArrayDataInMemoryStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000);
        assertEquals("doubleArrayDataInMemoryStore.datastore.length", 1000, doubleArrayDataInMemoryStore.datastore.length);
        assertEquals("doubleArrayDataInMemoryStore.size", 100, doubleArrayDataInMemoryStore.size);
        assertEquals("doubleArrayDataInMemoryStore.elementSize", 1000, ((Number) getPrivateField(doubleArrayDataInMemoryStore, "elementSize")).intValue());
    }
    
    public void testConstructor2() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        long[] measuresOffsetsArray = new long[1];
        int[] measuresLengthArray = new int[0];
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[4];
        compressionModel.setUnCompressValues(unCompressValues);
        DoubleArrayDataInMemoryStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000, compressionModel, measuresOffsetsArray, measuresLengthArray, "testDoubleArrayDataInMemoryStoreFileName", new FileHolderImpl(100));
        assertSame("doubleArrayDataInMemoryStore.compressionModel", compressionModel, doubleArrayDataInMemoryStore.compressionModel);
        assertEquals("doubleArrayDataInMemoryStore.datastore.length", 1000, doubleArrayDataInMemoryStore.datastore.length);
        assertEquals("doubleArrayDataInMemoryStore.values.length", 4, doubleArrayDataInMemoryStore.values.length);
        assertEquals("doubleArrayDataInMemoryStore.size", 100, doubleArrayDataInMemoryStore.size);
        assertEquals("doubleArrayDataInMemoryStore.elementSize", 1000, ((Number) getPrivateField(doubleArrayDataInMemoryStore, "elementSize")).intValue());
    }
    
    public void testConstructor3() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        long[] measuresOffsetsArray = new long[4];
        int[] measuresLengthArray = new int[2];
        DoubleArrayDataInMemoryStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000, compressionModel, measuresOffsetsArray, measuresLengthArray, "testDoubleArrayDataInMemoryStoreFileName", new FileHolderImpl());
        assertSame("doubleArrayDataInMemoryStore.compressionModel", compressionModel, doubleArrayDataInMemoryStore.compressionModel);
        assertEquals("doubleArrayDataInMemoryStore.datastore.length", 1000, doubleArrayDataInMemoryStore.datastore.length);
        assertEquals("doubleArrayDataInMemoryStore.values.length", 100, doubleArrayDataInMemoryStore.values.length);
        assertEquals("doubleArrayDataInMemoryStore.size", 100, doubleArrayDataInMemoryStore.size);
        assertEquals("doubleArrayDataInMemoryStore.elementSize", 1000, ((Number) getPrivateField(doubleArrayDataInMemoryStore, "elementSize")).intValue());
    }
    
    public void testConstructor4() throws Throwable {
        ValueCompressionModel valueCompressionModel = new ValueCompressionModel();
        long[] longs = new long[0];
        int[] ints = new int[0];
        Mockingbird.ignoreConstructorExceptions(AbstractDoubleArrayDataStore.class);
        valueCompressionModel.setUnCompressValues((ValueCompressonHolder.UnCompressValue[]) null);
        DoubleArrayDataInMemoryStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(0, 0, valueCompressionModel, longs, ints, "", null);
        assertNull("doubleArrayDataInMemoryStore.compressionModel.getMaxValue()", doubleArrayDataInMemoryStore.compressionModel.getMaxValue());
        assertEquals("doubleArrayDataInMemoryStore.datastore.length", 0, doubleArrayDataInMemoryStore.datastore.length);
        assertNull("doubleArrayDataInMemoryStore.values", doubleArrayDataInMemoryStore.values);
        assertEquals("doubleArrayDataInMemoryStore.size", 0, doubleArrayDataInMemoryStore.size);
        assertEquals("doubleArrayDataInMemoryStore.elementSize", 0, ((Number) getPrivateField(doubleArrayDataInMemoryStore, "elementSize")).intValue());
    }
    
    public void testGetBackData() throws Throwable {
        int[] cols = new int[0];
        MeasureDataWrapper result = new DoubleArrayDataInMemoryStore(100, 1000).getBackData(cols, new FileHolderImpl());
        assertNull("result", result);
    }
    
    public void testGetBackData1() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        DoubleArrayDataInMemoryStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000, compressionModel);
        int[] cols = new int[0];
        CompressedDataMeasureDataWrapper result = (CompressedDataMeasureDataWrapper) doubleArrayDataInMemoryStore.getBackData(cols, new FileHolderImpl(100));
        assertNotNull("result", result);
    }
    
    public void testConstructorThrowsArrayIndexOutOfBoundsException() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 0);
        long[] measuresOffsetsArray = new long[2];
        FileHolder fileHolder = new FileHolderImpl();
        int[] measuresLengthArray = new int[3];
        try {
            new DoubleArrayDataInMemoryStore(100, 1000, compressionModel, measuresOffsetsArray, measuresLengthArray, "testDoubleArrayDataInMemoryStoreFileName", fileHolder);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "0", ex.getMessage());
            assertThrownBy(DoubleArrayDataInMemoryStore.class, ex);
        }
    }
    
    public void testConstructorThrowsArrayIndexOutOfBoundsException1() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        long[] measuresOffsetsArray = new long[1];
        int[] measuresLengthArray = new int[4];
        FileHolder fileHolder = new FileHolderImpl();
        try {
            new DoubleArrayDataInMemoryStore(100, 1000, compressionModel, measuresOffsetsArray, measuresLengthArray, "testDoubleArrayDataInMemoryStoreFileName", fileHolder);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "1", ex.getMessage());
            assertThrownBy(DoubleArrayDataInMemoryStore.class, ex);
        }
    }
    
    public void testConstructorThrowsArrayIndexOutOfBoundsException2() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        FileHolder fileHolder = new FileHolderImpl(100);
        long[] measuresOffsetsArray = new long[0];
        int[] measuresLengthArray = new int[2];
        try {
            new DoubleArrayDataInMemoryStore(100, 1000, compressionModel, measuresOffsetsArray, measuresLengthArray, "testDoubleArrayDataInMemoryStoreFileName", fileHolder);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "0", ex.getMessage());
            assertThrownBy(DoubleArrayDataInMemoryStore.class, ex);
        }
    }
    
    public void testConstructorThrowsIllegalArgumentException() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        int[] measuresLengthArray = new int[2];
        measuresLengthArray[1] = -8;
        FileHolder fileHolder = new FileHolderImpl(100);
        long[] measuresOffsetsArray = new long[2];
        try {
            new DoubleArrayDataInMemoryStore(100, 1000, compressionModel, measuresOffsetsArray, measuresLengthArray, "testDoubleArrayDataInMemoryStoreFileName", fileHolder);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(ByteBuffer.class, ex);
        }
    }
    
    public void testConstructorThrowsNegativeArraySizeException() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        try {
            new DoubleArrayDataInMemoryStore(100, -1, compressionModel);
            fail("Expected NegativeArraySizeException to be thrown");
        } catch (NegativeArraySizeException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
        }
    }
    
    public void testConstructorThrowsNegativeArraySizeException1() throws Throwable {
        try {
            new DoubleArrayDataInMemoryStore(100, -1);
            fail("Expected NegativeArraySizeException to be thrown");
        } catch (NegativeArraySizeException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
        }
    }
    
    public void testConstructorThrowsNegativeArraySizeException2() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        FileHolder fileHolder = new FileHolderImpl(100);
        long[] measuresOffsetsArray = new long[1];
        int[] measuresLengthArray = new int[0];
        try {
            new DoubleArrayDataInMemoryStore(100, -1, compressionModel, measuresOffsetsArray, measuresLengthArray, "testDoubleArrayDataInMemoryStoreFileName", fileHolder);
            fail("Expected NegativeArraySizeException to be thrown");
        } catch (NegativeArraySizeException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
        }
    }
    
    public void testConstructorThrowsNullPointerException() throws Throwable {
        try {
            new DoubleArrayDataInMemoryStore(100, 1000, new ValueCompressionModel());
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
        }
    }
    
    public void testConstructorThrowsNullPointerException1() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        long[] measuresOffsetsArray = new long[1];
        int[] measuresLengthArray = new int[1];
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[4];
        unCompressValues[0] = new UnCompressNoneDefault();
        compressionModel.setUnCompressValues(unCompressValues);
        try {
            new DoubleArrayDataInMemoryStore(100, 1000, compressionModel, measuresOffsetsArray, measuresLengthArray, "testDoubleArrayDataInMemoryStoreFileName", null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(DoubleArrayDataInMemoryStore.class, ex);
        }
    }
    
    public void testConstructorThrowsNullPointerException2() throws Throwable {
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testDoubleArrayDataInMemoryStoreMeasureMetaDataFileLocation", 100);
        FileHolder fileHolder = new FileHolderImpl(100);
        long[] measuresOffsetsArray = new long[3];
        try {
            new DoubleArrayDataInMemoryStore(100, 1000, compressionModel, measuresOffsetsArray, null, "testDoubleArrayDataInMemoryStoreFileName", fileHolder);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(DoubleArrayDataInMemoryStore.class, ex);
        }
    }
}

