package org.carbondata.query.carbon.executor.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.carbondata.common.logging.LogService;
import org.carbondata.common.logging.LogServiceFactory;
import org.carbondata.core.constants.CarbonCommonConstants;
import org.carbondata.core.iterator.CarbonIterator;
import org.carbondata.core.util.CarbonProperties;
import org.carbondata.query.carbon.executor.InternalQueryExecutor;
import org.carbondata.query.carbon.executor.exception.QueryExecutionException;
import org.carbondata.query.carbon.executor.infos.BlockExecutionInfo;
import org.carbondata.query.carbon.processor.ScannedResultMerger;
import org.carbondata.query.carbon.processor.impl.UnSortedScannedResultMerger;
import org.carbondata.query.carbon.result.Result;
import org.carbondata.query.util.CarbonEngineLogEvent;

/**
 * Below Class will be used to execute the detail query
 */
public class DetailQueryExecutor implements InternalQueryExecutor {

  /**
   * LOGGER.
   */
  private static final LogService LOGGER =
      LogServiceFactory.getLogService(DetailQueryExecutor.class.getName());

  /**
   * number of cores can be used to execute the query
   */
  private int numberOfCores;

  public DetailQueryExecutor() {

    // below code will be used to update the number of cores based on number
    // records we
    // can keep in memory while executing the query execution
    int recordSize = 0;
    String defaultInMemoryRecordsSize =
        CarbonProperties.getInstance().getProperty(CarbonCommonConstants.INMEMORY_REOCRD_SIZE);
    if (null != defaultInMemoryRecordsSize) {
      try {
        recordSize = Integer.parseInt(defaultInMemoryRecordsSize);
      } catch (NumberFormatException ne) {
        LOGGER.error(CarbonEngineLogEvent.UNIBI_CARBONENGINE_MSG,
            "Invalid inmemory records size. Using default value");
        recordSize = CarbonCommonConstants.INMEMORY_REOCRD_SIZE_DEFAULT;
      }
    }
    this.numberOfCores = recordSize / Integer.parseInt(CarbonProperties.getInstance()
        .getProperty(CarbonCommonConstants.LEAFNODE_SIZE,
            CarbonCommonConstants.LEAFNODE_SIZE_DEFAULT_VAL));
    if (numberOfCores == 0) {
      numberOfCores++;
    }
  }

  /**
   * Below method will be used to used to execute the detail query
   * and it will return iterator over result
   *
   * @param executionInfos block execution info which will have all the properties
   *                       required for query execution
   * @param sliceIndexes   slice indexes to be executed
   * @return query result
   */
  @Override public CarbonIterator<Result> executeQuery(List<BlockExecutionInfo> executionInfos,
      int[] sliceIndexes) throws QueryExecutionException {
    long startTime = System.currentTimeMillis();
    QueryRunner task = null;
    ScannedResultMerger scannedResultProcessor =
        new UnSortedScannedResultMerger(executionInfos.get(executionInfos.size() - 1),
            sliceIndexes.length);
    ExecutorService execService = Executors.newFixedThreadPool(numberOfCores);
    try {
      for (int currentSliceIndex : sliceIndexes) {
        if (currentSliceIndex == -1) {
          continue;
        }
        executionInfos.get(currentSliceIndex).setScannedResultProcessor(scannedResultProcessor);
        task = new QueryRunner(executionInfos.get(currentSliceIndex));
        execService.submit(task);
      }
      execService.shutdown();
      execService.awaitTermination(2, TimeUnit.DAYS);
      LOGGER.info(CarbonEngineLogEvent.UNIBI_CARBONENGINE_MSG,
          "Total time taken for scan " + (System.currentTimeMillis() - startTime));
      return scannedResultProcessor.getQueryResultIterator();
    } catch (QueryExecutionException exception) {
      LOGGER.error(CarbonEngineLogEvent.UNIBI_CARBONENGINE_MSG, exception, exception.getMessage());
      throw new QueryExecutionException(exception);
    } catch (InterruptedException e) {
      LOGGER.error(CarbonEngineLogEvent.UNIBI_CARBONENGINE_MSG, e, e.getMessage());
      throw new QueryExecutionException(e);
    } finally {
      execService = null;
    }
  }

}