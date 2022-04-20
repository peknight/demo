package com.peknight.demo.cats.casestudy.crdt

import cats.kernel.CommutativeMonoid
import cats.syntax.foldable.*
import cats.syntax.semigroup.*
import com.peknight.demo.cats.casestudy.crdt.KeyValueStore.*

trait GCounter[F[_, _], K, V]:
  def increment(f: F[K, V])(k: K, v: V)(using CommutativeMonoid[V]): F[K, V]

  def merge(f1: F[K, V], f2: F[K, V])(using BoundedSemiLattice[V]): F[K, V]

  def total(f: F[K, V])(using CommutativeMonoid[V]): V

object GCounter:

  def apply[F[_, _], K, V](using counter: GCounter[F, K, V]) = counter

  given mapGCountInstance[K, V]: GCounter[Map, K, V] with
    def increment(f: Map[K, V])(k: K, v: V)(using m: CommutativeMonoid[V]): Map[K, V] =
      val value = v |+| f.getOrElse(k, m.empty)
      f + (k -> value)

    def merge(f1: Map[K, V], f2: Map[K, V])(using BoundedSemiLattice[V]): Map[K, V] = f1 |+| f2

    def total(f: Map[K, V])(using m: CommutativeMonoid[V]): V = f.values.toList.combineAll

  given gcounterInstance[F[_, _], K, V](using KeyValueStore[F], CommutativeMonoid[F[K, V]]): GCounter[F, K, V] with
    def increment(f: F[K, V])(k: K, v: V)(using m: CommutativeMonoid[V]): F[K, V] =
      val total = f.getOrElse(k, m.empty) |+| v
      f.put(k, total)

    def merge(f1: F[K, V], f2: F[K, V])(using BoundedSemiLattice[V]): F[K, V] = f1 |+| f2

    def total(f: F[K, V])(using CommutativeMonoid[V]): V = f.values.combineAll
