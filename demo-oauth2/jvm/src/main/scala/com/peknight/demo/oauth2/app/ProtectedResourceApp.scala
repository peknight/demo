package com.peknight.demo.oauth2.app

import cats.Functor
import cats.data.OptionT
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.functor.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.page.ProtectedResourcePage
import com.peknight.demo.oauth2.random.*
import com.peknight.demo.oauth2.repository.getRecordByAccessToken
import fs2.Stream
import fs2.text.{hex, utf8}
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.http4s.scalatags.*
import org.http4s.server.middleware.CORS
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*
import pdi.jwt.JwtCirce

import scala.collection.immutable.Queue
import scala.concurrent.duration.*

object ProtectedResourceApp extends IOApp.Simple :

  val run: IO[Unit] =
    for
      savedWordsR <- Ref.of[IO, Queue[String]](Queue.empty)
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8002"
      _ <- start[IO](serverPort)(corsPolicy(service(savedWordsR)).orNotFound)
      _ <- info"OAuth Resource Server is listening at https://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

  given EntityDecoder[IO, Resource] = jsonOf[IO, Resource]

  given EntityDecoder[IO, ResourceScope] = jsonOf[IO, ResourceScope]

  given EntityDecoder[IO, IntrospectionResponse] = jsonOf[IO, IntrospectionResponse]

  val corsPolicy = CORS.policy
    .withAllowOriginHost(Set(Origin.Host(
      Uri.Scheme.https,
      Uri.RegName.fromHostname(host"local.peknight.com"),
      Some(8010))
    ))
    .withAllowMethodsIn(Set(Method.GET, Method.POST))

  object LanguageQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String]("language")

  def service(savedWordsR: Ref[IO, Queue[String]])(using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ProtectedResourcePage.Text.index)
    // case OPTIONS -> Root / "resource" => NoContent()
    case req @ POST -> Root / "resource" => requireAccessToken(req, protectedResourceAddr)(introspectJwt) { record =>
      Ok(ResourceScope(resource, record.scope).asJson)
    }
    case req @ GET -> Root / "words" => requireAccessTokenScope(req, "read") {
      for
        savedWords <- savedWordsR.get
        realTime <- IO.realTime
        resp <- Ok(WordsData(savedWords, realTime.toMillis).asJson)
      yield resp
    }
    case req @ POST -> Root / "words" => requireAccessTokenScope(req, "write") {
      for
        body <- req.as[UrlForm]
        _ <- body.get("word").find(_.nonEmpty).fold(IO.unit)(word => savedWordsR.update(_.appended(word)))
        resp <- Created()
      yield resp
    }
    case req @ DELETE -> Root / "words" => requireAccessTokenScope(req, "delete") {
      savedWordsR.update(_.init) *> NoContent()
    }
    case req @ GET -> Root / "produce" => requireAccessToken(req, protectedResourceAddr)(introspectJwt) { record =>
      val fruit = if record.scope.exists(_.contains("fruit")) then Seq("apple", "banana", "kiwi") else Seq.empty[String]
      val veggies = if record.scope.exists(_.contains("veggies")) then Seq("lettuce", "onion", "potato") else Seq.empty[String]
      val meats = if record.scope.exists(_.contains("meats")) then Seq("becon", "steak", "chicken breast") else Seq.empty[String]
      val produce = ProduceData(fruit, veggies, meats)
      info"Sending produce: $produce" *> Ok(produce.asJson)
    }
    case req @ GET -> Root / "favorites" => requireAccessToken(req, protectedResourceAddr)(introspectJwt) { record =>
      record.user match
        case Some("alice") => Ok(UserFavoritesData("Alice", aliceFavorites).asJson)
        case Some("bob") => Ok(UserFavoritesData("Bob", bobFavorites).asJson)
        case _ => Ok(UserFavoritesData("Unknown", FavoritesData.empty).asJson)
    }
    // case OPTIONS -> Root / "helloWorld" => NoContent()
    case req @ GET -> Root / "helloWorld" :? LanguageQueryParamMatcher(language) =>
      requireAccessToken(req, protectedResourceAddr)(introspectJwt) { _ =>
        val greeting = language match
          case Some("en") => "Hello World"
          case Some("de") => "Hallo Welt"
          case Some("it") => "Ciao Mondo"
          case Some("fr") => "Bonjour monde"
          case Some("es") => "Hola mundo"
          case _ => s"Error, invalid language: ${language.map(Uri.encode(_)).getOrElse("None")}"
        Ok(
          GreetingResource(greeting).asJson,
          // 可以研究下`Content-Security-Policy`这个头
          // 下面两个头Mozilla Firefox不支持
          // 防止在没有声明Content-Type的情况下（以防万一）执行MIME嗅探
          Header.Raw(CIString("X-Content-Type-Options"), "nosniff"),
          // 启用当前大多数浏览器内置的XSS过滤器
          Header.Raw(CIString("X-XSS-Protection"), "1; mode=block"),
          `Strict-Transport-Security`.unsafeFromDuration(365.days, includeSubDomains = false)
        )
      }
  }

  private[this] def requireAccessTokenScope(req: Request[IO], scope: String)(pass: => IO[Response[IO]])
                                           (using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, protectedResourceAddr)(introspectJwt) { record =>
      if record.scope.exists(_.contains(scope)) then pass
      else
        Forbidden.headers(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, protectedResourceAddr, Map(
          "error" -> "insufficient_scope",
          scopeKey -> scope
        ))))
    }

  private[this] def introspect(accessToken: String)(using Logger[IO]): IO[Option[OAuthTokenRecord]] =
    val req: Request[IO] = POST(
      UrlForm(
        "token" -> accessToken
      ),
      authServer.introspectionEndpoint,
      Headers(
        Authorization(BasicCredentials(Uri.encode(protectedResource.resourceId),
          Uri.encode(protectedResource.resourceSecret))),
        `Content-Type`(MediaType.application.`x-www-form-urlencoded`)
      )
    )
    runHttpRequest(req) { response =>
      for
        introspectionResponse <- response.as[IntrospectionResponse]
        _ <- info"Got introspection response $introspectionResponse"
      yield introspectionResponse.toOAuthTokenRecord
    }{ _ => IO.pure(None) }

  private[this] def checkProtectedResourceJws(accessToken: String)(using Logger[IO]): IO[Option[OAuthTokenRecord]] =
    checkJws(accessToken, protectedResourceIndex.toString).map(_.map(toOAuthTokenRecord))

  private[this] def introspectJwt(accessToken: String)(using Logger[IO]): IO[Option[OAuthTokenRecord]] =
    val payloadOptionT =
      for
        payload <- OptionT(IO(JwtCirce.decode(accessToken).toOption))
        _ <- info"Payload $payload".optionT
        _ <- OptionT.fromOption(payload.issuer.filter(_ == authorizationServerIndex.toString))
        _ <- info"issuer OK".optionT
        _ <- OptionT.fromOption(payload.audience.find(_.contains(protectedResourceIndex.toString)))
        _ <- info"Audience OK".optionT
        realTime <- IO.realTime.optionT
        _ <- OptionT.fromOption(payload.issuedAt.filter(_ <= realTime.toSeconds))
        _ <- info"issue-at OK".optionT
        _ <- OptionT.fromOption(payload.expiration.filter(_ >= realTime.toSeconds))
        _ <- info"expiration OK".optionT
        _ <- info"Token valid!".optionT
      yield toOAuthTokenRecord(toIdToken(payload))
    payloadOptionT.value

  private[this] def toHex(value: String): String =
    Stream(sharedTokenSecret).through(utf8.encode).through(hex.encode).toList.mkString("")


