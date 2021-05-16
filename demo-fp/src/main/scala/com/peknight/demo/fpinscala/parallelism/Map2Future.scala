package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.{Future, TimeUnit}

case class Map2Future[A, B, C](a: Future[A], b: Future[B], f: (A, B) => C) extends Future[C] {

  @volatile var cache: Option[C] = None

  override def cancel(mayInterruptIfRunning: Boolean): Boolean = ???

  override def isCancelled: Boolean = a.isCancelled || b.isCancelled

  override def isDone: Boolean = cache.isDefined

  override def get(): C = ???

  override def get(timeout: Long, unit: TimeUnit): C = ???
}
