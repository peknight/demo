package com.peknight.demo.cats.effect.concepts

import cats.Monad
import cats.effect.std.Console
import cats.effect.{IO, IOApp}
import cats.syntax.all.*

import java.util.concurrent.{Executors, TimeUnit}

object ConceptsApp extends IOApp.Simple:

  lazy val loop: IO[Unit] = IO.println("Hello, World!") >> loop

  val scheduler = Executors.newScheduledThreadPool(1)

  val schedule = IO.async_[Unit] { cb =>
    scheduler.schedule(new Runnable { override def run(): Unit = cb(Right(())) }, 500, TimeUnit.MILLISECONDS)
    ()
  }

  def example[F[_]: Monad: Console](str: String): F[String] =
    val printer: F[Unit] = Console[F].println(str)
    (printer >> printer).as(str)

  val run = schedule.flatMap(IO.println) >>
    // 当你希望能中断执行时用`interruptible`（尝试打断一次）或`interruptibleMany`（不断尝试直到成功打断），不希望中断则用`blocking`即可
    IO.interruptible(Thread.sleep(500)) >>
    IO.println("Hello, World!") >>
    example[IO]("Hello, World") >>
    IO(scheduler.shutdown())
