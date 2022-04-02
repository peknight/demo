package com.peknight.demo.catseffect.tutorial.producerconsumer

import cats.effect._
import cats.effect.std.Console
import cats.syntax.all._

object ProducerConsumerQueue extends IOApp {

  def producer[F[_]: Async: Console](id: Int, counterR: Ref[F, Int], queue: Queue[F, Int]): F[Unit] = for {
    i <- counterR.getAndUpdate(_ + 1)
    _ <- queue.offer(i)
    _ <- if (i % 10000 == 0) Console[F].println(s"Producer $id has reached $i items") else Async[F].unit
    _ <- producer(id, counterR, queue)
  } yield ()

  def consumer[F[_]: Async: Console](id: Int, queue: Queue[F, Int]): F[Unit] = for {
    i <- queue.take
    _ <- if (i % 10000 == 0) Console[F].println(s"Consumer $id has reached $i items") else Async[F].unit
    _ <- consumer(id, queue)
  } yield()

  override def run(args: List[String]): IO[ExitCode] = for {
    queue <- Queue[IO, Int](100)
    counterR <- Ref.of[IO, Int](1)
    producers = List.range(1, 11).map(producer(_, counterR, queue))
    consumers = List.range(1, 11).map(consumer(_, queue))
    res <- (producers ++ consumers).parSequence.as(ExitCode.Success).handleErrorWith { t =>
      Console[IO].errorln(s"Error caught: ${t.getMessage}").as(ExitCode.Error)
    }
  } yield res
}
