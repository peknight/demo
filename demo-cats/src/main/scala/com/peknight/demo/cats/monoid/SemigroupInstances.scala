package com.peknight.demo.cats.monoid

import cats.Semigroup

object SemigroupInstances {
  implicit def setIntersectionSemigroup[A]: Semigroup[Set[A]] = new Semigroup[Set[A]] {
    def combine(a: Set[A], b: Set[A]) = a intersect b
  }
}
