package com.peknight.demo.shapeless.typesandimplicits

import shapeless3.deriving.K0

object DependentTypesApp extends App:
  def getRepr[A <: Product](value: A)(using gen: K0.ProductGeneric[A]): gen.MirroredElemTypes =
    Tuple.fromProductTyped(value)

  println(getRepr(Vec(1, 2)))
  println(getRepr(Rect(Vec(1, 2), Vec(5, 5))))
