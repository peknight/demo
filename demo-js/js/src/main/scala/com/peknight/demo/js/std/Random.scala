package com.peknight.demo.js.std

import cats.data.State
import cats.effect.Ref

trait Random:

  def nextInt: (Random, Int)

end Random

object Random:

  private[this] case class LinearCongruentialRandom(seed: Long) extends Random:
    def nextInt: (Random, Int) =
      val nextSeed = (seed * 0x5DEECE66DL + 0XBL) & 0xFFFFFFFFFFFFL
      val nextRandom = LinearCongruentialRandom(nextSeed)
      val n = (nextSeed >>> 16).toInt
      (nextRandom, n)

  def apply(seed: Long): Random = LinearCongruentialRandom(seed)

  type Rand[A] = State[Random, A]

  def nextInt: Rand[Int] = State(_.nextInt)

  def nonNegativeInt: Rand[Int] = State { random =>
    val (nextRandom, n) = random.nextInt
    (nextRandom, if n >= 0 then n else -(n + 1))
  }

  def generator[F[_]](seedUniquifier: Ref[F, Long]): F[Random] =
    seedUniquifier.modify {
      
    }

  val random = new java.util.Random()
  random.nextInt()


end Random
