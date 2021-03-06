/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:35:25 PM
 * Time to generate: 00:17.352 seconds
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
import org.xerial.snappy.Snappy;

public class UnCompressNoneByteAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return UnCompressNoneByte.class;
    }
    
    public void testConstructor() throws Throwable {
        UnCompressNoneByte unCompressNoneByte = new UnCompressNoneByte();
        assertNull("unCompressNoneByte.getBackArrayData()", unCompressNoneByte.getBackArrayData());
    }
    
    public void testCompressWithAggressiveMocks() throws Throwable {
        storeStaticField(UnCompressNoneByte.class, "byteCompressor");
        UnCompressNoneByte unCompressNoneByte = (UnCompressNoneByte) Mockingbird.getProxyObject(UnCompressNoneByte.class, true);
        byte[] bytes = new byte[0];
        Mockingbird.enterRecordingMode();
        Mockingbird.replaceObjectForRecording(UnCompressNoneByte.class, "<init>()", new UnCompressNoneByte());
        Compressor compressor = (Compressor) Mockingbird.getProxyObject(Compressor.class);
        byte[] bytes2 = new byte[0];
        Mockingbird.enterNormalMode();
        unCompressNoneByte.setValue(bytes);
        setPrivateField(UnCompressNoneByte.class, "byteCompressor", compressor);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(compressor.compress(bytes), bytes2);
        Mockingbird.enterTestMode(UnCompressNoneByte.class);
        ValueCompressonHolder.UnCompressValue result = unCompressNoneByte.compress();
        assertEquals("result.getBackArrayData().length", 0, result.getBackArrayData().length);
        assertNotNull("unCompressNoneByteUnCompressNoneByte.byteCompressor", getPrivateField(UnCompressNoneByte.class, "byteCompressor"));
    }
    
    public void testGetBackArrayData() throws Throwable {
        byte[] value = new byte[2];
        UnCompressNoneByte unCompressNoneByte = new UnCompressNoneByte();
        unCompressNoneByte.setValueInBytes(value);
        byte[] result = unCompressNoneByte.getBackArrayData();
        assertSame("result", value, result);
        assertEquals("value[0]", (byte)0, value[0]);
    }
    
    public void testGetBackArrayData1() throws Throwable {
        byte[] value = new byte[0];
        UnCompressNoneByte unCompressNoneByte = new UnCompressNoneByte();
        unCompressNoneByte.setValueInBytes(value);
        byte[] result = unCompressNoneByte.getBackArrayData();
        assertSame("result", value, result);
    }
    
    public void testGetCompressorObject() throws Throwable {
        UnCompressNoneByte result = (UnCompressNoneByte) new UnCompressNoneByte().getCompressorObject();
        assertNull("result.getBackArrayData()", result.getBackArrayData());
    }
    
    public void testGetNew() throws Throwable {
        UnCompressNoneByte result = (UnCompressNoneByte) new UnCompressNoneByte().getNew();
        assertNull("result.getBackArrayData()", result.getBackArrayData());
    }
    
    public void testGetNewWithAggressiveMocks() throws Throwable {
        storeStaticField(UnCompressNoneByte.class, "LOGGER");
        storeStaticField(MolapCoreLogEvent.class, "UNIBI_MOLAPCORE_MSG");
        UnCompressNoneByte unCompressNoneByte = (UnCompressNoneByte) Mockingbird.getProxyObject(UnCompressNoneByte.class, true);
        CloneNotSupportedException cloneNotSupportedException = (CloneNotSupportedException) Mockingbird.getProxyObject(CloneNotSupportedException.class);
        LogService logService = (LogService) Mockingbird.getProxyObject(LogService.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setException(false, unCompressNoneByte, "clone", "()java.lang.Object", new Object[] {}, cloneNotSupportedException, 1);
        setPrivateField(UnCompressNoneByte.class, "LOGGER", logService);
        setPrivateField(MolapCoreLogEvent.class, "UNIBI_MOLAPCORE_MSG", null);
        Mockingbird.setReturnValue(false, cloneNotSupportedException, "getMessage", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, logService, "error", "(com.huawei.iweb.platform.logging.LogEvent,java.lang.Throwable,java.lang.Object[])void", null, 1);
        Mockingbird.enterTestMode(UnCompressNoneByte.class);
        ValueCompressonHolder.UnCompressValue result = unCompressNoneByte.getNew();
        assertNull("result", result);
    }
    
    public void testGetValue() throws Throwable {
        UnCompressNoneByte unCompressNoneByte = new UnCompressNoneByte();
        byte[] value = new byte[3];
        value[0] = (byte)-74;
        unCompressNoneByte.setValueInBytes(value);
        double result = unCompressNoneByte.getValue(0, 100, 100.0);
        assertEquals("result", -74.0, result, 1.0E-6);
    }
    
    public void testGetValue1() throws Throwable {
        UnCompressNoneByte unCompressNoneByte = new UnCompressNoneByte();
        byte[] value = new byte[3];
        unCompressNoneByte.setValueInBytes(value);
        double result = unCompressNoneByte.getValue(0, 100, 100.0);
        assertEquals("result", 0.0, result, 1.0E-6);
    }
    
    public void testSetValue() throws Throwable {
        UnCompressNoneByte unCompressNoneByte = new UnCompressNoneByte();
        byte[] value = new byte[0];
        unCompressNoneByte.setValue(value);
        assertSame("unCompressNoneByte.getBackArrayData()", value, unCompressNoneByte.getBackArrayData());
    }
    
    public void testSetValueInBytes() throws Throwable {
        byte[] value = new byte[2];
        UnCompressNoneByte unCompressNoneByte = new UnCompressNoneByte();
        unCompressNoneByte.setValueInBytes(value);
        assertSame("unCompressNoneByte.getBackArrayData()", value, unCompressNoneByte.getBackArrayData());
    }
    
    public void testUncompressWithAggressiveMocks() throws Throwable {
        UnCompressNoneByte unCompressNoneByte = (UnCompressNoneByte) Mockingbird.getProxyObject(UnCompressNoneByte.class, true);
        byte[] bytes = new byte[0];
        unCompressNoneByte.setValue(bytes);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(ValueCompressionUtil.unCompressNone(null, null), null);
        ValueCompressonHolder.unCompress(null, null, bytes);
        Mockingbird.setNormalReturnForVoid();
        Mockingbird.enterTestMode(UnCompressNoneByte.class);
        ValueCompressonHolder.UnCompressValue result = unCompressNoneByte.uncompress(null);
        assertNull("result", result);
    }
    
    public void testGetValueThrowsArrayIndexOutOfBoundsException() throws Throwable {
        byte[] value = new byte[1];
        UnCompressNoneByte unCompressNoneByte = new UnCompressNoneByte();
        unCompressNoneByte.setValueInBytes(value);
        try {
            unCompressNoneByte.getValue(100, 1000, 100.0);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "100", ex.getMessage());
            assertThrownBy(UnCompressNoneByte.class, ex);
        }
    }
    
    public void testGetValueThrowsNullPointerException() throws Throwable {
        try {
            new UnCompressNoneByte().getValue(100, 1000, 100.0);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(UnCompressNoneByte.class, ex);
        }
    }
    
    public void testUncompressThrowsNullPointerException() throws Throwable {
        try {
            new UnCompressNoneByte().uncompress(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(ValueCompressionUtil.class, ex);
        }
    }
    
    public void testUncompressThrowsNullPointerException1() throws Throwable {
        try {
            new UnCompressNoneByte().uncompress(ValueCompressionUtil.DataType.DATA_BYTE);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Snappy.class, ex);
        }
    }
}

