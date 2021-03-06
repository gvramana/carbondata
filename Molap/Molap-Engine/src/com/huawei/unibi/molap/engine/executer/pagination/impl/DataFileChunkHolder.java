/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwedLwWEET5JCCp2J65j3EiB2PJ4ohyqaGEDuXyJ
TTt3d0DebYPOTY5EEEbPew1WgO40egWK6f0mL8apDujTbAdIJaFxmwMbIXXKWkCJl61wFgMl
xUphcjumnqiX4p6rdg4Jh6sECPH//yeB0vWgW+2VHgTwIhKwbO44RUeCnJrG0g==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
 *
 */
package com.huawei.unibi.molap.engine.executer.pagination.impl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;
import com.huawei.unibi.molap.engine.executer.pagination.exception.MolapPaginationException;
import com.huawei.unibi.molap.engine.util.MolapEngineLogEvent;
import com.huawei.unibi.molap.engine.wrappers.ByteArrayWrapper;
import com.huawei.unibi.molap.util.MolapUtil;

/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap Engine
 * Author K00900841
 * Created Date :21-May-2013 6:42:29 PM 
 * FileName :DataFileChunkHolder.java
 * Class Description : DataFileChunkHolder  class  
 * Version 1.0
 */
public class DataFileChunkHolder
{
    
    private static final LogService LOGGER = LogServiceFactory.getLogService(DataFileChunkHolder.class.getName());
    /**
     *  temp file 
     */
    private File inFile;

    /**
     * read stream
     */
    private DataInputStream stream;

    /**
     * entry count 
     */
    private int entryCount;

    /**
     * number record read 
     */
    private int numberOfRecordRead;

    /**
     * fileBufferSize
     */
    private int fileBufferSize;
    
    /**
     * keySize
     */
    private int keySize;
    
    /**
     * MeasureAggregator
     */
    private  MeasureAggregator[] measureAggregator;
    
    /**
     * byteArrayWrapper
     */
    private ByteArrayWrapper byteArrayWrapper;
    
    /**
     * MolapSortTempFileChunkHolder Constructor
     * 
     * @param inFile
     *          temp file 
     * @param recordSize
     *          measure count 
     * @param keySize
     *          mdkey length 
     *
     */
    public DataFileChunkHolder(File inFile, int keySize, MeasureAggregator[] measureAggregator, int fileBufferSize)
    {
        // set temp file 
        this.inFile = inFile;
        // key size
        this.keySize=keySize;
        // measure aggregator
        this.measureAggregator = measureAggregator;
        //fileBufferSize
        this.fileBufferSize=fileBufferSize;
        // byte array wrapper
        this.byteArrayWrapper= new ByteArrayWrapper(); 
        // out stream
    }
        

    /**
     * This method will be used to initialize 
     * 
     * @throws MolapSortKeyAndGroupByException
     *          problem while initializing 
     */
    public void initialize() throws MolapPaginationException
    {
        // file holder 
        long length = 0;  
        FileInputStream in = null;
        try
        {
            // create reader stream
            in = new FileInputStream(this.inFile);
            FileChannel channel = in.getChannel();
            length = channel.size();
            long position = channel.position();
            channel.position(length-4);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            channel.read(buffer);
            buffer.rewind();
//            this.stream = new DataInputStream( new BufferedInputStream(
//                    new FileInputStream( this.inFile),this.fileBufferSize));
            // read enrty count;
            this.entryCount= buffer.getInt();
            channel.position(position);
//            channel.close();
            this.stream = new DataInputStream(new BufferedInputStream(in,this.fileBufferSize));
            readRow();
        }
        catch(Exception e)
        {  
            MolapUtil.closeStreams(in);               
            MolapUtil.closeStreams(stream); 
            throw new MolapPaginationException(" Problem while reading" + this.inFile + length, e);
        }
    }

    /**
     * This method will be used to read new row from file 
     * 
     * @throws MolapSortKeyAndGroupByException
     *          problem while reading 
     *
     */
    public void readRow() throws MolapPaginationException
    {
        try
        {
            byte[] mdKey = new byte[this.keySize];
            if(this.stream.read(mdKey)<0)
            {
                LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, "Problem while reading mdkey from pagination temp file");
            }
            this.byteArrayWrapper.setMaskedKey(mdKey);
            for(int i = 0;i <  this.measureAggregator.length;i++)
            {
                this.measureAggregator[i].readData(this.stream);
            }
            numberOfRecordRead++;
        }
        catch(FileNotFoundException e)
        {
            throw new MolapPaginationException(this.inFile + " No Found ", e);
        }
        catch(IOException e)
        {
            throw new MolapPaginationException(" Problem while reading" + this.inFile, e);
        }
    }

    /**
     * below method will be used to check whether any more records are present in file or not
     * 
     * @return more row present in file 
     *
     */
    public boolean hasNext()
    {
        return this.numberOfRecordRead < this.entryCount;
    }
    
    /**
     * below method will be used to get the row 
     * 
     * @return row
     *
     */
    public byte[] getRow()
    {
        return this.byteArrayWrapper.getMaskedKey();
    }
    
    /**
     * This method will return the key array
     * 
     * @return
     *
     */
    public byte[] getKey()
    {
        return this.byteArrayWrapper.getMaskedKey();
    }

    /**
     * Below method will be used to close stream
     */
    public void closeStream()
    {
        MolapUtil.closeStreams(this.stream);
    }
    
    public void setMeasureAggs(MeasureAggregator[] aggs)
    {
        this.measureAggregator = aggs;
    }
    
    /**
     * This method will return the data array
     * 
     * @param index
     * @return
     *
     */
    public MeasureAggregator[] getMeasures() 
    {
        return this.measureAggregator;
    }
    
}
