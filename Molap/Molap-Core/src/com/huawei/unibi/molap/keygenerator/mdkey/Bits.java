/**
 * 
 */
package com.huawei.unibi.molap.keygenerator.mdkey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.util.MolapCoreLogEvent;


/**
 * @author R00900208
 * 
 */
public class Bits implements Serializable
{

    /**
     * Bits MAX_LENGTH
     */
    private static final int MAX_LENGTH = 63;

	private static final int LONG_LENGTH = 64;

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = 1555114921503304849L;
    /**
     * LONG_MAX.
     */
    private static final long LONG_MAX = 0x7fffffffffffffffL;
    /**
     * length.
     */
    private int length = 100;
    /**
     * lens.
     */
    private int[] lens;
    /**
     * wsize.
     */
    private int wsize;
    /**
     * byteSize.
     */
    private int byteSize;
    
    /**
     * Attribute for Molap LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(Bits.class.getName());

    public int getByteSize()
    {
        return byteSize;
    }

    public Bits(int[] lens)
    {
        this.lens = lens;
        this.length = getTotalLength(lens);

		wsize = length / LONG_LENGTH;
		byteSize = length / 8;

        if (length % LONG_LENGTH != 0) 
        {
            wsize++;
        }
        
        if (length % 8 != 0) 
        {
            byteSize++;
        }
    }

    private int getTotalLength(int[] lens)
    {
        int tLen = 0;
        for(int len : lens)
        {
            tLen += len;
        }
        return tLen;
    }

    /**
     * Return the start and end Byte offsets of dimension in the MDKey. int []
     * {start, end}
     */
    public int[] getKeyByteOffsetsOld(int index)
    {
        int priorLen = 0;
        int start = 0;
        int end = 0;
        for(int i = 0;i < index;i++)
        {
            priorLen += lens[i];
        }
        start = priorLen / 8;
        int rem = priorLen % 8;
        // if(start>0)
        // {
        // start--;
        // }
        end = start;
        if(rem >= 0)
        {
            start++;
        }
        int endrem = lens[index] - (8 - rem);
        if(endrem > 0)
        {
            end++;
            end += endrem / 8;
            if(endrem % 8 > 0)
            {
                end++;
            }
        }
        if(end < start)
        {
            end = start;
        }
        return new int[]{start == 0 ? start : start - 1, end == 0 ? end : end - 1};
    }

    public int getDimCount()
    {
        return lens.length;
    }

    /**
     * Return the start and end Byte offsets of dimension in the MDKey. int []
     * {start, end}
     */
    public int[] getKeyByteOffsets(int index)
    {
        int prefixPaddingBits = length % 8 == 0 ? 0 : (8 - length % 8);

        int priorLen = prefixPaddingBits;
        int start = 0;
        int end = 0;

        // Calculate prior length for all previous keys
        for(int i = 0;i < index;i++)
        {
            priorLen += lens[i];
        }

        // Start
        start = priorLen / 8;

        int tillKeyLength = priorLen + lens[index];

        // End key
        end = (tillKeyLength) / 8;

        // Consider if end is the last bit. No need to include the next byte.
        if(tillKeyLength % 8 == 0)
        {
            end--;
        }

        return new int[]{start, end};
    }

    protected long[] get(long[] keys)
    {
        long[] words = new long[wsize];
        int ll = 0;
        for(int i = lens.length - 1;i >= 0;i--)
        {

            long val = keys[i];

            int idx = ll >> 6;// divide by 64 to get the new word index
            int position = ll & 0x3f;// to ignore sign bit and consider the remaining
            val = val & (LONG_MAX >> (MAX_LENGTH - lens[i]));// To control the
                                                             // logic so that
                                                             // any val do not
                                                             // exceed the
                                                             // cardinality
            long mask = (val << position);
            long word = words[idx];
            words[idx] = (word | mask);
            ll += lens[i];

            int nextIndex = ll >> 6;// This is divide by 64

            if(nextIndex != idx)
            {
	        	int consideredBits = lens[i]-ll & 0x3f;
	        	if(consideredBits<lens[i]) //Check for spill over only if all the bits are not considered
	        	{
    	        	mask  = (val>>(lens[i]-ll & 0x3f));//& (0x7fffffffffffffffL >> (0x3f-pos));
    		        word  = words[nextIndex];
    		        words[nextIndex] = (word | mask);
	        	}
	        }
	        			
		} 
		
		return words;
    }
    
    
    protected long[] get(int[] keys)
    {
        long[] words = new long[wsize];
        int ll = 0;
        for(int i = lens.length - 1;i >= 0;i--)
        {

            long val = keys[i];

            int index = ll >> 6;// divide by 64 to get the new word index
            int pos = ll & 0x3f;// to ignore sign bit and consider the remaining
            val = val & (LONG_MAX >> (MAX_LENGTH - lens[i]));// To control the
                                                             // logic so that
                                                             // any val do not
                                                             // exceed the
                                                             // cardinality
            long mask = (val << pos); 
            long word = words[index];
            words[index] = (word | mask);
            ll += lens[i];

            int nextIndex = ll >> 6;// This is divide by 64

            if(nextIndex != index)
            {
                int consideredBits = lens[i]-ll & 0x3f;
                if(consideredBits<lens[i]) //Check for spill over only if all the bits are not considered
                {
                    // Check for spill over
                    mask = (val >> (lens[i] - ll & 0x3f));// & (0x7fffffffffffffffL
                                                          // >> (0x3f-pos));
                    word = words[nextIndex];
                    words[nextIndex] = (word | mask);
                }
            }

        }

        return words;
    }

    private long[] getArray(long[] words)
    {
        long[] vals = new long[lens.length];
        int ll = 0;
        for(int i = lens.length - 1;i >= 0;i--)
        {

            int index = ll >> 6;
            int pos = ll & 0x3f;
            long val = words[index];
            long mask = (LONG_MAX >>> (MAX_LENGTH - lens[i]));
            mask = mask << pos;
            vals[i] = (val & mask);
            vals[i] >>>= pos;
            ll += lens[i];

            int nextIndex = ll >> 6;
            if(nextIndex != index)
            {
    	        	pos =  ll & 0x3f;
	        	if(pos != 0) // Number of bits pending for current key is zero, no spill over 
	        	{
	        	    mask = (LONG_MAX >>> (MAX_LENGTH-pos));
	        	    val = words[nextIndex];
//		            long valMask = (0x7fffffff >>> (0x3f-pos));
	        	    vals[i] = vals[i]|((val & mask)<<(lens[i]-pos));
	        	}            
			}
            // LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, vals[i]);
        }
        return vals;
    }

    public byte[] getBytes(long[] keys)
    {

        long[] words = get(keys);

        return getBytesVal(words);
    }
    
    
    //TODO SIMIAN
    /**
     * @param words
     * @return
     */
    private byte[] getBytesVal(long[] words)
    {
        int length = 8;
        byte[] bytes = new byte[byteSize];

        int l = byteSize - 1;
        for(int i = 0;i < words.length;i++)
        {
            long val = words[i];

            for(int j = length - 1;j > 0 && l > 0;j--)
            {
                bytes[l] = (byte)val;
                val >>>= 8;
                l--;
            }
            bytes[l] = (byte)val;
            l--;
        }
        return bytes;
    }
    
    public byte[] getBytes(int[] keys)
    {

        long[] words = get(keys);

        return getBytesVal(words);
    }
    

    public List<List<Long>> getMasks()
    {
        List<List<Long>> masks = new ArrayList<List<Long>>(MolapCommonConstants.CONSTANT_SIZE_TEN);

        int ll = 0;
        for(int i = 0;i < lens.length;i++)
        {
            List<Long> list = new ArrayList<Long>(MolapCommonConstants.CONSTANT_SIZE_TEN);
            masks.add(list);
            int index = ll >> 6;
            int pos = ll & 0x3f;
            long mask = LONG_MAX >> (MAX_LENGTH - lens[i]);
            mask <<= pos;
            list.add(mask);
            LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "mask1 : " + Long.toBinaryString(mask));
            ll += lens[i];

            int nextIndex = ll >> 6;

            if(nextIndex != index)
            {
                list.add((LONG_MAX >> (lens[i] - (ll & 0x3f))));// &
                                                                // (0x7fffffffffffffffL
                                                                // >>
                                                                // (0x3f-pos));
                // LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "mask2 : "+Long.toBinaryString(masks[i][1]));
            }

        }
        return masks;

    }

    public long[] getKeyArray(byte[] key)
    {

        int length = 8;
        int ls = byteSize;
        long[] words = new long[wsize];
        for(int i = 0;i < words.length;i++)
        {
            long l = 0;
            ls -= 8;
            int m = 0;
            if(ls < 0)
            {
                m = ls + length;
                ls = 0;
            }
            else
            {
                m = ls + 8;
            }
            for(int j = ls;j < m;j++)
            {
                l <<= 8;
                l ^= key[j] & 0xFF;
            }
            words[i] = l;
        }

        return getArray(words);

    }
    
    public long[] getKeyArray(byte[] key, int start, int end)
    {

        int length = 8;
        int ls = byteSize;
        long[] words = new long[wsize];
        for(int i = 0;i < words.length;i++)
        {
            long l = 0;
            ls -= 8;
            int m1 = 0;
            if(ls < 0)
            {
                m1 = ls + length;
                ls = 0;
            }
            else
            {
                m1 = ls + 8;
            }
            for(int j = ls+start;j < m1+end;j++)
            {
                l <<= 8;
                l ^= key[j] & 0xFF;
            }
            words[i] = l;
        }

        return getArray(words);

    }
    
    public long[] getKeyArray(byte[] key,int[] maskByteRanges)
    {

        int length = 8;
        int ls = byteSize;
        long[] words = new long[wsize];
        for(int i = 0;i < words.length;i++)
        {
            long l = 0;
            ls -= 8;
            int m2 = 0;
            if(ls < 0)
            {
                m2 = ls + length;
                ls = 0;
            }
            else
            {
                m2 = ls + 8;
            }
            if(maskByteRanges == null)
            {
                for(int j = ls;j < m2;j++)
                {
                    l <<= 8;
                    l ^= key[j] & 0xFF;
                }
            }
            else
            {
                for(int j = ls;j < m2;j++)
                {
                    l <<= 8;
                    if(maskByteRanges[j] != -1)
                    {
                        l ^= key[maskByteRanges[j]] & 0xFF;
                    }
                    else
                    {
                        l ^= 0 & 0xFF;
                    }
                }
            }
            words[i] = l;
        }

        return getArray(words);

    }

 /*   public static void main(String[] args)
    {

        // long l = -112121212121l;
        // LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, Long.toBinaryString(l));
        // LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, (byte)l);
        // for (int i = 0; i < 8; i++) {
        // l>>>=8;
        // LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, Long.toBinaryString(l));
        // LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, (byte)l);
        // }
        LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, Long.toBinaryString(127).length());

        Bits bits = new Bits(new int[]{7, 7, 16});

        bits.getBytes(new long[]{1l, 2l, 3l});
    }*/

   /* private static void print(keyComp[] comps, Bits bits)
    {
        for(int i = 0;i < comps.length;i++)
        {
            LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, Arrays.toString(bits.getArray(comps[i].buffer1)));
        }
        LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "end" + comps.length);
    }

    private static void print(keyCompByte[] comps, Bits bits)
    {
        LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "Printing...");
        for(int i = 0;i < comps.length;i++)
        {
            LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, Arrays.toString(bits.getKeyArray(comps[i].buffer1)));
        }
        LOGGER.info(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, "end" + comps.length);
    }*/


    /**
     * 
     */
    public static final int SIZEOF_LONG = Long.SIZE / Byte.SIZE;

    public int getKeySize()
    {
        return byteSize;
    }
    
    /**
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Bits)
        {
            Bits other = (Bits)obj;
            return Arrays.equals(lens, other.lens);
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return Arrays.hashCode(lens);
    }
}
