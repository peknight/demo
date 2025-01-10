package com.peknight.demo.cats.mtl

import cats.Applicative
import cats.mtl.Ask

object AskApp extends App:
  def functionAsk[E]: Ask[[X] =>> E => X, E] = new Ask[[X] =>> E => X, E] {
    def ask[E2 >: E]: E => E2 = identity
    def applicative: Applicative[[X] =>> E => X] = new Applicative[[X] =>> E => X]:
      def pure[A](x: A): E => A = e => x
      def ap[A, B](ff: E => A => B)(fa: E => A): E => B = e => ff(e)(fa(e))
  }
end AskApp
