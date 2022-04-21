package com.peknight.demo.fpinscala.monoids

import com.peknight.demo.fpinscala.datastructures.Tree
import com.peknight.demo.fpinscala.monoids.WC.{Part, Stub}

object MonoidsApp extends App {
  def stringMonoid = new Monoid[String] {
    def op(a1: String, a2: String) = a1 + a2
    def zero = ""
  }

  def listMonoid[A] = new Monoid[List[A]] {
    def op(a1: List[A], a2: List[A]): List[A] = a1 ++ a2
    def zero: List[A] = List.empty[A]
  }

  // Exercise 10.1

  val intAddition: Monoid[Int] = new Monoid[Int] {
    def op(a1: Int, a2: Int): Int = a1 + a2
    def zero: Int = 0
  }

  val intMultiplication: Monoid[Int] = new Monoid[Int] {
    def op(a1: Int, a2: Int): Int = a1 * a2
    def zero: Int = 1
  }

  val booleanOr: Monoid[Boolean] = new Monoid[Boolean] {
    def op(a1: Boolean, a2: Boolean): Boolean = a1 || a2
    def zero: Boolean = false
  }

  val booleanAnd: Monoid[Boolean] = new Monoid[Boolean] {
    def op(a1: Boolean, a2: Boolean): Boolean = a1 && a2
    def zero: Boolean = true
  }

  // Exercise 10.2

  def optionMonoid[A]: Monoid[Option[A]] = new Monoid[Option[A]] {
    def op(x: Option[A], y: Option[A]) = x orElse y
    def zero = None
  }

  def dual[A](m: Monoid[A]): Monoid[A] = new Monoid[A] {
    // 反向计算
    def op(a1: A, a2: A): A = m.op(a2, a1)
    def zero: A = m.zero
  }

  def firstOptionMonoid[A]: Monoid[Option[A]] = optionMonoid[A]
  def lastOptionMonoid[A]: Monoid[Option[A]] = dual(firstOptionMonoid[A])

  // Exercise 10.3 endofunction 自函数

  def endoMonoid[A]: Monoid[A => A] = new Monoid[A => A] {
    // 反向即 a1 andThen a2
    def op(a1: A => A, a2: A => A): A => A = a1 compose a2
    def zero: A => A = identity
  }

  // Exercise 10.4

  import com.peknight.demo.fpinscala.testing.*
  import com.peknight.demo.fpinscala.testing.Prop.*
  def monoidLaws[A](m: Monoid[A], gen: Gen[A])(using CanEqual[A, A]): Prop =
    forAll(for {
      x <- gen
      y <- gen
      z <- gen
    } yield (x, y, z))(p => m.op(p._1, m.op(p._2, p._3)) == m.op(m.op(p._1, p._2), p._3)) &&
      forAll(gen)((a: A) => m.op(a, m.zero) == a && m.op(m.zero, a) == a)

  val words = List("Hic", "Est", "Index")
  val s = words.foldRight(stringMonoid.zero)(stringMonoid.op)
  val t = words.foldLeft(stringMonoid.zero)(stringMonoid.op)
  println(s)
  println(t)

  def concatenate[A](as: List[A], m: Monoid[A]): A = as.foldLeft(m.zero)(m.op)

  // Exercise 10.5

  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B = as.foldLeft(m.zero)((b, a) => m.op(b, f(a)))

  // Exercise 10.6

  def foldRightViaFoldMap[A, B](as: List[A])(z: B)(f: (A, B) => B): B = foldMap(as, endoMonoid[B])(f.curried)(z)

  def foldLeftViaFoldMap[A, B](as: List[A])(z: B)(f: (B, A) => B): B = foldMap(as, dual(endoMonoid[B]))(a => b => f(b, a))(z)

  // Exercise 10.7

  println(List("lorem", "ipsum", "dolor", "sit").foldLeft("")(_ + _))

  def foldMapV[A, B](as: IndexedSeq[A], m: Monoid[B])(f: A => B): B = {
    val length = as.length
    if (length == 0) m.zero
    else if (length == 1) f(as(0))
    else {
      val (l, r) = as.splitAt(length / 2)
      m.op(foldMapV(l, m)(f), foldMapV(r, m)(f))
    }
  }

  println(foldMapV(IndexedSeq("lorem", "ipsum", "dolor", "sit"), stringMonoid)(identity))

  // Exercise 10.8

  import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par
  import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par.*
  def par[A](m: Monoid[A]): Monoid[Par[A]] = new Monoid[Par[A]] {
    def zero = Par.unit(m.zero)
    def op(a: Par[A], b: Par[A]) = Par.map2(a, b)(m.op)
  }

  def parFoldMap[A, B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): Par[B] = Par.parMap(v.toList)(f).flatMap { bs =>
    foldMapV(bs.toIndexedSeq, par(m))(b => lazyUnit(b))
  }

  // Exercise 10.9

  def ordered(is: IndexedSeq[Int]): Boolean = {
    val mon = new Monoid[Option[(Int, Int, Boolean)]] {
      def op(o1: Option[(Int, Int, Boolean)], o2: Option[(Int, Int, Boolean)]) = (o1, o2) match {
        case (Some((x1, y1, p)), Some((x2, y2, q))) => Some((x1 min x2, y1 max y2, p && q && y1 <= x2))
        case (x, None) => x
        case (None, y) => y
      }
      def zero = None
    }
    foldMapV(is, mon)(i => Some((i, i, true))).map(_._3).getOrElse(true)
  }

  // Exercise 10.10

  val wcMonoid: Monoid[WC] = new Monoid[WC] {
    def op(a1: WC, a2: WC): WC = (a1, a2) match {
      case (Stub(x), Stub(y)) => Stub(x + y)
      case (Stub(x), Part(l, w, r)) => Part(x + l, w, r)
      case (Part(l, w, r), Stub(y)) => Part(l, w, r + y)
      case (Part(l1, w1, r1), Part(l2, w2, r2)) => Part(l1, w1 + (if ((r1 + l2).isEmpty) 0 else 1) +w2, r2)
    }
    def zero: WC = Stub("")
  }

  // Exercise 10.11

  def count(s: String): Int = {
    def wc(c: Char): WC = if (c.isWhitespace) Part("", 0, "") else Stub(c.toString)
    def unstub(str: String) = str.length min 1
    foldMapV(s.toIndexedSeq, wcMonoid)(wc) match {
      case Stub(str) => unstub(str)
      case Part(l, w, r) => unstub(l) + w + unstub(r)
    }
  }

  // Exercise 10.12

  object ListFoldable extends Foldable[List] {
    override def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B): B = as.foldRight(z)(f)
    override def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B): B = as.foldLeft(z)(f)
    override def foldMap[A, B](as: List[A])(f: A => B)(mb: Monoid[B]): B = as.foldLeft(mb.zero)((b, a) => mb.op(b, f(a)))
  }

  object IndexedSeqFoldable extends Foldable[IndexedSeq] {
    override def foldRight[A, B](as: IndexedSeq[A])(z: B)(f: (A, B) => B): B = as.foldRight(z)(f)
    override def foldLeft[A, B](as: IndexedSeq[A])(z: B)(f: (B, A) => B): B = as.foldLeft(z)(f)
    override def foldMap[A, B](as: IndexedSeq[A])(f: A => B)(mb: Monoid[B]): B = foldMapV(as, mb)(f)
  }

  object LazyListFoldable extends Foldable[LazyList] {
    override def foldRight[A, B](as: LazyList[A])(z: B)(f: (A, B) => B): B = as.foldRight(z)(f)
    override def foldLeft[A, B](as: LazyList[A])(z: B)(f: (B, A) => B): B = as.foldLeft(z)(f)
  }

  // Exercise 10.13

  object TreeFoldable extends Foldable[Tree] {
    override def foldMap[A, B](as: Tree[A])(f: A => B)(mb: Monoid[B]): B = as match {
      case Tree.Leaf(value) => f(value)
      case Tree.Branch(left, right) => mb.op(foldMap(left)(f)(mb), foldMap(right)(f)(mb))
    }

    override def foldLeft[A, B](as: Tree[A])(z: B)(f: (B, A) => B): B = as match {
      case Tree.Leaf(value) => f(z, value)
      case Tree.Branch(left, right) => foldLeft(right)(foldLeft(left)(z)(f))(f)
    }

    override def foldRight[A, B](as: Tree[A])(z: B)(f: (A, B) => B): B = as match {
      case Tree.Leaf(value) => f(value, z)
      case Tree.Branch(left, right) => foldRight(left)(foldRight(right)(z)(f))(f)
    }
  }

  // Exercise 10.14

  object OptionFoldable extends Foldable[Option] {
    override def foldLeft[A, B](as: Option[A])(z: B)(f: (B, A) => B): B = as match {
      case Some(value) => f(z, value)
      case None => z
    }

    override def foldRight[A, B](as: Option[A])(z: B)(f: (A, B) => B): B = as match {
      case Some(value) => f(value, z)
      case None => z
    }

    override def foldMap[A, B](as: Option[A])(f: A => B)(mb: Monoid[B]): B = as match {
      case Some(value) => f(value)
      case None => mb.zero
    }
  }

  def productMonoid[A, B](ma: Monoid[A], mb: Monoid[B]): Monoid[(A, B)] = new Monoid[(A, B)] {
    def op(a1: (A, B), a2: (A, B)): (A, B) = (ma.op(a1._1, a2._1), mb.op(a1._2, a2._2))
    def zero: (A, B) = (ma.zero, mb.zero)
  }

  def mapMergeMonoid[K, V](vm: Monoid[V]): Monoid[Map[K, V]] = new Monoid[Map[K, V]] {
    def op(a1: Map[K, V], a2: Map[K, V]): Map[K, V] = (a1.keySet ++ a2.keySet).foldLeft(zero) { (acc, k) =>
      acc.updated(k, vm.op(a1.getOrElse(k, vm.zero), a2.getOrElse(k, vm.zero)))
    }
    def zero: Map[K, V] = Map.empty[K, V]
  }

  val stringStringIntMapMonoid: Monoid[Map[String, Map[String, Int]]] = mapMergeMonoid(mapMergeMonoid(intAddition))

  val m1 = Map("o1" -> Map("i1" -> 1, "i2" -> 2))
  val m2 = Map("o1" -> Map("i2" -> 3))
  val m3 = stringStringIntMapMonoid.op(m1, m2)
  println(m3)

  // Exercise 10.17

  def functionMonoid[A, B](mb: Monoid[B]): Monoid[A => B] = new Monoid[A => B] {
    def op(a1: A => B, a2: A => B): A => B = a => mb.op(a1(a), a2(a))
    def zero: A => B = _ => mb.zero
  }

  // Exercise 10.18

  def bag[A](as: IndexedSeq[A]): Map[A, Int] = foldMapV(as, mapMergeMonoid[A, Int](intAddition))(a => Map(a -> 1))

  val lengthSumMonoid = productMonoid(intAddition, intAddition)
  val lengthSum = ListFoldable.foldMap(List(1, 2, 3, 4))(a => (1, a))(lengthSumMonoid)
  val mean = lengthSum._2 / lengthSum._1.toDouble
  println(mean)
}
