/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 4:49:32 PM
 * Time to generate: 00:27.012 seconds
 *
 */

package com.huawei.unibi.molap.engine.datastorage.tree;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import com.huawei.unibi.molap.datastorage.store.MeasureDataWrapper;
import com.huawei.unibi.molap.datastorage.store.NodeKeyStore;
import com.huawei.unibi.molap.datastorage.store.StoreFactory;
import com.huawei.unibi.molap.datastorage.store.impl.FileHolderImpl;
import com.huawei.unibi.molap.datastorage.store.impl.key.uncompressed.AbstractSingleArrayKeyStore;
import com.huawei.unibi.molap.engine.datastorage.Pair;
import com.huawei.unibi.molap.engine.scanner.impl.KeyValue;

public class CSBInternalNodeAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return CSBInternalNode.class;
    }
    
    public void testConstructor() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        byte[] writableKeyArray = ((NodeKeyStore) getPrivateField(cSBInternalNode, "keyStore")).getWritableKeyArray();
        assertEquals("cSBInternalNode.keyStore.getWritableKeyArray().length", 100000, writableKeyArray.length);
    }
    
    public void testAddEntry() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        cSBInternalNode.addEntry((Pair) new Pair());
        assertEquals("cSBInternalNode.getnKeys()", 0, cSBInternalNode.getnKeys());
    }
    
    public void testGetBackKeyArray() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        byte[] result = cSBInternalNode.getBackKeyArray(new FileHolderImpl());
        assertEquals("result.length", 100000, result.length);
        assertEquals("result[0]", (byte)0, result[0]);
        Object actual = ((NodeKeyStore) getPrivateField(cSBInternalNode, "keyStore")).getWritableKeyArray();
        assertSame("cSBInternalNode.keyStore.getWritableKeyArray()", result, actual);
    }
    
    public void testGetBackKeyArray1() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 0, "testCSBInternalNodeTableName");
        byte[] result = cSBInternalNode.getBackKeyArray(new FileHolderImpl());
        assertEquals("result.length", 0, result.length);
        Object actual = ((NodeKeyStore) getPrivateField(cSBInternalNode, "keyStore")).getWritableKeyArray();
        assertSame("cSBInternalNode.keyStore.getWritableKeyArray()", result, actual);
    }
    
    public void testGetChild() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        CSBNode cSBInternalNode2 = new CSBInternalNode(1000, 0, "testCSBInternalNodeTableName1");
        CSBNode[] children = new CSBNode[3];
        children[0] = cSBInternalNode2;
        cSBInternalNode.setChildren(children);
        CSBNode result = cSBInternalNode.getChild(0);
        assertSame("result", cSBInternalNode2, result);
    }
    
    public void testGetKey() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 0, "testCSBInternalNodeTableName");
        byte[] result = cSBInternalNode.getKey(100, new FileHolderImpl());
        assertEquals("result.length", 0, result.length);
        byte[] writableKeyArray = ((NodeKeyStore) getPrivateField(cSBInternalNode, "keyStore")).getWritableKeyArray();
        assertEquals("cSBInternalNode.keyStore.getWritableKeyArray().length", 0, writableKeyArray.length);
    }
    
    public void testGetKey1() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        byte[] result = cSBInternalNode.getKey(0, new FileHolderImpl());
        assertEquals("result.length", 1000, result.length);
        assertEquals("result[0]", (byte)0, result[0]);
        byte[] writableKeyArray = ((NodeKeyStore) getPrivateField(cSBInternalNode, "keyStore")).getWritableKeyArray();
        assertEquals("cSBInternalNode.keyStore.getWritableKeyArray().length", 100000, writableKeyArray.length);
    }
    
    public void testGetNext() throws Throwable {
        CSBNode result = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName").getNext();
        assertNull("result", result);
    }
    
    public void testGetNextKeyValue() throws Throwable {
        KeyValue result = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName").getNextKeyValue(100);
        assertNull("result", result);
    }
    
    public void testGetNodeMsrDataWrapper() throws Throwable {
        int[] cols = new int[2];
        MeasureDataWrapper result = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName").getNodeMsrDataWrapper(cols, new FileHolderImpl());
        assertNull("result", result);
    }
    
    public void testGetValue() throws Throwable {
        double[] result = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName").getValue(100);
        assertNull("result", result);
    }
    
    public void testGetValueSize() throws Throwable {
        short result = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName").getValueSize();
        assertEquals("result", (short)0, result);
    }
    
    public void testIsLeafNode() throws Throwable {
        boolean result = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName").isLeafNode();
        assertFalse("result", result);
    }
    
    public void testSetChildren() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        CSBNode[] children = new CSBNode[2];
        cSBInternalNode.setChildren(children);
        assertSame("cSBInternalNode.getChildren()", children, cSBInternalNode.getChildren());
    }
    
    public void testSetKey() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 0, "testCSBInternalNodeTableName");
        byte[] key = new byte[1];
        cSBInternalNode.setKey(100, key);
        assertEquals("cSBInternalNode.getnKeys()", 1, cSBInternalNode.getnKeys());
        byte[] writableKeyArray = ((NodeKeyStore) getPrivateField(cSBInternalNode, "keyStore")).getWritableKeyArray();
        assertEquals("cSBInternalNode.keyStore.getWritableKeyArray().length", 0, writableKeyArray.length);
    }
    
    public void testSetNext() throws Throwable {
        CSBNode nextNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        CSBInternalNode cSBInternalNode = new CSBInternalNode(1000, 0, "testCSBInternalNodeTableName1");
        cSBInternalNode.setNext(nextNode);
        assertEquals("cSBInternalNode.getnKeys()", 0, cSBInternalNode.getnKeys());
    }
    
    public void testSetNextNode() throws Throwable {
        CSBInternalNode nextNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        nextNode.setNextNode(nextNode);
        assertEquals("nextNode.getnKeys()", 0, nextNode.getnKeys());
    }
    
    public void testSetPrevNode() throws Throwable {
        CSBNode prevNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        CSBInternalNode cSBInternalNode = new CSBInternalNode(1000, 0, "testCSBInternalNodeTableName1");
        cSBInternalNode.setPrevNode(prevNode);
        assertEquals("cSBInternalNode.getnKeys()", 0, cSBInternalNode.getnKeys());
    }
    
    public void testSetnKeys() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        cSBInternalNode.setnKeys(100);
        assertEquals("cSBInternalNode.getnKeys()", 100, cSBInternalNode.getnKeys());
    }
    
    public void testConstructorThrowsNegativeArraySizeException() throws Throwable {
        try {
            new CSBInternalNode(-1, 100, "testCSBInternalNodeTableName");
            fail("Expected NegativeArraySizeException to be thrown");
        } catch (NegativeArraySizeException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractSingleArrayKeyStore.class, ex);
        }
    }
    
    public void testGetChildThrowsArrayIndexOutOfBoundsException() throws Throwable {
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName");
        CSBNode[] children = new CSBNode[3];
        cSBInternalNode.setChildren(children);
        try {
            cSBInternalNode.getChild(100);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertEquals("ex.getMessage()", "100", ex.getMessage());
            assertThrownBy(CSBInternalNode.class, ex);
        }
    }
    
    public void testGetChildThrowsNullPointerException() throws Throwable {
        try {
            new CSBInternalNode(100, 1000, "testCSBInternalNodeTableName").getChild(100);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(CSBInternalNode.class, ex);
        }
    }
    
    public void testGetKeyThrowsArrayIndexOutOfBoundsException() throws Throwable {
        Mockingbird.enterRecordingMode();
        NodeKeyStore singleArrayKeyInMemoryStore = (NodeKeyStore) Mockingbird.getProxyObject(NodeKeyStore.class);
        Mockingbird.setReturnValue(StoreFactory.createKeyStore(0, 1, false), singleArrayKeyInMemoryStore);
        Mockingbird.enterTestMode(CSBInternalNode.class);
        CSBInternalNode cSBInternalNode = new CSBInternalNode(0, 1, "testCSBInternalNodeTableName");
        FileHolderImpl fileHolder = (FileHolderImpl) Mockingbird.getProxyObject(FileHolderImpl.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setException(singleArrayKeyInMemoryStore.get(33, fileHolder), (Throwable) Mockingbird.getProxyObject(ArrayIndexOutOfBoundsException.class));
        Mockingbird.enterTestMode(CSBInternalNode.class);
        try {
            cSBInternalNode.getKey(33, fileHolder);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertSame("cSBInternalNode.keyStore", singleArrayKeyInMemoryStore, getPrivateField(cSBInternalNode, "keyStore"));
        }
    }
    
    public void testGetKeyThrowsNegativeArraySizeException() throws Throwable {
        Mockingbird.enterRecordingMode();
        NodeKeyStore singleArrayKeyInMemoryStore = (NodeKeyStore) Mockingbird.getProxyObject(NodeKeyStore.class);
        Mockingbird.setReturnValue(StoreFactory.createKeyStore(0, -1, false), singleArrayKeyInMemoryStore);
        Mockingbird.enterTestMode(CSBInternalNode.class);
        CSBInternalNode cSBInternalNode = new CSBInternalNode(0, -1, "testCSBInternalNodeTableName");
        FileHolderImpl fileHolder = (FileHolderImpl) Mockingbird.getProxyObject(FileHolderImpl.class);
        Mockingbird.enterRecordingMode();
        Mockingbird.setException(singleArrayKeyInMemoryStore.get(8, fileHolder), (Throwable) Mockingbird.getProxyObject(NegativeArraySizeException.class));
        Mockingbird.enterTestMode(CSBInternalNode.class);
        try {
            cSBInternalNode.getKey(8, fileHolder);
            fail("Expected NegativeArraySizeException to be thrown");
        } catch (NegativeArraySizeException ex) {
            assertSame("cSBInternalNode.keyStore", singleArrayKeyInMemoryStore, getPrivateField(cSBInternalNode, "keyStore"));
        }
    }
    
    public void testSetKeyThrowsArrayIndexOutOfBoundsException() throws Throwable {
        Mockingbird.enterRecordingMode();
        NodeKeyStore singleArrayKeyInMemoryStore = (NodeKeyStore) Mockingbird.getProxyObject(NodeKeyStore.class);
        Mockingbird.setReturnValue(StoreFactory.createKeyStore(100, 21, false), singleArrayKeyInMemoryStore);
        Mockingbird.enterTestMode(CSBInternalNode.class);
        CSBInternalNode cSBInternalNode = new CSBInternalNode(100, 21, "testCSBInternalNodeTableName");
        byte[] key = new byte[0];
        Mockingbird.enterRecordingMode();
        singleArrayKeyInMemoryStore.put(1, key);
        Mockingbird.setExceptionForVoid((Throwable) Mockingbird.getProxyObject(ArrayIndexOutOfBoundsException.class));
        Mockingbird.enterTestMode(CSBInternalNode.class);
        try {
            cSBInternalNode.setKey(1, key);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException ex) {
            assertSame("cSBInternalNode.keyStore", singleArrayKeyInMemoryStore, getPrivateField(cSBInternalNode, "keyStore"));
            assertEquals("cSBInternalNode.getnKeys()", 0, cSBInternalNode.getnKeys());
        }
    }
    
    public void testSetKeyThrowsNullPointerException() throws Throwable {
        Mockingbird.enterRecordingMode();
        NodeKeyStore singleArrayKeyInMemoryStore = (NodeKeyStore) Mockingbird.getProxyObject(NodeKeyStore.class);
        Mockingbird.setReturnValue(StoreFactory.createKeyStore(79, 0, false), singleArrayKeyInMemoryStore);
        Mockingbird.enterTestMode(CSBInternalNode.class);
        CSBInternalNode cSBInternalNode = new CSBInternalNode(79, 0, "testCSBInternalNodeTableName");
        Mockingbird.enterRecordingMode();
        singleArrayKeyInMemoryStore.put(2, (byte[]) null);
        Mockingbird.setExceptionForVoid((Throwable) Mockingbird.getProxyObject(NullPointerException.class));
        Mockingbird.enterTestMode(CSBInternalNode.class);
        try {
            cSBInternalNode.setKey(2, (byte[]) null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertSame("cSBInternalNode.keyStore", singleArrayKeyInMemoryStore, getPrivateField(cSBInternalNode, "keyStore"));
            assertEquals("cSBInternalNode.getnKeys()", 0, cSBInternalNode.getnKeys());
        }
    }
}

