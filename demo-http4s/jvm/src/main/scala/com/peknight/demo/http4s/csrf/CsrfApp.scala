package com.peknight.demo.http4s.csrf

import cats.effect.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.server.middleware.*

object CsrfApp extends IOApp.Simple:

  val service = HttpRoutes.of[IO] { case _ => Ok() }

  val request = Request[IO](Method.GET, uri"/")

  val cookieName = "csrf-token"

  val defaultOriginCheck: Request[IO] => Boolean =
    CSRF.defaultOriginCheck[IO](_, "localhost", Uri.Scheme.http, None)

  val dummyRequest: Request[IO] =
    Request[IO](method = Method.GET).putHeaders("Origin" -> "http://localhost")

  val run =
    for
      resp <- service.orNotFound(request)
      _ <- IO.println(resp)
      key <- CSRF.generateSigningKey[IO]()
      csrfBuilder = CSRF[IO,IO](key, defaultOriginCheck)
      csrf = csrfBuilder
        .withCookieName(cookieName)
        .withCookieDomain(Some("localhost"))
        .withCookiePath(Some("/"))
        .build
      csrfResp <- csrf.validate()(service.orNotFound)(dummyRequest)
      _ <- IO.println(csrfResp.headers)
      cookie = csrfResp.cookies.head
      dummyPostRequest = Request[IO](method = Method.POST)
        .putHeaders("Origin" -> "http://localhost", "X-Csrf-Token" -> cookie.content)
        .addCookie(RequestCookie(cookie.name,cookie.content))
      validateResp <- csrf.validate()(service.orNotFound)(dummyPostRequest)
      _ <- IO.println(validateResp.headers)
    yield ()
