package com.huawei.unibi.molap.merger.columnar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.factreader.FactReaderInfo;
import com.huawei.unibi.molap.factreader.MolapSurrogateTupleHolder;
import com.huawei.unibi.molap.merger.columnar.iterator.MolapDataIterator;
import com.huawei.unibi.molap.merger.columnar.iterator.impl.MolapColumnarLeafTupleDataIterator;
import com.huawei.unibi.molap.merger.columnar.iterator.impl.MolapLeafTupleWrapperIterator;
import com.huawei.unibi.molap.merger.exeception.SliceMergerException;
import com.huawei.unibi.molap.schema.metadata.MolapColumnarFactMergerInfo;
import com.huawei.unibi.molap.store.MolapFactDataHandlerColumnarMerger;
import com.huawei.unibi.molap.store.MolapFactHandler;
import com.huawei.unibi.molap.store.writer.exception.MolapDataWriterException;
import com.huawei.unibi.molap.util.MolapSliceAndFiles;


public abstract class ColumnarFactFileMerger
{

    /**
     * otherMeasureIndex
     */
    protected int[] otherMeasureIndex;

    /**
     * customMeasureIndex
     */
    protected int[] customMeasureIndex;

    /**
     * dataHandler
     */
    public MolapFactHandler dataHandler;

    /**
     * mdkeyLength
     */
    protected int mdkeyLength;

    protected List<MolapDataIterator<MolapSurrogateTupleHolder>> leafTupleIteratorList;

    public ColumnarFactFileMerger(
            MolapColumnarFactMergerInfo molapColumnarFactMergerInfo, int currentRestructNumber)
    {
        this.mdkeyLength = molapColumnarFactMergerInfo.getMdkeyLength();
        // CHECKSTYLE:OFF Approval No:Approval-367
        List<Integer> otherMeasureIndexList = new ArrayList<Integer>(
                MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        List<Integer> customMeasureIndexList = new ArrayList<Integer>(
                MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        for(int i = 0;i < molapColumnarFactMergerInfo.getType().length;i++)
        {
            if(molapColumnarFactMergerInfo.getType()[i] != 'c')
            {
                otherMeasureIndexList.add(i);
            }
            else
            {
                customMeasureIndexList.add(i);
            }
        }
        otherMeasureIndex = new int[otherMeasureIndexList.size()];
        customMeasureIndex = new int[customMeasureIndexList.size()];
        for(int i = 0;i < otherMeasureIndex.length;i++)
        {
            otherMeasureIndex[i] = otherMeasureIndexList.get(i);
        }
        for(int i = 0;i < customMeasureIndex.length;i++)
        {
            customMeasureIndex[i] = customMeasureIndexList.get(i);
        }

        this.leafTupleIteratorList = new ArrayList<MolapDataIterator<MolapSurrogateTupleHolder>>(
                molapColumnarFactMergerInfo.getSlicesFromHDFS().size());
        MolapDataIterator<MolapSurrogateTupleHolder> leaftTupleIterator = null;
        for(MolapSliceAndFiles sliceInfo : molapColumnarFactMergerInfo
                .getSlicesFromHDFS())
        {
            /*leaftTupleIterator = new MolapColumnarLeafTupleDataIterator(
                    sliceInfo.getPath(), sliceInfo.getSliceFactFilesList(),
                    getFactReaderInfo(molapColumnarFactMergerInfo), mdkeyLength);*/
            
            leaftTupleIterator = new MolapLeafTupleWrapperIterator(sliceInfo.getKeyGen(), molapColumnarFactMergerInfo.getGlobalKeyGen(), new MolapColumnarLeafTupleDataIterator(
                    sliceInfo.getPath(), sliceInfo.getSliceFactFilesList(),
                    getFactReaderInfo(molapColumnarFactMergerInfo), mdkeyLength));
            if(leaftTupleIterator.hasNext())
            {
                leaftTupleIterator.fetchNextData();
                leafTupleIteratorList.add(leaftTupleIterator); // CHECKSTYLE:ON
            }
        }
        dataHandler = new MolapFactDataHandlerColumnarMerger(
                molapColumnarFactMergerInfo, currentRestructNumber);
    }

    public abstract void mergerSlice() throws SliceMergerException;

    private FactReaderInfo getFactReaderInfo(
            MolapColumnarFactMergerInfo molapColumnarFactMergerInfo)
    {
        FactReaderInfo factReaderInfo = new FactReaderInfo();
        String[] aggType = new String[molapColumnarFactMergerInfo
                .getMeasureCount()];
        
        Arrays.fill(aggType, "n");
        if(null!=molapColumnarFactMergerInfo.getAggregators())
        {
            for(int i = 0;i < aggType.length;i++)
            {
                if(molapColumnarFactMergerInfo.getAggregators()[i]
                        .equals(MolapCommonConstants.CUSTOM)
                        || molapColumnarFactMergerInfo.getAggregators()[i]
                                .equals(MolapCommonConstants.DISTINCT_COUNT))
                {
                    aggType[i] = "c";
                }
                else
                {
                    aggType[i] = "n";
                }
            }
        }
        //factReaderInfo.setAggType(aggType);
        factReaderInfo.setCubeName(molapColumnarFactMergerInfo.getCubeName());
        factReaderInfo.setSchemaName(molapColumnarFactMergerInfo
                .getSchemaName());
        factReaderInfo.setMeasureCount(molapColumnarFactMergerInfo
                .getMeasureCount());
        factReaderInfo.setTableName(molapColumnarFactMergerInfo.getTableName());
        factReaderInfo.setDimLens(molapColumnarFactMergerInfo.getDimLens());
        int[] blockIndex = new int[molapColumnarFactMergerInfo.getDimLens().length];
        for(int i = 0;i < blockIndex.length;i++)
        {
            blockIndex[i] = i;
        }
        factReaderInfo.setBlockIndex(blockIndex);
        factReaderInfo.setUpdateMeasureRequired(true);

        return factReaderInfo;
    }

    /**
     * Below method will be used to add sorted row
     * 
     * @param rowKey
     * @param measure
     * @param measureIndexToRead
     * @param sliceUniqueValue
     * @throws SliceMergerException
     */
    protected void addRow(MolapSurrogateTupleHolder molapTuple)
            throws SliceMergerException
    {
        Object[] row = new Object[molapTuple.getMeasures().length + 1];
        System.arraycopy(molapTuple.getMeasures(), 0, row, 0,
                molapTuple.getMeasures().length);
        row[row.length - 1] = molapTuple.getMdKey();
        try
        {
            this.dataHandler.addDataToStore(row);
        }
        catch(MolapDataWriterException e)
        {
            throw new SliceMergerException("Problem in merging the slice", e);
        }
    }

}
