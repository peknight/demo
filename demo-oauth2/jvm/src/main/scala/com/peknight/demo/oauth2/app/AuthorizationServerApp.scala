package com.peknight.demo.oauth2.app

import cats.data.{Chain, NonEmptyList, OptionT}
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.either.*
import cats.syntax.option.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.page.AuthorizationServerPage
import com.peknight.demo.oauth2.random.*
import com.peknight.demo.oauth2.repository.*
import fs2.Stream
import fs2.text.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.http4s.scalatags.*
import org.http4s.syntax.literals.uri
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim, JwtHeader}
import scodec.bits.Bases.Alphabets.Base64Url

import java.security.spec.RSAPrivateKeySpec
import java.security.{KeyFactory, PrivateKey}
import scala.concurrent.duration.*

object AuthorizationServerApp extends IOApp.Simple :

  //noinspection HttpUrlsUsage
  val run: IO[Unit] =
    for
      requestsR <- Ref.of[IO, Map[String, AuthorizeParam]](Map.empty)
      codesR <- Ref.of[IO, Map[String, AuthorizeCodeCache]](Map.empty)
      clientsR <- Ref.of[IO, Seq[ClientInfo]](clients)
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8001"
      _ <- clearRecord
      _ <- insertRecord(OAuthTokenRecord("oauth-client-1", None, "j2r3oj32r23rmasd98uhjrk2o3i".some,
        Set("foo", "bar").some, None)).timeout(5.seconds)
      _ <- start[IO](serverPort)(service(random, requestsR, codesR, clientsR).orNotFound)
      _ <- info"OAuth Authorization Server is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

  def service(random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]],
              codesR: Ref[IO, Map[String, AuthorizeCodeCache]], clientsR: Ref[IO, Seq[ClientInfo]])
             (using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => clientsR.get.flatMap(clients => Ok(AuthorizationServerPage.Text.index(authServer, clients)))
    case GET -> Root / "authorize" :? AuthorizeParam(authorizeParamValid) => authorizeParamValid.fold(
      msg => Ok(AuthorizationServerPage.Text.error(msg)), param => authorize(param, random, requestsR, clientsR)
    )
    case req @ POST -> Root / "approve" => approve(req, random, requestsR, codesR, clientsR)
    case req @ POST -> Root / "token" => token(req, random, codesR, clientsR)
    case req @ POST -> Root / "revoke" => revoke(req, clientsR)
    case req @ POST -> Root / "introspect" => introspect(req)
    case req @ POST -> Root / "register" => postRegister(req, random, clientsR)
    case req @ GET -> Root / "register" / clientId => getRegister(clientId, req, clientsR)
    case req @ PUT -> Root / "register" / clientId => putRegister(clientId, req, clientsR)
    case req @ DELETE -> Root / "register" / clientId => deleteRegister(clientId, req, clientsR)
  }

  def authorize(param: AuthorizeParam, random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]],
                clientsR: Ref[IO, Seq[ClientInfo]])(using Logger[IO]): IO[Response[IO]] =
    getClient(param.clientId, clientsR).flatMap {
      case None => info"Unknown client ${param.clientId}" *> Ok(AuthorizationServerPage.Text.error("Unknown client"))
      case Some(client) => client.redirectUris.find(_ == param.redirectUri) match
        case None =>
          info"Mismatched redirect URI, expected ${client.redirectUris.toList.mkString(" ")} got ${param.redirectUri}" *>
            Ok(AuthorizationServerPage.Text.error("Invalid redirect URI"))
        case Some(redirectUri) =>
          if param.scope.diff(client.scope).nonEmpty then
            Found(Location(redirectUri.withQueryParam("error", "invalid_scope")))
          else
            for
              reqId <- randomString[IO](random, 8)
              _ <- requestsR.update(_ + (reqId -> param))
              resp <- Ok(AuthorizationServerPage.Text.approve(client, reqId, param.scope.toList))
            yield resp
    }
  end authorize

  def approve(req: Request[IO], random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]],
              codesR: Ref[IO, Map[String, AuthorizeCodeCache]], clientsR: Ref[IO, Seq[ClientInfo]])
             (using Logger[IO]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body => body.getParam("reqid") match
      case None => Ok(AuthorizationServerPage.Text.error("No matching authorization request"))
      case Some(reqId) => requestsR.modify(requests => (requests.removed(reqId), requests.get(reqId))).flatMap {
        case None => Ok(AuthorizationServerPage.Text.error("No matching authorization request"))
        case Some(query) => body.get("approve").find(_ == "Approve") match
          case Some(_) => query.responseType match
            case ResponseType.Code => checkScope(body, query, clientsR) { scope =>
              val user = body.getParam("user")
              for
                code <- randomString[IO](random, 8)
                _ <- codesR.update(_ + (code -> AuthorizeCodeCache(query, scope, user)))
                resp <- Found(Location(query.redirectUri
                  .withQueryParam("code", code)
                  .withOptionQueryParam("state", query.state)
                ))
              yield resp
            }
            case ResponseType.Token => checkScope(body, query, clientsR) { scope =>
              val user = body.getParam("user")
              user.flatMap(userInfos.get).fold[IO[Response[IO]]] {
                val username = user.getOrElse("None")
                info"Unknown user $username" *>
                  InternalServerError(AuthorizationServerPage.Text.error(s"Unknown user $username"))
              } { userInfo =>
                for
                  _ <- info"User $userInfo"
                  tokenResponse <- generateTokens(random, query.clientId, userInfo.some, scope, query.state,
                    false, true)
                  resp <- Found(Location(query.redirectUri.withFragment(tokenResponse.toFragment)))
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

  def token(req: Request[IO], random: Random[IO], codesR: Ref[IO, Map[String, AuthorizeCodeCache]],
            clientsR: Ref[IO, Seq[ClientInfo]])(using Logger[IO]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body => checkAuthorization(req, body, clientsR) { client =>
      body.getValue("grant_type")(GrantType.fromString) match
        case Some(GrantType.AuthorizationCode) => authorizationCode(client.id, body, random, codesR)
        case Some(GrantType.ClientCredentials) => clientCredentials(client, body, random)
        case Some(GrantType.RefreshToken) => refreshToken(client.id, body, random)
        case Some(GrantType.Password) => password(client, body, random)
        case grantType => info"Unknown grant type ${grantType.getOrElse("None")}" *>
          Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, authorizationServerAddr)))
    }}
  end token

  def authorizationCode(clientId: String, body: UrlForm, random: Random[IO],
                        codesR: Ref[IO, Map[String, AuthorizeCodeCache]])(using Logger[IO]): IO[Response[IO]] =
    for
      codeCache <- body.getParam("code") match
        case Some(code) => codesR.modify(codes => (codes.removed(code), code.some -> codes.get(code)))
        case None => IO((none[String], none[AuthorizeCodeCache]))
      resp <- codeCache match
        case (Some(code), Some(cache)) if cache.authorizationEndpointRequest.clientId == clientId =>
          cache.user.flatMap(userInfos.get) match
            case Some(user) =>
              for
                _ <- info"User $user"
                tokenResponse <- generateTokens(random, clientId, cache.user.flatMap(userInfos.get), cache.scope, None,
                  true, true)
                _ <- info"Issued tokens for code $code"
                resp <- Ok(tokenResponse.asJson)
              yield resp
            case None =>
              val preferredUsername = cache.user.getOrElse("None")
              info"Unknown user $preferredUsername" *>
                InternalServerError(AuthorizationServerPage.Text.error(s"Unknown user $preferredUsername"))
        case (_, Some(cache)) =>
          info"Client mismatch, expected ${cache.authorizationEndpointRequest.clientId} got $clientId" *> invalidGrantResp
        case (code, _) => info"Unknown code, ${code.getOrElse("None")}" *>
          BadRequest(ErrorInfo("invalid_grant").asJson)
    yield resp

  def clientCredentials(client: ClientInfo, body: UrlForm, random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    checkScope(body, client) { scope =>
      for
        tokenResponse <- generateTokens(random, client.id, None, scope, None, false, false)
        resp <- Ok(tokenResponse.asJson)
      yield resp
    }

  def refreshToken(clientId: String, body: UrlForm, random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    for
      refreshTokenRecord <- body.getParam("refresh_token") match
        case r@Some(refreshToken) => getRecordByRefreshToken(refreshToken).map(r -> _)
        case None => IO((none[String], none[OAuthTokenRecord]))
      resp <- refreshTokenRecord match
        case (Some(refreshToken), Some(record)) if record.clientId != clientId =>
          info"Invalid client using a refresh token, expected ${record.clientId} got $clientId" *>
            removeRecordByRefreshToken(refreshToken) *>
            BadRequest(ErrorInfo("invalid_grant").asJson)
        case (Some(refreshToken), Some(record)) =>
          for
            _ <- info"We found a matching refresh token $refreshToken"
            accessToken <- randomString[IO](random, 32)
            _ <- insertRecord(OAuthTokenRecord(clientId, accessToken.some, None, record.scope, record.user))
            _ <- info"Issuing access token $accessToken for refresh token $refreshToken"
            resp <- Ok(OAuthToken(accessToken, AuthScheme.Bearer, body.getParam("refresh_token"),
              None, None, None).asJson)
          yield resp
        case _ => info"No matching token was found." *> invalidGrantResp
    yield resp

  def password(client: ClientInfo, body: UrlForm, random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    val usernameOption = body.getParam("username")
    val userOption = usernameOption.flatMap(getUser)
    userOption match
      case None => info"Unknown user ${usernameOption.getOrElse("None")}" *> invalidGrantResp
      case Some(user) =>
        for
          _ <- info"user is $user"
          password = body.getParam("password")
          resp <- password.filter(user.password.contains).fold[IO[Response[IO]]] {
            val bodyPwd = password.getOrElse("None")
            val userPwd = user.password.getOrElse("None")
            info"Mismatched resource owner password, expected $userPwd got $bodyPwd" *> invalidGrantResp
          } { _ => checkScope(body, client) { scope =>
            for
              tokenResponse <- generateTokens(random, client.id, userOption, scope, None, true, true)
              resp <- Ok(tokenResponse.asJson)
            yield resp
          }}
        yield resp

  def revoke(req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]])(using Logger[IO]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body => checkAuthorization(req, body, clientsR) { client =>
      body.getParam("token").fold(NoContent()) { inToken =>
        for
          count <- removeRecordByAccessTokenAndClientId(inToken, client.id)
          _ <- info"Removed $count tokens"
          resp <- NoContent()
        yield resp
      }
    }}

  def introspect(req: Request[IO])(using Logger[IO]): IO[Response[IO]] =
    req.headers.get[Authorization] match
      case Some(Authorization(BasicCredentials((resourceId, resourceSecret)))) => getProtectedResource(resourceId) match
        case None => info"Unknown resource $resourceId" *> unauthorizedBasicResp
        case Some(resource) if resource.resourceSecret != resourceSecret =>
          info"Mismatched secret, expected ${resource.resourceSecret} got $resourceSecret" *> unauthorizedBasicResp
        case _ =>
          val recordOptionT: OptionT[IO, OAuthTokenRecord] =
            for
              body <- req.as[UrlForm].optionT
              inToken <- OptionT(IO.pure(body.getParam("token")))
              record <- OptionT(getRecordByAccessToken(inToken))
            yield record
          for
            recordOption <- recordOptionT.value
            resp <- recordOption match
              case Some(record) => info"We found a matching token: ${record.accessToken.getOrElse("None")}" *>
                Ok(IntrospectionResponse(true, uri"http://localhost:8001/".some, record.user, record.scope,
                  record.clientId.some).asJson)
              case _ => info"No matching token was found" *>
                Ok(IntrospectionResponse(false, None, None, None, None).asJson)
          yield resp
      case _ => info"Unknown resource None" *> unauthorizedBasicResp

  def postRegister(req: Request[IO], random: Random[IO], clientsR: Ref[IO, Seq[ClientInfo]]): IO[Response[IO]] =
    req.as[UrlForm].flatMap { body => checkClientMetadata(body) match
      case Left(error) => BadRequest(ErrorInfo(error).asJson)
      case Right(clientMetadata) =>
        for
          clientId <- randomString(random, 32)
          clientSecretOption <-
            if Seq(AuthMethod.ClientSecretBasic, AuthMethod.ClientSecretPost).contains(clientMetadata.tokenEndpointAuthMethod) then
              randomString(random, 32).map(_.some)
            else IO.pure(none[String])
          realTime <- IO.realTime
          clientIdCreatedAt = realTime.toSeconds
          clientSecretExpiresAt = 0L
          registrationAccessToken <- randomString(random, 32)
          registrationClientUri = Uri.unsafeFromString(s"http://localhost:8001/register/$clientId")
          client = clientMetadata.toClientInfo(clientId, clientSecretOption, clientIdCreatedAt,
            clientSecretExpiresAt, registrationAccessToken, registrationClientUri)
          _ <- clientsR.update(_ :+ client)
          resp <- Created(client.asJson)
        yield resp
    }

  def getRegister(clientId: String, req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]]): IO[Response[IO]] =
    validateConfigurationEndpointRequest(clientId, req, clientsR)(client => Ok(client.asJson))

  def putRegister(clientId: String, req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]]): IO[Response[IO]] =
    validateConfigurationEndpointRequest(clientId, req, clientsR) { client => req.as[UrlForm].flatMap { body =>
      if !body.getParam(clientIdKey).contains(client.id) then
        BadRequest(ErrorInfo("invalid_client_metadata").asJson)
      else if body.getParam(clientSecretKey).exists(!client.secret.contains(_)) then
        BadRequest(ErrorInfo("invalid_client_metadata").asJson)
      else checkClientMetadata(body) match
        case Left(error) => BadRequest(ErrorInfo(error).asJson)
        case Right(clientMetadata) =>
          val updatedClient = clientMetadata.updateClientInfo(client)
          clientsR.update(clients => clients.filter(_.id == client.id) :+ updatedClient) *> Ok(updatedClient.asJson)
    }}

  def deleteRegister(clientId: String, req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]])
                    (using Logger[IO]): IO[Response[IO]] =
    validateConfigurationEndpointRequest(clientId, req, clientsR) { client =>
      for
        _ <- clientsR.update(_.filter(_.id == client.id))
        count <- removeRecordByClientId(client.id)
        _ <- if count > 0 then info"Removed $count tokens" else IO.unit
        resp <- NoContent()
      yield resp
    }

  def userInfoEndpoint(req: Request[IO])(using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, authorizationServerAddr) { record =>
      if !record.scope.exists(_.contains("openid")) then Forbidden() else
        record.user.flatMap(userInfos.get).fold(NotFound()) { user =>
          ???
        }
    }

  def checkAuthorization(req: Request[IO], body: UrlForm, clientsR: Ref[IO, Seq[ClientInfo]])
                        (f: ClientInfo => IO[Response[IO]])(using Logger[IO]): IO[Response[IO]] =
    val (hClientId, hClientSecret) = req.headers.get[Authorization] match
      case Some(Authorization(BasicCredentials((clientId, clientSecret)))) =>
        (Uri.decode(clientId).some, Uri.decode(clientSecret).some)
      case _ => (none[String], none[String])
    val bClientId = body.getParam("client_id")
    val bClientSecret = body.getParam("client_secret")
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

  def generateTokens(random: Random[IO], clientId: String, user: Option[UserInfo], scope: Set[String],
                     state: Option[String], generateRefreshToken: Boolean, generateIdToken: Boolean)
                    (using Logger[IO]): IO[OAuthToken] =
    for
      accessToken <- randomString(random, 32)
      _ <- insertRecord(OAuthTokenRecord(clientId, accessToken.some, None, scope.some, user.flatMap(_.preferredUsername)))
      _ <- info"Issuing accessToken $accessToken"
      refreshTokenOption <-
        if generateRefreshToken then
          for
            refresh <- randomString(random, 32)
            refreshOption = refresh.some
            _ <- insertRecord(OAuthTokenRecord(clientId, None, refreshOption, scope.some, user.flatMap(_.preferredUsername)))
            _ <- info"and refresh token $refresh"
          yield refreshOption
        else IO.pure(None)
      _ <- info"with scope ${scope.mkString(" ")}"
      idTokenOption <-
        if generateIdToken then
          for
            realTime <- IO.realTime
            idToken <- sign(clientId, user, realTime)
            _ <- info"Issuing ID token $idToken"
          yield idToken.some
        else IO.pure(None)
    yield OAuthToken(accessToken, AuthScheme.Bearer, refreshTokenOption, scope.some, state, idTokenOption)

  def sign(clientId: String, user: Option[UserInfo], issueAt: Duration): IO[String] =
    val issueAtSec: Long = issueAt.toSeconds
    val header = JwtHeader(JwtAlgorithm.RS256.some, JwtHeader.DEFAULT_TYPE.some, none, "authserver".some)
    val payload = JwtClaim(
      issuer = uri"http://localhost:8001/".toString.some,
      subject = user.flatMap(_.sub),
      audience = Set(clientId).some,
      expiration = (issueAtSec + (5 * 60)).some,
      issuedAt = issueAtSec.some
    )
    rsaPrivateKey.flatMap(key => IO(JwtCirce.encode(header, payload, key)))

  def checkClientMetadata(body: UrlForm): Either[String, ClientMetadata] =
    val tokenEndpointAuthMethod = body.getValue(tokenEndpointAuthMethodKey)(AuthMethod.fromString)
      .getOrElse(AuthMethod.SecretBasic)
    if !Seq(AuthMethod.SecretBasic, AuthMethod.SecretPost, AuthMethod.None).contains(tokenEndpointAuthMethod) then
      "invalid_client_metadata".asLeft
    else
      val bodyGrantTypes: List[GrantType] = body.getListParam(grantTypesKey).mapOption(GrantType.fromString)
      val bodyResponseTypes: List[ResponseType] = body.getListParam(responseTypesKey).mapOption(ResponseType.fromString)
      val (grantTypes, responseTypes) = (bodyGrantTypes, bodyResponseTypes) match
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
        NonEmptyList.fromList(body.getListParam(redirectUrisKey)
          .foldRight(List.empty[Uri])((uri, list) => Uri.fromString(uri).fold(_ => list, _ :: list))) match
          case None => "invalid_redirect_uri".asLeft
          case Some(redirectUris) =>
            val clientName: Option[String] = body.getParam(clientNameKey)
            val clientUri: Option[Uri] = body.getUri(clientUriKey)
            val logoUri: Option[Uri] = body.getUri(logoUriKey)
            val scope: Option[Set[String]] = getScope(body)
            ClientMetadata(tokenEndpointAuthMethod, grantTypes, responseTypes, redirectUris, clientName, clientUri,
              logoUri, scope).asRight
  end checkClientMetadata

  def validateConfigurationEndpointRequest(clientId: String, req: Request[IO], clientsR: Ref[IO, Seq[ClientInfo]])
                                          (f: ClientInfo => IO[Response[IO]]): IO[Response[IO]] =
    getClient(clientId, clientsR).flatMap {
      case None => NotFound()
      case Some(client) => req.headers.get[Authorization] match
        case Some(Authorization(Credentials.Token(AuthScheme.Bearer, token))) if client.registrationAccessToken.contains(token) =>
          f(client)
        case Some(Authorization(Credentials.Token(AuthScheme.Bearer, _))) => Forbidden()
        case _ => unauthorizedBearerResp
    }

  def checkScope(body: UrlForm, query: AuthorizeParam, clientsR: Ref[IO, Seq[ClientInfo]])
                (resp: Set[String] => IO[Response[IO]]): IO[Response[IO]] =
    val scope = body.values.keys.filter(_.startsWith("scope_")).map(_.substring("scope_".length)).toSet[String]
    getClient(query.clientId, clientsR).flatMap { clientOption =>
      val cScope = clientOption.map(_.scope).getOrElse(Set.empty[String])
      if scope.diff(cScope).nonEmpty then
        Found(Location(query.redirectUri.withQueryParam("error", "invalid_scope")))
      else resp(scope)
    }

  def checkScope(body: UrlForm, client: ClientInfo)(resp: Set[String] => IO[Response[IO]]): IO[Response[IO]] =
    val scope = getScope(body).getOrElse(Set.empty[String])
    if scope.diff(client.scope).nonEmpty then BadRequest(ErrorInfo("invalid_scope").asJson)
    else resp(scope)

  def decodeToBigInt(base64UrlEncoded: String): IO[BigInt] =
    Stream(base64UrlEncoded)
      .covary[IO]
      .through(base64.decodeWithAlphabet(Base64Url))
      .compile.toList
      .map(bytes => BigInt(1, bytes.toArray))

  val rsaPrivateKey: IO[PrivateKey] =
    for
      modulus <- decodeToBigInt(rsaKey.n)
      privateExponent <- decodeToBigInt(rsaKey.d)
      key <- IO(KeyFactory.getInstance("RSA").generatePrivate(RSAPrivateKeySpec(
        modulus.bigInteger, privateExponent.bigInteger
      )))
    yield key

  def getClient(clientId: String, clientsR: Ref[IO, Seq[ClientInfo]]): IO[Option[ClientInfo]] =
    clientsR.get.map(clients => clients.find(_.id == clientId))

  def getProtectedResource(resourceId: String): Option[ProtectedResource] =
    protectedResources.find(_.resourceId == resourceId)

  def getUser(username: String): Option[UserInfo] = userInfos.values.find(_.username.contains(username))

  def getScope(body: UrlForm): Option[Set[String]] =
    body.getParam("scope").map(_.split("\\s++").toSet[String])

  def invalidResp(error: String): IO[Response[IO]] = Ok(ErrorInfo(error).asJson).map(_.copy(status = Status.Unauthorized))

  val invalidClientResp: IO[Response[IO]] = invalidResp("invalid_client")

  val invalidGrantResp: IO[Response[IO]] = invalidResp("invalid_grant")

  val unauthorizedBasicResp: IO[Response[IO]] =
    Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Basic.toString, authorizationServerAddr)))

  val unauthorizedBearerResp: IO[Response[IO]] =
    Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, authorizationServerAddr)))
