package nishiseto

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.ForeachWriter
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.streaming.OutputMode.Update
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions.udf

object SourceAggregation {
    /**
      * Check if signal value is a number, if not - consider it invalid
      * Group valid and invalid signals by source
      */
    def validBySource(ds: Dataset[Row]) = {
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

                println(e)
            }

            def close(errorOrNull: Throwable) = {} // Close the connection

        })

        dsWriter.outputMode(Update)
        dsWriter.start
    }
}