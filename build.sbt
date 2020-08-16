enablePlugins(JavaAppPackaging, AshScriptPlugin)

name := "mini-blog"

version := "0.1"

scalaVersion := "2.13.3"

val akkaVersion = "2.6.8"
val akkaHttpVersion = "10.2.0"
val scalaMockVersion = "5.0.0"
val scalatestVersion = "3.2.0"
val akkaHttpCirceVersion = "1.31.0"
val circeVersion = "0.12.3"
val jwtCirceVersion = "4.2.0"
val testcontainersVersion = "0.38.1"
val postgresqlVersion = "42.2.11"
val scalaLoggingVersion = "3.9.2"
val logbackVersion = "1.2.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "org.postgresql" % "postgresql" % postgresqlVersion,
  "org.scalamock" %% "scalamock" % scalaMockVersion % Test,
  "org.scalatest" %% "scalatest" % scalatestVersion % Test,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
  "com.pauldijou" %% "jwt-circe" % jwtCirceVersion,
  "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersVersion % Test,
  "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersVersion % Test,
  "com.google.cloud.sql" % "postgres-socket-factory" % "1.0.16"
)
