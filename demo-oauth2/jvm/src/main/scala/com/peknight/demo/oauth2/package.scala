package com.peknight.demo

import cats.{Applicative, Functor}
import cats.data.OptionT
import cats.effect.std.Random
import cats.effect.{Async, IO, Resource}
import cats.syntax.functor.*
import cats.syntax.option.*
import cats.syntax.traverse.*
import com.comcast.ip4s.*
import com.peknight.demo.oauth2.domain.OAuthTokenRecord
import fs2.io.file.Flags.Append
import fs2.io.file.{Files, Path}
import fs2.{Stream, text}
import io.circe.fs2.{byteStreamParser, decoder}
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import org.http4s.server.middleware.Logger
import io.circe.syntax.*
import scala.concurrent.duration.*

package object oauth2:

  val serverHost = host"localhost"

  def start[F[_]: Async](port: Port)(httpApp: HttpApp[F]): F[(Server, F[Unit])] =
    EmberServerBuilder.default[F]
      .withHost(serverHost)
      .withPort(port)
      .withHttpApp(Logger.httpApp(true, true)(httpApp))
      .build.allocated

  def randomString[F[_]: Applicative](random: Random[F], length: Int): F[String] =
    List.fill(length)(random.nextAlphaNumeric).sequence.map(_.mkString)

  val databaseNoSqlPath = Path("demo-oauth2/database.nosql")

  val clearRecord = Stream[IO, Byte]().through(Files[IO].writeAll(databaseNoSqlPath)).compile.drain

  def insertRecord(record: OAuthTokenRecord) =
    Stream(s"${record.asJson.noSpaces}\n").covary[IO]
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(databaseNoSqlPath, Append))
      .compile.drain
      .timeout(5.seconds)

  private[this] val tokenRecordStream: Stream[IO, OAuthTokenRecord] =
    Files[IO].readAll(databaseNoSqlPath).through(byteStreamParser).through(decoder[IO, OAuthTokenRecord])

  private[this] def findTokenRecord(p: OAuthTokenRecord => Boolean): IO[Option[OAuthTokenRecord]] =
    tokenRecordStream.filter(p).pull.take(1).void.stream.compile.toList.map(_.headOption)

  def getRecordByRefreshToken(refreshToken: String): IO[Option[OAuthTokenRecord]] = findTokenRecord {
    case OAuthTokenRecord(_, _, Some(refresh), _) if refresh == refreshToken => true
    case _ => false
  }

  def getRecordByAccessToken(accessToken: String): IO[Option[OAuthTokenRecord]] = findTokenRecord {
    case OAuthTokenRecord(_, Some(access), _, _) if access == accessToken => true
    case _ => false
  }

  def removeRecordByRefreshToken(refreshToken: String): IO[Unit] =
    for
      records <- tokenRecordStream.filter {
        case OAuthTokenRecord(_, _, Some(refresh), _) if refresh == refreshToken => false
        case _ => true
      }.compile.toList
      _ <- Stream(records.map(record => s"${record.asJson.noSpaces}\n")*).covary[IO]
        .through(text.utf8.encode)
        .through(Files[IO].writeAll(databaseNoSqlPath))
        .compile.drain
    yield ()

  extension [F[_]: Functor, A](fa: F[A])
    def optionT = OptionT(fa.map(_.some))


