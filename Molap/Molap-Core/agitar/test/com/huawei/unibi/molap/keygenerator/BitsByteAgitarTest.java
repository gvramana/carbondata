/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:29:18 PM
 * Time to generate: 00:13.193 seconds
 *
 */

package com.huawei.unibi.molap.keygenerator;

import com.agitar.lib.junit.AgitarTestCase;

public class BitsByteAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return BitsByte.class;
    }
    
    public void testConstructor() throws Throwable {
        int[] lens = new int[2];
        lens[1] = 1;
        BitsByte bitsByte = new BitsByte(lens);
        assertEquals("bitsByte.length", 1, ((Number) getPrivateField(bitsByte, "length")).intValue());
        assertSame("bitsByte.lens", lens, getPrivateField(bitsByte, "lens"));
        assertEquals("bitsByte.wsize", 1, ((Number) getPrivateField(bitsByte, "wsize")).intValue());
    }
    
    public void testConstructor1() throws Throwable {
        int[] lens = new int[0];
        BitsByte bitsByte = new BitsByte(lens);
        assertEquals("bitsByte.length", 0, ((Number) getPrivateField(bitsByte, "length")).intValue());
        assertSame("bitsByte.lens", lens, getPrivateField(bitsByte, "lens"));
        assertEquals("bitsByte.wsize", 0, ((Number) getPrivateField(bitsByte, "wsize")).intValue());
    }
    
    public void testConstructor2() throws Throwable {
        int[] lens = new int[1];
        BitsByte bitsByte = new BitsByte(lens);
        assertEquals("bitsByte.length", 0, ((Number) getPrivateField(bitsByte, "length")).intValue());
        assertSame("bitsByte.lens", lens, getPrivateField(bitsByte, "lens"));
        assertEquals("bitsByte.wsize", 0, ((Number) getPrivateField(bitsByte, "wsize")).intValue());
    }
    
    public void testGetTotalLength() throws Throwable {
        int[] lens = new int[1];
        BitsByte bitsByte = new BitsByte(lens);
        int[] ints = new int[0];
        int result = ((Number) callPrivateMethod("com.huawei.unibi.molap.keygenerator.BitsByte", "getTotalLength", new Class[] {int[].class}, bitsByte, new Object[] {ints})).intValue();
        assertEquals("result", 0, result);
    }
    
    public void testGetTotalLength1() throws Throwable {
        int[] ints = new int[1];
        int[] lens = new int[7];
        BitsByte bitsByte = new BitsByte(lens);
        int result = ((Number) callPrivateMethod("com.huawei.unibi.molap.keygenerator.BitsByte", "getTotalLength", new Class[] {int[].class}, bitsByte, new Object[] {ints})).intValue();
        assertEquals("result", 0, result);
    }
    
    public void testGetTotalLength2() throws Throwable {
        int[] lens = new int[8];
        BitsByte bitsByte = new BitsByte(lens);
        int[] ints = new int[3];
        ints[0] = 100;
        int result = ((Number) callPrivateMethod("com.huawei.unibi.molap.keygenerator.BitsByte", "getTotalLength", new Class[] {int[].class}, bitsByte, new Object[] {ints})).intValue();
        assertEquals("result", 100, result);
    }
    
    public void testConstructorThrowsNullPointerException() throws Throwable {
        try {
            new BitsByte(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(BitsByte.class, ex);
        }
    }
    
    public void testGetTotalLengthThrowsNullPointerException() throws Throwable {
        int[] lens = new int[1];
        BitsByte bitsByte = new BitsByte(lens);
        try {
            callPrivateMethod("com.huawei.unibi.molap.keygenerator.BitsByte", "getTotalLength", new Class[] {int[].class}, bitsByte, new Object[] {null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(BitsByte.class, ex);
        }
    }
}

