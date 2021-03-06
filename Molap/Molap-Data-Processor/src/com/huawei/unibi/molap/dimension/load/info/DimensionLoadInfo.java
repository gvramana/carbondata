/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwddts1/q4bCGDA4M3dH8C2PEEMnfDqqdF4ZhcSc
1BeEnD1TexCrh3BYUMJJf9QcpL4JQH9tlS1kZo2HODl0XL2W4kYsH7fYaEknGQUNalhOWZ7B
O60xIHr6OIkOMW/pEtCRbCsi/49h9RNlD6lsbiqg3mwwtfq6+mUqBEaYfqmv4Q==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
*/

package com.huawei.unibi.molap.dimension.load.info;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.keygenerator.KeyGenerator;
import com.huawei.unibi.molap.schema.metadata.HierarchiesInfo;
import com.huawei.unibi.molap.surrogatekeysgenerator.csvbased.MolapCSVBasedDimSurrogateKeyGen;
import com.huawei.unibi.molap.surrogatekeysgenerator.csvbased.MolapCSVBasedSeqGenMeta;

/**
 * Project Name NSE V3R7C00 
 * Module Name : 
 * Author V00900840
 * Created Date :14-Nov-2013 6:57:43 PM
 * FileName : DimensionLoadInfo.java
 * Class Description :
 * Version 1.0
 */
public class DimensionLoadInfo
{
    /**
     * Hierarchies Info
     */
    private List<HierarchiesInfo> hierVOlist;
    
    /**
     * Surrogate keyGen
     */
    private MolapCSVBasedDimSurrogateKeyGen surrogateKeyGen;
    
    /**
     * modifiedDimesions
     */
    private String modifiedDimesions;
    
    /**
     * Map of Connection
     */
    private Map<String, Connection> cons = new HashMap<String, Connection>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);
    
    /**
     * dimFileLocDir
     */
    private String dimFileLocDir;
    
    /**
     * MolapCSVBasedSeqGenMeta
     */
    private MolapCSVBasedSeqGenMeta meta;
    
    /**
     * dimTableNames
     */
    private String[] dimTableNames;
    
    /**
     * drivers
     */
    private Map<String, String> drivers;
    
    /**
     * keyGenerator
     */
    private Map<String,KeyGenerator> keyGeneratorMap;
    
    /**
     * Dimcardinality
     */
    private int[] dimCardinality;

    /**
     * 
     * @return Returns the hierVOlist.
     * 
     */
    public List<HierarchiesInfo> getHierVOlist()
    {
        return hierVOlist;
    }

    /**
     * 
     * @param hierVOlist The hierVOlist to set.
     * 
     */
    public void setHierVOlist(List<HierarchiesInfo> hierVOlist)
    {
        this.hierVOlist = hierVOlist;
    }

    /**
     * 
     * @return Returns the surrogateKeyGen.
     * 
     */
    public MolapCSVBasedDimSurrogateKeyGen getSurrogateKeyGen()
    {
        return surrogateKeyGen;
    }

    /**
     * 
     * @param surrogateKeyGen The surrogateKeyGen to set.
     * 
     */
    public void setSurrogateKeyGen(MolapCSVBasedDimSurrogateKeyGen surrogateKeyGen)
    {
        this.surrogateKeyGen = surrogateKeyGen;
    }

    /**
     * 
     * @return Returns the modifiedDimesions.
     * 
     */
    public String getModifiedDimesions()
    {
        return modifiedDimesions;
    }

    /**
     * 
     * @param modifiedDimesions The modifiedDimesions to set.
     * 
     */
    public void setModifiedDimesions(String modifiedDimesions)
    {
        this.modifiedDimesions = modifiedDimesions;
    }

    /**
     * 
     * @return Returns the cons.
     * 
     */
    public Map<String, Connection> getCons()
    {
        return cons;
    }

    /**
     * 
     * @param cons The cons to set.
     * 
     */
    public void setCons(Map<String, Connection> cons)
    {
        this.cons = cons;
    }

    /**
     * 
     * @return Returns the meta.
     * 
     */
    public MolapCSVBasedSeqGenMeta getMeta()
    {
        return meta;
    }

    /**
     * 
     * @param meta The meta to set.
     * 
     */
    public void setMeta(MolapCSVBasedSeqGenMeta meta)
    {
        this.meta = meta;
    }

    /**
     * 
     * @return Returns the dimFileLocDir.
     * 
     */
    public String getDimFileLocDir()
    {
        return dimFileLocDir;
    }

    /**
     * 
     * @param dimFileLocDir The dimFileLocDir to set.
     * 
     */
    public void setDimFileLocDir(String dimFileLocDir)
    {
        this.dimFileLocDir = dimFileLocDir;
    }

    /**
     * 
     * @return Returns the dimTableNames.
     * 
     */
    public String[] getDimTableNames()
    {
        return dimTableNames;
    }

    /**
     * 
     * @param dimTableNames The dimTableNames to set.
     * 
     */
    public void setDimTableNames(String[] dimTableNames)
    {
        this.dimTableNames = dimTableNames;
    }

    /**
     * 
     * @return Returns the drivers.
     * 
     */
    public Map<String, String> getDrivers()
    {
        return drivers;
    }

    /**
     * 
     * @param drivers The drivers to set.
     * 
     */
    public void setDrivers(Map<String, String> drivers)
    {
        this.drivers = drivers;
    }

    /**
     * @return the dimCardinality
     */
    public int[] getDimCardinality()
    {
        return dimCardinality;
    }

    /**
     * @param dimCardinality the dimCardinality to set
     */
    public void setDimCardinality(int[] dimCardinality)
    {
        this.dimCardinality = dimCardinality;
    }

    /**
     * get key generator.
     * 
     * @return
     *
     */
    public Map<String, KeyGenerator> getKeyGenerator()
    {
        return keyGeneratorMap;
    }

    /**
     * Set key generator.
     * 
     * @param keyGenMap
     *
     */
    public void setKeyGeneratorMap(Map<String, KeyGenerator> keyGenMap)
    {
        this.keyGeneratorMap = keyGenMap;
    }
    
    
    
}

