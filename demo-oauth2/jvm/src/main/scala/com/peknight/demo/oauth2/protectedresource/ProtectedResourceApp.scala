package com.peknight.demo.oauth2.protectedresource

import cats.effect.{IO, IOApp}
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.server.start
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*

object ProtectedResourceApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val resource = Resource("Protected Resource", "This data has been protected by OAuth 2.0")

  // TODO cors
  val app = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ProtectedResourcePage.index)
  }.orNotFound

  val run =
    for
      _ <- start[IO](port"8002")(app)
      _ <- IO.never
    yield ()


