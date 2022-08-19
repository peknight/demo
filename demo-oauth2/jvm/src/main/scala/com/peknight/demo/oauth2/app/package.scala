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
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.*
import org.http4s.server.Server
import org.http4s.server.middleware.Logger as MiddlewareLogger
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import fs2.Stream
import fs2.text.base64
import org.typelevel.log4cats.syntax.*
import scodec.bits.Bases.Alphabets.Base64Url

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

  def requireAccessToken(req: Request[IO], realm: String)(queryOAuthTokenRecord: String => IO[Option[OAuthTokenRecord]])
                        (handleOAuthTokenRecord: OAuthTokenRecord => IO[Response[IO]])
                        (using Logger[IO]): IO[Response[IO]] =
    val oauthTokenRecord =
      for
        accessToken <- OptionT(req.headers.get[Authorization] match
          case Some(Authorization(Credentials.Token(AuthScheme.Bearer, token))) => IO(Some(token))
          case _ => req.as[UrlForm].attempt
            .map(_.toOption.flatMap(_.get(accessTokenKey).headOption).orElse(req.params.get(accessTokenKey)))
        )
        _ <- info"Incoming token: $accessToken".optionT
        record <- OptionT(queryOAuthTokenRecord(accessToken))
      yield record
    oauthTokenRecord.value.flatMap {
      case Some(record) => info"We found a matching token: ${record.accessToken.getOrElse("")}" *>
        handleOAuthTokenRecord(record)
      case _ => info"No matching token was found." *>
        Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, realm)))
    }
    
  def runHttpRequest[T](req: Request[IO])(onSuccess: Response[IO] => IO[T])(onFailure: Int => IO[T]): IO[T] =
    EmberClientBuilder.default[IO].build.use { httpClient =>
      httpClient.run(req).use { response =>
        val statusCode = response.status.code
        if statusCode >= 200 && statusCode < 300 then onSuccess(response)
        else onFailure(statusCode)
      }
    }

  def decodeToBigInt(base64UrlEncoded: String): IO[BigInt] =
    Stream(base64UrlEncoded)
      .covary[IO]
      .through(base64.decodeWithAlphabet(Base64Url))
      .compile.toList
      .map(bytes => BigInt(1, bytes.toArray))

