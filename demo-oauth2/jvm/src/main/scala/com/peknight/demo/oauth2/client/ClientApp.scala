package com.peknight.demo.oauth2.client

import cats.data.NonEmptyList
import cats.effect.std.Random
import cats.effect.{IO, IOApp}
import com.comcast.ip4s.port
import com.peknight.demo.oauth2.server.start
import org.http4s.*
import org.http4s.dsl.io.{->, /, :?, Found, GET, Ok, Path, QueryParamDecoderMatcher, Root, http4sFoundSyntax, http4sOkSyntax}
import org.http4s.headers.Location
import org.http4s.internal.CharPredicate.AlphaNum
import org.http4s.scalatags.scalatagsEncoder
import org.http4s.syntax.literals.uri

object ClientApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  object CodeQueryParamMatcher extends QueryParamDecoderMatcher[String]("code")

  val client = ClientInfo("oauth-client-1", "oauth-client-secret-1", NonEmptyList(uri"http://localhost:8000/callback", Nil))

  val authServer = AuthServerInfo(uri"http://localhost:9001/authorize", uri"http://localhost:9001/token")

  val app = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ClientPage.index(None))
    case GET -> Root / "authorize" => Found(Location(authServer.authorizationEndpoint
      .withQueryParams(Map("response_type" -> "code", "client_id" -> client.id))
      .withQueryParam("redirect_uri", client.redirectUris.head)
    ))
    case GET -> Root / "callback" :? CodeQueryParamMatcher(code) => Ok()
  }.orNotFound

  val run =
    for
      _ <- start[IO](port"8000")(app)
    yield ()
