import org.flywaydb.sbt.FlywayPlugin._

val SlickVersion     = "3.1.0"
val MySQLConnectorVersion = "5.1.37"
val FlywayVersion = "3.2.1"
val ScalaTestVersion = "3.0.0-M14"
val ScalaMockVersion = "3.2.2"

Seq(flywaySettings: _*)

flywayUrl       := "jdbc:mysql://localhost/test"

flywayUser      := "root"

flywayPassword  := ""

flywayLocations := Seq("filesystem:repository/src/main/resources/db/migration")

flywayValidateOnMigrate := true

scalaVersion := "2.11.7"

scalaBinaryVersion:= CrossVersion.binaryScalaVersion("2.11.7")

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % MySQLConnectorVersion,
  "com.typesafe.slick" % "slick_2.11" % SlickVersion,
  "org.flywaydb" % "flyway-core" % FlywayVersion,
  "org.scalatest" % "scalatest_2.11" % ScalaTestVersion % "test",
  "org.scalamock" % "scalamock-scalatest-support_2.11" % ScalaMockVersion % "test"
)