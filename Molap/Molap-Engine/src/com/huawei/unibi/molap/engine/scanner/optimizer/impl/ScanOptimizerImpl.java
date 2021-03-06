/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwfQVwqh74rUY6n+OZ2pUrkn1TkkvO60rFu08DZa
JnQq9DX/uXEFMlXnYpH7jRZCAXRwVTQ6gUb4acnhTfC24QNjVC5P58u91gLUkkGOWuTsrIZV
ZLXsqhLqJq7Zvsags/CrCsVVsA/JpsWN592mKXbJxi1lQIUiyZy2jhj+soXVbw==*/
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
package com.huawei.unibi.molap.engine.scanner.optimizer.impl;

//import org.apache.log4j.Logger;
import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.engine.scanner.optimizer.ScanOptimizer;
import com.huawei.unibi.molap.engine.util.MolapEngineLogEvent;
import com.huawei.unibi.molap.keygenerator.KeyGenerator;

/**
 * This class provides the concrete implementation for ScanOptimizer and mainly used to get the 
 * Next key to start scan while scanning the tree with some filter is applied.
 *  
 * @author R00900208
 * 
 */
public class ScanOptimizerImpl implements ScanOptimizer
{

//    /**
//     * 
//     */
//    protected long[][] filters;
//
//    /**
//     * 
//     */
//    protected long[] maxKey;
//
//    /**
//     * 
//     */
//    protected byte[] endKey ;
//
//    /**
//     * 
//     */
//    protected long[] nextEndKey;
//
//    /**
//     * 
//     */
//    protected KeyGenerator generator;
//
//    /**
//     * 
//     */
//    protected boolean isDone;
//    
    /**
     * 
     */
    private static final LogService LOGGER = LogServiceFactory.getLogService(ScanOptimizerImpl.class.getName());

//    // public static int i;
//
    public ScanOptimizerImpl(final long[] maxKey, final long[][] filters, final KeyGenerator keyGenerator)
    {
        LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, "UNSUPPORT Operation");
    }

    @Override
    public byte[] getNextKey(final long[] originalKey, final byte[] transKey)
    {
        LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, "UNSUPPORT Operation");
        return new byte[0];
    }
    
//
//    /**
//     * 
//     * 
//     * @see com.huawei.unibi.molap.engine.scanner.optimizer.ScanOptimizer#getNextKey(long[], byte[])
//     *
//     */
//    public byte[] getNextKey(long[] key, byte[] byteKey)
//    {
//        try
//        {
//
//            // i++;
//            if(ByteUtil.compare(byteKey, endKey) > 0)
//            {
//                isDone = true;
//                return null;
//            }
//            if(nextEndKey != null && compareTo(key, nextEndKey) < 0)
//            {
//                return null;
//            }
//            // key = key.clone();
//            int curr = checkWithFilter(key);
//            long[] nextEnd = key.clone();
//            nextEnd[curr]++;
//            nextEndKey = nextEnd;
//            return generator.generateKey(key);
//        }
//        catch(Exception e)
//        {
//            LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, e, "Key Generation failed");
//        }
//        return null;
//        // return getKey(key, byteKey,nextEnd,curr);
//    }
//
//    private int compareTo(long[] left, long[] right)
//    {
//        for(int i = 0;i < left.length;i++)
//        {
//            if(left[i] > right[i])
//            {
//                return 1;
//            }
//            else if(left[i] < right[i])
//            {
//                return -1;
//            }
//        }
//
//        return 0;
//    }
//
//    protected int checkWithFilter(long[] key)
//    {
//        boolean changed = false;
//        int index = 0;
//
//        // iterate all columns and set index to the last modified column which
//        // has filter
//        colIndxloop: for(int colIndex = 0;colIndex < key.length;colIndex++)
//        {
//            long[] includeFil = filters[colIndex];
//            if(includeFil != null)
//            {
//                if(changed)
//                {
//                    // if any of the previous column already has got changed
//                    // then reset to the first filter
//                    // for all succeeding columns which has filter and move on
//                    // to next column.
//                    key[colIndex] = includeFil[0];
//                    index = colIndex;
//                }
//                else
//                {
//                    for(long aFilter : includeFil)
//                    {
//                        if(key[colIndex] == aFilter)
//                        {
//                            index = colIndex;
//                            continue colIndxloop;
//                        }
//                        else if(key[colIndex] < aFilter)
//                        {
//                            key[colIndex] = aFilter;
//                            changed = true;
//                            index = colIndex;
//                            continue colIndxloop;
//                        }
//                    }
//                    // Now current col value exceeded all the filters,
//                    // so move the cursor a step back and try reset the prev col
//                    // val
//
//                    // TODO if -1 is returned then all scanning is finished
//                    // returned i value indicates from which position the
//                    // columns needs to recheck with filter
//                    colIndex = setKey(key, colIndex);
//                    changed = true;
//                }
//            }
//            else
//            {
//                if(changed)
//                {
//                    // if there is no filter on the current column and if any of
//                    // previous column has got changed
//                    // then reset this col to initial key
//                    key[colIndex] = 0L;
//                }
//            }
//        }
//        if(!changed)
//        {
//            key[key.length - 1]++;
//        }
//        return index;
//    }
//
//    protected int setKey(long[] key, int index)
//    {
//        for(int i = index - 1;i >= 0;i--)
//        {
//            long[] fil = filters[i];
//            if(fil != null)
//            {
//                //
//                for(long f : fil)
//                {
//                    if(key[i] < f)
//                    {
//                        key[i] = f;
//                        return i;
//                    }
//                }
//            }
//            else
//            {
//                //
//                if(key[i] < maxKey[i])
//                {
//                    key[i]++;
//                    return i;
//                }
//            }
//        }
//        return index;
//    }
//
//    public boolean isDone()
//    {
//        return isDone;
//    }
//
///*  
// * Commented this main Method so that later if someone wants to debug the code then with this will be easy.
// *   
// *   public static void main(String[] args) throws KeyGenException
//    {
//        long[][] filters = new long[10][];
//        filters[2] = new long[]{2L, 5L};
//        filters[4] = new long[]{2L, 5L};
//
//        long[] maxKey = new long[]{10L, 10L, 5l, 10l, 10l};
//        ScanOptimizerImpl opt = new ScanOptimizerImpl(maxKey, filters, new MultiDimKeyGenerator());
//        long[] p = new long[]{1l, 1l, 1l, 1l, 1l};
//        for(int i = maxKey.length - 1;i >= 0;i--)
//        {
//            p = p.clone();
//            for(int j = 0;j < 10;j++)
//            {
//                p[i]++;
//                byte[] k = new MultiDimKeyGenerator().generateKey(p);
//                opt.getNextKey(p, k);
//
//            }
//        }
//    }*/

}
