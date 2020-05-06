package nishiseto

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Encoders


/**
  * Run the app locally, from sbt: sbt "run argument0 argument1"
  * (+ select SourceAnalysisLocalApp when prompted)
  */
object SourceAnalysisLocalApp extends App {
  val conf = Map(
    "appName" -> "SourceAnalysisJob",
    "sparkMaster" -> "local",
    "inputFolder" -> ConfigHelper.getArg(args, 0, "/tmp/input")
  )

  val spark = SparkSession.builder.master("local").appName(conf("appName")).getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  spark.sparkContext.uiWebUrl match {
    case Some(value) => println("WebUI: " + value)
    case None => println("Could not get Spark web UI")
  }

  println(s"Running SourceAnalysisLocalApp. Config:\n${conf("inputFolder")}")
  Runner.run(spark, conf)
}


/**
  * Use this object to submit the app to a cluster with spark-submit
  * */
object SourceAnalysisApp extends App {
  val conf = Map(
    "appName" -> "SourceAnalysisJob",
    "inputFolder" -> ConfigHelper.getArg(args, 0, "/tmp/input")
  )

  val spark = SparkSession.builder().getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  println(s"Running SourceAnalysisApp. Config:\n${conf("inputFolder")}")
  Runner.run(spark, conf)
}


object Runner {
  def run(spark: SparkSession, conf: Map[String,String]) = {
    val ds = spark.readStream.format("json").option("inferSchema", "true").text(conf("inputFolder")).as(Encoders.STRING)

    SourceAggregation.functionalFailedBySource
    spark.streams.awaitAnyTermination
  }
}


object ConfigHelper {
  def getArg(args: Array[String], index: Int, default: String): String = args.length match {
    case l if l > index => args(index)
    case _ => default
  }
}