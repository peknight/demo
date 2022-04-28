ThisBuild / version := "0.1-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.2"

ThisBuild / organization := "com.peknight"

Docker / packageName := "pek/demo"
Docker / maintainer := "peknight <JKpeknight@gmail.com>"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:strictEquality",
    // "-Ywarn-value-discard",
  ),
)

lazy val demo = (project in file("."))
  .aggregate(
    demoScala,
    demoFpInScala,
    demoCats,
    demoCatsEffect,
    demoFs2,
    demoCirce,
    demoMonocle,
    demoSpire,
    demoAkka,
    demoJs.jvm,
    demoJs.js,
    demoJs2.jvm,
    demoJs2.js,
    demoAsync,
    demoRx,
  )
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)
  .settings(
    name := "demo",
  )

lazy val demoScala = (project in file("demo-scala"))
  .settings(commonSettings)
  .settings(
    name := "demo-scala",
    libraryDependencies ++= Seq(
      scalaTest % Test,
    ),
  )

lazy val demoFpInScala = (project in file("demo-fp"))
  .settings(commonSettings)
  .settings(
    name := "demo-fp",
    libraryDependencies ++= Seq(
      scalacheck,
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
      pekCommonCore,
      fs2Core,
      fs2IO,
      fs2ReactiveStreams,
      fs2Scodec,
    ),
  )

lazy val demoCirce = (project in file("demo-circe"))
  .settings(commonSettings)
  .settings(
    name := "demo-circe",
    libraryDependencies ++= Seq(
      circeCore,
      circeGeneric,
      circeParser,
    ),
  )

lazy val demoMonocle = (project in file("demo-monocle"))
  .settings(commonSettings)
  .settings(
    name := "demo-monocle",
    libraryDependencies ++= Seq(
      monocleCore,
      monocleMacro,
      alleyCatsCore,
    ),
  )

lazy val demoSpire = (project in file("demo-spire"))
  .settings(commonSettings)
  .settings(
    name := "demo-spire",
    libraryDependencies ++= Seq(
      spire,
    ),
  )

lazy val demoAkka = (project in file("demo-akka"))
  .settings(commonSettings)
  .settings(
    name := "demo-akka",
    libraryDependencies ++= Seq(
      akkaActorTyped,
      logbackClassic,
    ),
  )

lazy val demoJs = (crossProject(JSPlatform, JVMPlatform) in file("demo-js"))
  //  .enablePlugins(ScalaJSPlugin) crossProject下不设置
  .settings(commonSettings)
  .settings(
    name := "demo-js",
    scalacOptions ++= Seq(
    ),
    libraryDependencies ++= Seq(
      "com.peknight" %%% "common-core" % pekCommonVersion,
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "cats-effect" % catsEffectVersion,
      "co.fs2" %%% "fs2-core" % fs2Version,
      "org.typelevel" %%% "spire" % spireVersion,
      "com.lihaoyi" %%% "utest" % uTestVersion % "test",
    ),
  )
  .jvmSettings(
    // Add JVM-specific settings here
    libraryDependencies ++= Seq(
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
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
    ),
  )

lazy val demoJs2 = (crossProject(JSPlatform, JVMPlatform) in file("demo-js2"))
  //  .enablePlugins(ScalaJSPlugin) crossProject下看起来不需要设置
  .settings(commonSettings)
  .settings(
    name := "demo-js2",
    scalaVersion := scala2Version,
    scalacOptions ++= Seq(
      "-Ymacro-annotations",
      "-Xasync",
    ),
    scalacOptions --= Seq(
      "-language:strictEquality",
    ),
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % catsVersion,
      "com.lihaoyi" %%% "scalarx" % scalaRxVersion,
      "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
      "com.lihaoyi" %%% "upickle" % uPickleVersion,
      "com.lihaoyi" %%% "utest" % uTestVersion,
      "com.lihaoyi" %%% "autowire" % autowireVersion,
    ),
  )
  .jvmSettings(
    // Add JVM-specific settings here
    libraryDependencies ++= Seq(
      akkaActor,
      akkaStream,
      akkaHttp,
      bootstrap,
    ),
  )
  .jsSettings(
    // Add JS-specific settings here
    // This is an application with a main method
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.2.0",
      scalaAsync,
    ),
  )

lazy val demoAsync = (project in file("demo-async"))
  .settings(commonSettings)
  .settings(
    name := "demo-async",
    scalaVersion := scala2Version,
    scalacOptions ++= Seq(
      "-Ymacro-annotations",
      "-Xasync",
    ),
    scalacOptions --= Seq(
      "-language:strictEquality",
    ),
    libraryDependencies ++= Seq(
      scalaAsync,
      scalaReflect % Provided,
    ),
  )

lazy val demoRx = (project in file("demo-rx"))
  .settings(commonSettings)
  .settings(
    name := "demo-rx",
    scalaVersion := scala2Version,
    scalacOptions ++= Seq(
      "-Ymacro-annotations",
      "-Xasync",
    ),
    scalacOptions --= Seq(
      "-language:strictEquality",
    ),
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalarx" % scalaRxVersion,
      "com.lihaoyi" %%% "utest" % uTestVersion,
    ),
  )

// Peknight

val pekCommonVersion = "0.1-SNAPSHOT"

val pekCommonCore = "com.peknight" %% "common-core" % pekCommonVersion

// Scala

val scalaTestVersion = "3.2.11"

val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion

// Functional

val catsVersion = "2.7.0"
val catsEffectVersion = "3.3.11"
val fs2Version = "3.2.7"
val circeVersion = "0.14.1"
val monocleVersion = "3.1.0"
val spireVersion = "0.18.0-M3"

val catsCore = "org.typelevel" %% "cats-core" % catsVersion
val alleyCatsCore = "org.typelevel" %% "alleycats-core" % catsVersion
val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectVersion withSources() withJavadoc()
val fs2Core = "co.fs2" %% "fs2-core" % fs2Version
val fs2IO = "co.fs2" %% "fs2-io" % fs2Version
val fs2ReactiveStreams = "co.fs2" %% "fs2-reactive-streams" % fs2Version
val fs2Scodec = "co.fs2" %% "fs2-scodec" % fs2Version
val circeCore = "io.circe" %% "circe-core" % circeVersion
val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
val circeParser = "io.circe" %% "circe-parser" % circeVersion
val monocleCore = "dev.optics" %% "monocle-core" % monocleVersion
val monocleMacro = "dev.optics" %% "monocle-macro" % monocleVersion
val spire = "org.typelevel" %% "spire" % spireVersion

// Library

val logbackVersion = "1.2.11"
val akkaVersion = "2.6.19"
val akkaHttpVersion = "10.2.9"
val apacheCommonsMathVersion = "3.6.1"
val bootstrapVersion = "5.1.3"

val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion
val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
val apacheCommonsMath = "org.apache.commons" % "commons-math3" % apacheCommonsMathVersion
val bootstrap = "org.webjars" % "bootstrap" % bootstrapVersion

// Test

val scalacheckVersion = "1.15.4"
val catsEffectTestingSpecsVersion = "1.4.0"
val mUnitCatsEffectVersion = "1.0.7"
val weaverCatsVersion = "0.7.11"

val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckVersion
val catsEffectTestkit = "org.typelevel" %% "cats-effect-testkit" % catsEffectVersion
val catsEffectTestingSpecs = "org.typelevel" %% "cats-effect-testing-specs2" % catsEffectTestingSpecsVersion
val mUnitCatsEffect = "org.typelevel" %% "munit-cats-effect-3" % mUnitCatsEffectVersion
val weaverCats = "com.disneystreaming" %% "weaver-cats" % weaverCatsVersion

// Scala 2

val scala2Version = "2.13.8"
val scalaAsyncVersion = "1.0.1"
val scalaReflectVersion = "2.13.8"

val scalaAsync = "org.scala-lang.modules" %% "scala-async" % scalaAsyncVersion
val scalaReflect = "org.scala-lang" % "scala-reflect" % scalaReflectVersion

// Scala JS

val scalaJsDomVersion = "2.1.0"
val scalaRxVersion = "0.4.3"
val scalaTagsVersion = "0.9.4"
val uPickleVersion = "1.4.0"
val uTestVersion = "0.7.11"
val autowireVersion = "0.3.3"
