package com.peknight.demo.catseffect.standard

import cats.effect.std.{AtomicCell, Mutex}
import cats.effect.{IO, Ref}

object AtomicCellApp:

  trait State

  class ServiceWithMutexAndRef(mtx: Mutex[IO], ref: Ref[IO, State]):
    def modify(f: State => IO[State]): IO[Unit] =
      mtx.lock.surround {
        for
          current <- ref.get
          next <- f(current)
          _ <- ref.set(next)
        yield ()
      }
  end ServiceWithMutexAndRef

  class Service(cell: AtomicCell[IO, State]):
    def modify(f: State => IO[State]): IO[Unit] = cell.evalUpdate(f)
  end Service
