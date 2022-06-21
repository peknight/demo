package com.peknight.demo.oauth2.protectedresource

import cats.data.OptionT
import cats.effect.{IO, IOApp}
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.*
import com.peknight.demo.oauth2.domain.Resource
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

object ProtectedResourceApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived
  given CanEqual[CIString, AuthScheme] = CanEqual.derived

  given EntityDecoder[IO, Resource] = jsonOf[IO, Resource]

  object OptionalAccessTokenQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String](accessTokenKey)

  private[this] val resource: Resource = Resource("Protected Resource", "This data has been protected by OAuth 2.0")

  private[this] val accessTokenKey: String = "access_token"

  // TODO cors
  def service(using Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(ProtectedResourcePage.index)
    case OPTIONS -> Root / "resource" => NoContent()
    case req @ POST -> Root / "resource" :? OptionalAccessTokenQueryParamMatcher(queryAccessToken) =>
      val oauthTokenRecord =
        for
          accessToken <- OptionT(req.headers.get[Authorization] match
            case Some(Authorization(Credentials.Token(AuthScheme.Bearer, token))) => IO(Some(token))
            case _ => req.as[UrlForm].attempt
              .map(_.toOption.flatMap(_.get(accessTokenKey).headOption).orElse(queryAccessToken))
          )
          _ <- info"Incoming token: $accessToken".optionT
          record <- OptionT(getRecordByAccessToken(accessToken))
        yield record
      oauthTokenRecord.value.flatMap {
        case Some(record) => info"We found a matching token: ${record.accessToken.getOrElse("")}" *>
          Ok(resource.asJson)
        case _ => info"No matching token was found." *>
          Unauthorized(`WWW-Authenticate`(Challenge(AuthScheme.Bearer.toString, "Protected Resource")))
      }
  }

  //noinspection HttpUrlsUsage
  val run: IO[Unit] =
    for
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      serverPort = port"8002"
      _ <- start[IO](serverPort)(service.orNotFound)
      _ <- info"OAuth Resource Server is listening at http://$serverHost:$serverPort"
      _ <- IO.never
    yield ()

