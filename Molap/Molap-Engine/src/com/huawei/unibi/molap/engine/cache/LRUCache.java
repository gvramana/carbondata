/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwdEVzw1icjfRowqz2DW4XzUpEhhSzBOwVynEHjc
u0090QH7ysyJHivtf6CjMSmHvAouxlrnRKVRwbiTFLAUMgy6FikicKyTq2dMGZtKqsjQCszi
E/TQt09KWG30amB9/cL5wWTlHZ41woRxAsOiH7A1mycQdVOQPvwSrTjIlMGQpw==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
package com.huawei.unibi.molap.engine.cache;

/**
 * @author R00900208
 *
 */
public class LRUCache 
{
    
//
//    /**
//     * fCacheMap
//     */
//    private Map<MolapSegmentHeader, Byte> fCacheMap;
//    
//    /**
//     * fCacheSize
//     */
//    private int fCacheSize;
//	
//	/**
//	 * Get instance of class
//	 * @param hashMap
//	 * @return
//	 */
//	public static synchronized LRUCache getInstance(Map<String,Map<MolapSegmentHeader, MolapSegmentBody>> hashMap)
//	{
//	    long mem = 0;
//	    try
//	    {
//    	    mem = Long.parseLong(MondrianProperties.instance().getProperty(
//                    "com.huawei.datastore.lrusize", 5000+""));
//	    }
//	    catch (NumberFormatException e) 
//	    {
//            mem = 5000;
//        }
//	    mem = MolapProperties.getInstance().validate(mem, 100000, 100, 5000);
//		return new LRUCache(3000, mem,hashMap);
//	}
//
//	/**
//	 * Instantiate LRU cache.
//	 * @param size
//	 * @param memSize
//	 * @param hashMap
//	 */
//    @SuppressWarnings("unchecked")
//    public LRUCache(int size,final long memSize,final Map<String,Map<MolapSegmentHeader, MolapSegmentBody>> hashMap)
//    {
//        fCacheSize = size;
//
//        // If the cache is to be used by multiple threads,
//        // the hashMap must be wrapped with code to synchronize 
//        fCacheMap = Collections.synchronizedMap
//        (
//            //true = use access order instead of insertion order
//            new LinkedHashMap<MolapSegmentHeader,Byte>(fCacheSize, .75F, true)
//            { 
//            	private long size;
//                @Override
//                public boolean removeEldestEntry(Map.Entry<MolapSegmentHeader, Byte> eldest)  
//                {
//                	if(size > memSize)
//                	{
//                		size --;
//                		Map<MolapSegmentHeader, MolapSegmentBody> cache = hashMap.get(eldest.getKey().getCubeName());
//                		cache.remove(eldest.getKey());
//                		return true;
//                	}
//                    //when to remove the eldest entry
//                    return false;   //size exceeded the max allowed
//                }
//                
//                public Byte put(MolapSegmentHeader key,Byte value) 
//                {
//                	size ++;
//                	return super.put(key, value);
//                }
//                
//                public void clear() 
//                {
//                    size =0;
//                    super.clear();
//                }
//            }
//        );
//    }
//
//    /**
//     * Put the key
//     * @param key
//     * @param elem
//     */
//    public void put(MolapSegmentHeader key, Byte elem)
//    {
//        fCacheMap.put(key, elem);
//    }
//
//    /**
//     * Get the key
//     * @param key
//     * @return
//     */
//    public Byte get(MolapSegmentHeader key)
//    {
//        return fCacheMap.get(key);
//    }
//    
//    /**
//     * Get headers
//     * @return
//     */
//    public List<MolapSegmentHeader> getHeaders()
//    {
//        return new ArrayList<MolapSegmentHeader>(fCacheMap.keySet());
//    }
//    
//    /**
//     * Remove key
//     * @param key
//     * @return
//     */
//    public Byte remove(MolapSegmentHeader key)
//    {
//        return fCacheMap.remove(key);
//    }
//    
//    /**
//     * To string
//     */
//    @Override
//    public String toString() {
//    	// TODO Auto-generated method stub
//    	return fCacheMap.toString();
//    }
//    
//    
////    public static void main(String[] args) 
////    {
////    	LRUCache cache = new LRUCache(1,100,null);
////    	for (long i = 0; i < 500; i++) {
////    		//cache.put(i+"", new byte[]{1,2});
////		}
////    	
////    	System.out.println(cache);
////	}
//
//    /**
//     * Clear cache
//     */
//    public void clear()
//    {
//        fCacheMap.clear();
//        fCacheSize = 0;
//    }
    
    
    
    
    
}
