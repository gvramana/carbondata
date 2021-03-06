package com.huawei.datasight.molap.datastats;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.huawei.datasight.molap.autoagg.exception.AggSuggestException;
import com.huawei.datasight.molap.datastats.load.FactDataHandler;
import com.huawei.datasight.molap.datastats.load.FactDataReader;
import com.huawei.datasight.molap.datastats.load.LoadHandler;
import com.huawei.datasight.molap.datastats.model.LoadModel;
import com.huawei.datasight.molap.datastats.util.DataStatsUtil;
import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFile;
import com.huawei.unibi.molap.datastorage.store.filesystem.MolapFileFilter;
import com.huawei.unibi.molap.datastorage.store.impl.FileFactory;
import com.huawei.unibi.molap.engine.datastorage.InMemoryCube;
import com.huawei.unibi.molap.engine.datastorage.InMemoryCubeStore;
import com.huawei.unibi.molap.engine.datastorage.Member;
import com.huawei.unibi.molap.engine.datastorage.MemberStore;
import com.huawei.unibi.molap.engine.querystats.Preference;
import com.huawei.unibi.molap.engine.util.MolapEngineLogEvent;
import com.huawei.unibi.molap.metadata.MolapMetadata.Cube;
import com.huawei.unibi.molap.metadata.MolapMetadata.Dimension;
import com.huawei.unibi.molap.metadata.SliceMetaData;
import com.huawei.unibi.molap.olap.MolapDef;
import com.huawei.unibi.molap.olap.MolapDef.Schema;
import com.huawei.unibi.molap.util.MolapProperties;
import com.huawei.unibi.molap.util.MolapUtil;

/**
 * This class have all loads for which data sampling needs to be done
 * also it delegates the task of loading configured loads to BPlus tree
 * @author A00902717
 *
 */
public class LoadSampler
{
	private static final LogService LOGGER = LogServiceFactory
			.getLogService(LoadSampler.class.getName());

	/**
	 * folder name where molap data writer will write
	 */
	//private static final String RS_FOLDER_NAME = "RS_";

	private List<LoadHandler> loadHandlers;

	private String tableName;

	private String schemaName;

	private String cubeName;

	private Cube metaCube;

	private Schema schema;

	private String cubeUniqueName;
	
	private List<String> allLoads;
	
	/**
	 * This will have dimension ordinal as key and dimension cardinality as value
	 */
	private Map<Integer,Integer> dimCardinality=new HashMap<>();;

	/**
	 * Dimension present in cube.
	 */
	private List<Dimension> visibleDimensions;
	
	
	/**
	 * loading stores 
	 * @param loadModel
	 */
	public void loadCube(LoadModel loadModel)
	{
		this.schema = loadModel.getSchema();
		this.allLoads = loadModel.getAllLoads();
		MolapDef.Cube cube = loadModel.getCube();
		
		String partitionId = loadModel.getPartitionId();
		if (null != partitionId)
		{
			schemaName=loadModel.getSchemaName()+'_'+partitionId;
			cubeName=loadModel.getCubeName()+'_'+partitionId;
		}
	    this.tableName=loadModel.getTableName();
        
		this.cubeUniqueName = schemaName + '_' + cubeName;

		loadHandlers = new ArrayList<LoadHandler>();
		// Load data in memory
		LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
				"Loading data to BPlus tree started");
		metaCube = InMemoryCubeStore.getInstance().loadCubeMetadataIfRequired(schema, cube, partitionId, loadModel.getSchemaLastUpdatedTime());
		DataStatsUtil.createDataSource(schema,metaCube, partitionId, loadModel.getValidSlices(),
					tableName,loadModel.getValidUpdateSlices(),loadModel.getDataPath(),loadModel.getRestructureNo(), loadModel.getCubeCreationtime());	
		//set visible dimensions
		this.visibleDimensions=getVisibleDimensions(cube);
		LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
				"Loading data to BPlus tree completed");
		
		//int restructureNo=Integer.parseInt(rsFolder.getName().substring(rsFolder.getName().indexOf('_')+1));
		MolapFile[] rsFolders=DataStatsUtil.getRSFolderListList(loadModel);
		
		for(MolapFile rsFolder:rsFolders)
		{
			MolapFile tableFile = getTableFile(rsFolder,
					tableName);
			SliceMetaData sliceMetaData= (SliceMetaData)DataStatsUtil.readSerializedFile(tableFile.getAbsolutePath() + File.separator
	                + MolapUtil.getSliceMetaDataFileName(loadModel.getRestructureNo()));
			MolapFile[] loads = getLoadFolderList(
					tableFile.getAbsolutePath(), loadModel.getValidSlices());
			
			if(null==loads)
			{
				continue;
			}
			/*if((RS_FOLDER_NAME+loadModel.getRestructureNo()).equals(rsFolder.getName()))
			{
				this.dimCardinality=DataStatsUtil.getCardinalityForLastLoadFolder(loads[loads.length-1], tableName);
				//this.dimCardinality=sliceMetaData.getActualDimLens();
			}*/
			
			String confloadSize = MolapProperties.getInstance().getProperty(
					Preference.AGG_LOAD_COUNT);
			int loadSize = loads.length;
			if (null != confloadSize && Integer.parseInt(confloadSize) < loadSize)
			{
				loadSize = Integer.parseInt(confloadSize);
			}
			int consideredLoadCounter=0;
			for(MolapFile load:loads)
			{
				LoadHandler loadHandler = new LoadHandler(sliceMetaData,metaCube,load);
				if(loadHandler.isDataAvailable(load, tableName))
				{
					loadHandlers.add(loadHandler);
					updateDimensionCardinality(sliceMetaData,tableName);
					consideredLoadCounter++;
				}
				if(consideredLoadCounter==loadSize)
				{
					break;
				}
			}
			
		}
		
	}


	/**
	 * Update dimension cardinality with its ordinal and cardinality value
	 * @param sliceMetaData
	 * @param tableName
	 */
	private void updateDimensionCardinality(SliceMetaData sliceMetaData,String tableName)
	{
		String[] sliceDimensions=sliceMetaData.getDimensions();
		int[] sliceCardinalities=sliceMetaData.getDimLens();
		for(Dimension dimension:visibleDimensions)
		{
			String dimName=dimension.getColName();
			Integer dimensionCardinality=1;
			for(int i=0;i<sliceDimensions.length;i++)
			{
				String sliceColName=sliceDimensions[i].substring(sliceDimensions[i].indexOf(tableName+"_")+tableName.length()+1);
				if(dimName.equals(sliceColName))
				{
					dimensionCardinality=sliceCardinalities[i];
					break;
				}
				
			}
			dimCardinality.put(dimension.getOrdinal(), dimensionCardinality);
			
		}
	}





	/**
	 * This will extract visible dimension from cube
	 * @param cube
	 * @return
	 */
	private List<Dimension> getVisibleDimensions(MolapDef.Cube cube)
	{
		List<Dimension> visibleDimensions=new ArrayList<Dimension>();
		MolapDef.CubeDimension[] cubeDimensions=cube.dimensions;
		for(MolapDef.CubeDimension cubeDimension:cubeDimensions)
		{
			if(cubeDimension.visible)
			{
				Dimension dim=metaCube.getDimension(cubeDimension.name, getTableName());
				visibleDimensions.add(dim);
			}
		}
		return visibleDimensions;
	}
	

	private MolapFile getTableFile(MolapFile rsFolder, final String tableName)
	{
		MolapFile[] tableFiles = rsFolder.listFiles(new MolapFileFilter()
		{
			public boolean accept(MolapFile pathname)
			{
				return (pathname.isDirectory())
						&& tableName.equals(pathname.getName());
			}
		});
		return tableFiles[0];
	}

	private MolapFile[] getLoadFolderList(String path,final List<String> validLoads)
	{
		MolapFile file = FileFactory.getMolapFile(path,
				FileFactory.getFileType(path));
		MolapFile[] files = null;
		if (file.isDirectory())
		{
			files = file.listFiles(new MolapFileFilter()
			{

				@Override
				public boolean accept(MolapFile pathname)
				{
					String name = pathname.getName();
					return validLoads.contains(name);
				}
			});

		}
		return files;
	}

	/**
	 * Get sample data
	 * @param dimension
	 * @param cubeUniqueName
	 * @return
	 * @throws AggSuggestException 
	 */
	public List<String> getSampleData(Dimension dimension, String cubeUniqueName) throws AggSuggestException
	{

		// Sample data
		HashSet<Integer> surrogates = new HashSet<Integer>(100);
		for(LoadHandler loadHandler:loadHandlers)
		{
			
			try 
			{
				//check if dimension exist in this load
				if(!loadHandler.isDimensionExist(dimension,tableName))
				{
					continue;
				}
				FactDataHandler factDataHandler = loadHandler
						.handleFactData(getTableName());
				if (null != factDataHandler) 
				{
					FactDataReader reader = factDataHandler.getFactDataReader();
					surrogates.addAll(reader.getSampleFactData(
							dimension.getOrdinal(),
							DataStatsUtil.getNumberOfRows(dimension)));
				}
			} 
			catch (AggSuggestException e) 
			{
				LOGGER.error(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
						e.getMessage());
			}
			
		}
		if(surrogates.size()==0)
		{
			// in case of cube alteration, its possible that dimension will not have value in load. Hence
			// in that case, pass "null" value.
			List<String> nullData=new ArrayList<String>(1);
			nullData.add("null");
			return nullData;
		}

		String levelName = dimension.getTableName() + '_'
				+ dimension.getColName() + '_' + dimension.getDimName() + '_'
				+ dimension.getHierName();
		List<String> realDatas = new ArrayList<String>(surrogates.size());
		for (int surrogate : surrogates)
		{

			String data = getDimensionValueFromSurrogate(cubeUniqueName,
					levelName, surrogate);
			if (null == data)
			{
				LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
						"Member value of dimension," + dimension.getName()
								+ ",for surrogate," + surrogate
								+ ",is not found in level file");
				continue;
			}

			realDatas.add(data);

		}
		LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
				dimension.getName() + " Load size:" + loadHandlers.size()
						+ ":Sample size:" + realDatas.size());
		return realDatas;
	}

	/**
	 * Get actual dimension value from member cache by passing surrogate key
	 *
	 * @param cubeName
	 * @param levelName
	 * @param surrogate
	 * @return
	 */
	public String getDimensionValueFromSurrogate(String cubeName,
			String levelName, int surrogate)
	{
		List<InMemoryCube> inMemoryCubes = InMemoryCubeStore.getInstance()
				.getActiveSlices(cubeName);
		for (InMemoryCube inMemoryCube : inMemoryCubes)
		{
			MemberStore memberStore = inMemoryCube.getMemberCache(levelName);
			Member member = memberStore.getMemberByID(surrogate);
			if (null != member)
			{
				return member.toString();
			}

		}
		return null;

	}
	
	
	public List<Dimension> getDimensions()
	{
		return this.visibleDimensions;
	}

	public String getTableName()
	{
		return tableName;
	}

	public Map<Integer,Integer> getLastLoadCardinality()
	{
		return this.dimCardinality;

	}

	public Cube getMetaCube()
	{
		return metaCube;
	}

	public String getCubeUniqueName()
	{
		// TODO Auto-generated method stub
		return cubeUniqueName;
	}	
	
	public List<LoadHandler> getLoadHandlers()
	{
		return this.loadHandlers; 
	}
	
	public List<String> getAllLoads()
	{
		return this.allLoads;
	}

}
