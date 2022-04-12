package com.peknight.demo.fs2.guide

import cats.effect.{IO, IOApp, Resource}
import fs2.Stream
import fs2.interop.reactivestreams.{StreamOps, StreamUnicastPublisher}

object ReactiveStreamsApp extends IOApp.Simple {

  val stream = Stream(1, 2, 3).covary[IO]

  val publisher: Resource[IO, StreamUnicastPublisher[IO, Int]] = stream.toUnicastPublisher

  publisher.use { p => p.toStreamBuffered[IO](1).compile.toList }

  val run = for {
    _ <- publisher.use { p => p.toStreamBuffered[IO](1).compile.toList }.flatMap(IO.println)
  } yield ()
}
