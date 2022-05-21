package com.peknight.demo.oauth2.client

import cats.data.NonEmptyList
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import com.comcast.ip4s.port
import com.peknight.demo.oauth2.server.start
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.{Authorization, Location}
import org.http4s.internal.CharPredicate.AlphaNum
import org.http4s.scalatags.scalatagsEncoder
import org.http4s.syntax.literals.uri

object ClientApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  object CodeQueryParamMatcher extends QueryParamDecoderMatcher[String]("code")

  val client = ClientInfo("oauth-client-1", "oauth-client-secret-1", NonEmptyList(uri"http://localhost:8000/callback", Nil))

  val authServer = AuthServerInfo(uri"http://localhost:9001/authorize", uri"http://localhost:9001/token")

  def app(accessTokenR: Ref[IO, Option[String]]) = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ClientPage.index(None))
    case GET -> Root / "authorize" => Found(Location(authServer.authorizationEndpoint
      .withQueryParams(Map("response_type" -> "code", "client_id" -> client.id))
      .withQueryParam("redirect_uri", client.redirectUris.head)
    ))
    case GET -> Root / "callback" :? CodeQueryParamMatcher(code) =>
      for
        oauthToken <- EmberClientBuilder.default[IO].build.use { httpClient =>
          httpClient.expect[OAuthToken](POST(
            UrlForm(
              "grant_type" -> "authorization_code",
              "code" -> code,
              "redirect_uri" -> client.redirectUris.head.toString
            ),
            authServer.tokenEndpoint,
            Headers(Authorization(BasicCredentials(Uri.encode(client.id), Uri.encode(client.secret))))
          ))
        }
        accessTokenOption <- accessTokenR.updateAndGet(_ => Some(oauthToken.accessToken))
        resp <- Ok(ClientPage.index(accessTokenOption))
      yield resp
  }.orNotFound

  val run =
    for
      accessTokenR <- Ref.of[IO, Option[String]](None)
      _ <- start[IO](port"8000")(app(accessTokenR))
    yield ()
