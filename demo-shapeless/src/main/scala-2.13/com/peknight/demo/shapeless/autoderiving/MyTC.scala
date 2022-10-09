package com.peknight.demo.shapeless.autoderiving

import shapeless._

trait MyTC[A]

object MyTC {
  implicit def intInstance: MyTC[Int] = ???
  implicit def stringInstance: MyTC[String] = ???
  implicit def booleanInstance: MyTC[Boolean] = ???

  implicit def hnilInstance: MyTC[HNil] = ???

  implicit def hlistInstance[H, T <: HList](implicit hInstance: Lazy[MyTC[H]], tInstance: MyTC[T]): MyTC[H :: T] = ???

  implicit def cnilInstance: MyTC[CNil] = ???

  implicit def coproductInstance[H, T <: Coproduct](implicit hInstance: Lazy[MyTC[H]], tInstance: MyTC[T]): MyTC[H :+: T] = ???

  implicit def genericInstance[A, R](implicit generic: Generic.Aux[A, R], rInstance: Lazy[MyTC[R]]): MyTC[A] = ???
}
