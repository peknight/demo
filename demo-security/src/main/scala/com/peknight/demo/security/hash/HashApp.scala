package com.peknight.demo.security.hash

import cats.effect.{IO, IOApp, Sync}
import fs2.hashing.HashAlgorithm.{MD5, Named, SHA1}
import fs2.hashing.{HashAlgorithm, Hashing}
import fs2.text.{hex, utf8}
import fs2.{Pipe, Pure, Stream}
import org.bouncycastle.jce.provider.BouncyCastleProvider

import java.security.Security

object HashApp extends IOApp.Simple:

  def pipe[F[_]: Sync](algorithm: HashAlgorithm): Pipe[F, String, String] =
    in => in.through(utf8.encode)
      .through(Hashing.forSync[F].hash(algorithm))
      .through(_.mapChunks(_.flatMap(_.bytes)))
      .through(hex.encode)

  def md5[F[_]: Sync]: Pipe[F, String, String] = pipe[F](MD5)
  def sha1[F[_]: Sync]: Pipe[F, String, String] = pipe[F](SHA1)
  def ripeMD160[F[_]: Sync]: Pipe[F, String, String] = pipe[F](Named("RipeMD160"))

  def go(stream: Stream[Pure, String])(pipe: Pipe[IO, String, String]): IO[Unit] =
    stream.covary[IO].through(pipe).compile.toList.flatMap(list => IO.println(list.mkString("")))

  val initBouncyCastle: IO[Int] = IO(Security.addProvider(new BouncyCastleProvider()))

  val run: IO[Unit] =
    for
      _ <- go(Stream("Hello").append(Stream("World")))(md5)
      _ <- go(Stream("hello123"))(md5)
      _ <- go(Stream("12345678"))(md5)
      _ <- go(Stream("passw0rd"))(md5)
      _ <- go(Stream("19700101"))(md5)
      _ <- go(Stream("20201231"))(md5)
      _ <- go(Stream("H1r0a", "hello123"))(md5)
      _ <- go(Stream("7$p2w", "12345678"))(md5)
      _ <- go(Stream("z5Sk9", "passw0rd"))(md5)
      _ <- go(Stream("Hello").append(Stream("World")))(sha1)
      _ <- initBouncyCastle
      _ <- go(Stream("HelloWorld"))(ripeMD160)
    yield ()
