package com.peknight.demo.fpinscala.applicative

import com.peknight.demo.fpinscala.applicative.Applicative.{Const, monoidApplicative}
import com.peknight.demo.fpinscala.applicative.Traverse.{Id, idMonad}
import com.peknight.demo.fpinscala.monads.{Functor, Monad}
import com.peknight.demo.fpinscala.monoids.{Foldable, Monoid}

trait Traverse[F[_]] extends Functor[F] with Foldable[F] {
  def traverse[G[_], A, B](fa: F[A])(f: A => G[B])(implicit G: Applicative[G]): G[F[B]] = sequence(map(fa)(f))
  def sequence[G[_], A](fga: F[G[A]])(implicit G: Applicative[G]): G[F[A]] = traverse(fga)(ga => ga)
  def map[A, B](fa: F[A])(f: A => B): F[B] = traverse[Id, A, B](fa)(a => f(a))(idMonad)
  override def foldMap[A, M](as: F[A])(f: A => M)(mb: Monoid[M]): M =
    traverse[({type f[x] = Const[M, x]})#f, A, Nothing](as)(f)(monoidApplicative(mb))
}
object Traverse {
  type Id[A] = A

  val idMonad = new Monad[Id] {
    def unit[A](a: => A) = a
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
  }
}
