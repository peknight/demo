package com.peknight.demo.fpinscala.monads

trait Monadic[F[_], A] {
  val F: Monad[F]
  def get: F[A]
  private val a = get
  def map[B](f: A => B): F[B] = F.map(a)(f)
  def flatMap[B](f: A => F[B]): F[B] = F.flatMap(a)(f)
  def **[B](b: F[B]) = F.map2(a, b)((_, _))
  def *>[B](b: F[B]) = F.map2(a, b)((_, b) => b)
  def map2[B, C](b: F[B])(f: (A, B) => C): F[C] = F.map2(a, b)(f)
  def as[B](b: B): F[B] = F.as(a)(b)
  def skip: F[Unit] = F.skip(a)
  def replicateM(n: Int) = F.replicateM(n)(a)
  def replicateM_(n: Int) = F.replicateM_(n)(a)
}
