/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on Jul 31, 2013 6:28:12 PM
 * Time to generate: 00:39.633 seconds
 *
 */

package com.huawei.unibi.molap.merger.impl;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import com.huawei.unibi.molap.merger.AbstractSliceMerger;

public class TenMBSliceMergerAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return TenMBSliceMerger.class;
    }
    
    public void testConstructor() throws Throwable {
        String[] strings = new String[0];
        Mockingbird.ignoreConstructorExceptions(AbstractSliceMerger.class);
        new TenMBSliceMerger("", 0, 0, "", "", strings);
        assertTrue("Test call resulted in expected outcome", true);
    }
}

