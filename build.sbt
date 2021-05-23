name := "BrightnessVerifier"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.3"
libraryDependencies += "com.rabbitmq" % "amqp-client" % "5.12.0"

assembly / test := {}
assembly / assemblyOutputPath := new File("/opt/app/verifier.jar")

run := Defaults.runTask(fullClasspath in Runtime, mainClass in run in Compile, runner in run).evaluated
