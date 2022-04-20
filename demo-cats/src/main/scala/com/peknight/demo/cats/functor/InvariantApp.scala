package com.peknight.demo.cats.functor

import cats.Monoid
import cats.syntax.semigroup.*
import com.peknight.demo.cats.functor.CodecInstances.given
import com.peknight.demo.cats.monoid.MonoidInstances.given

object InvariantApp extends App:
  println(Codec.encode(123.4))
  println(Codec.decode[Double]("123.4"))
  println(Codec.encode[Box[Double]](Box(123.4)))
  println(Codec.decode[Box[Double]]("123.4"))

  println(Monoid[Symbol].empty)
  println(Symbol("a") |+| Symbol("few") |+| Symbol("words"))
