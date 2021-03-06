/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwdXNiZ+oxCgSX2SR8ePIzMmJfU7u5wJZ2zRTi4X
XHfqbYhEjTE3WD2TY05AEmzrfHcXhkrchEOBy4PSigQM+p6oUFJt+mLjp10il/rEV4YYuMqW
yUtXLsOjd7kq3M0C5PjfLUMPeUOhhnwXwvLML5SVwG3kSWKgHjyPv8nSqVxIGw==*/
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
package com.huawei.unibi.molap.util;

import java.io.File;

import com.huawei.unibi.molap.exception.MolapDataProcessorException;

/**
 * 
 * Project Name NSE V3R7C00 
 * Module Name : Molap Data Processor
 * Author K00900841
 * Created Date :21-May-2013 6:42:29 PM
 * FileName : MolapDirectoryCreater.java
 * Class Description : This class is responsible for creating the molap Directory 
 *                      all the methods are synchronized, to avoid creation of duplicate directory
 * Version 1.0
 */
public final class MolapDirectoryCreater
{
    
    /**
     * will be used to get the lock on put row method
     */
    private static final Object GETRSLOCK = new Object();
    
    private MolapDirectoryCreater()
    {
    	
    }
    
    /**
     * This method will be used to create the RS folder 
     * 
     * @param baseStorePath
     *          base path
     * @return created path
     * @throws MolapDataProcessorException
     *          MolapDataProcessorException
     *
     */
    public static String createRSDirectory(String baseStorePath) throws MolapDataProcessorException
    {
        synchronized(GETRSLOCK)
        {
            File file = null;
            file = new File(baseStorePath);
            if(!file.exists() && !file.mkdirs())
            {
				throw new MolapDataProcessorException(
						"Problem while creating the RS Directory: "
								+ baseStorePath);
            }
            return file.getAbsolutePath();
        }
    }
}
