# Scala.js

### Prerequisites

[Download & install Node.js](https://nodejs.org/en/download/)

### Add the Scala.js sbt plugin to the build

Adding the Scala.js sbt plugin is one-liner in `project/plugins.sbt`

```
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.10.0")
```

### Source maps in Node.js

To get your stack traces resolved on Node.js, you will have to install the `source-map-support` package.

```
npm install source-map-support
```

### Generate JavaScript

To generate JavaScript using sbt, use the `fastLinkJS` task:

```
> fastLinkJS
[info] Fast optimizing (...)/scalajs-tutorial/target/scala-2.13/scala-js-tutorial-fastopt
[success] (...)
```

### Supporting the DOM

To make the DOM available, add the following to your `project/plugins.sbt`:
```
libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
```

Initialize a private `package.json`:
```
npm init private
```

Install jsdom:
```
npm install jsdom
```

### Full Optimization

To run full optimizations, simply use the `fullLinkJS` task:

```
> fullLinkJS
[info] Full optimizing (...)/scalajs-tutorial/target/scala-2.13/scala-js-tutorial-opt
[inof] Closure: 0 error(s), 0 warning(s)
[success] (...)
```

If you want to `run` and `test` the full-optimized version from sbt, 
you need to change the stage using the following sbt settings:

```
set scalaJSStage in Global := FullOptState
```

(by default, the stage is `FastOptStage`)