package com.peknight.demo.shapeless.functionaloperations

import scala.deriving.Mirror

object ProductMapper {
  class Builder[A <: Product, B <: Product](a: A):
    def apply[F[_]](f: [t] => t => F[t])(using aMirror: Mirror.ProductOf[A], bMirror: Mirror.ProductOf[B]): B =
      bMirror.fromProduct(Tuple.fromProductTyped(a).map[F](f))

  extension [A <: Product] (a: A)
    def mapTo[B <: Product]: Builder[A, B] = new Builder[A, B](a)
}
