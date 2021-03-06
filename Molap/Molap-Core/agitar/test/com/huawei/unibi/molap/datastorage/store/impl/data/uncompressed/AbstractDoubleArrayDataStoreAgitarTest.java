/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:28:56 PM
 * Time to generate: 00:22.025 seconds
 *
 */

package com.huawei.unibi.molap.datastorage.store.impl.data.uncompressed;

import com.agitar.lib.junit.AgitarTestCase;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionModel;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionUtil;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressonHolder;
import com.huawei.unibi.molap.datastorage.store.compression.type.UnCompressNonDecimalByte;
import com.huawei.unibi.molap.datastorage.store.compression.type.UnCompressNoneByte;
import com.huawei.unibi.molap.datastorage.store.compression.type.UnCompressNoneFloat;

public class AbstractDoubleArrayDataStoreAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return AbstractDoubleArrayDataStore.class;
    }
    
    public void testClear() throws Throwable {
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000);
        doubleArrayDataInMemoryStore.clear();
        assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.datastore.length", 1000, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).datastore.length);
    }
    
    public void testGetLength() throws Throwable {
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[0];
        compressionModel.setUnCompressValues(unCompressValues);
        short result = new DoubleArrayDataInMemoryStore(100, 1000, compressionModel).getLength();
        assertEquals("result", (short)0, result);
    }
    
    public void testGetLength1() throws Throwable {
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[2];
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        compressionModel.setUnCompressValues(unCompressValues);
        short result = new DoubleArrayDataFileStore(100, 1000, compressionModel).getLength();
        assertEquals("result", (short)2, result);
    }
    
    public void testGetLength2() throws Throwable {
        short result = new DoubleArrayDataInMemoryStore(100, 1000).getLength();
        assertEquals("result", (short)0, result);
    }
    
    public void testGetWritableMeasureDataArray() throws Throwable {
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[0];
        compressionModel.setUnCompressValues(unCompressValues);
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000, compressionModel);
        byte[][] result = doubleArrayDataInMemoryStore.getWritableMeasureDataArray();
        assertEquals("result.length", 0, result.length);
        assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.values.length", 0, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).values.length);
    }
    
    public void testPut() throws Throwable {
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1);
        double[] value = new double[2];
        doubleArrayDataInMemoryStore.put(0, value);
        assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.datastore.length", 1, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).datastore.length);
    }
    
    public void testPut1() throws Throwable {
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[2];
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        compressionModel.setUnCompressValues(unCompressValues);
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 0, compressionModel);
        double[] value = new double[3];
        doubleArrayDataInMemoryStore.put(100, value);
        assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.datastore.length", 0, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).datastore.length);
    }
    
    public void testPut2() throws Throwable {
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1);
        double[] value = new double[2];
        doubleArrayDataInMemoryStore.put(0, value);
        doubleArrayDataInMemoryStore.put(0, value);
        assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.datastore.length", 1, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).datastore.length);
    }
    
    public void testGetWritableMeasureDataArrayThrowsArrayIndexOutOfBoundsException() throws Throwable {
        double[] doubles = new double[4];
        double[] doubles2 = new double[1];
        int[] ints = new int[3];
        ValueCompressionModel compressionModel = (ValueCompressionModel) callPrivateMethod("com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionUtil", "getValueCompressionModel", new Class[] {double[].class, double[].class, int[].class, int.class}, null, new Object[] {doubles, doubles2, ints, new Integer(1)});
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 0, compressionModel);
        try {
            doubleArrayDataInMemoryStore.getWritableMeasureDataArray();
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "0", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.values.length", 1, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).values.length);
        }
    }
    
    public void testGetWritableMeasureDataArrayThrowsNullPointerException() throws Throwable {
        long[] measuresOffsetsArray = new long[0];
        int[] measuresLengthArray = new int[2];
        double[] doubles = new double[2];
        double[] doubles2 = new double[3];
        int[] ints = new int[1];
        ValueCompressionModel compressionModel = (ValueCompressionModel) callPrivateMethod("com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionUtil", "getValueCompressionModel", new Class[] {double[].class, double[].class, int[].class, int.class}, null, new Object[] {doubles, doubles2, ints, new Integer(1)});
        AbstractDoubleArrayDataStore doubleArrayDataFileStore = new DoubleArrayDataFileStore(100, 1000, compressionModel, measuresOffsetsArray, "testAbstractDoubleArrayDataStoreFileName", measuresLengthArray);
        try {
            doubleArrayDataFileStore.getWritableMeasureDataArray();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataFileStore) doubleArrayDataFileStore.values.length", 1, ((DoubleArrayDataFileStore) doubleArrayDataFileStore).values.length);
        }
    }
    
    public void testGetWritableMeasureDataArrayThrowsNullPointerException1() throws Throwable {
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[1];
        unCompressValues[0] = new UnCompressNonDecimalByte();
        compressionModel.setUnCompressValues(unCompressValues);
        ValueCompressionUtil.DataType[] changedDataType = new ValueCompressionUtil.DataType[2];
        compressionModel.setChangedDataType(changedDataType);
        ValueCompressionUtil.COMPRESSION_TYPE[] compType = new ValueCompressionUtil.COMPRESSION_TYPE[2];
        compressionModel.setCompType(compType);
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000, compressionModel);
        try {
            doubleArrayDataInMemoryStore.getWritableMeasureDataArray();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.values.length", 1, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).values.length);
        }
    }
    
    public void testGetWritableMeasureDataArrayThrowsNullPointerException2() throws Throwable {
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000);
        try {
            doubleArrayDataInMemoryStore.getWritableMeasureDataArray();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertNull("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.values", ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).values);
        }
    }
    
    public void testGetWritableMeasureDataArrayThrowsNullPointerException3() throws Throwable {
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[1];
        ValueCompressionModel compressionModel = ValueCompressionUtil.getValueCompressionModel("testAbstractDoubleArrayDataStoreMeasureMetaDataFileLocation", 100);
        compressionModel.setUnCompressValues(unCompressValues);
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000, compressionModel);
        try {
            doubleArrayDataInMemoryStore.getWritableMeasureDataArray();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.values.length", 1, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).values.length);
        }
    }
    
    public void testGetWritableMeasureDataArrayThrowsNullPointerException4() throws Throwable {
        double[] doubles = new double[4];
        double[] doubles2 = new double[1];
        int[] ints = new int[3];
        ValueCompressionModel compressionModel = (ValueCompressionModel) callPrivateMethod("com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionUtil", "getValueCompressionModel", new Class[] {double[].class, double[].class, int[].class, int.class}, null, new Object[] {doubles, doubles2, ints, new Integer(1)});
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[2];
        unCompressValues[0] = new UnCompressNoneByte();
        compressionModel.setUnCompressValues(unCompressValues);
        AbstractDoubleArrayDataStore doubleArrayDataFileStore = new DoubleArrayDataFileStore(100, 1000, compressionModel);
        try {
            doubleArrayDataFileStore.getWritableMeasureDataArray();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(ValueCompressionUtil.class, ex);
            assertEquals("(DoubleArrayDataFileStore) doubleArrayDataFileStore.values.length", 2, ((DoubleArrayDataFileStore) doubleArrayDataFileStore).values.length);
        }
    }
    
    public void testGetWritableMeasureDataArrayThrowsNullPointerException5() throws Throwable {
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[2];
        unCompressValues[0] = new UnCompressNoneFloat();
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        compressionModel.setUnCompressValues(unCompressValues);
        AbstractDoubleArrayDataStore doubleArrayDataFileStore = new DoubleArrayDataFileStore(100, 1000, compressionModel);
        try {
            doubleArrayDataFileStore.getWritableMeasureDataArray();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataFileStore) doubleArrayDataFileStore.values.length", 2, ((DoubleArrayDataFileStore) doubleArrayDataFileStore).values.length);
        }
    }
    
    public void testPutThrowsArrayIndexOutOfBoundsException() throws Throwable {
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[2];
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        compressionModel.setUnCompressValues(unCompressValues);
        AbstractDoubleArrayDataStore doubleArrayDataFileStore = new DoubleArrayDataFileStore(100, 1, compressionModel);
        double[] value = new double[5];
        doubleArrayDataFileStore.put(0, value);
        double[] value2 = new double[0];
        try {
            doubleArrayDataFileStore.put(100, value2);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "0", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataFileStore) doubleArrayDataFileStore.datastore.length", 1, ((DoubleArrayDataFileStore) doubleArrayDataFileStore).datastore.length);
        }
    }
    
    public void testPutThrowsArrayIndexOutOfBoundsException1() throws Throwable {
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[2];
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        compressionModel.setUnCompressValues(unCompressValues);
        AbstractDoubleArrayDataStore doubleArrayDataFileStore = new DoubleArrayDataFileStore(0, 100, compressionModel);
        double[] value = new double[3];
        try {
            doubleArrayDataFileStore.put(100, value);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "100", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataFileStore) doubleArrayDataFileStore.datastore.length", 100, ((DoubleArrayDataFileStore) doubleArrayDataFileStore).datastore.length);
        }
    }
    
    public void testPutThrowsArrayIndexOutOfBoundsException2() throws Throwable {
        double[] value = new double[3];
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(100, 1000);
        try {
            doubleArrayDataInMemoryStore.put(0, value);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "3", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.datastore.length", 1000, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).datastore.length);
        }
    }
    
    public void testPutThrowsNegativeArraySizeException() throws Throwable {
        AbstractDoubleArrayDataStore doubleArrayDataInMemoryStore = new DoubleArrayDataInMemoryStore(-1, 100);
        double[] value = new double[3];
        try {
            doubleArrayDataInMemoryStore.put(100, value);
            fail("Expected NegativeArraySizeException to be thrown");
        } catch (NegativeArraySizeException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertEquals("(DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore.datastore.length", 100, ((DoubleArrayDataInMemoryStore) doubleArrayDataInMemoryStore).datastore.length);
        }
    }
    
    public void testPutThrowsNullPointerException() throws Throwable {
        ValueCompressonHolder.UnCompressValue[] unCompressValues = new ValueCompressonHolder.UnCompressValue[2];
        ValueCompressionModel compressionModel = new ValueCompressionModel();
        compressionModel.setUnCompressValues(unCompressValues);
        double[] value = new double[3];
        long[] measuresOffsetsArray = new long[3];
        int[] measuresLengthArray = new int[3];
        AbstractDoubleArrayDataStore doubleArrayDataFileStore = new DoubleArrayDataFileStore(100, 1000, compressionModel, measuresOffsetsArray, "testAbstractDoubleArrayDataStoreFileName", measuresLengthArray);
        try {
            doubleArrayDataFileStore.put(100, value);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractDoubleArrayDataStore.class, ex);
            assertNull("(DoubleArrayDataFileStore) doubleArrayDataFileStore.datastore", ((DoubleArrayDataFileStore) doubleArrayDataFileStore).datastore);
        }
    }
}

