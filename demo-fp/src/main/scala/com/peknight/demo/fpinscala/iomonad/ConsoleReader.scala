package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.monads.Monad

case class ConsoleReader[A](run: String => A):
  def map[B](f: A => B): ConsoleReader[B] = ConsoleReader(r => f(run(r)))
  def flatMap[B](f: A => ConsoleReader[B]): ConsoleReader[B] = ConsoleReader(r => f(run(r)).run(r))

object ConsoleReader:
  given monad: Monad[ConsoleReader] with
    def unit[A](a: => A) = ConsoleReader(_ => a)
    override def flatMap[A, B](ra: ConsoleReader[A])(f: A => ConsoleReader[B]) = ra.flatMap(f)
