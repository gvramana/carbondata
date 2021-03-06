/**
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
 */

package com.huawei.unibi.molap.api.dataloader;

/**
 * Project Name NSE V3R7C00 
 * Module Name : Molap 
 * Author V00900840 
 * Created Date:29-May-2013 4:31:02 AM 
 * FileName : SchemaInfo.java 
 * Class Description : schema info
 * class Version 1.0
 */
public class SchemaInfo
{

    /**
     * schemaName
     */
    private String schemaName;

    /**
     * srcDriverName
     */
    private String srcDriverName;

    /**
     * srcConUrl
     */
    private String srcConUrl;

    /**
     * srcUserName
     */
    private String srcUserName;

    /**
     * srcPwd
     */
    private String srcPwd;

    /**
     * cubeName
     */
    private String cubeName;

    /**
     * isAutoAggregateRequest
     */
    private boolean isAutoAggregateRequest;

    /**
     * isAggregateTableCSVLoadRequest
     */
    private boolean isAggregateTableCSVLoadRequest;

    /**
     * schemaPath
     */
    private String schemaPath;

    /**
     * schemaConnName
     */
    private String schemaConnName;
    
    /**
     * isBackgroundMergingRequest
     */
    private boolean isBackgroundMergingRequest;
    
    private String complexDelimiterLevel1;

    private String complexDelimiterLevel2;

    public String getComplexDelimiterLevel1() {
		return complexDelimiterLevel1;
	}

	public void setComplexDelimiterLevel1(String complexDelimiterLevel1) {
		this.complexDelimiterLevel1 = complexDelimiterLevel1;
	}

	public String getComplexDelimiterLevel2() {
		return complexDelimiterLevel2;
	}

	public void setComplexDelimiterLevel2(String complexDelimiterLevel2) {
		this.complexDelimiterLevel2 = complexDelimiterLevel2;
	}

    /**
     * 
     * @return Returns the srcDriverName.
     * 
     */
    public String getSrcDriverName()
    {
        return srcDriverName;
    }

    /**
     * 
     * @param srcDriverName
     *            The srcDriverName to set.
     * 
     */
    public void setSrcDriverName(String srcDriverName)
    {
        this.srcDriverName = srcDriverName;
    }

    /**
     * 
     * @return Returns the srcConUrl.
     * 
     */
    public String getSrcConUrl()
    {
        return srcConUrl;
    }

    /**
     * 
     * @param srcConUrl
     *            The srcConUrl to set.
     * 
     */
    public void setSrcConUrl(String srcConUrl)
    {
        this.srcConUrl = srcConUrl;
    }

    /**
     * 
     * @return Returns the srcUserName.
     * 
     */
    public String getSrcUserName()
    {
        return srcUserName;
    }

    /**
     * 
     * @param srcUserName
     *            The srcUserName to set.
     * 
     */
    public void setSrcUserName(String srcUserName)
    {
        this.srcUserName = srcUserName;
    }

    /**
     * 
     * @return Returns the srcPwd.
     * 
     */
    public String getSrcPwd()
    {
        return srcPwd;
    }

    /**
     * 
     * @param srcPwd
     *            The srcPwd to set.
     * 
     */
    public void setSrcPwd(String srcPwd)
    {
        this.srcPwd = srcPwd;
    }

    public String getCubeName()
    {
        return cubeName;
    }

    public void setCubeName(String cubeName)
    {
        this.cubeName = cubeName;
    }

    /**
     * @return the isAutoAggregateRequest
     */
    public boolean isAutoAggregateRequest()
    {
        return isAutoAggregateRequest;
    }

    /**
     * @param isAutoAggregateRequest
     *            the isAutoAggregateRequest to set
     */
    public void setAutoAggregateRequest(boolean isAutoAggregateRequest)
    {
        this.isAutoAggregateRequest = isAutoAggregateRequest;
    }

    /**
     * @return the isAggregateTableCSVLoadRequest
     */
    public boolean isAggregateTableCSVLoadRequest()
    {
        return isAggregateTableCSVLoadRequest;
    }

    /**
     * @param isAggregateTableCSVLoadRequest
     *            the isAggregateTableCSVLoadRequest to set
     */
    public void setAggregateTableCSVLoadRequest(
            boolean isAggregateTableCSVLoadRequest)
    {
        this.isAggregateTableCSVLoadRequest = isAggregateTableCSVLoadRequest;
    }

    /**
     * @return the schemaName
     */
    public String getSchemaName()
    {
        return schemaName;
    }

    /**
     * @param schemaName
     *            the schemaName to set
     */
    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }

    /**
     * @return the schemaPath
     */
    public String getSchemaPath()
    {
        return schemaPath;
    }

    /**
     * @param schemaPath
     *            the schemaPath to set
     */
    public void setSchemaPath(String schemaPath)
    {
        this.schemaPath = schemaPath;
    }

    /**
     * @return the schemaConnName
     */
    public String getSchemaConnName()
    {
        return schemaConnName;
    }

    /**
     * @param schemaConnName
     *            the schemaConnName to set
     */
    public void setSchemaConnName(String schemaConnName)
    {
        this.schemaConnName = schemaConnName;
    }

	/**
	 * @return the isBackgroundMergingRequest
	 */
	public boolean isBackgroundMergingRequest()
	{
		return isBackgroundMergingRequest;
	}

	/**
	 * @param isBackgroundMergingRequest the isBackgroundMergingRequest to set
	 */
	public void setBackgroundMergingRequest(boolean isBackgroundMergingRequest) 
	{
		this.isBackgroundMergingRequest = isBackgroundMergingRequest;
	}

}
