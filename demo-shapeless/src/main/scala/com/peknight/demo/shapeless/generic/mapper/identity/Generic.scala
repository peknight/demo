package com.peknight.demo.shapeless.generic.mapper.identity

import cats.Id
import com.peknight.demo.shapeless.generic.*


trait Generic[A]:
  self =>
  type B
  def to(a: A): B
  def from(b: B): A
  val mapper: BiMapper[A, B] = new BiMapper[A, B]:
    def to(a: A): B = self.to(a)
    def from(b: B): A = self.from(b)

object Generic extends App:

  type Aux[A, B0] = Generic[A] { type B = B0 }

  def apply[A](using gen: Generic[A]): gen.mapper.type = gen.mapper

  inline given [A <: Product, B0 <: Tuple] (using biMapper: BiMapper[A, B0]): Aux[A, B0] = new Generic[A]:
    type B = B0
    def to(a: A): B = biMapper.to(a)
    def from(b: B): A = biMapper.from(b)
    override val mapper: BiMapper[A, B] = biMapper