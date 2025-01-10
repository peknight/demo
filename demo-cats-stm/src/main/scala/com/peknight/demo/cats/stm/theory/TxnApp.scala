package com.peknight.demo.cats.stm.theory

import cats.effect.{IO, IOApp}
import cats.syntax.apply.*
import io.github.timwspence.cats.stm.STM

object TxnApp extends IOApp.Simple:

  class CatsSTM(val stm: STM[IO]):
    import stm.*
    val prog: IO[(Int, Int)] =
      for
        to <- stm.commit(TVar.of(0))
        from <- stm.commit(TVar.of(100))
        _ <- stm.commit {
          for
            balance <- from.get
            _ <- from.modify(_ - balance)
            _ <- to.modify(_ + balance)
          yield ()
        }
        v <- stm.commit((to.get, from.get).tupled)
      yield v
  end CatsSTM

  val run: IO[Unit] =
    for
      stm <- STM.runtime[IO]
      catsStm = CatsSTM(stm)
      result <- catsStm.prog
      _ <- IO.println(s"result=$result")
      // Retries
      to <- stm.commit(stm.TVar.of(1))
      from <- stm.commit(stm.TVar.of(0))
      retriesTxn = stm.commit {
        for
          balance <- from.get
          _ <- stm.check(balance > 100)
          _ <- from.modify(_ - 100)
          _ <- to.modify(_ + 100)
        yield ()
      }
      // _ <- retriesTxn // never ends
      // OrElse
      transferHundered: stm.Txn[Unit] =
        for
          b <- from.get
          _ <- stm.check(b > 100)
          _ <- from.modify(_ - 100)
          _ <- to.modify(_ + 100)
        yield ()
      transferRemaining: stm.Txn[Unit] =
        for
          balance <- from.get
          _ <- from.modify(_ - balance)
          _ <- to.modify(_ + balance)
        yield ()
      orElseTxn =
        for
          _ <- transferHundered.orElse(transferRemaining)
          f <- from.get
          t <- to.get
        yield (f, t)
      orElseResult <- stm.commit(orElseTxn)
      _ <- IO.println(orElseResult)
      // Aborting
      abortingTxn =
        for
          balance <- from.get
          _ <- if balance < 100 then stm.abort(new RuntimeException("Balance must be at least 100")) else stm.unit
          _ <- from.modify(_ - 100)
          _ <- to.modify(_ + 100)
        yield ()
      abortingResult <- stm.commit(abortingTxn).attempt
      _ <- IO.println(abortingResult)
      _ <- IO.println("Bye!")
    yield ()

