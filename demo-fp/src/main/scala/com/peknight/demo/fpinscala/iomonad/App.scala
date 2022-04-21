package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.iomonad.App.unsafePerformIO
import com.peknight.demo.fpinscala.monads.Monad.given
import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par

import java.util.concurrent.{ExecutorService, Executors}

abstract class App:

  def main(args: Array[String]): Unit =
    given pool: ExecutorService = Executors.newFixedThreadPool(8)
    unsafePerformIO(pureMain(args.toIndexedSeq))

  def pureMain(args: IndexedSeq[String]): App.IO[Unit]

object App:

  type IO[A] = Free[Par, A]

  def unsafePerformIO[A](io: IO[A])(using E: ExecutorService): A = Par.run(E) { Free.run(io) }
