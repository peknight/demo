package com.peknight.demo.oauth2.app

import cats.Functor
import cats.data.OptionT
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.functor.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.common.Mapper.*
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
import org.http4s.server.middleware.{CORS, CORSPolicy}
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*
import pdi.jwt.{JwtAlgorithm, JwtCirce}

import scala.collection.immutable.Queue
import scala.concurrent.duration.*
import scala.util.Try

object ProtectedResourceApp extends IOApp.Simple :

  //noinspection ForwardReference
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

  private[this] val corsPolicy: CORSPolicy = CORS.policy
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
    case req @ POST -> Root / "resource" => getResource(req)
    case req @ GET -> Root / "words" => getWords(req, savedWordsR)
    case req @ POST -> Root / "words" => postWords(req, savedWordsR)
    case req @ DELETE -> Root / "words" => deleteWords(req, savedWordsR)
    case req @ GET -> Root / "produce" => produce(req)
    case req @ GET -> Root / "favorites" => favorites(req)
    // case OPTIONS -> Root / "helloWorld" => NoContent()
    case req @ GET -> Root / "helloWorld" :? LanguageQueryParamMatcher(language) => helloWorld(req, language)
  }

  private[this] def getResource(req: Request[IO])(using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, protectedResourceAddr)(introspect)(record => Ok(ResourceScope(resource, record.scope).asJson))

  private[this] def getWords(req: Request[IO], savedWordsR: Ref[IO, Queue[String]])
                            (using Logger[IO]): IO[Response[IO]] =
    requireAccessTokenScope(req, "read") {
      for
        savedWords <- savedWordsR.get
        realTime <- IO.realTime
        resp <- Ok(WordsData(savedWords, realTime.toMillis).asJson)
      yield resp
    }

  private[this] def postWords(req: Request[IO], savedWordsR: Ref[IO, Queue[String]])
                             (using Logger[IO]): IO[Response[IO]] =
    requireAccessTokenScope(req, "write") {
      for
        body <- req.as[UrlForm]
        _ <- body.get("word").find(_.nonEmpty).fold(IO.unit)(word => savedWordsR.update(_.appended(word)))
        resp <- Created()
      yield resp
    }

  private[this] def deleteWords(req: Request[IO], savedWordsR: Ref[IO, Queue[String]])
                               (using Logger[IO]): IO[Response[IO]] =
    requireAccessTokenScope(req, "delete")(savedWordsR.update(_.init) *> NoContent())

  private[this] def produce(req: Request[IO])(using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, protectedResourceAddr)(introspect) { record =>
      val fruit =
        if record.scope.exists(_.contains("fruit")) then Seq("apple", "banana", "kiwi")
        else Seq.empty[String]
      val veggies =
        if record.scope.exists(_.contains("veggies")) then Seq("lettuce", "onion", "potato")
        else Seq.empty[String]
      val meats =
        if record.scope.exists(_.contains("meats")) then Seq("bacon", "steak", "chicken breast")
        else Seq.empty[String]
      val produce = ProduceData(fruit, veggies, meats)
      info"Sending produce: $produce" *> Ok(produce.asJson)
    }

  private[this] def favorites(req: Request[IO])(using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, protectedResourceAddr)(introspect) { record =>
      record.user match
        case Some("alice") => Ok(UserFavoritesData("Alice", aliceFavorites).asJson)
        case Some("bob") => Ok(UserFavoritesData("Bob", bobFavorites).asJson)
        case _ => Ok(UserFavoritesData("Unknown", FavoritesData.empty).asJson)
    }

  private[this] def helloWorld(req: Request[IO], language: Option[String])(using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, protectedResourceAddr)(introspect) { _ =>
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
      yield introspectionResponse.to[Option, OAuthTokenRecord]
    }{ _ => IO.pure(None) }

  //noinspection ScalaUnusedSymbol
  private[this] def checkUnsafeJwt(accessToken: String)(using Logger[IO]): IO[Option[OAuthTokenRecord]] =
    checkJwt[OAuthTokenRecord](IO(JwtCirce.decode(accessToken)), protectedResourceIndex.toString)

  //noinspection ScalaUnusedSymbol
  private[this] def checkHS256Jwt(accessToken: String)(using Logger[IO]): IO[Option[OAuthTokenRecord]] =
    checkJwt[OAuthTokenRecord](IO(JwtCirce.decode(accessToken, toHex(sharedTokenSecret), Seq(JwtAlgorithm.HS256))),
      protectedResourceIndex.toString)

  //noinspection ScalaUnusedSymbol
  private[this] def checkRS256Jwt(accessToken: String)(using Logger[IO]): IO[Option[OAuthTokenRecord]] =
    checkJwt[OAuthTokenRecord](jwtRS256Decode(accessToken), protectedResourceIndex.toString)
  private[this] def requireAccessTokenScope(req: Request[IO], scope: String)(pass: => IO[Response[IO]])
                                           (using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, protectedResourceAddr)(introspect) { record =>
      if record.scope.exists(_.contains(scope)) then pass
      else
        Forbidden.headers(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, protectedResourceAddr, Map(
          "error" -> "insufficient_scope",
          scopeKey -> scope
        ))))
    }

