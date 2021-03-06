package org.apache.spark.sql.execution.joins

import scala.Array.canBuildFrom

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.sql.OlapCubeScan
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.BindReferences
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.Literal
import org.apache.spark.sql.execution.BinaryNode
import org.apache.spark.sql.execution.SparkPlan
import org.apache.spark.sql.execution.metric.SQLMetrics
import org.apache.spark.unsafe.types.UTF8String

/**
  * Created by k00900207 on 2015/11/05.
  */

case class FilterPushJoin(
                           leftKeys: Seq[Expression],
                           rightKeys: Seq[Expression],
                           buildSide: BuildSide,
                           left: SparkPlan,
                           right: SparkPlan,
                           condition: Option[Expression]) extends BinaryNode with HashJoin {

  override private[sql] lazy val metrics = Map(
    "numLeftRows" -> SQLMetrics.createLongMetric(sparkContext, "number of left rows"),
    "numRightRows" -> SQLMetrics.createLongMetric(sparkContext, "number of right rows"),
    "numOutputRows" -> SQLMetrics.createLongMetric(sparkContext, "number of output rows"))

  override def doExecute() = {

    val numOutputRows = longMetric("numOutputRows")
    val (numBuildRows, numStreamedRows) = buildSide match {
      case BuildLeft => (longMetric("numLeftRows"), longMetric("numRightRows"))
      case BuildRight => (longMetric("numRightRows"), longMetric("numLeftRows"))
    }

    //Referred the doExecute method from ShuffeldedHashJoin & BroadcastHashJoin
    //TODO Need to implement this join through broadcast like BroadcastHashJoin

    val buildPlanOutput = buildPlan.execute()
    val input: Array[InternalRow] = buildPlanOutput.map(_.copy()).collect()
    val input2: Array[InternalRow] = buildPlanOutput.map(_.copy()).collect()

    val keys = buildKeys.map { a =>
      BindReferences.bindReference(a, buildPlan.output)
    }.toArray

    val filters = keys.map {
      k =>
        input.map(
          r => {
            val curr = k.eval(r)
            if (curr.isInstanceOf[UTF8String])
              Literal(curr.toString).asInstanceOf[Expression]
            else Literal(curr).asInstanceOf[Expression]
          })
    }
    val olapScan = buildSide match {
      case BuildLeft => right
      case BuildRight => left
    }

    val cubeScan = olapScan.collectFirst { case a: OlapCubeScan => a }
    if (cubeScan.isDefined)
      cubeScan.get.addPushdownFilters(streamedKeys, filters, condition)

    val streamedPlanOutput = streamedPlan.execute()

    //    buildPlanOutput.zipPartitions(streamedPlanOutput) { (buildIter, streamIter) =>
    //      val hashed = HashedRelation(buildIter, numBuildRows, buildSideKeyGenerator)
    //      hashJoin(streamIter, numStreamedRows, hashed, numOutputRows)

    streamedPlanOutput.mapPartitions { streamedIter =>
      val hashed = HashedRelation(input2.iterator, numBuildRows, buildSideKeyGenerator)
      hashJoin(streamedIter, numStreamedRows, hashed, numOutputRows)
    }

  }
}
