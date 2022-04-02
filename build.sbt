ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

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
  .aggregate(demoCore, demoMath, demoFpInScala, demoCats, demoCatsEffect, demoMonocle, demoJson, demoAkka, demoRx,
    demoAsync, demoApp, demoScala3, demoJs.jvm, demoJs.js)
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
      pekCommonTest % Test,
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

lazy val demoRx = (project in file("demo-rx"))
  .dependsOn(demoCore)
  .settings(commonSettings)
  .settings(
    name := "demo-rx",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalarx" % "0.4.3",
      "com.lihaoyi" %%% "utest" % "0.7.4",
    ),
  )

lazy val demoAsync = (project in file("demo-async"))
  .dependsOn(demoCore)
  .settings(commonSettings)
  .settings(
    name := "demo-async",
    scalacOptions ++= Seq(
      "-Xasync",
    ),
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-async" % "1.0.0",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
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
    scalaVersion := "3.1.1"
  )

lazy val demoJs = (crossProject(JSPlatform, JVMPlatform) in file("demo-js"))
//  .enablePlugins(ScalaJSPlugin) crossProject下看起来不需要设置
  .settings(commonSettings)
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
val pekCommonMath = "com.peknight" %% "common-math" % pekCommonVersion
val pekCommonFp = "com.peknight" %% "common-fp" % pekCommonVersion
val pekCommonTest = "com.peknight" %% "common-test" % pekCommonVersion
val pekCommonJson = "com.peknight" %% "common-json" % pekCommonVersion
val pekCommonAkka = "com.peknight" %% "common-akka" % pekCommonVersion
