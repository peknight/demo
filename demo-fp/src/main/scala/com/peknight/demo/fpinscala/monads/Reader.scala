package com.peknight.demo.fpinscala.monads

case class Reader[R, A](run: R => A)

object Reader:
  def readerMonad[R]: Monad[[T] =>> Reader[R, T]] = new Monad:
    def unit[A](a: => A): Reader[R, A] = Reader(_ => a)
    override def flatMap[A, B](fa: Reader[R, A])(f: A => Reader[R, B]): Reader[R, B] = Reader(r => f(fa.run(r)).run(r))

  def ask[R]: Reader[R, R] = Reader(r => r)
