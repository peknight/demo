package com.peknight.demo.cats.casestudy.crdt

import cats.kernel.CommutativeMonoid
import cats.syntax.foldable._
import cats.syntax.semigroup._

final case class OldGCounter[A](counters: Map[String, A]) {

  def increment(machine: String, amount: A)(implicit m: CommutativeMonoid[A]): OldGCounter[A] = {
    val value = amount |+| counters.getOrElse(machine, m.empty)
    OldGCounter(counters + (machine -> value))
  }

  def merge(that: OldGCounter[A])(implicit b: BoundedSemiLattice[A]): OldGCounter[A] = OldGCounter(
    this.counters |+| that.counters
  )

  def total(implicit m: CommutativeMonoid[A]): A = counters.values.toList.combineAll
}
