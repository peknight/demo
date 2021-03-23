package com.peknight.demo.cats.effect.tutorial.producerconsumer

import cats.effect.concurrent.{Ref, Semaphore}
import cats.effect.{Concurrent, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._

/**
 *  For a production-ready, high-performance concurrent queue it is strongly advised to consider
 *  Monix’s concurrent queue,
 *  which is cats-effect compatible.
 *
 * https://monix.io/api/current/monix/catnap/ConcurrentQueue.html
 */
trait ConcurrentQueue[F[_], A] {
  /** Get and remove first element from queue, blocks if queue empty. */
  def poll: F[A]
  /** Get and remove first `n` elements from queue, blocks if less than `n` items are available in queue.
   * Error raised if `n < 0` or, in bounded queues, if `n > max size of queue`.*/
  def pollN(n: Int): F[List[A]]
  /** Get, but not remove, first element in queue, blocks if queue empty. */
  def peek: F[A]
  /** Get, but not remove, first `n` elements in queue, blocks if less than `n` items are available in queue.
   * Error raised if `n < 0` or, in bounded queues, if `n > max size of queue`.*/
  def peekN(n: Int): F[List[A]]
  /** Put element at then end of queue, blocks if queue is bounded and full. */
  def put(a: A): F[Unit]
  /** Puts elements at the end of the queue, blocks if queue is bounded and does not have spare size for all items.
   * Error raised in bounded queues if `as.size > max size of queue`.*/
  def putN(as: List[A]): F[Unit]
  /** Try to get and remove first element from queue, immediately returning `F[None]` if queue empty. Non-blocking. */
  def tryPoll: F[Option[A]]
  /** Try to get and remove first `n` elements from queue, immediately returning `F[None]` if less than `n` items are available in queue. Non-blocking.
   * Error raised if n < 0. */
  def tryPollN(n: Int): F[Option[List[A]]]
  /** Try to get, but not remove, first element from queue, immediately returning `F[None]` if queue empty. Non-blocking. */
  def tryPeek: F[Option[A]]
  /** Try to get, but not remove, first  `n` elements from queue, immediately returning `F[None]` if less than `n` items are available in queue. Non-blocking.
   * Error raised if n < 0. */
  def tryPeekN(n: Int): F[Option[List[A]]]
  /** Try to put element at the end of queue, immediately returning `F[false]` if queue is bounded and full. Non-blocking. */
  def tryPut(a: A): F[Boolean]
  /** Try to put elements in list at the end of queue, immediately returning `F[false]` if queue is bounded and does not have spare size for all items. Non-blocking. */
  def tryPutN(as: List[A]): F[Boolean]
  /** Returns # of items in queue. Non-blocking. */
  def size: F[Long]
  /** Returns `F[true]` if queue empty, `F[false]` otherwise. Non-blocking. */
  def isEmpty: F[Boolean]
}

object ConcurrentQueue {

  private def assertNonNegative[F[_]: Sync](n: Int): F[Unit] =
    if (n < 0) Sync[F].raiseError(new IllegalArgumentException(s"Argument $n must be >= 0 but it is $n"))
    else Sync[F].unit

  private def assertPositive[F[_]: Sync](n: Int): F[Unit] =
    if (n <= 0) Sync[F].raiseError(new IllegalArgumentException(s"Argument must be > 0 but it is $n"))
    else Sync[F].unit

  private def assertNonGreaterThanMaxQueueSize[F[_]: Sync, A](n: Int, maxQueueSize: Int): F[Unit] =
    if (n > maxQueueSize)
      Sync[F].raiseError(new IllegalArgumentException(s"Argument must be <= $maxQueueSize (max queue size) but it is $n"))
    else
      Sync[F].unit

  private def assertListFitsInQueue[F[_]: Sync, A](as: List[A], maxQueueSize: Int): F[Unit] =
    if (as.size > maxQueueSize)
      Sync[F].raiseError(
        new IllegalArgumentException(s"Cannot insert list of ${as.size} elements as max queue size if $maxQueueSize")
      )
    else Sync[F].unit

  private abstract class AbstractConcurrentQueue[F[_]: Sync, A] extends ConcurrentQueue[F, A] {
    override def poll: F[A] = pollN(1).map(_.head)

    override def peek: F[A] = peekN(1).map(_.head)

    override def put(a: A): F[Unit] = putN(a :: Nil)

    override def tryPoll: F[Option[A]] = tryPollN(1).map(_.map(_.head))

    override def tryPeek: F[Option[A]] = tryPeekN(1).map(_.map(_.head))

    override def tryPut(a: A): F[Boolean] = tryPutN(a :: Nil)

    override def isEmpty: F[Boolean] = size.map(_ == 0)
  }

  def bounded[F[_]: Concurrent: Sync, A](maxSize: Int): F[BoundedConcurrentQueue[F, A]] =
    for {
      _ <- assertPositive(maxSize)
      queueR <- Ref.of[F, Vector[A]](Vector.empty[A])
      filled <- Semaphore[F](0)
      empty <- Semaphore[F](maxSize)
    } yield buildBounded(queueR, filled, empty, maxSize)

  private def buildBounded[F[_]: Sync, A](queueR: Ref[F, Vector[A]], filled: Semaphore[F], empty: Semaphore[F],
                                          maxSize: Int): BoundedConcurrentQueue[F, A] =
    new AbstractConcurrentQueue[F, A] with BoundedConcurrentQueue[F, A] {
      override val maxQueueSize: Int = maxSize

      override def emptyBuckets: F[Long] = empty.available

      override def isFull: F[Boolean] = emptyBuckets.map(_ == 0)

      override def pollN(n: Int): F[List[A]] = {
        if (n == 0) Sync[F].pure(List.empty[A])
        else Sync[F].uncancelable(
          for {
            _ <- assertNonNegative(n)
            _ <- assertNonGreaterThanMaxQueueSize(n, maxQueueSize)
            _ <- filled.acquireN(n)
            as <- queueR.modify(_.splitAt(n).swap)
            _ <- empty.releaseN(n)
          } yield as.toList
        )
      }

      override def peekN(n: Int): F[List[A]] = {
        if (n == 0) Sync[F].pure(List.empty[A])
        else Sync[F].uncancelable(
          for {
            _ <- assertNonNegative(n)
            _ <- assertNonGreaterThanMaxQueueSize(n, maxQueueSize)
            _ <- filled.acquireN(n)
            as <- queueR.modify(queue => (queue, queue.take(n)))
            _ <- filled.releaseN(n)
          } yield as.toList
        )
      }

      override def putN(as: List[A]): F[Unit] = {
        if (as.isEmpty) Sync[F].unit
        else Sync[F].uncancelable(
          for {
            _ <- assertListFitsInQueue(as, maxQueueSize)
            _ <- empty.acquireN(as.size)
            _ <- queueR.update(_ ++ as)
            _ <- filled.releaseN(as.size)
          } yield ()
        )
      }

      override def tryPollN(n: Int): F[Option[List[A]]] = {
        if (n == 0) Sync[F].pure(Option(List.empty[A]))
        else Sync[F].uncancelable(
          for {
            _ <- assertNonNegative(n)
            acquired <- filled.tryAcquireN(n)
            asO <- {
              if (!acquired) Sync[F].pure(None)
              else queueR.modify(_.splitAt(n).swap).map(Option(_)) >>= { asO =>
                empty.releaseN(n).as(asO)
              }
            }
          } yield asO.map(_.toList)
        )
      }

      override def tryPeekN(n: Int): F[Option[List[A]]] =
        if (n == 0) Sync[F].pure(Option(List.empty[A]))
        else Sync[F].uncancelable(
          for {
            _ <- assertNonNegative(n)
            acquired <- filled.tryAcquireN(n)
            asO <- {
              if (!acquired) Sync[F].pure(None)
              else queueR.modify(queue => (queue, queue.take(n))).map(Option(_)) >>= { asO =>
                filled.releaseN(n).as(asO)
              }
            }
          } yield asO.map(_.toList)
        )

      override def tryPutN(as: List[A]): F[Boolean] =
        if (as.isEmpty) Sync[F].pure(true)
        else Sync[F].uncancelable(
          for {
            acquired <- empty.tryAcquireN(as.size)
            _ <- {
              if (!acquired) Sync[F].unit
              else queueR.update(_ ++ as) >> filled.releaseN(as.size)
            }
          } yield acquired
        )

      override def size: F[Long] = filled.available
    }

  def unbounded[F[_]: Concurrent: Sync, A]: F[ConcurrentQueue[F, A]] =
    for {
      queueR <- Ref.of[F, Vector[A]](Vector.empty[A])
      filled <- Semaphore[F](0)
    } yield buildUnbounded(queueR, filled)

  private def buildUnbounded[F[_]: Sync, A](queueR: Ref[F, Vector[A]], filled: Semaphore[F]): ConcurrentQueue[F, A] =
    new AbstractConcurrentQueue[F, A] {
      override def pollN(n: Int): F[List[A]] =
        if (n == 0) Sync[F].pure(List.empty)
        else Sync[F].uncancelable(
          for {
            _ <- assertNonNegative(n)
            _ <- filled.acquireN(n)
            as <- queueR.modify(_.splitAt(n).swap)
          } yield as.toList
        )

      override def peekN(n: Int): F[List[A]] =
        if (n == 0) Sync[F].pure(List.empty[A])
        else Sync[F].uncancelable(
          for {
            _ <- assertNonNegative(n)
            _ <- filled.acquireN(n)
            as <- queueR.modify(queue => (queue, queue.take(n)))
            _ <-filled.releaseN(n)
          } yield as.toList
        )

      override def putN(as: List[A]): F[Unit] =
        if (as.isEmpty) Sync[F].unit
        else Sync[F].uncancelable(
          for {
            _ <- queueR.update(_ ++ as)
            _ <- filled.releaseN(as.size)
          } yield ()
        )

      override def tryPollN(n: Int): F[Option[List[A]]] =
        if (n == 0) Sync[F].pure(Option(List.empty[A]))
        else Sync[F].uncancelable(
          for {
            _ <- assertNonNegative(n)
            acquired <- filled.tryAcquireN(n)
            asO <- {
              if (!acquired) Sync[F].pure(None)
              else queueR.modify(_.splitAt(n).swap).map(Option(_))
            }
          } yield asO.map(_.toList)
        )

      override def tryPeekN(n: Int): F[Option[List[A]]] =
        if (n == 0) Sync[F].pure(Option(List.empty[A]))
        else Sync[F].uncancelable(
          for {
            _ <- assertNonNegative(n)
            acquired <- filled.tryAcquireN(n)
            asO <- {
              if (!acquired) Sync[F].pure(None)
              else queueR.modify(queue => (queue, queue.take(n))).map(Option(_)) >>= { asO =>
                filled.releaseN(n).as(asO)
              }
            }
          } yield asO.map(_.toList)
        )

      override def tryPutN(as: List[A]): F[Boolean] =
        putN(as).as(true)

      override def size: F[Long] = filled.available
    }

}