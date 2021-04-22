package com.peknight.demo.fpinscala.parallelism

trait Par[+A] {

}
object Par {
  def unit[A](a: A): Par[A] = ???

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  @deprecated("use run")
  def get[A](a: Par[A]): A = run(a)

  def run[A](a: Par[A]): A = ???

  // Exercise 7.1
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = ???

  def fork[A](a: => Par[A]): Par[A] = ???
}
