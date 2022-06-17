package com.peknight.demo.security.codec

import cats.effect.std.Console
import cats.effect.{Concurrent, IO, IOApp}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import fs2.text.base64
import fs2.{RaiseThrowable, Stream}

/**
 * 3个字节一组
 */
object Base64App extends IOApp.Simple:

  def demo[F[_]: Concurrent: RaiseThrowable: Console](input: Int*): F[Unit] =
    val b64encodedStream = Stream(input*).map(_.toByte).through(base64.encode)
    val b64decodedStream = b64encodedStream.covary[F].through(base64.decode)
    for
      _ <- Console[F].println(s" -- input: ${input.map(_.toByte).mkString("[", ", ", "]")}")
      _ <- Console[F].println(s"encoded: ${b64encodedStream.toList.mkString("")}")
      decodedList <- b64decodedStream.compile.toList
      _ <- Console[F].println(s"decoded: ${decodedList.mkString("[", ", ", "]")}")
    yield ()

  val run: IO[Unit] =
    for
      _ <- demo[IO](0xe4, 0xb8, 0xad)
      _ <- demo[IO](0x01, 0x02, 0x7f, 0x00)
      _ <- demo[IO](0xff, 0xa3, 0x98, 0x01, 0x02, 0x7f, 0x00)
      _ <- demo[IO](0xa3, 0x98, 0x01, 0x02, 0x7f, 0x00)
      _ <- demo[IO](0x98, 0x01, 0x02, 0x7f, 0x00)
      _ <- demo[IO](0x01, 0x02, 0x7f, 0x00)
      _ <- demo[IO](0x02, 0x7f, 0x00)
      _ <- demo[IO](0x7f, 0x00)
      _ <- demo[IO](0x00)
    yield ()