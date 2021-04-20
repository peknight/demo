package com.peknight.demo.fpinscala.state

trait RNG {
  def nextInt: (Int, RNG)
}
object RNG {

  type Rand[+A] = RNG => (A, RNG)

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

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] = rng => {
    val (a, rng2) = s(rng)
    (f(a), rng2)
  }

  def nonNegativeEven: Rand[Int] = map(nonNegativeInt)(i => i - i % 2)

  // Exercise 6.5
  def doubleViaMap: Rand[Double] = map(nonNegativeInt)(_ / (Int.MaxValue.toDouble + 1))

  // Exercise 6.6
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = rng => {
    val (a, rng2) = ra(rng)
    val (b, rng3) = rb(rng2)
    (f(a, b), rng3)
  }

  // TODO 提前实现
  def flatMap[A, B](s: Rand[A])(f: A => Rand[B]): Rand[B] = rng => {
    val (a, rng2) = s(rng)
    f(a)(rng2)
  }
}
