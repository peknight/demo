package com.peknight.demo.http4s.httpclient

import cats.effect.*
import com.peknight.demo.http4s.runLogEmberServer
import org.http4s.*
import org.http4s.client.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.*
import org.http4s.implicits.*

import scala.concurrent.duration.*

object HttpClientApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val app = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello, $name.")
  }.orNotFound

  val shutdown = runLogEmberServer[IO](app)

  EmberClientBuilder.default[IO].build.use { client => IO.unit }

  val run =
    for
      _ <- runLogEmberServer[IO](app)
    yield ()
