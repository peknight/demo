package com.peknight.demo.redis4cats.streams

import cats.effect.{IO, IOApp}
import cats.syntax.all.*
import com.peknight.demo.redis4cats.redisUri
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.data.*
import dev.profunktor.redis4cats.effects.XReadOffsets
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

  val streamKey1 = "demo_stream"
  val streamKey2 = "users_stream"

  def randomMessage: Stream[IO, XAddMessage[String, String]] = Stream.evals {
    val rndKey = IO(Random.nextInt(1000).toString)
    val rndValue = IO(Random.nextString(10))
    (rndKey, rndValue).parMapN { case (k, v) =>
      List(
        XAddMessage(streamKey1, Map(k -> v)),
        XAddMessage(streamKey2, Map(k -> v))
      )
    }
  }

  private val readStream: Stream[IO, Unit] =
    for
      redis <- Stream.resource(Redis[IO].simple(redisUri, stringCodec))
      streaming = RedisStream[IO, String, String](redis)
      message <- streaming.read(XReadOffsets.all(streamKey1, streamKey2))
      _ <- Stream.eval(IO.println(message))
    yield
      ()

  private val writeStream: Stream[IO, Unit] =
    for
      redis <- Stream.resource(Redis[IO].simple(redisUri, stringCodec))
      streaming = RedisStream[IO, String, String](redis)
      message <- Stream.awakeEvery[IO](2.seconds)
      _ <- randomMessage.through(streaming.append)
    yield
      ()

  def run: IO[Unit] =
    readStream.concurrently(writeStream).interruptAfter(5.seconds).compile.drain

end StreamingExample
