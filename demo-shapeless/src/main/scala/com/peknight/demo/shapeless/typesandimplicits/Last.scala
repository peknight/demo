package com.peknight.demo.shapeless.typesandimplicits


trait Last[L <: Tuple]:
  type Out
  def apply(in: L): Out

object Last:
  type Aux[L <: Tuple, O] = Last[L] { type Out = O }
  def apply[L <: Tuple](using inst: Last[L]): Aux[L, inst.Out] = inst

  given [H]: Aux[H *: EmptyTuple, H] =
    new Last[H *: EmptyTuple]:
      type Out = H
      def apply(l: H *: EmptyTuple): Out = l.head

  given [H, T <: Tuple, OutT](using lt: Last.Aux[T, OutT]): Aux[H *: T, OutT] =
    new Last[H *: T]:
      type Out = OutT
      def apply(l: H *: T): Out = lt(l.tail)




