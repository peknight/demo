package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.state.{RNG, State}

case class Gen[+A](sample: State[RNG, A]):
  def map[B](f: A => B): Gen[B] = Gen(sample.map(f))

  def map2[B, C](g: Gen[B])(f: (A, B) => C): Gen[C] = Gen(sample.map2(g.sample)(f))

  // Exercise 8.6
  def flatMap[B](f: A => Gen[B]): Gen[B] = Gen(sample.flatMap(a => f(a).sample))

  def listOfN(size: Int): Gen[List[A]] = Gen.listOfN(size, this)

  def listOfN(size: Gen[Int]): Gen[List[A]] = size.flatMap(listOfN)

  // Exercise 8.10
  def unsized: SGen[A] = SGen(_ => this)

  def **[B](g: Gen[B]): Gen[(A, B)] = (this map2 g)((_, _))

end Gen

object Gen:

  // Exercise 8.4
  def choose(start: Int, stopExclusive: Int): Gen[Int] =
    Gen(State(RNG.nonNegativeInt).map(n => start + n % (stopExclusive - start)))

  /*
   * We could write this as an explicit state action,
   * but this is far less convenient,
   * since it requires us to manually thread the `RNG` through the computation.
   */
  def choose2(start: Int, stopExclusive: Int): Gen[Int] =
    Gen(State(rng => RNG.nonNegativeInt(rng) match
      case (n, rng2) => (start + n % (stopExclusive - start), rng2)
    ))

  // Exercise 8.5
  def unit[A](a: => A): Gen[A] = Gen(State.unit(a))

  def boolean: Gen[Boolean] = Gen(State(RNG.boolean))

  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] = Gen(State.sequence(List.fill(n)(g.sample)))

  // Exercise 8.7
  def union[A](g1: Gen[A], g2: Gen[A]): Gen[A] = boolean.flatMap(b => if b then g1 else g2)

  // Exercise 8.8
  def weighted[A](g1: (Gen[A], Double), g2: (Gen[A], Double)): Gen[A] =
    /* The probability we should pull from `g1`. */
    val g1Threshold = g1._2.abs / (g1._2.abs + g2._2.abs)
    Gen(State(RNG.double).flatMap(d => if d < g1Threshold then g1._1.sample else g2._1.sample))

  // Exercise 8.12
  def listOf[A](g: Gen[A]): SGen[List[A]] = SGen(n => g.listOfN(n))

  // Exercise 8.13
  def listOf1[A](g: Gen[A]): SGen[List[A]] = SGen(n => g.listOfN(n max 1))

  /*
   * Not the most efficent implementation, but it's simple.
   * This generates ASCII strings.
   */
  def stringN(n: Int): Gen[String] = listOfN(n, choose(0, 127)).map(_.map(_.toChar).mkString)

  val string: SGen[String] = SGen(stringN)

  object ** {
    def unapply[A, B](p: (A, B)) = Some(p)
  }

end Gen
