package com.peknight.demo.cats.monoid

import cats.Monoid
import cats.syntax.invariant.*
import cats.syntax.semigroup.*

object MonoidInstances:

  given booleanAndMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean) = a && b
    def empty = true

  given booleanOrMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean) = a || b
    def empty = false

  given booleanEitherMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean) = (a && !b) || (!a && b)
    def empty = false

  given booleanXnorMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean) = (!a || b)  && (a || !b)
    def empty = true

  given setUnionMonoid[A]: Monoid[Set[A]] with
    def combine(a: Set[A], b: Set[A]) = a union b
    def empty = Set.empty[A]

  val intSetMonoid = Monoid[Set[Int]](setUnionMonoid)
  val strSetMonoid = Monoid[Set[String]](setUnionMonoid)

  given symDiffMonoid[A]: Monoid[Set[A]] with
    def combine(a: Set[A], b: Set[A]): Set[A] = (a diff b) union (b diff a)
    def empty: Set[A] = Set.empty

  given orderMonoid: Monoid[Order] with
    def combine(a: Order, b: Order): Order = Order(a.totalCost |+| b.totalCost, a.quantity |+| b.quantity)
    def empty: Order = Order(Monoid[Double].empty, Monoid[Double].empty)

  // sbt可编译通过，但idea编译不通过，why?
  given symbolMonoid: Monoid[Symbol] = Monoid[String].imap[Symbol](Symbol.apply)(_.name)
