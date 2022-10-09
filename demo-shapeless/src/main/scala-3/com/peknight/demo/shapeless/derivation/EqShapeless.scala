package com.peknight.demo.shapeless.derivation

import shapeless3.deriving.{Complete, K0}

trait EqShapeless[T]:
  def eqv(x: T, y: T): Boolean

object EqShapeless:

  given EqShapeless[Int] with
    def eqv(x: Int, y: Int) = x == y

  given eqSum[A](using inst: => K0.CoproductInstances[EqShapeless, A]): EqShapeless[A] with
    def eqv(x: A, y: A): Boolean = inst.fold2(x, y)(false)(
      [t] => (eqt: EqShapeless[t], t0: t, t1: t) => eqt.eqv(t0, t1)
    )

  given eqProduct[A](using inst: K0.ProductInstances[EqShapeless, A]): EqShapeless[A] with
    def eqv(x: A, y: A): Boolean = inst.foldLeft2(x, y)(true: Boolean)(
      // 这个CompleteOr 是个 Boolean | Complete[Boolean]，如果是Complete[Boolean]会中断操作，Boolean则会继续fold
      [t] => (acc: Boolean, eqt: EqShapeless[t], t0: t, t1: t) => Complete(!eqt.eqv(t0, t1))(false)(true)
    )

  inline def derived[A](using gen: K0.Generic[A]): EqShapeless[A] = gen.derive(eqProduct, eqSum)
