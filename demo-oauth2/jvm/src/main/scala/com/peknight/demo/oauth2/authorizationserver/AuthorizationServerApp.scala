package com.peknight.demo.oauth2.authorizationserver

import cats.data.OptionT
import cats.effect.std.Random
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.either.*
import cats.syntax.traverse.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.domain.{AuthorizeParam, ResponseType}
import com.peknight.demo.oauth2.{serverHost, start}
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.headers.Location
import org.http4s.scalatags.*
import org.http4s.syntax.literals.uri
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

import scala.util.Try

object AuthorizationServerApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
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

  def service(random: Random[IO], requestsR: Ref[IO, Map[String, AuthorizeParam]])(using Logger[IO]) = HttpRoutes.of[IO] {
    case GET -> Root => Ok(AuthorizationServerPage.index(authServer, clients))

    case GET -> Root / "authorize" :? AuthorizeParam(authorizeParamValid) =>
      authorizeParamValid.fold(msg => Ok(AuthorizationServerPage.error(msg)), param => {
        getClient(param.clientId) match
          case None => info"Unknown client ${param.clientId}" *>
            Ok(AuthorizationServerPage.error("Unknown client"))
          case Some(client) => client.redirectUris.find(_ == param.redirectUri) match
            case None =>
              val redirectUrisStr = client.redirectUris.mkString(" ")
              info"Mismatched redirect URI, expected $redirectUrisStr got ${param.redirectUri}" *>
                Ok(AuthorizationServerPage.error("Invalid redirect URI"))
            case Some(redirectUri) =>
              val cScope = client.scope
              if param.scope.diff(cScope).nonEmpty then
                Found(Location(redirectUri.withQueryParam("error", "invalid_scope")))
              else
                for
                  reqId <- List.fill(8)(random.nextAlphaNumeric).sequence.map(_.mkString)
                  _ <- requestsR.update(_ + (reqId -> param))
                  resp <- Ok(AuthorizationServerPage.approve(client, reqId, param.scope.toList))
                yield resp
      })

    case req @ POST -> Root / "approve" => req.as[UrlForm].flatMap { body =>
      body.get("reqid").find(_.nonEmpty) match
        case None => Ok(AuthorizationServerPage.error("No matching authorization request"))
        case Some(reqId) => requestsR.modify(requests => (requests.removed(reqId), requests.get(reqId))).flatMap {
          case None => Ok(AuthorizationServerPage.error("No matching authorization request"))
          case Some(query) => body.get("approve").find(_ == "Approve") match
            case Some(_) => query.responseType match
              case ResponseType.Code =>
                for
                  code <- List.fill(8)(random.nextAlphaNumeric).sequence.map(_.mkString)
                  resp <- Ok()
                yield resp
              case null => Ok()
            case None => Ok()
        }
    }
  }

  val run =
    for
      requestsR <- Ref.of[IO, Map[String, AuthorizeParam]](Map.empty)
      random <- Random.scalaUtilRandom[IO]
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8001"
      _ <- start[IO](serverPort)(service(random, requestsR).orNotFound)
      _ <- info"OAuth Authorization Server is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()
