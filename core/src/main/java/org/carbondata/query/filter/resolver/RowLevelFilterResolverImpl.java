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

import java.util.List;

import org.carbondata.core.carbon.AbsoluteTableIdentifier;
import org.carbondata.core.metadata.CarbonMetadata.Measure;
import org.carbondata.query.carbonfilterinterface.FilterExecuterType;
import org.carbondata.query.evaluators.DimColumnResolvedFilterInfo;
import org.carbondata.query.evaluators.MsrColumnEvalutorInfo;
import org.carbondata.query.expression.ColumnExpression;
import org.carbondata.query.expression.Expression;
import org.carbondata.query.expression.conditional.ConditionalExpression;
import org.carbondata.query.filter.executer.FilterExecuter;
import org.carbondata.query.filter.executer.RowLevelFilterExecuterImpl;

public class RowLevelFilterResolverImpl extends ConditionalFilterResolverImpl {
	
	private List<DimColumnResolvedFilterInfo> dimColEvaluatorInfoList;
	private List<MsrColumnEvalutorInfo> msrColEvalutorInfoList;
    protected Expression exp;
    protected boolean isExpressionResolve;
    protected boolean isIncludeFilter;
	private AbsoluteTableIdentifier tableIdentifier;
    public RowLevelFilterResolverImpl(Expression exp, boolean isExpressionResolve,
            boolean isIncludeFilter, AbsoluteTableIdentifier tableIdentifier) {
    	super(exp, isExpressionResolve, isIncludeFilter);
    	this.tableIdentifier=tableIdentifier;
    }

    @Override
    public void resolve(AbsoluteTableIdentifier absoluteTableIdentifier) {
        DimColumnResolvedFilterInfo dimColumnEvaluatorInfo = null;
        MsrColumnEvalutorInfo msrColumnEvalutorInfo = null;
        int index = 0;
        if (exp instanceof ConditionalExpression) {
            ConditionalExpression conditionalExpression = (ConditionalExpression) exp;
            List<ColumnExpression> columnList = conditionalExpression.getColumnList();
            for (ColumnExpression columnExpression : columnList) {
                if (columnExpression.isDimension()) {
                    dimColumnEvaluatorInfo = new DimColumnResolvedFilterInfo();
                    dimColumnEvaluatorInfo.setRowIndex(index++);
                    dimColumnEvaluatorInfo.setDimension(columnExpression.getDimension());
                        dimColumnEvaluatorInfo.setDimensionExistsInCurrentSilce(false);
                    dimColEvaluatorInfoList.add(dimColumnEvaluatorInfo);
				} else {
					msrColumnEvalutorInfo = new MsrColumnEvalutorInfo();
					msrColumnEvalutorInfo.setRowIndex(index++);
					msrColumnEvalutorInfo
							.setAggregator(((Measure) columnExpression.getDim())
									.getAggName());
					// if measure is found then index returned will be > 0 .
					// else it will be -1 . here if the measure is a newly added
					// measure then index will be >0.

					msrColumnEvalutorInfo.setMeasureExistsInCurrentSlice(false);
					msrColumnEvalutorInfo.setDataType(columnExpression.getDataType());
					msrColEvalutorInfoList.add(msrColumnEvalutorInfo);
				}
            }
        }
    }
    
    @Override
	public FilterExecuter getFilterExecuterInstance() {
		// TODO Auto-generated method stub
		return new RowLevelFilterExecuterImpl(dimColEvaluatorInfoList,msrColEvalutorInfoList,exp,tableIdentifier);
	}

	@Override
	public FilterExecuterType getFilterExecuterType() {
		return FilterExecuterType.ROWLEVEL;
	}
}