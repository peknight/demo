package com.peknight.demo.cats.effect.tutorial.producerconsumer

import cats.effect.concurrent.{Ref, Semaphore}
import cats.effect.{Concurrent, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._

import scala.collection.immutable.Queue

trait SimpleConcurrentQueue[F[_], A] {
  def poll: F[A]
  def put(a: A): F[Unit]
}
object SimpleConcurrentQueue {

  protected def assertPositive[F[_]: Sync](n: Int): F[Unit] =
    if (n <= 0) Sync[F].raiseError(new IllegalArgumentException(s"Argument must be > 0 but it is $n"))
    else Sync[F].unit

  def unbounded[F[_]: Concurrent: Sync, A]: F[SimpleConcurrentQueue[F, A]] = {
    for {
      queueR <- Ref[F].of(Queue.empty[A])
      filled <- Semaphore[F](0)
    } yield buildUnbounded(queueR, filled)
  }

  def buildUnbounded[F[_]: Sync, A](queueR: Ref[F, Queue[A]], filled: Semaphore[F]): SimpleConcurrentQueue[F, A] =
    new SimpleConcurrentQueue[F, A] {
      override def poll: F[A] =
        Sync[F].uncancelable(
          for {
            _ <- filled.acquire
            a <- queueR.modify(_.dequeue.swap)
          } yield a
        )

      override def put(a: A): F[Unit] =
        Sync[F].uncancelable(
          for {
            _ <- queueR.getAndUpdate(_.enqueue(a))
            _ <- filled.release
          } yield ()
        )
    }

  def bounded[F[_]: Concurrent: Sync, A](size: Int): F[SimpleConcurrentQueue[F, A]] =
    for {
      _ <- assertPositive(size)
      queueR <- Ref[F].of(Queue.empty[A])
      filled <- Semaphore[F](0)
      empty <- Semaphore[F](size)
    } yield buildBounded(queueR, filled, empty)

  def buildBounded[F[_]: Sync, A](queueR: Ref[F, Queue[A]], filled: Semaphore[F], empty: Semaphore[F])
  : SimpleConcurrentQueue[F, A] =
    new SimpleConcurrentQueue[F, A] {
      override def poll: F[A] =
        Sync[F].uncancelable(
          for {
            _ <- filled.acquire
            a <- queueR.modify(_.dequeue.swap)
            _ <- empty.release
          } yield a
        )

      override def put(a: A): F[Unit] =
        Sync[F].uncancelable(
          for {
            _ <- empty.acquire
            _ <- queueR.getAndUpdate(_.enqueue(a))
            _ <- filled.release
          } yield ()
        )

    }
}
