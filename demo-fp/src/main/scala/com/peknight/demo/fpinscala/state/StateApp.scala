package com.peknight.demo.fpinscala.state

import com.peknight.demo.fpinscala.state.RNG.*
import com.peknight.demo.fpinscala.state.State.*

import scala.util.Random

object StateApp extends App:

  val scalaRng = new Random()
  println(scalaRng.nextDouble())
  println(scalaRng.nextDouble())
  println(scalaRng.nextInt())
  println(scalaRng.nextInt(10))

  def rollDieV1: Int =
    val rng = new Random()
    // Returns a random number from 0 to 5
    // off-by-one error supposed to return a value between 1 and 6
    rng.nextInt(6)

  def rollDieV2(rng: Random): Int = rng.nextInt(6) + 1

  val rng = SimpleRNG(42)
  val (n1, rng2) = rng.nextInt
  println(n1)
  println(rng2)
  val (n2, rng3) = rng2.nextInt
  println(n2)
  println(rng3)

  println(randomPair(rng))
  println(ints(3)(rng))
  println(sequence[RNG, Int](List(unit(1), unit(2), unit(3))).run(rng)._1)

  println(rollDie.run(SimpleRNG(5))._1)

  val ns: Rand[List[Int]] =
    for
      x <- int
      y <- int
      xs <- intsViaSequence(x)
    yield xs.map(_ % y)

end StateApp
