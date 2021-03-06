/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:33:49 PM
 * Time to generate: 00:16.648 seconds
 *
 */

package com.huawei.unibi.molap.datastorage.store.compression.type;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import com.huawei.iweb.platform.logging.LogService;
import com.huawei.unibi.molap.datastorage.store.compression.Compressor;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionUtil;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressonHolder;
import com.huawei.unibi.molap.util.MolapCoreLogEvent;
import java.nio.ByteBuffer;

public class UnCompressMaxMinShortAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return UnCompressMaxMinShort.class;
    }
    
    public void testConstructor() throws Throwable {
        new UnCompressMaxMinShort();
        assertTrue("Test call resulted in expected outcome", true);
    }
    
    public void testCompressWithAggressiveMocks() throws Throwable {
        storeStaticField(UnCompressMaxMinShort.class, "shortCompressor");
        UnCompressMaxMinShort unCompressMaxMinShort = (UnCompressMaxMinShort) Mockingbird.getProxyObject(UnCompressMaxMinShort.class, true);
        short[] shorts = new short[0];
        Mockingbird.enterRecordingMode();
        Mockingbird.replaceObjectForRecording(UnCompressMaxMinByte.class, "<init>()", new UnCompressMaxMinByte());
        Compressor compressor = (Compressor) Mockingbird.getProxyObject(Compressor.class);
        byte[] bytes = new byte[0];
        Mockingbird.enterNormalMode();
        unCompressMaxMinShort.setValue(shorts);
        setPrivateField(UnCompressMaxMinShort.class, "shortCompressor", compressor);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(compressor.compress(shorts), bytes);
        Mockingbird.enterTestMode(UnCompressMaxMinShort.class);
        ValueCompressonHolder.UnCompressValue result = unCompressMaxMinShort.compress();
        assertEquals("result.getBackArrayData().length", 0, result.getBackArrayData().length);
        assertNotNull("unCompressMaxMinShortUnCompressMaxMinShort.shortCompressor", getPrivateField(UnCompressMaxMinShort.class, "shortCompressor"));
    }
    
    public void testGetBackArrayData() throws Throwable {
        short[] value = new short[0];
        UnCompressMaxMinShort unCompressMaxMinShort = new UnCompressMaxMinShort();
        unCompressMaxMinShort.setValue(value);
        byte[] result = unCompressMaxMinShort.getBackArrayData();
        assertEquals("result.length", 0, result.length);
    }
    
    public void testGetBackArrayData1() throws Throwable {
        short[] value = new short[3];
        UnCompressMaxMinShort unCompressMaxMinShort = new UnCompressMaxMinShort();
        unCompressMaxMinShort.setValue(value);
        byte[] result = unCompressMaxMinShort.getBackArrayData();
        assertEquals("result.length", 6, result.length);
        assertEquals("result[0]", (byte)0, result[0]);
    }
    
    public void testGetCompressorObject() throws Throwable {
        UnCompressMaxMinByte result = (UnCompressMaxMinByte) new UnCompressMaxMinShort().getCompressorObject();
        assertNull("result.getBackArrayData()", result.getBackArrayData());
    }
    
    public void testGetNew() throws Throwable {
        UnCompressMaxMinShort result = (UnCompressMaxMinShort) new UnCompressMaxMinShort().getNew();
        assertNotNull("result", result);
    }
    
    public void testGetNewWithAggressiveMocks() throws Throwable {
        storeStaticField(MolapCoreLogEvent.class, "UNIBI_MOLAPCORE_MSG");
        storeStaticField(UnCompressMaxMinShort.class, "LOGGER");
        UnCompressMaxMinShort unCompressMaxMinShort = (UnCompressMaxMinShort) Mockingbird.getProxyObject(UnCompressMaxMinShort.class, true);
        CloneNotSupportedException cloneNotSupportedException = (CloneNotSupportedException) Mockingbird.getProxyObject(CloneNotSupportedException.class);
        LogService logService = (LogService) Mockingbird.getProxyObject(LogService.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setException(false, unCompressMaxMinShort, "clone", "()java.lang.Object", new Object[] {}, cloneNotSupportedException, 1);
        setPrivateField(UnCompressMaxMinShort.class, "LOGGER", logService);
        setPrivateField(MolapCoreLogEvent.class, "UNIBI_MOLAPCORE_MSG", null);
        Mockingbird.setReturnValue(false, cloneNotSupportedException, "getMessage", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, logService, "error", "(com.huawei.iweb.platform.logging.LogEvent,java.lang.Throwable,java.lang.Object[])void", null, 1);
        Mockingbird.enterTestMode(UnCompressMaxMinShort.class);
        ValueCompressonHolder.UnCompressValue result = unCompressMaxMinShort.getNew();
        assertNull("result", result);
    }
    
    public void testGetValue() throws Throwable {
        UnCompressMaxMinShort new2 = (UnCompressMaxMinShort) new UnCompressMaxMinShort().getNew();
        short[] value = new short[2];
        value[0] = (short)-30849;
        new2.setValue(value);
        double result = new2.getValue(0, 100, 100.0);
        assertEquals("result", 30949.0, result, 1.0E-6);
    }
    
    public void testGetValue1() throws Throwable {
        UnCompressMaxMinShort unCompressMaxMinShort = new UnCompressMaxMinShort();
        short[] value = new short[2];
        unCompressMaxMinShort.setValue(value);
        double result = unCompressMaxMinShort.getValue(0, 100, 0.0);
        assertEquals("result", 0.0, result, 1.0E-6);
    }
    
    public void testSetValue() throws Throwable {
        short[] value = new short[0];
        UnCompressMaxMinShort unCompressMaxMinShort = new UnCompressMaxMinShort();
        unCompressMaxMinShort.setValue(value);
        assertEquals("unCompressMaxMinShort.getBackArrayData().length", 0, unCompressMaxMinShort.getBackArrayData().length);
    }
    
    public void testSetValueInBytes() throws Throwable {
        UnCompressMaxMinShort new2 = (UnCompressMaxMinShort) new UnCompressMaxMinShort().getNew();
        byte[] value = new byte[0];
        new2.setValueInBytes(value);
        assertEquals("new2.getBackArrayData().length", 0, new2.getBackArrayData().length);
    }
    
    public void testUncompress() throws Throwable {
        ValueCompressonHolder.UnCompressValue result = new UnCompressMaxMinShort().uncompress(ValueCompressionUtil.DataType.DATA_LONG);
        assertNull("result", result);
    }
    
    public void testGetBackArrayDataThrowsNullPointerException() throws Throwable {
        UnCompressMaxMinShort new2 = (UnCompressMaxMinShort) new UnCompressMaxMinShort().getNew();
        new2.setValue((short[]) null);
        try {
            new2.getBackArrayData();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(ValueCompressionUtil.class, ex);
        }
    }
    
    public void testGetValueThrowsArrayIndexOutOfBoundsException() throws Throwable {
        short[] value = new short[1];
        UnCompressMaxMinShort unCompressMaxMinShort = new UnCompressMaxMinShort();
        unCompressMaxMinShort.setValue(value);
        try {
            unCompressMaxMinShort.getValue(100, 1000, 100.0);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "100", ex.getMessage());
            assertThrownBy(UnCompressMaxMinShort.class, ex);
        }
    }
    
    public void testGetValueThrowsNullPointerException() throws Throwable {
        try {
            new UnCompressMaxMinShort().getValue(100, 1000, 100.0);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(UnCompressMaxMinShort.class, ex);
        }
    }
    
    public void testSetValueInBytesThrowsNullPointerException() throws Throwable {
        UnCompressMaxMinShort unCompressMaxMinShort = new UnCompressMaxMinShort();
        try {
            unCompressMaxMinShort.setValueInBytes((byte[]) null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(ByteBuffer.class, ex);
            assertNull("unCompressMaxMinShort.value", getPrivateField(unCompressMaxMinShort, "value"));
        }
    }
}

