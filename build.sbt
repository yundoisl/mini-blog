enablePlugins(JavaAppPackaging, AshScriptPlugin)

name := "mini-blog"

version := "0.1"

scalaVersion := "2.13.3"

val akkaVersion = "2.6.8"
val akkaHttpVersion = "10.2.0"
val scalaMockVersion = "5.0.0"
val scalatestVersion = "3.2.0"
val circeVersion = "0.12.3"
val testcontainersVersion = "0.38.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.postgresql" % "postgresql" % "42.2.11",
  "org.scalamock" %% "scalamock" % scalaMockVersion % Test,
  "org.scalatest" %% "scalatest" % scalatestVersion % Test,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",
  "com.pauldijou" %% "jwt-circe" % "4.2.0",
  "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersVersion % "test",
  "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersVersion % "test"
)
// TODO: organize version
