/**
 * 
 */
package com.huawei.unibi.molap.datastorage.store.columnar;

//import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huawei.unibi.molap.constants.MolapCommonConstants;

/**
 * @author R00900208
 * 
 */
public class BlockIndexerStorage implements IndexStorage<short[]>
{

    private boolean alreadySorted;

    private short[] dataAfterComp;
    
    private short[] indexMap;

    private short[] dataIndexMap;
    
    private byte[][] keyBlock;
    
    private int totalSize;
    
    public BlockIndexerStorage(byte[][] keyBlock)
    {
        this(keyBlock,false);
    }
    
    public BlockIndexerStorage(byte[][] keyBlock, boolean compressData)
    {
        ColumnWithIndex[] columnWithIndexs = createColumnWithIndexArray(keyBlock);
        Arrays.sort(columnWithIndexs);
        if(compressData)
        {
            compressDataMyOwnWay(columnWithIndexs);
        }
        compressMyOwnWay(extractDataAndReturnIndexes(columnWithIndexs, keyBlock, compressData));
    }
    
    /**
     * Create an object with each column array and respective index
     * 
     * @param cols
     * @return
     */
    private ColumnWithIndex[] createColumnWithIndexArray(byte[][] keyBlock)
    {
        ColumnWithIndex[] columnWithIndexs = new ColumnWithIndex[keyBlock.length];
        for(int i = 0;i < columnWithIndexs.length;i++)
        {
            columnWithIndexs[i] = new ColumnWithIndex(keyBlock[i], (short)i);
        }
        return columnWithIndexs;
    }

    private short[] extractDataAndReturnIndexes(
            ColumnWithIndex[] columnWithIndexs, byte[][] keyBlock, boolean compressData)
    {
        short[] indexes = new short[columnWithIndexs.length];
        for(int i = 0;i < indexes.length;i++)
        {
            indexes[i] = columnWithIndexs[i].getIndex();
            if(!compressData)
            {
            keyBlock[i]=columnWithIndexs[i].getColumn();
            }
        }
        if(!compressData)
        {
        this.keyBlock=keyBlock;
        }
        return indexes;
    }

    /**
     * It compresses depends up on the sequence numbers.
     * [1,2,3,4,6,8,10,11,12,13] is translated to [1,4,6,8,10,13] and [0,7]. In
     * first array the start and end of sequential numbers and second array
     * keeps the indexes of where sequential numbers starts. If there is no
     * sequential numbers then the same array it returns with empty second
     * array.
     * 
     * @param indexes
     */
    public void compressMyOwnWay(short[] indexes)
    {
        List<Short> list = new ArrayList<Short>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        List<Short> map = new ArrayList<Short>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        int k = 0;
        int i = 1;
        for(;i < indexes.length;i++)
        {
            if(indexes[i] - indexes[i - 1] == 1)
            {
                k++;
            }
            else
            {
                if(k > 0)
                {
                    map.add((short)(list.size()));
                    list.add(indexes[i - k - 1]);
                    list.add(indexes[i - 1]);
                }
                else
                {
                    list.add(indexes[i - 1]);
                }
                k = 0;
            }
        }
        if(k > 0)
        {
            map.add((short)(list.size()));
            list.add(indexes[i - k - 1]);
            list.add(indexes[i - 1]);
        }
        else
        {
            list.add(indexes[i - 1]);
        }
        dataAfterComp = convertToArray(list);
        if(indexes.length == dataAfterComp.length)
        {
            indexMap = new short[0];
        }
        else
        {
            indexMap = convertToArray(map);
        }
        if(dataAfterComp.length == 2 && indexMap.length == 1)
        {
            alreadySorted = true;
        }
    }
    
    public void compressDataMyOwnWay(ColumnWithIndex[] indexes)
    {
        List<ColumnWithIndex> list = new ArrayList<ColumnWithIndex>(MolapCommonConstants.CONSTANT_SIZE_TEN);
        List<Short> map = new ArrayList<Short>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
        int k = 0;
        int i = 1;
        for(;i < indexes.length;i++)
        {
            if(indexes[i].compareTo(indexes[i - 1]) == 0)
            {
                k++;
            }
            else
            {
                if(k > 0)
                {
                    map.add((short)(list.size()));
                    map.add((short)(k+1));
                    list.add(indexes[i - 1]);
                }
                else
                {
                    list.add(indexes[i - 1]);
                }
                k = 0;
            }
        }
        if(k > 0)
        {
            map.add((short)(list.size()));
            map.add((short)(k+1));
            list.add(indexes[i - 1]);
        }
        else
        {
            list.add(indexes[i - 1]);
        }
        this.keyBlock = convertToKeyArray(list);
        if(indexes.length == keyBlock.length)
        {
            dataIndexMap = new short[0];
        }
        else
        {
            dataIndexMap = convertToArray(map);
        }
    }

    private short[] convertToArray(List<Short> list)
    {
        short[] shortArray = new short[list.size()];
        for(int i = 0;i < shortArray.length;i++)
        {
            shortArray[i] = list.get(i);
        }
        return shortArray;
    }
    
    private byte[][] convertToKeyArray(List<ColumnWithIndex> list)
    {
        byte[][] shortArray = new byte[list.size()][];
        for(int i = 0;i < shortArray.length;i++)
        {
            shortArray[i] = list.get(i).getColumn();
            totalSize+=shortArray[i].length;
        }
        return shortArray;
    }

    /**
     * @return the alreadySorted
     */
    public boolean isAlreadySorted()
    {
        return alreadySorted;
    }

    /**
     * @return the dataAfterComp
     */
    public short[] getDataAfterComp()
    {
        return dataAfterComp;
    }

    /**
     * @return the indexMap
     */
    public short[] getIndexMap()
    {
        return indexMap;
    }

    /**
     * @return the keyBlock
     */
    public byte[][] getKeyBlock()
    {
        return keyBlock;
    }

    /**
     * @param keyBlock the keyBlock to set
     */
    public void setKeyBlock(byte[][] keyBlock)
    {
        this.keyBlock = keyBlock;
    }

    /**
     * @return the dataIndexMap
     */
    public short[] getDataIndexMap()
    {
        return dataIndexMap;
    }
    
  /*  public static void main(String[] args)
    {
        int[] data = {1,3,1,3,4,2,1,4,4,9,19,3,1};
        byte[][] array = new byte[data.length][];
        for(int i = 0;i < data.length;i++)
        {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(data[i]);
            buffer.rewind();
            array[i] = buffer.array();
        }
        BlockIndexerStorage indexerStorage = new BlockIndexerStorage(array,true);
        System.out.println(Arrays.toString(indexerStorage.getDataAfterComp()));
        System.out.println(Arrays.toString(indexerStorage.getIndexMap()));
        int[] finalData = new int[indexerStorage.getKeyBlock().length];
        for(int i = 0;i < indexerStorage.getKeyBlock().length;i++)
        {
            ByteBuffer buffer = ByteBuffer.wrap(indexerStorage.getKeyBlock()[i]);
            buffer.rewind();
            finalData[i] = buffer.getInt();
        }
        Arrays.sort(data);
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(finalData));
        System.out.println(Arrays.toString(indexerStorage.getDataIndexMap()));
        
        byte[][] b = UnBlockIndexer.uncompressData(indexerStorage.getKeyBlock(), indexerStorage.getDataIndexMap());
        int[] finalData1 = new int[b.length];
        for(int i = 0;i < b.length;i++)
        {
            ByteBuffer buffer = ByteBuffer.wrap(b[i]);
            buffer.rewind();
            finalData1[i] = buffer.getInt();
        }
        System.out.println(Arrays.toString(finalData1));
    }*/
    
    @Override
    public int getTotalSize()
    {
        return totalSize;
    }
}
