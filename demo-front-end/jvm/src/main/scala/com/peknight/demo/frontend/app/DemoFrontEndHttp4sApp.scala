package com.peknight.demo.frontend.app

import cats.effect.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import ciris.*
import com.comcast.ip4s.*
import fs2.io.file
import fs2.io.net.Network
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.http4s.server.middleware.Logger as MiddlewareLogger
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

trait DemoFrontEndHttp4sApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  def routes(using Logger[IO]): HttpRoutes[IO]

  private[this] val storePasswordConfig: ConfigValue[Effect, Secret[String]] =
    env("STORE_PASSWORD").default("123456").secret
  private[this] val keyPasswordConfig: ConfigValue[Effect, Secret[String]] =
    env("KEY_PASSWORD").default("123456").secret

  private[this] def start[F[_]: Async](httpApp: HttpApp[F]): F[(Server, F[Unit])] =
    for
      storePassword <- storePasswordConfig.load[F]
      keyPassword <- keyPasswordConfig.load[F]
      tlsContext <- Network[F].tlsContext.fromKeyStoreFile(
        file.Path("demo-security/keystore/letsencrypt.keystore").toNioPath,
        storePassword.value.toCharArray, keyPassword.value.toCharArray)
      res <- EmberServerBuilder.default[F]
        .withHostOption(None)
        .withPort(port"8080")
        .withTLS(tlsContext)
        .withHttpApp(MiddlewareLogger.httpApp(true, false)(httpApp))
        .build.allocated
    yield res

  val run: IO[Unit] =
    for
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      _ <- start[IO](routes.orNotFound)
      _ <- IO.never
    yield ()
