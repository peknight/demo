package com.peknight.demo.oauth2.app

import cats.data.Chain
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.option.*
import com.comcast.ip4s.port
import com.peknight.demo.oauth2.common.Getter.*
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.domain.WordsResult.*
import com.peknight.demo.oauth2.page.ClientPage
import com.peknight.demo.oauth2.random.*
import com.peknight.demo.oauth2.request.*
import io.circe.Json
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.circe.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.http4s.scalatags.scalatagsEncoder
import org.http4s.{client as _, *}
import org.typelevel.ci.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*
import pdi.jwt.*
import pdi.jwt.algorithms.JwtRSAAlgorithm

object ClientApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      clientR <- Ref.of[IO, Option[ClientInfo]](client.some)
      oauthTokenCacheR <- Ref.of[IO, OAuthTokenCache](OAuthTokenCache( None,
        None, // "j2r3oj32r23rmasd98uhjrk2o3i".some,
        None, None, None, None))
      stateR <- Ref.of[IO, Option[String]](None)
      codeVerifierR <- Ref.of[IO, Option[String]](None)
      nonceR <- Ref.of[IO, Option[String]](None)
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8000"
      _ <- start[IO](serverPort)(service(clientR, oauthTokenCacheR, stateR, codeVerifierR, nonceR, random))
      _ <- info"OAuth Client is listening at https://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

  given EntityDecoder[IO, WordsData] = jsonOf[IO, WordsData]
  given EntityDecoder[IO, ProduceData] = jsonOf[IO, ProduceData]
  given EntityDecoder[IO, FavoritesData] = jsonOf[IO, FavoritesData]
  given EntityDecoder[IO, UserFavoritesData] = jsonOf[IO, UserFavoritesData]

  object CodeQueryParamMatcher extends QueryParamDecoderMatcher[String]("code")
  object StateQueryParamMatcher extends QueryParamDecoderMatcher[String]("state")
  object ErrorQueryParamMatcher extends QueryParamDecoderMatcher[String]("error")
  object LanguageQueryParamMatcher extends QueryParamDecoderMatcher[String]("language")
  object WordQueryParamMatcher extends QueryParamDecoderMatcher[String]("word")

  def service(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache],
              stateR: Ref[IO, Option[String]], codeVerifierR: Ref[IO, Option[String]], nonceR: Ref[IO, Option[String]],
              random: Random[IO])(using Logger[IO]): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root => index(clientR, oauthTokenCacheR)
      case GET -> Root / "authorize" => authorize(clientR, stateR, codeVerifierR, nonceR, random)
      // 客户端凭据许可类型
      case GET -> Root / "client_credentials" => clientCredentials(clientR, oauthTokenCacheR)
      case GET -> Root / "username_password" => Ok(ClientPage.Text.usernamePassword)
      case req @ POST -> Root / "username_password" => usernamePassword(req, clientR, oauthTokenCacheR)
      case GET -> Root / "callback" :? CodeQueryParamMatcher(code) +& StateQueryParamMatcher(state) =>
        callback(code, state, clientR, oauthTokenCacheR, stateR, codeVerifierR, nonceR)
      case GET -> Root / "callback" :? ErrorQueryParamMatcher(error) => Ok(ClientPage.Text.error(error))
      case GET -> Root / "refresh" => refresh(clientR, oauthTokenCacheR)
      case GET -> Root / "fetch_resource" => fetchResource(clientR, oauthTokenCacheR)(fetchResourceRequest)
      case GET -> Root / "greeting" :? LanguageQueryParamMatcher(language) =>
        greeting(language, clientR, oauthTokenCacheR)
      case GET -> Root / "words" => Ok(ClientPage.Text.words(Seq.empty[String], 0L, NoGet))
      case GET -> Root / "get_words" => getWords(clientR, oauthTokenCacheR)
      case GET -> Root / "add_word" :? WordQueryParamMatcher(word) => addWord(word, clientR, oauthTokenCacheR)
      case GET -> Root / "delete_word" => deleteWord(clientR, oauthTokenCacheR)
      case GET -> Root / "produce" => produce(clientR, oauthTokenCacheR)
      case GET -> Root / "favorites" => favorites(clientR, oauthTokenCacheR)
      case GET -> Root / "revoke" =>
        oauthTokenCacheR.get.flatMap(oauthTokenCache => Ok(ClientPage.Text.revoke(oauthTokenCache)))
      case POST -> Root / "revoke" => revoke(clientR, oauthTokenCacheR)
      case GET -> Root / "userinfo" => userInfo(oauthTokenCacheR)
      case GET -> Root / "read_client" => readClient(clientR)
      case req @ POST -> Root / "update_client" => updateClient(req, clientR, oauthTokenCacheR)
      case GET -> Root / "unregister_client" => unregisterClient(clientR, oauthTokenCacheR)
    }

  private[this] def index(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
  : IO[Response[IO]] =
    for
      client <- clientR.get
      oauthTokenCache <- oauthTokenCacheR.get
      resp <- Ok(ClientPage.Text.index(client, oauthTokenCache))
    yield resp

  private[this] def authorize(clientR: Ref[IO, Option[ClientInfo]], stateR: Ref[IO, Option[String]],
                              codeVerifierR: Ref[IO, Option[String]], nonceR: Ref[IO, Option[String]], random: Random[IO])
                             (using Logger[IO]): IO[Response[IO]] =
    getOrRegisterClient(clientR) { client =>
      for
        state <- randomString[IO](random, 32)
        codeVerifier <- randomString[IO](random, 80)
        _ <- codeVerifierR.set(codeVerifier.some)
        codeChallenge = getCodeChallenge(codeVerifier)
        _ <- info"Generated code verifier $codeVerifier and challenge $codeChallenge"
        _ <- stateR.set(Some(state))
        nonce <- randomString[IO](random, 32)
        _ <- nonceR.set(Some(nonce))
        authorizeUrl = authServer.authorizationEndpoint.withQueryParams(AuthorizeParam(client.id,
          client.redirectUris.head, client.scope, ResponseType.Code, Some(state), Some(codeChallenge),
          Some(CodeChallengeMethod.S256), Some(nonce)))
        _ <- info"redirect: $authorizeUrl"
        resp <- Found(Location(authorizeUrl))
      yield resp
    }

  private[this] def clientCredentials(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                                     (using Logger[IO]): IO[Response[IO]] =
    getOrRegisterClient(clientR) { client =>
      val req: Request[IO] = POST(
        UrlForm(
          "grant_type" -> GrantType.ClientCredentials.value,
          scopeKey -> client.scope.mkString(" ")
        ),
        authServer.tokenEndpoint,
        basicHeaders(client.id, client.secret)
      )
      fetchOAuthToken(req, client, None, oauthTokenCacheR)
    }

  private[this] def usernamePassword(req: Request[IO], clientR: Ref[IO, Option[ClientInfo]],
                                     oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                                    (using Logger[IO]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body =>
      getOrRegisterClient(clientR) { client =>
        val reqOption: Option[Request[IO]] =
          for
            username <- body.get(usernameKey).find(_.nonEmpty)
            password <- body.get("password").find(_.nonEmpty)
          yield POST(
            UrlForm(
              "grant_type" -> GrantType.Password.value,
              usernameKey -> username,
              "password" -> password,
              scopeKey -> client.scope.mkString(" ")
            ),
            authServer.tokenEndpoint,
            basicHeaders(client.id, client.secret)
          )
        reqOption.fold[IO[Response[IO]]](Ok(ClientPage.Text.error("Param error")))(req =>
          fetchOAuthToken(req, client, None, oauthTokenCacheR)
        )
      }
    }

  private[this] def callback(code: String, state: String, clientR: Ref[IO, Option[ClientInfo]],
                             oauthTokenCacheR: Ref[IO, OAuthTokenCache], stateR: Ref[IO, Option[String]],
                             codeVerifierR: Ref[IO, Option[String]], nonceR: Ref[IO, Option[String]])
                            (using Logger[IO]): IO[Response[IO]] =
    stateR.get.flatMap { originState =>
      if !originState.contains(state) then
        for
          _ <- warn"State DOES NOT MATCH: expected ${originState.getOrElse("None")} got $state"
          stateResp <- Ok(ClientPage.Text.error("State value did not match"))
        yield stateResp
      else info"State value matches: expected $state got $state" *> getClientOrError(clientR) { client =>
        for
          codeVerifier <- codeVerifierR.get
          req = POST(
            UrlForm(
              "grant_type" -> GrantType.AuthorizationCode.value,
              "code" -> code,
              // "client_id" -> client.id,
              // "client_secret" -> client.secret,
              redirectUriKey -> client.redirectUris.head.toString,
            ).updateFormField(codeVerifierKey, codeVerifier),
            authServer.tokenEndpoint,
            basicHeaders(client.id, client.secret)
          )
          _ <- info"Requesting access token for code $code"
          nonce <- nonceR.get
          resp <- fetchOAuthToken(req, client, nonce, oauthTokenCacheR)
        yield resp
      }
    }

  private[this] def refresh(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                           (using Logger[IO]): IO[Response[IO]] =
    for
      oauthTokenCache <- oauthTokenCacheR.get
      resp <- refreshAccessToken(oauthTokenCache, clientR, oauthTokenCacheR, "Missing refresh token.")
    yield resp

  private[this] def fetchResource(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                                 (buildRequest: (String, OAuthTokenCache) => IO[Request[IO]])
                                 (using Logger[IO]): IO[Response[IO]] =
    getOrRefreshAccessToken(clientR, oauthTokenCacheR) { (accessToken, cache) =>
      for
        req <- buildRequest(accessToken, cache)
        _ <- info"Making request with access token $accessToken"
        _ <- info"protectedResourceEndpoint ${req.uri}"
        response <- runHttpRequest(req) { resp =>
          resp.as[Json].flatMap(data => Ok(ClientPage.Text.data(data)))
        } { statusCode =>
          for
            oauthTokenCache <- oauthTokenCacheR.updateAndGet(_.copy(accessToken = None))
            resp <- refreshAccessToken(oauthTokenCache, clientR, oauthTokenCacheR,
              s"Server returned response code: $statusCode")
          yield resp
        }
      yield response
    }

  private[this] def greeting(language: String, clientR: Ref[IO, Option[ClientInfo]],
                             oauthTokenCacheR: Ref[IO, OAuthTokenCache])(using Logger[IO]): IO[Response[IO]] =
    fetchResource(clientR, oauthTokenCacheR)((accessToken, _) => IO.pure(GET(
      helloWorldApi.withQueryParam("language", language),
      Headers(Authorization(Credentials.Token(AuthScheme.Bearer, accessToken)))
    )))

  private[this] def getWords(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                            (using Logger[IO]): IO[Response[IO]] =
    getOrRefreshAccessToken(clientR, oauthTokenCacheR) { (accessToken, _) =>
      runHttpRequest(GET(wordApi, bearerHeaders(accessToken))) { response =>
        for
          data <- response.as[WordsData]
          resp <- Ok(ClientPage.Text.words(data.words, data.timestamp, Get))
        yield resp
      } { _ =>
        Ok(ClientPage.Text.words(Seq.empty[String], 0L, NoGet))
      }
    }

  private[this] def addWord(word: String, clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                            (using Logger[IO]): IO[Response[IO]] =
    getOrRefreshAccessToken(clientR, oauthTokenCacheR) { (accessToken, _) =>
      runHttpRequest(POST(UrlForm("word" -> word), wordApi, bearerHeaders(accessToken))) { _ =>
        Ok(ClientPage.Text.words(Seq.empty[String], 0L, Add))
      } { _ =>
        Ok(ClientPage.Text.words(Seq.empty[String], 0L, NoAdd))
      }
    }

  private[this] def deleteWord(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                               (using Logger[IO]): IO[Response[IO]] =
    getOrRefreshAccessToken(clientR, oauthTokenCacheR) { (accessToken, _) =>
      runHttpRequest(DELETE(wordApi, bearerHeaders(accessToken))) { _ =>
        Ok(ClientPage.Text.words(Seq.empty[String], 0L, Rm))
      } { _ =>
        Ok(ClientPage.Text.words(Seq.empty[String], 0L, NoRm))
      }
    }

  private[this] def produce(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                           (using Logger[IO]): IO[Response[IO]] =
    getOrRefreshAccessToken(clientR, oauthTokenCacheR) { (accessToken, _) =>
      runHttpRequest(GET(produceApi, bearerHeaders(accessToken))) { response =>
        for
          data <- response.as[ProduceData]
          cache <- oauthTokenCacheR.get
          resp <- Ok(ClientPage.Text.produce(cache.scope.getOrElse(Set.empty[String]), data))
        yield resp
      } { _ =>
        for
          cache <- oauthTokenCacheR.get
          resp <- Ok(ClientPage.Text.produce(cache.scope.getOrElse(Set.empty[String]), ProduceData.empty))
        yield resp
      }
    }

  private[this] def favorites(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                             (using Logger[IO]): IO[Response[IO]] =
    getOrRefreshAccessToken(clientR, oauthTokenCacheR) { (accessToken, _) =>
      runHttpRequest(GET(favoritesApi, bearerHeaders(accessToken))) { response =>
        for
          data <- response.as[UserFavoritesData]
          _ <- info"Got data: $data"
          resp <- Ok(ClientPage.Text.favorites(data))
        yield resp
      } { _ =>
        Ok(ClientPage.Text.favorites(UserFavoritesData("", FavoritesData.empty)))
      }
    }

  private[this] def revoke(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                          (using Logger[IO]): IO[Response[IO]] =
    getClientOrError(clientR) { client =>
      handleOAuthToken(oauthTokenCacheR)(cache => Ok(ClientPage.Text.index(client.some, cache))) { (_, accessToken) =>
        val req = POST(
          // 可选：可以增加token_type_hint参数来提示授权服务器先查询哪种令牌（访问/刷新），但授权服务器可以忽略该参数两种都检查
          UrlForm("token" -> accessToken),
          authServer.revocationEndpoint,
          basicHeaders(client.id, client.secret)
        )
        for
          _ <- info"Revoking token $accessToken"
          _ <- oauthTokenCacheR.set(OAuthTokenCache(None, None, None, None, None, None))
          response <- runHttpRequest(req){ _ =>
            oauthTokenCacheR.get.flatMap(cache => Ok(ClientPage.Text.index(client.some, cache)))
          } { statusCode => Ok(ClientPage.Text.error(s"$statusCode")) }
        yield response
      }
    }

  private[this] def userInfo(oauthTokenCacheR: Ref[IO, OAuthTokenCache])(using Logger[IO]): IO[Response[IO]] =
    handleOAuthToken(oauthTokenCacheR)(_ => Ok(ClientPage.Text.error("Missing access token."))) {
      (cache, accessToken) => runHttpRequest(GET(authServer.userInfoEndpoint, bearerHeaders(accessToken))) { response =>
        for
          userInfo <- response.as[UserInfo]
          _ <- info"Got data: $userInfo"
          resp <- Ok(ClientPage.Text.userInfo(userInfo.some, cache.idToken))
        yield resp
      } { _ => Ok(ClientPage.Text.error("Unable to fetch user information")) }
    }

  private[this] def readClient(clientR: Ref[IO, Option[ClientInfo]])(using Logger[IO]): IO[Response[IO]] =
    getOrRegisterClient(clientR) { client =>
      val req = GET(
        client.registrationClientUri.getOrElse(getRegistrationClientUri(client.id)),
        bearerHeaders(client.registrationAccessToken.getOrElse(""))
      )
      runHttpRequest(req) { response =>
        for
          client <- response.as[ClientInfo]
          resp <- Ok(ClientPage.Text.data(client.asJson))
        yield resp
      } { statusCode =>
        Ok(ClientPage.Text.error(s"Unable to read client $statusCode"))
      }
    }

  private[this] def updateClient(req: Request[IO], clientR: Ref[IO, Option[ClientInfo]],
                                 oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                                (using Logger[IO]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body =>
      getOrRegisterClient(clientR) { client =>
        val updated = client.copy(
          name = body.fGet[Option, String, String](clientNameKey),
          clientIdCreatedAt = None,
          clientSecretExpiresAt = None,
          registrationClientUri = None,
          registrationAccessToken = None
        )
        val req = PUT(
          updated.asJson,
          client.registrationClientUri.getOrElse(getRegistrationClientUri(client.id)),
          bearerHeaders(client.registrationAccessToken.getOrElse(""))
        )
        info"Sending updated client: $updated" *> runHttpRequest(req) { response =>
          for
            updatedClient <- response.as[ClientInfo]
            oauthTokenCache <- oauthTokenCacheR.get
            resp <- Ok(ClientPage.Text.index(updatedClient.some, oauthTokenCache))
          yield resp
        } { statusCode =>
          Ok(ClientPage.Text.error(s"Unable to update client $statusCode"))
        }
      }
    }

  private[this] def unregisterClient(clientR: Ref[IO, Option[ClientInfo]], oauthTokenCacheR: Ref[IO, OAuthTokenCache])
  : IO[Response[IO]] =
    getClientOrError(clientR) { client =>
      val req = DELETE(
        client.registrationClientUri.getOrElse(getRegistrationClientUri(client.id)),
        bearerHeaders(client.registrationAccessToken.getOrElse(""))
      )
      clientR.set(None) *> runHttpRequest(req) { _ =>
        oauthTokenCacheR.get.flatMap(cache => Ok(ClientPage.Text.index(None, cache)))
      } { statusCode =>
        Ok(ClientPage.Text.error(s"Unable to delete client $statusCode"))
      }
    }

  private[this] def getClientOrError(clientR: Ref[IO, Option[ClientInfo]])(f: ClientInfo => IO[Response[IO]])
  : IO[Response[IO]] =
    clientR.get.flatMap {
      case None => Ok(ClientPage.Text.error("Client not registered yet!"))
      case Some(client) => f(client)
    }

  private[this] def getOrRegisterClient(clientR: Ref[IO, Option[ClientInfo]])(f: ClientInfo => IO[Response[IO]])
                                       (using Logger[IO]): IO[Response[IO]] =
    for
      currentCli <- clientR.get
      registeredClient <- currentCli.fold(registerClient(clientR))(_ => IO.pure(currentCli))
      resp <- registeredClient match
        case None => Ok(ClientPage.Text.error("Unable to register client."))
        case Some(client) => f(client)
    yield resp

  private[this] def registerClient(clientR: Ref[IO, Option[ClientInfo]])(using Logger[IO]): IO[Option[ClientInfo]] =
    val template = ClientMetadata(
      AuthMethod.SecretBasic,
      List(GrantType.AuthorizationCode),
      List(ResponseType.Code),
      client.redirectUris,
      "OAuth in Action Dynamic Test Client".some,
      clientIndex.some,
      none[Uri],
      Set("foo", "bar", "openid", "profile", "email", "address", "phone").some
    )
    val req: Request[IO] = POST(template.asJson, authServer.registrationEndpoint)
    runHttpRequest(req) { response =>
      for
        client <- response.as[ClientInfo]
        _ <- info"Got registered client $client"
        res <- if client.id.nonEmpty then clientR.set(client.some) *> IO.pure(client.some) else IO.pure(none[ClientInfo])
      yield res
    } { _ => IO.pure(none[ClientInfo]) }

  private[this] def fetchOAuthToken(req: Request[IO], client: ClientInfo, nonce: Option[String],
                                    oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                                   (using Logger[IO]): IO[Response[IO]] =
    runHttpRequest(req) { response =>
      for
        oauthToken <- response.as[OAuthToken]
        oauthTokenCache <- updateOAuthTokenCache(oauthToken, client.id, nonce, oauthTokenCacheR)
        resp <- oauthToken.idToken.fold(Ok(ClientPage.Text.index(client.some, oauthTokenCache)))(_ =>
          Ok(ClientPage.Text.userInfo(None, oauthTokenCache.idToken))
        )
      yield resp
    } { statusCode =>
      Ok(ClientPage.Text.error(s"Unable to fetch access token, server response: $statusCode"))
    }

  private[this] def refreshAccessToken(oauthTokenCache: OAuthTokenCache, clientR: Ref[IO, Option[ClientInfo]],
                                       oauthTokenCacheR: Ref[IO, OAuthTokenCache], error: String)
                                      (using Logger[IO]): IO[Response[IO]] =
    getClientOrError(clientR) { client =>
      oauthTokenCache.refreshToken.fold(Ok(ClientPage.Text.error(error)))(refreshToken =>
        runHttpRequest(refreshTokenRequest(client, refreshToken)) { response =>
          for
            oauthToken <- response.as[OAuthToken]
            _ <- updateOAuthTokenCache(oauthToken, client.id, None, oauthTokenCacheR)
            resp <- Ok(ClientPage.Text.error("Fetch resource error"))
            // resp <- Found(Location(uri"/fetch_resource"))
          yield resp
        } { _ =>
          for
            _ <- info"No refresh token, asking the user to get a new access token"
            _ <- oauthTokenCacheR.update(_.copy(refreshToken = None))
            resp <- Ok(ClientPage.Text.error("Unable to refresh token."))
            // resp <- Found(Location(uri"/authorize"))
          yield resp
        }
      )
    }

  private[this] def fetchResourceRequest(accessToken: String, oauthTokenCache: OAuthTokenCache): IO[Request[IO]] =
    oauthTokenCache.key match
      case Some(sk) =>
        val jwtHeader = JwtHeader(oauthTokenCache.algorithm.map(JwtAlgorithm.fromString), "PoP".some, None, sk.kid)
        for
          realTime <- IO.realTime
          payload = PopPayload(accessToken, realTime.toSeconds, POST.name.some, protectedResourceAddr.some,
            "/resource".some)
          privateKey <- rsaPrivateKey(sk)
          _ <- IO.println(s"sk=$sk, privateKey=$privateKey")
          signed <- IO(JwtCirce.encode(jwtHeader.toJson, payload.asJson.deepDropNullValues.noSpaces, privateKey,
            oauthTokenCache.algorithm.flatMap(JwtAlgorithm.optionFromString).getOrElse(JwtAlgorithm.RS256)
              .asInstanceOf[JwtRSAAlgorithm]))
        yield POST(protectedResourceApi, Headers(Authorization(Credentials.Token(ci"PoP", signed))))
      case None => IO.pure(resourceRequest(accessToken))

  private[this] def refreshTokenRequest(client: ClientInfo, refreshToken: String): Request[IO] =
    POST(
      UrlForm(
        "grant_type" -> "refresh_token",
        "refresh_token" -> refreshToken
        // "client_id" -> client.id,
        // "client_secret" -> client.secret
      ).updateFormFields(redirectUriKey, Chain.fromSeq(client.redirectUris.toList)),
      authServer.tokenEndpoint,
      basicHeaders(client.id, client.secret)
    )

  private[this] def updateOAuthTokenCache(oauthToken: OAuthToken, clientId: String, nonce: Option[String],
                                          oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                                         (using Logger[IO]): IO[OAuthTokenCache] =
    for
      _ <- info"Got access token: ${oauthToken.accessToken}"
      _ <- oauthToken.refreshToken.fold(IO.unit)(refresh => info"Got refresh_token: $refresh")
      payloadOption <- oauthToken.idToken.fold(IO.pure(none[IdToken])) { idToken =>
        for
          _ <- info"Got ID token: $idToken"
          payload <- checkJwt[IdToken](jwtRS256Decode(idToken), clientId, nonce)
        yield payload
      }
      _ <- info"Got scope: ${oauthToken.scope}"
      oauthTokenCache <- oauthTokenCacheR.updateAndGet(origin => OAuthTokenCache(
        accessToken = Some(oauthToken.accessToken),
        refreshToken = oauthToken.refreshToken.orElse(origin.refreshToken),
        scope = oauthToken.scope.orElse(origin.scope),
        idToken = payloadOption.orElse(origin.idToken),
        key = oauthToken.accessTokenKey.orElse(origin.key),
        algorithm = oauthToken.algorithm.orElse(origin.algorithm)
      ))
    yield oauthTokenCache

  private[this] def getOrRefreshAccessToken(clientR: Ref[IO, Option[ClientInfo]],
                                            oauthTokenCacheR: Ref[IO, OAuthTokenCache])
                                           (handleToken: (String, OAuthTokenCache) => IO[Response[IO]])
                                           (using Logger[IO]): IO[Response[IO]] =
    oauthTokenCacheR.get.flatMap(oauthTokenCache => oauthTokenCache.accessToken
      .fold(refreshAccessToken(oauthTokenCache, clientR, oauthTokenCacheR, "Missing access token."))(
        accessToken => handleToken(accessToken, oauthTokenCache)
      )
    )

  private[this] def handleOAuthToken[T](oauthTokenCacheR: Ref[IO, OAuthTokenCache])(ifEmpty: OAuthTokenCache => IO[T])
                                       (f: (OAuthTokenCache, String) => IO[T]): IO[T] =
    oauthTokenCacheR.get.flatMap(oauthTokenCache =>
      oauthTokenCache.accessToken.fold(ifEmpty(oauthTokenCache))(accessToken => f(oauthTokenCache, accessToken))
    )

  private[this] def bearerHeaders(accessToken: String): Headers = Headers(
    Authorization(Credentials.Token(AuthScheme.Bearer, accessToken))
  )

  private[this] def basicHeaders(clientId: String, clientSecret: Option[String]): Headers = Headers(
    Authorization(BasicCredentials(Uri.encode(clientId), Uri.encode(clientSecret.getOrElse(""))))
  )
