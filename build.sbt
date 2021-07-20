ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.6"

ThisBuild / organization := "com.peknight"

Docker / packageName := "pek/demo"
Docker / maintainer := "peknight <JKpeknight@gmail.com>"

lazy val commonSettings = Seq(
  addCompilerPlugin(kindProjector),
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ymacro-annotations",
  ),
)

lazy val demo = (project in file("."))
  .aggregate(demoCore, demoMath, demoFpInScala, demoCats, demoCatsEffect, demoMonocle, demoJson, demoAkka, demoApp,
    demoScala3, demoJs)
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
      pekCommonTest,
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

lazy val demoCatsEffect = (project in file("demo-cats-effect"))
  .settings(commonSettings)
  .settings(
    name := "demo-cats-effect",
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

lazy val demoScala3 = (project in file("demo-scala3"))
  .settings(
    name := "demo-scala3",
    scalaVersion := "3.0.0"
  )

lazy val demoJs = (project in file("demo-js"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "demo-js",
    // This is an application with a main method
    scalaJSUseMainModuleInitializer := true,
  )

val kindProjectorVersion = "0.13.0"
val pekCommonVersion = "0.1-SNAPSHOT"

val kindProjector = "org.typelevel" % "kind-projector" % kindProjectorVersion cross CrossVersion.full
val pekCommonCore = "com.peknight" %% "common-core" % pekCommonVersion
val pekCommonMath = "com.peknight" %% "common-math" % pekCommonVersion
val pekCommonFp = "com.peknight" %% "common-fp" % pekCommonVersion
val pekCommonTest = "com.peknight" %% "common-test" % pekCommonVersion
val pekCommonJson = "com.peknight" %% "common-json" % pekCommonVersion
val pekCommonAkka = "com.peknight" %% "common-akka" % pekCommonVersion
