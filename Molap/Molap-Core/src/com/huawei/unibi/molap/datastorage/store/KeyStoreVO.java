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

/**
 * 
 * Project Name NSE V3R7C00 
 * Module Name : Molap Data Processor 
 * Author K00900841
 * Created Date :23-May-2013 5:46:57 PM
 * FileName : KeyStoreVO.java
 * Class Description : This class will holding all the properties required for getting the keyStore instance
 * Version 1.0
 */
public class KeyStoreVO
{
    /**
     * totalSize.
     */
    private int totalSize;
    
    /**
     *  sizeOfEachElement.
     */
    private int sizeOfEachElement;
    
    /**
     * isLeaf.
     */
    private boolean isLeaf;
    
    /**
     * isFileStore.
     */
    private boolean isFileStore;
    
    /**
     * offset.
     */
    private long offset;
    
    /**
     * fileName.
     */
    private String fileName;
    
    /**
     * length.
     */
    private int length;
    
    /**
     * fileHolder.
     */
    private FileHolder fileHolder;
    


    /**
     * getTotalSize.
     * @return int
     */
    public int getTotalSize()
    {
        return totalSize;
    }

    /**
     * setTotalSize.
     * @param totalSize
     */
    public void setTotalSize(int totalSize)
    {
        this.totalSize = totalSize;
    }

    /**
     * getSizeOfEachElement
     * @return int
     */
    public int getSizeOfEachElement()
    {
        return sizeOfEachElement;
    }

    /**
     * setSizeOfEachElement
     * @param sizeOfEachElement
     */
    public void setSizeOfEachElement(int sizeOfEachElement)
    {
        this.sizeOfEachElement = sizeOfEachElement;
    }

    /**
     * isLeaf.
     * @return boolean.
     */
    public boolean isLeaf()
    {
        return isLeaf;
    }

    /**
     * setLeaf.
     * @param isLeaf
     */
    public void setLeaf(boolean isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    /**
     * isFileStore()
     * @return boolean.
     */
    public boolean isFileStore()
    {
        return isFileStore;
    }

    /**
     * setFileStore
     * @param isFileStore boolean variable.
     */
    public void setFileStore(boolean isFileStore)
    {
        this.isFileStore = isFileStore;
    }

    /**
     * getOffset()
     * @return long.
     */
    public long getOffset()
    {
        return offset;
    }

    /**
     * setOffset.
     * @param offset
     */
    public void setOffset(long offset)
    {
        this.offset = offset;
    }

    /**
     * getFileName
     * @return String.
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * setFileName.
     * @param fileName
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * getLength.
     * @return int
     */
    public int getLength()
    {
        return length;
    }

    /**
     * setLength
     * @param length
     */
    public void setLength(int length)
    {
        this.length = length;
    }

    /**
     * getFileHolder()
     * @return FileHolder.
     */
    public FileHolder getFileHolder()
    {
        return fileHolder;
    }

    /**
     * setFileHolder.
     * @param fileHolder
     */
    public void setFileHolder(FileHolder fileHolder)
    {
        this.fileHolder = fileHolder;
    }
    
}
