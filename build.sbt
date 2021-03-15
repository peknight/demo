ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.4"

ThisBuild / organization := "com.peknight"

lazy val commonSettings = Seq(
  addCompilerPlugin(kindProjector),
  scalacOptions ++= Seq(
    "-deprecation",
    "-Xfatal-warnings",
    "-Ymacro-annotations",
  ),
)

packageName in Docker := "pek/demo"
maintainer in Docker := "peknight <JKpeknight@gmail.com>"

lazy val demo = (project in file("."))
  .aggregate(demoCore, demoMath, demoFpInScala, demoCats, demoMonocle, demoJson, demoAkka, demoApp)
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)
  .settings(
    name := "demo",
  )

lazy val demoCore = (project in file("demo-core"))
  .settings(commonSettings)
  .settings(
    name := "demo-core",
    libraryDependencies ++= Seq(
      pekCommonCore,
    ),
  )

lazy val demoMath = (project in file("demo-math"))
  .settings(commonSettings)
  .settings(
    name := "demo-math",
    libraryDependencies ++= Seq(
      pekCommonMath,
    ),
  )

lazy val demoFpInScala = (project in file("demo-fp"))
  .settings(commonSettings)
  .settings(
    name := "demo-fp",
    libraryDependencies ++= Seq(
      pekCommonFp,
    ),
  )

lazy val demoCats = (project in file("demo-cats"))
  .settings(commonSettings)
  .settings(
    name := "demo-cats",
    libraryDependencies ++= Seq(
      pekCommonFp,
    ),
  )

lazy val demoMonocle = (project in file("demo-monocle"))
  .settings(commonSettings)
  .settings(
    name := "demo-monocle",
    libraryDependencies ++= Seq(
      pekCommonFp,
    ),
  )

lazy val demoJson = (project in file("demo-json"))
  .settings(commonSettings)
  .settings(
    name := "demo-json",
    libraryDependencies ++= Seq(
      pekCommonJson,
    ),
  )

lazy val demoAkka = (project in file("demo-akka"))
  .dependsOn(demoCore)
  .settings(commonSettings)
  .settings(
    name := "demo-akka",
    libraryDependencies ++= Seq(
      pekCommonAkka,
    ),
  )

lazy val demoApp = (project in file("demo-app"))
  .dependsOn(demoCore)
  .settings(commonSettings)
  .settings(
    name := "demo-app",
    libraryDependencies ++= Seq(
    ),
  )

val kindProjectorVersion = "0.11.3"
val pekCommonVersion = "0.1-SNAPSHOT"

val kindProjector = "org.typelevel" % "kind-projector" % kindProjectorVersion cross CrossVersion.full
val pekCommonCore = "com.peknight" %% "common-core" % pekCommonVersion
val pekCommonMath = "com.peknight" %% "common-math" % pekCommonVersion
val pekCommonFp = "com.peknight" %% "common-fp" % pekCommonVersion
val pekCommonJson = "com.peknight" %% "common-json" % pekCommonVersion
val pekCommonAkka = "com.peknight" %% "common-akka" % pekCommonVersion
