package com.peknight.demo.fpinscala.laziness

import com.peknight.demo.fpinscala.laziness.Stream._

import scala.annotation.tailrec

sealed trait Stream[+A] {
  def headOption: Option[A] = this match {
    case Cons(h, _) => Some(h())
    case Empty => None
  }

  // Exercise 5.1 use recursive not stack safe
  def toListRecursive: List[A] = this match {
    case Cons(h, t) => h() :: t().toListRecursive
    case Empty => Nil
  }

  // Exercise 5.1 use reverse
  def toList: List[A] = {
    @tailrec
    def go(s: Stream[A], acc: List[A]): List[A] = s match {
      case Cons(h, t) => go(t(), h() :: acc)
      case _ => acc
    }
    go(this, Nil).reverse
  }

  // Exercise 5.1 use ListBuffer
  def toListFast: List[A] = {
    val buf = new collection.mutable.ListBuffer[A]
    def go(s: Stream[A]): List[A] = s match {
      case Cons(h, t) =>
        buf += h()
        go(t())
      case _ => buf.toList
    }
    go(this)
  }

  // Exercise 5.2
  def take(n: Int): Stream[A] = this match {
    case Cons(h, t) if n > 1 => cons(h(), t().take(n - 1))
    case Cons(h, _) if n == 1 => cons(h(), empty)
    case _ => empty
  }

  @tailrec
  final def drop(n: Int): Stream[A] = this match {
    case Cons(_, t) if n > 0 => t().drop(n - 1)
    case s => s
  }

  def takeWhile(p: A => Boolean): Stream[A] = this match {
    case Cons(h, t) if p(h()) => cons(h(), t().takeWhile(p))
    case _ => empty
  }

  @tailrec
  final def exists(p: A => Boolean): Boolean = this match {
    case Cons(h, t) => p(h()) || t().exists(p)
    case _ => false
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    case Cons(h, t) => f(h(), t().foldRight(z)(f))
    case _ => z
  }

  def existsViaFoldRight(p: A => Boolean): Boolean = foldRight(false)((a, b) => p(a) || b)

  // Exercise 5.4
  def forAll(p: A => Boolean): Boolean = foldRight(true)((a, b) => p(a) && b)

  @tailrec
  final def forAllWithRecursiveLoop(p: A => Boolean): Boolean = this match {
    case Empty => true
    case Cons(h, t) if p(h()) => t().forAllWithRecursiveLoop(p)
    case _ => false
  }

  // Exercise 5.5
  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] = foldRight(empty[A])((a, b) => {
    if (p(a)) cons(a, b)
    else empty
  })

  // Exercise 5.6
  def headOptionViaFoldRight: Option[A] = foldRight[Option[A]](None)((h, _) => Some(h))

  // Exercise 5.7
  def map[B](f: A => B): Stream[B] = foldRight(empty[B])((a, b) => cons(f(a), b))

  def filter(p: A => Boolean): Stream[A] = foldRight(empty[A])((a, b) => if (p(a)) cons(a, b) else b)

  def append[B >: A](sa: => Stream[B]): Stream[B] = foldRight(sa)((a, b) => cons(a, b))

  def flatMap[B](f: A => Stream[B]): Stream[B] = foldRight(empty[B])((a, b) => f(a).append(b))

  def find(p: A => Boolean): Option[A] = filter(p).headOptionViaFoldRight

  // Exercise 5.13
  def mapViaUnfold[B](f: A => B): Stream[B] = unfold(this) {
    case Cons(h, t) => Some((f(h()), t()))
    case Empty => None
  }

  def takeViaUnfold(n: Int): Stream[A] = unfold((this, n)) {
    case (Cons(h, t), 1) => Some((h(), (empty, 0)))
    case (Cons(h, t), n) if n > 1 => Some(h(), (t(), n - 1))
    case _ => None
  }

  def takeWhileViaUnfold(p: A => Boolean): Stream[A] = unfold(this) {
    case Cons(h, t) if p(h()) => Some((h(), t()))
    case _ => None
  }

  def zipWith[B, C](s2: Stream[B])(f: (A, B) => C): Stream[C] = unfold((this, s2)) {
    case (Cons(ha, ta), Cons(hb, tb)) => Some((f(ha(), hb()), (ta(), tb())))
    case _ => None
  }

  def zip[B](s2: Stream[B]): Stream[(A, B)] = zipWith(s2)((_, _))

  def zipAll[B](s2: Stream[B]): Stream[(Option[A], Option[B])] = zipWithAll(s2)((_, _))

  def zipWithAll[B, C](s2: Stream[B])(f: (Option[A], Option[B]) => C): Stream[C] = unfold((this, s2)) {
    case (Cons(ha, ta), Cons(hb, tb)) => Some((f(Some(ha()), Some(hb())), (ta(), tb())))
    case (Cons(ha, ta), Empty) => Some((f(Some(ha()), None), (ta(), Empty)))
    case (Empty, Cons(hb, tb)) => Some((f(None, Some(hb())), (Empty, tb())))
    case (Empty, Empty) => None
  }

  // Exercise 5.14
  def startsWith[A](prefix: Stream[A]): Boolean =
    zipAll(prefix).takeWhile(!_._2.isEmpty) forAll {
      case (h1, h2) => h1 == h2
    }

  // Exercise 5.15
  def tails: Stream[Stream[A]] = unfold(this) {
    case s @ Cons(_, t) => Some((s, t()))
    case Empty => None
  } append Stream(empty)

  def hasSubsequence[A](sub: Stream[A]): Boolean = tails exists (_ startsWith sub)

  // Exercise 5.16
  def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] = foldRight((z, Stream(z)))((a, p0) => {
    // p0 is passwd by-name and used in by-name args in f and cons. So use lazy val to ensure only one evaluation...
    lazy val p1 = p0
    val b2 = f(a, p1._1)
    (b2, cons(b2, p1._2))
  })._2

  def tailsViaScanRight: Stream[Stream[A]] = scanRight(empty[A])((a, b) => {
    cons(a, b)
  })
}
object Stream {
  case object Empty extends Stream[Nothing]
  case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]
  def cons[A](hd: => A, t1: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = t1
    Cons(() => head, () => tail)
  }
  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] = cons(1, ones)

  // Exercise 5.8
  def constant[A](a: A): Stream[A] = {
    // cons(a, constant(a))
    // This is more efficient than `cons(a, constant(a))` since it's just
    // one object referencing itself.
    lazy val tail: Stream[A] = Cons(() => a, () => tail)
    tail
  }

  // Exercise 5.9 这个引用是不同的，不能像constant一样复用同一个引用
  def from(n: Int): Stream[Int] = cons(n, from(n + 1))

  // Exercise 5.10
  val fibs: Stream[Int] = {
    def go(current: Int, next: Int): Stream[Int] = cons(current, go(next, current + next))
    go(0, 1)
  }

  // Exercise 5.11
  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = f(z) match {
    case Some((a, s)) => cons(a, unfold(s)(f))
    case None => empty
  }

  // Exercise 5.12
  val fibsViaUnfold: Stream[Int] = unfold((0, 1)){
    case (current, next) => Some(current, (next, current + next))
  }

  def fromViaUnfold(n: Int): Stream[Int] = unfold(n)(s => Some(s, s + 1))

  def constantViaUnfold[A](a: A): Stream[A] = unfold(a)(_ => Some(a, a))

  val onesViaUnfold: Stream[Int] = unfold(1)(_ => Some(1, 1))

}
