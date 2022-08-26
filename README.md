# Pek Demo

学习Scala与Cats全家桶过程中的练习代码。  

### Log

* 2022.08.12: 当前主要在学习OAuth2。

### Modules

#### demo-scala

* Scala3学习。
* 参考: 书[《Scala编程第五版》](https://www.artima.com/shop/programming_in_scala_5ed)、元组[Tuples bring generic programming to Scala 3](https://www.scala-lang.org/2021/02/26/tuples-bring-generic-programming-to-scala-3.html)、[Magic Mirror Scala3](https://blog.philipp-martini.de/blog/magic-mirror-scala3/)
* 进度: 100%，一些特别基础的代码没跟着写。

#### demo-shapeless

* shapless3泛化编程，涉及一些元编程内容。原版shapeless支持到Scala2，而在Scala3中shapeless被直接集成到了标准库。
* 参考：火箭书[The Type Astronaut's Guide to Shapeless Book](https://underscore.io/books/shapeless-guide/)、[Type Class Derivation](https://dotty.epfl.ch/docs/reference/contextual/derivation.html)
* 进度: 0%

#### demo-fp

* Scala函数式编程，函数式理论知识丰富，构建自己的FP库，可对照Cats全家桶学习。
* 参考: 小红书[Functional Programming in Scala](https://www.manning.com/books/functional-programming-in-scala)（推荐看英文版，中文版翻译质量不敢恭维）、[代码](https://github.com/fpinscala/fpinscala)
* 进度: 100%，代码已迁移Scala3。

#### demo-cats

* Cats全家桶基础，推荐用于函数式编程入门。
* 参考: 猫书[Scala with Cats](https://underscore.io/books/scala-with-cats/)
* 进度: 100%，代码已迁移Scala3。

#### demo-cats-effect

* Cats函数式IO基础，可对照《Functional Programming in Scala》第13章进行学习
* 参考: [Cats Effect官方文档](https://typelevel.org/cats-effect/docs/getting-started)
* 进度: 100%（仅学习了3.x版本），代码已迁移Scala3。

#### demo-fs2

* Cats函数式流库，可对照《Functional Programming in Scala》第15章进行学习
* 参考: [Fs2官方文档](https://fs2.io/)
* 进度: 100%（仅学习了3.x版本），代码已迁移Scala3。

#### demo-fs2-grpc

* 基于Fs2的函数式grpc库
* 参考: [Github fs2-grpc](https://github.com/typelevel/fs2-grpc)与[Github fs2-grpc-example](https://github.com/fiadliel/fs2-grpc-example)
* 进度: 100%，参考的代码只有一个Demo，后续实践中再深入学习。

#### demo-doobie

* Cats全家桶，基于Fs2的数据库操作库。写SQL的纯函数式操作。就是还没出正式版。
* 参考: [Doobie官方文档](https://tpolecat.github.io/doobie/)
* 进度: 100%

#### demo-circe

* Cats全家桶的JSON库
* 参考: [Circe官方文档](https://circe.github.io/circe/)
* 进度: 100%，已迁移Scala3，文档中介绍的部分模块暂不支持Scala3（比如Shapless不支持Scala3，但被Scala3集成了，而这些模块还是不能直接用），这部分未保留。

#### demo-monocle

* Cats全家桶的Optics库，感觉处理一些比较复杂的模型会很有用。
* 参考: [Monocle官方文档](https://www.optics.dev/Monocle/)
* 进度: 100%，已迁移Scala3。

#### demo-ciris

* Cats全家桶的配置读取库，因为pureconfig暂不支持scala3，先学了ciris。
* 参考: [Ciris官方文档](https://cir.is/) 
* 进度: 100%(照抄文档)，但是没做深入研究，后面实践用到再说。

#### demo-log4cats

* Cats全家桶的日志库，底层还是要用Slf4j、Logback这套的，只是一层函数式的封装。
* 参考: [Log4Cats官方文档](https://typelevel.org/log4cats/)
* 进度: 100%

#### demo-redis4cats

* Cats全家桶的Redis操作库。
* 参考: [Redis4Cats官方文档](https://redis4cats.profunktor.dev/)
* 进度: 100%

#### demo-spire

* Cats全家桶的数学库，还没出正式版。
* 参考: [Spire官方文档](https://typelevel.org/spire/)
* 进度: 100%

#### demo-http4s

* Cats全家桶，基于Fs2的HTTP库。挺好用的，就是还没出正式版。
* 参考: [Http4s官方文档](https://http4s.org/v1/docs/quickstart.html)
* 进度: 100%，有些涉及前端安全等领域的中间件内容还没深入研究，后面结合前端知识学习补充。

#### demo-js

* Scala JS，支持Scala3，Cats全家桶也基本都支持，虽然有些文档比较老了，但是也很实用。
* 参考: ScalaJS官方文档[Basic Tutorial](https://www.scala-js.org/doc/tutorial/basic/)、Lihaiyi的[Hands-on Scala.js](https://www.lihaoyi.com/hands-on-scala-js/)、Lihaoyi的[uPickle](https://com-lihaoyi.github.io/upickle/)、Lihaoyi的[ScalaTags](https://com-lihaoyi.github.io/scalatags/)、[ScalaCss](https://japgolly.github.io/scalacss/book/)
* 进度: 100%，学习scalajs时正好刚学完Fs2这些Cats全家桶知识，把练习的代码用Cats全家桶改写了一波巩固一下。另外Lihaoyi的Autowire库不支持Scala3，只能用Http4s自己手动完成了相关练习。

#### demo-h5

* 前端知识学习
* 参考: [B站黑马前端H5+CSS3](https://www.bilibili.com/video/BV14J4114768?p=1)
* 进度: P302品优购项目，暂停了一年了，前面有些东西忘了，后面再补上吧。

#### demo-oauth2

* OAuth2学习，OAuth2目前主要基于HTTPS实现。
* 参考: 书[《OAuth2实战》](https://www.manning.com/books/oauth-2-in-action)、[代码](https://github.com/oauthinaction/oauth-in-action-code)
* 进度: 正在研究第六章原生客户端实现，书中代码由node.js实现，我在练习中用Scala重新实现（大量使用http4s，页面使用ScalaTags及ScalaCss构建）。

#### demo-akka

* 基于Actor模型的重型框架
* 参考: [Akka官方文档](https://doc.akka.io/docs/akka/current/typed/actors.html)
* 进度: 由于近期沉迷函数式编辑，akka学习被无限延后，进度1%。

#### demo-acme4j

* Let's Encrypt证书管理
* 参考: [Acme4j官方文档](https://shredzone.org/maven/acme4j/index.html)
* 进度: 0%，刚提起兴趣，没正式开学。

#### demo-security

* 研究一下编解码、摘要、加解密相关的知识
* 参考: [廖雪峰 Java教程 加密与安全](https://www.liaoxuefeng.com/wiki/1252599548343744/1255943717668160)
* 进度: 100%，用fs2支持了AES的ECB模式与CBC模式的流式加解密。

#### demo-shapeless2

* Shapeless泛型编程学习，仅scala2，据说scala3集成了shapeless，那就先学scala2的。
* 参考：火箭书[The Type Astronaut's Guide to Shapeless Book](https://underscore.io/books/shapeless-guide/)
* 进度：0%

#### demo-playground

增加上述模块全部的依赖项，用于自己随时测试一些代码特性，验证自己的部分想法。

#### demo-monix

* Cats全家桶，响应式编程库，等它支持Cats Effect 3.x我就开始学。不过看他们意思迁移到CE3.x是个复杂的过程，不知道要等多久，先占个坑。
* 参考: [Monix官方文档](https://monix.io/)
* 进度: 0%

#### demo-squants

* 单位换算相关库，有点感兴趣，但是学习优先级不高，先占个坑。
* 参考: [Squants官方文档](https://www.squants.com/)
* 进度: 0%
