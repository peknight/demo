package com.peknight.demo.shapeless.functionaloperations

import shapeless._
import shapeless.ops.hlist

trait ProductMapper[A, B, P] {
  def apply(a: A): B
}
object ProductMapper {
  implicit def genericProductMapper[
    A, B, P <: Poly, ARepr <: HList, BRepr <: HList
  ](
    implicit
    aGen: Generic.Aux[A, ARepr],
    bGen: Generic.Aux[B, BRepr],
    mapper: hlist.Mapper.Aux[P, ARepr, BRepr]
  ): ProductMapper[A, B, P] = (a: A) => bGen.from(mapper.apply(aGen.to(a)))

  implicit class ProductMapperOps[A](a: A) {
    class Builder[B] {
      def apply[P <: Poly](poly: P)(implicit pm: ProductMapper[A, B, P]): B = pm.apply(a)
    }
    def mapTo[B]: Builder[B] = new Builder[B]
  }
}
