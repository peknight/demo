package com.peknight.demo.fpinscala.state

import com.peknight.demo.fpinscala.state.State._

trait RNG {
  def nextInt: (Int, RNG)
}
object RNG {

//  type Rand[+A] = RNG => (A, RNG)
  type Rand[A] = State[RNG, A]

  def randomPair(rng: RNG): ((Int, Int), RNG) = {
    val (i1, rng2) = rng.nextInt
    val (i2, rng3) = rng2.nextInt
    ((i1, i2), rng3)
  }

  // Exercise 6.1
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (value, rng2) = rng.nextInt
    (if (value >= 0) value else -(value + 1), rng2)
  }

  // Exercise 6.2
  def double(rng: RNG): (Double, RNG) = {
    val (value, rng2) = nonNegativeInt(rng)
    (value / (Int.MaxValue.toDouble + 1), rng2)
  }

  // Exercise 6.3
  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (intValue, rng2) = rng.nextInt
    val (doubleValue, rng3) = double(rng2)
    ((intValue, doubleValue), rng3)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val ((intValue, doubleValue), rng2) = intDouble(rng)
    ((doubleValue, intValue), rng2)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, rng2) = double(rng)
    val (d2, rng3) = double(rng2)
    val (d3, rng4) = double(rng3)
    ((d1, d2, d3), rng4)
  }

  // Exercise 6.4
  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    if (count <= 0) (Nil, rng) else {
      val (head, rng2) = rng.nextInt
      val (tail, rng3) = ints(count - 1)(rng2)
      (head :: tail, rng3)
    }
  }

  def ints2(count: Int)(rng: RNG): (List[Int], RNG) = {
    def go(count: Int, r: RNG, xs: List[Int]): (List[Int], RNG) = {
      if (count <= 0) (xs, r) else {
        val (x, rng2) = rng.nextInt
        go(count - 1, rng2, x :: xs)
      }
    }
    go(count, rng, Nil)
  }

  val int: Rand[Int] = State(_.nextInt)

//  def unit[A](a: A): Rand[A] = State(rng => (a, rng))

//  def map[A, B](s: Rand[A])(f: A => B): Rand[B] = State { rng =>
//    val (a, rng2) = s.run(rng)
//    (f(a), rng2)
//  }

  def nonNegativeEven: Rand[Int] = State(nonNegativeInt).map(i => i - i % 2)

  // Exercise 6.5
  def doubleViaMap: Rand[Double] = State(nonNegativeInt).map(_ / (Int.MaxValue.toDouble + 1))

  // Exercise 6.6
//  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = State { rng =>
//    val (a, rng2) = ra.run(rng)
//    val (b, rng3) = rb.run(rng2)
//    (f(a, b), rng3)
//  }

  def both[A, B](ra:Rand[A], rb: Rand[B]): Rand[(A, B)] = ra.map2(rb)((_, _))

  val randIntDouble: Rand[(Int, Double)] = both(int, doubleViaMap)

  val randDoubleInt: Rand[(Double, Int)] = both(doubleViaMap, int)

  // Exercise 6.7
  def sequenceWithRecursive[A](fs: List[Rand[A]]): Rand[List[A]] = {
    fs match {
      case h :: t => State(rng => {
        val (h1, rng2) = h.run(rng)
        val (t1, rng3) = sequenceWithRecursive(t).run(rng2)
        (h1 :: t1, rng3)
      })
      case Nil => State(rng => (List(), rng))
    }
  }

//  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] =
//    fs.foldRight(unit[RNG, List[A]](List()))((f, acc) => f.map2(acc)(_ :: _))

  def intsViaSequence(count: Int): Rand[List[Int]] = sequence(List.fill(count)(int))

  def nonNegativeLessThanV1(n: Int): Rand[Int] = State(nonNegativeInt).map(_ % n)

  def nonNegativeLessThanV2(n: Int): Rand[Int] = State { rng =>
    val (i, rng2) = nonNegativeInt(rng)
    val mod = i % n
    if (i + (n - 1) - mod >= 0) (mod, rng2)
    else nonNegativeLessThanV2(n).run(rng2)
    // 书上写错了，这里应该用rng2而不是rng
  }

  // Exercise 6.8
//  def flatMap[A, B](s: Rand[A])(f: A => Rand[B]): Rand[B] = State { rng =>
//    val (a, rng2) = s.run(rng)
//    f(a).run(rng2)
//  }

  def nonNegativeLessThan(n: Int): Rand[Int] = State(nonNegativeInt) flatMap { i =>
    val mod = i % n
    if (i + (n - 1) - mod >= 0) unit(mod)
    else nonNegativeLessThan(n)
  }

  // Exercise 6.9
  def mapViaFlatMap[A, B](s: Rand[A])(f: A => B): Rand[B] = s.flatMap(a => unit(f(a)))

  def map2ViaFlatMap[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    ra.flatMap(a => rb.map(b => f(a, b)))

  def rollDie: Rand[Int] = nonNegativeLessThan(6).map(_ + 1)
}
