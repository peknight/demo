addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.8.1")
// for demo-js
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.7.0")
// for demo-js cross build
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.1.0")
// for demo-js client-server
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
// for demo-js
libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
