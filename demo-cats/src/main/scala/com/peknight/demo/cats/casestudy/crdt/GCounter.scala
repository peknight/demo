package com.peknight.demo.cats.casestudy.crdt

import cats.kernel.CommutativeMonoid
import cats.syntax.foldable._
import cats.syntax.semigroup._
import com.peknight.demo.cats.casestudy.crdt.KeyValueStore._

trait GCounter[F[_, _], K, V] {
  def increment(f: F[K, V])(k: K, v: V)(implicit m: CommutativeMonoid[V]): F[K, V]

  def merge(f1: F[K, V], f2: F[K, V])(implicit b: BoundedSemiLattice[V]): F[K, V]

  def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V
}
object GCounter {
  def apply[F[_, _], K, V](implicit counter: GCounter[F, K, V]) = counter

  implicit def mapGCountInstance[K, V]: GCounter[Map, K, V] = new GCounter[Map, K, V] {
    def increment(f: Map[K, V])(k: K, v: V)(implicit m: CommutativeMonoid[V]): Map[K, V] = {
      val value = v |+| f.getOrElse(k, m.empty)
      f + (k -> value)
    }
    def merge(f1: Map[K, V], f2: Map[K, V])(implicit b: BoundedSemiLattice[V]): Map[K, V] = {
      f1 |+| f2
    }
    def total(f: Map[K, V])(implicit m: CommutativeMonoid[V]): V = {
      f.values.toList.combineAll
    }
  }

  implicit def gcounterInstance[F[_, _], K, V](implicit kvs: KeyValueStore[F], km: CommutativeMonoid[F[K, V]]) = new GCounter[F, K, V] {
    def increment(f: F[K, V])(k: K, v: V)(implicit m: CommutativeMonoid[V]): F[K, V] = {
      val total = f.getOrElse(k, m.empty) |+| v
      f.put(k, total)
    }
    def merge(f1: F[K, V], f2: F[K, V])(implicit b: BoundedSemiLattice[V]): F[K, V] = {
      f1 |+| f2
    }
    def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V = {
      f.values.combineAll
    }
  }
}
