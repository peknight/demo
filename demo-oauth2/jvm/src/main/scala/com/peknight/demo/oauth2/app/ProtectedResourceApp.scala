package com.peknight.demo.oauth2.app

import cats.Functor
import cats.data.OptionT
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.functor.*
import cats.syntax.option.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.data.*
import com.peknight.demo.oauth2.domain.*
import com.peknight.demo.oauth2.page.ProtectedResourcePage
import com.peknight.demo.oauth2.random.*
import com.peknight.demo.oauth2.repository.getRecordByAccessToken
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.http4s.scalatags.*
import org.http4s.server.middleware.CORS
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

import scala.collection.immutable.Queue

object ProtectedResourceApp extends IOApp.Simple :

  //noinspection HttpUrlsUsage
  val run: IO[Unit] =
    for
      savedWordsR <- Ref.of[IO, Queue[String]](Queue.empty)
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8002"
      _ <- start[IO](serverPort)(corsPolicy(service(savedWordsR)).orNotFound)
      _ <- info"OAuth Resource Server is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

  given EntityDecoder[IO, Resource] = jsonOf[IO, Resource]

  given EntityDecoder[IO, ResourceScope] = jsonOf[IO, ResourceScope]

  val corsPolicy = CORS.policy
    .withAllowOriginHost(Set(Origin.Host(Uri.Scheme.http, Uri.RegName("localhost"), Some(8010))))
    .withAllowMethodsIn(Set(Method.GET, Method.POST))

  def service(savedWordsR: Ref[IO, Queue[String]])(using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ProtectedResourcePage.Text.index)
    case OPTIONS -> Root / "resource" => NoContent()
    case req @ POST -> Root / "resource" => requireAccessToken(req, protectedResourceAddr) { record =>
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
    case req @ GET -> Root / "produce" => requireAccessToken(req, protectedResourceAddr) { record =>
      val fruit = if record.scope.exists(_.contains("fruit")) then Seq("apple", "banana", "kiwi") else Seq.empty[String]
      val veggies = if record.scope.exists(_.contains("veggies")) then Seq("lettuce", "onion", "potato") else Seq.empty[String]
      val meats = if record.scope.exists(_.contains("meats")) then Seq("becon", "steak", "chicken breast") else Seq.empty[String]
      val produce = ProduceData(fruit, veggies, meats)
      info"Sending produce: $produce" *> Ok(produce.asJson)
    }
    case req @ GET -> Root / "favorites" => requireAccessToken(req, protectedResourceAddr) { record =>
      record.user match
        case Some("alice") => Ok(UserFavoritesData("Alice", aliceFavorites).asJson)
        case Some("bob") => Ok(UserFavoritesData("Bob", bobFavorites).asJson)
        case _ => Ok(UserFavoritesData("Unknown", FavoritesData.empty).asJson)
    }
  }

  private[this] def requireAccessTokenScope(req: Request[IO], scope: String)(pass: => IO[Response[IO]])
                                           (using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req, protectedResourceAddr) { record =>
      if record.scope.exists(_.contains(scope)) then pass
      else
        Forbidden.headers(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, protectedResourceAddr, Map(
          "error" -> "insufficient_scope",
          "scope" -> scope
        ))))
    }
