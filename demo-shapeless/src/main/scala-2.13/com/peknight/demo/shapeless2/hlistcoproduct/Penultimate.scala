package com.peknight.demo.shapeless2.hlistcoproduct

import shapeless.{Generic, HList}
import shapeless.ops.hlist

trait Penultimate[L] {
  type Out
  def apply(l: L): Out
}
object Penultimate {
  type Aux[L, O] = Penultimate[L] { type Out = O }
  def apply[L](implicit p: Penultimate[L]): Aux[L, p.Out] = p

  implicit def hlistPenultimate[L <: HList, M <: HList, O](implicit init: hlist.Init.Aux[L, M],
                                                           last: hlist.Last.Aux[M, O]): Penultimate.Aux[L, O] =
    new Penultimate[L] {
      type Out = O
      def apply(l: L): Out = last.apply(init.apply(l))
    }

  implicit class PenultimateOps[A](a: A) {
    def penultimate(implicit inst: Penultimate[A]): inst.Out = inst.apply(a)
  }

  implicit def genericPenultimate[A, R, O](implicit generic: Generic.Aux[A, R], penultimate: Penultimate.Aux[R, O])
  : Penultimate.Aux[A, O] =
    new Penultimate[A] {
      type Out = O
      def apply(a: A): Out = penultimate.apply(generic.to(a))
    }
}
