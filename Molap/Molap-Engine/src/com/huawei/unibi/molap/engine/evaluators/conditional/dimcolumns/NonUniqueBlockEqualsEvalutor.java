package com.huawei.unibi.molap.engine.evaluators.conditional.dimcolumns;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Map;

import com.huawei.unibi.molap.datastorage.store.columnar.ColumnarKeyStoreDataHolder;
import com.huawei.unibi.molap.engine.evaluators.AbstractConditionalEvalutor;
import com.huawei.unibi.molap.engine.evaluators.BlockDataHolder;
import com.huawei.unibi.molap.engine.evaluators.FilterProcessorPlaceHolder;
import com.huawei.unibi.molap.engine.expression.Expression;
import com.huawei.unibi.molap.util.ByteUtil;
import com.huawei.unibi.molap.util.MolapUtil;

public class NonUniqueBlockEqualsEvalutor extends AbstractConditionalEvalutor
{

    public NonUniqueBlockEqualsEvalutor(Expression exp, boolean isExpressionResolve, boolean isExcludeFilter)
    {
        super(exp,isExpressionResolve,isExcludeFilter);
    }

    @Override
    public BitSet applyFilter(BlockDataHolder blockDataHolder, FilterProcessorPlaceHolder placeHolder)
    {
        if(null==blockDataHolder.getColumnarKeyStore()[dimColEvaluatorInfoList.get(0).getColumnIndex()])
        {
            blockDataHolder.getColumnarKeyStore()[dimColEvaluatorInfoList.get(0).getColumnIndex()] = blockDataHolder
                    .getLeafDataBlock().getColumnarKeyStore(blockDataHolder.getFileHolder(),
                            dimColEvaluatorInfoList.get(0).getColumnIndex(),
                            dimColEvaluatorInfoList.get(0).isNeedCompressedData());
        }
        return getFilteredIndexes(
                blockDataHolder.getColumnarKeyStore()[dimColEvaluatorInfoList.get(0).getColumnIndex()],
                blockDataHolder.getLeafDataBlock().getnKeys());
    }

    private BitSet getFilteredIndexes(ColumnarKeyStoreDataHolder keyBlockArray, int numerOfRows)
    {
        if(keyBlockArray.getColumnarKeyStoreMetadata().isDirectSurrogateColumn())
        {
            return setDirectKeyFilterIndexToBitSet(keyBlockArray,numerOfRows);
        }
        else if(null!=keyBlockArray.getColumnarKeyStoreMetadata().getColumnIndex())
        {
            return setFilterdIndexToBitSetWithColumnIndex(keyBlockArray,numerOfRows);
        }

        return setFilterdIndexToBitSet(keyBlockArray,numerOfRows);
    }
    
    private BitSet setDirectKeyFilterIndexToBitSet(ColumnarKeyStoreDataHolder keyBlockArray, int numerOfRows)
    {
        BitSet bitSet = new BitSet(numerOfRows);
        Map<Integer, byte[]> mapOfColumnarKeyBlockDataForDirectSurroagtes = keyBlockArray
                .getColumnarKeyStoreMetadata().getMapOfColumnarKeyBlockDataForDirectSurroagtes();
        byte[][] filterValues = dimColEvaluatorInfoList.get(0).getFilterValues();
        int[] columnIndexArray = keyBlockArray.getColumnarKeyStoreMetadata().getColumnIndex();
        int[] columnReverseIndexArray = keyBlockArray.getColumnarKeyStoreMetadata()
                .getColumnReverseIndex();
        for(int i = 0;i < filterValues.length;i++)
        {
            byte[] filterVal = filterValues[i];
            if(null != mapOfColumnarKeyBlockDataForDirectSurroagtes)
            {

                if(null != columnIndexArray)
                {
                    for(int index : columnIndexArray)
                    {
                        byte[] directSurrogate = mapOfColumnarKeyBlockDataForDirectSurroagtes.get(columnReverseIndexArray[index]);
                        if(ByteUtil.UnsafeComparer.INSTANCE.compareTo(filterVal, directSurrogate) == 0)
                        {
                            bitSet.set(index);
                        }
                    }
                }
                else if(null != columnReverseIndexArray)
                {

                    for(int index : columnReverseIndexArray)
                    {
                        byte[] directSurrogate = mapOfColumnarKeyBlockDataForDirectSurroagtes.get(columnReverseIndexArray[index]);
                        if(ByteUtil.UnsafeComparer.INSTANCE.compareTo(filterVal, directSurrogate) == 0)
                        {
                            bitSet.set(index);
                        }
                    }
                }

            }
        }
        return bitSet;
        
    }

    private BitSet setFilterdIndexToBitSetWithColumnIndex(ColumnarKeyStoreDataHolder keyBlockArray, int numerOfRows)
    {
        int[] columnIndex = keyBlockArray.getColumnarKeyStoreMetadata().getColumnIndex();
        int start = 0;
        int last = 0;
        int startIndex = 0;
        BitSet bitSet = new BitSet(numerOfRows);
        byte[][] filterValues = dimColEvaluatorInfoList.get(0).getFilterValues();
        for(int i = 0;i < filterValues.length;i++)
        {
            start = MolapUtil.getFirstIndexUsingBinarySearch(keyBlockArray, startIndex, numerOfRows - 1,
                    filterValues[i]);
            if(start == -1)
            {
                continue;
            }
            bitSet.set(columnIndex[start]);
            last = start;
            for(int j = start + 1;j < numerOfRows;j++)
            {
                if(ByteUtil.UnsafeComparer.INSTANCE.compareTo(keyBlockArray.getKeyBlockData(), j
                        * filterValues[i].length,
                        filterValues[i].length,
                        filterValues[i], 0,
                        filterValues[i].length) == 0)
                {
                    bitSet.set(columnIndex[j]);
                    last++;
                }
                else
                {
                    break;
                }
            }
            startIndex = last;
            if(startIndex >= numerOfRows)
            {
                break;
            }
        }
        return bitSet;
    }
    
//    private BitSet setFilterdIndexToBitSetWithColumnIndex(ColumnarKeyStoreDataHolder keyBlockArray, int numerOfRows)
//    {
//        int[] columnIndex = keyBlockArray.getColumnarKeyStoreMetadata().getColumnIndex();
//        BitSet bitSet = new BitSet(numerOfRows);
//        byte[][] filterValues = dimColEvaluatorInfoList.get(0).getFilterValues();
//        
//        for(int i = 0;i < filterValues.length;i++)
//        {
//            for(int j = 0;j < numerOfRows;j++)
//            {
//                if(ByteUtil.UnsafeComparer.INSTANCE.compareTo(keyBlockArray.getKeyBlockData(), j
//                        * filterValues[i].length,
//                        filterValues[i].length,
//                        filterValues[i], 0,
//                        filterValues[i].length) == 0)
//                {
//                    if(null!=columnIndex)
//                    {
//                        bitSet.set(columnIndex[j]);
//                    }
//                    else
//                    {
//                        bitSet.set(j); 
//                    }
//                }
//            }
//        }
//        
//        return bitSet;
//    }
    private BitSet setFilterdIndexToBitSet(ColumnarKeyStoreDataHolder keyBlockArray, int numerOfRows)
    {
        int start = 0;
        int last = 0;
        BitSet bitSet = new BitSet(numerOfRows);
        int startIndex = 0;
        byte[][] filterValues = dimColEvaluatorInfoList.get(0).getFilterValues();
        for(int k = 0;k < filterValues.length;k++)
        {
            start = MolapUtil.getFirstIndexUsingBinarySearch(keyBlockArray, startIndex, numerOfRows - 1,
                    filterValues[k]);
            if(start == -1)
            {
                continue;
            }
            bitSet.set(start);
            last = start;
            for(int j = start + 1;j < numerOfRows;j++)
            {
                if(ByteUtil.UnsafeComparer.INSTANCE.compareTo(keyBlockArray.getKeyBlockData(), j
                        *filterValues[k].length,
                        filterValues[k].length,
                        filterValues[k], 0,
                        filterValues[k].length) == 0)
                {
                    bitSet.set(j);
                    last++;
                }
                else
                {
                    break;
                }
            }
            startIndex = last;
            if(startIndex >= numerOfRows)
            {
                break;
            }
        }
        return bitSet;
    }

    @Override
    public BitSet isScanRequired(byte[][] blkMaxVal, byte[][] blkMinVal)  
    {
        BitSet bitSet = new BitSet(1);
        byte[][] filterValues = dimColEvaluatorInfoList.get(0).getFilterValues();
        int columnIndex = dimColEvaluatorInfoList.get(0).getColumnIndex();
        boolean isScanRequired=false;
        for(int k = 0;k < filterValues.length;k++)
        {
            //filter value should be in range of max and min value i.e max>filtervalue>min
            //so filter-max should be negative
            int maxCompare=ByteUtil.UnsafeComparer.INSTANCE.compareTo(filterValues[k],blkMaxVal[columnIndex]);
            //and filter-min should be positive
            int minCompare=ByteUtil.UnsafeComparer.INSTANCE.compareTo(filterValues[k],blkMinVal[columnIndex]);
            
            //if any filter value is in range than this block needs to be scanned
            if(maxCompare<=0 && minCompare>=0)
            {
                isScanRequired=true;
                break;
            }
        }
        if(isScanRequired)
        {
            bitSet.set(0);
        }
        return bitSet;
    }
    
    
}
