package com.peknight.demo.oauth2.app

import cats.effect.{IO, IOApp}
import com.comcast.ip4s.port
import com.peknight.demo.oauth2.page.ClientPage
import fs2.io.file
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

object WebClientApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8010"
      _ <- start[IO](serverPort)(service)
      _ <- info"OAuth Client is listening at https://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

  val service: HttpApp[IO] = HttpRoutes.of[IO] {
      case req @ GET -> Root / path if Set(".js", ".map").exists(path.endsWith) =>
        StaticFile.fromPath(file.Path(s"./demo-oauth2/js/target/scala-3.2.0/demo-oauth2-opt/$path"), Some(req))
          .getOrElseF(NotFound())
      case GET -> _ => Ok(ClientPage.Text.webIndex)
    }.orNotFound

