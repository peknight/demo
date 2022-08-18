package com.peknight.demo.oauth2

import cats.data.OptionT
import cats.effect.{Async, IO}
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.constant.accessTokenKey
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.OAuthTokenRecord
import com.peknight.demo.oauth2.repository.getRecordByAccessToken
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.*
import org.http4s.server.Server
import org.http4s.server.middleware.Logger as MiddlewareLogger
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.syntax.*

package object app:

  given CanEqual[Path, Path] = CanEqual.derived

  given CanEqual[Method, Method] = CanEqual.derived

  given CanEqual[Uri, Uri] = CanEqual.derived
  
  given CanEqual[CIString, AuthScheme] = CanEqual.derived

  val serverHost = host"localhost"

  def start[F[_] : Async](port: Port)(httpApp: HttpApp[F]): F[(Server, F[Unit])] =
    EmberServerBuilder.default[F]
      .withHost(serverHost)
      .withPort(port)
      .withHttpApp(MiddlewareLogger.httpApp(true, true)(httpApp))
      .build.allocated

  def requireAccessToken(req: Request[IO], realm: String)(handleOAuthTokenRecord: OAuthTokenRecord => IO[Response[IO]])
                        (using Logger[IO]): IO[Response[IO]] =
    val oauthTokenRecord =
      for
        accessToken <- OptionT(req.headers.get[Authorization] match
          case Some(Authorization(Credentials.Token(AuthScheme.Bearer, token))) => IO(Some(token))
          case _ => req.as[UrlForm].attempt
            .map(_.toOption.flatMap(_.get(accessTokenKey).headOption).orElse(req.params.get(accessTokenKey)))
        )
        _ <- info"Incoming token: $accessToken".optionT
        record <- OptionT(getRecordByAccessToken(accessToken))
      yield record
    oauthTokenRecord.value.flatMap {
      case Some(record) => info"We found a matching token: ${record.accessToken.getOrElse("")}" *>
        handleOAuthTokenRecord(record)
      case _ => info"No matching token was found." *>
        Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, realm)))
    }
