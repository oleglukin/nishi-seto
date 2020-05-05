
scalaVersion := "2.12.11"

name := "sparkclient"
organization := "com.oleglukin"
version := "1.0"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5"