package com.huawei.unibi.molap.dataprocessor.dataretention;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.huawei.datasight.molap.core.load.LoadMetadataDetails;
import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.constants.MolapCommonConstants;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionModel;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFile;
import com.huawei.unibi.molap.datastorage.store.impl.FileFactory;
import com.huawei.unibi.molap.exception.MolapDataProcessorException;
import com.huawei.unibi.molap.factreader.FactReaderInfo;
import com.huawei.unibi.molap.factreader.MolapSurrogateTupleHolder;
import com.huawei.unibi.molap.factreader.columnar.MolapColumnarLeafTupleIterator;
import com.huawei.unibi.molap.keygenerator.KeyGenerator;
import com.huawei.unibi.molap.keygenerator.factory.KeyGeneratorFactory;
import com.huawei.unibi.molap.metadata.LeafNodeInfoColumnar;
import com.huawei.unibi.molap.metadata.SliceMetaData;
import com.huawei.unibi.molap.schema.metadata.MolapColumnarFactMergerInfo;
import com.huawei.unibi.molap.store.MolapFactDataHandlerColumnarMerger;
import com.huawei.unibi.molap.store.writer.exception.MolapDataWriterException;
import com.huawei.unibi.molap.util.MolapDataProcessorLogEvent;
import com.huawei.unibi.molap.util.MolapUtil;
import com.huawei.unibi.molap.util.MolapUtilException;
import com.huawei.unibi.molap.util.ValueCompressionUtil;

public class DataRetentionHandler
{

    private String schemaName;

    private String cubeName;

    private String hdsfStoreLocation;

    private String columnName;

    private String columnValue;

    private String tableName;

    private int surrogateKeyIndex;

    private KeyGenerator keyGenerator;

    private int mdKeySize;

    private int measureLength;

    private String loadSliceLocation;

    private SliceMetaData sliceMetadata;

    private long[] surrogateKeyArray;

    private MolapFile[] factFiles;

    private int[] compareIndex;

    // private boolean isSurrogateKeyPresent;
    private int retentionSurrogateKey = -1;

    private int[] dimensionCardinality;

    private Map<Integer, Integer> retentionSurrogateKeyMap;

    /**
     * rsFiles.
     */
    private MolapFile[] loadFiles;

    private String dateFormat;

    private int currentRestructNumber;

	private String columnActualName;

	private List<LoadMetadataDetails> listOfLoadMetadataDetails;

	private String dimensionTableName;

    /**
     * 
     * Comment for <code>LOGGER</code>
     * 
     */
    private static final LogService LOGGER = LogServiceFactory
            .getLogService(DataRetentionHandler.class.getName());

    public DataRetentionHandler(String schemaName, String cubeName,
            String tableName,String dimensionTableName, String hdsfStoreLocation, String columnName,String columnActualName,
            String columnValue, String dateFormat, int currentRestructNum,List<LoadMetadataDetails> listOfLoadMetadataDetails)
    {
        this.schemaName = schemaName;
        this.cubeName = cubeName;
        this.hdsfStoreLocation = hdsfStoreLocation + '/' + "store";
        this.columnName = columnName;
        this.columnActualName = columnActualName;
        this.columnValue = columnValue;
//        this.loadFiles = null;
        this.tableName = tableName;
        this.dateFormat = dateFormat;
        this.currentRestructNumber = currentRestructNum;
        this.listOfLoadMetadataDetails=listOfLoadMetadataDetails;
        this.dimensionTableName=dimensionTableName;
    }

    /**
     * updateFactFileBasedOnDataRetentionPolicy.This API will apply the data
     * retention policy in store and find whether the complete load has been
     * undergone for deletion or updation.
     * 
     * @return Map<String, String> , Data load name and status
     * @throws MolapDataProcessorException
     */
    public Map<String, String> updateFactFileBasedOnDataRetentionPolicy()
            
    {
        Map<String, String> loadDetails = new HashMap<String, String>();
        for(int restrucureNum = currentRestructNumber; restrucureNum >=0; restrucureNum--)
        {
            
            loadFiles = MolapDataRetentionUtil.getAllLoadFolderSlices(
                    schemaName, cubeName, tableName, this.hdsfStoreLocation,
                    restrucureNum);

            if(null == loadFiles)
            {
               /* loadDetails = new HashMap<String, String>(20);
                loadDetails.put("", "");
                return loadDetails;*/
                continue;
            }
//            loadDetails = new HashMap<String, String>(loadFiles.length);
            LOGGER.info(
                    MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                    "System is going Data retention policy based on member"
                            + columnValue + " For column:" + columnName);

            String sliceMetadataLocation = MolapUtil.getRSPath(schemaName,
                    cubeName, tableName, hdsfStoreLocation,
                    restrucureNum);
            // int rsCounter = MolapUtil.checkAndReturnNextRestructFolderNumber(
            // hdsfStoreLocation, "RS_");
            // if (rsCounter == -1) {
            // rsCounter = 0;
            // }
            sliceMetadata = MolapUtil.readSliceMetaDataFile(
                    sliceMetadataLocation, restrucureNum);

            loadFiles = MolapUtil.getSortedFileList(loadFiles);
            // Getting the details as per retention member for applying data
            // retention policy.
            for(MolapFile molapFile : loadFiles)
            {
				if(isLoadFolderDeleted(molapFile))
				{
					continue;
				}
                factFiles = MolapUtil.getAllFactFiles(
                        molapFile.getAbsolutePath(), tableName,
                        FileFactory.getFileType(molapFile.getAbsolutePath()));
                try
                {
                    dimensionCardinality = MolapUtil
                            .getCardinalityFromLevelMetadataFile(molapFile
                                    .getAbsolutePath()
                                    + '/'
                                    + MolapCommonConstants.LEVEL_METADATA_FILE
                                    + tableName + ".metadata");
                    if(null == dimensionCardinality)
                    {
                        continue;
                    }
                }
                catch(MolapUtilException e)
                {
                    LOGGER.error(
                            MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                            "Failed to apply retention policy.", e);
                }

                applyRetentionDetailsBasedOnRetentionMember();

            // skip deleted and merged load folders.
            if(!isLoadValid(listOfLoadMetadataDetails,molapFile.getName()))
            {
                continue;
            }
                /*
                 * if(retentionSurrogateKey==-1) { throw new
                 * MolapDataProcessorException("Invalid Date Member..."); }
                 */
                factFiles = MolapUtil.getSortedFileList(factFiles);
                loadSliceLocation = molapFile.getAbsolutePath();
                try
                {
                    for(MolapFile factFile : factFiles)
                    {

                        applyDataRetentionPolicy(factFile.getAbsolutePath(),
                                loadDetails, molapFile.getName(),
                                molapFile.getAbsolutePath(),restrucureNum);

                    }
                }
                catch(MolapDataProcessorException e)
                {
                    LOGGER.error(
                            MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                            "Failed to apply retention policy.", e);
                }
            }

        }
        return loadDetails;

    }

	private boolean isLoadFolderDeleted(MolapFile molapFile) {
		boolean status=false;
		for (LoadMetadataDetails loadMetadata : listOfLoadMetadataDetails) {

			if (loadMetadata.getLoadName().equals(
					molapFile.getName().substring(
							molapFile.getName().lastIndexOf('_') + 1,
							molapFile.getName().length()))
					&& MolapCommonConstants.MARKED_FOR_DELETE
							.equalsIgnoreCase(loadMetadata.getLoadStatus())) {
				status=true;
				break;
			}
		}
		return status;
	}

	 /**
    * 
    * @param loadMetadataDetails2
    * @param name
    * @return
    */
    private boolean isLoadValid(
            List<LoadMetadataDetails> loadMetadataDetails2, String name)
    {
        String loadName = name.substring(name.indexOf(MolapCommonConstants.LOAD_FOLDER)+MolapCommonConstants.LOAD_FOLDER.length(), name.length());
        
        for(LoadMetadataDetails loads : loadMetadataDetails2)
        {
            if(loads.getLoadName().equalsIgnoreCase(loadName))
            {
                if(null != loads.getMergedLoadName())
                {
                    return false;
                }
                else if(loads.getLoadStatus().equalsIgnoreCase(MolapCommonConstants.MARKED_FOR_DELETE))
                {
                    return false;
                }
                return true;
            }
            else if( null != loads.getMergedLoadName() && loads.getMergedLoadName().equalsIgnoreCase(loadName) && !loads.getLoadStatus().equalsIgnoreCase(MolapCommonConstants.MARKED_FOR_DELETE))
            {
                return true;
            }
        }
        
        
        return false;
    }
    private void applyRetentionDetailsBasedOnRetentionMember()
    {
        measureLength = sliceMetadata.getMeasures().length;
        String[] dimensions = sliceMetadata.getDimensions();

        int dimensionLenghts = dimensions.length;
        surrogateKeyArray = new long[dimensionLenghts];
        Arrays.fill(surrogateKeyArray, 0);
        StringBuilder fileNameSearchPattern = new StringBuilder();

        // Forming the member name for reading the same inorder to get the
        // surrogate
        // key.tableName.append(table).append('_').append(levelColName).append(MolapCommonConstants.LEVEL_FILE_EXTENSION);
        if(!columnActualName.equals(columnName))
        {
        	if(null==dimensionTableName|| "".equals(dimensionTableName))
        	{
                fileNameSearchPattern.append(tableName).append('_').append(columnActualName)
                .append(MolapCommonConstants.LEVEL_FILE_EXTENSION);
                columnName=columnActualName;
        	}
        	else
        	{
                fileNameSearchPattern.append(dimensionTableName).append('_').append(columnActualName)
                .append(MolapCommonConstants.LEVEL_FILE_EXTENSION);
                columnName=columnActualName;
        	}
        }
        else
        {
        	if(null==dimensionTableName|| "".equals(dimensionTableName))
        	{
                fileNameSearchPattern.append(tableName).append('_').append(columnName)
                .append(MolapCommonConstants.LEVEL_FILE_EXTENSION);
        	}
        	else
        	{
                fileNameSearchPattern.append(dimensionTableName).append('_').append(columnName)
                .append(MolapCommonConstants.LEVEL_FILE_EXTENSION);
        	}
        }
        retentionSurrogateKeyMap = new HashMap<Integer, Integer>(1);
        MolapFile[] rsLoadFiles;
        for(int restrucureNum = currentRestructNumber; restrucureNum >=0; restrucureNum--)
        {
        
        	rsLoadFiles = MolapDataRetentionUtil.getAllLoadFolderSlices(
                    schemaName, cubeName, tableName, this.hdsfStoreLocation,
                    restrucureNum);
            for(MolapFile molapFile : rsLoadFiles)
            {
                MolapFile[] molapLevelFile = MolapDataRetentionUtil.getFilesArray(
                        molapFile.getAbsolutePath(),
                        fileNameSearchPattern.toString());
                // Retrieving the surrogate key.
                if(molapLevelFile.length > 0)
                {

                    MolapDataRetentionUtil.getSurrogateKeyForRetentionMember(
                            molapLevelFile[0], columnName, columnValue, dateFormat,
                            retentionSurrogateKeyMap);
                    /*
                     * Iterator itr=retentionSurrogateKeyMap.entrySet().iterator();
                     * while(itr.hasNext()) { Map.Entry<Integer,Integer>
                     * entry=(Map.Entry<Integer,Boolean>) itr.next();
                     * retentionSurrogateKey= entry.getKey();
                     * if(retentionSurrogateKey==-1) { continue; } else
                     * if(retentionSurrogateKey>-1 && !entry.getValue()) {
                     * isSurrogateKeyPresent=false; } else
                     * if(retentionSurrogateKey>-1) { isSurrogateKeyPresent=true; }
                     * } if(isSurrogateKeyPresent) { break; }
                     */
                }
            }
        	
        }

        List<Integer> compareIndexList = new ArrayList<Integer>(16);
        for(int i = 0;i < dimensionLenghts;i++)
        {
            if(dimensions[i].equals(tableName + '_' + columnName))
            {
                surrogateKeyArray[i] = retentionSurrogateKey;
                compareIndexList.add(i);
                surrogateKeyIndex = i;
                break;
            }
        }
        compareIndex = new int[compareIndexList.size()];
        for(int i = 0;i < compareIndex.length;i++)
        {
            compareIndex[i] = compareIndexList.get(i);
        }

        keyGenerator = KeyGeneratorFactory
                .getKeyGenerator(dimensionCardinality);
        mdKeySize = keyGenerator.getKeySizeInBytes();
    }

    private void applyDataRetentionPolicy(String absoluteFactFilePath,
            Map<String, String> loadDetails, String loadName, String loadPath, int restrucureNum)
            throws MolapDataProcessorException
    {
        if(null != loadName)
        {
            loadName = loadName.substring(loadName.indexOf('_') + 1,
                    loadName.length());
        }
        List<LeafNodeInfoColumnar> leafNodeInfoList = null;
        String fileToBeUpdated = null;
        for(int i = 0;i < factFiles.length;i++)
        {
        	

            leafNodeInfoList = MolapUtil.getLeafNodeInfoColumnar(factFiles[i],
                    measureLength, mdKeySize);

            /*
             * if (compare(keyGenerator.getKeyArray(leafNodeInfoList.get(0)
             * .getStartKey()), surrogateKeyArray) < 0 && compare(
             * keyGenerator.getKeyArray(leafNodeInfoList.get(
             * leafNodeInfoList.size() - 1).getEndKey()), surrogateKeyArray) <=
             * 0) { // The files can be successfully deleted
             * fileToBeDeleted.add(factFiles[i]); } else if
             * (compare(keyGenerator.getKeyArray(leafNodeInfoList.get(0)
             * .getStartKey()), surrogateKeyArray) < 0 && compare(
             * keyGenerator.getKeyArray(leafNodeInfoList.get(
             * leafNodeInfoList.size() - 1).getEndKey()), surrogateKeyArray) >=
             * 0) {
             */
            fileToBeUpdated = factFiles[i].getAbsolutePath();
            // break;
            // }

            /*
             * if (factFiles.length == fileToBeDeleted.size()) {
             * LOGGER.info(MolapDataProcessorLogEvent
             * .UNIBI_MOLAPDATAPROCESSOR_MSG,
             * "Following load file will be marked for deletion: " +loadName);
             * loadDetails.put(loadName,
             * MolapCommonConstants.MARKED_FOR_DELETE); }
             */
            if(null != fileToBeUpdated)
            {
                try
                {
                    LOGGER.info(
                            MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                            "Following load file will be marked for update: "
                                    + loadName);
                    loadDetails.put(loadName,
                            MolapCommonConstants.MARKED_FOR_UPDATE);
                    processFactFileAsPerFileToBeUpdatedDetails(
                            leafNodeInfoList, fileToBeUpdated, loadPath,
                            loadDetails, loadName, restrucureNum);
                    // loadDetails.put(loadName,
                    // MolapCommonConstants.MARKED_FOR_UPDATE);
                }
                catch(MolapDataProcessorException e)
                {
                    // TODO Auto-generated catch block
                    throw new MolapDataProcessorException(e.getMessage());
                }
            }
        }
    }

    private void processFactFileAsPerFileToBeUpdatedDetails(
            List<LeafNodeInfoColumnar> leafNodeInfoList,
            String fileToBeUpdated, String loadPath,
            Map<String, String> loadDetails, String loadName, int restructureNumber)
            throws MolapDataProcessorException
    {
        // FileHolder fileHolder = new FileHolderImpl();
        // int leafNodeInfoIndex =
        // getLeafNodeInfoIndexForFactFileUpdation(leafNodeInfoList);
        // LeafNodeInfoColumnar leafNodeInfoColumnar =
        // leafNodeInfoList.get(leafNodeInfoIndex);
        // int numberOfKeys = leafNodeInfoColumnar.getNumberOfKeys();

        ValueCompressionModel valueCompressionModel = ValueCompressionUtil
                .getValueCompressionModel(loadSliceLocation
                        + MolapCommonConstants.MEASURE_METADATA_FILE_NAME
                        + tableName
                        + MolapCommonConstants.MEASUREMETADATA_FILE_EXT,
                        measureLength);
        try
        {
            FactReaderInfo factReaderInfo = getFactReaderInfo();
            // Passing the leafNodeInfoColumnar which already been derived for
            // further processing.
            MolapColumnarLeafTupleIterator columnarLeafTupleItr = new MolapColumnarLeafTupleIterator(
                    loadPath, factFiles, factReaderInfo, mdKeySize);
            MolapColumnarFactMergerInfo molapColumnarFactMergerInfo = new MolapColumnarFactMergerInfo();
            molapColumnarFactMergerInfo.setCubeName(cubeName);
            molapColumnarFactMergerInfo.setSchemaName(schemaName);
            molapColumnarFactMergerInfo.setDestinationLocation(loadPath);
            molapColumnarFactMergerInfo.setDimLens(sliceMetadata.getDimLens());
            molapColumnarFactMergerInfo.setMdkeyLength(keyGenerator
                    .getKeySizeInBytes());
            molapColumnarFactMergerInfo.setTableName(tableName);
            molapColumnarFactMergerInfo
                    .setType(valueCompressionModel.getType());
            molapColumnarFactMergerInfo.setMeasureCount(measureLength);
            molapColumnarFactMergerInfo.setIsUpdateFact(true);
            MolapFactDataHandlerColumnarMerger mergerInstance = new MolapFactDataHandlerColumnarMerger(
                    molapColumnarFactMergerInfo, restructureNumber);
            mergerInstance.initialise();
            int counter = 0;
            try
            {
                if(retentionSurrogateKeyMap.isEmpty())
                {
                    return;
                }
                while(columnarLeafTupleItr.hasNext())
                {
                    MolapSurrogateTupleHolder molapSurrogateTuHolder = columnarLeafTupleItr
                            .next();
                    byte[] mdKeyFromStore = molapSurrogateTuHolder.getMdKey();
                    Object[] row = new Object[molapSurrogateTuHolder
                            .getMeasures().length + 1];
                    System.arraycopy(molapSurrogateTuHolder.getMeasures(), 0,
                            row, 0, molapSurrogateTuHolder.getMeasures().length);

                    // comparing the MD keys with retention member retention
                    // keys
                    long[] storeTupleSurrogates = keyGenerator
                            .getKeyArray(mdKeyFromStore);

                    int res = -1;
                    int surrKey =-1;
                    try
                    {
                        surrKey = (int)storeTupleSurrogates[surrogateKeyIndex];
                        res = retentionSurrogateKeyMap.get(surrKey);
                    }
                    catch(Exception e)
                    {
                        LOGGER.info(
                                MolapDataProcessorLogEvent.UNIBI_MOLAPDATAPROCESSOR_MSG,
                                "Member needs to be added in updated fact file surrogate key is : "+surrKey);
                    }
                    if(res == -1)
                    {
                        row[row.length - 1] = mdKeyFromStore;
                        mergerInstance.addDataToStore(row);
                        counter++;
                    }
                }

                if(counter == 0)
                {
                    try
                    {
                        FileFactory.createNewFile(fileToBeUpdated
                                + MolapCommonConstants.FACT_DELETE_EXTENSION,
                                FileFactory.getFileType(fileToBeUpdated));
                    }
                    catch(IOException e)
                    {
                        throw new MolapDataProcessorException(e.getMessage());
                    }
                    loadDetails.put(loadName,
                            MolapCommonConstants.MARKED_FOR_DELETE);
                    return;
                }
            }
            catch(MolapDataWriterException e)
            {
                throw new MolapDataProcessorException(e.getMessage());
            }
            finally
            {
                if(counter != 0)
                {
                    mergerInstance.finish();
                    mergerInstance.closeHandler();
                    mergerInstance.copyToHDFS(loadPath);
                }

            }

        }
        catch(MolapUtilException e)
        {
            // TODO Auto-generated catch block
            throw new MolapDataProcessorException(e.getMessage());
        }
        catch(MolapDataWriterException e)
        {
            // TODO Auto-generated catch block
            throw new MolapDataProcessorException(e.getMessage());
        }
    }

    private FactReaderInfo getFactReaderInfo() throws MolapUtilException
    {
        FactReaderInfo factReaderInfo = new FactReaderInfo();
        int[] blockIndex = new int[sliceMetadata.getDimensions().length];
        for(int i = 0;i < blockIndex.length;i++)
        {
            blockIndex[i] = i;
        }
        factReaderInfo.setBlockIndex(blockIndex);
        factReaderInfo.setCubeName(cubeName);
        factReaderInfo.setDimLens(sliceMetadata.getDimLens());
        factReaderInfo.setMeasureCount(measureLength);
        factReaderInfo.setSchemaName(schemaName);
        factReaderInfo.setTableName(tableName);
        factReaderInfo.setUpdateMeasureRequired(true);
        return factReaderInfo;
    }

    /*
     * private int getLeafNodeInfoIndexForFactFileUpdation(
     * List<LeafNodeInfoColumnar> leafNodeInfoList) { LeafNodeInfoColumnar
     * leafNodeInfoColumnar; int leafNodeInfoIndex = -1; for (int i = 0; i <
     * leafNodeInfoList.size(); i++) { leafNodeInfoColumnar =
     * leafNodeInfoList.get(i);
     * 
     * if (compare(keyGenerator.getKeyArray(leafNodeInfoColumnar
     * .getStartKey()), surrogateKeyArray) < 0 &&
     * compare(keyGenerator.getKeyArray(leafNodeInfoColumnar .getEndKey()),
     * surrogateKeyArray) >= 0) { leafNodeInfoIndex = i; break; }
     * 
     * } return leafNodeInfoIndex; }
     */

    /*
     * private int compare(long[] first, long[] second) { for (int i = 0; i <
     * compareIndex.length; i++) { if (first[compareIndex[i]] >
     * second[compareIndex[i]]) { return 1; } else if (first[compareIndex[i]] <
     * second[compareIndex[i]]) { return -1; } } return 0; }
     */

}
