package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.{Callable, CountDownLatch, ExecutorService, Executors}

object Nonblocking {

  sealed trait Future[+A] {
    private[parallelism] def apply(k: A => Unit): Unit
  }

  type Par[+A] = ExecutorService => Future[A]

  def run[A](es: ExecutorService)(p: Par[A]): A = {
    val ref = new AtomicReference[A]
    val latch = new CountDownLatch(1)
    p(es) { a => ref.set(a); latch.countDown() }
    latch.await()
    ref.get
  }

  def unit[A](a: A): Par[A] = es => new Future[A] { def apply(cb: A => Unit): Unit = cb(a) }

  def fork[A](a: => Par[A]): Par[A] = es => new Future[A] {
    def apply(cb: A => Unit): Unit = eval(es)(a(es)(cb))
  }

  def eval(es: ExecutorService)(r: => Unit): Unit = {
    val callable: Callable[Unit] = () => r
    es.submit(callable)
  }

  // 难 用actors实现
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    ???

}
