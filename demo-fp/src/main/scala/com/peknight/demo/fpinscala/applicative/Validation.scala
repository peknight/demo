package com.peknight.demo.fpinscala.applicative

sealed trait Validation[+E, +A]

object Validation {
  case class Failure[E](head: E, tail: Vector[E] = Vector()) extends Validation[E, Nothing]
  case class Success[A](a: A) extends Validation[Nothing, A]

  // Exercise 12.6

  def validationApplicative[E] = new Applicative[({type f[x] = Validation[E, x]})#f] {
    def unit[A](a: => A): Validation[E, A] = Success(a)
    override def apply[A, B](fab: Validation[E, A => B])(fa: Validation[E, A]): Validation[E, B] = (fab, fa) match {
      case (Success(f), Success(a)) => Success(f(a))
      case (Failure(fh, ft), Failure(eh, et)) => Failure(fh, ft ++ Vector(eh) ++ et)
      case (f @ Failure(_, _), _) => f
      case (_, f @ Failure(_, _)) => f
    }

    override def map2[A, B, C](fa: Validation[E, A], fb: Validation[E, B])(f: (A, B) => C): Validation[E, C] =
      (fa, fb) match {
        case (Success(a), Success(b)) => Success(f(a, b))
        case (Failure(h1, t1), Failure(h2, t2)) => Failure(h1, t1 ++ Vector(h2) ++ t2)
        case (e @ Failure(_, _), _) => e
        case (_, e @ Failure(_, _)) => e
      }
  }
}