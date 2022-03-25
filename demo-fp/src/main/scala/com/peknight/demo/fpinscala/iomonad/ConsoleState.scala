package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.monads.Monad

case class ConsoleState[A](run: Buffers => (A, Buffers))

object ConsoleState {
  implicit val monad: Monad[ConsoleState] = ???
}
