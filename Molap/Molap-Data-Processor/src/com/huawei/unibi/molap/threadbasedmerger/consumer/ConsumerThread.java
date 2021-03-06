/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcfJtSMNYgnOYiEQwbS13nxM8hk/dmbY4B4u+tG
aRAl/uuyIii2kxoHP4E3CI7RCeEqpOT/7rfB3ypLUrTx51VVplzlYT50hS2m5gA90bn4f/rh
NlWN5qRhkM2cn0nfDrb1hdMkhOwGoBeDYv/2aoPzeTQdPw5UkR1UGMLggPtEtg==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2014
 * =====================================
*/

package com.huawei.unibi.molap.threadbasedmerger.consumer;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.sortandgroupby.sortKey.MolapSortKeyException;
import com.huawei.unibi.molap.threadbasedmerger.container.Container;
import com.huawei.unibi.molap.threadbasedmerger.iterator.RecordIterator;
import com.huawei.unibi.molap.util.MolapDataProcessorLogEvent;

/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap Data Processor
 * Author K00900841
 * Created Date :10-June-2014 6:42:29 PM
 * FileName : ConsumerThread.java
 * Class Description : Consumer Thread class 
 * Version 1.0
 */
public class ConsumerThread implements Callable<Void>
{
    /**
     * LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory.getLogService(ConsumerThread.class.getName());
    /**
     * list of producer container
     */
    private List<Container> producerContainer = new ArrayList<Container>(0);

    /**
     * recordHolderHeap
     */
    private AbstractQueue<RecordIterator> recordHolderHeap;

    /**
     * current buffer
     */
    private Object[][] currentBuffer;

    /**
     * backup buffer
     */
    private Object[][] backupBuffer;

    /**
     * read buffer size
     */
    private int readBufferSize;

    /**
     * producer counter
     */
    private int producerCounter;

    /**
     * molapSortHolderContainer
     */
    private final Container container;

    /**
     * is current filled to check current buffer is filled or not 
     */
    private boolean isCurrentFilled;

    /**
     * consumer number
     */
    private int counter;
    
    /**
     * mdKeyIndex
     */
    private int mdKeyIndex;

    /**
     * ConsumerThread
     * 
     * @param producerContainer
     * @param readBufferSize
     * @param container
     * @param counter
     */
    public ConsumerThread(List<Container> producerContainer,
            int readBufferSize, Container container, int counter, int mdKeyIndex)
    {
        this.producerContainer = producerContainer;
        this.readBufferSize = readBufferSize;
        this.container = container;
        this.counter = counter;
        this.mdKeyIndex=mdKeyIndex;
    }

    @Override
    public Void call() throws Exception
    {
        RecordIterator[] iterators = new RecordIterator[producerContainer
                .size()];
        int i = 0;
      //CHECKSTYLE:OFF    Approval No:Approval-V3R8C00_015
        for(Container container : producerContainer)
        {
            iterators[i++] = new RecordIterator(container);
        }
        
      //CHECKSTYLE:ON
        createRecordHolderQueue(iterators);
        try
        {
            initialiseHeap(iterators);
        }
        catch(MolapSortKeyException e)
        {
            LOGGER.error(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,e,
                    "Problem while initialising the heap");
        }
        try
        {
            fillBuffer(false);
            isCurrentFilled = true;
        }
        catch(MolapSortKeyException e1)
        {
            LOGGER.error(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,e1,
                    "Problem while Filling the buffer");
        }
        try
        {
        while(producerCounter > 0 || isCurrentFilled)
        {
            this.container.fillContainer(currentBuffer);
            isCurrentFilled = false;
                synchronized(this.container)
                {
                    this.container.setFilled(true);
                    fillBuffer(true);
                    this.container.wait();
                }
                currentBuffer = backupBuffer;
                if(currentBuffer != null)
                {
                    isCurrentFilled = true;
                }
                else
                {
                    isCurrentFilled = false;
                }
        }
        }
        catch(InterruptedException e)
        {
        	LOGGER.error(
        			MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,e);
        }
        catch(Exception e)
        {
        	LOGGER.error(
        			MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,e);
        }
        LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, "Consumer Thread: " + this.counter + ": Done");
        this.container.setDone(true);
        return null;
    }

    private void initialiseHeap(RecordIterator[] iterators)
            throws MolapSortKeyException
    {
      //CHECKSTYLE:OFF    Approval No:Approval-V3R8C00_016
        for(RecordIterator iterator : iterators)
        {
            if(iterator.hasNext())
            {
                this.recordHolderHeap.add(iterator);
                producerCounter++;
            }
        }
        //CHECKSTYLE:ON
    }

    /**
     * This method will be used to get the sorted record from file
     * 
     * @return sorted record sorted record
     * @throws MolapSortKeyException
     * 
     */
    private void fillBuffer(boolean isBackupFilling)
            throws MolapSortKeyException
    {
        if(producerCounter < 1)
        {
            if(isBackupFilling)
            {
                backupBuffer = null;
            }
            else
            {
                currentBuffer = null;
            }
            return;
        }
        Object[][] sortRecordHolders = new Object[readBufferSize][];
        int counter = 0;
        Object[] row = null;
        RecordIterator poll = null;
        while(counter < readBufferSize && producerCounter > 0)
        {
            // poll the top object from heap
            // heap maintains binary tree which is based on heap condition that
            // will
            // be based on comparator we are passing the heap
            // when will call poll it will always delete root of the tree and
            // then
            // it does trickel down operation complexity is log(n)
        	poll = this.recordHolderHeap.poll();
            row = poll.getRow();
            poll.next();
            // get the row from chunk
            // check if there no entry present
            if(!poll.hasNext())
            {
                sortRecordHolders[counter++] = row;
                --producerCounter;
                continue;
            }
            sortRecordHolders[counter++] = row;
            this.recordHolderHeap.add(poll);
        }
        // return row
        if(counter < readBufferSize)
        {
            Object[][] temp = new Object[counter][];
            System.arraycopy(sortRecordHolders, 0, temp, 0, temp.length);
//            for(int i = 0;i < temp.length;i++)
//            {
//                temp[i]=sortRecordHolders[i];
//            }
            sortRecordHolders = temp;
        }
        if(isBackupFilling)
        {
            backupBuffer = sortRecordHolders;
        }
        else
        {
            currentBuffer = sortRecordHolders;
        }
    }

    /**
     * This method will be used to create the heap which will be used to hold
     * the chunk of data
     * 
     * @param listFiles
     *            list of temp files
     * 
     */
    private void createRecordHolderQueue(RecordIterator[] iterators)
    {
        // creating record holder heap
        this.recordHolderHeap = new PriorityQueue<RecordIterator>(
                iterators.length, new Comparator<RecordIterator>()
                {
                    public int compare(RecordIterator r1, RecordIterator r2)
                    {
                        byte[] b1 = (byte[])r1.getRow()[mdKeyIndex];
                        byte[] b2 = (byte[])r2.getRow()[mdKeyIndex];
                        int cmp = 0;
                        int a = 0;
                        int b = 0;
                        for(int i = 0;i < b2.length;i++)
                        {
                            a = b1[i] & 0xFF;
                            b = b2[i] & 0xFF;
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

}
