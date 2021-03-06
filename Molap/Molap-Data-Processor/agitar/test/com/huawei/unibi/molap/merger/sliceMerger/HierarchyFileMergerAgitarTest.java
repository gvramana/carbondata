/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on Jul 31, 2013 6:20:25 PM
 * Time to generate: 00:18.785 seconds
 *
 */

package com.huawei.unibi.molap.merger.sliceMerger;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import com.huawei.unibi.molap.merger.Util.MolapSliceMergerUtil;
import com.huawei.unibi.molap.merger.exeception.SliceMergerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HierarchyFileMergerAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return HierarchyFileMerger.class;
    }
    
    public void testConstructor() throws Throwable {
        Map hierarchyAndKeySizeMap = new HashMap(100, 100.0F);
        HierarchyFileMerger hierarchyFileMerger = new HierarchyFileMerger("testHierarchyFileMergerMergeLocation", hierarchyAndKeySizeMap);
        assertSame("hierarchyFileMerger.hierarchyAndKeySizeMap", hierarchyAndKeySizeMap, getPrivateField(hierarchyFileMerger, "hierarchyAndKeySizeMap"));
        assertEquals("hierarchyFileMerger.mergeLocation", "testHierarchyFileMergerMergeLocation", getPrivateField(hierarchyFileMerger, "mergeLocation"));
    }
    
    public void testGetSortedPathForFilesThrowsNullPointerException() throws Throwable {
        HierarchyFileMerger hierarchyFileMerger = new HierarchyFileMerger("testHierarchyFileMergerMergeLocation", new HashMap(100, 100.0F));
        try {
            callPrivateMethod("com.huawei.unibi.molap.merger.sliceMerger.HierarchyFileMerger", "getSortedPathForFiles", new Class[] {String.class}, hierarchyFileMerger, new Object[] {null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(File.class, ex);
        }
    }
    
    public void testMergerDataThrowsNoClassDefFoundError() throws Throwable {
        HierarchyFileMerger hierarchyFileMerger = new HierarchyFileMerger("testHierarchyFileMergerMergeLocation", new HashMap(100, 100.0F));
        String[] sliceLocation = new String[0];
        try {
            hierarchyFileMerger.mergerData(sliceLocation);
            fail("Expected NoClassDefFoundError to be thrown");
        } catch (NoClassDefFoundError ex) {
            assertEquals("ex.getMessage()", "com/huawei/unibi/molap/datastorage/store/FileHolder", ex.getMessage());
            assertThrownBy(Class.class, ex);
        }
    }
    
    public void testMergerDataThrowsNullPointerException() throws Throwable {
        HierarchyFileMerger hierarchyFileMerger = new HierarchyFileMerger("testHierarchyFileMergerMergeLocation", new HashMap(100, 100.0F));
        try {
            hierarchyFileMerger.mergerData((String[]) null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(HierarchyFileMerger.class, ex);
        }
    }
    
    public void testMergerDataThrowsNullPointerException1() throws Throwable {
        HierarchyFileMerger hierarchyFileMerger = new HierarchyFileMerger("testHierarchyFileMergerMergeLocation", new HashMap(100, 100.0F));
        String[] sliceLocation = new String[1];
        try {
            hierarchyFileMerger.mergerData(sliceLocation);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(File.class, ex);
        }
    }
    
    public void testMergerDataThrowsSliceMergerExceptionWithAggressiveMocks() throws Throwable {
        HierarchyFileMerger hierarchyFileMerger = (HierarchyFileMerger) Mockingbird.getProxyObject(HierarchyFileMerger.class, true);
        String[] strings = new String[0];
        Map map = (Map) Mockingbird.getProxyObject(Map.class);
        Set set = (Set) Mockingbird.getProxyObject(Set.class);
        Iterator iterator = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        Map.Entry entry = (Map.Entry) Mockingbird.getProxyObject(Map.Entry.class);
        List list = (List) Mockingbird.getProxyObject(List.class);
        Map.Entry entry2 = (Map.Entry) Mockingbird.getProxyObject(Map.Entry.class);
        List list2 = (List) Mockingbird.getProxyObject(List.class);
        Map.Entry entry3 = (Map.Entry) Mockingbird.getProxyObject(Map.Entry.class);
        List list3 = (List) Mockingbird.getProxyObject(List.class);
        Map.Entry entry4 = (Map.Entry) Mockingbird.getProxyObject(Map.Entry.class);
        List list4 = (List) Mockingbird.getProxyObject(List.class);
        Iterator iterator2 = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        File file = (File) Mockingbird.getProxyObject(File.class);
        File file2 = (File) Mockingbird.getProxyObject(File.class);
        File file3 = (File) Mockingbird.getProxyObject(File.class);
        IOException iOException = (IOException) Mockingbird.getProxyObject(IOException.class);
        setPrivateField(hierarchyFileMerger, "mergeLocation", "");
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(true, MolapSliceMergerUtil.class, "getFileMap", "(java.io.File[][])java.util.Map", map, 1);
        Mockingbird.setReturnValue(map.entrySet(), set);
        Mockingbird.setReturnValue(set.iterator(), iterator);
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), entry);
        Mockingbird.setReturnValue(entry.getValue(), list);
        Mockingbird.setReturnValue(list.size(), 0);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), entry2);
        Mockingbird.setReturnValue(entry2.getValue(), list2);
        Mockingbird.setReturnValue(list2.size(), 0);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), entry3);
        Mockingbird.setReturnValue(entry3.getValue(), list3);
        Mockingbird.setReturnValue(list3.size(), 1);
        Mockingbird.setReturnValue(list3.get(0), null);
        Mockingbird.setReturnValue(arrayList.add((Object) null), false);
        iterator.remove();
        Mockingbird.setNormalReturnForVoid();
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), entry4);
        Mockingbird.setReturnValue(entry4.getValue(), list4);
        Mockingbird.setReturnValue(list4.size(), 1);
        Mockingbird.setReturnValue(list4.get(0), null);
        Mockingbird.setReturnValue(arrayList.add((Object) null), false);
        iterator.remove();
        Mockingbird.setNormalReturnForVoid();
        Mockingbird.setReturnValue(iterator.hasNext(), false);
        Mockingbird.setReturnValue(arrayList.iterator(), iterator2);
        Mockingbird.setReturnValue(iterator2.hasNext(), true);
        Mockingbird.setReturnValue(iterator2.next(), file);
        Mockingbird.setReturnValue(false, file, "getName", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        File file4 = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file4);
        MolapSliceMergerUtil.copyFile(file, file4);
        Mockingbird.setNormalReturnForVoid();
        Mockingbird.setReturnValue(iterator2.hasNext(), true);
        Mockingbird.setReturnValue(iterator2.next(), file2);
        Mockingbird.setReturnValue(false, file2, "getName", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        File file5 = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file5);
        MolapSliceMergerUtil.copyFile(file2, file5);
        Mockingbird.setNormalReturnForVoid();
        Mockingbird.setReturnValue(iterator2.hasNext(), true);
        Mockingbird.setReturnValue(iterator2.next(), file3);
        Mockingbird.setReturnValue(false, file3, "getName", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        File file6 = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file6);
        MolapSliceMergerUtil.copyFile(file3, file6);
        Mockingbird.setExceptionForVoid(iOException);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.replaceObjectForRecording(SliceMergerException.class, "<init>(java.lang.String,java.lang.Throwable)", Mockingbird.getProxyObject(SliceMergerException.class));
        Mockingbird.enterTestMode(HierarchyFileMerger.class);
        try {
            hierarchyFileMerger.mergerData(strings);
            fail("Expected SliceMergerException to be thrown");
        } catch (SliceMergerException ex) {
            assertTrue("Test call resulted in expected outcome", true);
        }
    }
}

