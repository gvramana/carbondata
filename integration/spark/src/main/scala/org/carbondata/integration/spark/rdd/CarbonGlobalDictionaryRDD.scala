/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.carbondata.integration.spark.rdd

import java.nio.charset.Charset
import java.util
import java.util.regex.Pattern
import java.util.Comparator

import scala.collection.mutable.{ArrayBuffer, HashMap, HashSet}

import org.apache.commons.lang3.{ArrayUtils, StringUtils}
import org.apache.spark.{Logging, Partition, Partitioner, TaskContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

import org.carbondata.common.logging.LogServiceFactory
import org.carbondata.core.cache.dictionary.DictionaryByteArrayWrapper
import org.carbondata.core.carbon.CarbonTableIdentifier
import org.carbondata.core.carbon.metadata.schema.table.column.CarbonDimension
import org.carbondata.core.constants.CarbonCommonConstants
import org.carbondata.core.util.ByteUtil
import org.carbondata.integration.spark.load.CarbonLoaderUtil
import org.carbondata.integration.spark.util.{CarbonSparkInterFaceLogEvent, GlobalDictionaryUtil}

/**
 * A partitioner partition by column.
 *
 * @constructor create a partitioner
 * @param numParts  the number of partitions
 */
class ColumnPartitioner(numParts: Int) extends Partitioner {
  override def numPartitions: Int = numParts

  override def getPartition(key: Any): Int = key.asInstanceOf[DictionaryShuffleKey].columnId
}

trait GenericParser {
  val dimension: CarbonDimension
  def addChild(child: GenericParser): Unit
  def parseString(input: String): Unit
}

case class PrimitiveParser(dimension: CarbonDimension,
  setOpt: Option[HashSet[String]]) extends GenericParser {
  val charset = Charset.forName(CarbonCommonConstants.DEFAULT_CHARSET)
  val (hasDictEncoding, set: HashSet[String]) = setOpt match {
    case None => (false, new HashSet[String])
    case Some(x) => (true, x)
  }

  def addChild(child: GenericParser): Unit = {
  }

  def parseString(input: String): Unit = {
    if (hasDictEncoding) {
      set.add(input)
    }
  }
}

case class ArrayParser(val dimension: CarbonDimension,
                       val format: DataFormat) extends GenericParser {
  var children: GenericParser = _
  def addChild(child: GenericParser): Unit = {
    children = child
  }
  def parseString(input: String): Unit = {
    if (StringUtils.isNotEmpty(input)) {
      val splits = format.getSplits(input)
      if (ArrayUtils.isNotEmpty(splits)) {
        for (i <- 0 until splits.length) {
          children.parseString(splits(i))
        }
      }
    }
  }
}

case class StructParser(dimension: CarbonDimension,
                        format: DataFormat) extends GenericParser {
  val children = new ArrayBuffer[GenericParser]
  def addChild(child: GenericParser): Unit = {
    children += child
  }
  def parseString(input: String): Unit = {
    if (StringUtils.isNotEmpty(input)) {
      val splits = format.getSplits(input)
      val len = Math.min(children.length, splits.length)
      for (i <- 0 until len) {
        children(i).parseString(splits(i))
      }
    }
  }
}

case class DataFormat(delimiters: Array[String],
                      var delimiterIndex: Int,
                      patterns: Array[Pattern]) extends Serializable {
  self =>
  def getSplits(input: String): Array[String] = {
    // -1 in case after splitting the last column is empty, the surrogate key ahs to be generated
    // for empty value too
    patterns(delimiterIndex).split(input, -1)
  }

  def cloneAndIncreaseIndex: DataFormat = {
    DataFormat(delimiters, Math.min(delimiterIndex + 1, delimiters.length - 1), patterns)
  }
}

class ByteArrayComparator extends Comparator[DictionaryByteArrayWrapper]{

  def compare(o1: DictionaryByteArrayWrapper, o2: DictionaryByteArrayWrapper): Int = {
    o1.compareTo(o2)
  }

  override def equals(obj: Any): Boolean = {
    false
  }

}

case class DictionaryShuffleKey( columnId: Int, data: String)
/*    extends Comparable[DictionaryShuffleKey]  {

//  override def equals(obj: java.lang.Object): Boolean = {
//    equals(obj.asInstanceOf[scala.Any])
//  }
//
//  override def equals(obj: scala.Any): Boolean = {
//    if (this == obj) {
//      true
//    } else if (!obj.isInstanceOf[DictionaryShuffleKey]) {
//      false
//    } else {
//      val objKey = obj.asInstanceOf[DictionaryShuffleKey]
//      if (this.columnId == objKey.columnId
//          && ByteUtil.UnsafeComparer.INSTANCE.equals(data, objKey.data)) {
//        return true
//      } else {
//        false
//      }
//    }
//  }


//  override def hashCode(): Int = {
//    val state = Seq(columnId, data)
//    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
//  }
  def canEqual(other: Any): Boolean = other.isInstanceOf[DictionaryShuffleKey]

  override def equals(other: Any): Boolean = other match {
    case that: DictionaryShuffleKey =>
      (that canEqual this) &&
       columnId == that.columnId &&
       ByteUtil.UnsafeComparer.INSTANCE.equals(data, that.data)
    case _ => false
  }

  override def hashCode(): Int = {
    columnId + (31 * util.Arrays.hashCode(data))
  }

  override def compareTo(o: DictionaryShuffleKey): Int =
    ByteUtil.UnsafeComparer.INSTANCE.compareTo(data, o.data)
}
*/
/**
 * a case class to package some attributes
 */
case class DictionaryLoadModel(table: CarbonTableIdentifier,
                               dimensions: Array[CarbonDimension],
                               hdfsLocation: String,
                               dictfolderPath: String,
                               dictFilePaths: Array[String],
                               dictFileExists: Array[Boolean],
                               isComplexes: Array[Boolean],
                               primDimensions: Array[CarbonDimension],
                               delimiters: Array[String]) extends Serializable
/**
 * A RDD to combine distinct values in block.
 *
 * @constructor create a RDD with RDD[Row]
 * @param prev the input RDD[Row]
 * @param model a model package load info
 */
class CarbonBlockDistinctValuesCombineRDD(
  prev: RDD[Row],
  model: DictionaryLoadModel)
    extends RDD[(DictionaryShuffleKey, Int)](prev) with Logging {

  override def getPartitions: Array[Partition] = firstParent[Row].partitions

  override def compute(split: Partition, context: TaskContext):
  Iterator[(DictionaryShuffleKey, Int)] = {
    val LOGGER = LogServiceFactory.getLogService(this.getClass().getName());

    val distinctValuesList = new ArrayBuffer[(Int, HashSet[String])]
    try {
      // local combine set
      val dimNum = model.dimensions.length
      val primDimNum = model.primDimensions.length
      val columnValues = new Array[HashSet[String]](primDimNum)
      val mapColumnValuesWithId = new HashMap[String, HashSet[String]]
      for (i <- 0 until primDimNum) {
        columnValues(i) = new HashSet[String]
        distinctValuesList += ((i, columnValues(i)))
        mapColumnValuesWithId.put(model.primDimensions(i).getColumnId, columnValues(i))
      }
      val dimensionParsers = new Array[GenericParser](dimNum)
      for (j <- 0 until dimNum) {
        dimensionParsers(j) = GlobalDictionaryUtil.generateParserForDimension(
          Some(model.dimensions(j)),
          GlobalDictionaryUtil.createDataFormat(model.delimiters),
          mapColumnValuesWithId).get
      }
      var row: Row = null
      val rddIter = firstParent[Row].iterator(split, context)
      // generate block distinct value set
      while (rddIter.hasNext) {
        row = rddIter.next()
        if (row != null) {
          for (i <- 0 until dimNum) {
            dimensionParsers(i).parseString(row.getString(i))
          }
        }
      }
    } catch {
      case ex: Exception =>
        LOGGER.error(CarbonSparkInterFaceLogEvent.UNIBI_CARBON_SPARK_INTERFACE_MSG, ex)
    }
    val totalSize = distinctValuesList.foldLeft[Int](0)(
      (size, oneColumnValues) => size + oneColumnValues._2.size)
    val distinctIndividualValueList = new ArrayBuffer[(DictionaryShuffleKey, Int)](totalSize)
    distinctValuesList.map { iter =>
      val valueList = iter._2.toArray
      // java.util.Arrays.sort(valueList, new ByteArrayComparator)
      valueList.foreach { x =>
        distinctIndividualValueList.+= ((new DictionaryShuffleKey(iter._1, x), 0))
      }
    }
    distinctIndividualValueList.iterator
  }
}

/**
 * A RDD to generate dictionary file for each column
 *
 * @constructor create a RDD with RDD[Row]
 * @param prev the input RDD[Row]
 * @param model a model package load info
 */
class CarbonGlobalDictionaryGenerateRDD(
  prev: RDD[(DictionaryShuffleKey, Int)],
  model: DictionaryLoadModel)
    extends RDD[(String, String)](prev) with Logging {

  override def getPartitions: Array[Partition] =
    firstParent[(DictionaryShuffleKey, Int)].partitions

  override def compute(split: Partition, context: TaskContext): Iterator[(String, String)] = {
    val LOGGER = LogServiceFactory.getLogService(this.getClass().getName());
    var status = CarbonCommonConstants.STORE_LOADSTATUS_SUCCESS
    val iter = new Iterator[(String, String)] {
      // generate distinct value list
      try {
        val t1 = System.currentTimeMillis
        val dictionary = if (model.dictFileExists(split.index)) {
          CarbonLoaderUtil.getDictionary(model.table,
            model.primDimensions(split.index).getColumnId,
            model.hdfsLocation,
            model.primDimensions(split.index).getDataType
          )
        } else {
          null
        }
        val t2 = System.currentTimeMillis
        val rddIter = firstParent[(DictionaryShuffleKey, Int)].iterator(split, context)
        val distinctValueCount = GlobalDictionaryUtil.generateAndWriteNewDistinctValueList(
          rddIter, dictionary, model, split.index
        )

        val t3 = System.currentTimeMillis

        if (distinctValueCount > 0) {
          val columnDictionary = CarbonLoaderUtil.getDictionary(model.table,
            model.primDimensions(split.index).getColumnId,
            model.hdfsLocation,
            model.primDimensions(split.index).getDataType)
          GlobalDictionaryUtil.writeGlobalDictionaryColumnSortInfo(model, split.index,
            columnDictionary
          )
          val t4 = System.currentTimeMillis

          LOGGER.info(CarbonSparkInterFaceLogEvent.UNIBI_CARBON_SPARK_INTERFACE_MSG,
            "\n columnName : " + model.primDimensions(split.index).getColName +
              "\n columnId : " + model.primDimensions(split.index).getColumnId +
              "\n new distinct values count : " + distinctValueCount +
              "\n create dictionary cache : " + (t2 - t1) +
              "\n write dictionary : " + (t3 - t2) +
              "\n write sort info : " + (t4 - t3)
          )
        }
      } catch {
        case ex: Exception =>
          status = CarbonCommonConstants.STORE_LOADSTATUS_FAILURE
          LOGGER.error(CarbonSparkInterFaceLogEvent.UNIBI_CARBON_SPARK_INTERFACE_MSG, ex)
      }
      var finished = false

      override def hasNext: Boolean = {

        if (!finished) {
          finished = true
          finished
        } else {
          !finished
        }
      }

      override def next(): (String, String) = {
        (model.primDimensions(split.index).getColName, status)
      }
    }
    iter
  }
}
