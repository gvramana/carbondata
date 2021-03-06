/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 5:04:01 PM
 * Time to generate: 00:14.444 seconds
 *
 */

package com.huawei.unibi.molap.engine.datastorage.tree;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MolapExecutionServiceAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return MolapExecutionService.class;
    }
    
    public void testConstructor() throws Throwable {
        new MolapExecutionService();
        assertTrue("Test call resulted in expected outcome", true);
    }
    
    public void testGetExecutionService() throws Throwable {
        ThreadPoolExecutor result = (ThreadPoolExecutor) MolapExecutionService.getExecutionService();
        assertEquals("result.getCorePoolSize()", 7, result.getCorePoolSize());
    }
    
    public void testGetExecutionService1() throws Throwable {
        storeStaticField(MolapExecutionService.class, "execService");
        Runtime runtime = (Runtime) Mockingbird.getProxyObject(Runtime.class);
        setPrivateField(MolapExecutionService.class, "execService", null);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(true, Runtime.class, "getRuntime", "()java.lang.Runtime", new Object[] {}, runtime, 1);
        Mockingbird.setReturnValue(false, runtime, "availableProcessors", "()int", new Object[] {}, new Integer(-1), 1);
        Mockingbird.setReturnValue(true, Executors.class, "newFixedThreadPool", "(int)java.util.concurrent.ExecutorService", new Object[] {new Integer(-1)}, null, 1);
        Mockingbird.enterTestMode(MolapExecutionService.class);
        ExecutorService result = MolapExecutionService.getExecutionService();
        assertNull("result", result);
        assertNotNull("MolapExecutionService.getExecutionService()", MolapExecutionService.getExecutionService());
    }
    
    public void testGetExecutionService2() throws Throwable {
        storeStaticField(MolapExecutionService.class, "execService");
        Runtime runtime = (Runtime) Mockingbird.getProxyObject(Runtime.class);
        setPrivateField(MolapExecutionService.class, "execService", null);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(true, Runtime.class, "getRuntime", "()java.lang.Runtime", new Object[] {}, runtime, 1);
        Mockingbird.setReturnValue(false, runtime, "availableProcessors", "()int", new Object[] {}, new Integer(1), 1);
        Mockingbird.setReturnValue(true, Executors.class, "newFixedThreadPool", "(int)java.util.concurrent.ExecutorService", new Object[] {new Integer(0)}, null, 1);
        Mockingbird.enterTestMode(MolapExecutionService.class);
        ExecutorService result = MolapExecutionService.getExecutionService();
        assertNull("result", result);
        assertNotNull("MolapExecutionService.getExecutionService()", MolapExecutionService.getExecutionService());
    }
}

