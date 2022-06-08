package com.peknight.demo.oauth2.authorizationserver

import cats.data.OptionT
import cats.effect.{IO, IOApp}
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.server.{host, start}
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.scalatags.*
import org.http4s.syntax.literals.uri
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

object AuthorizationServerApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[Uri, Uri] = CanEqual.derived

  object ClientIdParam extends OptionalQueryParamDecoderMatcher[String]("client_id")
  object RedirectUriParam extends OptionalQueryParamDecoderMatcher[Uri]("redirect_uri")

  val authServer = AuthServerInfo(uri"http://localhost:8001/authorize", uri"http://localhost:8001/token")

  val clients = Seq(
    ClientInfo(
      "oauth-client-1",
      "oauth-client-secret-1",
      List("foo", "bar"),
      List(uri"http://localhost:8000/callback")
    )
  )

  def getClient(clientId: String): Option[ClientInfo] = clients.find(_.id == clientId)

  def service(using Logger[IO]) = HttpRoutes.of[IO] {
    case GET -> Root => Ok(AuthorizationServerPage.index(authServer, clients))

    case GET -> Root / "authorize" :? ClientIdParam(clientIdOption) +& RedirectUriParam(redirectUriOption) =>
      clientIdOption.flatMap(getClient) match
        case None => info"Unknown client ${clientIdOption.getOrElse("None")}" *>
          Ok(AuthorizationServerPage.error("Unknown client"))
        case Some(client) => redirectUriOption.filter(redirectUri => client.redirectUris.contains(redirectUri)) match
          case None =>
            val redirectUrisStr = client.redirectUris.mkString(" ")
            val redirectUriStr = redirectUriOption.map(_.toString).getOrElse("None")
            info"Mismatched redirect URI, expected $redirectUrisStr got $redirectUriStr" *>
              Ok(AuthorizationServerPage.error("Invalid redirect URI"))
          case Some(redirectUri) => Ok(redirectUri.toString)
  }

  val run =
    for
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8001"
      _ <- start[IO](serverPort)(service.orNotFound)
      _ <- info"OAuth Authorization Server is listening at http://$host:$serverPort"
      _ <- IO.never
    yield ()
