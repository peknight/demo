import org.scalajs.linker.interface.ModuleInitializer

ThisBuild / version := "0.1-SNAPSHOT"

val scala3Version = "3.2.0"
val scala2Version = "2.13.8"

ThisBuild / scalaVersion := scala3Version

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
    "-Xmax-inlines:64"
    // "-Ywarn-value-discard",
  ),
)

lazy val demo = (project in file("."))
  .aggregate(
    demoScala,
    demoShapeless,
    demoFpInScala,
    demoCats,
    demoCatsEffect,
    demoCatsParse,
    demoFs2,
    demoFs2Grpc,
    demoCirce,
    demoMonocle,
    demoLog4Cats,
    demoCiris,
    demoCatsStm,
    demoSpire,
    demoSquants,
    demoHttp4s.jvm,
    demoHttp4s.js,
    demoDoobie,
    demoRedis4Cats,
    demoZio,
    demoAkka,
    demoJs.jvm,
    demoJs.js,
    demoJsModuleA,
    demoJsModuleB,
    demoFrontEnd.jvm,
    demoFrontEnd.js,
    demoFrontEndModuleHeimamm,
    demoOAuth2.jvm,
    demoOAuth2.js,
    demoAcme4j,
    demoSecurity,
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

lazy val demoShapeless = (project in file("demo-shapeless"))
  .settings(commonSettings)
  .settings(
    name := "demo-shapeless",
    crossScalaVersions := List(scala3Version, scala2Version),
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) => Seq(
          shapeless,
          catsCore,
          scalaCheck
        )
        case Some((2, _)) => Seq(
          shapeless2,
          catsCore,
          scalaCheck
        )
        case _ => Seq()
      }
    },
    scalacOptions --= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, _)) => Seq(
          "-language:strictEquality",
          "-Xmax-inlines:64"
        )
        case _ => Seq()
      }
    }
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

lazy val demoCatsParse = (project in file("demo-cats-parse"))
  .settings(commonSettings)
  .settings(
    name := "demo-cats-parse",
    libraryDependencies ++= Seq(
      catsParse,
      jawnAst,
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

lazy val demoFs2Grpc = (project in file("demo-fs2-grpc"))
  .aggregate(demoFs2GrpcProtobuf, demoFs2GrpcClient, demoFs2GrpcServer)
  .settings(commonSettings)
  .settings(
    name := "demo-fs2-grpc",
  )

lazy val demoFs2GrpcProtobuf = (project in file("demo-fs2-grpc/protobuf"))
  .enablePlugins(Fs2Grpc)
  .settings(commonSettings)
  .settings(
    name := "demo-fs2-grpc-protobuf",
  )

lazy val demoFs2GrpcClient = (project in file("demo-fs2-grpc/client"))
  .dependsOn(demoFs2GrpcProtobuf)
  .settings(commonSettings)
  .settings(
    name := "demo-fs2-grpc-client",
    libraryDependencies ++= Seq(
      grpcNettyShaded,
    )
  )

lazy val demoFs2GrpcServer = (project in file("demo-fs2-grpc/server"))
  .dependsOn(demoFs2GrpcProtobuf)
  .settings(commonSettings)
  .settings(
    name := "demo-fs2-grpc-server",
    libraryDependencies ++= Seq(
      grpcNettyShaded,
    )
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

lazy val demoLog4Cats = (project in file("demo-log4cats"))
  .settings(commonSettings)
  .settings(
    name := "demo-log4cats",
    libraryDependencies ++= Seq(
      log4CatsSlf4j,
      logbackClassic % Runtime,
      jansi % Runtime,
    ),
  )

lazy val demoCiris = (project in file("demo-ciris"))
  .settings(commonSettings)
  .settings(
    name := "demo-ciris",
    libraryDependencies ++= Seq(
      ciris,
      cirisCirce,
      cirisCirceYaml,
      cirisHttp4s,
      cirisRefined,
      cirisSquants,
      cirisHocon,
      circeGeneric,
      refinedCats,
    ),
  )

lazy val demoCatsStm = (project in file("demo-cats-stm"))
  .settings(commonSettings)
  .settings(
    name := "demo-cats-stm",
    libraryDependencies ++= Seq(
      catsStm,
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

lazy val demoSquants = (project in file("demo-squants"))
  .settings(commonSettings)
  .settings(
    name := "demo-squants",
    libraryDependencies ++= Seq(
      squants,
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
      http4sPrometheusMetrics,
      http4sDropwizardMetrics,
      http4sJdkHttpClient,
      jQuery,
      logbackClassic % Runtime,
      jansi % Runtime,
    ),
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-dom" % http4sDomVersion,
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
      jansi % Runtime,
      doobieScalaTest % Test,
      h2 % Test,
    ),
  )

lazy val demoRedis4Cats = (project in file("demo-redis4cats"))
  .settings(commonSettings)
  .settings(
    name := "demo-redis4cats",
    libraryDependencies ++= Seq(
      redis4CatsEffects,
      redis4CatsStreams,
      redis4CatsLog4Cats,
      circeCore,
      circeGeneric,
      circeParser,
      log4CatsSlf4j,
      logbackClassic % Runtime,
      jansi % Runtime,
    ),
  )

lazy val demoZio = (project in file("demo-zio"))
  .settings(commonSettings)
  .settings(
    name := "demo-zio",
    libraryDependencies ++= Seq(
      zio,
      zioStreams,
    ),
  )

lazy val demoAkka = (project in file("demo-akka"))
  .settings(commonSettings)
  .settings(
    name := "demo-akka",
    libraryDependencies ++= Seq(
      akkaActorTyped,
      logbackClassic % Runtime,
      jansi % Runtime,
    ),
  )

lazy val demoJs = (crossProject(JSPlatform, JVMPlatform) in file("demo-js"))
  //  .enablePlugins(ScalaJSPlugin) crossProject下不设置
  .settings(commonSettings)
  .settings(
    name := "demo-js",
    libraryDependencies ++= Seq(
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
      ciris,
      log4CatsSlf4j,
      logbackClassic % Runtime,
      jansi % Runtime,
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
      "org.http4s" %%% "http4s-dom" % http4sDomVersion,
    ),
  )

lazy val demoJsModuleA = (project in file("demo-js/module/module-a"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    name := "demo-js-module-a",
    // scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
    ),
  )

lazy val demoJsModuleB = (project in file("demo-js/module/module-b"))
   .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    name := "demo-js-module-b",
    Compile / scalaJSModuleInitializers ++= Seq(
      ModuleInitializer.mainMethod("com.peknight.demo.js.module.emitting.AppB", "main")
        .withModuleID("b")
    ),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
    ),
  )

lazy val demoFrontEnd = (crossProject(JSPlatform, JVMPlatform) in file("demo-front-end"))
  .settings(commonSettings)
  .settings(
    name := "demo-front-end",
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-dsl" % http4sVersion,
      "org.http4s" %%% "http4s-ember-server" % http4sVersion,
      "org.http4s" %%% "http4s-ember-client" % http4sVersion,
      "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
      "com.github.japgolly.scalacss" %% "core" % scalaCssVersion,
    ),
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      http4sScalaTags,
      ciris,
      log4CatsSlf4j,
      flexible,
      swiper,
      bootstrap,
      bootstrapIcons,
      logbackClassic % Runtime,
      jansi % Runtime,
    ),
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-dom" % http4sDomVersion,
    ),
  )

lazy val demoFrontEndModuleHeimamm = (project in file("demo-front-end/module/heimamm"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    name := "demo-front-end-module-heimamm",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
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
      "org.typelevel" %%% "cats-parse" % catsParseVersion,
      "org.typelevel" %%% "shapeless3-deriving" % shapelessVersion,
      "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
      "com.github.japgolly.scalacss" %% "core" % scalaCssVersion,
    ),
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      circeFs2,
      http4sScalaTags,
      jwtCirce,
      ciris,
      log4CatsSlf4j,
      logbackClassic % Runtime,
      jansi % Runtime,
    ),
  )
  .jsSettings(
    scalaJSUseMainModuleInitializer := true,
    Compile / mainClass := Some("com.peknight.demo.oauth2.webclient.WebClient"),
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-dom" % http4sDomVersion,
    ),
  )

lazy val demoAcme4j = (project in file("demo-acme4j"))
  .settings(commonSettings)
  .settings(
    name := "demo-acme4j",
    libraryDependencies ++= Seq(
      acme4jClient,
      acme4jUtils,
    ),
  )

lazy val demoSecurity = (project in file("demo-security"))
  .settings(commonSettings)
  .settings(
    name := "demo-security",
    libraryDependencies ++= Seq(
      fs2Core,
      bouncyCastle,
      apacheCommonsCodec,
      jwtCirce,
      scalaJwk,
    ),
  )

lazy val demoPlayground = (crossProject(JSPlatform, JVMPlatform) in file("demo-playground"))
  .settings(commonSettings)
  .settings(
    name := "demo-playground",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "shapeless3-deriving" % shapelessVersion,
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.typelevel" %%% "alleycats-core" % catsVersion,
      "org.typelevel" %%% "cats-effect" % catsEffectVersion,
      "org.typelevel" %%% "cats-parse" % catsParseVersion,
      "co.fs2" %%% "fs2-core" % fs2Version,
      "co.fs2" %%% "fs2-io" % fs2Version,
      "co.fs2" %%% "fs2-scodec" % fs2Version,
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "dev.optics" %%% "monocle-core" % monocleVersion,
      "dev.optics" %%% "monocle-macro" % monocleVersion,
      "io.github.timwspence" %% "cats-stm" % catsStmVersion,
      "org.typelevel" %%% "spire" % spireVersion,
      "org.typelevel" %%% "squants" % squantsVersion,
      "org.http4s" %%% "http4s-dsl" % http4sVersion,
      "org.http4s" %%% "http4s-ember-server" % http4sVersion,
      "org.http4s" %%% "http4s-ember-client" % http4sVersion,
      "org.http4s" %%% "http4s-server" % http4sVersion,
      "org.http4s" %%% "http4s-client" % http4sVersion,
      "org.http4s" %%% "http4s-circe" % http4sVersion,
      "org.typelevel" %%% "jawn-ast" % jawnAstVersion,
      "dev.zio" %%% "zio" % zioVersion,
      "dev.zio" %%% "zio-streams" % zioVersion,
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
      circeFs2,
      fs2ReactiveStreams,
      akkaActorTyped,
      log4CatsSlf4j,
      ciris,
      cirisCirce,
      cirisCirceYaml,
      cirisRefined,
      cirisSquants,
      cirisHocon,
      circeGeneric,
      refinedCats,
      http4sScalaTags,
      http4sPrometheusMetrics,
      http4sDropwizardMetrics,
      http4sJdkHttpClient,
      doobieCore,
      doobieH2,
      doobieHikari,
      doobiePostgres,
      doobiePostgresCirce,
      redis4CatsEffects,
      redis4CatsStreams,
      redis4CatsLog4Cats,
      bouncyCastle,
      jwtCirce,
      scalaJwk,
      acme4jClient,
      acme4jUtils,
      apacheCommonsCodec,
      jQuery,
      logbackClassic % Runtime,
      jansi % Runtime,
      doobieScalaTest % Test,
      h2 % Test,
    ),
  )
  .jsSettings(
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
      "org.http4s" %%% "http4s-dom" % http4sDomVersion,
    ),
  )

// Scala

val scalaCheckVersion = "1.17.0"
val scalaTestVersion = "3.2.12"

val scalaCheck = "org.scalacheck" %% "scalacheck" % scalaCheckVersion
val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion

// Functional

val shapelessVersion = "3.2.0"
val shapeless2Version = "2.3.10"
val catsVersion = "2.8.0"
val catsEffectVersion = "3.3.14"
val catsParseVersion = "0.3.8"
val fs2Version = "3.3.0"
val circeVersion = "0.14.3"
val circeFs2Version = "0.14.0"
val monocleVersion = "3.1.0"
val log4CatsVersion = "2.5.0"
val cirisVersion = "2.4.0"
val cirisHoconVersion = "1.0.1"
val refinedCatsVersion = "0.10.1"
val catsStmVersion = "0.11.0"
val spireVersion = "0.18.0-M3"
val squantsVersion = "1.8.3"
val http4sVersion = "1.0.0-M32"
val http4sDomVersion = "1.0.0-M32"
val http4sDropwizardMetricsVersion = "1.0.0-M32"
val http4sJdkHttpClientVersion = "1.0.0-M1"
val doobieVersion = "1.0.0-RC2"
val redis4CatsVersion = "1.2.0"
val jawnAstVersion = "1.4.0"
val zioVersion = "2.0.2"

val shapeless = "org.typelevel" %% "shapeless3-deriving" % shapelessVersion
val shapeless2 = "com.chuusai" %% "shapeless" % shapeless2Version
val catsCore = "org.typelevel" %% "cats-core" % catsVersion
val alleyCatsCore = "org.typelevel" %% "alleycats-core" % catsVersion
val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectVersion withSources() withJavadoc()
val catsParse = "org.typelevel" %% "cats-parse" % catsParseVersion
val fs2Core = "co.fs2" %% "fs2-core" % fs2Version
val fs2IO = "co.fs2" %% "fs2-io" % fs2Version
val fs2ReactiveStreams = "co.fs2" %% "fs2-reactive-streams" % fs2Version
val fs2Scodec = "co.fs2" %% "fs2-scodec" % fs2Version
val circeCore = "io.circe" %% "circe-core" % circeVersion
val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
val circeParser = "io.circe" %% "circe-parser" % circeVersion
val circeFs2 = "io.circe" %% "circe-fs2" % circeFs2Version
val monocleCore = "dev.optics" %% "monocle-core" % monocleVersion
val monocleMacro = "dev.optics" %% "monocle-macro" % monocleVersion
val log4CatsSlf4j = "org.typelevel" %% "log4cats-slf4j" % log4CatsVersion
val ciris = "is.cir" %% "ciris" % cirisVersion
val cirisCirce = "is.cir" %% "ciris-circe" % cirisVersion
val cirisCirceYaml = "is.cir" %% "ciris-circe-yaml" % cirisVersion
val cirisHttp4s = "is.cir" %% "ciris-http4s" % cirisVersion
val cirisRefined = "is.cir" %% "ciris-refined" % cirisVersion
val cirisSquants = "is.cir" %% "ciris-squants" % cirisVersion
val cirisHocon = "lt.dvim.ciris-hocon" %% "ciris-hocon" % cirisHoconVersion
val refinedCats = "eu.timepit" %% "refined-cats" % refinedCatsVersion
val catsStm = "io.github.timwspence" %% "cats-stm" % catsStmVersion
val spire = "org.typelevel" %% "spire" % spireVersion
val squants = "org.typelevel" %% "squants" % squantsVersion
val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sVersion
val http4sEmberServer = "org.http4s" %% "http4s-ember-server" % http4sVersion
val http4sEmberClient = "org.http4s" %% "http4s-ember-client" % http4sVersion
val http4sCirce = "org.http4s" %% "http4s-circe" % http4sVersion
val http4sScalaTags = "org.http4s" %% "http4s-scalatags" % http4sVersion
val http4sPrometheusMetrics = "org.http4s" %% "http4s-prometheus-metrics" % http4sVersion
val http4sDropwizardMetrics = "org.http4s" %% "http4s-dropwizard-metrics" % http4sDropwizardMetricsVersion
val http4sJdkHttpClient = "org.http4s" %% "http4s-jdk-http-client" % http4sJdkHttpClientVersion
val doobieCore = "org.tpolecat" %% "doobie-core" % doobieVersion
val doobieH2 = "org.tpolecat" %% "doobie-h2" % doobieVersion
val doobieHikari = "org.tpolecat" %% "doobie-hikari" % doobieVersion
val doobiePostgres = "org.tpolecat" %% "doobie-postgres" % doobieVersion
val doobiePostgresCirce = "org.tpolecat" %% "doobie-postgres-circe" % doobieVersion
val doobieScalaTest = "org.tpolecat" %% "doobie-scalatest" % doobieVersion
val redis4CatsEffects = "dev.profunktor" %% "redis4cats-effects" % redis4CatsVersion
val redis4CatsStreams = "dev.profunktor" %% "redis4cats-streams" % redis4CatsVersion
val redis4CatsLog4Cats = "dev.profunktor" %% "redis4cats-log4cats" % redis4CatsVersion
val jawnAst = "org.typelevel" %% "jawn-ast" % jawnAstVersion
val zio = "dev.zio" %% "zio" % zioVersion
val zioStreams = "dev.zio" %% "zio-streams" % zioVersion

// Library

val logbackVersion = "1.4.1"
val jansiVersion = "1.18"
val akkaVersion = "2.6.20"
val apacheCommonsCodecVersion = "1.15"
val h2Version = "2.1.214"
val postgisJdbcVersion = "2021.1.0"
val acme4jVersion = "2.14"
val bouncyCastleVersion = "1.70"
val jwtCirceVersion = "9.1.1"
val scalaJwkVersion = "1.2.24"

val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion
val jansi = "org.fusesource.jansi" % "jansi" % jansiVersion
val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
val apacheCommonsCodec = "commons-codec" % "commons-codec" % apacheCommonsCodecVersion
val h2 = "com.h2database" % "h2" % h2Version
val postgisJdbc = "net.postgis" % "postgis-jdbc" % postgisJdbcVersion
val grpcNettyShaded = "io.grpc" % "grpc-netty-shaded" % scalapb.compiler.Version.grpcJavaVersion
val acme4jClient = "org.shredzone.acme4j" % "acme4j-client" % acme4jVersion
val acme4jUtils = "org.shredzone.acme4j" % "acme4j-utils" % acme4jVersion
val bouncyCastle = "org.bouncycastle" % "bcprov-jdk15on" % bouncyCastleVersion
val jwtCirce = "com.github.jwt-scala" %% "jwt-circe" % jwtCirceVersion
val scalaJwk = "com.chatwork" %% "scala-jwk" % scalaJwkVersion

// Webjars

val jQueryVersion = "3.6.1"
val flexibleVersion = "2.2.1"
val swiperVersion = "8.3.2"
val bootstrapVersion = "5.2.2"
val bootstrapIconsVersion = "1.9.1"

val jQuery = "org.webjars" % "jquery" % jQueryVersion
val flexible = "org.webjars.npm" % "amfe-flexible" % flexibleVersion
val swiper = "org.webjars.npm" % "swiper" % swiperVersion
val bootstrap = "org.webjars.npm" % "bootstrap" % bootstrapVersion
val bootstrapIcons = "org.webjars.npm" % "bootstrap-icons" % bootstrapIconsVersion

// Test

val catsEffectTestingSpecsVersion = "1.4.0"
val mUnitCatsEffectVersion = "1.0.7"
val weaverCatsVersion = "0.8.0"

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
