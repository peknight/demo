package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.monads.Monad

import scala.language.implicitConversions

case class ConsoleState[A](run: Buffers => (A, Buffers))

object ConsoleState:
  given monad: Monad[ConsoleState] with
    override def unit[A](a: => A): ConsoleState[A] = ConsoleState((a, _))
    override def flatMap[A, B](fa: ConsoleState[A])(f: A => ConsoleState[B]): ConsoleState[B] = fa.flatMap(f)
