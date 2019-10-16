name := "BrightnessVerifier"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.3"

run := Defaults.runTask(fullClasspath in Runtime, mainClass in run in Compile, runner in run).evaluated
