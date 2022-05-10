package com.peknight.demo.http4s.cors

import cats.effect.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.server.middleware.*

object CorsApp extends IOApp.Simple:
  val service = HttpRoutes.of[IO] {
    case _ => Ok()
  }

  val request = Request[IO](Method.GET, uri"/")

  val corsService = CORS.policy.withAllowOriginAll(service)

  val run =
    for
      rootResponse <- service.orNotFound(request)
      _ <- IO.println(rootResponse)
      corsResponse <- corsService.orNotFound(request)
      _ <- IO.println(corsResponse)
    yield ()

