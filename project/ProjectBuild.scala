import sbt.Keys._
import sbt._

object ProjectBuild extends Build {

  val ScalaVersion = "2.11.7"
  val ScalatestVersion = "3.0.0-M7"
  val ApacheCommonsVersion = "2.4"
  val SpecsVersion = "3.0.0-M9"
  val Json4sVersion = "3.3.0"
  val AkkaStreamVersion = "1.0"
  val GuiceVersion = "4.0"
  val SprayTestkitVersion = "1.3.1"
  val ScalamockVersion = "3.2.2"

  lazy val parent = Project(
    id = "parent",
    base = file("."),
    settings = super.settings ++ sharedSettings
  )
  .settings(
    name := "Many thanks!"
  )
  .aggregate(gcmTransport, repository, webService, serviceAPI, serviceImpl)

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

  lazy val webService = Project(
    id = "webService",
    base = file("webService"),
    settings = super.settings ++ sharedSettings
  )
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka"  % "akka-stream-experimental_2.11"             % AkkaStreamVersion,
        "com.typesafe.akka"  % "akka-http-core-experimental_2.11"          % AkkaStreamVersion,
        "com.typesafe.akka"  % "akka-http-spray-json-experimental_2.11"    % AkkaStreamVersion,
        "com.google.inject"  % "guice"                                     % GuiceVersion,
        "org.scalatest"      % "scalatest_2.11"                            % ScalatestVersion  % "test",
        "com.typesafe.akka"  % "akka-http-testkit-experimental_2.11"       % AkkaStreamVersion % "it,test",
        "io.spray"           % "spray-routing_2.11"                        % SprayTestkitVersion % "test",
        "org.scalamock"      % "scalamock-scalatest-support_2.11"          % ScalamockVersion % "test"
      )
    )
    .configs(IntegrationTest)
    .dependsOn(serviceAPI)

  lazy val repository = project.in(file("repository"))

  lazy val serviceAPI = project.in(file("serviceAPI"))

  lazy val serviceImpl = project
    .in(file("serviceImpl"))
    .dependsOn(serviceAPI, repository, gcmTransport)

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