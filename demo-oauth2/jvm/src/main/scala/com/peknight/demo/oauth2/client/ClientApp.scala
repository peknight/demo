package com.peknight.demo.oauth2.client

import cats.data.NonEmptyList
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.traverse.*
import com.comcast.ip4s.port
import com.peknight.demo.oauth2.server.start
import io.circe.Json
import io.circe.generic.auto.*
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
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

object ClientApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  object CodeQueryParamMatcher extends QueryParamDecoderMatcher[String]("code")
  object StateQueryParamMatcher extends QueryParamDecoderMatcher[String]("state")
  object ErrorQueryParamMatcher extends QueryParamDecoderMatcher[String]("error")

  val authServer = AuthServerInfo(uri"http://localhost:9001/authorize", uri"http://localhost:9001/token")
  val client = ClientInfo("oauth-client-1", "oauth-client-secret-1", NonEmptyList(uri"http://localhost:8000/callback", Nil))
  val protectedResource = uri"http://localhost:9002/resource"

  def authorize(stateR: Ref[IO, Option[String]], random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    for
      stateChars <- List.fill(32)(random.nextAlphaNumeric).sequence
      state = stateChars.mkString
      _ <- stateR.set(Some(state))
      authorizeUrl = authServer.authorizationEndpoint
        .withQueryParams(Map("response_type" -> "code", "client_id" -> client.id, "state" -> state))
        .withQueryParam("redirect_uri", client.redirectUris.head)
      _ <- info"redirect: $authorizeUrl"
      resp <- Found(Location(authorizeUrl))
    yield resp

  def callbackStateNotMatch(originState: Option[String], state: String)(using Logger[IO]): IO[Response[IO]] =
    for
      _ <- warn"State DOES NOT MATCH: expected ${originState.getOrElse("None")} got $state"
      stateResp <- Ok(ClientPage.error("State value did not match"))
    yield stateResp

  def tokenRequest(code: String): Request[IO] =
    POST(
      UrlForm(
        "grant_type" -> "authorization_code",
        "code" -> code,
        "redirect_uri" -> client.redirectUris.head.toString
      ),
      authServer.tokenEndpoint,
      Headers(Authorization(BasicCredentials(Uri.encode(client.id), Uri.encode(client.secret))))
    )

  def callbackWithCode(accessTokenR: Ref[IO, Option[String]], code: String)(using Logger[IO]): IO[Response[IO]] =
    for
      _ <- info"Requesting access token for code $code"
      resp <- EmberClientBuilder.default[IO].build.use { httpClient =>
        httpClient.run(tokenRequest(code)).use { tokenResponse =>
          val statusCode = tokenResponse.status.code
          if statusCode >= 200 && statusCode < 300 then
            for
              oauthToken <- tokenResponse.as[OAuthToken]
              _ <- info"Got access token: ${oauthToken.accessToken}"
              accessTokenOption <- accessTokenR.updateAndGet(_ => Some(oauthToken.accessToken))
              okResp <- Ok(ClientPage.index(accessTokenOption))
            yield okResp
          else Ok(ClientPage.error(s"Unable to fetch access token, server response: $statusCode"))
        }
      }
    yield resp

  def resourceRequest(accessToken: String): Request[IO] =
    POST(protectedResource, Headers(Authorization(Credentials.Token(AuthScheme.Bearer, accessToken))))

  def fetchResource(accessTokenR: Ref[IO, Option[String]], accessToken: String)(using Logger[IO]): IO[Response[IO]] =
    info"Making request with access token $accessToken" *>
      EmberClientBuilder.default[IO].build.use { httpClient =>
        httpClient.run(resourceRequest(accessToken)).use { resourceResponse =>
          val statusCode = resourceResponse.status.code
          if statusCode >= 200 && statusCode < 300 then
            resourceResponse.as[Json].flatMap(data => Ok(ClientPage.data(data)))
          else accessTokenR.getAndSet(None).flatMap(_ => Ok(ClientPage.error(s"$statusCode")))
        }
      }

  def app(accessTokenR: Ref[IO, Option[String]], stateR: Ref[IO, Option[String]], random: Random[IO])(using Logger[IO]) =
    HttpRoutes.of[IO] {
      case GET -> Root => Ok(ClientPage.index(None))
      case GET -> Root / "authorize" => authorize(stateR, random)
      case GET -> Root / "callback" :? CodeQueryParamMatcher(code) +& StateQueryParamMatcher(state) =>
        stateR.get.flatMap(originState =>
          if !originState.contains(state) then callbackStateNotMatch(originState, state)
          else callbackWithCode(accessTokenR, code)
        )
      case GET -> Root / "callback" :? ErrorQueryParamMatcher(error) => Ok(ClientPage.error(error))
      case GET -> Root / "fetch_resource" =>
        accessTokenR.get.flatMap(_.fold(Ok(ClientPage.error("Missing access token.")))(tok =>
          fetchResource(accessTokenR, tok)
        ))
    }.orNotFound

  val run =
    for
      accessTokenR <- Ref.of[IO, Option[String]](None)
      stateR <- Ref.of[IO, Option[String]](None)
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8000"
      _ <- start[IO](serverPort)(app(accessTokenR, stateR, random))
      _ <- info"OAuth Client is listening at http://localhost:$serverPort"
      _ <- IO.never
    yield ()
