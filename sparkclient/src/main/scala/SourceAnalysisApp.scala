package nishiseto

/**
  * Run the app locally, from sbt:
  * sbt "run argument1 argument2"
  * (+ select SourceAnalysisLocalApp when prompted)
  */
object SourceAnalysisLocalApp extends App {
  println("Running SourceAnalysisLocalApp")
}

/**
  * Use this object to submit the app to a cluster with spark-submit
  * */
object SourceAnalysisApp extends App {
  println("Running SourceAnalysisApp")
}