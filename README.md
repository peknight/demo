# Pek Demo

主要为学习Scala与Cats全家桶过程中的练习代码。  

### Log

###### 2023.10.31
关注下[Laminar](https://laminar.dev/), [Github](https://github.com/raquo/Laminar)
###### 2023.10.16
对比了一下，好像nebula比neo4j更适合我，学下nebula
###### 2023.10.09
学习neo4j与neotypes
###### 2023.02.03
阳康+过年断了一段时间，研究下scalatest与scalacheck等测试库
###### 2022.12.01
过了一遍Vue.js与React.js
###### 2022.11.18
B站Pink老师JavaScript课程学完，准备研究研究模块化与Vue
###### 2022.10.21
B站Pink老师H5C3课程完结撒花，准备继续补补JavaScript与Scala.JS
###### 2022.10.10
跟B站pink老师学学前端
###### 2022.10.02
搞定OAuth2实战，准备补补前端
###### 2022.09.21
边学边练完OAuth2前9章，搞定了https访问的问题。
###### 2022.09.05
学习OAuth2的过程中有些样板代码需要Shapeless相关能力解决，刚学习完一波Shapeless，继续搞OAuth2。
###### 2022.08.12
当前主要在学习OAuth2。

### Modules

#### demo-scala

* Scala3学习。
* 参考: 书[《Scala编程第五版》](https://www.artima.com/shop/programming_in_scala_5ed)、元组[Tuples bring generic programming to Scala 3](https://www.scala-lang.org/2021/02/26/tuples-bring-generic-programming-to-scala-3.html)、宏[Scala3 Macros](https://docs.scala-lang.org/scala3/guides/macros/macros.html)、[Macros](https://docs.scala-lang.org/scala3/reference/metaprogramming/macros.html)、[Magic Mirror Scala3](https://blog.philipp-martini.de/blog/magic-mirror-scala3/)
* 进度: 100%，一些特别基础的代码没跟着写。

#### demo-shapeless

* shapless3泛化编程，涉及一些元编程内容。原版shapeless支持到Scala2，而在Scala3中shapeless被直接集成到了标准库。
* 参考：火箭书[The Type Astronaut's Guide to Shapeless Book](https://underscore.io/books/shapeless-guide/)、[Type Class Derivation](https://dotty.epfl.ch/docs/reference/contextual/derivation.html)
* 进度: 100%

#### demo-fp

* Scala函数式编程，函数式理论知识丰富，构建自己的FP库，可对照Cats全家桶学习。
* 参考: 小红书[Functional Programming in Scala](https://www.manning.com/books/functional-programming-in-scala)（推荐看英文版，中文版翻译质量不敢恭维）、[代码](https://github.com/fpinscala/fpinscala)
* 进度: 100%，代码已迁移Scala3。

#### demo-cats

* Cats全家桶基础，推荐用于函数式编程入门。
* 参考: 猫书[Scala with Cats](https://underscore.io/books/scala-with-cats/)，这本真是强烈推荐，学了猫书再学小红书无敌。
* 进度: 100%，代码已迁移Scala3。

#### demo-cats-effect

* Cats函数式IO基础，可对照《Functional Programming in Scala》第13章进行学习
* 参考: [Cats Effect官方文档](https://typelevel.org/cats-effect/docs/getting-started)
* 进度: 100%（仅学习了3.x版本），代码已迁移Scala3。

#### demo-cats-parse

* Cats函数式解析库，可对照《Functional Programming in Scala》第9章进行学习
* 参考: [Cats Parse GitHub](https://github.com/typelevel/cats-parse)
* 进度: 100%，这个库居然没提供一个regex方法搞正则解析，还把类搞成了sealed没法自行扩展，不爽。

#### demo-fs2

* Cats函数式流库，可对照《Functional Programming in Scala》第15章进行学习
* 参考: [Fs2官方文档](https://fs2.io/)
* 进度: 100%（仅学习了3.x版本），代码已迁移Scala3。

#### demo-fs2-grpc

* 基于Fs2的函数式grpc库
* 参考: [GitHub fs2-grpc](https://github.com/typelevel/fs2-grpc)与[GitHub fs2-grpc-example](https://github.com/fiadliel/fs2-grpc-example)
* 进度: 100%，参考的代码只有一个Demo，后续实践中再深入学习。

#### demo-circe

* Cats全家桶的JSON库
* 参考: [Circe官方文档](https://circe.github.io/circe/)
* 进度: 100%，已迁移Scala3，文档中介绍的部分模块暂不支持Scala3（比如Shapless不支持Scala3，但被Scala3集成了，而这些模块还是不能直接用），这部分未保留。

#### demo-monocle

* Cats全家桶的Optics库，感觉处理一些比较复杂的模型会很有用。
* 参考: [Monocle官方文档](https://www.optics.dev/Monocle/)
* 进度: 100%，已迁移Scala3。

#### demo-log4cats

* Cats全家桶的日志库，底层还是要用Slf4j、Logback这套的，只是一层函数式的封装。
* 参考: [Log4Cats官方文档](https://typelevel.org/log4cats/)
* 进度: 100%

#### demo-ciris

* Cats全家桶的配置读取库，因为pureconfig暂不支持scala3，先学了ciris。
* 参考: [Ciris官方文档](https://cir.is/) 
* 进度: 100%(照抄文档)，但是没做深入研究，后面实践用到再说。

#### demo-cats-stm

* Cats全家桶的STM内存事务库。
* 参考: [Cats STM官方文档](https://timwspence.github.io/cats-stm/)
* 进度: 100%

#### demo-spire

* Cats全家桶的数学库，还没出正式版。
* 参考: [Spire官方文档](https://typelevel.org/spire/)
* 进度: 100%

#### demo-squants

* 单位换算相关库。
* 参考: [Squants官方文档](https://www.squants.com/)
* 进度: 100%，一些实验内容没搞

#### demo-http4s

* Cats全家桶，基于Fs2的HTTP库。挺好用的，就是还没出正式版。
* 参考: [Http4s官方文档](https://http4s.org/v1/docs/quickstart.html)
* 进度: 100%，有些涉及前端安全等领域的中间件内容还没深入研究，后面结合前端知识学习补充。

#### demo-doobie

* Cats全家桶，基于Fs2的数据库操作库。写SQL的纯函数式操作。就是还没出正式版。
* 参考: [Doobie官方文档](https://tpolecat.github.io/doobie/)
* 进度: 100%

#### demo-redis4cats

* Cats全家桶的Redis操作库。
* 参考: [Redis4Cats官方文档](https://redis4cats.profunktor.dev/)
* 进度: 100%

#### demo-neo4j

* neo4j与neotypes学习，社区版只能单机总觉得不长久，已转去学习nebula。
* 参考: [neotypes](https://neotypes.github.io/neotypes/)
* 进度: 100%

#### demo-nebula

* nebula学习，国产开源分布式图数据库
* 参考: [NebulaGraph Database 手册](https://docs.nebula-graph.com.cn/3.6.0/), [zio-nebula](https://github.com/nebula-contrib/zio-nebula)(暂时没找到CE的封装，先参考下ZIO的封装)
* 进度: 1%

#### demo-zio

* ZIO 一个和Cats Effect类似的另一个很火爆的Effect库
* 参考: [ZIO官方文档](https://zio.dev/)
* 进度: 10%，学完了Overview部分，后续Reference、Ecosystem等部分尚未提起兴趣学

#### demo-akka

* 基于Actor模型的重型框架
* 参考: [Akka官方文档](https://doc.akka.io/docs/akka/current/typed/actors.html)
* 进度: 由于近期沉迷函数式编辑，akka学习被无限延后，进度1%。

#### demo-scalatest

* Scala测试库
* 参考: [ScalaTest User Guide](https://www.scalatest.org/user_guide)
* 进度：0%

#### demo-scalacheck

* 基于Prop的测试库
* 参考: [ScalaCheck User Guide](https://github.com/typelevel/scalacheck/blob/main/doc/UserGuide.md)
* 进度：90%

#### demo-js

* Scala JS，支持Scala3，Cats全家桶也基本都支持，虽然有些文档比较老了，但是也很实用。
* 参考: ScalaJS官方文档[Basic Tutorial](https://www.scala-js.org/doc/tutorial/basic/)、Lihaiyi的[Hands-on Scala.js](https://www.lihaoyi.com/hands-on-scala-js/)、Lihaoyi的[uPickle](https://com-lihaoyi.github.io/upickle/)、Lihaoyi的[ScalaTags](https://com-lihaoyi.github.io/scalatags/)、[ScalaCss](https://japgolly.github.io/scalacss/book/)
* 进度: 100%，学习scalajs时正好刚学完Fs2这些Cats全家桶知识，把练习的代码用Cats全家桶改写了一波巩固一下。另外Lihaoyi的Autowire库不支持Scala3，只能用Http4s自己手动完成了相关练习。

#### demo-front-end

* 前端知识学习，用scalatags与scalacss写前端页面，目标是能看懂JavaScript/TypeScript代码以及基于Vue.js、React.js的项目代码（但我自己只写Scala，不写js/ts）。
* 参考: [B站黑马前端H5+CSS3](https://www.bilibili.com/video/BV14J4114768?p=1)
* 进度: H5C3与JavaScript课程完成，Vue3没找到现成好用的Scala的facade层，自己写又有点麻烦。学习了diff算法的源码，准备再学学Scala的FRP函数式响应编程（比如lihaoyi的scala.rx但它仅支持到Scala2，看看能不能自己搞一套在Scala能直接好用的。
* 素材: [Pink素材](https://gitee.com/xiaoqiang001)

#### demo-oauth2

* OAuth2学习，OAuth2目前主要基于HTTPS实现。
* 参考: 书[《OAuth2实战》](https://www.manning.com/books/oauth-2-in-action)、[代码](https://github.com/oauthinaction/oauth-in-action-code)
* 进度: 100%，原生客户端实现部分跳过，书中代码由node.js实现，我在练习中用Scala重新实现（大量使用http4s，页面使用ScalaTags及ScalaCss构建）。

#### demo-acme4j

* Let's Encrypt证书管理
* 参考: [Acme4j官方文档](https://shredzone.org/maven/acme4j/index.html)
* 进度: 0%，刚提起兴趣，没正式开学。

#### demo-security

* 研究一下编解码、摘要、加解密相关的知识
* 参考: [廖雪峰 Java教程 加密与安全](https://www.liaoxuefeng.com/wiki/1252599548343744/1255943717668160)
* 进度: 100%，用fs2支持了AES的ECB模式与CBC模式的流式加解密。

#### demo-playground

增加上述模块全部的依赖项，用于自己随时测试一些代码特性，验证自己的部分想法。

#### demo-frp

* Functional reactive programming，响应式编程，这类库都不咋维护了或者更新较慢，先放一放。
* 参考: [Deprecating the Observer Pattern](http://infoscience.epfl.ch/record/176887/files/DeprecatingObservers2012.pdf), [ingoem/scala-react](https://github.com/ingoem/scala-react), [lihaoyi/scala.rx](https://github.com/lihaoyi/scala.rx)
* 进度: 0%

#### demo-monix

* Cats全家桶，响应式编程库，等它支持Cats Effect 3.x我就开始学。不过看他们意思迁移到CE3.x是个复杂的过程，不知道要等多久，先占个坑。（不过这也太久了吧，一年半不更新了）
* 参考: [Monix官方文档](https://monix.io/)
* 进度: 0%
