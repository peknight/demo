package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.{IO, IOApp}
import fs2.Stream
import fs2.concurrent.{SignallingRef, Topic}

object SinglePublisherMultipleSubscriberApp extends IOApp.Simple:
  val program =
    for
      topic <- Stream.eval(Topic[IO, Event])
      signal <- Stream.eval(SignallingRef[IO, Boolean](false))
      service = new EventService[IO](topic, signal)
      _ <- service.startPublisher.concurrently(service.startSubscribers)
    yield ()

  val run = program.compile.drain
