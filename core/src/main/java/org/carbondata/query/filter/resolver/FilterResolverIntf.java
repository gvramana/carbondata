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
package org.carbondata.query.filter.resolver;

import org.carbondata.core.carbon.AbsoluteTableIdentifier;
import org.carbondata.core.carbon.datastore.IndexKey;
import org.carbondata.core.carbon.datastore.block.AbstractIndex;
import org.carbondata.core.keygenerator.KeyGenerator;
import org.carbondata.query.carbonfilterinterface.FilterExecuterType;
import org.carbondata.query.evaluators.DimColumnResolvedFilterInfo;
import org.carbondata.query.filter.executer.FilterExecuter;

public interface FilterResolverIntf {

	/**
	 * This API will resolve the filter expression and generates the
	 * dictionaries for executing/evaluating the filter expressions in the
	 * executer layer.
	 * 
	 * @param info
	 */
	void resolve(AbsoluteTableIdentifier absoluteTableIdentifier);

	/**
	 * This API will provide the left column filter expression
	 * inorder to resolve the left expression filter.
	 * @return FilterResolverIntf
	 */
	FilterResolverIntf getLeft();

	/**
	 * API will provide the right column filter expression inorder to resolve
	 * the right expression filter.
	 * 
	 * @return FilterResolverIntf
	 */
	FilterResolverIntf getRight();

	/**
	 * This API will get the filter executer instance which is required to
	 * evaluate/execute the resolved filter expressions in the executer layer.
	 * This executer instance will be identified based on the resolver instance
	 * 
	 * @return FilterExecuter instance.
	 */
	FilterExecuter getFilterExecuterInstance();

	/**
	 * API will return the resolved filter instance, this instance will provide
	 * the resolved surrogates based on the applied filter
	 * 
	 * @return DimColumnResolvedFilterInfo object
	 */
	DimColumnResolvedFilterInfo getDimColResolvedFilterInfo();

	/**
	 * API will get the start key based on the filter applied
	 * based on the key generator
	 * @return long[], array of start keys.
	 */
	IndexKey getstartKey(KeyGenerator keyGenerator);

	/**
	 * API will read the end key based on the max surrogate of
	 * particular dimension column
	 * 
	 * @return
	 */
	IndexKey getEndKey(AbstractIndex segmentIndexBuilder,
			AbsoluteTableIdentifier tableIdentifier);
	
	/**
	 * 
	 * @return
	 */
	  FilterExecuterType getFilterExecuterType();

}