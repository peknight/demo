package com.peknight.demo.fpinscala.errorhandling

import com.peknight.demo.fpinscala.errorhandling.Either.*

sealed trait Either[+E, +A]:

  def map[B](f: A => B): Either[E, B] = this match
    case Right(a) => Right(f(a))
    case l @ Left(_) => l

  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match
    case Right(a) => f(a)
    case l @ Left(_) => l

  def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = this match
    case Left(_) => b
    case r @ Right(_) => r

  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = flatMap(aa => b.map(bb => f(aa, bb)))

end Either

object Either:

  case class Left[+E](value: E) extends Either[E, Nothing]
  case class Right[+A](value: A) extends Either[Nothing, A]

  def Try[A](a: => A): Either[Exception, A] =
    try Right(a)
    catch case e: Exception => Left(e)

  def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = traverse(es)(identity)

  def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = as match
    case Nil => Right(Nil)
    case h :: t => f(h).map2(traverse(t)(f))(_ :: _)

  def traverseViaFoldRightAndMap2[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
    as.foldRight[Either[E, List[B]]](Right(Nil))((a, es) => f(a).map2(es)(_ :: _))

end Either
