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
package com.huawei.unibi.molap.datastorage.store;

import java.nio.ByteBuffer;

/**
 * 
 * Project Name NSE V3R7C00 
 * Module Name : Molap Commons 
 * Author K00900841
 * Created Date :21-May-2013 7:06:55 PM
 * FileName : FileHolder.java
 * Class Description : This class will be used to the file streams so every time for one thread 
 * we dont have to open same file multiple times, This will used for reading the files
 * Version 1.0
 */
public interface FileHolder
{
    /**
     * This method will be used to read the byte array from file based on offset and length(number of bytes) need to read
     * 
     * @param filePath
     *          fully qualified file path
     * @param offset
     *          reading start position, 
     * @param length
     *          number of bytes to be read
     * @return read byte array
     *
     */
     byte[] readByteArray(String filePath, long offset, int length);
    
    /**
     * This method will be used to read the byte array from file based on length(number of bytes) 
     * 
     * @param filePath
     *          fully qualified file path
     * @param length
     *          number of bytes to be read
     * @return read byte array
     *
     */
     byte[] readByteArray(String filePath, int length);
    
    /**
     * This method will be used to read the bytebuffer from file based on offset and length(number of bytes) need to read
     * 
     * @param filePath
     *          fully qualified file path
     * @param offset
     *          reading start position, 
     * @param length
     *          number of bytes to be read
     * @return read byte array
     *
     */
     ByteBuffer readByteBuffer(String filePath, long offset, int length);
    
    /**
     * This method will be used to read int from file from postion(offset), here
     * length will be always 4 bacause int byte size if 4
     * 
     * @param filePath
     *       fully qualified file path
     * @param offset
     *          reading start position, 
     * @return read int
     * 
     */
     int readInt(String filePath, long offset);
    
    
    /**
     * This method will be used to read long from file from postion(offset), here
     * length will be always 8 bacause int byte size is 8
     * 
     * @param filePath
     *       fully qualified file path
     * @param offset
     *          reading start position, 
     * @return read long
     * 
     */
     long readLong(String filePath, long offset);
    
    /**
     * This method will be used to read int from file from postion(offset), here
     * length will be always 4 bacause int byte size if 4
     * 
     * @param filePath
     *       fully qualified file path
     * @return read int
     * 
     */
     int readInt(String filePath);
  
    
    /**
     * This method will be used to read long value from file from postion(offset), here
     * length will be always 8 because long byte size if 4
     * 
     * @param filePath
     *       fully qualified file path
     * @param offset
     *          reading start position, 
     * @return read long
     * 
     */
     long readDouble(String filePath, long offset);
    
    
    /**
     * This method will be used to close all the streams currently present in the cache
     *
     */
     void finish();
    
    
    /**
     * This method will return the size of the file
     * 
     * @param filePath 
     *          fully qualified file path
     * @return filesize
     *
     */
     long getFileSize(String filePath);
}
 