package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.{ExecutorService, Future}

object Par {
  type Par[A] = ExecutorService => Future[A]

  //  @deprecated("use run")
  //  def get[A](a: Par[A]): A = run(a)

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  /*
   * `unit` is represented as a function that returns a `UnitFuture`,
   * which is a simple implementation of `Future` that just wraps a constant value.
   * It doesn't use the `ExecutorService` at all.
   * It's always done and can't be cancelled.
   * Its `get` method simply returns the value that we gave it.
   */
  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  // Exercise 7.1
  def map2WithUnitFuture[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = (es: ExecutorService) => {
    val af = a(es)
    val bf = b(es)
    /*
     * This implementation of `map2` does not respect timeouts.
     * It simply passes the `ExecutorService` on to both `Par` values,
     * waits for the results of the Futures `af` and `bf`,
     * applies `f` to them, and wraps them in a `UnitFuture`,
     * In order to respect timeouts,
     * we'd need a new `Future` implementation that records the amount of time spent evaluating `af`,
     * then subtracts that time from the available time allocated for evaluating `bf`.
     */
    UnitFuture(f(af.get, bf.get))
  }

  /*
   * `map2` doesn't evaluate the call to `f` in a separate logical thread,
   * in accord with our design choice of having `fork` be the sole function in the API for controlling parallelism.
   * We can always do `fork(map2(a, b)(f))` if we want the evaluation of `f` to occur in a separate thread.
   */
  // Exercise 7.3
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = (es: ExecutorService) => {
    val (af, bf) = (a(es), b(es))
    Map2Future(af, bf, f)
  }

  def fork[A](a: => Par[A]): Par[A] = es => es.submit(() => a(es).get)

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  // Exercise 7.4
  def asyncF[A, B](f:A => B): A => Par[B] = (a: A) => lazyUnit(f(a))

  def map[A, B](pa: Par[A])(f: A => B) = map2(pa, unit(()))((a, _) => f(a))

  def sortPar(parList: Par[List[Int]]): Par[List[Int]] = map(parList)(_.sorted)

  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = {
    val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence(fbs)
  }

  // Exercise 7.5
  def sequence_simple[A](l: List[Par[A]]): Par[List[A]] =
    l.foldRight[Par[List[A]]](unit(List()))((h, t) => map2(h, t)(_ :: _))

  def sequenceRight[A](as: List[Par[A]]): Par[List[A]] =
    as match {
      case Nil => unit(Nil)
      case h :: t => map2(h, fork(sequenceRight(t)))(_ :: _)
    }

  def sequenceBalanced[A](as:IndexedSeq[Par[A]]): Par[IndexedSeq[A]] = fork {
    if (as.isEmpty) unit(Vector())
    else if (as.length == 1) map(as.head)(a => Vector(a))
    else {
      val (l, r) = as.splitAt(as.length / 2)
      map2(sequenceBalanced(l), sequenceBalanced(r))(_ ++ _)
    }
  }

  def sequence[A](as: List[Par[A]]): Par[List[A]] = map(sequenceBalanced(as.toIndexedSeq))(_.toList)

  // Exercise 7.6
  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = {
    val pars: List[Par[List[A]]] = as.map(asyncF((a: A) => if (f(a)) List(a) else List()))
    map(sequence(pars))(_.flatten)
  }

  def map3[A, B, C, D](pa: Par[A], pb: Par[B], pc: Par[C])(f: (A, B, C) => D): Par[D] =
    map2(map2(pa, pb)((_, _)), pc) {
      case ((a, b), c) => f(a, b, c)
    }

  def map4[A, B, C, D, E](pa: Par[A], pb: Par[B], pc: Par[C], pd: Par[D])(f: (A, B, C, D) => E): Par[E] =
    map2(map3(pa, pb, pc)((_, _, _)), pd) {
      case ((a, b, c), d) => f(a, b, c, d)
    }

  def map5[A, B, C, D, E, F](pa: Par[A], pb: Par[B], pc: Par[C], pd: Par[D], pe: Par[E])(f: (A, B, C, D, E) => F): Par[F] =
    map2(map4(pa, pb, pc, pd)((_, _, _, _)), pe) {
      case ((a, b, c, d), e) => f(a, b, c, d, e)
    }

  def equal[A](e: ExecutorService)(p1: Par[A], p2: Par[A]): Boolean = p1(e).get() == p2(e).get()

  def delay[A](fa: => Par[A]):Par[A] = es => fa(es)

  def choice[A](cond: Par[Boolean])(t: Par[A], f: Par[A]): Par[A] =
    es => if (run(es)(cond).get) t(es) else f(es)

  def choiceN[A](n: Par[Int])(choices: List[Par[A]]): Par[A] =
    es => choices(run(es)(n).get)(es)

  def choiceViaChoiceN[A](a: Par[Boolean])(ifTrue: Par[A], ifFalse: Par[A]): Par[A] =
    choiceN(map(a)(b => if (b) 0 else 1))(List(ifTrue, ifFalse))

  def choiceMap[K, V](key: Par[K])(choice: Map[K, Par[V]]): Par[V] =
    es => run(es)(choice(run(es)(key).get))

  def chooser[A, B](pa: Par[A])(choices: A => Par[B]): Par[B] =
    es => run(es)(choices(run(es)(pa).get))

  /* `chooser` is usually called `flatMap` or `bind`. */
  def flatMap[A, B](p: Par[A])(choices: A => Par[B]): Par[B] =
    es => {
      val k = run(es)(p).get()
      run(es)(choices(k))
    }

  def choiceViaFlatMap[A](cond: Par[Boolean])(t: Par[A], f: Par[A]): Par[A] =
    flatMap(cond)(b => if (b) t else f)

  def choiceNViaFlatMap[A](n: Par[Int])(choices: List[Par[A]]): Par[A] =
    flatMap(n)(i => choices(i))

  def join[A](a: Par[Par[A]]): Par[A] = es => run(es)(run(es)(a).get)

  def joinViaFlatMap[A](a: Par[Par[A]]): Par[A] = flatMap(a)(x => x)

  def flatMapViaJoin[A, B](p: Par[A])(f: A => Par[B]): Par[B] = join(map(p)(f))

  def map2ViaFlatMap[A, B, C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] =
    flatMap(pa)(a => flatMap(pb)(b => unit(f(a, b))))

}
