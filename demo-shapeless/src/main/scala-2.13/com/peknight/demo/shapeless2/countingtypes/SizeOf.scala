package com.peknight.demo.shapeless2.countingtypes

import shapeless._
import shapeless.ops.{hlist, nat}

trait SizeOf[A] {
  def value: Int
}
object SizeOf {
  implicit def genericSizeOf[A, L <: HList, N <: Nat](
    implicit
    generic: Generic.Aux[A, L],
    size: hlist.Length.Aux[L, N],
    sizeToInt: nat.ToInt[N]
  ): SizeOf[A] = new SizeOf[A] {
    val value = sizeToInt.apply()
  }
}
