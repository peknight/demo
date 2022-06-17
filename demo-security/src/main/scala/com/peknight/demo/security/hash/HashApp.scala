package com.peknight.demo.security.hash

import cats.effect.{IO, IOApp}
import fs2.text.{hex, utf8}
import fs2.{Pipe, Pure, Stream, hash}
import org.bouncycastle.jce.provider.BouncyCastleProvider

import java.security.{MessageDigest, Security}

object HashApp extends IOApp.Simple:

  val md5: Pipe[Pure, String, String] = in => in.through(utf8.encode).through(hash.md5).through(hex.encode)
  val sha1: Pipe[Pure, String, String] = in => in.through(utf8.encode).through(hash.sha1).through(hex.encode)

  val initBouncyCastle: IO[Int] = IO(Security.addProvider(new BouncyCastleProvider()))
  val ripeMD160: Pipe[Pure, String, String] = in =>
    in.through(utf8.encode).through(hash.digest(MessageDigest.getInstance("RipeMD160"))).through(hex.encode)

  val run: IO[Unit] =
    for
      _ <- IO.println(Stream("Hello").append(Stream("World")).through(md5).toList.mkString(""))
      _ <- IO.println(Stream("hello123").through(md5).toList.mkString(""))
      _ <- IO.println(Stream("12345678").through(md5).toList.mkString(""))
      _ <- IO.println(Stream("passw0rd").through(md5).toList.mkString(""))
      _ <- IO.println(Stream("19700101").through(md5).toList.mkString(""))
      _ <- IO.println(Stream("20201231").through(md5).toList.mkString(""))
      _ <- IO.println(Stream("H1r0a", "hello123").through(md5).toList.mkString(""))
      _ <- IO.println(Stream("7$p2w", "12345678").through(md5).toList.mkString(""))
      _ <- IO.println(Stream("z5Sk9", "passw0rd").through(md5).toList.mkString(""))
      _ <- IO.println(Stream("Hello").append(Stream("World")).through(sha1).toList.mkString(""))
      _ <- initBouncyCastle
      _ <- IO.println(Stream("HelloWorld").through(ripeMD160).toList.mkString(""))
    yield ()

