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
import org.carbondata.core.carbon.datastore.IndexKey;
import org.carbondata.core.carbon.datastore.block.AbstractIndex;
import org.carbondata.core.keygenerator.KeyGenerator;
import org.carbondata.query.carbonfilterinterface.FilterExecuterType;
import org.carbondata.query.evaluators.DimColumnResolvedFilterInfo;
import org.carbondata.query.expression.ColumnExpression;
import org.carbondata.query.expression.DataType;
import org.carbondata.query.expression.Expression;
import org.carbondata.query.expression.conditional.BinaryConditionalExpression;
import org.carbondata.query.expression.conditional.ConditionalExpression;
import org.carbondata.query.filters.measurefilter.util.FilterUtil;

public class RestructureFilterResolverImpl implements FilterResolverIntf {
  protected DimColumnResolvedFilterInfo dimColumnResolvedFilterInfo;

  private Expression exp;

  private String defaultValue;

  private int surrogate;

  private boolean isExpressionResolve;

  private boolean isIncludeFilter;

  public RestructureFilterResolverImpl(Expression exp, String defaultValue, int surrogate,
      boolean isExpressionResolve, boolean isIncludeFilter) {
    dimColumnResolvedFilterInfo = new DimColumnResolvedFilterInfo();
    this.exp = exp;
    this.defaultValue = defaultValue;
    this.surrogate = surrogate;
    this.isExpressionResolve = isExpressionResolve;
    this.isIncludeFilter = isIncludeFilter;
  }

  @Override public void resolve(AbsoluteTableIdentifier absoluteTableIdentifier) {

    DimColumnResolvedFilterInfo dimColumnResolvedFilterInfo = new DimColumnResolvedFilterInfo();
    if (!this.isExpressionResolve && exp instanceof BinaryConditionalExpression) {
      BinaryConditionalExpression binaryConditionalExpression = (BinaryConditionalExpression) exp;
      Expression left = binaryConditionalExpression.getLeft();
      Expression right = binaryConditionalExpression.getRight();
      if (left instanceof ColumnExpression) {
        ColumnExpression columnExpression = (ColumnExpression) left;
        if (columnExpression.getDataType().equals(DataType.TimestampType)) {
          isExpressionResolve = true;
        } else {
          // If imei=imei comes in filter condition then we need to
          // skip processing of right expression.
          // This flow has reached here assuming that this is a single
          // column expression.
          // we need to check if the other expression contains column
          // expression or not in depth.
          if (FilterUtil.checkIfExpressionContainsColumn(right)) {
            isExpressionResolve = true;
          } else {
            dimColumnResolvedFilterInfo.setColumnIndex(columnExpression.getDim().getOrdinal());
            // dimColumnResolvedFilterInfo
            // .setNeedCompressedData(info.getSlices().get(info.getCurrentSliceIndex())
            // .getDataCache(info.getFactTableName()).getAggKeyBlock()[columnExpression.getDim()
            // .getOrdinal()]);
            dimColumnResolvedFilterInfo.setFilterValues(
                FilterUtil.getFilterListForRS(right, columnExpression, defaultValue, surrogate));
          }
        }
      } else if (right instanceof ColumnExpression) {
        ColumnExpression columnExpression = (ColumnExpression) right;
        if (columnExpression.getDataType().equals(DataType.TimestampType)) {
          isExpressionResolve = true;
        } else {

          // If imei=imei comes in filter condition then we need to
          // skip processing of right expression.
          // This flow has reached here assuming that this is a single
          // column expression.
          // we need to check if the other expression contains column
          // expression or not in depth.
          if (checkIfExpressionContainsColumn(left)) {
            isExpressionResolve = true;
          } else {
            dimColumnResolvedFilterInfo.setColumnIndex(columnExpression.getDim().getOrdinal());
            // dimColumnResolvedFilterInfo
            // .setNeedCompressedData(info.getSlices().get(info.getCurrentSliceIndex())
            // .getDataCache(info.getFactTableName()).getAggKeyBlock()[columnExpression.getDim()
            // .getOrdinal()]);
            dimColumnResolvedFilterInfo.setFilterValues(
                FilterUtil.getFilterListForRS(left, columnExpression, defaultValue, surrogate));
          }
        }
      }
    }
    if (this.isExpressionResolve && exp instanceof ConditionalExpression) {
      ConditionalExpression conditionalExpression = (ConditionalExpression) exp;
      List<ColumnExpression> columnList = conditionalExpression.getColumnList();
      dimColumnResolvedFilterInfo.setColumnIndex(columnList.get(0).getDim().getOrdinal());
      dimColumnResolvedFilterInfo.setFilterValues(FilterUtil
          .getFilterListForAllMembersRS(exp, columnList.get(0), defaultValue, surrogate,
              isIncludeFilter));
    }

  }

  /**
   * This method will check if a given expression contains a column expression recursively.
   *
   * @return
   */
  private boolean checkIfExpressionContainsColumn(Expression expression) {
    if (expression instanceof ColumnExpression) {
      return true;
    }
    for (Expression child : expression.getChildren()) {
      if (checkIfExpressionContainsColumn(child)) {
        return true;
      }
    }

    return false;
  }

  @Override public FilterResolverIntf getLeft() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public FilterResolverIntf getRight() {
    // TODO Auto-generated method stub
    return null;
  }

  public DimColumnResolvedFilterInfo getDimColResolvedFilterInfo() {
    // TODO Auto-generated method stub
    return dimColumnResolvedFilterInfo;
  }

  @Override public IndexKey getstartKey(KeyGenerator keyGenerator) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public IndexKey getEndKey(AbstractIndex segmentIndexBuilder,
      AbsoluteTableIdentifier tableIdentifier) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public FilterExecuterType getFilterExecuterType() {
    return FilterExecuterType.RESTRUCTURE;
  }
}
