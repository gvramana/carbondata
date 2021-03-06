/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
 *
 */
package com.huawei.unibi.molap.datastorage.store.impl.key.uncompressed;

import com.huawei.unibi.molap.datastorage.store.FileHolder;

/**
 * 
 * Project Name NSE V3R7C00 
 * Module Name : Molap Commons 
 * Author K00900841
 * Created Date :21-May-2013 7:21:49 PM
 * FileName : CompressedSingleArrayKeyFileStore.java
 * Class Description : 
 * 
 * Version 1.0
 */
public class SingleArrayKeyFileStore extends AbstractSingleArrayKeyStore
{
    /**
     * offset, this will be used for seek position
     */
    private long offset;
    
    /**
     * fully qualified file path
     */
    private String filePath;
    
    /**
     * length to be read
     */
    private int length;
    

    /**
     * 
     * 
     * @param size
     * @param elementSize
     *
     */
    public SingleArrayKeyFileStore(int size, int elementSize)
    {
        super(size, elementSize);
    }

    /**
     * 
     * 
     * @param size
     * @param elementSize
     * @param offset
     * @param filePath
     * @param length
     *
     */
    public SingleArrayKeyFileStore(int size, int elementSize, long offset, String filePath, int length)
    {
        this(size, elementSize);
        this.offset=offset;
        this.filePath = filePath;
        this.length = length;
        datastore=null;
    }

    /**
     * This method will be used to get the actual keys array present in the
     * store. This method will read
     * the data from file based on offset and length then return the data read from file
     * 
     * @param fileHolder
     *            file holder will be used to read the file
     * @return uncompressed 
     *          keys will return uncompressed key
     * 
     * 
     */
    @Override
    public byte[] getBackArray(FileHolder fileHolder)
    {
        if (null != fileHolder)
        {
            return fileHolder.readByteArray(filePath, offset, length);
        }
        else
        {
            return new byte[0];
        }
    }
    
    /**
     * This method will be used to get the key array based on index This method
     * will first read the data from file based on offset and length then get
     * the array for index and return
     * 
     * @param index
     *            index in store
     * @param fileHolder
     *            file holder will be used to read the file
     * @return key
     * 
     */
    @Override
    public byte[] get(int index,FileHolder fileHolder)
    {
        // read from file based on offset and index, fileholder will read that
        // much byte from that offset,
        byte[] unCompress = fileHolder.readByteArray(filePath, offset, length);
     // create new array of size of each element
        byte[] copy = new byte[sizeOfEachElement];
        // copy array for given index
        // copy will done based on below calculation
        // eg: index is 4 and size of each key is 6 then copy from 6*4= 24th
        // index till 29th index 
        System.arraycopy(unCompress, ((index) * sizeOfEachElement), copy, 0,
                sizeOfEachElement);
        return copy;
    }

}
