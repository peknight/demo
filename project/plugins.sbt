addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.10.0")
// for demo-js
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.16.0")
// for demo-js cross build
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")
// for demo-js client-server
addSbtPlugin("io.spray" % "sbt-revolver" % "0.10.0")
// for demo-http4s deployment 需要有一个main方法，然后执行sbt assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.2.0")
// for demo-fs2-grpc
addSbtPlugin("org.typelevel" % "sbt-fs2-grpc" % "2.7.16")
// for demo-js
libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"