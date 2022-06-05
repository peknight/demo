package com.peknight.demo.oauth2.protectedresource

import cats.effect.{IO, IOApp}
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.server.start
import org.http4s.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.Authorization
import org.http4s.scalatags.*
import org.http4s.syntax.literals.uri
import org.typelevel.ci.CIString

object ProtectedResourceApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[CIString, AuthScheme] = CanEqual.derived

  val resource = Resource("Protected Resource", "This data has been protected by OAuth 2.0")

  // TODO cors
  val app = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ProtectedResourcePage.index)
    case req @ POST -> Root / "resource" =>
      req.headers.get[Authorization]
        .flatMap {
          case Authorization(Credentials.Token(AuthScheme.Bearer, token)) => Some(token)
          case _ => None
        }
//        .orElse(req.as[UrlForm].map(_.get("access_token").headOption))
      Ok()
  }.orNotFound

  val run =
    for
      _ <- start[IO](port"8002")(app)
      resp <- app(POST(
        UrlForm(
          "access_token" -> "rua",
        ),
        uri"http://localhost:8002/resource"
      ))
      _ <- IO.println(resp)
      _ <- IO.never
    yield ()


