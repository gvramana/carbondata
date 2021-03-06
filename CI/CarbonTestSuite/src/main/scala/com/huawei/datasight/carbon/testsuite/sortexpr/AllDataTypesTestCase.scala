package com.huawei.datasight.carbon.testsuite.sortexpr

import org.scalatest.BeforeAndAfter
import org.apache.spark.sql.common.util.QueryTest
import org.apache.spark.sql.common.util.CarbonHiveContext._
import org.apache.spark.sql.Row

/**
 * Test Class for sort expression query on multiple datatypes
 * @author N00902756
 *
 */

class AllDataTypesTestCase extends QueryTest with BeforeAndAfter {
  
  import org.apache.spark.sql.common.util.CarbonHiveContext.implicits._
  
  before
  {
	  sql("CREATE CUBE alldatatypescube DIMENSIONS (empno Integer, empname String, designation String, doj Timestamp, workgroupcategory Integer, workgroupcategoryname String, deptno Integer, deptname String, projectcode Integer, projectjoindate Timestamp, projectenddate Timestamp) MEASURES (attendance Integer,utilization Integer,salary Integer) OPTIONS (PARTITIONER [PARTITION_COUNT=1])")
	  sql("LOAD DATA fact from './TestData/data.csv' INTO CUBE alldatatypescube PARTITIONDATA(DELIMITER ',', QUOTECHAR '\"')");
  }
  test("select empno,empname,utilization,count(salary),sum(empno) from alldatatypescube where empname in ('arvind','ayushi') group by empno,empname,utilization") 
  {
    checkAnswer(
      sql("select empno,empname,utilization,count(salary),sum(empno) from alldatatypescube where empname in ('arvind','ayushi') group by empno,empname,utilization"),
      Seq(Row(11,"arvind",96.2,1,11), Row(15,"ayushi",91.5,1,15)))
  }
  after
  {
	  sql("drop cube alldatatypescube")
  }
}