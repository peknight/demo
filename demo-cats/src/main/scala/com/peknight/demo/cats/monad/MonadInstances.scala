package com.peknight.demo.cats.monad

import cats.{Id, Monad}

object MonadInstances {
  val idDemoMonad: DemoMonad[Id] = new DemoMonad[Id] {
    def pure[A](a: A): Id[A] = a
    override def map[A, B](value: Id[A])(func: A => B): Id[B] = func(value)
    def flatMap[A, B](value: Id[A])(f: A => Id[B]): Id[B] = f(value)
  }

  val idMonad: Monad[Id] = new Monad[Id] {
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)

    override def tailRecM[A, B](a: A)(f: A => Id[Either[A, B]]): Id[B] = f(a) match {
      case Right(b) => b
      case Left(la) => tailRecM(la)(f)
    }

    def pure[A](x: A): Id[A] = x
  }

}
