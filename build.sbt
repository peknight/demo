ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

ThisBuild / organization := "com.peknight"

Docker / packageName := "pek/demo"
Docker / maintainer := "peknight <JKpeknight@gmail.com>"

lazy val commonSettings2 = Seq(
  addCompilerPlugin(kindProjector),
  scalaVersion := "2.13.8",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ymacro-annotations",
//    "-Ywarn-value-discard",
  ),
)

lazy val commonSettings = Seq(
  scalaVersion := "3.1.2",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:strictEquality",
    //    "-Ywarn-value-discard",
  ),
)

lazy val demo = (project in file("."))
  .aggregate(demoCore, demoMath, demoFpInScala, demoCats, demoCatsEffect, demoFs2, demoMonocle, demoJson, demoAkka,
    demoRx, demoAsync, demoApp, demoScala3, demoJs.jvm, demoJs.js)
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings2)
  .settings(
    name := "demo",
  )

lazy val demoCore = (project in file("demo-core"))
  .settings(commonSettings2)
  .settings(
    name := "demo-core",
    libraryDependencies ++= Seq(
      pekCommonCore,
    ),
  )

lazy val demoMath = (project in file("demo-math"))
  .settings(commonSettings2)
  .settings(
    name := "demo-math",
    libraryDependencies ++= Seq(
      pekCommonMath,
    ),
  )

lazy val demoFpInScala = (project in file("demo-fp"))
  .settings(commonSettings2)
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
      catsCore,
    ),
  )

lazy val demoCatsEffect = (project in file("demo-cats-effect"))
  .settings(commonSettings)
  .settings(
    name := "demo-cats-effect",
    libraryDependencies ++= Seq(
      catsEffect,
      catsEffectTestkit % Test,
      catsEffectTestingSpecs % Test,
      mUnitCatsEffect % Test,
      weaverCats % Test,
    ),
  )

lazy val demoFs2 = (project in file("demo-fs2"))
  .settings(commonSettings)
  .settings(
    name := "demo-fs2",
    libraryDependencies ++= Seq(
      fs2Core,
      fs2IO,
      fs2ReactiveStreams,
      fs2Scodec,
    ),
  )

lazy val demoMonocle = (project in file("demo-monocle"))
  .settings(commonSettings2)
  .settings(
    name := "demo-monocle",
    libraryDependencies ++= Seq(
      pekCommonFp,
    ),
  )

lazy val demoJson = (project in file("demo-json"))
  .settings(commonSettings2)
  .settings(
    name := "demo-json",
    libraryDependencies ++= Seq(
      pekCommonJson,
    ),
  )

lazy val demoAkka = (project in file("demo-akka"))
//  .dependsOn(demoCore)
  .settings(commonSettings)
  .settings(
    name := "demo-akka",
    libraryDependencies ++= Seq(
      akkaActorTyped,
      logbackClassic,
    ),
  )

lazy val demoRx = (project in file("demo-rx"))
  .dependsOn(demoCore)
  .settings(commonSettings2)
  .settings(
    name := "demo-rx",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalarx" % "0.4.3",
      "com.lihaoyi" %%% "utest" % "0.7.4",
    ),
  )

lazy val demoAsync = (project in file("demo-async"))
  .settings(commonSettings2)
  .settings(
    name := "demo-async",
    scalacOptions ++= Seq(
      "-Xasync",
    ),
    libraryDependencies ++= Seq(
      scalaAsync,
      scalaReflect % Provided,
    ),
  )

lazy val demoApp = (project in file("demo-app"))
//  .dependsOn(demoCore)
  .settings(commonSettings)
  .settings(
    name := "demo-app",
    libraryDependencies ++= Seq(
    ),
  )

lazy val demoScala3 = (project in file("demo-scala3"))
//  .dependsOn(demoCore)
  .settings(commonSettings)
  .settings(
    name := "demo-scala3",
    libraryDependencies ++= Seq(
      scalaTest % Test,
    ),
  )

lazy val demoJs = (crossProject(JSPlatform, JVMPlatform) in file("demo-js"))
//  .enablePlugins(ScalaJSPlugin) crossProject下看起来不需要设置
  .settings(commonSettings2)
  .settings(
    name := "demo-js",
    scalacOptions ++= Seq(
      "-Xasync",
    ),
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % "2.6.1",
      "com.lihaoyi" %%% "utest" % "0.7.4",
      "com.lihaoyi" %%% "scalatags" % "0.9.4",
      "com.lihaoyi" %%% "upickle" % "1.4.0",
      "com.lihaoyi" %%% "autowire" % "0.3.3",
      "com.lihaoyi" %%% "scalarx" % "0.4.3",
    ),
  )
  .jvmSettings(
    // Add JVM-specific settings here
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.6.15",
      "com.typesafe.akka" %% "akka-stream" % "2.6.15",
      "com.typesafe.akka" %% "akka-http" % "10.2.5",
      "org.webjars" % "bootstrap" % "5.0.2",
    ),
  )
  .jsSettings(
    // Add JS-specific settings here
    // This is an application with a main method
    scalaJSUseMainModuleInitializer := true,
    Compile / mainClass := Some("com.peknight.demo.js.tutorial.webapp.TutorialApp"),
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.1.0",
      "org.scala-lang.modules" %% "scala-async" % "1.0.0",
    ),
  )

val kindProjectorVersion = "0.13.2"
val pekCommonVersion = "0.1-SNAPSHOT"

val kindProjector = "org.typelevel" % "kind-projector" % kindProjectorVersion cross CrossVersion.full
val pekCommonCore = "com.peknight" %% "common-core" % pekCommonVersion
val pekCommonLog = "com.peknight" %% "common-log" % pekCommonVersion
val pekCommonMath = "com.peknight" %% "common-math" % pekCommonVersion
val pekCommonFp = "com.peknight" %% "common-fp" % pekCommonVersion
val pekCommonTest = "com.peknight" %% "common-test" % pekCommonVersion
val pekCommonJson = "com.peknight" %% "common-json" % pekCommonVersion
val pekCommonAkka = "com.peknight" %% "common-akka" % pekCommonVersion

// Scala

val scalaTestVersion = "3.2.11"

val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion

// Functional

val catsVersion = "2.7.0"
val catsEffectVersion = "3.3.11"
val fs2Version = "3.2.7"

val catsCore = "org.typelevel" %% "cats-core" % catsVersion
val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectVersion withSources() withJavadoc()
val fs2Core = "co.fs2" %% "fs2-core" % fs2Version
val fs2IO = "co.fs2" %% "fs2-io" % fs2Version
val fs2ReactiveStreams = "co.fs2" %% "fs2-reactive-streams" % fs2Version
val fs2Scodec = "co.fs2" %% "fs2-scodec" % fs2Version

// Library

val logbackVersion = "1.2.11"
val akkaVersion = "2.6.19"

val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion
val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion

// Test

val catsEffectTestingSpecsVersion = "1.4.0"
val mUnitCatsEffectVersion = "1.0.7"
val weaverCatsVersion = "0.7.11"

val catsEffectTestkit = "org.typelevel" %% "cats-effect-testkit" % catsEffectVersion
val catsEffectTestingSpecs = "org.typelevel" %% "cats-effect-testing-specs2" % catsEffectTestingSpecsVersion
val mUnitCatsEffect = "org.typelevel" %% "munit-cats-effect-3" % mUnitCatsEffectVersion
val weaverCats = "com.disneystreaming" %% "weaver-cats" % weaverCatsVersion

// Scala 2

val scalaAsyncVersion = "1.0.1"
val scalaReflectVersion = "2.13.8"

val scalaAsync = "org.scala-lang.modules" %% "scala-async" % scalaAsyncVersion
val scalaReflect = "org.scala-lang" % "scala-reflect" % scalaReflectVersion
