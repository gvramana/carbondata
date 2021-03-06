/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on Jul 31, 2013 6:23:48 PM
 * Time to generate: 00:12.752 seconds
 *
 */

package com.huawei.unibi.molap.merger.sliceMerger;

import com.agitar.lib.junit.AgitarTestCase;
import java.util.ArrayList;
import java.util.List;

public class TimeBasedSameCompressionMergerThreadAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return TimeBasedSameCompressionMergerThread.class;
    }
    
    public void testConstructor() throws Throwable {
        List sliceInfoList = new ArrayList(100);
        String[] aggType = new String[2];
        TimeBasedSameCompressionMergerThread timeBasedSameCompressionMergerThread = new TimeBasedSameCompressionMergerThread(sliceInfoList, "testTimeBasedSameCompressionMergerThreadDestinationLocation", 100L, "testTimeBasedSameCompressionMergerThreadTableName", 100, 1000, aggType);
        assertEquals("timeBasedSameCompressionMergerThread.destinationLocation", "testTimeBasedSameCompressionMergerThreadDestinationLocation", getPrivateField(timeBasedSameCompressionMergerThread, "destinationLocation"));
        assertEquals("timeBasedSameCompressionMergerThread.measureCount", 1000, ((Number) getPrivateField(timeBasedSameCompressionMergerThread, "measureCount")).intValue());
        assertSame("timeBasedSameCompressionMergerThread.sliceInfoList", sliceInfoList, getPrivateField(timeBasedSameCompressionMergerThread, "sliceInfoList"));
        assertEquals("timeBasedSameCompressionMergerThread.leafMetaDataSize", 12216, ((Number) getPrivateField(timeBasedSameCompressionMergerThread, "leafMetaDataSize")).intValue());
        assertSame("timeBasedSameCompressionMergerThread.aggType", aggType, getPrivateField(timeBasedSameCompressionMergerThread, "aggType"));
        assertEquals("timeBasedSameCompressionMergerThread.tableName", "testTimeBasedSameCompressionMergerThreadTableName", getPrivateField(timeBasedSameCompressionMergerThread, "tableName"));
        assertEquals("timeBasedSameCompressionMergerThread.fileSizeToBeMerge", 100L, ((Number) getPrivateField(timeBasedSameCompressionMergerThread, "fileSizeToBeMerge")).longValue());
    }
    
    public void testWriteleafMetaDataToFileThrowsNullPointerException() throws Throwable {
        String[] aggType = new String[0];
        TimeBasedSameCompressionMergerThread timeBasedSameCompressionMergerThread = new TimeBasedSameCompressionMergerThread(new ArrayList(100), "testTimeBasedSameCompressionMergerThreadDestinationLocation", 100L, "testTimeBasedSameCompressionMergerThreadTableName", 100, 1000, aggType);
        List arrayList = new ArrayList(1000);
        try {
            callPrivateMethod("com.huawei.unibi.molap.merger.sliceMerger.TimeBasedSameCompressionMergerThread", "writeleafMetaDataToFile", new Class[] {List.class}, timeBasedSameCompressionMergerThread, new Object[] {arrayList});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(TimeBasedSameCompressionMergerThread.class, ex);
            assertNull("timeBasedSameCompressionMergerThread.fileChannel", getPrivateField(timeBasedSameCompressionMergerThread, "fileChannel"));
            assertEquals("(ArrayList) arrayList.size()", 0, arrayList.size());
        }
    }
}

