import compiler._
import deps._

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1"
ThisBuild / turbo := true
ThisBuild / scalacOptions := CompilerOpts.scalacFlags

lazy val root = project
  .in(file("."))
  .enablePlugins(RootProjectPlugin)
  .aggregate(
    core
  )

lazy val core = project
  .in(file("core"))
  .settings(
    name := "core",
    scalacOptions -= "-Ywarn-dead-code",
    libraryDependencies ++=
      Seq(
        typesafeConfig
      ) ++
      logging ++
      akka ++
      alpakka
  )

