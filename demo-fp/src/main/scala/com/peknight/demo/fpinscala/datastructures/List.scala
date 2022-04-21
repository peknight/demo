package com.peknight.demo.fpinscala.datastructures

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

sealed trait List[+A] derives CanEqual

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

  // Exercise 3.15
  def concat[A](ls: List[List[A]]): List[A] =
    foldRight(ls, Nil: List[A])(append)

  // Exercise 3.16
  def add1(l: List[Int]): List[Int] =
    foldRight(l, Nil: List[Int])((h, t) => Cons(h + 1, t))

  // Exercise 3.17
  def doubleToString(l: List[Double]): List[String] =
    foldRight(l, Nil: List[String])((h, t) => Cons(h.toString, t))

  // Exercise 3.18
  def map[A, B](as: List[A])(f: A => B): List[B] =
    foldRight(as, Nil: List[B])((h, t) => Cons(f(h), t))

  def map_1[A, B](l: List[A])(f: A => B): List[B] =
    foldRightViaFoldLeft(l, Nil: List[B])((h, t) => Cons(f(h), t))

  def map_2[A, B](l: List[A])(f: A => B): List[B] = {
    val buf = new collection.mutable.ListBuffer[B]
    def go(l: List[A]): Unit = l match {
      case Nil => ()
      case Cons(h, t) => {
        buf += f(h)
        go(t)
      }
    }
    go(l)
    List(buf.toList: _*)
  }

  // Exercise 3.19
  def filter[A](as: List[A])(f: A => Boolean): List[A] =
    foldRight(as, Nil: List[A])((h, t) => if (f(h)) Cons(h, t) else t)

  def filter_1[A](l: List[A])(f: A => Boolean): List[A] =
    foldRightViaFoldLeft(l, Nil: List[A])((h, t) => if (f(h)) Cons(h, t) else t)

  def filter_2[A](l: List[A])(f: A => Boolean): List[A] = {
    val buf = new collection.mutable.ListBuffer[A]
    def go(l: List[A]): Unit = l match {
      case Nil => ()
      case Cons(h, t) => {
        if (f(h)) {
          buf += h
        }
        go(t)
      }
    }
    go(l)
    List(buf.toList: _*)
  }

  def removeAllOddNumber(as: List[Int]): List[Int] =
    filter(as)(_ % 2 == 0)

  // Exercise 3.20
  def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] =
    concat(map(as)(f))

  def flatMapViaFoldRight[A, B](as: List[A])(f: A => List[B]): List[B] =
    foldRight(as, Nil: List[B])((h, t) => append(f(h), t))

  // Exercise 3.21
  def filterViaFlatMap[A](as: List[A])(f: A => Boolean): List[A] =
    flatMap(as)(a => if (f(a)) List(a) else Nil)

  // Exercise 3.22
  def addPairwise(a: List[Int], b: List[Int]): List[Int] = (a, b) match {
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (Cons(h1, t1), Cons(h2, t2)) => Cons(h1 + h2, addPairwise(t1, t2))
  }

  // Exercise 3.23
  def zipWith[A, B, C](a: List[A], b: List[B])(f: (A, B) => C): List[C] = (a, b) match {
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (Cons(ha, ta), Cons(hb, tb)) => Cons(f(ha, hb), zipWith(ta, tb)(f))
  }

  // Exercise 3.24
  @tailrec
  def startsWith[A](l: List[A], prefix: List[A])(using CanEqual[A, A]): Boolean = {
    (l, prefix) match {
      case (_, Nil) => true
      case (Cons(hp, tp), Cons(hb, tb)) if (hp == hb) => startsWith(tp, tb)
      case _ => false
    }
  }
  @tailrec
  def hasSubsequence[A](sup: List[A], sub: List[A])(using CanEqual[A, A]): Boolean =
    sup match {
      case Nil => sub == Nil
      case _ if startsWith(sup, sub) => true
      case Cons(_, t) => hasSubsequence(t, sub)
    }
}