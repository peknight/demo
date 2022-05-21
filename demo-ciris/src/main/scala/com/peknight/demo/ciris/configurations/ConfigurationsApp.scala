package com.peknight.demo.ciris.configurations

import cats.effect.{IO, IOApp}
import cats.syntax.applicativeError.*
import cats.syntax.option.*
import cats.syntax.parallel.*
import ciris.*

import scala.concurrent.duration.*

object ConfigurationsApp extends IOApp.Simple:

  val port: ConfigValue[Effect, Int] = env("API_PORT").or(prop("api.port")).as[Int]
  // port: ConfigValue[IO, Int]
  port.covary[IO]

  final case class ApiConfig(port: Int, timeout: Option[Duration])

  val timeout: ConfigValue[Effect, Option[Duration]] = env("API_TIMEOUT").as[Duration].option

  // Applicative 的方式，可以整合错误
  val apiConfig1: ConfigValue[Effect, ApiConfig] = (port, timeout).parMapN(ApiConfig.apply)
  // Monad 的方式 不能整合错误
  val apiConfig2 =
    for
      p <- port
      t <- timeout
    yield ApiConfig(p, t)

  // 组合配置都有值 则不会使用默认值
  val defaultApiConfig1 = (env("API_PORT").as[Int], env("API_TIMEOUT").as[Duration].option)
    .parMapN(ApiConfig.apply).default(ApiConfig(3000, 20.seconds.some))

  // 组合配置部分有值，则失败
  val defaultApiConfig2 =
    (
      env("API_PORT_NOT_EXISTS").as[Int],
      env("API_TIMEOUT").as[Duration].option
    ).parMapN(ApiConfig.apply).default(ApiConfig(3000, 20.seconds.some))

  // 组合配置都没有值，则使用默认值
  val defaultApiConfig3 =
    (
      env("API_PORT_NOT_EXISTS").as[Int],
      env("API_TIMEOUT_NOT_EXISTS").as[Duration].option
    ).parMapN(ApiConfig.apply).default(ApiConfig(3000, 20.seconds.some))

  // fallback中的default会覆盖前面的default，这里返回3000
  val defaultPort = env("API_PORT_NOT_EXISTS").as[Int].default(9000)
    .or(prop("api.port.not.exists").as[Int].default(3000))

  // `a.default(b)` equivalent to `a.or(default(b))`.
  env("API_PORT").as[Int].or(default(9000))

  // 加载敏感信息可以用secret
  val apiKey: ConfigValue[Effect, Secret[String]] = env("API_KEY").secret

  val run =
    for
      conf1 <- apiConfig1.load[IO]
      _ <- IO.println(conf1)
      conf2 <- apiConfig2.load[IO]
      _ <- IO.println(conf2)
      defaultTimeout <- env("API_TIMEOUT_NOT_EXISTS").as[Duration].default(10.seconds).load[IO]
      _ <- IO.println(defaultTimeout)
      defaultConf1 <- defaultApiConfig1.load[IO]
      _ <- IO.println(defaultConf1)
      _ <- defaultApiConfig2.attempt[IO].flatMap(IO.println)
      defaultConf3 <- defaultApiConfig3.load[IO]
      _ <- IO.println(defaultConf3)
      _ <- defaultPort.load[IO].flatMap(IO.println)
      // echo -n "RacrqvWjuu4KVmnTG9b6xyZMTP7jnX" | sha1sum | head -c 7
      _ <- IO.println(Secret("RacrqvWjuu4KVmnTG9b6xyZMTP7jnX"))
      // 使用redacted错误信息不会包含敏感数据，这里使用不能转换为Duration的配置人为造个错误
      _ <- env("API_KEY").as[Duration].redacted.attempt[IO].flatMap(IO.println)
      _ <- env("API_KEY").as[Duration].attempt[IO].flatMap(IO.println)
      _ <- env("MAX_RETRIES").as[PosInt].load[IO].flatMap(IO.println)
    yield ()
