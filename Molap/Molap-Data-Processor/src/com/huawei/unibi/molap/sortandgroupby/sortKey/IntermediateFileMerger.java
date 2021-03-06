/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcfJtSMNYgnOYiEQwbS13nxM8hk/dmbY4B4u+tG
aRAl/tuwMPqocmSoNUH09B680WgNtQ0QReGSY/IYAE2a7JlLgUzquecsikgnIYbRgarkIY2J
FMZI9M9SKK453MGJagXlh8SqmppN9M7QRRkCkziEBjVRkhBU9+0WHqY4R4OQDA==*/
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
package com.huawei.unibi.molap.sortandgroupby.sortKey;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.engine.aggregator.MeasureAggregator;
import com.huawei.unibi.molap.sortandgroupby.exception.MolapSortKeyAndGroupByException;
import com.huawei.unibi.molap.util.MolapDataProcessorLogEvent;
import com.huawei.unibi.molap.util.MolapUtil;
import com.huawei.unibi.molap.util.MolapUtilException;

/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap Data Processor 
 * Author K00900841
 * Created Date :21-May-2013 6:42:29 PM 
 * FileName :IntermediateFileMerger.java
 * Class Description : Thread class to merge sorted intermediate files  
 * Version 1.0
 */

public class IntermediateFileMerger implements Callable<Void>
{

    /**
     * LOGGER
     */
    private static final LogService FILEMERGERLOGGER = LogServiceFactory
            .getLogService(IntermediateFileMerger.class.getName());

    /**
     * recordHolderHeap
     */
    private AbstractQueue<MolapSortTempFileChunkHolder> recordHolderHeap;

    /**
     * measure count
     */
    private int measureCount;

    /**
     * mdKeyLenght
     */
    private int mdKeyLength;

    /**
     * intermediateFiles
     */
    private File[] intermediateFiles;

    /**
     * outFile
     */
    private File outFile;

    /**
     * fileCounter
     */
    private int fileCounter;

    /**
     * mdkeyIndex
     */
    private int mdKeyIndex;

    /**
     * fileBufferSize
     */
    private int fileReadBufferSize;
    
    /**
     * fileWriteSize
     */
    private int fileWriteBufferSize;

    /**
     * stream
     */
    private DataOutputStream stream;

    /**
     * totalNumberOfRecords
     */
    private int totalNumberOfRecords;
    
    /**
     * isRenamingRequired
     */
    private boolean isRenamingRequired;
    
    /**
     * isFactMdkeyInInputRow
     */
    private boolean isFactMdkeyInInputRow;
    
    /**
     * factMdkeyLength
     */
    private int factMdkeyLength;
    
    /**
     * sortTempFileNoOFRecordsInCompression
     */
    private int sortTempFileNoOFRecordsInCompression;
    
    /**
     * isSortTempFileCompressionEnabled
     */
    private boolean isSortTempFileCompressionEnabled;
    
    /**
     * records
     */
    private Object[][] records;
    
    /**
     * entryCount
     */
    private int entryCount;
    
    /**
     * writer
     */
    private MolapSortTempFileWriter writer;

    /**
     * type
     */
    private char[] type;

    /**
     * prefetch
     */
    private boolean prefetch;

    /**
     * prefetchBufferSize
     */
    private int prefetchBufferSize;
    
    /**
     * totalSize
     */
    private int totalSize;
    
    private String[] aggregator;

    /**
     * IntermediateFileMerger Constructor
     * 
     * @param intermediateFiles
     *            intermediateFiles
     * @param fileBufferSize
     *            fileBufferSize
     * @param measureCount
     *            measureCount
     * @param mdKeyLength
     *            mdKeyLength
     * @param outFile
     *            outFile
     */
    public IntermediateFileMerger(File[] intermediateFiles,
            int fileReadBufferSize, int measureCount, int mdKeyLength,
            File outFile, int mdkeyIndex, int fileWriteBufferSize,
            boolean isRenamingRequired, boolean isFactMdkeyInInputRow,
            int factMdkeyLength, int sortTempFileNoOFRecordsInCompression,
            boolean isSortTempFileCompressionEnabled, char[] type,boolean prefetch,
            int prefetchBufferSize, String[] aggregator)
    {
        this.intermediateFiles = intermediateFiles;
        this.measureCount = measureCount;
        this.mdKeyLength = mdKeyLength;
        this.outFile = outFile;
        this.fileCounter = intermediateFiles.length;
        this.mdKeyIndex = mdkeyIndex;
        this.fileReadBufferSize = fileReadBufferSize;
        this.fileWriteBufferSize=fileWriteBufferSize;
        this.isRenamingRequired=isRenamingRequired;
        this.isFactMdkeyInInputRow=isFactMdkeyInInputRow;
        this.factMdkeyLength=factMdkeyLength;
        this.sortTempFileNoOFRecordsInCompression=sortTempFileNoOFRecordsInCompression;
        this.isSortTempFileCompressionEnabled=isSortTempFileCompressionEnabled;
        this.type=type;
        this.prefetch=prefetch;
        this.prefetchBufferSize=prefetchBufferSize;
        this.aggregator = aggregator;
    }

    @Override
    public Void call() throws Exception
    {
        boolean isFailed=false;
        try
        {
            startSorting();
            initialize();
            //CHECKSTYLE:OFF    Approval No:Approval-367
            while(hasNext())
            {
              //CHECKSTYLE:ON
                writeDataTofile(next());
            }
            if(isSortTempFileCompressionEnabled || prefetch)
            {
                if(entryCount>0)
                {
                    if(entryCount<totalSize)
                    {
                        Object[][] tempArr = new Object[entryCount][];
                        System.arraycopy(records, 0, tempArr, 0, entryCount);
                        records=tempArr;
                        this.writer.writeSortTempFile(tempArr);
                    }
                    else
                    {
                        this.writer.writeSortTempFile(records);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            FILEMERGERLOGGER.error(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    ex, "Problem while intermediate merging");
            isFailed=true;
        }
        finally
        {
        	MolapUtil.closeStreams(this.stream);
            records= null;
            if(null!=writer)
            {
                writer.finish();
            }
            if(!isFailed)
            {
                try
                {
                    finish();
                }
                catch (MolapSortKeyAndGroupByException e)
                {
                    FILEMERGERLOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, e, "Problem while deleting the merge file");
                }
            }
            else
            {
                if(this.outFile.delete())
                {
                    FILEMERGERLOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, "Problem while deleting the merge file");
                }
            }
            
        }
        
        if(this.isRenamingRequired)
        {
        	String destFileName = this.outFile.getAbsolutePath();
        	String[] split = destFileName.split(MolapCommonConstants.BAK_EXT);
        	File renamed = new File(split[0]);
        	if(!this.outFile.renameTo(renamed))
        	{
        		FILEMERGERLOGGER.error(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, "Problem while renaming the checkpoint file");
        	}
        }
        return null;
    }

    /**
     * This method is responsible for initialising the out stream
     * @throws MolapSortKeyAndGroupByException
     */
    private void initialize() throws MolapSortKeyAndGroupByException
    {
        if(!isSortTempFileCompressionEnabled && !prefetch)
        {
            try
            {
                this.stream = new DataOutputStream(new BufferedOutputStream(
                        new FileOutputStream(this.outFile), this.fileWriteBufferSize));
                this.stream.writeInt(this.totalNumberOfRecords);
            }
            catch(FileNotFoundException e)
            {
                throw new MolapSortKeyAndGroupByException(
                        "Problem while getting the file", e);
            }
            catch(IOException e)
            {
                throw new MolapSortKeyAndGroupByException(
                        "Problem while writing the data to file", e);
            }
        }
        else if(prefetch && !isSortTempFileCompressionEnabled)
        {
            writer = new MolapUnCompressedSortTempFileWriter(measureCount,
                    mdKeyIndex, mdKeyLength, isFactMdkeyInInputRow,
                    factMdkeyLength, fileWriteBufferSize, type);
            totalSize = prefetchBufferSize;
            writer.initiaize(outFile, totalNumberOfRecords);
        }
        else
        {
            writer = new MolapCompressedSortTempFileWriter(measureCount,
                    mdKeyIndex, mdKeyLength, isFactMdkeyInInputRow,
                    factMdkeyLength, fileWriteBufferSize, type);
            totalSize = sortTempFileNoOFRecordsInCompression;
            writer.initiaize(outFile, totalNumberOfRecords);
        }
        
    }

    /**
     * This method will be used to get the sorted record from file
     * 
     * @return sorted record sorted record
     * @throws MolapSortKeyAndGroupByException
     * 
     */
    private Object[] getSortedRecordFromFile()
            throws MolapSortKeyAndGroupByException
    {
        Object[] row = null;
        // poll the top object from heap
        // heap maintains binary tree which is based on heap condition that will
        // be based on comparator we are passing the heap
        // when will call poll it will always delete root of the tree and then
        // it does trickel down operation complexity is log(n)
        MolapSortTempFileChunkHolder chunkHolderPoll = this.recordHolderHeap.poll();
        // get the row from chunk
        row = chunkHolderPoll.getRow();
        // check if there no entry present
        if(!chunkHolderPoll.hasNext())
        {
            // if chunk is empty then close the stream
            chunkHolderPoll.closeStream();
            // change the file counter
            --this.fileCounter;
            // reaturn row
            return row;
        }
        // read new row
        chunkHolderPoll.readRow();
        // add to heap
        this.recordHolderHeap.add(chunkHolderPoll);
        // return row
        return row;
    }

    /**
     * Below method will be used to start storing process This method will get
     * all the temp files present in sort temp folder then it will create the
     * record holder heap and then it will read first record from each file and
     * initialize the heap
     * 
     * @throws MolapSortKeyAndGroupByException
     * 
     */
    private void startSorting() throws MolapSortKeyAndGroupByException
    {
        FILEMERGERLOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "Number of temp file: " + this.fileCounter);
        // create record holder heap
        createRecordHolderQueue(this.intermediateFiles);
        // iterate over file list and create chunk holder and add to heap
        FILEMERGERLOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "Started adding first record from each file");
        MolapSortTempFileChunkHolder molapSortTempFileChunkHolder = null;
      //CHECKSTYLE:OFF    Approval No:Approval-367
        for(File tempFile : this.intermediateFiles)
        {
          //CHECKSTYLE:ON
            // create chunk holder
            molapSortTempFileChunkHolder = new MolapSortTempFileChunkHolder(
                    tempFile, this.measureCount, this.mdKeyLength,
                    this.fileReadBufferSize, this.isFactMdkeyInInputRow,this.factMdkeyLength, this.aggregator);
            // initialize
            molapSortTempFileChunkHolder.initialize();
            molapSortTempFileChunkHolder.readRow();
            this.totalNumberOfRecords += molapSortTempFileChunkHolder
                    .getEntryCount();
            // add to heap
            this.recordHolderHeap.add(molapSortTempFileChunkHolder);
        }
        FILEMERGERLOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                "Heap Size" + this.recordHolderHeap.size());
    }

    /**
     * This method will be used to create the heap which will be used to hold
     * the chunk of data
     * 
     * @param listFiles
     *            list of temp files
     * 
     */
    private void createRecordHolderQueue(File[] listFiles)
    {
        // creating record holder heap
        this.recordHolderHeap = new PriorityQueue<MolapSortTempFileChunkHolder>(
                listFiles.length,
                new Comparator<MolapSortTempFileChunkHolder>()
                {
                    public int compare(MolapSortTempFileChunkHolder r1,
                            MolapSortTempFileChunkHolder r2)
                    {
                        byte[] b1 = (byte[])r1.getRow()[mdKeyIndex];
                        byte[] b2 = (byte[])r2.getRow()[mdKeyIndex];
                        int cmp = 0;
//                        int length = b1.length < b2.length ? b1.length
//                                : b2.length;

                        for(int i = 0;i < mdKeyLength;i++)
                        {
                          //CHECKSTYLE:OFF    Approval No:Approval-367
                            int a = b1[i] & 0xFF;
                            int b = b2[i] & 0xFF;
                          //CHECKSTYLE:ON
                            cmp = a - b;
                            if(cmp != 0)
                            {
                                return cmp;
                            }
                        }
                        return cmp;
                    }
                });
    }

    /**
     * This method will be used to get the sorted row
     * 
     * @return sorted row
     * @throws MolapSortKeyAndGroupByException
     * 
     */
    private Object[] next() throws MolapSortKeyAndGroupByException
    {
        return getSortedRecordFromFile();
    }

    /**
     * This method will be used to check whether any more element is present or
     * not
     * 
     * @return more element is present
     * 
     */
    private boolean hasNext()
    {
        return this.fileCounter > 0;
    }
    
    //TODO SIMIAN
    /**
     * Below method will be used to write data to file
     * 
     * @throws MolapSortKeyAndGroupByException
     *             problem while writing
     * 
     */
    private void writeDataTofile(Object[] row)
            throws MolapSortKeyAndGroupByException
    {
        if(isSortTempFileCompressionEnabled || prefetch)
        {
            if(entryCount==0)
            {
                records = new Object[totalSize][];
                records[entryCount++]=row;
                return;
            }
            records[entryCount++]=row;
            if(entryCount==totalSize)
            {
            	entryCount=0;
                this.writer.writeSortTempFile(records);
                records = new Object[totalSize][];
            }
            return;
        }
        try
        {
            int aggregatorIndexInRowObject = 0;
            // get row from record holder list
            MeasureAggregator[] aggregator = (MeasureAggregator[])row[aggregatorIndexInRowObject];
            for(int j = 0;j < aggregator.length;j++)
            {
                byte[] byteArray = aggregator[j].getByteArray();
                stream.writeInt(byteArray.length);
                stream.write(byteArray);
            }
            stream.writeDouble((Double)row[this.mdKeyIndex - 1]);
            // write mdkye
            stream.write((byte[])row[this.mdKeyIndex]);
            if(this.isFactMdkeyInInputRow)
            {
                stream.write((byte[])row[row.length - 1]);
            }
            // write measure value first
//            for(int m = 0;m < measureCount;m++)	
//            {
//                if(type[m]!='c')
//                {
//                    // check if row is null or not
//                    if(null != row[m])
//                    {
//                        // if not null than write 1 byte with value 1 to
//                        // check
//                        // whether that measure value is null or not
//                        stream.write(1);
//                        // than write measure value
//                        stream.writeDouble((Double)row[m]);
//                    }
//                    else
//                    {
//                        // other wise write 0 for null check
//                        stream.write(0);
//                        stream.writeDouble(0.0);
//                    }
//                }
//                else
//                {
//                    stream.writeInt(((byte[])row[m]).length);
//                    stream.write((byte[])row[m]);
//                }
//                // write mdkye
//            }
//            stream.write((byte[])row[this.mdKeyIndex]);
//            if(this.isFactMdkeyInInputRow)
//            {
//                stream.write((byte[])row[row.length-1]);
//            }
        }
        catch(IOException e)
        {
            throw new MolapSortKeyAndGroupByException(
                    "Problem while writing the file", e);
        }
    }
    
    private void finish() throws MolapSortKeyAndGroupByException
    {
        if(recordHolderHeap!=null)
        {
            int size = recordHolderHeap.size();
            for(int i = 0;i < size;i++)
            {
                recordHolderHeap.poll().closeStream();
            }
        }
        try
        {
            MolapUtil.deleteFiles(this.intermediateFiles);
        }
        catch(MolapUtilException e)
        {
           throw new MolapSortKeyAndGroupByException("Problem while deleting the intermediate files");
        }
    }
}
