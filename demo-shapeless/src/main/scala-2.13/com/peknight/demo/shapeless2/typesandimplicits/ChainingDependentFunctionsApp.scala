package com.peknight.demo.shapeless2.typesandimplicits

import shapeless.{::, Generic, HList, HNil}
import shapeless.ops.hlist.{IsHCons, Last}

object ChainingDependentFunctionsApp extends App {
  def lastField[A, Repr <: HList](input: A)(implicit gen: Generic.Aux[A, Repr], last: Last[Repr]): last.Out =
    last.apply(gen.to(input))

  println(lastField(Rect(Vec(1, 2), Vec(3, 4))))

  def getWrappedValueV1[A, H](input: A)(implicit gen: Generic.Aux[A, H :: HNil]): H = gen.to(input).head

  // Scala2 不行，Scala3 行
  // println(getWrappedValueV1(Wrapper(42)))

  // 需要IsHCons
  // def getWrappedValueV2[A, Repr <: HList, Head, Tail <: HList](input: A)(implicit gen: Generic.Aux[A, Repr],
  //                                                                        ev: (Head :: Tail) =:= Repr): Head =
  //   gen.to(input).head

  def getWrappedValueV3[A, Repr <: HList, Head](in: A)(implicit gen: Generic.Aux[A, Repr],
                                                       isHCons: IsHCons.Aux[Repr, Head, HNil]): Head =
    gen.to(in).head

  println(getWrappedValueV3(Wrapper(42)))
}
