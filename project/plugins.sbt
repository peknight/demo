addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.15")
// for demo-js
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.13.0")
// for demo-js cross build
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.2.0")
// for demo-js client-server
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
// for demo-http4s deployment 需要有一个main方法，然后执行sbt assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.1")
// for demo-fs2-grpc
addSbtPlugin("org.typelevel" % "sbt-fs2-grpc" % "2.5.10")
// for demo-js
libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"