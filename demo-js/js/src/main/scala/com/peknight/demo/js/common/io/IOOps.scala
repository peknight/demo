package com.peknight.demo.js.common.io

import cats.effect.IO

object IOOps:

  extension[A] (io: IO[A])
    def run(): Unit =
      import cats.effect.unsafe.implicits.global
      io.unsafeRunAndForget()
  end extension
