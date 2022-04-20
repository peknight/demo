package com.peknight.demo.cats.monad

trait DemoMonad[F[_]]:
  def pure[A](a: A): F[A]
  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  def map[A, B](value: F[A])(func: A => B): F[B] = flatMap(value)((a: A) => pure(func(a)))
