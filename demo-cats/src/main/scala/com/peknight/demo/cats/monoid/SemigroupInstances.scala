package com.peknight.demo.cats.monoid

import cats.Semigroup

object SemigroupInstances:
  given setIntersectionSemigroup[A]: Semigroup[Set[A]] with
    def combine(a: Set[A], b: Set[A]): Set[A] = a intersect b