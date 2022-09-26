package com.peknight.demo.shapeless.generic.mapper.identity

import cats.Id
import com.peknight.demo.shapeless.generic.*

trait BiMapper[A, B] extends com.peknight.demo.shapeless.generic.mapper.BiMapper[Id, A, B], Mapper[A, B]

object BiMapper:
  inline given [A <: Product, B <: Tuple](using mirror: MirrorProductAux[A, B]): BiMapper[A, B] with
    def to(a: A): B = Tuple.fromProductTyped(a)
    def from(b: B): A = mirror.fromProduct(b)