/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 2:32:14 PM
 * Time to generate: 00:14.522 seconds
 *
 */

package com.huawei.unibi.molap.datastorage.store.impl.key.uncompressed;

import com.agitar.lib.junit.AgitarTestCase;
import com.huawei.unibi.molap.datastorage.store.FileHolder;
import com.huawei.unibi.molap.datastorage.store.impl.FileHolderImpl;
import java.nio.ByteBuffer;

public class SingleArrayKeyInMemoryStoreAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return SingleArrayKeyInMemoryStore.class;
    }
    
    public void testConstructor() throws Throwable {
        SingleArrayKeyInMemoryStore singleArrayKeyInMemoryStore = new SingleArrayKeyInMemoryStore(100, 1000);
        assertEquals("singleArrayKeyInMemoryStore.totalNumberOfElements", 100, singleArrayKeyInMemoryStore.totalNumberOfElements);
        assertEquals("singleArrayKeyInMemoryStore.datastore.length", 100000, singleArrayKeyInMemoryStore.datastore.length);
        assertEquals("singleArrayKeyInMemoryStore.sizeOfEachElement", 1000, singleArrayKeyInMemoryStore.sizeOfEachElement);
    }
    
    public void testConstructor1() throws Throwable {
        SingleArrayKeyInMemoryStore singleArrayKeyInMemoryStore = new SingleArrayKeyInMemoryStore(100, 1000, 100L, "testSingleArrayKeyInMemoryStoreFilePath", new FileHolderImpl(), 0);
        assertEquals("singleArrayKeyInMemoryStore.totalNumberOfElements", 100, singleArrayKeyInMemoryStore.totalNumberOfElements);
        assertEquals("singleArrayKeyInMemoryStore.datastore.length", 0, singleArrayKeyInMemoryStore.datastore.length);
        assertEquals("singleArrayKeyInMemoryStore.sizeOfEachElement", 1000, singleArrayKeyInMemoryStore.sizeOfEachElement);
    }
    
    public void testConstructorThrowsIllegalArgumentException() throws Throwable {
        FileHolder fileHolder = new FileHolderImpl(100);
        try {
            new SingleArrayKeyInMemoryStore(100, 1000, 100L, "testSingleArrayKeyInMemoryStoreFilePath", fileHolder, -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(ByteBuffer.class, ex);
        }
    }
    
    public void testConstructorThrowsNegativeArraySizeException() throws Throwable {
        try {
            new SingleArrayKeyInMemoryStore(-1, 100);
            fail("Expected NegativeArraySizeException to be thrown");
        } catch (NegativeArraySizeException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractSingleArrayKeyStore.class, ex);
        }
    }
    
    public void testConstructorThrowsNegativeArraySizeException1() throws Throwable {
        FileHolder fileHolder = new FileHolderImpl(100);
        try {
            new SingleArrayKeyInMemoryStore(-1, 100, 100L, "testSingleArrayKeyInMemoryStoreFilePath", fileHolder, 1000);
            fail("Expected NegativeArraySizeException to be thrown");
        } catch (NegativeArraySizeException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AbstractSingleArrayKeyStore.class, ex);
        }
    }
    
    public void testConstructorThrowsNullPointerException() throws Throwable {
        try {
            new SingleArrayKeyInMemoryStore(100, 1000, 100L, "testSingleArrayKeyInMemoryStoreFilePath", null, 0);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(SingleArrayKeyInMemoryStore.class, ex);
        }
    }
}

