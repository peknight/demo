package com.peknight.demo.fpinscala.applicative

import com.peknight.demo.fpinscala.monads.Functor
import com.peknight.demo.fpinscala.monoids.Monoid

trait Applicative[F[_]] extends Functor[F] { self =>

  def apply[A, B](fab: F[A => B])(fa: F[A]): F[B] = map2(fab, fa)((f, a) => f(a))

  def unit[A](a: => A): F[A]

  // Exercise 12.2
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = apply(map(fa)(f.curried))(fb)

  def map[A, B](fa: F[A])(f: A => B): F[B] = apply(unit(f))(fa)
  def mapViaMap2[A, B](fa: F[A])(f: A => B): F[B] = map2(fa, unit(()))((a, _) => f(a))

  def traverse[A, B](as: List[A])(f: A => F[B]): F[List[B]] =
    as.foldRight(unit(List.empty[B]))((a, fbs) => map2(f(a), fbs)(_ :: _))

  // Exercise 12.1

  def sequence[A](fas: List[F[A]]): F[List[A]] = traverse(fas)(identity)

  def replicateM[A](n: Int)(fa: F[A]): F[List[A]] = sequence(List.fill(n)(fa))

  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = map2(fa, fb)((_, _))

  // Exercise 12.3
  def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] =
    apply(apply(apply(unit(f.curried))(fa))(fb))(fc)

  def map4[A, B, C, D, E](fa: F[A], fb: F[B], fc: F[C], fd: F[D])(f: (A, B, C, D) => E): F[E] =
    apply(apply(apply(apply(unit(f.curried))(fa))(fb))(fc))(fd)

  // Exercise 12.8

  def product[G[_]](G: Applicative[G]): Applicative[({type f[x] = (F[x], G[x])})#f] = new Applicative[({type f[x] = (F[x], G[x])})#f] {
    def unit[A](a: => A): (F[A], G[A]) = (self.unit(a), G.unit(a))
    override def apply[A, B](fab: (F[A => B], G[A => B]))(fa: (F[A], G[A])): (F[B], G[B]) =
      (self.apply(fab._1)(fa._1), G.apply(fab._2)(fa._2))
  }

  // Exercise 12.9

  def compose[G[_]](G: Applicative[G]): Applicative[({type f[x] = F[G[x]]})#f] = new Applicative[({type f[x] = F[G[x]]})#f] {
    def unit[A](a: => A): F[G[A]] = self.unit(G.unit(a))
    override def apply[A, B](fab: F[G[A => B]])(fa: F[G[A]]): F[G[B]] = self.map2(fab, fa)((gab, gb) => G.apply(gab)(gb))
    override def map2[A, B, C](fa: F[G[A]], fb: F[G[B]])(f: (A, B) => C): F[G[C]] = self.map2(fa, fb)(G.map2(_, _)(f))
  }

  // Exercise 12.10 TODO https://github.com/runarorama/sannanir/blob/master/Applicative.v

  // Exercise 12.12

  def sequenceMap[K, V](ofa: Map[K, F[V]]): F[Map[K, V]] =
    ofa.foldLeft(unit(Map.empty[K, V])){ case (acc, (k, fv)) => map2(acc, fv)((m, v) => m + (k -> v)) }
//    ofa.foldRight(unit(Map.empty[K, V])){ case ((k, fv), map) => map2(fv, map)((v, m) => m.updated(k, v)) }

}

object Applicative {

  def assoc[A, B, C](p: (A, (B, C))): ((A, B), C) = p match { case (a, (b, c)) => ((a, b), c) }

  def productF[I, O, I2, O2](f: I => O, g: I2 => O2): (I, I2) => (O, O2) = (i, i2) => (f(i), g(i2))

  type Const[M, B] = M

  import scala.language.implicitConversions

  given monoidApplicative[M]: Conversion[Monoid[M], Applicative[[B] =>> Const[M, B]]] = (m: Monoid[M]) =>
    new Applicative[[B] =>> Const[M, B]]:
      def unit[A](a: => A): Const[M, A] = m.zero
      override def map2[A, B, C](fa: Const[M, A], fb: Const[M, B])(f: (A, B) => C): Const[M, C] = m.op(fa, fb)
}
