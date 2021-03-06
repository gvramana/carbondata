/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:30:15 PM
 * Time to generate: 00:13.459 seconds
 *
 */

package com.huawei.unibi.molap.keygenerator.util;

import com.agitar.lib.junit.AgitarTestCase;

public class KeyGenUtilAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return KeyGenUtil.class;
    }
    
    public void testConstructor() throws Throwable {
        new KeyGenUtil();
        assertTrue("Test call resulted in expected outcome", true);
    }
    
    public void testToLong() throws Throwable {
        byte[] bytes = new byte[2];
        bytes[0] = (byte)-128;
        long result = KeyGenUtil.toLong(bytes, 0, 1);
        assertEquals("result", 128L, result);
    }
    
    public void testToLong1() throws Throwable {
        byte[] bytes = new byte[2];
        long result = KeyGenUtil.toLong(bytes, 0, 1);
        assertEquals("result", 0L, result);
    }
    
    public void testToLong2() throws Throwable {
        byte[] bytes = new byte[3];
        long result = KeyGenUtil.toLong(bytes, 100, 0);
        assertEquals("result", 0L, result);
    }
    
    public void testToLongThrowsArrayIndexOutOfBoundsException() throws Throwable {
        byte[] bytes = new byte[3];
        try {
            KeyGenUtil.toLong(bytes, 100, 1000);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "100", ex.getMessage());
            assertThrownBy(KeyGenUtil.class, ex);
        }
    }
    
    public void testToLongThrowsArrayIndexOutOfBoundsException1() throws Throwable {
        byte[] bytes = new byte[4];
        try {
            KeyGenUtil.toLong(bytes, 0, 100);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "4", ex.getMessage());
            assertThrownBy(KeyGenUtil.class, ex);
        }
    }
    
    public void testToLongThrowsNullPointerException() throws Throwable {
        try {
            KeyGenUtil.toLong((byte[]) null, 100, 1000);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(KeyGenUtil.class, ex);
        }
    }
}

