package nishiseto

import org.apache.spark.sql.{Dataset,ForeachWriter,Row}
import org.apache.spark.sql.functions.{col,udf}
import org.apache.spark.sql.streaming.OutputMode.Update
import scalaj.http.Http

object SourceAggregation {
  /**
    * Check if signal value is a number, if not - consider it invalid
    * Group valid and invalid signals by source
    */
  def validBySource(ds: Dataset[Row], apiUrl: String) = {
    // Check if value is a number: https://rosettacode.org/wiki/Determine_if_a_string_is_numeric#Scala
    def isNumeric(str: String): Boolean = {
      !throwsNumberFormatException(str.toLong) || !throwsNumberFormatException(str.toDouble)
    } 
    
    def throwsNumberFormatException(f: => Any): Boolean = {
      try { f; false } catch { case e: NumberFormatException => true }
    }

    val valideUDF = udf {
      (value: String) => isNumeric(value)
    }


    val withValid = ds.withColumn("valid", valideUDF(col("value")))

    val grouped = withValid.groupBy("source", "valid").count

    val dsWriter = grouped.writeStream.foreach(new ForeachWriter[Row]() {
      def open(partitionId: Long, version: Long) = true

      def process(e: Row) = {
        val source = e.getAs[String]("source")
        val valid = e.getAs[Boolean]("valid")
        val count = e.getAs[Long]("count")

        val url = apiUrl + s"$source/$valid/$count"

        Http(url).postData("").asString.code match {
          case 200 => ()
          case code => println(s"Post response: $code")
        }
      }

      def close(errorOrNull: Throwable) = {} // Close the connection
    })

    dsWriter.outputMode(Update)
    dsWriter.start
  }
}