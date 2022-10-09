package com.peknight.demo.shapeless2.typesandimplicits

import shapeless.Generic

object DependentTypesApp extends App {
  def getRepr[A](value: A)(implicit gen: Generic[A]): gen.Repr = gen.to(value)

  println(getRepr(Vec(1, 2)))
  println(getRepr(Rect(Vec(0, 0), Vec(5, 5))))
}
