package com.huawei.unibi.molap.engine.merger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFile;
import com.huawei.unibi.molap.engine.aggregator.util.AggUtil;
//import com.huawei.unibi.molap.engine.executer.calcexp.MolapCalcFunction;
import com.huawei.unibi.molap.engine.merger.exception.ResultMergerException;
import com.huawei.unibi.molap.engine.processor.DataProcessor;
import com.huawei.unibi.molap.engine.processor.exception.DataProcessorException;
import com.huawei.unibi.molap.engine.reader.ResultTempFileReader;
import com.huawei.unibi.molap.engine.reader.exception.ResultReaderException;
import com.huawei.unibi.molap.engine.schema.metadata.DataProcessorInfo;
import com.huawei.unibi.molap.engine.util.MolapEngineLogEvent;
import com.huawei.unibi.molap.util.MolapUtil;

/**
 * 
 * Project Name  : Carbon 
 * Module Name   : MOLAP Data Processor
 * Author    : R00903928,k00900841
 * Created Date  : 27-Aug-2015
 * FileName   : UnSortedResultMerger.java
 * Description   : This class is responsible for handling the merge of the
 *         result data present in the intermediate temp files in an unsorted
 *         order. This will use arraylist to hold all data and process it
 *         sequentially.
 * Class Version  : 1.0
 */
public class UnSortedResultMerger implements Callable<Void>
{
    /**
     * LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory.getLogService(UnSortedResultMerger.class
            .getName());

    /**
     * dataProcessorInfo
     */
    private DataProcessorInfo dataProcessorInfo;

	/**
     * List to hold all the temp files.
     */
    private List<ResultTempFileReader> recordHolderList;
    
    /**
     * dataProcessor This is the data processor object which will process the data.
     */
    private DataProcessor dataProcessor;
    
    /**
     * files Temp files.
     */
    private MolapFile[] files;

    /**
     * 
     * @param dataProcessor
     * @param dataProcessorInfo
     * @param files
     */
    public UnSortedResultMerger(DataProcessor dataProcessor, DataProcessorInfo dataProcessorInfo,MolapFile[] files)
    {
        this.dataProcessorInfo = dataProcessorInfo;
        this.recordHolderList = new ArrayList<ResultTempFileReader>(files.length);
        this.dataProcessor=dataProcessor;
        this.files=files;
    }

    /**
     * 
     * @see java.util.concurrent.Callable#call()
     * 
     */
    @Override
    public Void call() throws Exception
    {
        try
        {
            this.dataProcessor.initialise(dataProcessorInfo);
            
            // For each intermediate result file.
            for(MolapFile file : files)
            {
                // reads the temp files and creates ResultTempFileReader object.
                ResultTempFileReader molapSortTempFileChunkHolder = new ResultTempFileReader(file.getAbsolutePath(),
                        dataProcessorInfo.getKeySize(), AggUtil.getAggregators(dataProcessorInfo.getAggType(), false, dataProcessorInfo.getKeyGenerator(), dataProcessorInfo
                                .getCubeUniqueName(),dataProcessorInfo.getMsrMinValue(),dataProcessorInfo.getHighCardinalityTypes()),
                                dataProcessorInfo.getFileBufferSize());
                // initialize
                molapSortTempFileChunkHolder.initialize();
                // add to list
                this.recordHolderList.add(molapSortTempFileChunkHolder);
            }
			// iterate through list and for each file process the each row of data.
            writeRecordToFile();
        }
        catch(ResultReaderException e)
        {
            LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, e);
            throw new ResultMergerException(e);
        }
        finally
        {
            dataProcessor.finish();
			 //delete the temp files.
            MolapUtil.deleteFoldersAndFiles(files);
        }
        return null;
    }


    /**
     * This method will be used to get the record from file
     * 
     * @throws ResultMergerException
     * 
     */
    private void writeRecordToFile() throws ResultMergerException
    {
		// for each file.
        for(ResultTempFileReader poll : this.recordHolderList)
        {      //CHECKSTYLE:OFF    Approval No:Approval-V1R2C10_007
            try
            {
                // for each entry in the file.
                while(poll.hasNext())
                {
                    poll.readRow();
                    // process each data to the data processor.
                    dataProcessor.processRow(poll.getKey(), poll.getMeasures());

                poll.setMeasureAggs(AggUtil.getAggregators(dataProcessorInfo.getAggType(), false, dataProcessorInfo.getKeyGenerator(), dataProcessorInfo
                        .getCubeUniqueName(),dataProcessorInfo.getMsrMinValue(),dataProcessorInfo.getHighCardinalityTypes()));
                }
            }
            catch(DataProcessorException e)
            {
                throw new ResultMergerException(e);
            }
            catch(ResultReaderException e)
            {
                throw new ResultMergerException(e);
            }
            finally
            {
                poll.closeStream();
            }
        }//CHECKSTYLE:ON
  }
}
