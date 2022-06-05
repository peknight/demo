package com.peknight.demo.redis4cats.streams

import cats.effect.{IO, IOApp}
import cats.syntax.all.*
import com.peknight.demo.redis4cats.redisUri
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.data.*
import dev.profunktor.redis4cats.log4cats.*
import dev.profunktor.redis4cats.streams.RedisStream
import dev.profunktor.redis4cats.streams.data.*
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.duration.*
import scala.util.Random


object StreamingExample extends IOApp.Simple:

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  val stringCodec = RedisCodec.Utf8

  def putStrLn[A](a: A): IO[Unit] = IO.println(a)

  val streamKey1 = "demo_stream"
  val streamKey2 = "users_stream"

  def randomMessage: Stream[IO, XAddMessage[String, String]] = Stream.eval {
    val rndKey = IO(Random.nextInt(1000).toString)
    val rndValue = IO(Random.nextString(10))
    (rndKey, rndValue).parMapN { case (k, v) => XAddMessage(streamKey1, Map(k -> v))}
  }

  def run: IO[Unit] = {
    for
      client <- Stream.resource(RedisClient[IO].from(redisUri))
      streaming <- RedisStream.mkStreamingConnection[IO, String, String](client, stringCodec)
      source = streaming.read(Set(streamKey1, streamKey2), chunkSize = 1)
      appender = streaming.append
      _ <- Stream(
        source.evalMap(putStrLn),
        Stream.awakeEvery[IO](3.seconds) >> randomMessage.through(appender)
      ).parJoin(2).void
    yield ()
  }.compile.drain
