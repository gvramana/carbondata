package com.huawei.datasight.molap.datastats;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huawei.datasight.molap.autoagg.AutoAggSuggestionService;
import com.huawei.datasight.molap.autoagg.exception.AggSuggestException;
import com.huawei.datasight.molap.autoagg.model.AggSuggestion;
import com.huawei.datasight.molap.autoagg.util.CommonUtil;
import com.huawei.datasight.molap.datastats.analysis.QueryDistinctData;
import com.huawei.datasight.molap.datastats.model.DriverDistinctData;
import com.huawei.datasight.molap.datastats.model.Level;
import com.huawei.datasight.molap.datastats.model.LoadModel;
import com.huawei.datasight.molap.datastats.util.AggCombinationGeneratorUtil;
import com.huawei.datasight.molap.datastats.util.DataStatsUtil;
import com.huawei.iweb.platform.logging.LogService;
import com.huawei.iweb.platform.logging.LogServiceFactory;
import com.huawei.unibi.molap.engine.querystats.Preference;
import com.huawei.unibi.molap.engine.util.MolapEngineLogEvent;
import com.huawei.unibi.molap.util.MolapUtil;

/**
 * This class does below task 1.It delegates loading of Load data to LoadSampler
 * 2.It delegates creation of distinct data to QueryDistinctData 3.It delegates
 * aggregate combination generation to AutoAggregation
 * 
 * @author A00902717
 *
 */
public class DSAutoAggSuggestionService implements AutoAggSuggestionService
{

	private static final LogService LOGGER = LogServiceFactory
			.getLogService(DSAutoAggSuggestionService.class.getName());
	
	
	public DSAutoAggSuggestionService()
	{

	}

	/**
	 * This method gives list of all dimensions can be used in Aggregate table
	 * @param schema
	 * @param cube
	 * @return
	 * @throws AggSuggestException 
	 */
	@Override
	public List<String> getAggregateDimensions(LoadModel loadModel) throws AggSuggestException
	{
		try
		{
			List<AggSuggestion> dimsCombination = getAggregateCombination(loadModel);
			return AggCombinationGeneratorUtil.getDimensionsWithMeasures(dimsCombination,loadModel.getCube());	
		}
		catch(AggSuggestException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new AggSuggestException("Failed to get aggregate suggestion.",e);
		}
		

	}

	/**
	 * this method gives all possible aggregate table script
	 * @param schema
	 * @param cube
	 * @return
	 * @throws AggSuggestException 
	 */
	@Override
	public List<String> getAggregateScripts(LoadModel loadModel) throws AggSuggestException
	{
		try
		{
			List<AggSuggestion> dimsCombination = getAggregateCombination(loadModel);
			return AggCombinationGeneratorUtil.createAggregateScript(dimsCombination,loadModel.getCube(), loadModel.getSchemaName(),loadModel.getCubeName());	
		}
		catch(AggSuggestException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new AggSuggestException("Failed to get aggregate suggestion.",e);
		}
		
	}

	private List<AggSuggestion> getAggregateCombination(LoadModel loadModel) throws AggSuggestException
	{
		DriverDistinctData driverDistinctData=readFromStore(loadModel);
		// checking for aggregate combination if its already calculated
		
		Level[] visibleLevel = AggCombinationGeneratorUtil.getVisibleLevels(driverDistinctData.getLevels(), loadModel.getCube());
		if(visibleLevel.length==0)
		{
			return new ArrayList<AggSuggestion>(0);
		}
		AggCombinationGenerator autoAggregation = new AggCombinationGenerator(visibleLevel,loadModel.getTableName());

		long startTime=System.currentTimeMillis();
		//calculating the aggregate combination
		List<AggSuggestion> aggSuggest = autoAggregation
					.generateAggregate();
		long timeTaken=System.currentTimeMillis()-startTime;
		LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
					"Time taken to generate DataStats Aggreation combination for cube," + loadModel.getSchemaName()+'.'+loadModel.getCubeName()+':'+timeTaken);
			
		
		return Arrays.asList(aggSuggest.toArray(new AggSuggestion[aggSuggest
		                                       					.size()]));
	}

	/**
	 * reading data from store location
	 * 
	 * @param schema
	 * @param cube
	 * @return
	 * @throws AggSuggestException 
	 */
	private DriverDistinctData readFromStore(LoadModel loadModel) throws AggSuggestException
	{
		
		StringBuffer dataStatsPath = new StringBuffer(loadModel.getMetaDataPath());
		dataStatsPath.append(File.separator)
				.append(Preference.AGGREGATE_STORE_DIR);
				

		// checking for distinct data if its already calculated
		String distinctDataPath = dataStatsPath.toString() + File.separator
				+ Preference.DATASTATS_DISTINCT_FILE_NAME;
		DriverDistinctData driverDistinctData = (DriverDistinctData) DataStatsUtil
				.readSerializedFile(distinctDataPath);
		if(null!=driverDistinctData && CommonUtil.isLoadDeleted(loadModel.getValidSlices(),driverDistinctData.getLoads()))
		{
			//recalculate distinct relationship
			driverDistinctData=null;
		}
				
		
		if (null == driverDistinctData)
		{
			loadModel.setCalculatedLoads(new ArrayList<String>());
		}
		else
		{
			List<String> validSlices=loadModel.getValidSlices();
			//calculated loads means, these loads are already analyzed
			List<String> calculatedLoad=driverDistinctData.getLoads();
			boolean isScanRequired=false;
			for(String load:validSlices)
			{
				if(!calculatedLoad.contains(load))
				{
					isScanRequired=true;
					break;
				}
				
			}
			if(!isScanRequired)
			{
				return driverDistinctData;
			}
			loadModel.setCalculatedLoads(driverDistinctData.getLoads());
			
		}			
		

		
	    int currentRestructNumber = MolapUtil.checkAndReturnCurrentRestructFolderNumber(loadModel.getMetaDataPath(), "RS_", false);
		if (-1 == currentRestructNumber)
		{
		    currentRestructNumber = 0;
		}
		loadModel.setRestructureNo(currentRestructNumber);
		
			try
			{
				LoadSampler loadSampler=createDataSource(loadModel);
				//Check if any load available to calculate distinct count
				if(loadSampler.getLoadHandlers().size()==0)
				{
					//return emtpy stating no suggestion
					return new DriverDistinctData(loadModel.getValidSlices(),new Level[0]);
					
				}
				Level[] distinctData=getDimensionDistinctData(loadSampler,loadModel.getPartitionId());
				driverDistinctData=new DriverDistinctData(loadModel.getValidSlices(),distinctData);
				DataStatsUtil.serializeObject(driverDistinctData,
						dataStatsPath.toString(),
						Preference.DATASTATS_DISTINCT_FILE_NAME);
				return driverDistinctData;
				
			}
			catch(Exception e)
			{
				LOGGER.info(MolapEngineLogEvent.UNIBI_MOLAPENGINE_MSG,
						"Error getting distinct relationship" ,e);
				throw new AggSuggestException(e.getMessage(),e);
			}
		

	}

	/**
	 * It is responsible for getting sample data and loading data to BPlus tree
	 * 
	 * @param schema
	 * @param cube
	 */
	private LoadSampler createDataSource(LoadModel loadModel)
	{
		LoadSampler loadSampler = new LoadSampler();
		loadSampler.loadCube(loadModel);
		return loadSampler;
	}

	/**
	 * delegat task of getting distinct data
	 * 
	 * @param partitionId
	 * @return
	 * @throws AggSuggestException 
	 */
	private Level[] getDimensionDistinctData(LoadSampler loadSampler,String partitionId) throws AggSuggestException
	{
		QueryDistinctData queryExecutioner = new QueryDistinctData(loadSampler);
		return queryExecutioner.queryDistinctData(partitionId);

	}

}
