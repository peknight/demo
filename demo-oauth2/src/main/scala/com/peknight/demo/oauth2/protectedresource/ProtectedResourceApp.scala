package com.peknight.demo.oauth2.protectedresource

import cats.data.OptionT
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.option.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.*
import com.peknight.demo.oauth2.domain.{Resource, WordsModel}
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

  private[this] val resource: Resource = Resource("Protected Resource", "This data has been protected by OAuth 2.0")

  private[this] val accessTokenKey: String = "access_token"

  // TODO cors
  def service(savedWordsR: Ref[IO, Queue[String]])(using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ProtectedResourcePage.index)
    case OPTIONS -> Root / "resource" => NoContent()
    case req @ POST -> Root / "resource" => requireAccessToken(req, None)(Ok(resource.asJson))
    case req @ GET -> Root / "words" => requireAccessToken(req, "read".some) {
      for
        savedWords <- savedWordsR.get
        realTime <- IO.realTime
        resp <- Ok(WordsModel(savedWords, realTime.toMillis, None).asJson)
      yield resp
    }
    case req @ POST -> Root / "words" => requireAccessToken(req, "write".some) {
      for
        body <- req.as[UrlForm]
        _ <- body.get("word").find(_.nonEmpty).fold(IO.unit)(word => savedWordsR.update(_.appended(word)))
        resp <- Created()
      yield resp
    }
    case req @ DELETE -> Root / "words" => requireAccessToken(req, "delete".some) {
      savedWordsR.update(_.init) *> NoContent()
    }
  }

  private[this] def requireAccessToken(req: Request[IO], scopeOption: Option[String])(pass: => IO[Response[IO]])
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
        scopeOption.fold(pass) { scope =>
          if record.scope.exists(_.contains(scope)) then pass
          else
            Forbidden.headers(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "localhost:8002", Map(
              "error" -> "insufficient_scope",
              "scope" -> scope
            ))))
        }
      case _ => info"No matching token was found." *>
        Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "localhost:8002")))
    }
