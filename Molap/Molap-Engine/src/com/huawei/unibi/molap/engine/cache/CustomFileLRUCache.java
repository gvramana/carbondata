/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwdEVzw1icjfRowqz2DW4XzUpEhhSzBOwVynEHjc
u0090cEu54zK+p9B21AFDfEYsYsFMm34R5yUf22PezvX03tlIkfB3c4cwnX/gIosXp98Apev
aLvZ0TbGYVc4Gm8CueP8BmaBwSSGzQjnN960eyvuPNnNqyGTMXP+QoWLPZw0Ug==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
package com.huawei.unibi.molap.engine.cache;

/**
 * @author R00900208
 *
 */
public class CustomFileLRUCache 
{
//    
//
//    /**
//     * fCacheMap
//     */
//    private Map<MolapSegmentHeader, Byte> fCacheMap;
//    
//    private static final LogService LOGGER = LogServiceFactory.getLogService(CustomFileLRUCache.class.getName());
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
//	public static synchronized CustomFileLRUCache getInstance(Map<String,Map<MolapSegmentHeader,String>> hashMap)
//	{
//	    long mem = Long.parseLong(MondrianProperties.instance().getProperty(
//                "com.huawei.datastore.lrusize", 5000+""));
//		return new CustomFileLRUCache(3000, mem,hashMap);
//	}
//
//	/**
//	 * Instantiate LRU cache.
//	 * @param size
//	 * @param memSize
//	 * @param hashMap
//	 */
//    @SuppressWarnings("unchecked")
//    public CustomFileLRUCache(int size,final long memSize,final Map<String,Map<MolapSegmentHeader,String>> hashMap)
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
//                		Map<MolapSegmentHeader, String> cache = hashMap.get(eldest.getKey().getCubeName());
//                		String path = cache.remove(eldest.getKey());
//                		boolean delete = new File(path).delete();
//                		 if(!delete)
//                         {
//                             LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
//                                     "Custom File Lru cache removal is failed for "
//                                             + path);
//                             return false;
//                         }
//                         else
//                         {
//                             LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
//                                     "Custom File Lru cache removes the entry " + path);
//                             return true;
//                         }
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
