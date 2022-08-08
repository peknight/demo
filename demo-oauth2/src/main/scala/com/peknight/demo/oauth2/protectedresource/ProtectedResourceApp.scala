package com.peknight.demo.oauth2.protectedresource

import cats.data.OptionT
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.option.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.*
import com.peknight.demo.oauth2.domain.{FavoritesData, OAuthTokenRecord, ProduceData, Resource, UserFavoritesData, WordsData}
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.http4s.scalatags.*
import org.typelevel.ci.CIString
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

import scala.collection.immutable.Queue
import scala.concurrent.duration.FiniteDuration

object ProtectedResourceApp extends IOApp.Simple:

  private[this] val resource: Resource = Resource("Protected Resource", "This data has been protected by OAuth 2.0")

  private[this] val accessTokenKey: String = "access_token"

  private[this] val aliceFavorites: FavoritesData = FavoritesData(
    Seq("The Multidimensional Vector", "Space Fights", "Jewelry Boss"),
    Seq("bacon", "pizza", "bacon pizza"),
    Seq("techno", "industrial", "alternative")
  )

  private[this] val bobFavorites: FavoritesData = FavoritesData(
    Seq("An Unrequited Love", "Several Shades of Turquoise", "Think Of The Children"),
    Seq("bacon", "kale", "gravel"),
    Seq("baroque", "ukulele", "baroque ukulele")
  )

  //noinspection HttpUrlsUsage
  val run: IO[Unit] =
    for
      savedWordsR <- Ref.of[IO, Queue[String]](Queue.empty)
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8002"
      _ <- start[IO](serverPort)(service(savedWordsR).orNotFound)
      _ <- info"OAuth Resource Server is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[CIString, AuthScheme] = CanEqual.derived

  given EntityDecoder[IO, Resource] = jsonOf[IO, Resource]

  // TODO cors
  def service(savedWordsR: Ref[IO, Queue[String]])(using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ProtectedResourcePage.index)
    case OPTIONS -> Root / "resource" => NoContent()
    case req @ POST -> Root / "resource" => requireAccessToken(req)(_ => Ok(resource.asJson))
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
    case req @ GET -> Root / "produce" => requireAccessToken(req) { record =>
      val fruit = if record.scope.exists(_.contains("fruit")) then Seq("apple", "banana", "kiwi") else Seq.empty[String]
      val veggies = if record.scope.exists(_.contains("veggies")) then Seq("lettuce", "onion", "potato") else Seq.empty[String]
      val meats = if record.scope.exists(_.contains("meats")) then Seq("becon", "steak", "chicken breast") else Seq.empty[String]
      val produce = ProduceData(fruit, veggies, meats)
      info"Sending produce: $produce" *> Ok(produce.asJson)
    }
    case req @ GET -> Root / "favorites" => requireAccessToken(req) { record =>
      record.user match
        case Some("alice") => Ok(UserFavoritesData("Alice", aliceFavorites).asJson)
        case Some("bob") => Ok(UserFavoritesData("Bob", bobFavorites).asJson)
        case _ => Ok(UserFavoritesData("Unknown", FavoritesData.empty).asJson)
    }
  }

  private[this] def requireAccessTokenScope(req: Request[IO], scope: String)(pass: => IO[Response[IO]])
                                      (using Logger[IO]): IO[Response[IO]] =
    requireAccessToken(req) { record =>
      if record.scope.exists(_.contains(scope)) then pass
      else
        Forbidden.headers(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "localhost:8002", Map(
          "error" -> "insufficient_scope",
          "scope" -> scope
        ))))
    }

  private[this] def requireAccessToken(req: Request[IO])(handleOAuthTokenRecord: OAuthTokenRecord => IO[Response[IO]])
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
        Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "localhost:8002")))
    }
