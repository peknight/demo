package com.peknight.demo.oauth2.app

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, OptionT, Validated}
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.either.*
import cats.syntax.option.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.common.Getter
import com.peknight.demo.oauth2.common.Getter.*
import com.peknight.demo.oauth2.common.StringCaseStyle.camelToSnake
import com.peknight.demo.oauth2.common.UrlFragment.UrlFragmentValue
import com.peknight.demo.oauth2.common.UrlFragmentEncoder.*
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.page.AuthorizationServerPage
import com.peknight.demo.oauth2.random.*
import com.peknight.demo.oauth2.repository.*
import fs2.Stream
import fs2.text.base64
import io.circe.JsonObject
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.http4s.scalatags.*
import org.typelevel.ci.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtHeader}
import scodec.bits.Bases.Alphabets.Base64Url

import java.math.BigInteger
import java.security.*
import java.security.interfaces.{RSAPrivateCrtKey, RSAPublicKey}
import scala.concurrent.duration.*

object AuthorizationServerApp extends IOApp.Simple:

  val run: IO[Unit] =
    for
      requestsR <- Ref.of[IO, Map[String, AuthorizeParam]](Map.empty)
      codesR <- Ref.of[IO, Map[String, AuthorizeCodeCache]](Map.empty)
      clientsR <- Ref.of[IO, Seq[ClientInfo]](clients)
      usePop <- usePopConfig.load[IO]
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8001"
      _ <- clearRecord
      _ <- insertRecord(OAuthTokenRecord("oauth-client-1".some, None, "j2r3oj32r23rmasd98uhjrk2o3i".some,
        Set("foo", "bar").some, None, None)).timeout(5.seconds)
      _ <- start[IO](serverPort)(service(random, usePop, requestsR, codesR, clientsR))
      _ <- info"OAuth Authorization Server is listening at https://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

  given Getter[Option, UrlForm, String, GrantType] = Getter[Option, UrlForm, String, String].to[GrantType]
  given Getter[Option, UrlForm, String, Uri] = Getter[Option, UrlForm, String, String].to[Uri]

  private def service(random: Random[IO], usePop: Boolean, requestsR: Ref[IO, Map[String, AuthorizeParam]],
                            codesR: Ref[IO, Map[String, AuthorizeCodeCache]], clientsR: Ref[IO, Seq[ClientInfo]])
                           (using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => clientsR.get.flatMap(clients => Ok(AuthorizationServerPage.Text.index(authServer, clients)))
    case GET -> Root / "authorize" :? AuthorizeParam(authorizeParamValid) =>
      authorize(authorizeParamValid, random, requestsR, clientsR)
    case req @ POST -> Root / "approve" => approve(req, random, requestsR, codesR, clientsR)
    case req @ POST -> Root / "token" => token(req, random, usePop, codesR, clientsR)
    case req @ POST -> Root / "revoke" => revoke(req, clientsR)
    case req @ POST -> Root / "introspect" => introspect(req)
    case req @ POST -> Root / "register" => postRegister(req, random, clientsR)
    case req @ GET -> Root / "register" / clientId => getRegister(clientId, req, clientsR)
    case req @ PUT -> Root / "register" / clientId => putRegister(clientId, req, clientsR)
    case req @ DELETE -> Root / "register" / clientId => deleteRegister(clientId, req, clientsR)
    case req @ (GET | POST) -> Root / "userinfo" => userInfoEndpoint(req, authorizationServerAddr)
  }

  private def authorize(authorizeParamValid: Validated[String, AuthorizeParam], random: Random[IO],
                        requestsR: Ref[IO, Map[String, AuthorizeParam]], clientsR: Ref[IO, Seq[ClientInfo]])
                       (using Logger[IO]): IO[Response[IO]] = authorizeParamValid match
    case Invalid(msg) => Ok(AuthorizationServerPage.Text.error(msg))
    case Valid(param) =>
      getClient(param.clientId, clientsR).flatMap {
        case None => info"Unknown client ${param.clientId}" *> Ok(AuthorizationServerPage.Text.error("Unknown client"))
        case Some(client) => client.redirectUris.find(_ == param.redirectUri) match
          case None =>
            info"Mismatched redirect URI, expected ${client.redirectUris.toList.mkString(" ")} got ${param.redirectUri}" *>
              Ok(AuthorizationServerPage.Text.error("Invalid redirect URI"))
          // Some(redirectUri)
          case _ =>
            if param.scope.diff(client.scope).nonEmpty then
            // client asked for a scope it couldn't have
              info"Invalid scope requested ${param.scope.mkString(" ")}" *> BadRequest(ErrorInfo("invalid_scope").asJson)
            // 第九章漏洞防范，使用BadRequest而不是重定向
            // Found(Location(redirectUri.withQueryParam("error", "invalid_scope")))
            else
              for
                reqId <- randomString(random, 8)
                _ <- requestsR.update(_ + (reqId -> param))
                resp <- Ok(AuthorizationServerPage.Text.approve(client, reqId, param.scope.toList))
              yield resp
      }
  end authorize

  private def approve(req: Request[IO], random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]],
                            codesR: Ref[IO, Map[String, AuthorizeCodeCache]], clientsR: Ref[IO, Seq[ClientInfo]])
                           (using Logger[IO]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body => body.fGet[Option, String, String]("reqid") match
      case None => Ok(AuthorizationServerPage.Text.error("No matching authorization request"))
      case Some(reqId) => requestsR.modify(requests => (requests.removed(reqId), requests.get(reqId))).flatMap {
        case None => Ok(AuthorizationServerPage.Text.error("No matching authorization request"))
        case Some(query) => body.get("approve").find(_ == "Approve") match
          case Some(_) => query.responseType match
            case ResponseType.Code => checkScope(body, query, clientsR) { scope =>
              val user = body.fGet[Option, String, String]("user")
              for
                code <- randomString(random, 8)
                _ <- codesR.update(_ + (code -> AuthorizeCodeCache(query, scope, user)))
                resp <- Found(Location(query.redirectUri
                  .withQueryParam("code", code)
                  .withOptionQueryParam("state", query.state)
                ))
              yield resp
            }
            case ResponseType.Token => checkScope(body, query, clientsR) { scope =>
              val user = body.fGet[Option, String, String]("user")
              user.flatMap(userInfos.get).fold[IO[Response[IO]]] {
                val username = user.getOrElse("None")
                info"Unknown user $username" *>
                  InternalServerError(AuthorizationServerPage.Text.error(s"Unknown user $username"))
              } { userInfo =>
                given UrlFragmentValueEncoder[Set[String]] with
                  def encode(value: Set[String]): UrlFragmentValue = UrlFragmentValue(value.mkString(" "))
                for
                  _ <- info"User $userInfo"
                  tokenResponse <- generateTokens(random, query.clientId, userInfo.some, scope, query.state, None,
                    false, false)
                  x = tokenResponse.toFragment(camelToSnake)
                  resp <- Found(Location(query.redirectUri.withFragment(tokenResponse.toFragment(camelToSnake))))
                yield resp
              }
            }
            // 后续支持其它响应类型扩展
            case null => Found(Location(query.redirectUri
              .withQueryParam("error", "unsupported_response_type")
            ))
          case None => Found(Location(query.redirectUri.withQueryParam("error", "access_denied")))
      }
    }
  end approve

  private def token(req: Request[IO], random: Random[IO], usePop: Boolean,
                          codesR: Ref[IO, Map[String, AuthorizeCodeCache]], clientsR: Ref[IO, Seq[ClientInfo]])
                         (using Logger[IO]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body => checkAuthorization(req, body, clientsR) { client =>
      body.fGet[Option, String, GrantType]("grant_type") match
        case Some(GrantType.AuthorizationCode) => authorizationCode(client.id, body, random, usePop, codesR)
        case Some(GrantType.ClientCredentials) => clientCredentials(client, body, random)
        case Some(GrantType.RefreshToken) => refreshToken(client.id, body, random)
        case Some(GrantType.Password) => password(client, body, random)
        case grantType => info"Unknown grant type ${grantType.getOrElse("None")}" *>
          Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, authorizationServerAddr)))
    }}
  end token

  private def authorizationCode(clientId: String, body: UrlForm, random: Random[IO], usePop: Boolean,
                                codesR: Ref[IO, Map[String, AuthorizeCodeCache]])
                               (using Logger[IO]): IO[Response[IO]] =
    for
      codeCache <- body.fGet[Option, String, String]("code") match
        case Some(code) => codesR.modify(codes => (codes.removed(code), code.some -> codes.get(code)))
        case None => IO((none[String], none[AuthorizeCodeCache]))
      resp <- codeCache match
        case (_, Some(cache)) if !body.fGet[Option, String, Uri](redirectUriKey)
          .contains(cache.authorizationEndpointRequest.redirectUri) =>
          BadRequest(ErrorInfo("invalid_grant").asJson)
        case (_, Some(cache)) if cache.authorizationEndpointRequest.clientId != clientId =>
          info"Client mismatch, expected ${cache.authorizationEndpointRequest.clientId} got $clientId" *>
            invalidGrantResp
        case (Some(code), Some(cache)) =>
          cache.user.flatMap(userInfos.get) match
            case Some(user) =>
              for
                _ <- info"User $user"
                resp <- checkCodeChallenge(cache, body) {
                  for
                    tokenResponse <- generateTokens(random, clientId, cache.user.flatMap(userInfos.get), cache.scope,
                      None, cache.authorizationEndpointRequest.nonce, true, usePop)
                    _ <- info"Issued tokens for code $code"
                    resp <- Ok(tokenResponse.asJson)
                  yield resp
                }
              yield resp
            case None =>
              val preferredUsername = cache.user.getOrElse("None")
              info"Unknown user $preferredUsername" *>
                InternalServerError(AuthorizationServerPage.Text.error(s"Unknown user $preferredUsername"))
        case (code, _) => info"Unknown code, ${code.getOrElse("None")}" *> BadRequest(ErrorInfo("invalid_grant").asJson)
    yield resp

  private def clientCredentials(client: ClientInfo, body: UrlForm, random: Random[IO])
                               (using Logger[IO]): IO[Response[IO]] =
    checkScope(body, client) { scope =>
      for
        tokenResponse <- generateTokens(random, client.id, None, scope, None, None, false, false)
        resp <- Ok(tokenResponse.asJson)
      yield resp
    }

  private def refreshToken(clientId: String, body: UrlForm, random: Random[IO])
                          (using Logger[IO]): IO[Response[IO]] =
    for
      refreshTokenRecord <- body.fGet[Option, String, String]("refresh_token") match
        case r@Some(refreshToken) => getRecordByRefreshToken(refreshToken).map(r -> _)
        case None => IO((none[String], none[OAuthTokenRecord]))
      resp <- refreshTokenRecord match
        case (Some(refreshToken), Some(record)) if !record.clientId.contains(clientId) =>
          info"Invalid client using a refresh token, expected ${record.clientId.getOrElse("None")} got $clientId" *>
            removeRecordByRefreshToken(refreshToken) *>
            BadRequest(ErrorInfo("invalid_grant").asJson)
        case (Some(refreshToken), Some(record)) =>
          for
            _ <- info"We found a matching refresh token $refreshToken"
            user = record.user.flatMap(preferredUsername => userInfos.values
              .find(_.preferredUsername.contains(preferredUsername)))
            accessToken <- generateAccessToken(user, record.scope, random)
            _ <- insertRecord(OAuthTokenRecord(clientId.some, accessToken.some, None, record.scope, record.user, None))
            _ <- info"Issuing access token $accessToken for refresh token $refreshToken"
            resp <- Ok(OAuthToken(accessToken, AuthScheme.Bearer, body.fGet[Option, String, String]("refresh_token"),
              None, None, None, None, None).asJson)
          yield resp
        case _ => info"No matching token was found." *> invalidGrantResp
    yield resp

  private def password(client: ClientInfo, body: UrlForm, random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    val usernameOption = body.fGet[Option, String, String](usernameKey)
    val userOption = usernameOption.flatMap(getUser)
    userOption match
      case None => info"Unknown user ${usernameOption.getOrElse("None")}" *> invalidGrantResp
      case Some(user) =>
        for
          _ <- info"user is $user"
          password = body.fGet[Option, String, String]("password")
          resp <- password.filter(user.password.contains).fold[IO[Response[IO]]] {
            val bodyPwd = password.getOrElse("None")
            val userPwd = user.password.getOrElse("None")
            info"Mismatched resource owner password, expected $userPwd got $bodyPwd" *> invalidGrantResp
          } { _ => checkScope(body, client) { scope =>
            for
              tokenResponse <- generateTokens(random, client.id, userOption, scope, None, None,
                true, false)
              resp <- Ok(tokenResponse.asJson)
            yield resp
          }}
        yield resp

  // 可选：支持刷新信息撤回，刷新信息撤回时应将与其关联的访问令牌一并撤回
  private def revoke(req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]])(using Logger[IO]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body => checkAuthorization(req, body, clientsR) { client =>
      // 这里返回NoContent是为了防止透露过多关于信息的信息（撤销成功与否都不透出，均认为成功）
      body.fGet[Option, String, String]("token").fold(NoContent()) { inToken =>
        for
          count <- removeRecordByAccessTokenAndClientId(inToken, client.id)
          _ <- info"Removed $count tokens"
          resp <- NoContent()
        yield resp
      }
    }}

  // 可选：增加刷新令牌内省支持
  private def introspect(req: Request[IO])(using Logger[IO]): IO[Response[IO]] =
    req.headers.get[Authorization] match
      case Some(Authorization(BasicCredentials((resourceId, resourceSecret)))) => getProtectedResource(resourceId) match
        case None => info"Unknown resource $resourceId" *> unauthorizedBasicResp
        case Some(resource) if resource.resourceSecret != resourceSecret =>
          info"Mismatched secret, expected ${resource.resourceSecret} got $resourceSecret" *> unauthorizedBasicResp
        case _ =>
          val recordOptionT: OptionT[IO, OAuthTokenRecord] =
            for
              body <- req.as[UrlForm].optionT
              inToken <- OptionT(IO.pure(body.fGet[Option, String, String]("token")))
              _ <- info"Introspecting token $inToken".optionT
              record <- OptionT(getRecordByAccessToken(inToken))
            yield record
          for
            recordOption <- recordOptionT.value
            resp <- recordOption match
              case Some(record) => info"We found a matching token: ${record.accessToken.getOrElse("None")}" *>
                Ok(IntrospectionResponse(true, record.scope, record.clientId, record.user,
                  authorizationServerIndex.toString.some, record.user, protectedResourceIndex.toString.some,
                  None, None, record.accessTokenKey).asJson)
              case _ => info"No matching token was found" *>
                Ok(IntrospectionResponse(false).asJson)
          yield resp
      case _ => info"Unknown resource None" *> unauthorizedBasicResp

  private def postRegister(req: Request[IO], random: Random[IO], clientsR: Ref[IO, Seq[ClientInfo]])
                                (using Logger[IO]): IO[Response[IO]] =
    val parseBody = parseRequest(req)(checkClientMetadata)(checkClientMetadata)
    parseBody.flatMap {
      case Left(error) => BadRequest(ErrorInfo(error).asJson)
      case Right(clientMetadata) =>
        for
          clientId <- randomString(random, 32)
          clientSecretOption <-
            if Seq(AuthMethod.ClientSecretBasic, AuthMethod.ClientSecretPost, AuthMethod.SecretBasic,
              AuthMethod.SecretPost).contains(clientMetadata.tokenEndpointAuthMethod) then
              randomString(random, 32).map(_.some)
            else IO.pure(none[String])
          realTime <- IO.realTime
          clientIdCreatedAt = realTime.toSeconds
          clientSecretExpiresAt = 0L
          registrationAccessToken <- randomString(random, 32)
          registrationClientUri = getRegistrationClientUri(clientId)
          client = clientMetadata.toClientInfo(clientId, clientSecretOption, clientIdCreatedAt,
            clientSecretExpiresAt, registrationAccessToken, registrationClientUri)
          _ <- clientsR.update(_ :+ client)
          _ <- info"Registered new client: $client"
          resp <- Created(client.asJson.deepDropNullValues)
        yield resp
    }

  private def getRegister(clientId: String, req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]]): IO[Response[IO]] =
    authorizeConfigurationEndpointRequest(clientId, req, clientsR)(client => Ok(client.asJson))

  private def putRegister(clientId: String, req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]]): IO[Response[IO]] =
    authorizeConfigurationEndpointRequest(clientId, req, clientsR) { client =>
      parseRequest(req)(json => checkClientSecret(client)(json))(form => checkClientSecret(client)(form)).flatMap {
        case Left(error) => BadRequest(ErrorInfo(error).asJson)
        case Right(updatedClient) =>
          clientsR.update(clients => clients.filter(_.id != client.id) :+ updatedClient) *> Ok(updatedClient.asJson)
      }
    }

  private def deleteRegister(clientId: String, req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]])
                            (using Logger[IO]): IO[Response[IO]] =
    authorizeConfigurationEndpointRequest(clientId, req, clientsR) { client =>
      for
        _ <- clientsR.update(_.filter(_.id != client.id))
        count <- removeRecordByClientId(client.id)
        _ <- if count > 0 then info"Removed $count tokens" else IO.unit
        resp <- NoContent()
      yield resp
    }

  private def generateTokens(random: Random[IO], clientId: String, user: Option[UserInfo], scope: Set[String],
                             state: Option[String], nonce: Option[String], generateRefreshToken: Boolean,
                             generatePopKeys: Boolean)
                            (using Logger[IO]): IO[OAuthToken] =
    for
      accessToken <- generateAccessToken(user, scope.some, random)
      keyPair <-
        if generatePopKeys then
          rsaKeyPair.map {
            case (sk, pk) => (sk.some, pk.some)
          }
        else IO.pure((none[RsaKey], none[RsaKey]))
      _ <- insertRecord(OAuthTokenRecord(clientId.some, accessToken.some, None, scope.some,
        user.flatMap(_.preferredUsername), keyPair._2))
      _ <- info"Issuing accessToken $accessToken"
      refreshTokenOption <-
        if generateRefreshToken then
          for
            refresh <- randomString(random, 32)
            refreshOption = refresh.some
            _ <- insertRecord(OAuthTokenRecord(clientId.some, None, refreshOption, scope.some,
              user.flatMap(_.preferredUsername), None))
            _ <- info"and refresh token $refresh"
          yield refreshOption
        else IO.pure(None)
      _ <- info"with scope ${scope.mkString(" ")}"
      idTokenOption <-
        if scope.contains("openid") && user.isDefined then
          rs256JwtForClient(clientId, user, nonce).map(_.some)
        else IO.pure(None)
    yield OAuthToken(accessToken, if generatePopKeys then ci"PoP" else AuthScheme.Bearer, refreshTokenOption,
      scope.some, state, idTokenOption, keyPair._1, if generatePopKeys then JwtAlgorithm.RS256.name.some else None)

  private def checkScope(body: UrlForm, query: AuthorizeParam, clientsR: Ref[IO, Seq[ClientInfo]])
                        (resp: Set[String] => IO[Response[IO]]): IO[Response[IO]] =
    val scope = body.values.keys.filter(_.startsWith("scope_")).map(_.substring("scope_".length)).toSet[String]
    getClient(query.clientId, clientsR).flatMap { clientOption =>
      val cScope = clientOption.map(_.scope).getOrElse(Set.empty[String])
      if scope.diff(cScope).nonEmpty then
        Found(Location(query.redirectUri.withQueryParam("error", "invalid_scope")))
      else resp(scope)
    }

  private def checkScope(body: UrlForm, client: ClientInfo)(resp: Set[String] => IO[Response[IO]]): IO[Response[IO]] =
    val scope = getScope(body).getOrElse(Set.empty[String])
    if scope.diff(client.scope).nonEmpty then BadRequest(ErrorInfo("invalid_scope").asJson)
    else resp(scope)

  private def checkAuthorization(req: Request[IO], body: UrlForm, clientsR: Ref[IO, Seq[ClientInfo]])
                                (f: ClientInfo => IO[Response[IO]])(using Logger[IO]): IO[Response[IO]] =
    val (hClientId, hClientSecret) = req.headers.get[Authorization] match
      case Some(Authorization(BasicCredentials((clientId, clientSecret)))) =>
        (Uri.decode(clientId).some, Uri.decode(clientSecret).some)
      case _ => (none[String], none[String])
    val bClientId = body.fGet[Option, String, String]("client_id")
    val bClientSecret = body.fGet[Option, String, String]("client_secret")
    (hClientId, bClientId) match
      // if we've already seen the client's credentials in the authorization header, this is an error
      case (Some(_), Some(_)) => info"Client attempted to authenticate with multiple methods" *> invalidClientResp
      case (None, None) => info"Unknown client" *> invalidClientResp
      case _ =>
        val clientId = hClientId.orElse(bClientId).get
        val clientSecret = hClientSecret.orElse(bClientSecret).getOrElse("")
        getClient(clientId, clientsR).flatMap {
          case None => info"Unknown client $clientId" *> invalidClientResp
          case Some(client) if !client.secret.contains(clientSecret) =>
            info"Mismatched client secret, expected ${client.secret.getOrElse("None")} got $clientSecret" *> invalidClientResp
          case Some(client) => f(client)
        }
  end checkAuthorization

  private def checkCodeChallenge(cache: AuthorizeCodeCache, body: UrlForm)(pass: => IO[Response[IO]])
                                (using Logger[IO]): IO[Response[IO]] =
    cache.authorizationEndpointRequest.codeChallenge.fold(pass) { codeChallenge =>
      val codeVerifier = body.fGet[Option, String, String](codeVerifierKey)
      info"Testing challenge $codeChallenge against verifier ${codeVerifier.getOrElse("None")}" *> {
        cache.authorizationEndpointRequest.codeChallengeMethod.fold {
          info"Unknown code challenge method None" *> BadRequest(ErrorInfo("invalid_request").asJson)
        }{ codeChallengeMethod =>
          val challenge = codeChallengeMethod match
            case CodeChallengeMethod.Plain => codeVerifier
            case CodeChallengeMethod.S256 => codeVerifier.map(getCodeChallenge)
          challenge.filter(_ == codeChallenge).fold {
            info"Code challenge did not match, expected $codeChallenge got ${challenge.getOrElse("None")}" *>
              BadRequest(ErrorInfo("invalid_request").asJson)
          }(_ => pass)
        }
      }
    }

  private def authorizeConfigurationEndpointRequest(clientId: String, req: Request[IO],
                                                    clientsR: Ref[IO, Seq[ClientInfo]])
                                                   (f: ClientInfo => IO[Response[IO]]): IO[Response[IO]] =
    getClient(clientId, clientsR).flatMap {
      case None => NotFound()
      case Some(client) => req.headers.get[Authorization] match
        case Some(Authorization(Credentials.Token(AuthScheme.Bearer, token))) if client.registrationAccessToken.contains(token) =>
          f(client)
        case Some(Authorization(Credentials.Token(AuthScheme.Bearer, _))) => Forbidden()
        case _ => unauthorizedBearerResp
    }

  private def checkClientMetadata[T](params: T)(
    using
    getter: Getter[Option, T, String, String],
    listGetter: Getter[Option, T, String, List[String]]
  ): Either[String, ClientMetadata] =
    given Getter[Option, T, String, AuthMethod] = getter.to[AuthMethod]
    given Getter[Option, T, String, Uri] = getter.to[Uri]
    given grantTypesGetter: Getter[Option, T, String, List[GrantType]] = listGetter.to[List[GrantType]]
    given responseTypesGetter: Getter[Option, T, String, List[ResponseType]] = listGetter.to[List[ResponseType]]
    given nonEmptyUrisGetter: Getter[Option, T, String, NonEmptyList[Uri]] = listGetter.to[List[Uri]].to[NonEmptyList[Uri]]
    val tokenEndpointAuthMethod = params.fGet[Option, String, AuthMethod](tokenEndpointAuthMethodKey)
      .getOrElse(AuthMethod.SecretBasic)
    if !Seq(AuthMethod.SecretBasic, AuthMethod.SecretPost, AuthMethod.None).contains(tokenEndpointAuthMethod) then
      "invalid_client_metadata".asLeft
    else
      val paramGrantTypes: List[GrantType] = params.fGet[Option, String, List[GrantType]](grantTypesKey).getOrElse(Nil)
      val paramResponseTypes: List[ResponseType] = params.fGet[Option, String, List[ResponseType]](responseTypesKey)
        .getOrElse(Nil)
      val (grantTypes, responseTypes) = (paramGrantTypes, paramResponseTypes) match
        case (Nil, Nil) => (List(GrantType.AuthorizationCode), List(ResponseType.Code))
        case (Nil, responseTypes) =>
          val grantTypes = if responseTypes.contains(ResponseType.Code) then List(GrantType.AuthorizationCode) else Nil
          (grantTypes, responseTypes)
        case (grantTypes, Nil) =>
          val responseTypes = if grantTypes.contains(GrantType.AuthorizationCode) then List(ResponseType.Code) else Nil
          (grantTypes, responseTypes)
        case (grantTypes, responseTypes) =>
          val responses =
            if grantTypes.contains(GrantType.AuthorizationCode) && !responseTypes.contains(ResponseType.Code) then
              responseTypes :+ ResponseType.Code
            else responseTypes
          val grants =
            if !grantTypes.contains(GrantType.AuthorizationCode) && responseTypes.contains(ResponseType.Code) then
              grantTypes :+ GrantType.AuthorizationCode
            else grantTypes
          (grants, responses)
      if grantTypes.diff(Seq(GrantType.AuthorizationCode, GrantType.RefreshToken)).nonEmpty ||
        !responseTypes.forall(_ == ResponseType.Code) then "invalid_client_metadata".asLeft
      else
        params.fGet[Option, String, NonEmptyList[Uri]](redirectUrisKey) match
          case None => "invalid_redirect_uri".asLeft
          case Some(redirectUris) =>
            val clientName: Option[String] = params.fGet[Option, String, String](clientNameKey)
            val clientUri: Option[Uri] = params.fGet[Option, String, Uri](clientUriKey)
            val logoUri: Option[Uri] = params.fGet[Option, String, Uri](logoUriKey)
            val scope: Option[Set[String]] = getScope(params)
            ClientMetadata(tokenEndpointAuthMethod, grantTypes, responseTypes, redirectUris, clientName, clientUri,
              logoUri, scope).asRight
  end checkClientMetadata

  private def checkClientSecret[T](client: ClientInfo)(params: T)(
    using
    getter: Getter[Option, T, String, String],
    listGetter: Getter[Option, T, String, List[String]]
  ): Either[String, ClientInfo] =
    if !params.fGet[Option, String, String](clientIdKey).contains(client.id) then
      "invalid_client_metadata".asLeft[ClientInfo]
    else if params.fGet[Option, String, String](clientSecretKey).exists(!client.secret.contains(_)) then
      "invalid_client_metadata".asLeft[ClientInfo]
    else checkClientMetadata(params) match
      case Left(error) => error.asLeft[ClientInfo]
      case Right(clientMetadata) => clientMetadata.updateClientInfo(client).asRight[String]

  private def generateAccessToken(user: Option[UserInfo], scope: Option[Set[String]], random: Random[IO]): IO[String] =
    randomString(random, 32)
    // unsafeJwt(user, scope, random)
    // hs256Jwt(user, scope, random)
    // rs256Jwt(user, scope, random)

  //noinspection ScalaUnusedSymbol
  private def unsafeJwt(user: Option[UserInfo], scope: Option[Set[String]], random: Random[IO]): IO[String] =
    for
      realTime <- IO.realTime
      jwtId <- randomString(random, 8)
      header = JwtHeader(typ = JwtHeader.DEFAULT_TYPE.some)
      payload = createPayload(user, scope, protectedResourceIndex.toString, realTime, jwtId.some, None)
      accessToken <- IO(JwtCirce.encode(header.toJson, payload))
    yield accessToken

  //noinspection ScalaUnusedSymbol
  private def hs256Jwt(user: Option[UserInfo], scope: Option[Set[String]], random: Random[IO]): IO[String] =
    for
      realTime <- IO.realTime
      jwtId <- randomString(random, 8)
      algorithm = JwtAlgorithm.HS256
      header = JwtHeader(algorithm, JwtHeader.DEFAULT_TYPE)
      payload = createPayload(user, scope, protectedResourceIndex.toString, realTime, jwtId.some, None)
      accessToken <- IO(JwtCirce.encode(header.toJson, payload, toHex(sharedTokenSecret), algorithm))
    yield accessToken

  // P166 可以再提高下 变成jwe加密
  //noinspection ScalaUnusedSymbol
  private def rs256Jwt(user: Option[UserInfo], scope: Option[Set[String]], random: Random[IO]): IO[String] =
    for
      realTime <- IO.realTime
      jwtId <- randomString(random, 8)
      algorithm = JwtAlgorithm.RS256
      header = JwtHeader(algorithm.some, JwtHeader.DEFAULT_TYPE.some, none, rsaKey.kid)
      payload = createPayload(user, scope, protectedResourceIndex.toString, realTime, jwtId.some, None)
      privateKey <- rsaPrivateKey(rsaKey)
      accessToken <- IO(JwtCirce.encode(header.toJson, payload, privateKey, algorithm))
    yield accessToken

  private def rs256JwtForClient(clientId: String, user: Option[UserInfo], nonce: Option[String])
                                     (using Logger[IO]): IO[String] =
    for
      realTime <- IO.realTime
      algorithm = JwtAlgorithm.RS256
      header = JwtHeader(algorithm.some, JwtHeader.DEFAULT_TYPE.some, none, rsaKey.kid)
      payload = createPayload(user, none, clientId, realTime, none, nonce)
      key <- rsaPrivateKey(rsaKey)
      idToken <- IO(JwtCirce.encode(header.toJson, payload, key, algorithm))
      _ <- info"Issuing ID token $idToken"
    yield idToken

  private def createPayload(user: Option[UserInfo], scope: Option[Set[String]], audience: String,
                                  issuedAt: Duration, jwtId: Option[String], nonce: Option[String]): String =
    val issuedAtSec: Long = issuedAt.toSeconds
    IdToken(
      content = Some(scope.map(s => TokenScope(s).asJson.noSpaces).getOrElse("{}")),
      issuer = authorizationServerIndex.toString.some,
      subject = user.flatMap(_.sub),
      audience = Set(audience).some,
      expiration = (issuedAtSec + (5 * 60)).some,
      issuedAt = issuedAtSec.some,
      jwtId = jwtId,
      nonce = nonce
    ).asJson.deepDropNullValues.noSpaces

  private val rsaKeyPair = IO {
    val kpGen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
    kpGen.initialize(2048)
    val kp: KeyPair = kpGen.generateKeyPair()
    val sk: RSAPrivateCrtKey = kp.getPrivate.asInstanceOf[RSAPrivateCrtKey]
    val pk: RSAPublicKey = kp.getPublic.asInstanceOf[RSAPublicKey]
    val rsaSk = RsaKey(None, encodeToBase64Url(sk.getPrivateExponent).some, encodeToBase64Url(sk.getPublicExponent),
      encodeToBase64Url(sk.getModulus), sk.getAlgorithm, None, encodeToBase64Url(sk.getPrimeP).some,
      encodeToBase64Url(sk.getPrimeQ).some)
    val rsaPk = RsaKey(None, None, encodeToBase64Url(pk.getPublicExponent), encodeToBase64Url(pk.getModulus),
      pk.getAlgorithm, None, None, None)
    (rsaSk, rsaPk)
  }

  private def encodeToBase64Url(decoded: BigInteger): String =
    Stream(decoded.toByteArray*).through(base64.encodeWithAlphabet(Base64Url)).toList.mkString("")

  private def getClient(clientId: String, clientsR: Ref[IO, Seq[ClientInfo]]): IO[Option[ClientInfo]] =
    clientsR.get.map(clients => clients.find(_.id == clientId))

  private def getProtectedResource(resourceId: String): Option[ProtectedResource] =
    protectedResources.find(_.resourceId == resourceId)

  private def getUser(username: String): Option[UserInfo] = userInfos.values.find(_.username.contains(username))

  private def getScope[T](params: T)(using getter: Getter[Option, T, String, String]): Option[Set[String]] =
    given Getter[Option, T, String, Set[String]] = getter.andThen(_.split("\\s++").toSet[String].some)
    params.fGet[Option, String, Set[String]](scopeKey)

  private def invalidResp(error: String): IO[Response[IO]] =
    Ok(ErrorInfo(error).asJson).map(_.copy(status = Status.Unauthorized))

  private val invalidClientResp: IO[Response[IO]] = invalidResp("invalid_client")

  private val invalidGrantResp: IO[Response[IO]] = invalidResp("invalid_grant")

  private val unauthorizedBasicResp: IO[Response[IO]] =
    Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Basic.toString, authorizationServerAddr)))

  private val unauthorizedBearerResp: IO[Response[IO]] =
    Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, authorizationServerAddr)))
end AuthorizationServerApp
