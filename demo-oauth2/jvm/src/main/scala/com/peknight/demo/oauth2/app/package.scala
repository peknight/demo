package com.peknight.demo.oauth2

import cats.data.OptionT
import cats.effect.{Async, IO}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.option.*
import ciris.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.constant.{accessTokenKey, authorizationServerIndex, rsaKey}
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.{IdToken, OAuthTokenRecord}
import com.peknight.demo.oauth2.repository.getRecordByAccessToken
import fs2.Stream
import fs2.hash.sha256
import fs2.io.file
import fs2.io.net.Network
import fs2.text.{base64, utf8}
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

  val serverHost = host"local.peknight.com"

  val storePasswordConfig: ConfigValue[Effect, Secret[String]] = env("STORE_PASSWORD").secret
  val keyPasswordConfig: ConfigValue[Effect, Secret[String]] = env("KEY_PASSWORD").secret

  def start[F[_]: Async](port: Port)(httpApp: HttpApp[F]): F[(Server, F[Unit])] =
    for
      storePassword <- storePasswordConfig.load[F]
      keyPassword <- keyPasswordConfig.load[F]
      tlsContext <- Network[F].tlsContext.fromKeyStoreFile(
        file.Path("demo-oauth2/keystore/letsencrypt.keystore").toNioPath,
        storePassword.value.toCharArray, keyPassword.value.toCharArray)
      res <- EmberServerBuilder.default[F]
        // .withHost(serverHost)
        .withHostOption(None)
        .withPort(port)
        .withTLS(tlsContext)
        .withHttpApp(MiddlewareLogger.httpApp(true, true)(httpApp))
        .build.allocated
    yield res

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

  def checkJws(idToken: String, audience: String)(using Logger[IO]): IO[Option[IdToken]] =
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
      yield toIdToken(payload)
    payloadOptionT.value

  def toOAuthTokenRecord(payload: IdToken): OAuthTokenRecord =
    OAuthTokenRecord(payload.audience.fold(none[String])(_.find(_.nonEmpty)), None, None, None, payload.subject)

  def toIdToken(payload: JwtClaim): IdToken =
    IdToken(payload.content, payload.issuer, payload.subject, payload.audience, payload.expiration, payload.notBefore,
      payload.issuedAt, payload.jwtId)

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
  def getCodeChallenge(codeVerifier: String): String =
    Stream(codeVerifier)
      .through(utf8.encode)
      .through(sha256)
      .through(base64.encodeWithAlphabet(Base64Url))
      .toList.mkString.replaceAll("=", "")
