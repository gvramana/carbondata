/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:30:12 PM
 * Time to generate: 00:13.850 seconds
 *
 */

package com.huawei.unibi.molap.keygenerator;

import com.agitar.lib.junit.AgitarTestCase;

public class KeyGenExceptionAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return KeyGenException.class;
    }
    
    public void testConstructor() throws Throwable {
        KeyGenException keyGenException = new KeyGenException("testKeyGenExceptionMsg");
        assertEquals("keyGenException.getMessage()", "testKeyGenExceptionMsg", keyGenException.getMessage());
    }
    
    public void testConstructor1() throws Throwable {
        new KeyGenException();
        assertTrue("Test call resulted in expected outcome", true);
    }
    
    public void testConstructor2() throws Throwable {
        Exception e = new SecurityException();
        KeyGenException keyGenException = new KeyGenException(e, "testKeyGenExceptionMsg");
        assertEquals("keyGenException.getMessage()", "testKeyGenExceptionMsg", keyGenException.getMessage());
        assertSame("keyGenException.getCause()", e, keyGenException.getCause());
    }
    
    public void testConstructor3() throws Throwable {
        Exception e = new KeyGenException();
        KeyGenException keyGenException = new KeyGenException(e);
        assertEquals("keyGenException.getMessage()", "com.huawei.unibi.molap.keygenerator.KeyGenException", keyGenException.getMessage());
        assertSame("keyGenException.getCause()", e, keyGenException.getCause());
    }
}

