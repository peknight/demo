package com.peknight.demo.fs2.concurrencyprimitives

import cats.effect.std.Queue
import cats.effect.{IO, IOApp}
import cats.implicits.toTraverseOps
import fs2.Stream

object QueueApp extends IOApp.Simple {

  val program = for {
    queue <- Queue.unbounded[IO, Option[Int]]
    streamFromQueue = Stream.fromQueueNoneTerminated(queue)
    _ <- Seq(Some(1), Some(2), Some(3), None).map(queue.offer).sequence
    result <- streamFromQueue.compile.toList
  } yield result

  val run = for {
    _ <- program.flatMap(IO.println)
  } yield ()
}
