package com.peknight.demo.cats.casestudy.crdt

import cats.kernel.CommutativeMonoid

trait BoundedSemiLattice[A] extends CommutativeMonoid[A]:
  def combine(a1: A, a2: A): A
  def empty: A

object BoundedSemiLattice:
  given BoundedSemiLattice[Int] with
    def combine(a1: Int, a2: Int): Int = a1 max a2
    def empty: Int = 0

  given setInstance[A]: BoundedSemiLattice[Set[A]] with
    def combine(a1: Set[A], a2: Set[A]): Set[A] = a1 union a2
    def empty: Set[A] = Set.empty[A]
