package com.peknight.demo.ciris.quickexample

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import ciris.*
import ciris.refined.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto.*
import eu.timepit.refined.cats.*
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.string.NonEmptyString

import scala.concurrent.duration.*
import scala.language.implicitConversions
import scala.util.Try

/**
 * 配置环境变量
 */
object QuickExampleApp extends IOApp.Simple:

  enum AppEnvironment derives CanEqual:
    case Local
    case Testing
    case Production

  import AppEnvironment.*

  type ApiKey = String Refined MatchesRegex["[a-zA-Z0-9]{25,40}"]

  type DatabasePassword = String Refined MinSize[30]

  final case class ApiConfig(port: UserPortNumber, key: Secret[ApiKey], timeout: Option[FiniteDuration])

  final case class DatabaseConfig(username: NonEmptyString, password: Secret[DatabasePassword])

  final case class Config(appName: NonEmptyString, environment: AppEnvironment, api: ApiConfig, database: DatabaseConfig)

  given [T, P]: Conversion[T, T Refined P] = Refined.unsafeApply

  def apiConfig(environment: AppEnvironment): ConfigValue[Effect, ApiConfig] =
    (
      env("API_PORT").or(prop("api.port")).as[UserPortNumber].option,
      env("API_KEY").as[ApiKey].secret
    ).parMapN { (port, key) => ApiConfig(
      port = port getOrElse 9000,
      key = key,
      timeout = environment match
        case Local | Testing => None
        case Production => Some(10.seconds)
    )}

  val databaseConfig: ConfigValue[Effect, DatabaseConfig] =
    (
      env("DATABASE_USERNAME").as[NonEmptyString].default("username"),
      env("DATABASE_PASSWORD").as[DatabasePassword].secret
    ).parMapN(DatabaseConfig.apply)

  given ConfigDecoder[String, AppEnvironment] = ConfigDecoder.instance((key, value) =>
    Try(AppEnvironment.valueOf(value)).toEither.left.map(e => ConfigError(e.getMessage))
  )

  val config: ConfigValue[Effect, Config] =
    env("API_ENV").as[AppEnvironment].flatMap { environment =>
      (
        apiConfig(environment),
        databaseConfig
      ).parMapN { (api, database) => Config(
        appName = "my-api",
        environment = environment,
        api = api,
        database = database
      )}
    }

  val run =
    for
      conf <- config.load[IO]
      _ <- IO.println(conf)
    yield ()

