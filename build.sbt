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
  "org.hibernate" % "hibernate-entitymanager" % "4.3.9.Final",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
)

PlayKeys.externalizeResources := false

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
