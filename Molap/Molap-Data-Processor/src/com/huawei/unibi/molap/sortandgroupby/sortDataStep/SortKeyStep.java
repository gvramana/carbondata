/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwcfJtSMNYgnOYiEQwbS13nxM8hk/dmbY4B4u+tG
aRAl/uyw4g3oOJExnIih+scXjVBXXWxOJooJLiCQ2G9sO7HYCzcqzOeer8GZVZ/AhSy70OxO
EcQuiTHPrefC8/ba3um1fpUYZ2R3cDBRjrwOzo6mlirLR+bv+aWnjsF5qgyVrg==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
 */

package com.huawei.unibi.molap.sortandgroupby.sortDataStep;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.iweb.platform.logging.impl.StandardLogService;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.schema.metadata.SortObserver;
import com.huawei.unibi.molap.sortandgroupby.exception.MolapSortKeyAndGroupByException;
import com.huawei.unibi.molap.sortandgroupby.sortData.SortDataRows;
import com.huawei.unibi.molap.util.MolapDataProcessorLogEvent;
import com.huawei.unibi.molap.util.RemoveDictionaryUtil;

/**
 * Project Name 	: Carbon 
 * Module Name 		: MOLAP Data Processor
 * Author 			: Suprith T 72079 
 * Created Date 	: 25-Aug-2015
 * FileName 		: SortKeyStep.java
 * Description 		: Kettle step to sort data
 * Class Version 	: 1.0
 */
public class SortKeyStep extends BaseStep
{
    
    /**
     * LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(SortKeyStep.class.getName());

    /**
     * MolapSortKeyAndGroupByStepData
     */
    private SortKeyStepData data;

    /**
     * MolapSortKeyAndGroupByStepMeta
     */
    private SortKeyStepMeta meta;

    /**
     * molapSortKeys
     */
    private SortDataRows sortDataRows;

    /**
     * rowCounter
     */
    private long readCounter;
    
    /**
     * writeCounter
     */
    private long writeCounter;
    
    /**
     * logCounter
     */
    private int logCounter;
    
    /**
     * observer
     */
    private SortObserver observer; 
    
    /**
     * MolapSortKeyAndGroupByStep Constructor
     * 
     * @param stepMeta
     * @param stepDataInterface
     * @param copyNr
     * @param transMeta
     * @param trans
     *
     */
    public SortKeyStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
            TransMeta transMeta, Trans trans)
    {
        super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

    /**
     * Perform the equivalent of processing one row. Typically this means
     * reading a row from input (getRow()) and passing a row to output
     * (putRow)).
     * 
     * @param smi
     *            The steps metadata to work with
     * @param sdi
     *            The steps temporary working data to work with (database
     *            connections, result sets, caches, temporary variables, etc.)
     * @return false if no more rows can be processed or an error occurred.
     * @throws KettleException
     */
    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
    {
        // get step meta 
        this.meta = ((SortKeyStepMeta)smi);
        StandardLogService.setThreadName(StandardLogService.getPartitionID(this.meta.getCubeName()), null);
        // get step data 
        this.data = ((SortKeyStepData)sdi);
        
        // get row 
        Object[] row = getRow();
        
        // create sort observer
        this.observer= new SortObserver();
        
        // if row is null then this step can start processing the data 
        if(row == null)
        {
            return processRowToNextStep();
        }
        
        // check if all records are null than send empty row to next step
        else if(RemoveDictionaryUtil.checkAllValuesForNull(row))
        {
            // create empty row out size 
            int outSize = Integer.parseInt(meta.getOutputRowSize());
            
            Object[] outRow = new Object[outSize];
            
            // clone out row meta 
            this.data.setOutputRowMeta((RowMetaInterface)getInputRowMeta().clone());
            
            // get all fields 
            this.meta.getFields(data.getOutputRowMeta(), getStepname(), null, null, this);
            
            LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, "Record Procerssed For table: "+ meta.getTabelName());
            LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, "Record Form Previous Step was null");
                    String logMessage= "Summary: Molap Sort Key Step: Read: " + 1 + ": Write: "+ 1;
            LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, logMessage);
            
            putRow(data.getOutputRowMeta(), outRow);
            setOutputDone();
            return false;
        }
        
        // if first 
        if(first)
        {
            first = false;
           
            // clone out row meta 
            this.data.setOutputRowMeta((RowMetaInterface)getInputRowMeta().clone());
            
            // get all fields 
            this.meta.getFields(data.getOutputRowMeta(), getStepname(), null, null, this);

			/*this.sortDataRows = new SortDataRows(meta.getTabelName(),
					meta.getDimensionCount(), meta.getMeasureCount(), this.observer, meta.getCurrentRestructNumber(), meta.getHighCardinalityCount()); */
            // TODO : send high cardinality count.
            
            this.meta.setHighCardinalityCount(RemoveDictionaryUtil.extractHighCardCount(meta.getHighCardinalityDims()));
            
            this.sortDataRows = new SortDataRows(meta.getTabelName(),
					meta.getDimensionCount() - meta.getComplexDimensionCount(), 
					meta.getComplexDimensionCount(), meta.getMeasureCount(), this.observer, meta.getCurrentRestructNumber(),meta.getHighCardinalityCount());
			try 
			{
				// initialize sort
				this.sortDataRows.initialize(meta.getSchemaName(),
						meta.getCubeName());
			}
            catch(MolapSortKeyAndGroupByException e)
            {
                throw new KettleException(e);
            }
			
            this.logCounter = Integer.parseInt(MolapCommonConstants.DATA_LOAD_LOG_COUNTER_DEFAULT_COUNTER);
        }
        
        readCounter++;
        if(readCounter % logCounter == 0)
        {
            LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, "Record Procerssed For table: " + meta.getTabelName());
            String logMessage = "Molap Sort Key Step: Record Read: " + readCounter;
            LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, logMessage);
        }
        
        try
        {
            // add row 
            this.sortDataRows.addRow(row);
        }
        catch(Throwable e)
        {
        	LOGGER.info(MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG, e);
            throw new KettleException(e);
        }
        
        return true;
    }

    /**
     * Below method will be used to process data to next step 
     * @return false is finished 
     * @throws KettleException
     */
	private boolean processRowToNextStep() throws KettleException 
    {
        if(null == this.sortDataRows)
        {
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "Record Procerssed For table: " + meta.getTabelName());
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "Number of Records was Zero");
            String logMessage = "Summary: Molap Sort Key Step: Read: " + 0
                    + ": Write: " + 0;
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    logMessage);
            putRow(data.getOutputRowMeta(), new Object[0]);
            setOutputDone();
            return false;
        }
        
        try
        {
            // start sorting
            this.sortDataRows.startSorting();
            
            // check any more rows are present 
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "Record Procerssed For table: " + meta.getTabelName());
            String logMessage = "Summary: Molap Sort Key Step: Read: "
                    + readCounter + ": Write: " + writeCounter;
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    logMessage);
            putRow(data.getOutputRowMeta(),new Object[0]);
            
            this.sortDataRows.writeMeasureMetadataFile();
            
            setOutputDone();
            return false;
        }
        catch(MolapSortKeyAndGroupByException e)
        {
            throw new KettleException(e);
        }
        
    }

//    /**
//     * Below method will be used to check whether row is empty or not 
//     * 
//     * @param row
//     * @return row empty 
//     *
//     */
//    private boolean checkAllValuesAreNull(Object[] row)
//    {
//        for(int i = 0;i < row.length;i++)
//        {
//            if(null != row[i])
//            {
//                return false;
//            }
//        }
//        return true;
//    }

    /**
     * Initialize and do work where other steps need to wait for...
     * 
     * @param smi
     *            The metadata to work with
     * @param sdi
     *            The data to initialize
     * @return step initialize or not
     */
    public boolean init(StepMetaInterface smi, StepDataInterface sdi)
    {
        this.meta = ((SortKeyStepMeta)smi);
        this.data = ((SortKeyStepData)sdi);
        return super.init(smi, sdi);
    }

    /**
     * Dispose of this step: close files, empty logs, etc.
     * 
     * @param smi
     *            The metadata to work with
     * @param sdi
     *            The data to dispose of
     */
    public void dispose(StepMetaInterface smi, StepDataInterface sdi)
    {
        this.meta = ((SortKeyStepMeta)smi);
        this.data = ((SortKeyStepData)sdi);
        this.sortDataRows = null;
        super.dispose(smi, sdi);
        this.meta= null;
        this.data= null;
    }
}