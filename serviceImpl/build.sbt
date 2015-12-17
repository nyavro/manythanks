scalaVersion := "2.11.7"

scalaBinaryVersion:= CrossVersion.binaryScalaVersion("2.11.7")

libraryDependencies ++= Seq(
  "org.scalatest"      % "scalatest_2.11"                   % ScalatestVersion % "test",
  "org.scalamock"      % "scalamock-scalatest-support_2.11" % ScalamockVersion % "test"
)