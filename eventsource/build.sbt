import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.oleglukin"
ThisBuild / organizationName := "oleglukin"

lazy val root = (project in file("."))
  .settings(
    name := "eventsource",
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.


// https://mvnrepository.com/artifact/org.json4s/json4s-jackson
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.6.7"

// https://mvnrepository.com/artifact/org.scalaj/scalaj-http
libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2"
