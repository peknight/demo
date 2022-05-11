package com.peknight.demo.http4s.hsts

import cats.effect.{IO, IOApp}
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.http4s.implicits.*
import org.http4s.server.middleware.*

import scala.concurrent.duration.*

/**
 * HTTP Strict Transport Security
 */
object HstsApp extends IOApp.Simple:

  val service = HttpRoutes.of[IO] {
    case _ => Ok("ok")
  }

  val request = Request[IO](Method.GET, uri"/")

  val hstsService = HSTS(service)

  val hstsHeader = `Strict-Transport-Security`.unsafeFromDuration(30.days, includeSubDomains = true, preload = true)

  val hstsServiceCustom = HSTS(service, hstsHeader)

  def run =
    for
      responseOk <- service.orNotFound(request)
      _ <- IO.println(responseOk.headers)
      responseHSTS <- hstsService.orNotFound(request)
      _ <- IO.println(responseHSTS.headers)
      responseCustom <- hstsServiceCustom.orNotFound(request)
      _ <- IO.println(responseCustom.headers)
    yield ()

