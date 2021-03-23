package com.peknight.demo.cats.effect.tutorial.producerconsumer

import cats.effect.concurrent.Ref
import cats.effect.{ContextShift, ExitCode, IO, IOApp, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.parallel._

object ConcurrentQueueRunner extends IOApp {
  def process[F[_]: Sync](i: Int): F[Unit] =
    if (i % 10000 == 0) Sync[F].delay(println(s"Processed $i elements"))
    else Sync[F].unit

  def producer[F[_]: Sync: ContextShift](counterR: Ref[F, Int], cq: ConcurrentQueue[F, Int]): F[Unit] =
    (counterR.getAndUpdate(_ + 1) >>= cq.put) >> ContextShift[F].shift >> producer(counterR, cq)

  def consumer[F[_]: Sync: ContextShift](cq: ConcurrentQueue[F, Int]): F[Unit] =
    (cq.poll >>= process[F]) >> ContextShift[F].shift >> consumer(cq)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      cq <- ConcurrentQueue.bounded[IO, Int](10000)
      counterR <- Ref.of[IO, Int](0)
      producers = List.range(1, 11).as(producer[IO](counterR, cq))
      consumers = List.range(1, 11).as(consumer[IO](cq))
      res <- (producers ++ consumers)
        .parSequence.as(ExitCode.Success)
        .handleErrorWith { t =>
          IO(println(s"Error caught: ${t.getMessage}")).as(ExitCode.Error)
        }
    } yield res
}
