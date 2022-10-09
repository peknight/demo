package com.peknight.demo.shapeless.hlistcoproduct

trait Init[L <: Tuple]:
  type Out <: Tuple
  def apply(in: L): Out

object Init:

  type Aux[L <: Tuple, O <: Tuple] = Init[L] { type Out = O }
  def apply[L <: Tuple](using inst: Init[L]): Aux[L, inst.Out] = inst

  given [H]: Aux[H *: EmptyTuple, EmptyTuple] =
    new Init[H *: EmptyTuple]:
      type Out = EmptyTuple
      def apply(l: H *: EmptyTuple): Out = EmptyTuple

  given [H, T <: Tuple, OutT <: Tuple](using it: Init.Aux[T, OutT]): Aux[H *: T, H *: OutT] =
    new Init[H *: T]:
      type Out = H *: OutT
      def apply(l: H *: T): Out = l.head *: it(l.tail)






