package com.peknight.demo.cats.stm.gettingstarted

import cats.effect.{IO, IOApp}
import io.github.timwspence.cats.stm.*

object GettingStartedApp extends IOApp.Simple:
  // Defining a transaction
  def wibble(stm: STM[IO])(tvar: stm.TVar[Int]): stm.Txn[Int] =
    for
      current <- tvar.get
      updated = current + 1
      _ <- tvar.set(updated)
    yield updated

  // Committing a transaction
  val runIO: IO[Int] =
    def runSTM(stm: STM[IO]): IO[Int] =
      import stm.*
      for
        tvar <- stm.commit(TVar.of[Int](0))
        res <- stm.commit(wibble(stm)(tvar))
      yield res
    STM.runtime[IO].flatMap(runSTM)

  class CatsSTM(val stm: STM[IO]):
    import stm.*
    // Retrying a transaction
    def waitTillValueExceeds100(tvar: TVar[Int]): IO[Int] =
      stm.commit(
        for
          current <- tvar.get
          // 这里包含retry逻辑
          _ <- stm.check(current > 100)
        yield current
      )
    // Alternatives
    def transferAvailableFunds(from1: TVar[Int], from2: TVar[Int], to: TVar[Int]): IO[Unit] =
      def transferFrom(from: TVar[Int]): Txn[Unit] =
        for
          current <- from.get
          _ <- stm.check(current > 100)
          _ <- from.modify(_ - 100)
          _ <- to.modify(_ + 100)
        yield ()
      stm.commit(transferFrom(from1).orElse(transferFrom(from2)))
  end CatsSTM


  val run: IO[Unit] =
    for
      res <- runIO
      _ <- IO.println(res)
    yield ()
