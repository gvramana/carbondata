/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on Jul 31, 2013 6:23:08 PM
 * Time to generate: 00:09.065 seconds
 *
 */

package com.huawei.unibi.molap.merger;

import com.agitar.lib.junit.AgitarTestCase;
import java.io.File;

public class SliceAndFilesAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return SliceAndFiles.class;
    }
    
    public void testConstructor() throws Throwable {
        SliceAndFiles sliceAndFiles = new SliceAndFiles();
        assertNull("sliceAndFiles.getPath()", sliceAndFiles.getPath());
    }
    
    public void testSetPath() throws Throwable {
        SliceAndFiles sliceAndFiles = new SliceAndFiles();
        sliceAndFiles.setPath("testSliceAndFilesPath");
        assertEquals("sliceAndFiles.getPath()", "testSliceAndFilesPath", sliceAndFiles.getPath());
    }
    
    public void testSetSliceFactFilesList() throws Throwable {
        SliceAndFiles sliceAndFiles = new SliceAndFiles();
        File[] sliceFactFilesList = new File[0];
        sliceAndFiles.setSliceFactFilesList(sliceFactFilesList);
        assertSame("sliceAndFiles.getSliceFactFilesList()", sliceFactFilesList, sliceAndFiles.getSliceFactFilesList());
    }
}

