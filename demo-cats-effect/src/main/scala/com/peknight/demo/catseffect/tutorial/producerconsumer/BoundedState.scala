package com.peknight.demo.catseffect.tutorial.producerconsumer

import cats.effect.Deferred

import scala.collection.immutable.Queue

case class BoundedState[F[_], A](queue: Queue[A], capacity: Int, takers: Queue[Deferred[F, A]], offerers: Queue[(A, Deferred[F, Unit])])

object BoundedState:
  def empty[F[_], A](capacity: Int): BoundedState[F, A] =
    BoundedState(Queue.empty, capacity, Queue.empty, Queue.empty)
