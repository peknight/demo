package com.peknight.demo.cats.effect.tutorial.producerconsumer

import cats.effect.concurrent.{Ref, Semaphore}
import cats.effect.{ContextShift, ExitCode, IO, IOApp, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.parallel._

import scala.collection.immutable.Queue

object ProducerConsumer extends IOApp {
  def producer[F[_]: Sync: ContextShift](id: Int, queueR: Ref[F, Queue[Int]], counterR: Ref[F, Int],
                                         filled: Semaphore[F]): F[Unit] = {
    (for {
      i <- counterR.getAndUpdate(_ + 1)
      _ <- queueR.getAndUpdate(_.enqueue(i))
      _ <- filled.release
      _ <- if (i % 10000 == 0) Sync[F].delay(println(s"Producer $id has reached $i items")) else Sync[F].unit
      _ <- ContextShift[F].shift
    } yield ()) >> producer(id, queueR, counterR, filled)
  }

  def consumer[F[_]: Sync: ContextShift](id: Int, queueR: Ref[F, Queue[Int]], filled: Semaphore[F]): F[Unit] = {
    (for {
      _ <- filled.acquire
      i <- queueR.modify(_.dequeue.swap)
      _ <- if (i % 10000 == 0) Sync[F].delay(println(s"Consumer $id has reached $i items")) else Sync[F].unit
      _ <- ContextShift[F].shift
    } yield ()) >> consumer(id, queueR, filled)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      queueR <- Ref.of[IO, Queue[Int]](Queue.empty[Int])
      counterR <- Ref.of[IO, Int](1)
      filled <- Semaphore[IO](0)
      producers = List.range(1, 11).map(producer(_, queueR, counterR, filled))
      consumers = List.range(1, 11).map(consumer(_, queueR, filled))
      res <- (producers ++ consumers)
        .parSequence.as(ExitCode.Success)
        .handleErrorWith { t =>
          IO(println(s"Error caught: ${t.getMessage}")).as(ExitCode.Error)
        }
    } yield res
  }
}
