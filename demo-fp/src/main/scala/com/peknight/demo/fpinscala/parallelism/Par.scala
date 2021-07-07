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

  // 实际是用map2来实现map
  def sortPar(parList: Par[List[Int]]): Par[List[Int]] = map2(parList, unit(()))((a, _) => a.sorted)

  // TODO
  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = ???
}
