package com.peknight.demo.fpinscala.errorhandling

sealed trait Option[+A] {

  import Option._

  def map[B](f: A => B): Option[B] = this match {
    case Some(a) => Some(f(a))
    case None => None
  }

  def flatMap[B](f: A => Option[B]): Option[B] = this match {
    case Some(a) => f(a)
    case None => None
  }

  def flatMapViaGetOrElse[B](f: A => Option[B]): Option[B] =
    map(f) getOrElse None

  def getOrElse[B >: A](default: => B): B = this match {
    case Some(a) => a
    case None => default
  }

  def orElse[B >: A](ob: => Option[B]): Option[B] = this match {
    case oa @ Some(_) => oa
    case None => ob
  }

  def orElseViaGetOrElse[B >: A](ob: => Option[B]): Option[B] =
    this map (Some(_)) getOrElse ob

  def filter(f: A => Boolean): Option[A] = this match {
    case Some(a) if f(a)=> this
    case _ => None
  }

  def filterViaFlatMap(f: A => Boolean): Option[A] =
    flatMap(a => if (f(a)) Some(a) else None)
}

object Option {
  case class Some[+A](get: A) extends Option[A]
  case object None extends Option[Nothing]

  def some[A](get: A): Option[A] = Some(get)
  def none[A]: Option[A] = None

  def lift[A, B](f: A => B): Option[A] => Option[B] = _ map f

  def Try[A](a: => A): Option[A] = try Some(a) catch { case e: Exception => None }

  // Exercise 4.3
  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = (a, b) match {
    case (Some(sa), Some(sb)) => Some(f(sa, sb))
    case _ => None
  }

  def map2ViaFlatMap[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a.flatMap(sa => b.map(sb => f(sa, sb)))

  def map2ViaForComprehension[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    for {
      aa <- a
      bb <- b
    } yield f(aa, bb)

  def sequence[A](a: List[Option[A]]): Option[List[A]] =
    a match {
      case Nil => Some(Nil)
      case h :: t => h flatMap (hh => sequence(t) map (hh :: _))
    }

  def sequenceViaFoldRightAndMap2[A](a: List[Option[A]]): Option[List[A]] =
    a.foldRight[Option[List[A]]](Some(Nil))((x, y) => map2(x, y)(_ :: _))

  def parseInts(a: List[String]): Option[List[Int]] = sequence(a map (i => Try(i.toInt)))

  // Exercise 4.5
  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] =
    a match {
      case Nil => Some(Nil)
      case h :: t => map2(f(h), traverse(t)(f))(_ :: _)
    }

  def traverseViaFoldRightAndMap2[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] =
    a.foldRight[Option[List[B]]](Some(Nil))((x, y) => map2(f(x), y)(_ :: _))

  def sequenceViaTraverse[A, B](a: List[Option[A]]): Option[List[A]] = traverse(a)(identity)

}