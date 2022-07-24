package com.peknight.demo.oauth2.client

import cats.data.NonEmptyList
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import com.comcast.ip4s.port
import com.peknight.demo.oauth2.*
import com.peknight.demo.oauth2.domain.*
import io.circe.Json
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.{Authorization, Location}
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

  val authServer: AuthServerInfo = AuthServerInfo()
  val client: ClientInfo = ClientInfo(
    "oauth-client-1",
    "oauth-client-secret-1",
    Set("foo"),
    NonEmptyList.one(uri"http://localhost:8000/callback")
  )
  val protectedResource = uri"http://localhost:8002/resource"

  def authorize(stateR: Ref[IO, Option[String]], random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    for
      state <- randomString[IO](random, 32)
      _ <- stateR.set(Some(state))
      authorizeUrl = authServer.authorizationEndpoint.withQueryParams(AuthorizeParam(client.id,
        client.redirectUris.head, client.scope, ResponseType.Code, Some(state)))
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
        "grant_type" -> GrantType.AuthorizationCode.value,
        "code" -> code,
        "redirect_uri" -> client.redirectUris.head.toString
      ),
      authServer.tokenEndpoint,
      Headers(Authorization(BasicCredentials(Uri.encode(client.id), Uri.encode(client.secret))))
    )

  def updateOAuthTokenCache(oauthTokenCacheR: Ref[IO, OAuthTokenCache], oauthToken: OAuthToken)
                           (using Logger[IO]): IO[OAuthTokenCache] =
    for
      oauthTokenCache <- oauthTokenCacheR.updateAndGet(origin => OAuthTokenCache(
        accessToken = Some(oauthToken.accessToken),
        refreshToken = oauthToken.refreshToken.orElse(origin.refreshToken),
        scope = oauthToken.scope.orElse(origin.scope)
      ))
      _ <- info"Got access token: ${oauthToken.accessToken}"
      _ <- oauthToken.refreshToken.fold(IO.unit)(refresh => info"Got refresh_token: $refresh")
      _ <- info"Got scope: ${oauthToken.scope}"
    yield oauthTokenCache

  def callbackWithCode(oauthTokenCacheR: Ref[IO, OAuthTokenCache], code: String)(using Logger[IO]): IO[Response[IO]] =
    for
      _ <- info"Requesting access token for code $code"
      resp <- EmberClientBuilder.default[IO].build.use { httpClient =>
        httpClient.run(tokenRequest(code)).use { tokenResponse =>
          val statusCode = tokenResponse.status.code
          if statusCode >= 200 && statusCode < 300 then
            for
              oauthToken <- tokenResponse.as[OAuthToken]
              oauthTokenCache <- updateOAuthTokenCache(oauthTokenCacheR, oauthToken)
              okResp <- Ok(ClientPage.index(oauthTokenCache))
            yield okResp
          else Ok(ClientPage.error(s"Unable to fetch access token, server response: $statusCode"))
        }
      }
    yield resp

  def resourceRequest(accessToken: String): Request[IO] =
    POST(protectedResource, Headers(Authorization(Credentials.Token(AuthScheme.Bearer, accessToken))))

  def fetchResource(oauthTokenCacheR: Ref[IO, OAuthTokenCache], accessToken: String)(using Logger[IO]): IO[Response[IO]] =
    info"Making request with access token $accessToken" *>
      EmberClientBuilder.default[IO].build.use { httpClient =>
        httpClient.run(resourceRequest(accessToken)).use { resourceResponse =>
          val statusCode = resourceResponse.status.code
          if statusCode >= 200 && statusCode < 300 then
            resourceResponse.as[Json].flatMap(data => Ok(ClientPage.data(data)))
          else
            oauthTokenCacheR.updateAndGet(_.copy(accessToken = None))
              .flatMap(oauthTokenCache => refreshAccessToken(oauthTokenCacheR, oauthTokenCache, s"$statusCode"))
        }
      }

  def refreshTokenRequest(refreshToken: String): Request[IO] =
    POST(
      UrlForm(
        "grant_type" -> "refresh_token",
        "refresh_token" -> refreshToken
      ),
      authServer.tokenEndpoint,
      Headers(Authorization(BasicCredentials(Uri.encode(client.id), Uri.encode(client.secret))))
    )

  def refreshAccessToken(oauthTokenCacheR: Ref[IO, OAuthTokenCache], oauthTokenCache: OAuthTokenCache, error: String)
                        (using Logger[IO]): IO[Response[IO]] =
    oauthTokenCache.refreshToken.fold(Ok(ClientPage.error(error)))(refreshToken =>
      EmberClientBuilder.default[IO].build.use { httpClient =>
        httpClient.run(refreshTokenRequest(refreshToken)).use { refreshTokenResponse =>
          val statusCode = refreshTokenResponse.status.code
          if statusCode >= 200 && statusCode < 300 then
            for
              oauthToken <- refreshTokenResponse.as[OAuthToken]
              _ <- updateOAuthTokenCache(oauthTokenCacheR, oauthToken)
              resp <- Found(Location(uri"/fetch_resource"))
            yield resp
          else
            for
              _ <- info"No refresh token, asking the user to get a new access token"
              _ <- oauthTokenCacheR.update(_.copy(refreshToken = None))
              resp <- Ok(ClientPage.error("Unable to refresh token."))
            yield resp
        }
      }
    )

  def app(oauthTokenCacheR: Ref[IO, OAuthTokenCache], stateR: Ref[IO, Option[String]], random: Random[IO])
         (using Logger[IO]): HttpApp[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root => oauthTokenCacheR.get.flatMap(oauthTokenCache => Ok(ClientPage.index(oauthTokenCache)))
      case GET -> Root / "authorize" => authorize(stateR, random)
      case GET -> Root / "callback" :? CodeQueryParamMatcher(code) +& StateQueryParamMatcher(state) =>
        stateR.get.flatMap(originState =>
          if !originState.contains(state) then callbackStateNotMatch(originState, state)
          else info"State value matches: expected $state got $state" *> callbackWithCode(oauthTokenCacheR, code)
        )
      case GET -> Root / "callback" :? ErrorQueryParamMatcher(error) => Ok(ClientPage.error(error))
      case GET -> Root / "fetch_resource" =>
        oauthTokenCacheR.get.flatMap(oauthTokenCache => oauthTokenCache.accessToken
          .fold(refreshAccessToken(oauthTokenCacheR, oauthTokenCache, "Missing access token."))(tok =>
            fetchResource(oauthTokenCacheR, tok)
          )
        )
    }.orNotFound

  //noinspection HttpUrlsUsage
  val run: IO[Unit] =
    for
      oauthTokenCacheR <- Ref.of[IO, OAuthTokenCache](OAuthTokenCache(None, Some("j2r3oj32r23rmasd98uhjrk2o3i"), None))
      stateR <- Ref.of[IO, Option[String]](None)
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8000"
      _ <- start[IO](serverPort)(app(oauthTokenCacheR, stateR, random))
      _ <- info"OAuth Client is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()
