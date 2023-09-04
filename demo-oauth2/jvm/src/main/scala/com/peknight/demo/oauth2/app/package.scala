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
import io.circe.Decoder.Result
import io.circe.fs2.{byteStreamParser, decoder}
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.circe.{Decoder, HCursor, Json, JsonObject}
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.*
import org.http4s.server.{Router, Server}
import org.http4s.server.middleware.Logger as MiddlewareLogger
import org.http4s.server.staticcontent.webjarServiceBuilder
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.syntax.*
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim, JwtHeader}
import scodec.bits.Bases.Alphabets.Base64Url

import java.security.*
import java.security.spec.{RSAPrivateKeySpec, RSAPublicKeySpec}
import scala.util.{Failure, Success, Try}

package object app:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[Uri, Uri] = CanEqual.derived
  given CanEqual[CIString, AuthScheme] = CanEqual.derived

  given Mapper[Id, IdToken, OAuthTokenRecord] with
    def fMap(a: IdToken): Id[OAuthTokenRecord] =
      OAuthTokenRecord(a.audience.fold(none[String])(_.find(_.nonEmpty)), None, None,
        a.content.flatMap(content => decode[TokenScope](content).toOption).map(_.scope), a.subject, None)
  end given

  given Mapper[Option, IntrospectionResponse, OAuthTokenRecord] with
    def fMap(a: IntrospectionResponse): Option[OAuthTokenRecord] =
      if a.active then Some(OAuthTokenRecord(a.clientId, None, None, a.scope, a.subject, a.accessTokenKey)) else None
  end given

  given Decoder[JwtHeader] with
    def apply(c: HCursor): Result[JwtHeader] =
      for
        algorithm <- c.downField("alg").as[Option[String]]
        typ <- c.downField("typ").as[Option[String]]
        contentType <- c.downField("cty").as[Option[String]]
        keyId <- c.downField("kid").as[Option[String]]
      yield JwtHeader(algorithm.flatMap(JwtAlgorithm.optionFromString), typ, contentType, keyId)
  end given

  val serverHost = host"local.peknight.com"
  private[this] val storePasswordConfig: ConfigValue[Effect, Secret[String]] =
    env("STORE_PASSWORD").default("123456").secret
  private[this] val keyPasswordConfig: ConfigValue[Effect, Secret[String]] =
    env("KEY_PASSWORD").default("123456").secret
  val usePopConfig: ConfigValue[Effect, Boolean] =
    env("USE_POP").as[Boolean].default(false)

  private[this] def httpApp[F[_] : Async](routes: HttpRoutes[F]): HttpApp[F] = Router(
    "/" -> routes,
    "/webjars" -> webjarServiceBuilder[F].toRoutes
  ).orNotFound

  def start[F[_]: Async](port: Port)(routes: HttpRoutes[F]): F[(Server, F[Unit])] =
    for
      storePassword <- storePasswordConfig.load[F]
      keyPassword <- keyPasswordConfig.load[F]
      tlsContext <- Network[F].tlsContext.fromKeyStoreFile(
        file.Path("demo-security/keystore/letsencrypt.keystore").toNioPath,
        storePassword.value.toCharArray, keyPassword.value.toCharArray)
      res <- EmberServerBuilder.default[F]
        // .withHost(serverHost)
        .withHostOption(None)
        .withPort(port)
        .withTLS(tlsContext)
        .withHttpApp(MiddlewareLogger.httpApp(true, true)(httpApp(routes)))
        .build.allocated
    yield res

  def runHttpRequest[T](req: Request[IO])(onSuccess: Response[IO] => IO[T])(onFailure: Int => IO[T]): IO[T] =
    EmberClientBuilder.default[IO].build.use { httpClient =>
      httpClient.run(req).use { response =>
        val statusCode = response.status.code
        if statusCode >= 200 && statusCode < 300 then onSuccess(response)
        else onFailure(statusCode)
      }
    }

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
    handleOAuthTokenRecordOptionT(oauthTokenRecord, realm)(handleOAuthTokenRecord)

  def handleOAuthTokenRecordOptionT(oauthTokenRecordOptionT: OptionT[IO, OAuthTokenRecord], realm: String)
                                   (handleOAuthTokenRecord: OAuthTokenRecord => IO[Response[IO]])
                                   (using Logger[IO]): IO[Response[IO]] =
    oauthTokenRecordOptionT.value.flatMap {
      case Some(record) => info"We found a matching token: ${record.accessToken.getOrElse("")}" *>
        handleOAuthTokenRecord(record)
      case _ => info"No matching token was found." *>
        Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, realm)))
    }

  def checkJwt[A](payloadIO: IO[Try[Json]], audience: String, nonce: Option[String])
                 (using Mapper[Id, IdToken, A], Logger[IO]): IO[Option[A]] =
    val payloadOptionT =
      for
        payload <- OptionT(payloadIO.flatMap {
          case Success(value) => value.as[IdToken] match
            case Left(e) => info"Payload invalid: $e".as(none[IdToken])
            case Right(idToken) => IO.pure(idToken.some)
          case Failure(exception) => info"Token invalid: $exception" *> IO.pure(none[IdToken])
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
        _ <- nonce.fold(OptionT.pure[IO](()))(n => payload.nonce.filter(_ == n).fold(OptionT.none[IO, Unit])(
          _ => info"nonce OK".optionT
        ))
      yield payload.to[Id, A]
    payloadOptionT.value

  def userInfoEndpoint(req: Request[IO], realm: String)(using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, realm)(getRecordByAccessToken) { record =>
      val scope = record.scope.getOrElse(Set.empty[String])
      if !scope.contains("openid") then Forbidden() else
        record.user.flatMap(userInfos.get).fold(NotFound()) { user =>
          val containsProfile: Boolean = scope.contains("profile")
          val containsEmail: Boolean = scope.contains("email")
          val containsPhone: Boolean = scope.contains("phone")
          Ok(UserInfo(
            user.sub.filter(_ => scope.contains("openid")),
            user.preferredUsername.filter(_ => containsProfile),
            user.name.filter(_ => containsProfile),
            user.email.filter(_ => containsEmail),
            user.emailVerified.filter(_ => containsEmail),
            None,
            None,
            user.familyName.filter(_ => containsProfile),
            user.givenName.filter(_ => containsProfile),
            user.middleName.filter(_ => containsProfile),
            user.nickname.filter(_ => containsProfile),
            user.profile.filter(_ => containsProfile),
            user.picture.filter(_ => containsProfile),
            user.website.filter(_ => containsProfile),
            user.gender.filter(_ => containsProfile),
            user.birthdate.filter(_ => containsProfile),
            user.zoneInfo.filter(_ => containsProfile),
            user.locale.filter(_ => containsProfile),
            user.updatedAt.filter(_ => containsProfile),
            user.address.filter(_ => scope.contains("address")),
            user.phoneNumber.filter(_ => containsPhone),
            user.phoneNumberVerified.filter(_ => containsPhone)
          ).asJson)
        }
    }

  private[this] def rsaPublicKey(rsaKey: RsaKey): IO[PublicKey] =
    for
      modulus <- decodeToBigInt(rsaKey.n)
      exponent <- decodeToBigInt(rsaKey.e)
      key <- IO(KeyFactory.getInstance("RSA").generatePublic(RSAPublicKeySpec(
        modulus.bigInteger, exponent.bigInteger
      )))
    yield key

  def rsaPrivateKey(rsaKey: RsaKey): IO[PrivateKey] =
    for
      modulus <- decodeToBigInt(rsaKey.n)
      privateExponent <- decodeToBigInt(rsaKey.d.getOrElse(""))
      key <- IO(KeyFactory.getInstance("RSA").generatePrivate(RSAPrivateKeySpec(
        modulus.bigInteger, privateExponent.bigInteger
      )))
    yield key

  def jwtRS256Decode(accessToken: String): IO[Try[Json]] =
    rsaPublicKey(rsaKey).flatMap(pubKey => IO(JwtCirce.decodeJson(accessToken, pubKey, Seq(JwtAlgorithm.RS256))))

  def parseRequest[A](req: Request[IO])(jsonF: Json => A)(formF: UrlForm => A): IO[A] =
    req.headers.get[`Content-Type`] match
      case Some(`Content-Type`(mediaType, _)) if mediaType.subType == "json" => req.as[Json].map(jsonF)
      case _ => req.as[UrlForm].map(formF)

  def getCodeChallenge(codeVerifier: String): String =
    Stream(codeVerifier)
      .through(utf8.encode)
      .through(sha256)
      .through(base64.encodeWithAlphabet(Base64Url))
      .toList.mkString.replaceAll("=", "")

  def decodeToBigInt(base64UrlEncoded: String): IO[BigInt] =
    Stream(base64UrlEncoded)
      .covary[IO]
      .through(base64.decodeWithAlphabet(Base64Url))
      .compile.toList
      .map(bytes => BigInt(1, bytes.toArray))

  def getRegistrationClientUri(clientId: String): Uri =
    Uri.unsafeFromString(s"https://local.peknight.com:8001/register/$clientId")

  def toHex(value: String): String =
    Stream(value).through(utf8.encode).through(hex.encode).toList.mkString("")

  def parseJwt[A: Decoder](value: String): IO[Option[A]] =
    Stream(value).covary[IO]
      .through(base64.decodeWithAlphabet(Base64Url))
      .through(byteStreamParser)
      .through(decoder[IO, A])
      .compile.toList.map(_.headOption)

