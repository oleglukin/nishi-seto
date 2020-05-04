package nishiseto

import org.apache.spark.sql.SparkSession

/**
  * Run the app locally, from sbt: sbt "run argument1 argument2"
  * (+ select SourceAnalysisLocalApp when prompted)
  */
object SourceAnalysisLocalApp extends App {
  val conf = Map(
    "appName" -> "SourceAnalysisJob",
    "sparkMaster" -> "local",
    "inputFolder" -> ConfigHelper.getArg(args, 0, "/tmp/input")
  )

  val spark = SparkSession.builder
     .master("local")
     .appName("Word Count")
     .config("spark.some.config.option", "some-value")
     .getOrCreate()

  println(s"Running SourceAnalysisLocalApp. Config:\n${conf("inputFolder")}")
  Runner run conf
}

/**
  * Use this object to submit the app to a cluster with spark-submit
  * */
object SourceAnalysisApp extends App {
  val conf = Map(
    "appName" -> "SourceAnalysisJob",
    "inputFolder" -> ConfigHelper.getArg(args, 0, "/tmp/input")
  )

  println("Running SourceAnalysisApp")
  Runner run conf
}


object Runner {
  def run(conf: Map[String,String]) = {
    val spark = SparkSession.builder().getOrCreate()
    

    SourceAggregation.functionalFailedBySource
  }
}


object ConfigHelper {
  def getArg(args: Array[String], index: Int, default: String): String = args.length match {
    case l if l > index => args(index)
    case _ => default
  }
}