name := """http4s-crud-demo"""

version := "1.0"

scalaVersion := "2.11.8"

lazy val circeVersion = "0.5.1"
lazy val http4sVersion = "0.14.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest"            % "2.2.4"       % "test",
  "org.http4s"    %% "http4s-dsl"           % http4sVersion,
  "org.http4s"    %% "http4s-blaze-server"  % http4sVersion,
  "org.http4s"    %% "http4s-blaze-client"  % http4sVersion,
  "org.http4s"    %% "http4s-circe"         % http4sVersion,
  "io.circe"      %% "circe-core"           % circeVersion,
  "io.circe"      %% "circe-generic"        % circeVersion,
  "io.circe"      %% "circe-parser"         % circeVersion
)