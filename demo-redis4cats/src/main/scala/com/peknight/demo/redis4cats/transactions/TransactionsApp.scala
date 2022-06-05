package com.peknight.demo.redis4cats.transactions

import cats.effect.{IO, IOApp, Resource}
import cats.syntax.all.*
import com.peknight.demo.redis4cats.{commandsApi, redisUri}
import dev.profunktor.redis4cats.*
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.effect.Log
import dev.profunktor.redis4cats.log4cats.*
import dev.profunktor.redis4cats.tx.{TransactionDiscarded, TxStore}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object TransactionsApp extends IOApp.Simple:

  given CanEqual[TransactionDiscarded.type, Throwable] = CanEqual.derived
  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  val key1 = "test1"
  val key2 = "test2"
  val key3 = "test3"

  val showResult: String => Option[String] => IO[Unit] = key =>
    _.fold(Log[IO].info(s"Key not found: $key"))(s => Log[IO].info(s"$key: $s"))

  commandsApi.use { redis =>
    val setters = redis.set(key2, "delete_me") >> redis.set(key3, "foo")
    val getters = redis.get(key1).flatTap(showResult(key1)) >> redis.get(key2).flatTap(showResult(key2))
    val ops = (store: TxStore[IO, String, Option[String]]) => List(
      redis.set(key1, "foo"),
      redis.del(key2).void,
      redis.get(key3).flatMap(store.set(key3))
    )
    val prog = redis.transact(ops).flatMap(kv => IO.println(s"KV: $kv")).recoverWith {
      case TransactionDiscarded => Log[IO].error("[Error] - Transaction Discarded")
      case e => Log[IO].error(s"[Error] - $e")
    }
    setters >> getters >> prog >> getters.void
  }

  // How NOT to use transactions
  val deadLock = commandsApi.use { redis =>
    val getters = redis.get(key1).flatTap(showResult(key1)) *> redis.get(key2).flatTap(showResult(key2))
    // You should never pass a transactional command: MULTI, EXEC or DISCARD.
    val setters = redis.transact_(List(redis.set(key1, "foo"), redis.set(key2, "bar"), redis.discard))
    getters *> setters.void *> getters.void
  }
  commandsApi.use { redis =>
    val getters = redis.get(key1).flatTap(showResult(key1)) *> redis.get(key2).flatTap(showResult(key2))
    val failedTx = redis.transact_(
      List(redis.set(key1, "foo"), redis.set(key2, "bar"), IO.raiseError(new Exception("boom")))
    )
    getters *> failedTx.void *> getters.void
  }

  // Optimistic locking
  val mkRedis: Resource[IO, RedisCommands[IO, String, String]] =
    RedisClient[IO].from(redisUri).flatMap(cli => Redis[IO].fromClient(cli, RedisCodec.Utf8))

  //noinspection DuplicatedCode
  def txProgram(v1: String, v2: String) = mkRedis.use { redis =>
    val getters =
      redis.get(key1).flatTap(showResult(key1)) >>
        redis.get(key2).flatTap(showResult(key2)) >>
        redis.get(key2).flatTap(showResult(key3))

    val ops = List(redis.set(key1, v1), redis.set(key2, v2))

    val prog: IO[Unit] = redis.transact_(ops).onError {
      case TransactionDiscarded => Log[IO].error("[Error] - Transaction Discarded")
      case e => Log[IO].error(s"[Error] - $e")
    }

    val watching = redis.watch(key1, key2)

    getters >> watching >> prog >> getters >> Log[IO].info("keep doing stuff...")
  }

  IO.race(txProgram("osx", "linux"), txProgram("foo", "bar")).void

  def retriableTx: IO[Unit] = txProgram("foo", "bar").recoverWith {
    case TransactionDiscarded => retriableTx
  }.uncancelable

  val run = IO.race(txProgram("nix", "guix"), retriableTx).void


