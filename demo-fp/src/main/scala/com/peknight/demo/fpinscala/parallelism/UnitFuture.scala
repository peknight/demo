package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.{Future, TimeUnit}

case class UnitFuture[A](get: A) extends Future[A] {
  def isDone = true
  def get(timeout: Long, unit: TimeUnit): A = get
  def isCancelled: Boolean = false
  def cancel(mayInterruptIfRunning: Boolean): Boolean = false
}
