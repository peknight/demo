package com.peknight.demo.oauth2.authorizationserver

import cats.data.{NonEmptyList, OptionT}
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.either.*
import cats.syntax.option.*
import cats.syntax.traverse.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.*
import com.peknight.demo.oauth2.domain.*
import fs2.io.file.Files
import fs2.io.file.Flags.Append
import fs2.{Stream, text}
import io.circe.fs2.*
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

import scala.concurrent.duration.*
import scala.util.Try

object AuthorizationServerApp extends IOApp.Simple:

  val authServer: AuthServerInfo = AuthServerInfo()

  val clients: Seq[ClientInfo] = Seq(
    ClientInfo(
      "oauth-client-1",
      "oauth-client-secret-1",
      Set("foo", "bar", "read", "write", "delete"),
      NonEmptyList.one(uri"http://localhost:8000/callback")
    )
  )

  val userInfos: Map[String, UserInfo] = Map(
    "alice" -> UserInfo(
      "9XE3-JI34-00132A",
      "alice",
      "Alice",
      "alice.wonderland@example.com",
      true
    ),
    "bob" -> UserInfo(
      "1ZT5-OE63-57383B",
      "bob",
      "Bob",
      "bob.loblob@example.net",
      false
    ),
    "carol" -> UserInfo(
      "F5Q1-L6LGG-959FS",
      "carol",
      "Carol",
      "carol.lewis@example.net",
      true,
      "clewis".some,
      "user password!".some
    )
  )

  //noinspection HttpUrlsUsage
  val run: IO[Unit] =
    for
      requestsR <- Ref.of[IO, Map[String, AuthorizeParam]](Map.empty)
      codesR <- Ref.of[IO, Map[String, AuthorizeCodeCache]](Map.empty)
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8001"
      _ <- clearRecord
      _ <- insertRecord(OAuthTokenRecord("oauth-client-1", None, "j2r3oj32r23rmasd98uhjrk2o3i".some,
        Set("foo", "bar").some, None)).timeout(5.seconds)
      _ <- start[IO](serverPort)(service(random, requestsR, codesR).orNotFound)
      _ <- info"OAuth Authorization Server is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()


  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[CIString, AuthScheme] = CanEqual.derived
  given CanEqual[Uri, Uri] = CanEqual.derived

  def service(random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]],
              codesR: Ref[IO, Map[String, AuthorizeCodeCache]])(using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(AuthorizationServerPage.index(authServer, clients))
    case GET -> Root / "authorize" :? AuthorizeParam(authorizeParamValid) => authorizeParamValid.fold(
      msg => Ok(AuthorizationServerPage.error(msg)), param => authorize(param, random, requestsR)
    )
    case req @ POST -> Root / "approve" => req.as[UrlForm].flatMap(body => approve(body, random, requestsR, codesR))
    case req @ POST -> Root / "token" => req.as[UrlForm].flatMap(body => token(req, body, random, codesR))
  }

  def authorize(param: AuthorizeParam, random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]])
               (using Logger[IO]): IO[Response[IO]] =
    getClient(param.clientId) match
      case None => info"Unknown client ${param.clientId}" *> Ok(AuthorizationServerPage.error("Unknown client"))
      case Some(client) => client.redirectUris.find(_ == param.redirectUri) match
        case None =>
          info"Mismatched redirect URI, expected ${client.redirectUris.toList.mkString(" ")} got ${param.redirectUri}" *>
            Ok(AuthorizationServerPage.error("Invalid redirect URI"))
        case Some(redirectUri) =>
          if param.scope.diff(client.scope).nonEmpty then
            Found(Location(redirectUri.withQueryParam("error", "invalid_scope")))
          else
            for
              reqId <- randomString[IO](random, 8)
              _ <- requestsR.update(_ + (reqId -> param))
              resp <- Ok(AuthorizationServerPage.approve(client, reqId, param.scope.toList))
            yield resp
  end authorize

  def approve(body: UrlForm, random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]],
              codesR: Ref[IO, Map[String, AuthorizeCodeCache]])(using Logger[IO]): IO[Response[IO]] =
    body.get("reqid").find(_.nonEmpty) match
      case None => Ok(AuthorizationServerPage.error("No matching authorization request"))
      case Some(reqId) => requestsR.modify(requests => (requests.removed(reqId), requests.get(reqId))).flatMap {
        case None => Ok(AuthorizationServerPage.error("No matching authorization request"))
        case Some(query) => body.get("approve").find(_ == "Approve") match
          case Some(_) =>
            def checkScope(resp: Set[String] => IO[Response[IO]]): IO[Response[IO]] =
              val scope = body.values.keys.filter(_.startsWith("scope_")).map(_.substring("scope_".length)).toSet[String]
              val cScope = getClient(query.clientId).map(_.scope).getOrElse(Set.empty[String])
              if scope.diff(cScope).nonEmpty then
                Found(Location(query.redirectUri.withQueryParam("error", "invalid_scope")))
              else resp(scope)
            end checkScope
            query.responseType match
              case ResponseType.Code => checkScope { scope =>
                val user = body.get("user").find(_.nonEmpty)
                for
                  code <- randomString[IO](random, 8)
                  _ <- codesR.update(_ + (code -> AuthorizeCodeCache(query, scope, user)))
                  resp <- Found(Location(query.redirectUri
                    .withQueryParam("code", code)
                    .withOptionQueryParam("state", query.state)
                  ))
                yield resp
              }
              case ResponseType.Token => checkScope { scope =>
                val user = body.get("user").find(_.nonEmpty)
                val userInfoOption = user.flatMap(userInfos.get)
                userInfoOption match
                  case None => info"Unknown user ${user.getOrElse("None")}" *>
                    Ok(AuthorizationServerPage.error(s"Unknown user ${user.getOrElse("None")}"))
                      .map(_.copy(status = Status.InternalServerError))
                  case Some(userInfo) =>
                    for
                      _ <- info"User $userInfo"
                      tokenResponse <- generateTokens(random, query.clientId, userInfoOption, scope, query.state,
                        false)
                      resp <- Found(Location(query.redirectUri.withFragment(tokenResponse.toFragment)))
                    yield resp
              }
              // 后续支持其它响应类型扩展
              case null => Found(Location(query.redirectUri
                .withQueryParam("error", "unsupported_response_type")
            ))
          case None => Found(Location(query.redirectUri.withQueryParam("error", "access_denied")))
      }
  end approve

  val invalidClientResp: IO[Response[IO]] = invalidResp("invalid_client")

  def token(req: Request[IO], body: UrlForm, random: Random[IO], codesR: Ref[IO, Map[String, AuthorizeCodeCache]])
           (using Logger[IO]): IO[Response[IO]] =
    val (hClientId, hClientSecret) = req.headers.get[Authorization] match
      case Some(Authorization(BasicCredentials((clientId, clientSecret)))) =>
        (Uri.decode(clientId).some, Uri.decode(clientSecret).some)
      case _ => (none[String], none[String])
    val bClientId = body.get("client_id").find(_.nonEmpty)
    val bClientSecret = body.get("client_secret").find(_.nonEmpty)
    (hClientId, bClientId) match
      // if we've already seen the client's credentials in the authorization header, this is an error
      case (Some(_), Some(_)) => info"Client attempted to authenticate with multiple methods" *> invalidClientResp
      case (None, None) => info"Unknown client" *> invalidClientResp
      case _ =>
        val clientId = hClientId.orElse(bClientId).get
        val clientSecret = hClientSecret.orElse(bClientSecret).getOrElse("")
        getClient(clientId) match
          case None => info"Unknown client $clientId" *> invalidClientResp
          case Some(client) if client.secret != clientSecret =>
            info"Mismatched client secret, expected ${client.secret} got $clientSecret" *> invalidClientResp
          case Some(client) => body.get("grant_type").map(GrantType.fromString).find(_.isDefined).flatten match
            case Some(GrantType.AuthorizationCode) => authorizationCode(clientId, body, random, codesR)
            case Some(GrantType.ClientCredentials) => clientCredentials(client, body, random)
            case Some(GrantType.RefreshToken) => refreshToken(clientId, body, random)
            case Some(GrantType.Password) => password(clientId, body, random)
            case grantType => info"Unknown grant type ${grantType.getOrElse("None")}" *>
              Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "Grant Type")))
  end token

  def authorizationCode(clientId: String, body: UrlForm, random: Random[IO],
                        codesR: Ref[IO, Map[String, AuthorizeCodeCache]])(using Logger[IO]): IO[Response[IO]] =
    for
      codeCache <- body.get("code").find(_.nonEmpty) match
        case Some(code) => codesR.modify(codes => (codes.removed(code), code.some -> codes.get(code)))
        case None => IO((none[String], none[AuthorizeCodeCache]))
      resp <- codeCache match
        case (Some(code), Some(cache)) =>
          for
            tokenResponse <- generateTokens(random, clientId, cache.user.flatMap(userInfos.get), cache.scope, None,
              true)
            _ <- info"Issued tokens for code $code"
            resp <- Ok(tokenResponse.asJson)
          yield resp
        case (code, _) => info"Unknown code, ${code.getOrElse("None")}" *>
          BadRequest(ErrorInfo("invalid_grant").asJson)
    yield resp

  def clientCredentials(client: ClientInfo, body: UrlForm, random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    val scope = getScope(body)
    if scope.diff(client.scope).nonEmpty then
      BadRequest(ErrorInfo("invalid_scope").asJson)
    else
      for
        tokenResponse <- generateTokens(random, client.id, None, scope, None, false)
        resp <- Ok(tokenResponse.asJson)
      yield resp

  def refreshToken(clientId: String, body: UrlForm, random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    for
      refreshTokenRecord <- body.get("refresh_token").find(_.nonEmpty) match
        case r @ Some(refreshToken) => getRecordByRefreshToken(refreshToken).map(r -> _)
        case None => IO((none[String], none[OAuthTokenRecord]))
      resp <- refreshTokenRecord match
        case (Some(refreshToken), Some(record)) if record.clientId != clientId =>
          info"Invalid client using a refresh token, expected ${record.clientId} got $clientId" *>
            removeRecordByRefreshToken(refreshToken) *>
            BadRequest()
        case (Some(refreshToken), Some(_)) =>
          for
            _ <- info"We found a matching refresh token $refreshToken"
            accessToken <- randomString[IO](random, 32)
            _ <- insertRecord(OAuthTokenRecord(clientId, accessToken.some, None, None, None))
            _ <- info"Issuing access token $accessToken for refresh token $refreshToken"
            resp <- Ok(OAuthToken(accessToken, AuthScheme.Bearer, body.get("refresh_token").find(_.nonEmpty),
              None, None).asJson)
          yield resp
        case _ => info"No matching token was found." *>
          Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "Refresh Token")))
    yield resp

  val invalidGrantResp: IO[Response[IO]] = invalidResp("invalid_grant")

  def password(clientId: String, body: UrlForm, random: Random[IO])(using Logger[IO]): IO[Response[IO]] =
    val usernameOption = body.get("username").find(_.nonEmpty)
    val userOption = usernameOption.flatMap(getUser)
    userOption match
      case None => info"Unknown user ${usernameOption.getOrElse("None")}" *> invalidGrantResp
      case Some(user) =>
        for
          _ <- info"user is $user"
          password = body.get("password").find(_.nonEmpty)
          resp <-
            if password.exists(user.password.contains) then
              for
                tokenResponse <- generateTokens(random, clientId, userOption, getScope(body), None, false)
                resp <- Ok(tokenResponse.asJson)
              yield resp
            else info"Mismatched resource owner password, expected ${user.password.getOrElse("None")} got ${password.getOrElse("None")}" *>
              invalidGrantResp
        yield resp

  def generateTokens(random: Random[IO], clientId: String, user: Option[UserInfo], scope: Set[String],
                     state: Option[String], generateRefreshToken: Boolean)(using Logger[IO]): IO[OAuthToken] =
    for
      accessToken <- randomString(random, 32)
      _ <- insertRecord(OAuthTokenRecord(clientId, accessToken.some, None, scope.some, user))
      _ <- info"Issuing accessToken $accessToken"
      refreshTokenOption <-
        if generateRefreshToken then
          for
            refresh <- randomString(random, 32)
            refreshOption = refresh.some
            _ <- insertRecord(OAuthTokenRecord(clientId, None, refreshOption, scope.some, user))
            _ <- info"and refresh token $refresh"
          yield refreshOption
        else IO.pure(None)
      _ <- info"with scope ${scope.mkString(" ")}"
    yield OAuthToken(accessToken, AuthScheme.Bearer, refreshTokenOption, scope.some, state)

  def getClient(clientId: String): Option[ClientInfo] = clients.find(_.id == clientId)

  def getUser(username: String): Option[UserInfo] = userInfos.values.find(_.username.contains(username))

  def getScope(body: UrlForm): Set[String] =
    body.get("scope").find(_.nonEmpty).map(_.split("\\s++").toSet[String]).getOrElse(Set.empty[String])

  def invalidResp(error: String): IO[Response[IO]] = Ok(ErrorInfo(error).asJson).map(_.copy(status = Status.Unauthorized))
