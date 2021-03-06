/**
  *
  */
package com.huawei.datasight.spark.dsl.interpreter


import org.apache.spark.rdd.RDD
import java.io.File
import com.huawei.datasight.spark.processors.TransformHolder

/**
  * @author R00900208
  *
  */
class MolapSparkInterpreter {

  def evaluate(code: String, regAttrModel: EvaluatorAttributeModel): TransformHolder = {
    val exec = new Eval(Some(new File("temp"))).getInstance[SparkEvaluator](code)
    val trans = exec.getRDD(regAttrModel)
    trans
  }

}