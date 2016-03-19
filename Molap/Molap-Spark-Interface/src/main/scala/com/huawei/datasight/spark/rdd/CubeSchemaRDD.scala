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

package com.huawei.datasight.spark.rdd

import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan


trait CubeSchemaRDDLike {
  def getMeasures: Array[String]

  def getDimensions: Array[String]
}

/*
 * Check the possibility of using implicit to convert any SchemaRDD into Cube RDD
 * */
class CubeSchemaRDD(sqlContext: SQLContext,
                    logicalPlan: LogicalPlan) extends SchemaRDD(sqlContext, logicalPlan) with CubeSchemaRDDLike {
  override def getMeasures: Array[String] = {
    Array("HI")
  }

  override def getDimensions: Array[String] = {
    Array("HI")
  }


}