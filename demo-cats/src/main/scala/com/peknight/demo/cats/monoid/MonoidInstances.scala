package com.peknight.demo.cats.monoid

import cats.Monoid
import cats.syntax.invariant._
import cats.syntax.semigroup._

object MonoidInstances {

  implicit val booleanAndMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(a: Boolean, b: Boolean) = a && b
    def empty = true
  }

  implicit val booleanOrMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(a: Boolean, b: Boolean) = a || b
    def empty = false
  }

  implicit val booleanEitherMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(a: Boolean, b: Boolean) = (a && !b) || (!a && b)
    def empty = false
  }

  implicit val booleanXnorMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def combine(a: Boolean, b: Boolean) = (!a || b)  && (a || !b)
    def empty = true
  }

  implicit def setUnionMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    def combine(a: Set[A], b: Set[A]) = a union b
    def empty = Set.empty[A]
  }

  val intSetMonoid = Monoid[Set[Int]](setUnionMonoid)
  val strSetMonoid = Monoid[Set[String]](setUnionMonoid)

  implicit def symDiffMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
    def combine(a: Set[A], b: Set[A]): Set[A] = (a diff b) union (b diff a)
    def empty: Set[A] = Set.empty
  }

  implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
    def combine(a: Order, b: Order): Order = {
      Order(a.totalCost |+| b.totalCost, a.quantity |+| b.quantity)
    }
    def empty: Order = Order(Monoid[Double].empty, Monoid[Double].empty)
  }

  // sbt可编译通过，但idea编译不通过，why?
  implicit val symbolMonoid: Monoid[Symbol] = Monoid[String].imap[Symbol](Symbol.apply)(_.name)
}
