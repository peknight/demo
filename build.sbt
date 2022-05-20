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
    demoLog4Cats.jvm,
    demoLog4Cats.js,
    demoSpire,
    demoHttp4s.jvm,
    demoHttp4s.js,
    demoDoobie,
    demoAkka,
    demoJs.jvm,
    demoJs.js,
    demoOAuth2.jvm,
    demoOAuth2.js,
    demoPlayground.jvm,
    demoPlayground.js,
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
      scalaCheck,
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

lazy val demoLog4Cats = (crossProject(JSPlatform, JVMPlatform) in file("demo-log4cats"))
  .settings(commonSettings)
  .settings(
    name := "demo-log4cats",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "log4cats-core" % log4CatsVersion,
    ),
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      log4CatsSlf4j,
      logbackClassic % Runtime,
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

lazy val demoHttp4s = (crossProject(JSPlatform, JVMPlatform) in file("demo-http4s"))
  .settings(commonSettings)
  .settings(
    name := "demo-http4s",
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-dsl" % http4sVersion,
      "org.http4s" %%% "http4s-ember-server" % http4sVersion,
      "org.http4s" %%% "http4s-ember-client" % http4sVersion,
      "org.http4s" %%% "http4s-server" % http4sVersion,
      "org.http4s" %%% "http4s-client" % http4sVersion,
      "org.http4s" %%% "http4s-circe" % http4sVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
    ),
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      http4sScalaTags,
      http4sDropwizardMetrics,
      http4sPrometheusMetrics,
      http4sJdkHttpClient,
      jQuery,
      logbackClassic % Runtime,
    ),
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-dom" % http4sVersion,
    ),
  )

lazy val demoDoobie = (project in file("demo-doobie"))
  .settings(commonSettings)
  .settings(
    name := "demo-doobie",
    libraryDependencies ++= Seq(
      doobieCore,
      doobieH2,
      doobieHikari,
      doobiePostgres,
      doobiePostgresCirce,
      circeCore,
      circeGeneric,
      circeParser,
      logbackClassic % Runtime,
      doobieScalaTest % Test,
      h2 % Test,
    )
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
    libraryDependencies ++= Seq(
      "com.peknight" %%% "common-core" % pekCommonVersion,
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "cats-effect" % catsEffectVersion,
      "co.fs2" %%% "fs2-core" % fs2Version,
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "org.typelevel" %%% "spire" % spireVersion,
      "org.http4s" %%% "http4s-dsl" % http4sVersion,
      "org.http4s" %%% "http4s-ember-server" % http4sVersion,
      "org.http4s" %%% "http4s-ember-client" % http4sVersion,
      "org.http4s" %%% "http4s-circe" % http4sVersion,
      "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
      "com.lihaoyi" %%% "upickle" % uPickleVersion,
      "com.github.japgolly.scalacss" %% "core" % scalaCssVersion,
      "com.lihaoyi" %%% "utest" % uTestVersion % Test,
    ),
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      http4sScalaTags,
      logbackClassic % Runtime,
    ),
  )
  .jsSettings(
    // This is an application with a main method
    scalaJSUseMainModuleInitializer := true,
    Compile / mainClass := Some("com.peknight.demo.js.tutorial.webapp.TutorialApp"),
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
      "org.http4s" %%% "http4s-dom" % http4sVersion,
    ),
  )

lazy val demoOAuth2 = (crossProject(JSPlatform, JVMPlatform) in file("demo-oauth2"))
  .settings(commonSettings)
  .settings(
    name := "demo-oauth2",
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-dsl" % http4sVersion,
      "org.http4s" %%% "http4s-ember-server" % http4sVersion,
      "org.http4s" %%% "http4s-ember-client" % http4sVersion,
      "org.http4s" %%% "http4s-circe" % http4sVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
      "com.github.japgolly.scalacss" %% "core" % scalaCssVersion,
    ),
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      http4sScalaTags,
      logbackClassic % Runtime,
    ),
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-dom" % http4sVersion,
    ),
  )

lazy val demoPlayground = (crossProject(JSPlatform, JVMPlatform) in file("demo-playground"))
  .settings(commonSettings)
  .settings(
    name := "demo-playground",
    libraryDependencies ++= Seq(
      "com.peknight" %%% "common-core" % pekCommonVersion,
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "alleycats-core" % catsVersion,
      "org.typelevel" %%% "cats-effect" % catsEffectVersion,
      "co.fs2" %%% "fs2-core" % fs2Version,
      "co.fs2" %%% "fs2-io" % fs2Version,
      "co.fs2" %%% "fs2-scodec" % fs2Version,
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "dev.optics" %%% "monocle-core" % monocleVersion,
      "dev.optics" %%% "monocle-macro" % monocleVersion,
      "org.typelevel" %%% "spire" % spireVersion,
      "org.http4s" %%% "http4s-dsl" % http4sVersion,
      "org.http4s" %%% "http4s-ember-server" % http4sVersion,
      "org.http4s" %%% "http4s-ember-client" % http4sVersion,
      "org.http4s" %%% "http4s-server" % http4sVersion,
      "org.http4s" %%% "http4s-client" % http4sVersion,
      "org.http4s" %%% "http4s-circe" % http4sVersion,
      "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
      "com.lihaoyi" %%% "upickle" % uPickleVersion,
      "com.github.japgolly.scalacss" %% "core" % scalaCssVersion,
      "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % Test,
      "org.scalatest" %%% "scalatest" % scalaTestVersion % Test,
      "org.typelevel" %%% "cats-effect-testkit" % catsEffectVersion % Test,
      "org.typelevel" %%% "cats-effect-testing-specs2" % catsEffectTestingSpecsVersion % Test,
      "org.typelevel" %%% "munit-cats-effect-3" % mUnitCatsEffectVersion % Test,
      "com.disneystreaming" %%% "weaver-cats" % weaverCatsVersion % Test,
      "com.lihaoyi" %%% "utest" % uTestVersion % Test,
    ),
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      fs2ReactiveStreams,
      akkaActorTyped,
      http4sScalaTags,
      http4sDropwizardMetrics,
      http4sPrometheusMetrics,
      http4sJdkHttpClient,
      doobieCore,
      doobieH2,
      doobieHikari,
      doobiePostgres,
      doobiePostgresCirce,
      logbackClassic,
      jQuery,
      doobieScalaTest % Test,
      h2 % Test,
    ),
  )
  .jsSettings(
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
      "org.http4s" %%% "http4s-dom" % http4sVersion,
    ),
  )

// Peknight

val pekCommonVersion = "0.1-SNAPSHOT"

val pekCommonCore = "com.peknight" %% "common-core" % pekCommonVersion

// Scala

val scalaCheckVersion = "1.16.0"
val scalaTestVersion = "3.2.11"

val scalaCheck = "org.scalacheck" %% "scalacheck" % scalaCheckVersion
val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion

// Functional

val catsVersion = "2.7.0"
val catsEffectVersion = "3.3.11"
val fs2Version = "3.2.7"
val circeVersion = "0.14.1"
val monocleVersion = "3.1.0"
val log4CatsVersion = "2.3.1"
val spireVersion = "0.18.0-M3"
val http4sVersion = "1.0.0-M32"
val http4sJdkHttpClientVersion = "1.0.0-M1"
val doobieVersion = "1.0.0-RC2"

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
val log4CatsSlf4j = "org.typelevel" %% "log4cats-slf4j" % log4CatsVersion
val spire = "org.typelevel" %% "spire" % spireVersion
val http4sScalaTags = "org.http4s" %% "http4s-scalatags" % http4sVersion
val http4sDropwizardMetrics = "org.http4s" %% "http4s-dropwizard-metrics" % http4sVersion
val http4sPrometheusMetrics = "org.http4s" %% "http4s-prometheus-metrics" % http4sVersion
val http4sJdkHttpClient = "org.http4s" %% "http4s-jdk-http-client" % http4sJdkHttpClientVersion
val doobieCore = "org.tpolecat" %% "doobie-core" % doobieVersion
val doobieH2 = "org.tpolecat" %% "doobie-h2" % doobieVersion
val doobieHikari = "org.tpolecat" %% "doobie-hikari" % doobieVersion
val doobiePostgres = "org.tpolecat" %% "doobie-postgres" % doobieVersion
val doobiePostgresCirce = "org.tpolecat" %% "doobie-postgres-circe" % doobieVersion
val doobieScalaTest = "org.tpolecat" %% "doobie-scalatest" % doobieVersion


// Library

val logbackVersion = "1.2.11"
val akkaVersion = "2.6.19"
val apacheCommonsMathVersion = "3.6.1"
val h2Version = "2.1.212"
val jQueryVersion = "3.6.0"
val postgisJdbcVersion = "2021.1.0"

val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion
val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
val apacheCommonsMath = "org.apache.commons" % "commons-math3" % apacheCommonsMathVersion
val h2 = "com.h2database" % "h2" % h2Version
val jQuery = "org.webjars" % "jquery" % jQueryVersion
val postgisJdbc = "net.postgis" % "postgis-jdbc" % postgisJdbcVersion

// Test

val catsEffectTestingSpecsVersion = "1.4.0"
val mUnitCatsEffectVersion = "1.0.7"
val weaverCatsVersion = "0.7.11"

val catsEffectTestkit = "org.typelevel" %% "cats-effect-testkit" % catsEffectVersion
val catsEffectTestingSpecs = "org.typelevel" %% "cats-effect-testing-specs2" % catsEffectTestingSpecsVersion
val mUnitCatsEffect = "org.typelevel" %% "munit-cats-effect-3" % mUnitCatsEffectVersion
val weaverCats = "com.disneystreaming" %% "weaver-cats" % weaverCatsVersion

// Scala JS

val scalaJsDomVersion = "2.1.0"
val scalaTagsVersion = "0.11.1"
val uPickleVersion = "2.0.0"
val uTestVersion = "0.7.11"
val scalaCssVersion = "1.0.0"
