package com.peknight.demo.cats.stm.theory

import cats.effect.{IO, IOApp}
import io.github.timwspence.cats.stm.STM

object TVarApp extends IOApp.Simple:
  val run: IO[Unit] =
    for
      stm <- STM.runtime[IO]
      to <- stm.commit(stm.TVar.of(1))
      from <- stm.commit(stm.TVar.of(0))
      txn: stm.Txn[(Int, Int)] =
        for
          balance <- from.get
          _ <- from.modify(_ - balance)
          _ <- to.modify(_ + balance)
          res1 <- from.get
          res2 <- to.get
        yield (res1, res2)
      result <- stm.commit(txn)
      _ <- IO.println(result)
    yield ()

