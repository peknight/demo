package com.peknight.demo.catseffect.concepts

import cats.Monad
import cats.effect.std.Console
import cats.effect.{IO, IOApp}
import cats.syntax.all._

import java.util.concurrent.{Executors, TimeUnit}

object ConceptsApp extends IOApp.Simple {

  lazy val loop: IO[Unit] = IO.println("Hello, World!") >> loop

  val scheduler = Executors.newScheduledThreadPool(1)

  val schedule = IO.async_[Unit] { cb =>
    scheduler.schedule(new Runnable { override def run(): Unit = cb(Right(())) }, 500, TimeUnit.MILLISECONDS)
    ()
  }

  def example[F[_]: Monad: Console](str: String): F[String] = {
    val printer: F[Unit] = Console[F].println(str)
    (printer >> printer).as(str)
  }

  val run = schedule.flatMap(IO.println) >>
    IO.interruptible(Thread.sleep(500)) >>
    IO.println("Hello, World!") >>
    example[IO]("Hello, World") >>
    IO(scheduler.shutdown())

}
