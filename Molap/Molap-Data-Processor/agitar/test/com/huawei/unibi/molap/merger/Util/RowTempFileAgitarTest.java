/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on Jul 31, 2013 6:22:43 PM
 * Time to generate: 00:09.877 seconds
 *
 */

package com.huawei.unibi.molap.merger.Util;

import com.agitar.lib.junit.AgitarTestCase;

public class RowTempFileAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return RowTempFile.class;
    }
    
    public void testConstructor() throws Throwable {
        byte[] row = new byte[1];
        RowTempFile rowTempFile = new RowTempFile(row, 100L, 1000L, 100, "testRowTempFileFilePath");
        assertEquals("rowTempFile.getOffset()", 1000L, rowTempFile.getOffset());
        assertEquals("rowTempFile.getFileSize()", 100L, rowTempFile.getFileSize());
        assertEquals("rowTempFile.getFilePath()", "testRowTempFileFilePath", rowTempFile.getFilePath());
        assertEquals("rowTempFile.getFileHolderIndex()", 100, rowTempFile.getFileHolderIndex());
        assertSame("rowTempFile.getRow()", row, rowTempFile.getRow());
    }
    
    public void testSetFileHolderIndex() throws Throwable {
        byte[] row = new byte[0];
        RowTempFile rowTempFile = new RowTempFile(row, 100L, 1000L, 100, "testRowTempFileFilePath");
        rowTempFile.setFileHolderIndex(1000);
        assertEquals("rowTempFile.getFileHolderIndex()", 1000, rowTempFile.getFileHolderIndex());
    }
    
    public void testSetFilePath() throws Throwable {
        RowTempFile rowTempFile = new RowTempFile("testString".getBytes(), 100L, 1000L, 100, "testRowTempFileFilePath");
        rowTempFile.setFilePath("testRowTempFileFilePath1");
        assertEquals("rowTempFile.getFilePath()", "testRowTempFileFilePath1", rowTempFile.getFilePath());
    }
    
    public void testSetFileSize() throws Throwable {
        byte[] row = new byte[2];
        RowTempFile rowTempFile = new RowTempFile(row, 100L, 1000L, 100, "testRowTempFileFilePath");
        rowTempFile.setFileSize(100L);
        assertEquals("rowTempFile.getFileSize()", 100L, rowTempFile.getFileSize());
    }
    
    public void testSetOffset() throws Throwable {
        byte[] row = new byte[2];
        RowTempFile rowTempFile = new RowTempFile(row, 100L, 1000L, 100, "testRowTempFileFilePath");
        rowTempFile.setOffset(100L);
        assertEquals("rowTempFile.getOffset()", 100L, rowTempFile.getOffset());
    }
    
    public void testSetRow() throws Throwable {
        byte[] row = new byte[0];
        RowTempFile rowTempFile = new RowTempFile(row, 100L, 1000L, 100, "testRowTempFileFilePath");
        byte[] row2 = new byte[0];
        rowTempFile.setRow(row2);
        assertSame("rowTempFile.getRow()", row2, rowTempFile.getRow());
    }
}

