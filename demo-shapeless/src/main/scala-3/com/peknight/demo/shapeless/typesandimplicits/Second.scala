package com.peknight.demo.shapeless.typesandimplicits

trait Second[L <: Tuple]:
  type Out
  def apply(value: L): Out

object Second:
  type Aux[L <: Tuple, O] = Second[L] { type Out = O }
  def apply[L <: Tuple](using inst: Second[L]): Aux[L, inst.Out] = inst

  given [A, B, Rest <: Tuple]: Aux[A *: B *: Rest, B] =
    new Second[A *: B *: Rest]:
      type Out = B
      def apply(value: A *: B *: Rest): B = value.tail.head

