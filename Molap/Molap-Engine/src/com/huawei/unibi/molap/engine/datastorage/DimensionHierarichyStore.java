/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwdh/HjOjN0Brs7b7TRorj6S6iAIeaqK90lj7BAM
GSGxBmp06KryXpQHqSFui2gCm5Pfla1wTGsYpR0B+DkWYBRCOhwjuGPrW9Y4jRsasbkP85uk
Pa6ACSnr7XtTKCkOtfyMRePRWYopF9B4Ve9HIbfV6MsTKhQjI4vKPJbFZ1ZDBQ==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * 
 */
package com.huawei.unibi.molap.engine.datastorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.engine.util.MolapEngineLogEvent;
import com.huawei.unibi.molap.metadata.MolapSchemaReader;
import com.huawei.unibi.molap.olap.MolapDef;

/**
 * @author R00900208
 */
public class DimensionHierarichyStore
{
    /**
     * Hierarchy name --> HierarchyCache
     * 
     * Maintains all the hierarchies
     */
    private Map<String, HierarchyStore> hiers = new HashMap<String, HierarchyStore>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);

    /**
     * Can hold members, each level
     * 
     * column name and the cache
     */
    private Map<String, MemberStore> membersCache;

    /**
     * 
     */
    private String cubeName;

    /**
     * Attribute for Molap LOGGER
     */
    private static final LogService LOGGER = LogServiceFactory.getLogService(DimensionHierarichyStore.class.getName());

    /**
     * @return
     */
    public String getCubeName()
    {
        return cubeName;
    }

    public DimensionHierarichyStore(MolapDef.CubeDimension dimension, Map<String, MemberStore> membersCache, String cubeName,
            String factTableName, MolapDef.Schema schema)
    {
        this.membersCache = membersCache;
        this.cubeName = cubeName;

        com.huawei.unibi.molap.olap.MolapDef.Hierarchy[] extractHierarchies = MolapSchemaReader.extractHierarchies(schema, dimension);
        if(null != extractHierarchies)
        {
            for(com.huawei.unibi.molap.olap.MolapDef.Hierarchy hierarchy : extractHierarchies)
            {
                String hName = hierarchy.name == null ? dimension.name : hierarchy.name;

                hiers.put(hName, new HierarchyStore(hierarchy, factTableName, dimension.name));

                for(MolapDef.Level level : hierarchy.levels)
                {
                    String tableName = hierarchy.relation == null ? factTableName
                            : ((MolapDef.Table)hierarchy.relation).name;
                    // Store empty members
                    // if(!level.isAll())
                    // {
                    MemberStore memberCache = new MemberStore(level, tableName);
                    membersCache.put(memberCache.getTableForMember() + '_' + dimension.name + '_' + hName, memberCache);
                    // }
                }
            }
        }
    }
    
    /**
     * This method will unload the level file from memory
     * 
     */
    public void unloadLevelFile(String tableName, String dimName, String heirName, String levelActualName)
    {
        MemberStore memberStore = membersCache.get(tableName + '_' + levelActualName + '_' + dimName + '_' + heirName);
        memberStore.clear();
    }

    /**
     * Access the dimension members through levelName
     */
    public MemberStore getMemberCache(String levelName)
    {
        return membersCache.get(levelName);
    }
//Raghu check if  this works
    public Member getMember(int key, String tableName)
    {
        return membersCache.get(tableName).getMemberByID(key);
    }

    /**
     * @return the hiers
     */
    public HierarchyStore getHier(String hier)
    {
        return hiers.get(hier);
    }

    /**
     * @param hiers
     *            the hiers to set
     */
    public void setHiers(String hierName, HierarchyStore hiers)
    {
        this.hiers.put(hierName, hiers);
    }

    /**
     * Process cache from database store
     */
    /*public void processCache(DataSource datasource)
    {
        try
        {

            // Process hierarchies cache
            for(HierarchyStore hCache : hiers.values())
            {
                Level[] levels = hCache.getRolapHierarchy().getLevels();
                List<String> dimNames = new ArrayList<String>();
                int depth = 0;
                for(int i = 0;i < levels.length;i++)
                {
                    RolapLevel level3 = (RolapLevel)levels[i];
                    if(!level3.isAll())
                    {
                        depth++;

                        // Process level cache
                        String memberKey = ((MondrianDef.Column)level3.getKeyExp()).name;
                        MemberStore membercache = membersCache.get(memberKey);

                        DimensionCacheLoader.loadMembersFromDataSource(membercache, datasource);

                        dimNames.add(memberKey);
                    }
                }

                if(depth > 1)
                {
                    DimensionCacheLoader.loadHierarichyFromDataSource(hCache, datasource);
                }
                // else
                // {
                // // Hierarchy with single level can be loaded directly from
                // // level
                // // member cache. So, make it null here.
                // // hCache.setDimension(null);
                // }
            }

        }
        catch(IOException e)
        {
            LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, e, 
                    "IOException happened while processing cache");
        }
    }*/

    /**
     * Process all hierarchies and members of each level to load cache.
     * 
     * @param slice
     * @param fileStore
     * 
     * @return false if any problem during cache load
     */
    public boolean processCacheFromFileStore(final String fileStore,ExecutorService executorService)
    {
        try
        {
            // Process hierarchies cache
            for(final HierarchyStore hCache : hiers.values())
            {
                MolapDef.Level[] levels = hCache.getRolapHierarchy().levels;
                final List<String> dimNames = new ArrayList<String>(MolapCommonConstants.CONSTANT_SIZE_TEN);
                int depth = 0;
                final String tableName =  hCache.getRolapHierarchy().relation == null ? hCache.getFactTableName()
                        : ((MolapDef.Table)hCache.getRolapHierarchy().relation).name;
                for(int i = 0;i < levels.length;i++)
                {
                    final MolapDef.Level tempLevel = levels[i];
//                    if(!level3.isAll())
//                    {
                        depth++;
                        executorService.submit(new Callable<Void>()
                        {

                            @Override
                            public Void call() throws Exception
                            {
                                loadDimensionLevels(fileStore, hCache, dimNames, tempLevel,hCache.getHierName(),tableName,hCache.getDimensionName());
                                return null;
                            }
                            
                        });

//                    }
                }

                if(depth > 1)
                {
                    DimensionCacheLoader.loadHierarichyFromFileStore(hCache, fileStore);
                }
                // else
                // {
                    // Hierarchy with single level can be loaded directly from
                    // level member cache. So, make it null here.
                    // hCache.setDimension(null);
                // }
            }
        }
        catch(IOException e)
        {
            LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG, e);
            return false;
        }

        return true;
    }

    /**
     * @param fileStore
     * @param hCache
     * @param dimNames
     * @param level3
     */
    private void loadDimensionLevels(String fileStore, HierarchyStore hCache, List<String> dimNames,
            MolapDef.Level level3, String hierarchyName, String tableName, String dimensionName)
    {
  
        // Process level cache
        if(hierarchyName.contains("."))
        {
            hierarchyName = hierarchyName.substring(hierarchyName.indexOf(".") + 1, hierarchyName.length());
        }
        String memberKey = tableName+'_'+level3.column + '_' + dimensionName + '_' + hierarchyName;
        MemberStore membercache = membersCache.get(memberKey);
        if(null == membercache.getAllMembers())
        {
            DimensionCacheLoader.loadMemberFromFileStore(membercache, fileStore, level3.type,
                    hCache.getFactTableName(), tableName);
            dimNames.add(memberKey);
        }
    }

//    /**
//     * Process all hierarchies and members of each level to load cache.
//     * 
//     * @param slice
//     * @param fileStore
//     * 
//     * @return false if any problem during cache load
//     */
//    public boolean processCacheFromSlice(List<InMemoryCube> slices, String fileStore)
//    {
//        // Process hierarchies cache
//        for(HierarchyStore hCache : hiers.values())
//        {
//            Level[] levels = hCache.getRolapHierarchy().getLevels();
//            List<String> dimNames = new ArrayList<String>();
//            int depth = 0;
//            for(int i = 0;i < levels.length;i++)
//            {
//                RolapLevel level3 = (RolapLevel)levels[i];
//                if(!level3.isAll())
//                {
//                    depth++;
//
//                    // Process level cache
//                    String hierarchyName = level3.getHierarchy().getName();
//                    if(hierarchyName.contains("."))
//                    {
//                        hierarchyName = hierarchyName.substring(hierarchyName.indexOf(".") + 1, hierarchyName.length());
//                    }
//                    String memberKey = ((MondrianDef.Column)level3.getKeyExp()).table + '_'
//                            + ((MondrianDef.Column)level3.getKeyExp()).name + '_' + level3.getDimension().getName()
//                            + '_' + hierarchyName;
//                    MemberStore membercache = membersCache.get(memberKey);
//
//                    List<MemberStore> memberStores = new ArrayList<MemberStore>();
//                    for(InMemoryCube slice : slices)
//                    {
//                        memberStores.add(slice.getMemberCache(memberKey));
//                    }
//
//                    DimensionCacheLoader.loadMemberFromSlices(membercache, memberStores, fileStore,level3.getDatatype().name(),hCache.getFactTableName());
//
//                    dimNames.add(memberKey);
//                }
//            }
//
//            if(depth > 1)
//            {
//                List<HierarchyStore> hStores = new ArrayList<HierarchyStore>();
//                for(InMemoryCube slice : slices)
//                {
//                    hStores.add(slice.getDimensionAndHierarchyCache(hCache.getDimensionName()).getHier(
//                            hCache.getRolapHierarchy().getSubName()==null?hCache.getRolapHierarchy().getName():hCache.getRolapHierarchy().getSubName()));
//                }
//
//                DimensionCacheLoader.loadHierarchyFromSlice(hCache, hStores, fileStore);
//            }
//            // else
//            // {
//                // Hierarchy with single level can be loaded directly from level
//                // member cache. So, make it null here.
//                // hCache.setDimension(null);
//            // }
//        }
//
//        return true;
//    }

    // public static void main(String[] args) throws IOException
    // {
    //
    // String name = "ggsn";
    // HierarchyStore hie = new HierarchyStore(null);
    // // hie.setHierName("protocol");
    // // hie.setHierName("time");
    // hie.setHierName(name);
    //
    // // HbaseDataSource hbaseDataSource = new
    // // HbaseDataSource("jdbc:hbase://10.124.19.125:2181");
    // // HTablePool hTablePool = hbaseDataSource.getTablePool();
    //
    // // String[] dims = new String[] {"year","month","day", "hour"};
    // // String[] dims = new String[] {"prot_cat","prot_id"};
    // String[] dims = new String[]{"ggsn"};
    // // new DimensionCacheLoader(hTablePool).loadHierarichy(hie, dims);
    //
    // MemberStore memCache = new MemberStore(null);
    // memCache.setLevelName(name);
    //
    // // new DimensionCacheLoader(hbaseDataSource).loadMemberCache(memCache);
    //
    // // Map<Integer,String> maps =
    // // getMappingMems(hie.getDimension().keySet(), "prot_cat", hTablePool);
    //
    // // TODO this will not work until we load cube cache correctly
    // // InMemoryQueryExecutor inMemoryExecutor = new
    // // InMemoryQueryExecutor(null);
    //
    // Map<Integer, List<String>> cons = new HashMap<Integer, List<String>>();
    // // cons.put(0, Arrays.asList(new String[]{"2"}));
    //
    // // System.out.println(hie.getCache().keySet());
    //
    // // HIterator hIterator = new HIterator();
    // // inMemoryExecutor.executeHier(hie.getHierName(), new
    // // Integer[]{2,0,1,3}, Arrays.asList(dims), cons, hIterator);
    // // inMemoryExecutor.executeDimension("cube", "dimension",
    // // hie.getHierName(), hie.getHierName(), new Integer[]{0}, cons,
    // // hIterator);
    //
    // // for(Object[] row:hIterator.getData())
    // // {
    // //
    // // for(Object value : row)
    // // {
    // // String spacer = "  ";
    // // //TODO bad way of identifying the spacer even for sysop
    // // if(Integer.valueOf(String.valueOf(value))>10) spacer=" ";
    // //
    // // System.out.printf(value + spacer);
    // // }
    // // System.out.println(",");
    // // }
    // }

}
