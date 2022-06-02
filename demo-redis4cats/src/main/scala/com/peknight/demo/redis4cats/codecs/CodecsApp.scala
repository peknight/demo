package com.peknight.demo.redis4cats.codecs

import cats.effect.{IO, IOApp}
import com.peknight.demo.redis4cats.redisUri
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.codecs.Codecs
import dev.profunktor.redis4cats.codecs.splits.*
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.effect.Log.NoOp.*
import io.circe.generic.auto.*
import io.circe.parser.decode as jsonDecode
import io.circe.syntax.*

import javax.crypto.spec.SecretKeySpec
import scala.util.Try

object CodecsApp extends IOApp.Simple:

  // Compression
  val gzipCodec: RedisCodec[String, String] = RedisCodec.gzip(RedisCodec.Utf8)

  // Encryption
  def mkCodec(key: SecretKeySpec): IO[RedisCodec[String, String]] =
    for
      e <- RedisCodec.encryptSupplier[IO](key)
      d <- RedisCodec.decryptSupplier[IO](key)
    yield RedisCodec.secure(RedisCodec.Utf8, e, d)

  // Deriving codecs
  val stringDoubleEpi: SplitEpi[String, Double] =
    SplitEpi(s => Try(s.toDouble).getOrElse(0), _.toString)

  val stringLongEpi: SplitEpi[String, Long] =
    SplitEpi(s => Try(s.toLong).getOrElse(0), _.toString)

  val stringIntEpi: SplitEpi[String, Int] =
    SplitEpi(s => Try(s.toInt).getOrElse(0), _.toString)

  val longCodec: RedisCodec[String, Long] = Codecs.derive(RedisCodec.Utf8, stringLongEpi)

  val keysSplitEpi: SplitEpi[String, Keys] = SplitEpi(Keys.apply, _.value)

  val keysCodec: RedisCodec[Keys, Long] = Codecs.derive(RedisCodec.Utf8, keysSplitEpi, stringLongEpi)

  val eventSplitEpi: SplitEpi[String, Event] =
    SplitEpi[String, Event](str => jsonDecode[Event](str).getOrElse(Event.Unknown), _.asJson.noSpaces)

  val eventsCodec: RedisCodec[String, Event] = Codecs.derive(RedisCodec.Utf8, eventSplitEpi)

  val eventsKey = "events"


  val run =
    Redis[IO].simple(redisUri, eventsCodec).use { redis =>
      for
        x <- redis.sCard(eventsKey)
        _ <- IO.println(s"Number of events: $x")
        _ <- redis.sAdd(eventsKey, Event.Ack(1), Event.Message(23, "foo"))
        y <- redis.sMembers(eventsKey)
        _ <- IO.println(s"Events: $y")
      yield ()
    }