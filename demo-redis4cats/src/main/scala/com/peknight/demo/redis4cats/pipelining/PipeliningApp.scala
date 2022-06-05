package com.peknight.demo.redis4cats.pipelining

import cats.effect.{IO, IOApp}
import cats.syntax.all.*
import com.peknight.demo.redis4cats.commandsApi
import dev.profunktor.redis4cats.*
import dev.profunktor.redis4cats.tx.TxStore

object PipeliningApp extends IOApp.Simple:
  val key1 = "testp1"
  val key2 = "testp2"
  val key3 = "testp3"

  val showResult: String => Option[String] => IO[Unit] = key =>
    _.fold(IO.println(s"Not found key: $key"))(s => IO.println(s"$key: $s"))

  val run = commandsApi.use { redis =>
    //noinspection DuplicatedCode
    val getters =
      redis.get(key1).flatTap(showResult(key1)) >>
        redis.get(key2).flatTap(showResult(key2)) >>
        redis.get(key3).flatTap(showResult(key3))

    val ops = (store: TxStore[IO, String, Option[String]]) => List(
      redis.set(key1, "osx"),
      redis.get(key3).flatMap(store.set(key3)),
      redis.set(key2, "linux")
    )

    val runPipeline = redis.pipeline(ops).flatMap(kv => IO.println(s"KV: $kv")).recoverWith {
      case e => IO.println(s"[Error] - ${e.getMessage}")
    }

    val prog =
      for
        _ <- redis.set(key3, "3")
        _ <- runPipeline
        v1 <- redis.get(key1)
        v2 <- redis.get(key2)
      yield
        assert(v1.contains("osx"))
        assert(v2.contains("linux"))

    getters >> prog >> getters >> IO.println("keep doing stuff...")
  }

