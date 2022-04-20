package com.peknight.demo.cats.casestudy.crdt

import cats.kernel.CommutativeMonoid
import cats.syntax.foldable.*
import cats.syntax.semigroup.*

final case class OldGCounter[A](counters: Map[String, A]):

  def increment(machine: String, amount: A)(using m: CommutativeMonoid[A]): OldGCounter[A] =
    val value = amount |+| counters.getOrElse(machine, m.empty)
    OldGCounter(counters + (machine -> value))

  def merge(that: OldGCounter[A])(using BoundedSemiLattice[A]): OldGCounter[A] = OldGCounter(
    this.counters |+| that.counters
  )

  def total(using CommutativeMonoid[A]): A = counters.values.toList.combineAll
