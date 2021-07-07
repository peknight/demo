package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.{Future, TimeUnit}

case class Map2Future[A, B, C](a: Future[A], b: Future[B], f: (A, B) => C) extends Future[C] {

  @volatile var cache: Option[C] = None

  override def cancel(mayInterruptIfRunning: Boolean): Boolean =
    a.cancel(mayInterruptIfRunning) || b.cancel(mayInterruptIfRunning)

  override def isCancelled: Boolean = a.isCancelled || b.isCancelled

  override def isDone: Boolean = cache.isDefined

  override def get(): C = compute(Long.MaxValue)

  override def get(timeout: Long, unit: TimeUnit): C = compute(unit.toNanos(timeout))

  private def compute(timeoutInNanos: Long): C = cache match {
    case Some(c) => c
    case None =>
      val start = System.nanoTime()
      val ar = a.get(timeoutInNanos, TimeUnit.NANOSECONDS)
      val stop = System.nanoTime
      val aTime = stop - start
      val br = b.get(timeoutInNanos - aTime, TimeUnit.NANOSECONDS)
      val ret = f(ar, br)
      cache = Some(ret)
      ret
  }
}
