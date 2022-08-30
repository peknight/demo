package com.peknight.demo.shapeless.hlistcoproduct

import com.peknight.demo.shapeless.typesandimplicits.Last
import shapeless3.deriving.K0

trait Penultimate[L]:
  type Out
  def apply(l: L): Out

object Penultimate:
  type Aux[L, O] = Penultimate[L] { type Out = O }
  def apply[L](using p: Penultimate[L]): Aux[L, p.Out] = p

  given [L <: Tuple, M <: Tuple, O](using init: Init.Aux[L, M], last: Last.Aux[M, O]): Penultimate.Aux[L, O] =
    new Penultimate[L]:
      type Out = O
      def apply(l: L): Out = last.apply(init.apply(l))
  end given

  extension [A] (a: A)
    def penultimate(using inst: Penultimate[A]): inst.Out = inst.apply(a)

  given [A <: Product, R <: Tuple, O](using generic: K0.ProductGeneric[A] { type MirroredElemTypes = R },
                                      penultimate: Penultimate.Aux[R, O]): Penultimate.Aux[A, O] =
    new Penultimate[A]:
      type Out = O
      def apply(l: A): Out = penultimate.apply(Tuple.fromProductTyped(l))


