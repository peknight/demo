package com.peknight.demo.fpinscala.applicative

import com.peknight.demo.fpinscala.applicative.Applicative.{Const, monoidApplicative}
import com.peknight.demo.fpinscala.applicative.Traverse.{Id, idMonad}
import com.peknight.demo.fpinscala.monads.Monad.stateMonad
import com.peknight.demo.fpinscala.monads.{Functor, Monad}
import com.peknight.demo.fpinscala.monoids.{Foldable, Monoid}
import com.peknight.demo.fpinscala.state.State

trait Traverse[F[_]] extends Functor[F] with Foldable[F] { self =>
  def traverse[G[_], A, B](fa: F[A])(f: A => G[B])(implicit G: Applicative[G]): G[F[B]] = sequence(map(fa)(f))
  def sequence[G[_], A](fga: F[G[A]])(implicit G: Applicative[G]): G[F[A]] = traverse(fga)(ga => ga)
  def map[A, B](fa: F[A])(f: A => B): F[B] = traverse[Id, A, B](fa)(a => f(a))(idMonad)
  override def foldMap[A, M](as: F[A])(f: A => M)(mb: Monoid[M]): M =
    traverse[({type f[x] = Const[M, x]})#f, A, Nothing](as)(f)(monoidApplicative(mb))

  def traverseS[S, A, B](fa: F[A])(f: A => State[S, B]): State[S, F[B]] = traverse[({type f[x] = State[S, x]})#f, A, B](fa)(f)(stateMonad)

  def zipWithIndex[A](ta: F[A]): F[(A, Int)] = traverseS(ta)((a: A) => (for {
    i <- State.get[Int]
    _ <- State.set(i + 1)
  } yield (a, i))).run(0)._1

  override def toList[A](fa: F[A]): List[A] = traverseS(fa)((a: A) => (for {
    as <- State.get[List[A]]
    _ <- State.set(a :: as)
  } yield ())).run(List.empty[A])._2.reverse

  def mapAccum[S, A, B](fa: F[A], s: S)(f: (A, S) => (B, S)): (F[B], S) = traverseS(fa)((a: A) => (for {
    s1 <- State.get[S]
    (b, s2) = f(a, s1)
    _ <- State.set(s2)
  } yield b)).run(s)

  def toListViaMapAccum[A](fa: F[A]): List[A] = mapAccum(fa, List.empty[A])((a, s) => ((), a :: s))._2.reverse

  def zipWithIndexViaMapAccum[A](ta: F[A]): F[(A, Int)] = mapAccum(ta, 0)((a, s) => ((a, s), s + 1))._1

  /*
   * Exercise 12.16
   * We need to use a stack. Fortunately a `List` is the same thing as a stack,
   * and we already know how to turn any traversable into a list!
   */
  def reverse[A](fa: F[A]): F[A] = mapAccum(fa, toList(fa).reverse)((_, as) => (as.head, as.tail))._1

  // Exercise 12.17

  override def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B = mapAccum(as, z)((a, s) => ((), f(s, a)))._2

  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] = mapAccum(fa, toList(fb)) {
    case (_, Nil) => sys.error("zip: Incompatible shapes")
    case (a, b :: bs) => ((a, b), bs)
  }._1

  def zipL[A, B](fa: F[A], fb: F[B]): F[(A, Option[B])] = mapAccum(fa, toList(fb)) {
    case (a, Nil) => ((a, None), Nil)
    case (a, b :: bs) => ((a, Some(b)), bs)
  }._1

  def zipR[A, B](fa: F[A], fb: F[B]): F[(Option[A], B)] = mapAccum(fb, toList(fa)) {
    case (b, Nil) => ((None, b), Nil)
    case (b, a :: as) => ((Some(a), b), as)
  }._1

  // Exercise 12.18

  def fuse[G[_], H[_], A, B](fa: F[A])(f: A => G[B], g: A => H[B])(implicit G: Applicative[G], H: Applicative[H]): (G[F[B]], H[F[B]]) =
    traverse[({type f[x] = (G[x], H[x])})#f, A, B](fa)(a => (f(a), g(a)))(G product H)

  // Exercise 12.19

  def compose[G[_]](implicit G: Traverse[G]): Traverse[({type f[x] = F[G[x]]})#f] = new Traverse[({type f[x] = F[G[x]]})#f] {
    override def traverse[M[_], A, B](fa: F[G[A]])(f: A => M[B])(implicit M: Applicative[M]): M[F[G[B]]] =
      self.traverse(fa)(ha => G.traverse(ha)(f))
  }
}

object Traverse {
  type Id[A] = A

  val idMonad = new Monad[Id] {
    def unit[A](a: => A) = a
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
  }
}
