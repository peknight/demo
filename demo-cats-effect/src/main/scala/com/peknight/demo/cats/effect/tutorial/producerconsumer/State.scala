package com.peknight.demo.cats.effect.tutorial.producerconsumer

import cats.effect.Deferred

import scala.collection.immutable.Queue

case class State[F[_], A](queue: Queue[A], takers: Queue[Deferred[F, A]])
object State:
  def empty[F[_], A]: State[F, A] = State(Queue.empty, Queue.empty)
