package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.state.{RNG, State}

case class Gen[A](sample: State[RNG, A]) {
  // Exercise 8.6
  def flatMap[B](f: A => Gen[B]): Gen[B] = Gen(sample.flatMap(a => f(a).sample))

  def listOfN(size: Int): Gen[List[A]] = Gen.listOfN(size, this)

  def listOfN(size: Gen[Int]): Gen[List[A]] = size.flatMap(listOfN)
}

object Gen {
  def listOf[A](a: Gen[A]): Gen[List[A]] = ???

  def forAll[A](a: Gen[A])(f: A => Boolean): Prop = ???

  // Exercise 8.4
  def choose(start: Int, stopExclusive: Int): Gen[Int] =
    Gen(State(RNG.nonNegativeInt).map(n => start + n % (stopExclusive - start)))

  /*
   * We could write this as an explicit state action,
   * but this is far less convenient,
   * since it requires us to manually thread the `RNG` through the computation.
   */
  def choose2(start: Int, stopExclusive: Int): Gen[Int] =
    Gen(State(rng => RNG.nonNegativeInt(rng) match {
      case (n, rng2) => (start + n % (stopExclusive - start), rng2)
    }))

  // Exercise 8.5
  def unit[A](a: => A): Gen[A] = Gen(State.unit(a))

  def boolean: Gen[Boolean] = Gen(State(RNG.boolean))

  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] = Gen(State.sequence(List.fill(n)(g.sample)))
}
