package com.peknight.demo.fpinscala.parallelism

import java.util.concurrent.{ExecutorService, Future}

//trait Par[+A] {
//
//}
object Par {
  type Par[A] = ExecutorService => Future[A]

  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  // Exercise 7.1
  def map2WithUnitFuture[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = (es: ExecutorService) => {
    val af = a(es)
    val bf = b(es)
    UnitFuture(f(af.get, bf.get))
  }

  // Exercise 7.3
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = (es: ExecutorService) => {
    val (af, bf) = (a(es), b(es))
    Map2Future(af, bf, f)
  }

  def fork[A](a: => Par[A]): Par[A] = es => es.submit(() => a(es).get)

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

//  @deprecated("use run")
//  def get[A](a: Par[A]): A = run(a)

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

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


}
