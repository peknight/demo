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

  def map2[A, B, C](p1: Par[A], p2: Par[B])(f: (A, B) => C): Par[C] =
    es => new Future[C] {
      def apply(cb: C => Unit): Unit = {
        // Two mutable vars are used to store the two results
        var ar: Option[A] = None
        var br: Option[B] = None

        // An actor that awaits both results, combines them with f, and passes the result to cb
        val combiner = Actor[Either[A, B]](es) {
          // If the A result came in first, stores it in ar and waits for the B.
          // If the A result came last and we already have our B,
          // calls f with both results and passes the resulting C to the callback, cb.
          case Left(a) => br match {
            case None => ar = Some(a)
            case Some(b) => eval(es)(cb(f(a, b)))
          }
          // Analogously, if the B result came in first, stores it in br and waits for the A.
          // If the B result came last and we already have our A,
          // calls f with both results and passes the resulting C to the callback, cb.
          case Right(b) => ar match {
            case None => br = Some(b)
            case Some(a) => eval(es)(cb(f(a, b)))
          }
        }

        // Passes the actor as a continuation to both sides. On the A side,
        // we wrap the result in Left, and on the B side, we wrap it in Right.
        // These are the constructors of the Either data type,
        // and they serve to indicate to the actor where the result came from.
        p1(es)(a => combiner ! Left(a))
        p2(es)(b => combiner ! Right(b))

      }
    }

  def map[A, B](pa: Par[A])(f: A => B) = map2(pa, unit(()))((a, _) => f(a))

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  def sequenceBalanced[A](as:IndexedSeq[Par[A]]): Par[IndexedSeq[A]] = fork {
    if (as.isEmpty) unit(Vector())
    else if (as.length == 1) map(as.head)(a => Vector(a))
    else {
      val (l, r) = as.splitAt(as.length / 2)
      map2(sequenceBalanced(l), sequenceBalanced(r))(_ ++ _)
    }
  }

  def sequence[A](as: List[Par[A]]): Par[List[A]] = map(sequenceBalanced(as.toIndexedSeq))(_.toList)

  def asyncF[A, B](f:A => B): A => Par[B] = (a: A) => lazyUnit(f(a))

  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = {
    val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence(fbs)
  }
}
