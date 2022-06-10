package com.peknight.demo.oauth2.authorizationserver

import cats.data.OptionT
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.either.*
import cats.syntax.option.*
import cats.syntax.traverse.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.*
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

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[CIString, AuthScheme] = CanEqual.derived
  given CanEqual[Uri, Uri] = CanEqual.derived

  val authServer = AuthServerInfo(uri"http://localhost:8001/authorize", uri"http://localhost:8001/token")

  val clients = Seq(
    ClientInfo(
      "oauth-client-1",
      "oauth-client-secret-1",
      Set("foo", "bar"),
      List(uri"http://localhost:8000/callback")
    )
  )

  def getClient(clientId: String): Option[ClientInfo] = clients.find(_.id == clientId)

  def authorize(param: AuthorizeParam, random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]])
               (using Logger[IO]) =
    getClient(param.clientId) match
      case None => info"Unknown client ${param.clientId}" *> Ok(AuthorizationServerPage.error("Unknown client"))
      case Some(client) => client.redirectUris.find(_ == param.redirectUri) match
        case None =>
          info"Mismatched redirect URI, expected ${client.redirectUris.mkString(" ")} got ${param.redirectUri}" *>
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
              codesR: Ref[IO, Map[String, AuthorizeCodeCache]]) =
    body.get("reqid").find(_.nonEmpty) match
      case None => Ok(AuthorizationServerPage.error("No matching authorization request"))
      case Some(reqId) => requestsR.modify(requests => (requests.removed(reqId), requests.get(reqId))).flatMap {
        case None => Ok(AuthorizationServerPage.error("No matching authorization request"))
        case Some(query) => body.get("approve").find(_ == "Approve") match
          case Some(_) => query.responseType match
            case ResponseType.Code =>
              val scope = body.values.keys.filter(_.startsWith("scope_")).map(_.substring("scope_".length)).toSet[String]
              val cScope = getClient(query.clientId).map(_.scope).getOrElse(Set.empty[String])
              if scope.diff(cScope).nonEmpty then
                Found(Location(query.redirectUri.withQueryParam("error", "invalid_scope")))
              else
                val user = body.get("user").find(_.nonEmpty)
                for
                  code <- randomString[IO](random, 8)
                  _ <- codesR.update(_ + (code -> AuthorizeCodeCache(query, scope, user)))
                  resp <- Found(Location(query.redirectUri
                    .withQueryParam("code", code)
                    .withOptionQueryParam("state", query.state)
                  ))
                yield resp
            // 后续支持其它响应类型扩展
            case null => Found(Location(query.redirectUri
              .withQueryParam("error", "unsupported_response_type")
            ))
          case None => Found(Location(query.redirectUri.withQueryParam("error", "access_denied")))
      }
  end approve

  def service(random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]],
              codesR: Ref[IO, Map[String, AuthorizeCodeCache]])(using Logger[IO]) = HttpRoutes.of[IO] {
    case GET -> Root => Ok(AuthorizationServerPage.index(authServer, clients))
    case GET -> Root / "authorize" :? AuthorizeParam(authorizeParamValid) => authorizeParamValid.fold(
      msg => Ok(AuthorizationServerPage.error(msg)), param => authorize(param, random, requestsR)
    )
    case req @ POST -> Root / "approve" => req.as[UrlForm].flatMap { body => approve(body, random, requestsR, codesR) }
    case req @ POST -> Root / "token" => req.as[UrlForm].flatMap { body =>
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
              case Some(GrantType.AuthorizationCode) =>
                for
                  codeCache <- body.get("code").find(_.nonEmpty) match
                    case Some(code) => codesR.modify(codes => (codes.removed(code), code.some -> codes.get(code)))
                    case None => IO((none[String], none[AuthorizeCodeCache]))
                  resp <- codeCache match
                    case (Some(code), Some(cache)) =>
                      for
                        accessToken <- randomString[IO](random, 32)
                        _ <- insertRecord(OAuthTokenRecord(clientId, Some(accessToken), None, Some(cache.scope)))
                        _ <- info"Issuing access token $accessToken"
                        _ <- info"with scope ${cache.scope.mkString(" ")}"
                        _ <- info"Issued tokens for code $code"
                        resp <- Ok(OAuthToken(accessToken, AuthScheme.Bearer, None, Some(cache.scope)).asJson)
                      yield resp
                    case (code, _) => info"Unknown code, ${code.getOrElse("None")}" *>
                      BadRequest(ErrorInfo("invalid_grant").asJson)
                yield resp
              case Some(GrantType.RefreshToken) =>
                for
                  refreshTokenRecord <- body.get("refresh_token").find(_.nonEmpty) match
                    case r @ Some(refreshToken) => getRecordByRefreshToken(refreshToken).map(r -> _)
                    case None => IO((none[String], none[OAuthTokenRecord]))
                  _ <- refreshTokenRecord match
                    case (Some(refreshToken), Some(record)) if record.clientId != clientId =>
                      info"Invalid client using a refresh token, expected ${record.clientId} got $clientId" *>
                        removeRecordByRefreshToken(refreshToken) *>
                        BadRequest()
                    case (Some(refreshToken), Some(_)) =>
                      for
                        _ <- info"We found a matching refresh token $refreshToken"
                        accessToken <- randomString[IO](random, 32)
                        _ <- insertRecord(OAuthTokenRecord(clientId, Some(accessToken), None, None))
                        _ <- info"Issuing access token $accessToken for refresh token $refreshToken"
                        resp <- Ok(OAuthToken(accessToken, AuthScheme.Bearer, body.get("refresh_token").find(_.nonEmpty),
                          None).asJson)
                      yield resp
                    case _ => info"No matching token was found." *>
                      Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "Refresh Token")))
                  resp <- Ok()
                yield resp
              case grantType => info"Unknown grant type ${grantType.getOrElse("None")}" *>
                Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "Grant Type")))
    }
  }

  val invalidClientResp = Ok(ErrorInfo("invalid_client").asJson).map(_.copy(status = Status.Unauthorized))

  val run =
    for
      requestsR <- Ref.of[IO, Map[String, AuthorizeParam]](Map.empty)
      codesR <- Ref.of[IO, Map[String, AuthorizeCodeCache]](Map.empty)
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8001"
      _ <- clearRecord
      _ <- insertRecord(OAuthTokenRecord("oauth-client-1", None, Some("j2r3oj32r23rmasd98uhjrk2o3i"),
        Some(Set("foo", "bar"))))
      _ <- start[IO](serverPort)(service(random, requestsR, codesR).orNotFound)
      _ <- info"OAuth Authorization Server is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()
