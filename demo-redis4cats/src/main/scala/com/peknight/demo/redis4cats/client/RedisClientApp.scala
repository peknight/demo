package com.peknight.demo.redis4cats.client

import cats.effect.{IO, IOApp, Resource}
import cats.syntax.option.*
import com.peknight.demo.redis4cats.redisUri
import dev.profunktor.redis4cats.*
import dev.profunktor.redis4cats.algebra.StringCommands
import dev.profunktor.redis4cats.config.*
import dev.profunktor.redis4cats.connection.*
import dev.profunktor.redis4cats.data.{ReadFrom, RedisCodec}
import dev.profunktor.redis4cats.log4cats.*
// 如果不需要日志可以使用NoOp替换redis4cats.log4cats
// import dev.profunktor.redis4cats.effect.Log.NoOp.*
// 如果只想debug在stdout看日志那么可以用
// import dev.profunktor.redis4cats.effect.Log.Stdout.*
import io.lettuce.core.{ClientOptions, TimeoutOptions, RedisURI as JRedisURI}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.time.Duration
import scala.concurrent.duration.*

object RedisClientApp extends IOApp.Simple:

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  val stringCodec: RedisCodec[String, String] = RedisCodec.Utf8

  val commandsApi: Resource[IO, StringCommands[IO, String, String]] =
    RedisClient[IO]
      .from(redisUri)
      .flatMap(Redis[IO].fromClient(_, stringCodec))

  val mkOpts: IO[ClientOptions] = IO {
    ClientOptions.builder()
      .autoReconnect(false)
      .pingBeforeActivateConnection(false)
      .timeoutOptions(
        TimeoutOptions.builder()
          .fixedTimeout(Duration.ofSeconds(10))
          .build()
      )
      .build()
  }

  val api: Resource[IO, StringCommands[IO, String, String]] =
    for
      opts <- Resource.eval(mkOpts)
      client <- RedisClient[IO].withOptions(redisUri, opts)
      redis <- Redis[IO].fromClient(client, stringCodec)
    yield redis

  val config = Redis4CatsConfig().withShutdown(ShutdownConfig(1.seconds, 5.seconds))

  val configuredApi: Resource[IO, StringCommands[IO, String, String]] =
    for
      uri <- Resource.eval(RedisURI.make[IO](redisUri))
      opts <- Resource.eval(mkOpts)
      client <- RedisClient[IO].custom(uri, opts, config)
      redis <- Redis[IO].fromClient(client, stringCodec)
    yield redis

  val simpleApi: Resource[IO, StringCommands[IO, String, String]] =
    Redis[IO].simple(redisUri, RedisCodec.Ascii)

  val simpleOptsApi: Resource[IO, StringCommands[IO, String, String]] =
    Resource.eval(IO(ClientOptions.create())).flatMap { opts =>
      Redis[IO].withOptions(redisUri, opts, RedisCodec.Ascii)
    }

  val utf8Api: Resource[IO, StringCommands[IO, String, String]] = Redis[IO].utf8(redisUri)

  val jRedisUriApi: Resource[IO, StringCommands[IO, String, String]] =
    for
      uri <- RedisClient[IO].fromUri(RedisURI.fromUnderlying(
        JRedisURI.Builder.redis("localhost", 6379)
          .withAuthentication("", "redis4cats")
          .build()
      ))
      redis <- Redis[IO].fromClient(uri, stringCodec)
    yield redis

  val clusterApi: Resource[IO, StringCommands[IO, String, String]] =
    for
      uri <- Resource.eval(RedisURI.make[IO]("redis://localhost:30001"))
      client <- RedisClusterClient[IO](uri)
      redis <- Redis[IO].fromClusterClient(client, stringCodec)()
    yield redis

  // 不需要重用client的情况可以简化为
  val clusterUtf8Api: Resource[IO, StringCommands[IO, String, String]] =
    Redis[IO].clusterUtf8("redis://localhost:30001")()

  val commands: Resource[IO, StringCommands[IO, String, String]] =
    for
      uri <- Resource.eval(RedisURI.make[IO](redisUri))
      conn <- RedisMasterReplica[IO].make(RedisCodec.Utf8, uri)(ReadFrom.UpstreamPreferred.some)
      redis <- Redis[IO].masterReplica(conn)
    yield redis

  def run =
    for
      foo <- commands.use { redis => redis.set("foo", "123") *> redis.get("foo") }
      _ <- IO.println(foo)
    yield ()

