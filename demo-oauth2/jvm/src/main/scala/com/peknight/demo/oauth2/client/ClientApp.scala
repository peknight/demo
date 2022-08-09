package com.peknight.demo.oauth2.client

import cats.data.{Chain, NonEmptyList}
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.option.*
import com.comcast.ip4s.port
import com.peknight.demo.oauth2.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.domain.WordsResult.*
import io.circe.Json
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.headers.{Authorization, Location, `Content-Type`}
import org.http4s.scalatags.scalatagsEncoder
import org.http4s.syntax.literals.uri
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

object ClientApp extends IOApp.Simple:

  val rsaKey: RsaKey = RsaKey("RS256", "AQAB", "p8eP5gL1H_H9UNzCuQS-vNRVz3NWxZTHYk1tG9VpkfFjWNKG3MFTNZJ1l5g_COMm2_2i_YhQNH8MJ_nQ4exKMXrWJB4tyVZohovUxfw-eLgu1XQ8oYcVYW8ym6Um-BkqwwWL6CXZ70X81YyIMrnsGTyTV6M8gBPun8g2L8KbDbXR1lDfOOWiZ2ss1CRLrmNM-GRp3Gj-ECG7_3Nx9n_s5to2ZtwJ1GS1maGjrSZ9GRAYLrHhndrL_8ie_9DS2T-ML7QNQtNkg2RvLv4f0dpjRYI23djxVtAylYK4oiT_uEMgSkc4dxwKwGuBxSO0g9JOobgfy0--FUHHYtRi0dOFZw", "RSA", "authserver")

  val protectedResource = uri"http://localhost:8002/resource"

  val wordApi = uri"http://localhost:8002/words"

  val produceApi = uri"http://localhost:8002/produce"

  val favoritesApi = uri"http://localhost:8002/favorites"

  //noinspection HttpUrlsUsage
  val run: IO[Unit] =
    for
      oauthTokenCacheR <- Ref.of[IO, OAuthTokenCache](OAuthTokenCache(
        None,
        None, // "j2r3oj32r23rmasd98uhjrk2o3i".some,
        None))
      stateR <- Ref.of[IO, Option[String]](None)
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8000"
      _ <- start[IO](serverPort)(service(oauthTokenCacheR, stateR, random))
      _ <- info"OAuth Client is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given EntityDecoder[IO, WordsData] = jsonOf[IO, WordsData]
  given EntityDecoder[IO, ProduceData] = jsonOf[IO, ProduceData]
  given EntityDecoder[IO, FavoritesData] = jsonOf[IO,FavoritesData]
  given EntityDecoder[IO, UserFavoritesData] = jsonOf[IO, UserFavoritesData]

  object CodeQueryParamMatcher extends QueryParamDecoderMatcher[String]("code")
  object StateQueryParamMatcher extends QueryParamDecoderMatcher[String]("state")
  object ErrorQueryParamMatcher extends QueryParamDecoderMatcher[String]("error")
  object WordQueryParamMatcher extends QueryParamDecoderMatcher[String]("word")

  def service(oauthTokenCacheR: Ref[IO, OAuthTokenCache], stateR: Ref[IO, Option[String]], random: Random[IO])
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
      case GET -> Root / "refresh" => refresh(oauthTokenCacheR)
      case GET -> Root / "fetch_resource" =>
        getOrRefreshAccessToken(oauthTokenCacheR)(token => fetchResource(oauthTokenCacheR, token))
      case GET -> Root / "words" => Ok(ClientPage.words(Seq.empty[String], 0L, NoGet))
      case GET -> Root / "get_words" => getOrRefreshAccessToken(oauthTokenCacheR)(getWords)
      case GET -> Root / "add_word" :? WordQueryParamMatcher(word) =>
        getOrRefreshAccessToken(oauthTokenCacheR)(token => addWords(token, word))
      case GET -> Root / "delete_word" => getOrRefreshAccessToken(oauthTokenCacheR)(deleteWords)
      case GET -> Root / "produce" => getOrRefreshAccessToken(oauthTokenCacheR)(token => produce(token, oauthTokenCacheR))
      case GET -> Root / "favorites" => getOrRefreshAccessToken(oauthTokenCacheR)(favorites)
    }.orNotFound

  def authorize(stateR: Ref[IO, Option[String]], random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    for
      state <- randomString[IO](random, 32)
      _ <- stateR.set(Some(state))
      authorizeUrl = AuthServerInfo.authServer.authorizationEndpoint.withQueryParams(AuthorizeParam(ClientInfo.client.id,
        ClientInfo.client.redirectUris.head, ClientInfo.client.scope, ResponseType.Code, Some(state)))
      _ <- info"redirect: $authorizeUrl"
      resp <- Found(Location(authorizeUrl))
    yield resp

  def callbackStateNotMatch(originState: Option[String], state: String)(using Logger[IO]): IO[Response[IO]] =
    for
      _ <- warn"State DOES NOT MATCH: expected ${originState.getOrElse("None")} got $state"
      stateResp <- Ok(ClientPage.error("State value did not match"))
    yield stateResp

  def callbackWithCode(oauthTokenCacheR: Ref[IO, OAuthTokenCache], code: String)(using Logger[IO]): IO[Response[IO]] =
    for
      _ <- info"Requesting access token for code $code"
      resp <- runHttpRequest(tokenRequest(code)) { response =>
        for
          oauthToken <- response.as[OAuthToken]
          oauthTokenCache <- updateOAuthTokenCache(oauthTokenCacheR, oauthToken)
          resp <- Ok(ClientPage.index(oauthTokenCache))
        yield resp
      } { statusCode =>
        Ok(ClientPage.error(s"Unable to fetch access token, server response: $statusCode"))
      }
    yield resp

  def tokenRequest(code: String): Request[IO] =
    POST(
      UrlForm(
        "grant_type" -> GrantType.AuthorizationCode.value,
        "code" -> code,
        // "client_id" -> ClientInfo.client.id,
        // "client_secret" -> ClientInfo.client.secret,
        "redirect_uri" -> ClientInfo.client.redirectUris.head.toString
      ),
      AuthServerInfo.authServer.tokenEndpoint,
      Headers(Authorization(BasicCredentials(Uri.encode(ClientInfo.client.id), Uri.encode(ClientInfo.client.secret))))
    )

  def refresh(oauthTokenCacheR: Ref[IO, OAuthTokenCache])(using Logger[IO]): IO[Response[IO]] =
    for
      oauthTokenCache <- oauthTokenCacheR.get
      resp <- refreshAccessToken(oauthTokenCacheR, oauthTokenCache, "Missing refresh token.")
    yield resp

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

  def fetchResource(oauthTokenCacheR: Ref[IO, OAuthTokenCache], accessToken: String)(using Logger[IO]): IO[Response[IO]] =
    info"Making request with access token $accessToken" *>
      runHttpRequest(resourceRequest(accessToken)) { response =>
        response.as[Json].flatMap(data => Ok(ClientPage.data(data)))
      } { statusCode =>
        for
          oauthTokenCache <- oauthTokenCacheR.updateAndGet(_.copy(accessToken = None))
          resp <- refreshAccessToken(oauthTokenCacheR, oauthTokenCache, s"$statusCode")
        yield resp
      }

  def resourceRequest(accessToken: String): Request[IO] =
    POST(protectedResource, Headers(Authorization(Credentials.Token(AuthScheme.Bearer, accessToken))))

  def getWords(accessToken: String): IO[Response[IO]] =
    runHttpRequest(GET(wordApi, bearerHeaders(accessToken))) { response =>
      for
        data <- response.as[WordsData]
        resp <- Ok(ClientPage.words(data.words, data.timestamp, Get))
      yield resp
    } { _ =>
      Ok(ClientPage.words(Seq.empty[String], 0L, NoGet))
    }

  def addWords(accessToken: String, word: String): IO[Response[IO]] =
    runHttpRequest(POST(UrlForm("word" -> word), wordApi, bearerHeaders(accessToken))) { _ =>
      Ok(ClientPage.words(Seq.empty[String], 0L, Add))
    } { _ =>
      Ok(ClientPage.words(Seq.empty[String], 0L, NoAdd))
    }

  def deleteWords(accessToken: String): IO[Response[IO]] =
    runHttpRequest(DELETE(wordApi, bearerHeaders(accessToken))) { _ =>
      Ok(ClientPage.words(Seq.empty[String], 0L, Rm))
    } { _ =>
      Ok(ClientPage.words(Seq.empty[String], 0L, NoRm))
    }

  def produce(accessToken: String, oauthTokenCacheR: Ref[IO, OAuthTokenCache]): IO[Response[IO]] =
    runHttpRequest(GET(produceApi, bearerHeaders(accessToken))) { response =>
      for
        data <- response.as[ProduceData]
        cache <- oauthTokenCacheR.get
        resp <- Ok(ClientPage.produce(cache.scope.getOrElse(Set.empty[String]), data))
      yield resp
    } { _ =>
      for
        cache <- oauthTokenCacheR.get
        resp <- Ok(ClientPage.produce(cache.scope.getOrElse(Set.empty[String]), ProduceData.empty))
      yield resp
    }

  def favorites(accessToken: String)(using Logger[IO]): IO[Response[IO]] =
    runHttpRequest(GET(favoritesApi, bearerHeaders(accessToken))) { response =>
      for
        data <- response.as[UserFavoritesData]
        _ <- info"Got data: $data"
        resp <- Ok(ClientPage.favorites(data))
      yield resp
    } { _ =>
      Ok(ClientPage.favorites(UserFavoritesData("", FavoritesData.empty)))
    }

  def refreshAccessToken(oauthTokenCacheR: Ref[IO, OAuthTokenCache], oauthTokenCache: OAuthTokenCache, error: String)
                        (using Logger[IO]): IO[Response[IO]] =
    oauthTokenCache.refreshToken.fold(Ok(ClientPage.error(error)))(refreshToken =>
      runHttpRequest(refreshTokenRequest(refreshToken)) { response =>
        for
          oauthToken <- response.as[OAuthToken]
          _ <- updateOAuthTokenCache(oauthTokenCacheR, oauthToken)
          resp <- Found(Location(uri"/fetch_resource"))
        yield resp
      } { _ =>
        for
          _ <- info"No refresh token, asking the user to get a new access token"
          _ <- oauthTokenCacheR.update(_.copy(refreshToken = None))
          resp <- Ok(ClientPage.error("Unable to refresh token."))
        // resp <- Found(Location(uri"/authorize"))
        yield resp
      }
    )

  def refreshTokenRequest(refreshToken: String): Request[IO] =
    POST(
      UrlForm(
        "grant_type" -> "refresh_token",
        "refresh_token" -> refreshToken
        // "client_id" -> ClientInfo.client.id,
        // "client_secret" -> ClientInfo.client.secret
      ).updateFormFields("redirect_uri", Chain.fromSeq(ClientInfo.client.redirectUris.toList)),
      AuthServerInfo.authServer.tokenEndpoint,
      Headers(Authorization(BasicCredentials(Uri.encode(ClientInfo.client.id), Uri.encode(ClientInfo.client.secret))))
    )

  private[this] def getOrRefreshAccessToken(oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                                           (handleToken: String => IO[Response[IO]])(using Logger[IO]): IO[Response[IO]] =
    oauthTokenCacheR.get.flatMap(oauthTokenCache => oauthTokenCache.accessToken
      .fold(refreshAccessToken(oauthTokenCacheR, oauthTokenCache, "Missing access token."))(handleToken)
    )

  private[this] def bearerHeaders(accessToken: String): Headers = Headers(
    Authorization(Credentials.Token(AuthScheme.Bearer, accessToken)),
    `Content-Type`(MediaType.application.`x-www-form-urlencoded`)
  )

  private[this] def runHttpRequest(req: Request[IO])
                                  (onSuccess: Response[IO] => IO[Response[IO]])
                                  (onFailure: Int => IO[Response[IO]]): IO[Response[IO]] =
    EmberClientBuilder.default[IO].build.use { httpClient =>
      httpClient.run(req).use { response =>
        val statusCode = response.status.code
        if statusCode >= 200 && statusCode < 300 then onSuccess(response)
        else onFailure(statusCode)
      }
    }
