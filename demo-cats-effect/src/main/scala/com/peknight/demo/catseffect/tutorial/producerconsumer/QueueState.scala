package com.peknight.demo.catseffect.tutorial.producerconsumer

import cats.effect.Deferred

import scala.collection.immutable.Queue as ScQueue

case class QueueState[F[_], A](queue: ScQueue[A], capacity: Int, takers: ScQueue[Deferred[F, A]], offerers: ScQueue[(A, Deferred[F, Unit])])

object QueueState:
  def empty[F[_], A](capacity: Int): QueueState[F, A] = QueueState(ScQueue.empty, capacity, ScQueue.empty, ScQueue.empty)
