package com.peknight.demo.cats.casestudy.datavalidation

import cats.Semigroup
import cats.syntax.either.*
import cats.syntax.semigroup.*

final case class CheckF[E, A](func: A => Either[E, A]):
  def apply(a: A): Either[E, A] = func(a)

  def and(that: CheckF[E, A])(using s: Semigroup[E]): CheckF[E, A] = CheckF { a =>
    (this(a), that(a)) match
      case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft[A]
      case (Left(e), Right(_)) => e.asLeft[A]
      case (Right(_), Left(e)) => e.asLeft[A]
      case (Right(_), Right(_)) => a.asRight[E]
  }
