import play.PlayScala
import sbt.Keys._
import sbt._

object ProjectBuild extends Build {

  val ScalaVersion = "2.11.7"
  val ScalatestVersion = "3.0.0-M7"
  val ApacheCommonsVersion = "2.4"
  val SpecsVersion = "3.0.0-M9"
  val Json4sVersion = "3.3.0"
  val AkkaStreamVersion = "1.0"

  lazy val parent = Project(
    id = "parent",
    base = file("."),
    settings = super.settings ++ sharedSettings
  )
  .settings(
    name := "Many thanks!"
  )
  .aggregate(gcmTransport, webService)

  lazy val gcmTransport = Project(
    id = "gcmTransport",
    base = file("gcmTransport"),
    settings = super.settings ++ sharedSettings
  )
  .settings(
    libraryDependencies ++= Seq(
      "commons-io" % "commons-io" % "2.4",
      "org.json4s" % "json4s-native_2.11" % Json4sVersion
    )
  )

  lazy val webService2 = Project(
    id = "webService2",
    base = file("webService2"),
    settings = super.settings ++ sharedSettings
  )
    .settings(
      libraryDependencies ++= Seq(
        "com.google.inject" % "guice" % "4.0",
        "javax.inject" % "javax.inject" % "1"
      ),
      resolvers ++= Seq(
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
      )
    )
    .enablePlugins(PlayScala)

  lazy val webService = Project(
    id = "webService",
    base = file("webService"),
    settings = super.settings ++ sharedSettings
  )
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka"  %% "akka-stream-experimental"             % AkkaStreamVersion,
        "com.typesafe.akka"  %% "akka-http-core-experimental"          % AkkaStreamVersion,
        "com.typesafe.akka"  %% "akka-http-spray-json-experimental"    % AkkaStreamVersion
      )
    )

  lazy val sharedSettings = super.settings ++ Seq(
    version := "1.0.0",
    scalaVersion := ScalaVersion,
    scalaBinaryVersion:= CrossVersion.binaryScalaVersion(ScalaVersion),
    autoCompilerPlugins := true,
    scalacOptions ++= Seq(
      "-language:postfixOps",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:higherKinds",
      "-language:existentials",
      "-Yinline-warnings",
      "-Xlint",
      "-deprecation",
      "-feature",
      "-unchecked"
    ),
    ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
  )
}