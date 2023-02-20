package com.peknight.demo.cats.laws

import cats.{Eq, Functor}
import org.scalacheck.{Arbitrary, Gen}

sealed trait Tree[+A] derives CanEqual
object Tree:
  case object Leaf extends Tree[Nothing]
  case class Node[A](p: A, left: Tree[A], right: Tree[A]) extends Tree[A]

  given Functor[Tree] with
    def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match
      case Leaf => Leaf
      case Node(p, left, right) => Node(f(p), map(left)(f), map(right)(f))
  end given

  given [A : Eq]: Eq[Tree[A]] = Eq.fromUniversalEquals

  given [A : Arbitrary]: Arbitrary[Tree[A]] =
    Arbitrary(Gen.oneOf(Gen.const(Leaf), for a <- Arbitrary.arbitrary[A] yield Node(a, Leaf, Leaf)))

end Tree
