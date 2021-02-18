ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.4"

ThisBuild / organization := "com.peknight"

scalacOptions ++= Seq(
  "-Xfatal-warnings"
)

packageName in Docker := "pek/demo"
maintainer in Docker := "peknight <JKpeknight@gmail.com>"

lazy val demo = (project in file("."))
  .aggregate(demoCore, demoAkka, demoApp)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "demo",
  )

lazy val demoCore = (project in file("demo-core"))
  .settings(
    name := "demo-core",
    libraryDependencies ++= Seq(
    )
  )

val akkaVersion = "2.6.10"
val logbackVersion = "1.2.3"

val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion

lazy val demoAkka = (project in file("demo-akka"))
  .dependsOn(demoCore)
  .settings(
    name := "demo-akka",
    libraryDependencies ++= Seq(
      akkaActorTyped,
      logbackClassic
    )
  )

lazy val demoApp = (project in file("demo-app"))
  .dependsOn(demoCore)
  .settings(
    name := "demo-app",
    libraryDependencies ++= Seq(
    )
  )
