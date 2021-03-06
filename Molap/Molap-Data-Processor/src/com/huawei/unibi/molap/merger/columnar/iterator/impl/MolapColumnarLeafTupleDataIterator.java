package com.huawei.unibi.molap.merger.columnar.iterator.impl;

import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionModel;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFile;
import com.huawei.unibi.molap.engine.columnar.keyvalue.AbstractColumnarScanResult;
import com.huawei.unibi.molap.factreader.FactReaderInfo;
import com.huawei.unibi.molap.factreader.MolapSurrogateTupleHolder;
import com.huawei.unibi.molap.factreader.columnar.MolapColumnarLeafNodeIterator;
import com.huawei.unibi.molap.iterator.MolapIterator;
import com.huawei.unibi.molap.merger.columnar.iterator.MolapDataIterator;
import com.huawei.unibi.molap.util.ValueCompressionUtil;


public class MolapColumnarLeafTupleDataIterator implements MolapDataIterator<MolapSurrogateTupleHolder>
{
    
    /**
     * number of keys in one leaf node
     */

    /**
     * unique value if slice
     */
    private double[] uniqueValue;

    /**
     * hash next
     */
    private boolean hasNext;

    /**
     * leaf node iterator
     */
    private MolapIterator<AbstractColumnarScanResult> leafNodeIterator;
    
    /**
     * measureCount
     */
    private int measureCount;
    
    /**
     * aggType
     */
    private char [] aggType;
    
    /**
     * keyValue
     */
    private AbstractColumnarScanResult keyValue;
    
    /**
     * rowCounter
     */
    
    /**
     * tuple
     */
    private MolapSurrogateTupleHolder currentTuple;
    
    /**
     * isMeasureUpdateResuired
     */
    private boolean isMeasureUpdateResuired;

    /**
     * MolapSliceTupleIterator constructor to initialise
     * 
     * @param sliceModel
     *            slice model which will hold the slice information
     * @param mdkeyLength
     *            mdkey length
     * @param measureCount
     *            measure count
     */
    public MolapColumnarLeafTupleDataIterator(String sliceLocation, MolapFile[] factFiles,FactReaderInfo factItreatorInfo, int mdkeyLength)
    {
        this.measureCount=factItreatorInfo.getMeasureCount();
        ValueCompressionModel compressionModelObj = getCompressionModel(sliceLocation,
                factItreatorInfo.getTableName(), measureCount);
        this.uniqueValue=compressionModelObj.getUniqueValue();
        this.leafNodeIterator = new MolapColumnarLeafNodeIterator(factFiles,mdkeyLength,compressionModelObj,factItreatorInfo);
        this.aggType=compressionModelObj.getType();
        initialise();
        this.isMeasureUpdateResuired=factItreatorInfo.isUpdateMeasureRequired();
    }
    
    /**
     * below method will be used to initialise
     */
    private void initialise()
    {
        if(this.leafNodeIterator.hasNext())
        {
            keyValue=leafNodeIterator.next();
            this.hasNext=true;
        }
    }

    /**
     * This method will be used to get the compression model for slice
     * 
     * @param path
     *          slice path
     * @param measureCount
     *          measure count
     * @return compression model
     *
     */
    private ValueCompressionModel getCompressionModel(String sliceLocation,String tableName, int measureCount)
    {
        ValueCompressionModel compressionModelObj = ValueCompressionUtil
                .getValueCompressionModel(sliceLocation
                        + MolapCommonConstants.MEASURE_METADATA_FILE_NAME
                        + tableName
                        + MolapCommonConstants.MEASUREMETADATA_FILE_EXT,
                        measureCount);
        return compressionModelObj;
    }

    /**
     * below method will be used to get the measure value from measure data
     * wrapper
     * 
     * @return
     */
    private Object[] getMeasure()
    {
        Object[] measures = new Object[measureCount];
        double values=0;
        for(int i = 0;i < measures.length;i++)
        {
            if(aggType[i]=='n')
            {
                values = keyValue.getNormalMeasureValue(i);
                if(this.isMeasureUpdateResuired && values != uniqueValue[i])
                {
                    measures[i] = values;
                }
            }
            else
            {
                measures[i] = keyValue.getCustomMeasureValue(i);
            }
        }
        return measures;
    }

    @Override
    public boolean hasNext()
    {
        return hasNext;
    }

    @Override
    public void fetchNextData()
    {
        MolapSurrogateTupleHolder tuple = new MolapSurrogateTupleHolder();
        tuple.setSurrogateKey(keyValue.getKeyArray());
        tuple.setMeasures(getMeasure());
        if(keyValue.hasNext())
        {
             this.currentTuple=tuple;
        }
        else if(!leafNodeIterator.hasNext())
        {
            hasNext = false;
        }
        else
        {
            initialise();
        }
        this.currentTuple=tuple;
    }

    @Override
    public MolapSurrogateTupleHolder getNextData()
    {
        return this.currentTuple;
    }
}
