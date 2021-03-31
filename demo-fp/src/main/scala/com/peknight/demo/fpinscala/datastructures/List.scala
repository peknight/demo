package com.peknight.demo.fpinscala.datastructures

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

sealed trait List[+A] {
}

object List {
  case object Nil extends List[Nothing]
  case class Cons[+A](head: A, tail: List[A]) extends List[A]

  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  // Exercise 3.2
  def tail[A](as: List[A]): List[A] =
    as match {
      case Cons(_, t) => t
      case Nil => sys.error("tail of empty list")
    }

  // Exercise 3.3
  def setHead[A](as: List[A], head: A): List[A] =
    as match {
      case Cons(_, t) => Cons(head, t)
      case Nil => sys.error("setHead on empty list")
    }

  // Exercise 3.4
  @tailrec
  def drop[A](as: List[A], n: Int): List[A] =
    if (n <= 0) as
    else as match {
      case Nil => Nil
      case Cons(_, t) => drop(t, n - 1)
    }

  // Exercise 3.5
  def dropWhile[A](l: List[A])(f: A => Boolean): List[A] =
    l match {
      case Cons(h, t) if f(h) => dropWhile(t)(f)
      case _ => l
    }

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h, t) => Cons(h, append(t, a2))
    }

  // Exercise 3.6
  def init[A](l: List[A]): List[A] =
    l match {
      case Nil => sys.error("init of empty list")
      case Cons(_, Nil) => Nil
      case Cons(h, t) => Cons(h, init(t))
    }

  def init2[A](l: List[A]): List[A] = {
    val buf = new ListBuffer[A]
    @tailrec
    def go(cur: List[A]): List[A] = cur match {
      case Nil => sys.error("init of empty list")
      case Cons(_, Nil) => List(buf.toList: _*)
      case Cons(h, t) => {
        buf += h
        go(t)
      }
    }
    go(l)
  }

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sumWithFoldRight(ns: List[Int]) = foldRight(ns, 0)(_ + _)

  def productWithFoldRight(ns: List[Double]) = foldRight(ns, 1.0)(_ * _)

  // Exercise 3.9
  def length[A](as: List[A]): Int = foldRight(as, 0)((_, acc) => 1 + acc)

  // Exercise 3.10
  @tailrec
  def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B =
    as match {
      case Nil => z
      case Cons(h, t) => foldLeft(t, f(z, h))(f)
    }

  // Exercise 3.11
  def sumWithFoldLeft(ns: List[Int]) = foldLeft(ns, 0)(_ + _)
  def productWithFoldLeft(ns: List[Double]) = foldLeft(ns, 1.0)(_ * _)
  def lengthWithFoldLeft[A](ns: List[A]): Int = foldLeft(ns, 0)((acc, _) => acc + 1)

  // Exercise 3.12
  def reverse[A](as: List[A]): List[A] = foldLeft(as, Nil: List[A])((acc, a) => Cons(a, acc))

  // Exercise 3.13
  def foldLeftViaFoldRight[A, B](as: List[A], z: B)(f: (B, A) => B): B =
    foldRight(reverse(as), z)((a, b) => f(b, a))

  def foldRightViaFoldLeft[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    foldLeft(reverse(as), z)((b, a) => f(a, b))

  def foldRightViaFoldLeft_1[A,B](l: List[A], z: B)(f: (A, B) => B): B =
    foldLeft(l, (b: B) => b)((g, a) => b => g(f(a, b)))(z)
  // g, List
  // b => b, 1 :: 2 :: 3 :: Nil
  // b => f(1, b), 2 :: 3 :: Nil
  // b => f(1, f(2, b)), 3 :: Nil
  // b => f(1, f(2, f(3, b))), Nil
  // f(1, f(2, f(3, z)))
  // 这也太烧脑了。。。

  def foldLeftViaFoldRight_1[A,B](l: List[A], z: B)(f: (B,A) => B): B =
    foldRight(l, (b: B) => b)((a, g) => b => g(f(b, a)))(z)
  // 同理

  // Exercise 3.14
  def appendViaFoldRight[A](a1: List[A], a2: List[A]): List[A] =
    foldRight(a1, a2)(Cons(_, _))

  def appendViaFoldLeft[A](a1: List[A], a2: List[A]): List[A] =
    foldLeft(a1, (b: List[A]) => b)((g, a) => b => g(Cons(a, b)))(a2)

}