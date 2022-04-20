package com.peknight.demo.catseffect.tutorial.producerconsumer

import cats.effect.syntax.all.*
import cats.effect.{Async, Deferred, Ref}
import cats.syntax.all.*

/**
 * See `cats.effect.std.Queue`
 */
class Queue[F[_]: Async, A](stateR: Ref[F, QueueState[F, A]]):
  val take: F[A] = Deferred[F, A].flatMap { taker =>
    Async[F].uncancelable { poll =>
      stateR.modify {
        case QueueState(queue, capacity, takers, offerers) if queue.nonEmpty && offerers.isEmpty =>
          val (i, rest) = queue.dequeue
          QueueState(rest, capacity, takers, offerers) -> Async[F].pure(i)
        case QueueState(queue, capacity, takers, offerers) if queue.nonEmpty =>
          val (i, rest) = queue.dequeue
          val ((move, release), tail) = offerers.dequeue
          QueueState(rest.enqueue(move), capacity, takers, tail) -> release.complete(()).as(i)
        case QueueState(queue, capacity, takers, offerers) if offerers.nonEmpty =>
          val((i, release), tail) = offerers.dequeue
          QueueState(queue, capacity, takers, tail) -> release.complete(()).as(i)
        case QueueState(queue, capacity, takers, offerers) =>
          //noinspection DuplicatedCode
          val cleanup = stateR.update { state => state.copy(takers = state.takers.filter(_ ne taker)) }
          QueueState(queue, capacity, takers.enqueue(taker), offerers) -> poll(taker.get).onCancel(cleanup)
      }.flatten
    }
  }

  def offer(a: A): F[Unit] = Deferred[F, Unit].flatMap { offerer =>
    Async[F].uncancelable { poll =>
      stateR.modify {
        case QueueState(queue, capacity, takers, offerers) if takers.nonEmpty =>
          val (taker, tail) = takers.dequeue
          QueueState(queue, capacity, tail, offerers) -> taker.complete(a).void
        case QueueState(queue, capacity, takers, offerers) if queue.size < capacity =>
          QueueState(queue.enqueue(a), capacity, takers, offerers) -> Async[F].unit
        case QueueState(queue, capacity, takers, offerers) =>
          //noinspection DuplicatedCode
          val cleanup = stateR.update { state => state.copy(offerers = state.offerers.filter(_._2 ne offerer)) }
          QueueState(queue, capacity, takers, offerers.enqueue((a, offerer))) -> poll(offerer.get).onCancel(cleanup)
      }.flatten
    }
  }

object Queue:
  def apply[F[_]: Async, A](capacity: Int): F[Queue[F, A]] =
    Ref.of[F, QueueState[F, A]](QueueState.empty[F, A](capacity)).map(st => new Queue(st))
