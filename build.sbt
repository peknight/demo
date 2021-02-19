ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.4"

ThisBuild / organization := "com.peknight"

scalacOptions ++= Seq(
  "-Xfatal-warnings"
)

packageName in Docker := "pek/demo"
maintainer in Docker := "peknight <JKpeknight@gmail.com>"

lazy val demo = (project in file("."))
  .aggregate(demoCore, demoMath, demoAkka, demoApp)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "demo",
  )

lazy val demoCore = (project in file("demo-core"))
  .settings(
    name := "demo-core",
    libraryDependencies ++= Seq(
      pekCommonCore,
    ),
  )

lazy val demoMath = (project in file("demo-math"))
  .settings(
    name := "demo-math",
    libraryDependencies ++= Seq(
      pekCommonMath
    ),
  )

lazy val demoAkka = (project in file("demo-akka"))
  .dependsOn(demoCore)
  .settings(
    name := "demo-akka",
    libraryDependencies ++= Seq(
      pekCommonAkka,
    ),
  )

lazy val demoApp = (project in file("demo-app"))
  .dependsOn(demoCore)
  .settings(
    name := "demo-app",
    libraryDependencies ++= Seq(
    ),
  )

val pekCommonVersion = "0.1-SNAPSHOT"

val pekCommonCore = "com.peknight" %% "common-core" % pekCommonVersion
val pekCommonAkka = "com.peknight" %% "common-akka" % pekCommonVersion
val pekCommonMath = "com.peknight" %% "common-math" % pekCommonVersion
