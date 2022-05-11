package com.peknight.demo.http4s.cors

import cats.effect.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.headers.Origin
import org.http4s.implicits.*
import org.http4s.server.middleware.*

import scala.concurrent.duration.*

/**
 * Cross origin resource sharing
 */
object CorsApp extends IOApp.Simple:
  val service = HttpRoutes.of[IO] {
    case _ => Ok()
  }

  val request = Request[IO](Method.GET, uri"/")

  val corsService = CORS.policy.withAllowOriginAll(service)

  val corsRequest = request.putHeaders("Origin" -> "https://somewhere.com")

  val googleGet = Request[IO](Method.GET, uri"/", headers = Headers("Origin" -> "https://google.com"))

  val yahooPut = Request[IO](Method.PUT, uri"/", headers = Headers("Origin" -> "https://yahoo.com"))

  val duckPost = Request[IO](Method.POST, uri"/", headers = Headers("Origin" -> "https://duckduckgo.com"))

  val corsMethodSvc = CORS.policy
    .withAllowOriginAll
    .withAllowMethodsIn(Set(Method.GET, Method.POST))
    .withAllowCredentials(false)
    .withMaxAge(1.day)
    .apply(service)

  val corsOriginSvc = CORS.policy
    .withAllowOriginHost(Set(
      Origin.Host(Uri.Scheme.https, Uri.RegName("yahoo.com"), None),
      Origin.Host(Uri.Scheme.https, Uri.RegName("duckduckgo.com"), None)
    ))
    .withAllowCredentials(false)
    .withMaxAge(1.day)
    .apply(service)

  val run =
    for
      rootResponse1 <- service.orNotFound(request)
      _ <- IO.println(rootResponse1)
      rootResponse2 <- corsService.orNotFound(request)
      _ <- IO.println(rootResponse2)
      corsResponse <- corsService.orNotFound(corsRequest)
      _ <- IO.println(corsResponse)
      googleGetResp <- corsMethodSvc.orNotFound(googleGet)
      _ <- IO.println(googleGetResp)
      yahooPutResp <- corsMethodSvc.orNotFound(yahooPut)
      _ <- IO.println(yahooPutResp)
      duckPostResp <- corsMethodSvc.orNotFound(duckPost)
      _ <- IO.println(duckPostResp)
      googleGetOriginResp <- corsOriginSvc.orNotFound(googleGet)
      _ <- IO.println(googleGetOriginResp)
      yahooPutOriginResp <- corsOriginSvc.orNotFound(yahooPut)
      _ <- IO.println(yahooPutOriginResp)
      duckPostOriginResp <- corsOriginSvc.orNotFound(duckPost)
      _ <- IO.println(duckPostOriginResp)
    yield ()

