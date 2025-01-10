package com.peknight.demo.cats.effect.gettingstarted

import cats.effect.{IO, IOApp}

import scala.concurrent.duration.DurationInt

object StupidFizzBuzz extends IOApp.Simple:
  val run =
    for
      ctr <- IO.ref(0)

      wait = IO.sleep(1.second)
      poll = wait *> ctr.get

      _ <- poll.flatMap(IO.println(_)).foreverM.start
      _ <- poll.map(_ % 3 == 0).ifM(IO.println("fizz"), IO.unit).foreverM.start
      _ <- poll.map(_ % 5 == 0).ifM(IO.println("buzz"), IO.unit).foreverM.start

      _ <- (wait *> ctr.update(_ + 1)).foreverM.void
    yield ()
