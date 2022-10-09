package com.peknight.demo.shapeless.typesandimplicits

import shapeless3.deriving.K0


object ChainingDependentFunctionsApp extends App:

  type Aux[T, Repr <: Tuple] = K0.ProductGeneric[T] { type MirroredElemTypes = Repr }

  def lastField[A <: Product, Repr <: Tuple](input: A)(using gen: Aux[A, Repr], last: Last[Repr]): last.Out =
    last.apply(Tuple.fromProductTyped(input))

  println(lastField(Rect(Vec(1, 2), Vec(3, 4))))

  def getWrappedValue[A <: Product, H](input: A)(using gen: Aux[A, H *: EmptyTuple]): H =
    Tuple.fromProductTyped(input).head

  // Scala3牛逼，这样就可以 那就应该不需要IsHCons了
  println(getWrappedValue(Wrapper(42)))



