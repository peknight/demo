package com.peknight.demo.scalacheck.effect

import org.scalacheck.effect.PropF
import org.scalacheck.Test
import cats.effect.{ExitCode, IO, IOApp}

object Example extends IOApp.Simple:
  val run: IO[Unit] =
    val p: PropF[IO] = PropF.forAllF { (x: Int) => IO(x).map(res => assert(res == x)) }
    for
      result <- p.check()
      _ <- IO.println(result)
    yield ()
