import _root_.sbt.Keys._

name := """demo"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  javaWs,
  javaCore,
  cache,
  "org.apache.directory.studio" % "org.apache.commons.io" % "2.4",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.9.Final",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
)

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.10.21"
)

dependencyOverrides += "com.google.guava" % "guava" % "18.0"

dependencyOverrides += "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.3"

dependencyOverrides += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.3"

dependencyOverrides += "junit" % "junit" % "4.12"

//SendGrid
libraryDependencies += "com.sendgrid" % "sendgrid-java" % "2.2.2"


PlayKeys.externalizeResources := false

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
