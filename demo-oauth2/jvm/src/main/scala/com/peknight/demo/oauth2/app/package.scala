package com.peknight.demo.oauth2

import cats.data.OptionT
import cats.effect.{Async, IO}
import cats.syntax.option.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.constant.{accessTokenKey, authorizationServerIndex, rsaKey}
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.OAuthTokenRecord
import com.peknight.demo.oauth2.repository.getRecordByAccessToken
import fs2.Stream
import fs2.text.base64
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.*
import org.http4s.server.Server
import org.http4s.server.middleware.Logger as MiddlewareLogger
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.syntax.*
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import scodec.bits.Bases.Alphabets.Base64Url

import java.security.spec.RSAPublicKeySpec
import java.security.{KeyFactory, PublicKey}

package object app:

  given CanEqual[Path, Path] = CanEqual.derived

  given CanEqual[Method, Method] = CanEqual.derived

  given CanEqual[Uri, Uri] = CanEqual.derived
  
  given CanEqual[CIString, AuthScheme] = CanEqual.derived

  val serverHost = host"localhost"

  def start[F[_] : Async](port: Port)(httpApp: HttpApp[F]): F[(Server, F[Unit])] =
    EmberServerBuilder.default[F]
      // .withHost(serverHost)
      .withHostOption(None)
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

  def jws(idToken: String, audience: String)(using Logger[IO]): IO[Option[JwtClaim]] =
    val payloadOptionT =
      for
      // signatureValid <- IO(JwtCirce.isValid(idToken, toHex(sharedTokenSecret), Seq(JwtAlgorithm.HS256))).optionT
        pubKey <- rsaPublicKey.optionT
        payload <- OptionT(IO(JwtCirce.decode(idToken, pubKey, Seq(JwtAlgorithm.RS256)).toOption))
        _ <- info"Signature validated.".optionT
        _ <- info"Payload $payload".optionT
        _ <- OptionT.fromOption(payload.issuer.filter(_ == authorizationServerIndex.toString))
        _ <- info"issuer OK".optionT
        _ <- OptionT.fromOption(payload.audience.find(_.contains(audience)))
        _ <- info"Audience OK".optionT
        realTime <- IO.realTime.optionT
        _ <- OptionT.fromOption(payload.issuedAt.filter(_ <= realTime.toSeconds))
        _ <- info"issued-at OK".optionT
        _ <- OptionT.fromOption(payload.expiration.filter(_ >= realTime.toSeconds))
        _ <- info"expiration OK".optionT
        _ <- info"Token valid!".optionT
      yield payload
    payloadOptionT.value

  def toOAuthTokenRecord(payload: JwtClaim): OAuthTokenRecord =
    OAuthTokenRecord(payload.audience.fold(none[String])(_.find(_.nonEmpty)), None, None, None, payload.subject)

  val rsaPublicKey: IO[PublicKey] =
    for
      modulus <- decodeToBigInt(rsaKey.n)
      exponent <- decodeToBigInt(rsaKey.e)
      key <- IO(KeyFactory.getInstance("RSA").generatePublic(RSAPublicKeySpec(
        modulus.bigInteger, exponent.bigInteger
      )))
    yield key

  def decodeToBigInt(base64UrlEncoded: String): IO[BigInt] =
    Stream(base64UrlEncoded)
      .covary[IO]
      .through(base64.decodeWithAlphabet(Base64Url))
      .compile.toList
      .map(bytes => BigInt(1, bytes.toArray))
