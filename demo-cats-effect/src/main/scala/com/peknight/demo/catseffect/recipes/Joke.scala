package com.peknight.demo.catseffect.recipes

import cats.effect.std.Supervisor
import cats.effect.{IO, IOApp}

import scala.concurrent.duration.*

object Joke extends IOApp.Simple:
  val run = Supervisor[IO](await = true).use { supervisor =>
    for
      _ <- supervisor.supervise(IO.sleep(50.millis) >> IO.print("MOO!"))
      _ <- IO.println("Q: Knock, knock!")
      _ <- IO.println("A: Who's there?")
      _ <- IO.println("Q: Interrupting cow.")
      _ <- IO.print("A: Interrupting cow") >> IO.sleep(50.millis) >> IO.println(" who?")
    yield ()
  }
