package com.peknight.demo.oauth2

import cats.effect.IO
import com.peknight.demo.oauth2.domain.OAuthTokenRecord
import fs2.io.file.Flags.Append
import fs2.io.file.{Files, Path}
import fs2.{Stream, text}
import io.circe.fs2.{byteStreamParser, decoder}
import io.circe.syntax.*

import scala.concurrent.duration.*

package object repository:

  private[this] val databaseNoSqlPath: Path = Path("demo-oauth2/database.nosql")

  val clearRecord: IO[Unit] = Stream[IO, Byte]().through(Files[IO].writeAll(databaseNoSqlPath)).compile.drain

  // 为了增强突发，你可以只存储令牌值的加密散列
  def insertRecord(record: OAuthTokenRecord): IO[Unit] =
    Stream(s"${record.asJson.deepDropNullValues.noSpaces}\n").covary[IO]
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(databaseNoSqlPath, Append))
      .compile.drain
      .timeout(5.seconds)

  private[this] val tokenRecordStream: Stream[IO, OAuthTokenRecord] =
    Files[IO].readAll(databaseNoSqlPath).through(byteStreamParser).through(decoder[IO, OAuthTokenRecord])

  private[this] def findTokenRecord(p: OAuthTokenRecord => Boolean): IO[Option[OAuthTokenRecord]] =
    tokenRecordStream.filter(p).pull.take(1).void.stream.compile.toList.map(_.headOption)

  def getRecordByRefreshToken(refreshToken: String): IO[Option[OAuthTokenRecord]] = findTokenRecord {
    case OAuthTokenRecord(_, _, Some(refresh), _, _) if refresh == refreshToken => true
    case _ => false
  }

  def getRecordByAccessToken(accessToken: String): IO[Option[OAuthTokenRecord]] = findTokenRecord {
    case OAuthTokenRecord(_, Some(access), _, _, _) if access == accessToken => true
    case _ => false
  }

  def removeRecordByRefreshToken(refreshToken: String): IO[Unit] =
    for
      records <- tokenRecordStream.filter {
        case OAuthTokenRecord(_, _, Some(refresh), _, _) if refresh == refreshToken => false
        case _ => true
      }.compile.toList
      _ <- overwriteRecords(records)
    yield ()

  def removeRecordByAccessTokenAndClientId(inToken: String, clientId: String): IO[Int] =
    removeRecord(record => record.clientId.contains(clientId) && record.accessToken.contains(inToken))

  def removeRecordByClientId(clientId: String): IO[Int] =
    removeRecord(_.clientId.contains(clientId))

  def removeRecord(f: OAuthTokenRecord => Boolean): IO[Int] =
    for
      originRecords <- tokenRecordStream.compile.toList
      records = originRecords.filterNot(f)
      _ <- overwriteRecords(records)
    yield originRecords.size - records.size

  def overwriteRecords(records: List[OAuthTokenRecord]): IO[Unit] =
    Stream(records.map(record => s"${record.asJson.deepDropNullValues.noSpaces}\n") *)
      .covary[IO]
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(databaseNoSqlPath))
      .compile.drain