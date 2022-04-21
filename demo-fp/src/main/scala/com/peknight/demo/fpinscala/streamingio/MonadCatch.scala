package com.peknight.demo.fpinscala.streamingio

import com.peknight.demo.fpinscala.monads.Monad

trait MonadCatch[F[_]] extends Monad[F]:
  def attempt[A](a: F[A]): F[Either[Throwable, A]]
  def fail[A](t: Throwable): F[A]
