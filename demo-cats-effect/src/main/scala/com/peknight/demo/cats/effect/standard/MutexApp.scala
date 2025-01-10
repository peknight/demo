package com.peknight.demo.cats.effect.standard

import cats.effect.{IO, Ref}
import cats.effect.std.Mutex

object MutexApp:
  trait State

  class Service(mutex: Mutex[IO], ref: Ref[IO, State]):
    def withState(f: State => IO[Unit]): IO[Unit] =
      mutex.lock.surround {
        for
          current <- ref.get
          _ <- f(current)
        yield ()
      }

