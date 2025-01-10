package com.peknight.demo.cats.effect.standard

import cats.effect.std.{Dispatcher, Queue}
import cats.effect.{IO, IOApp}

object DispatcherApp extends IOApp.Simple:

  abstract class ImpureInterface:
    def onMessage(msg: String): Unit
    def init(): Unit = onMessage("init")

  //noinspection DuplicatedCode
  val queueDemo =
    for
      queue <- Queue.unbounded[IO, String]
      impureInterface = new ImpureInterface:
        // This returns an IO, so nothing really happens
        override def onMessage(msg: String): Unit = queue.offer(msg)
      _ <- IO.delay(impureInterface.init())
      value <- queue.tryTake
      _ <- value match
        case Some(v) => IO.println(s"Value found in queue! $v")
        case None => IO.println("Value not found in queue :(")
    yield ()

  //noinspection DuplicatedCode
  val dispatcherDemo = Dispatcher.sequential[IO].use { dispatcher =>
    for
      queue <- Queue.unbounded[IO, String]
      impureInterface <- IO.delay {
        new ImpureInterface:
          override def onMessage(msg: String): Unit = dispatcher.unsafeRunSync(queue.offer(msg))
      }
      _ <- IO.delay(impureInterface.init())
      value <- queue.tryTake
      _ <- value match
        case Some(v) => IO.println(s"Value found in queue! $v")
        case None => IO.println("Value not found in queue :(")
    yield ()
  }

  val run = dispatcherDemo
