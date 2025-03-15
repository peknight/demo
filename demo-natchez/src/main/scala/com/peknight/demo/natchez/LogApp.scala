package com.peknight.demo.natchez

import cats.effect.{IO, IOApp}
import natchez.log.Log
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import com.comcast.ip4s.*

object LogApp extends IOApp.Simple:
  val run: IO[Unit] =
    for
      log <- Slf4jLogger.fromName[IO]("example-logger")
      given Logger[IO] = log
      ep = Log.entryPoint[IO]("example-service")
      _ <- EmberServerBuilder.default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(EntryPointsApp.continuedRoutes(ep).orNotFound)
        .build.use(_ => IO.never)
    yield
      ()
end LogApp
