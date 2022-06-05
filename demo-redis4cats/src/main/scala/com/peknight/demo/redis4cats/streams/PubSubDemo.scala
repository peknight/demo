package com.peknight.demo.redis4cats.streams

import cats.effect.{IO, IOApp}
import cats.syntax.all.*
import com.peknight.demo.redis4cats.redisUri
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.data.*
import dev.profunktor.redis4cats.log4cats.*
import dev.profunktor.redis4cats.pubsub.PubSub
import fs2.{Pipe, Stream}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.duration.*
import scala.util.Random

object PubSubDemo extends IOApp.Simple:

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  private val stringCodec = RedisCodec.Utf8
  private val eventsChannel = RedisChannel("events")
  private val gamesChannel = RedisChannel("games")

  def sink(name: String): Pipe[IO, String, Unit] = _.evalMap(x => IO.println(s"Subscriber: $name >> $x"))

  val program: Stream[IO, Unit] =
    for
      client <- Stream.resource(RedisClient[IO].from(redisUri))
      pubSub <- Stream.resource(PubSub.mkPubSubConnection[IO, String, String](client, stringCodec))
      sub1 = pubSub.subscribe(eventsChannel)
      sub2 = pubSub.subscribe(gamesChannel)
      pub1 = pubSub.publish(eventsChannel)
      pub2 = pubSub.publish(gamesChannel)
      _ <- Stream(
        sub1.through(sink("#events")),
        sub2.through(sink("#games")),
        Stream.awakeEvery[IO](3.seconds) >> Stream.eval(IO(Random.nextInt(100).toString)).through(pub1),
        Stream.awakeEvery[IO](5.seconds) >> Stream.emit("Pac-Man!").through(pub2),
        Stream.awakeEvery[IO](11.seconds) >> pubSub.unsubscribe(gamesChannel),
        Stream.awakeEvery[IO](6.seconds) >> pubSub.pubSubSubscriptions(List(eventsChannel, gamesChannel)).evalMap(x => IO.println(x))
      ).parJoin(6).void
    yield ()

  val run = program.compile.drain
