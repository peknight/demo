package com.peknight.demo.http4s.service

import cats.effect.*
import org.http4s.*
import org.http4s.dsl.io.*

object HelloWorldRoutes:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val helloWorldRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "Hello" / name => Ok(s"Hello, $name.")
  }

