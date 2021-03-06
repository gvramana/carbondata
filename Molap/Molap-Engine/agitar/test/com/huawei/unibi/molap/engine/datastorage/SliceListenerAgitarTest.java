/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 5:11:10 PM
 * Time to generate: 01:26.647 seconds
 *
 */

package com.huawei.unibi.molap.engine.datastorage;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import com.huawei.iweb.platform.logging.LogService;
import com.huawei.unibi.molap.engine.util.MolapEngineLogEvent;
import java.util.HashSet;
import java.util.Set;

public class SliceListenerAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return SliceListener.class;
    }
    
    public void testConstructor() throws Throwable {
        Mockingbird.enterRecordingMode();
        Mockingbird.replaceObjectForRecording(HashSet.class, "<init>()", Mockingbird.getProxyObject(HashSet.class));
        Mockingbird.enterTestMode();
        SliceListener sliceListener = new SliceListener(null);
        assertNull("sliceListener.slice", getPrivateField(sliceListener, "slice"));
        assertNotNull("sliceListener.queries", getPrivateField(sliceListener, "queries"));
    }
    
    public void testFireQueryFinishWithAggressiveMocks() throws Throwable {
        storeStaticField(MolapEngineLogEvent.class, "UNIBI_MOLAPENGINE_MSG");
        storeStaticField(SliceListener.class, "LOGGER");
        SliceListener sliceListener = (SliceListener) Mockingbird.getProxyObject(SliceListener.class, true);
        InMemoryCube inMemoryCube = (InMemoryCube) Mockingbird.getProxyObject(InMemoryCube.class);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        LogService logService = (LogService) Mockingbird.getProxyObject(LogService.class);
        setPrivateField(sliceListener, "slice", inMemoryCube);
        setPrivateField(sliceListener, "queries", set);
        setPrivateField(SliceListener.class, "LOGGER", logService);
        setPrivateField(MolapEngineLogEvent.class, "UNIBI_MOLAPENGINE_MSG", null);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, logService, "info", "(com.huawei.iweb.platform.logging.LogEvent,java.lang.Object[])void", null, 1);
        Mockingbird.setReturnValue(inMemoryCube.isActive(), false);
        Mockingbird.setReturnValue(set.remove(null), false);
        Mockingbird.setReturnValue(set.size(), 1);
        Mockingbird.enterTestMode(SliceListener.class);
        sliceListener.fireQueryFinish(null);
        assertNull("sliceListener.getCubeUniqueName()", sliceListener.getCubeUniqueName());
        assertNotNull("sliceListener.queries", getPrivateField(sliceListener, "queries"));
        assertNotNull("sliceListenerSliceListener.LOGGER", getPrivateField(SliceListener.class, "LOGGER"));
    }
    
    public void testFireQueryFinishWithAggressiveMocks1() throws Throwable {
        storeStaticField(MolapEngineLogEvent.class, "UNIBI_MOLAPENGINE_MSG");
        storeStaticField(SliceListener.class, "LOGGER");
        SliceListener sliceListener = (SliceListener) Mockingbird.getProxyObject(SliceListener.class, true);
        InMemoryCube inMemoryCube = (InMemoryCube) Mockingbird.getProxyObject(InMemoryCube.class);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        LogService logService = (LogService) Mockingbird.getProxyObject(LogService.class);
        setPrivateField(sliceListener, "slice", inMemoryCube);
        setPrivateField(sliceListener, "queries", set);
        setPrivateField(SliceListener.class, "LOGGER", logService);
        setPrivateField(MolapEngineLogEvent.class, "UNIBI_MOLAPENGINE_MSG", null);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, logService, "info", "(com.huawei.iweb.platform.logging.LogEvent,java.lang.Object[])void", null, 1);
        Mockingbird.setReturnValue(inMemoryCube.isActive(), true);
        Mockingbird.setReturnValue(set.size(), 1);
        Mockingbird.enterTestMode(SliceListener.class);
        sliceListener.fireQueryFinish(null);
        assertNull("sliceListener.getCubeUniqueName()", sliceListener.getCubeUniqueName());
        assertNotNull("sliceListener.queries", getPrivateField(sliceListener, "queries"));
        assertNotNull("sliceListenerSliceListener.LOGGER", getPrivateField(SliceListener.class, "LOGGER"));
    }
    
    public void testFireQueryFinishWithAggressiveMocks2() throws Throwable {
        storeStaticField(InMemoryCubeStore.class, "instance");
        storeStaticField(MolapEngineLogEvent.class, "UNIBI_MOLAPENGINE_MSG");
        storeStaticField(SliceListener.class, "LOGGER");
        SliceListener sliceListener = (SliceListener) Mockingbird.getProxyObject(SliceListener.class, true);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        InMemoryCube inMemoryCube = (InMemoryCube) Mockingbird.getProxyObject(InMemoryCube.class);
        LogService logService = (LogService) Mockingbird.getProxyObject(LogService.class);
        InMemoryCubeStore inMemoryCubeStore = (InMemoryCubeStore) Mockingbird.getProxyObject(InMemoryCubeStore.class);
        setPrivateField(sliceListener, "queries", set);
        setPrivateField(sliceListener, "slice", inMemoryCube);
        setPrivateField(SliceListener.class, "LOGGER", logService);
        setPrivateField(MolapEngineLogEvent.class, "UNIBI_MOLAPENGINE_MSG", null);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        setPrivateField(inMemoryCube, "id", new Long(0L));
        setPrivateField(inMemoryCube, "cubeUniqueName", "");
        setPrivateField(InMemoryCubeStore.class, "instance", inMemoryCubeStore);
        Mockingbird.setReturnValue(false, logService, "info", "(com.huawei.iweb.platform.logging.LogEvent,java.lang.Object[])void", null, 1);
        Mockingbird.setReturnValue(inMemoryCube.isActive(), false);
        Mockingbird.setReturnValue(set.remove(null), false);
        Mockingbird.setReturnValue(set.size(), 0);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, logService, "error", "(com.huawei.iweb.platform.logging.LogEvent,java.lang.Object[])void", null, 1);
        Mockingbird.setReturnValue(false, inMemoryCubeStore, "unRegisterSlice", "(java.lang.String,com.huawei.unibi.molap.engine.datastorage.InMemoryCube)void", null, 1);
        inMemoryCube.clean();
        Mockingbird.setNormalReturnForVoid();
        Mockingbird.setReturnValue(false, inMemoryCubeStore, "afterClearQueriesAndCubes", "(java.lang.String)void", null, 1);
        Mockingbird.enterTestMode(SliceListener.class);
        sliceListener.fireQueryFinish(null);
        assertEquals("sliceListener.getCubeUniqueName()", "", sliceListener.getCubeUniqueName());
        assertNotNull("sliceListener.queries", getPrivateField(sliceListener, "queries"));
        assertNotNull("sliceListenerSliceListener.LOGGER", getPrivateField(SliceListener.class, "LOGGER"));
    }
    
    public void testGetCubeUniqueNameWithAggressiveMocks() throws Throwable {
        SliceListener sliceListener = (SliceListener) Mockingbird.getProxyObject(SliceListener.class, true);
        InMemoryCube inMemoryCube = (InMemoryCube) Mockingbird.getProxyObject(InMemoryCube.class);
        setPrivateField(sliceListener, "slice", inMemoryCube);
        setPrivateField(inMemoryCube, "cubeUniqueName", "");
        Mockingbird.enterTestMode(SliceListener.class);
        String result = sliceListener.getCubeUniqueName();
        assertEquals("result", "", result);
    }
    
    public void testRegisterQueryWithAggressiveMocks() throws Throwable {
        SliceListener sliceListener = (SliceListener) Mockingbird.getProxyObject(SliceListener.class, true);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        setPrivateField(sliceListener, "queries", set);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(set.add((Object) null), false);
        Mockingbird.enterTestMode(SliceListener.class);
        sliceListener.registerQuery(null);
        assertNotNull("sliceListener.queries", getPrivateField(sliceListener, "queries"));
    }
    
    public void testStillListeningWithAggressiveMocks() throws Throwable {
        SliceListener sliceListener = (SliceListener) Mockingbird.getProxyObject(SliceListener.class, true);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        setPrivateField(sliceListener, "queries", set);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(set.size(), 1);
        Mockingbird.enterTestMode(SliceListener.class);
        boolean result = sliceListener.stillListening();
        assertTrue("result", result);
        assertNotNull("sliceListener.queries", getPrivateField(sliceListener, "queries"));
    }
    
    public void testStillListeningWithAggressiveMocks1() throws Throwable {
        SliceListener sliceListener = (SliceListener) Mockingbird.getProxyObject(SliceListener.class, true);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        setPrivateField(sliceListener, "queries", set);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(set.size(), 0);
        Mockingbird.enterTestMode(SliceListener.class);
        boolean result = sliceListener.stillListening();
        assertFalse("result", result);
        assertNotNull("sliceListener.queries", getPrivateField(sliceListener, "queries"));
    }
}

