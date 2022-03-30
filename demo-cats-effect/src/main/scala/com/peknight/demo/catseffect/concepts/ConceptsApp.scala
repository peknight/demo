package com.peknight.demo.catseffect.concepts

import cats.effect.{IO, IOApp}

import java.util.concurrent.{Executors, TimeUnit}

object ConceptsApp extends IOApp.Simple {

  lazy val loop: IO[Unit] = IO.println("Hello, World!") >> loop

  val scheduler = Executors.newScheduledThreadPool(1)

  val schedule = IO.async_[Unit] { cb =>
    scheduler.schedule(new Runnable { override def run(): Unit = cb(Right(())) }, 5000, TimeUnit.MILLISECONDS)
    ()
  }

  val run = schedule.flatMap(IO.println) >>
    IO.interruptible(Thread.sleep(5000)) >>
    IO.println("Hello, World!") >>
    IO(scheduler.shutdown())
}
