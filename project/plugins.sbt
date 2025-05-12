// https://github.com/sbt/sbt-native-packager
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.11.1")
// for demo-js https://www.scala-js.org/
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.19.0")
// for demo-js cross build https://github.com/portable-scala/sbt-crossproject
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")
// for demo-js client-server https://github.com/spray/sbt-revolver
addSbtPlugin("io.spray" % "sbt-revolver" % "0.10.0")
// for demo-http4s deployment 需要有一个main方法，然后执行sbt assembly https://github.com/sbt/sbt-assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.3.1")
// for demo-fs2-grpc https://github.com/typelevel/fs2-grpc
addSbtPlugin("org.typelevel" % "sbt-fs2-grpc" % "2.8.0")
// for demo-js https://github.com/scala-js/scala-js-env-jsdom-nodejs
libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"