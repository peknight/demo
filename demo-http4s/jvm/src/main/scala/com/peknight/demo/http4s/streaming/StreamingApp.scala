package com.peknight.demo.http4s.streaming

import cats.effect.*
import fs2.Stream
import org.http4s.*
import org.http4s.dsl.io.*

import scala.concurrent.duration.*

object StreamingApp:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val seconds = Stream.awakeEvery[IO](1.second)

  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "seconds" => Ok(seconds.map(_.toString))
  }

