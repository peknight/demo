package com.peknight.demo.cats.casestudy.datavalidation

import cats.Semigroup
import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.syntax.apply.*
import cats.syntax.semigroup.*
import com.peknight.demo.cats.casestudy.datavalidation.OldCheck.*

sealed trait OldCheck[E, A]:
  def and(that: OldCheck[E, A]): OldCheck[E, A] = And(this, that)

  def apply(a: A)(using Semigroup[E]): Validated[E, A] = this match
    case Pure(func) => func(a)
    case And(left, right) => (left(a), right(a)).mapN((_, _) => a)
    case Or(left, right) => left(a) match
      case Valid(a) => Valid(a)
      case Invalid(e1) => right(a) match
        case Valid(a) => Valid(a)
        case Invalid(e2) => Invalid(e1 |+| e2)

object OldCheck:
  final case class And[E, A](left: OldCheck[E, A], right: OldCheck[E, A]) extends OldCheck[E, A]
  final case class Or[E, A](left: OldCheck[E, A], right: OldCheck[E, A]) extends OldCheck[E, A]
  final case class Pure[E, A](func: A => Validated[E, A]) extends OldCheck[E, A]

  def pure[E, A](f: A => Validated[E, A]): OldCheck[E, A] = Pure(f)
