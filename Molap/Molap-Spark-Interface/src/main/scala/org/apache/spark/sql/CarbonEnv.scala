package org.apache.spark.sql

import org.apache.spark.Logging
import org.apache.spark.sql.hive.HiveContext
import com.huawei.unibi.molap.util.MolapProperties
import com.huawei.unibi.molap.metadata.MolapMetadata
import com.huawei.datasight.molap.load.MolapLoaderUtil
import com.huawei.unibi.molap.util.MolapUtil
import com.huawei.unibi.molap.constants.MolapCommonConstants
import org.apache.hadoop.conf.Configuration

/**
  * Carbon Environment for unified context
  */
class CarbonEnv extends Logging {
  var carbonContext: HiveContext = _
  var carbonCatalog: OlapMetastoreCatalog = _
  val FS_DEFAULT_FS = "fs.defaultFS"
  val HDFSURL_PREFIX = "hdfs://"
  def init(context: HiveContext): Unit = {
    if (null == carbonContext) {
      carbonContext = context
      initCarbonCatalog(context);
    }
  }

  def initCarbonCatalog(context: HiveContext): Unit = {
    if (null == carbonCatalog) {
      var storeLocation = MolapProperties.getInstance().getProperty("carbon.storelocation")
      var conf:Configuration = new Configuration(true)
      var hdfsPath:String =conf.get(FS_DEFAULT_FS)
      if (storeLocation == null) {
        storeLocation = context.sparkContext.conf.get("carbon.storelocation", "/opt/carbon/store");
      }
      if(hdfsPath.startsWith(HDFSURL_PREFIX))
      {
        storeLocation=hdfsPath+storeLocation;
      }
      carbonCatalog = new OlapMetastoreCatalog(context.sparkContext, storeLocation)
      if (MolapProperties.getInstance.getProperty(MolapCommonConstants.LOADCUBE_STARTUP, "false") == "true") {
        val thread = new Thread {
          override def run {
            CarbonEnv.loadCarbonCubes(context, carbonCatalog)
          }
        }
        thread.start

      }
    }
  }

  
}

object CarbonEnv {
  val className = classOf[CarbonEnv].getCanonicalName

  def getInstance(sqlContext: SQLContext): CarbonEnv = {
    sqlContext.registerEnv[CarbonEnv](CarbonEnv.className)
  }

  var isloaded = false

  def loadCarbonCubes(sqlContext: SQLContext, carbonCatalog: OlapMetastoreCatalog): Unit = {
    val cubes = carbonCatalog.getAllCubes()(sqlContext)
    if (null != cubes && isloaded == false) {
      isloaded = true
      cubes.foreach {
        cube =>
          val schemaName = cube._1
          val cubeName = cube._2;
          val cubeInstance = MolapMetadata.getInstance().getCube(
            schemaName + '_' + cubeName);
          val filePath = cubeInstance.getMetaDataFilepath();
          val details = MolapUtil
            .readLoadMetadata(filePath)
          if (null != details) {
            var listOfLoadFolders = MolapLoaderUtil.getListOfValidSlices(details)
            if (null != listOfLoadFolders && listOfLoadFolders.size() > 0 /*&& MolapProperties.getInstance.getProperty("molap.kettle.home","false")=="true"*/ ) {
              var hc: HiveContext = sqlContext.asInstanceOf[HiveContext]
              hc.sql(" select count(*) from " + schemaName + "." + cubeName).collect()
            }
          }
      }

    }
  }

}


