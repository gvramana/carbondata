/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on Aug 3, 2013 7:43:14 PM
 * Time to generate: 00:09.129 seconds
 *
 */

package com.huawei.unibi.molap.engine.mondrian.extensions.rolap.concurrent;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import java.lang.management.ManagementFactory;

public class MemorySensorAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return MemorySensor.class;
    }
    
    public void testMemoryMonitorThreadConstructor() throws Throwable {
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(true, ManagementFactory.class, "getMemoryMXBean", "()java.lang.management.MemoryMXBean", new Object[] {}, null, 1);
        Mockingbird.enterTestMode();
        callPrivateMethod("com.huawei.unibi.molap.engine.mondrian.extensions.rolap.concurrent.MemorySensor$MemoryMonitorThread", "<init>", new Class[] {float.class, float.class}, null, new Object[] {new Float(2.8E-45F), new Float(2.8E-45F)});
        assertTrue("Test call resulted in expected outcome", true);
    }
}

