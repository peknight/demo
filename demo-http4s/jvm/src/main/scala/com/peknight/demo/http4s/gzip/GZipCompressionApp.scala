package com.peknight.demo.http4s.gzip

import cats.effect.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.server.middleware.*

object GZipCompressionApp extends IOApp.Simple:

  val service = HttpRoutes.of[IO] {
    case _ => Ok("I repeat myself when I'm under stress. " * 3)
  }

  val request = Request[IO](Method.GET, uri"/")

  val serviceZip = GZip(service)

  val requestZip = request.putHeaders("Accept-Encoding" -> "gzip")

  val run =
    for
      resp <- service.orNotFound(request)
      body <- resp.as[String]
      _ <- IO.println(body.length)
      respNormal <- serviceZip.orNotFound(request)
      _ <- IO.println(respNormal)
      respZip <- serviceZip.orNotFound(requestZip)
      _ <- IO.println(respZip)
      bodyZip <- respZip.as[String]
      _ <- IO.println(bodyZip)
      _ <- IO.println(bodyZip.length)
    yield ()

