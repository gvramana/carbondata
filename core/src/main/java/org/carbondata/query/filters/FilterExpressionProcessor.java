/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.carbondata.query.filters;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.carbondata.common.logging.LogService;
import org.carbondata.common.logging.LogServiceFactory;
import org.carbondata.core.carbon.AbsoluteTableIdentifier;
import org.carbondata.core.carbon.datastore.DataRefNode;
import org.carbondata.core.carbon.datastore.DataRefNodeFinder;
import org.carbondata.core.carbon.datastore.IndexKey;
import org.carbondata.core.carbon.datastore.block.AbstractIndex;
import org.carbondata.core.carbon.datastore.impl.btree.BTreeNode;
import org.carbondata.core.carbon.datastore.impl.btree.BtreeDataRefNodeFinder;
import org.carbondata.query.expression.Expression;
import org.carbondata.query.filter.resolver.FilterResolverIntf;
import org.carbondata.query.filters.measurefilter.util.FilterUtil;
import org.carbondata.query.util.CarbonEngineLogEvent;

public class FilterExpressionProcessor implements FilterProcessor {

    private static final LogService LOGGER =
            LogServiceFactory.getLogService(FilterExpressionProcessor.class.getName());

	/**
	 * Implementation will provide the resolved form of filters based on the
	 * filter expression tree which is been passed in Expression instance.
	 * 
	 * @param expressionTree
	 *            , filter expression tree
	 * @param tableIdentifier
	 *            ,contains carbon store informations
	 * @return a filter resolver tree
	 */
	public FilterResolverIntf getFilterResolver(Expression expressionTree,
			AbsoluteTableIdentifier tableIdentifier) {
		if (null != expressionTree && null != tableIdentifier) {
			FilterUtil.getFilterResolver(expressionTree, tableIdentifier);

		}
		return null;
	}

	/**
	 * This API will scan the Segment level all btrees and selects the required
	 * block reference  nodes inorder to push the same to executer for applying filters
	 * on the respective data reference node.
	 * 
	 * Following Algorithm is followed in below API
	 * Step:1 Get the start end key based on the filter tree resolver information
	 * 
	 * Step:2 Prepare the IndexKeys inorder to scan the tree and get the start and end reference
	 * node(block)
	 * 
	 * Step:3 Once data reference node ranges retrieved traverse the node within this range
	 * and select the node based on the block min and max value and the filter value.
	 * 
	 * Step:4 The selected blocks will be send to executers for applying the filters with the help
	 * of Filter executers.
	 */
	public List<DataRefNode> getFilterredBlocks(List<BTreeNode> listOfTree,
			FilterResolverIntf filterResolver, AbstractIndex tableSegment,
			AbsoluteTableIdentifier tableIdentifier) {
		// Need to get the current dimension tables
		List<DataRefNode> listOfDataBlocksToScan = new ArrayList<DataRefNode>();
		// getting the start and end index key based on filter for hitting the
		// selected block reference nodes based on filter resolver tree.
		LOGGER.info(CarbonEngineLogEvent.UNIBI_CARBONENGINE_MSG,
				"preparing the start and end key for finding"
						+ "start and end block as per filter resolver");
		IndexKey searchStartKey = filterResolver.getstartKey(tableSegment
				.getSegmentProperties().getDimensionKeyGenerator());
		IndexKey searchEndKey = filterResolver.getEndKey(tableSegment,
				tableIdentifier);
		LOGGER.info(CarbonEngineLogEvent.UNIBI_CARBONENGINE_MSG,
				"Successfully retrieved the start and end key");
		long startTimeInMillis = System.currentTimeMillis();
		for (BTreeNode btreeNode : listOfTree) {
			DataRefNodeFinder blockFinder = new BtreeDataRefNodeFinder(
					tableSegment.getSegmentProperties()
							.getDimensionColumnsValueSize());
			DataRefNode startBlock = blockFinder.findFirstDataBlock(
					btreeNode.getNextDataRefNode(), searchStartKey);
			DataRefNode endBlock = blockFinder.findLastDataBlock(
					btreeNode.getNextDataRefNode(), searchEndKey);
			while (startBlock != endBlock) {
				startBlock = startBlock.getNextDataRefNode();
				addBlockBasedOnMinMaxValue(filterResolver,
						listOfDataBlocksToScan, startBlock);
			}
			addBlockBasedOnMinMaxValue(filterResolver, listOfDataBlocksToScan,
					endBlock);

		}
		LOGGER.info(
				CarbonEngineLogEvent.UNIBI_CARBONENGINE_MSG,
				"Total Time in retrieving the data reference node"
						+ "after scanning the btree "
						+ (System.currentTimeMillis() - startTimeInMillis)
						+ " Total number of data reference node for executing filter(s) "
						+ listOfDataBlocksToScan.size());
		return listOfDataBlocksToScan;
	}

	/**
	 * Selects the blocks based on col max and min value.
	 * @param filterResolver
	 * @param listOfDataBlocksToScan
	 * @param dataBlock
	 */
	private void addBlockBasedOnMinMaxValue(FilterResolverIntf filterResolver,
			List<DataRefNode> listOfDataBlocksToScan, DataRefNode dataRefNode) {
		BitSet bitSet = filterResolver.getFilterExecuterInstance()
				.isScanRequired(dataRefNode.getColumnsMaxValue(),
						dataRefNode.getColumnsMinValue());
		if (!bitSet.isEmpty()) {
			listOfDataBlocksToScan.add(dataRefNode);

		}
	}
}
