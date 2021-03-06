/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwfQVwqh74rUY6n+OZ2pUrkn1TkkvO60rFu08DZa
JnQq9GjHw1JHL8HRPq8ZgoUh/4n13d2Q4g/VTa53EI9pjxN48UFXYm726GO//kLf5/3vNNW3
l1Jrt6XPgVywOm6Dl9p2gEmKGHgwkat6K76P8eN4GIthTQXpzx24paUWoNIs3Q==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * 
 */
package com.huawei.unibi.molap.engine.wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.jpountz.xxhash.XXHash32;

import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.util.ByteUtil.UnsafeComparer;


/**
 * Project Name NSE V3R7C00
 * 
 * Module Name : Molap
 * 
 * Author K00900841
 * 
 * Created Date :13-May-2013 3:35:33 PM
 * 
 * FileName : ByteArrayWrapper.java
 * 
 * Class Description :This class will be used as a key in the result map, it
 * will contain maksed key and actual data
 * 
 * Version 1.0
 */
public class ByteArrayWrapper implements Comparable<ByteArrayWrapper>,Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 2203622486612960809L;
    
    
    private XXHash32 xxHash32;

    /**
     * masked keys
     */
    protected byte[] maskedKey;




    private byte[] directSurrogateVal;
    
    List<byte[]> listOfDirectSurrogateVal;

    protected List<byte[]> complexTypesData;
    
    public ByteArrayWrapper()
    {
        this.complexTypesData = new ArrayList<byte[]>();
    }
    
    public ByteArrayWrapper(XXHash32 xxHash32)
    {
       this.xxHash32 = xxHash32;
       this.complexTypesData = new ArrayList<byte[]>();
    }
    
    public byte[] getComplexTypeData(int index)
    {
        return complexTypesData.get(index);
    }
    
    public List<byte[]> getCompleteComplexTypeData()
    {
        return complexTypesData;
    }
    
    public void addComplexTypeData(byte[] data)
    {
        complexTypesData.add(data);
    }

    /**
     * 
     * 
     * @param data
     *          keys  
     * @param offset
     *          key offset
     * @param maxKey
     *          max key
     * @param maskByteRanges
     *          mask byte range
     * @param byteCount
     *          total byte count
     *
     */
    public void setData(byte[] data, int offset,byte[] maxKey, int[] maskByteRanges, int byteCount)
    {
        // check masked key is null or not
        if(maskedKey == null)
        {
            this.maskedKey = new byte[byteCount];
        }
        int counter = 0;
        int byteRange = 0;
        for(int i = 0;i < maskByteRanges.length;i++)
        {
            byteRange = maskByteRanges[i];
            maskedKey[counter++] = (byte)(data[byteRange + offset] & maxKey[byteRange]);
        }

    }

    /**
     * 
     * 
     * @param data
     *          byte array 
     * @param offset
     *          key offset 
     * @param length
     *          key length
     * @param maxKey
     *          max key 
     * @param byteCount
     *          total byte count 
     *
     */
    public void setData(byte[] data, int offset, int length, byte[] maxKey, int byteCount)
    {
        if(maskedKey == null)
        {
            this.maskedKey = new byte[length];
        }

        for(int j = 0;j < length;j++)
        {
            maskedKey[j] = (byte)(data[offset] & maxKey[j]);
            offset++;
        }
    }

    /**
     * This method is used to calculate the hash code 
     * 
     * @param maskKey
     *          mask key
     * @return hashcode
     *
     */
    protected int getHashCode(byte[] maskKey)
    {
        int len = maskKey.length;
        if(xxHash32!=null)
        {
            return xxHash32.hash(maskKey, 0, len, 0);
        }
        
        int result = 1;
        for(int j = 0;j < len;j++)
        {
            result = 31 * result + maskKey[j];
        }
        return result;
    }

    /**
     * This method will be used to get the hascode, this will be used to the
     * index for inserting ArrayWrapper object as a key in Map
     * 
     * @return int hascode
     * 
     */
    public int hashCode()
    {
        int len = maskedKey.length;
        if(xxHash32!=null)
        {
            return xxHash32.hash(maskedKey, 0, len, 0);
        }
        
        int result = 1;
        for(int j = 0;j < len;j++)
        {
            result = 31 * result + maskedKey[j];
        }
        if(null!=listOfDirectSurrogateVal)
        {
            int index=0;
            for(byte[] directSurrogateValue:listOfDirectSurrogateVal)
            {
                for(int i=0;i<directSurrogateValue.length;i++)
                {
                result = 31 * result + directSurrogateValue[i];
                }
            }
        }
        return result;
    }

    /**
     * This method will be used check to ByteArrayWrapper object is equal or not 
     * 
     * @param object
     *          ArrayWrapper object 
     * @return boolean 
     *          equal or not
     *
     */
    public boolean equals(Object other)
    {

        if(null == other || !(other instanceof ByteArrayWrapper))
        {
            return false;
        }
        boolean result = false;
        // High cardinality dimension rows has been added in list of
        // ByteArrayWrapper, so
        // the same has to be compared to know whether the byte array wrappers
        // are equals or not.
        List<byte[]> otherList = ((ByteArrayWrapper)other).getDirectSurrogateKeyList();
        if(null != listOfDirectSurrogateVal)
        {
            if(listOfDirectSurrogateVal.size() != otherList.size())
            {
                return false;
            }
            else
            {
                for(int i = 0;i < listOfDirectSurrogateVal.size();i++)
                {
                    result = UnsafeComparer.INSTANCE.equals(listOfDirectSurrogateVal.get(i), otherList.get(i));
                    if(!result)
                    {
                        return false;
                    }
                }
            }

        }
        return UnsafeComparer.INSTANCE.equals(maskedKey, ((ByteArrayWrapper)other).maskedKey);
    }



    /**
     * Compare method for ByteArrayWrapper class this will used to compare Two
     * ByteArrayWrapper data object, basically it will compare two byte
     * array 
     * 
     * @param other
     *            ArrayWrapper Object
     * 
     */
    @Override
    public int compareTo(ByteArrayWrapper other)
    {
        int compareTo = UnsafeComparer.INSTANCE.compareTo(maskedKey, other.maskedKey);
        if(compareTo == 0)
        {
            if(null != listOfDirectSurrogateVal)
            {
                for(int i = 0;i < listOfDirectSurrogateVal.size();i++)
                {
                    compareTo = UnsafeComparer.INSTANCE.compareTo(listOfDirectSurrogateVal.get(i),
                            other.listOfDirectSurrogateVal.get(i));
                    if(compareTo != 0)
                    {
                        return compareTo;
                    }
                }
            }
        }
        return compareTo;
    }

    public byte[] getMaskedKey()
    {
        return maskedKey;
    }

    public void setMaskedKey(byte[] maskedKey)
    {
        this.maskedKey = maskedKey;
    }
    

    /**
     * addToDirectSurrogateKeyList
     * @param directSurrKeyData
     */
    public void addToDirectSurrogateKeyList(byte[] directSurrKeyData)
    {
        if(null == listOfDirectSurrogateVal)
        {
            listOfDirectSurrogateVal = new ArrayList<byte[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
            listOfDirectSurrogateVal.add(directSurrKeyData);
        }
        else
        {
            listOfDirectSurrogateVal.add(directSurrKeyData);
        }

    }
    
    /**
     * addToDirectSurrogateKeyList
     * @param directSurrKeyData
     */
    public void addToDirectSurrogateKeyList(List<byte[]> directSurrKeyData)
    {
        //Add if any direct surrogates are really present.
        if(null != directSurrKeyData && !directSurrKeyData.isEmpty())
        {
            if(null == listOfDirectSurrogateVal)
            {
                listOfDirectSurrogateVal = new ArrayList<byte[]>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
                listOfDirectSurrogateVal.addAll(directSurrKeyData);
            }
            else
            {
                listOfDirectSurrogateVal.addAll(directSurrKeyData);
            }
        }

    }
    
    /**
     * 
     * @return
     */
    public List<byte[]> getDirectSurrogateKeyList()
    {
        return listOfDirectSurrogateVal;
    }
}
