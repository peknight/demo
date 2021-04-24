package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.{Callable, ExecutorService, Future, TimeUnit}

//trait Par[+A] {
//
//}
object Par {
  type Par[A] = ExecutorService => Future[A]

  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true
    def get(timeout: Long, unit: TimeUnit): A = get
    def isCancelled: Boolean = false
    def cancel(mayInterruptIfRunning: Boolean): Boolean = false
  }

  // Exercise 7.1 7.3
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = (es: ExecutorService) => {
    val af = a(es)
    val bf = b(es)
    UnitFuture(f(af.get, bf.get))
  }

  def fork[A](a: => Par[A]): Par[A] = es => es.submit(() => a(es).get)

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

//  @deprecated("use run")
//  def get[A](a: Par[A]): A = run(a)

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)
}
