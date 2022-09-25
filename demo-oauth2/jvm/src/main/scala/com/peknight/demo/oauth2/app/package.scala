package com.peknight.demo.oauth2

import cats.Id
import cats.data.OptionT
import cats.effect.{Async, IO}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.option.*
import ciris.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.common.Mapper
import com.peknight.demo.oauth2.common.Mapper.*
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.repository.getRecordByAccessToken
import fs2.Stream
import fs2.hash.sha256
import fs2.io.file
import fs2.io.net.Network
import fs2.text.{base64, hex, utf8}
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.{Json, JsonObject}
import org.http4s.*
import org.http4s.circe.*
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
import scala.util.{Failure, Success, Try}

package object app:

  given CanEqual[Path, Path] = CanEqual.derived

  given CanEqual[Method, Method] = CanEqual.derived

  given CanEqual[Uri, Uri] = CanEqual.derived
  
  given CanEqual[CIString, AuthScheme] = CanEqual.derived

  given Mapper[Id, JwtClaim, IdToken] with
    def fMap(a: JwtClaim): Id[IdToken] =
      IdToken(a.content, a.issuer, a.subject, a.audience, a.expiration, a.notBefore, a.issuedAt, a.jwtId)
  end given

  given Mapper[Id, JwtClaim, OAuthTokenRecord] with
    def fMap(a: JwtClaim): Id[OAuthTokenRecord] =
      OAuthTokenRecord(a.audience.fold(none[String])(_.find(_.nonEmpty)), None, None,
        decode[TokenScope](a.content).map(_.scope).toOption, a.subject)
  end given

  given Mapper[Option, IntrospectionResponse, OAuthTokenRecord] with
    def fMap(a: IntrospectionResponse): Option[OAuthTokenRecord] =
      if a.active then Some(OAuthTokenRecord(a.clientId, None, None, a.scope, a.subject)) else None
  end given

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

  def parseRequest[A](req: Request[IO])(jsonF: Json => A)(formF: UrlForm => A): IO[A] =
    req.headers.get[`Content-Type`] match
      case Some(`Content-Type`(mediaType, _)) if mediaType.subType == "json" => req.as[Json].map(jsonF)
      case _ => req.as[UrlForm].map(formF)

  def checkJwt[A](payloadIO: IO[Try[JwtClaim]], audience: String)
                 (using Mapper[Id, JwtClaim, A], Logger[IO]): IO[Option[A]] =
    val payloadOptionT =
      for
        payload <- OptionT(payloadIO.flatMap {
          case Success(value) => IO.pure(value.some)
          case Failure(exception) => info"Token invalid: $exception" *> IO.pure(none[JwtClaim])
        })
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
      yield payload.to[Id, A]
    payloadOptionT.value

  def jwtRS256Decode(accessToken: String): IO[Try[JwtClaim]] =
    rsaPublicKey.flatMap(pubKey => IO(JwtCirce.decode(accessToken, pubKey, Seq(JwtAlgorithm.RS256))))

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
  def getRegistrationClientUri(clientId: String): Uri =
    Uri.unsafeFromString(s"https://local.peknight.com:8001/register/$clientId")

  def toHex(value: String): String =
    Stream(value).through(utf8.encode).through(hex.encode).toList.mkString("")

