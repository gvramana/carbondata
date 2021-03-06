/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2014
 * =====================================
 *
 */
package com.huawei.unibi.molap.dataprocessor.manager;

import java.util.HashMap;
import java.util.Map;

import com.huawei.unibi.molap.constants.MolapCommonConstants;

/**
 * Project Name NSE V3R8C10 
 * Module Name : MOLAP Data Processor
 * Author :k00900841 
 * Created Date:10-Aug-2014
 * FileName : MolapDataProcessorManager.java 
 * Class Description : Manger class, for managing the data processing request
 * Version 1.0
 */
public final class MolapDataProcessorManager
{
    /**
     * instance
     */
    private static final MolapDataProcessorManager INSTANCE = new MolapDataProcessorManager();
    
    /**
     * managerHandlerMap
     */
    private Map<String,Object> managerHandlerMap;
    
    /**
     * private constructor
     */
    private MolapDataProcessorManager()
    {
        managerHandlerMap= new HashMap<String, Object>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
    }
    
    /**
     * Get instance method will be used to get the class instance 
     * @return
     */
    public static MolapDataProcessorManager getInstance()
    {
        return INSTANCE;
    }
    

    /**
     * Below method will be used to get the lock object for all the data processing request.
     * form the local map, if empty than it will update the map and return the lock object
     * @param key
     * @return
     */
    public synchronized Object getDataProcessingLockObject(String key)
    {
        Object object = managerHandlerMap.get(key);
        if(null==object)
        {
            object = new Object();
            managerHandlerMap.put(key, object);
        }
        return object;
    }
}
