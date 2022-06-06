package com.peknight.demo.oauth2.protectedresource

import cats.data.NonEmptyList
import cats.effect.syntax.all.*
import cats.effect.{IO, IOApp}
import cats.syntax.traverse.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.server.start
import fs2.io.file.Files
import fs2.text
import org.http4s.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.*
import org.http4s.scalatags.*
import org.http4s.syntax.literals.uri
import org.typelevel.ci.CIString

object ProtectedResourceApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[CIString, AuthScheme] = CanEqual.derived

  val resource = Resource("Protected Resource", "This data has been protected by OAuth 2.0")

  val accessTokenKey = "access_token"

  object OptionalAccessTokenQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String](accessTokenKey)

  // TODO cors
  val app = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ProtectedResourcePage.index)
    case req @ POST -> Root / "resource" :? OptionalAccessTokenQueryParamMatcher(queryAccessToken) =>
      val accessToken = req.headers.get[Authorization] match
        case Some(Authorization(Credentials.Token(AuthScheme.Bearer, token))) => IO(Some(token))
        case _ => req.as[UrlForm].attempt
          .map(_.toOption.flatMap(_.get(accessTokenKey).headOption).orElse(queryAccessToken))
      accessToken.flatMap {
        case Some(token) => Ok()
        case _ => Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "Protected Resource")))
      }
  }.orNotFound

  val path = "/Users/pek/project/github/oauthinaction/oauth-in-action-code/exercises/ch-3-ex-2/database.nosql"

  val rua = Files[IO].readAll(fs2.io.file.Path(path))
    .through(text.utf8.decode)
    .through(text.lines)
    .filter(_.nonEmpty)
    .map(json => io.circe.parser.decode[OAuthTokenRecord](json))
    .evalMap(IO.println)
    .compile.drain


  val run =
    for
      _ <- start[IO](port"8002")(app)
      resp <- app(POST(
        uri"/resource"
      ))
      _ <- IO.println(resp)
      _ <- rua
      _ <- IO.never
    yield ()


