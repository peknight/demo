package com.peknight.demo.js.common.random

import scala.annotation.tailrec
import scala.collection.BuildFrom
import scala.collection.mutable.ArrayBuffer

trait Random:

  def next(bits: Int): (Random, Int)

  def nextInt: (Random, Int) = next(32)

  def nextIntBounded(bound: Int): (Random, Int) =
    require(bound > 0, s"bound must be positive: $bound")
    val (nextRandom, r) = next(31)
    val m = bound - 1
    if (bound & m) == 0 then (nextRandom, ((bound * r.toLong) >> 31).toInt)
    else
      @tailrec def go(rand: Random, bits: Int, rnd: Int): (Random, Int, Int) =
        if bits - rnd + m < 0 then
          val (nextRand, nextBits) = rand.next(31)
          go(nextRand, nextBits, nextBits % bound)
        else (rand, bits, rnd)
      val (lastRandom, bits, rnd) = go(nextRandom, r, r % bound)
      (lastRandom, rnd)
  end nextIntBounded

  def between(minInclusive: Int, maxExclusive: Int): (Random, Int) =
    require(minInclusive < maxExclusive, s"Invalid bounds: $minInclusive, $maxExclusive")
    val difference = maxExclusive - minInclusive
    if difference >= 0 then
      val (nextRandom, rnd) = nextIntBounded(difference)
      (nextRandom, rnd + minInclusive)
    else
      @tailrec def go(random: Random): (Random, Int) =
        val (nextRandom, n) = random.nextInt
        if n >= minInclusive && n < maxExclusive then (nextRandom, n)
        else go(nextRandom)
      go(this)
  end between

  def nextBytes(n: Int): (Random, Seq[Byte]) =
    require(n >= 0, s"size must be non-negative: $n")
    @tailrec def go(random: Random, bytes: Vector[Byte]): (Random, Vector[Byte]) =
      val length = n - bytes.size
      if length <= 0 then (random, bytes)
      else
        val (nextRandom, rnd) = random.nextInt
        val size = length min 4
        go(nextRandom, bytes ++ Vector.tabulate(size)(i => (rnd >> (8 * i)).toByte))
    go(this, Vector.empty[Byte])
  end nextBytes

  def nextLong: (Random, Long) =
    val (nextRandom, high) = nextInt
    val (lastRandom, low) = nextRandom.nextInt
    (lastRandom, (high.toLong << 32) + low)

  def nextLongBounded(n: Long): (Random, Long) =
    require(n > 0, s"n must be positive: $n")
    @tailrec def go(rand: Random, offset: Long, bound: Long): (Random, Long, Long) =
      if bound < Int.MaxValue then (rand, offset, bound)
      else
        val (nextRand, bits) = rand.nextIntBounded(2)
        val halfBound = bound >>> 1
        val nextBound = if (bits & 2) == 0 then halfBound else bound - halfBound
        val nextOffset = if (bits & 1) == 0 then offset + bound - nextBound else offset
        go(nextRand, nextOffset, nextBound)
    end go
    val (nextRandom, offset, bound) = go(this, 0L, n)
    val (lastRandom, rnd) = nextRandom.nextIntBounded(bound.toInt)
    (lastRandom, rnd + offset)
  end nextLongBounded

  def between(minInclusive: Long, maxExclusive: Long): (Random, Long) =
    require(minInclusive < maxExclusive, s"Invalid bounds: $minInclusive, $maxExclusive")
    val difference = maxExclusive - minInclusive
    if difference >= 0 then
      val (nextRandom, rnd) = nextLongBounded(difference)
      (nextRandom, rnd + minInclusive)
    else
      @tailrec def go(random: Random): (Random, Long) =
        val (nextRandom, n) = random.nextLong
        if n >= minInclusive && n < maxExclusive then (nextRandom, n)
        else go(nextRandom)
      go(this)
  end between

  def nextBoolean: (Random, Boolean) =
    val (nextRandom, rnd) = next(1)
    (nextRandom, rnd != 0)

  def nextFloat: (Random, Float) =
    val (nextRandom, rnd) = next(24)
    (nextRandom, rnd / (1 << 24).toFloat)

  //noinspection DuplicatedCode
  def between(minInclusive: Float, maxExclusive: Float): (Random, Float) =
    require(minInclusive < maxExclusive, s"Invalid bounds: $minInclusive, $maxExclusive")
    val (nextRandom, rnd) = nextFloat
    val next = rnd * (maxExclusive - minInclusive) + minInclusive
    if next < maxExclusive then (nextRandom, next)
    else (nextRandom, Math.nextAfter(maxExclusive, Float.NegativeInfinity))
  end between

  def nextDouble: (Random, Double) =
    val (nextRandom, high) = next(26)
    val (lastRandom, low) = nextRandom.next(27)
    (lastRandom, ((high.toLong << 27) + low) * Random.doubleUnit)

  //noinspection DuplicatedCode
  def between(minInclusive: Double, maxExclusive: Double): (Random, Double) =
    require(minInclusive < maxExclusive, s"Invalid bounds: $minInclusive, $maxExclusive")
    val (nextRandom, rnd) = nextDouble
    val next = rnd * (maxExclusive - minInclusive) + minInclusive
    if next < maxExclusive then (nextRandom, next)
    else (nextRandom, Math.nextAfter(maxExclusive, Double.NegativeInfinity))
  end between

  def nextString(length: Int): (Random, String) =
    if length <= 0 then (this, "")
    else
      def safeChar(random: Random): (Random, Char) =
        val surrogateStart: Int = 0xD800
        val (nextRandom, rnd) = random.nextIntBounded(surrogateStart - 1)
        (nextRandom, (rnd + 1).toChar)
      end safeChar
      val arr = new Array[Char](length)
      val lastRandom = Vector.tabulate[Int](length)(identity).foldLeft(this) { (random, index) =>
        val (nextRandom, ch) = safeChar(random)
        arr(index) = ch
        nextRandom
      }
      (lastRandom, new String(arr))
  end nextString

  def nextPrintableChar: (Random, Char) =
    val low = 33
    val high = 127
    val (nextRandom, rnd) = nextIntBounded(high - low)
    (nextRandom, (rnd + low).toChar)
  end nextPrintableChar

  def nextAlphaNumeric: (Random, Char) =
    val (nextRandom, rnd) = nextIntBounded(Random.chars.length)
    (nextRandom, Random.chars.charAt(rnd))

  def shuffle[T, C](xs: IterableOnce[T])(using bf: BuildFrom[xs.type, T, C]): (Random, C) =
    val buf = new ArrayBuffer[T] ++= xs
    def swap(i1: Int, i2: Int): Unit = {
      val tmp = buf(i1)
      buf(i1) = buf(i2)
      buf(i2) = tmp
    }
    val lastRandom = (buf.length to 2 by -1).foldLeft(this) { (rand, n) =>
      val (nextRand, k) = rand.nextIntBounded(n)
      swap(n - 1, k)
      nextRand
    }
    (lastRandom, (bf.newBuilder(xs) ++= buf).result())
  end shuffle

  def alphanumeric: LazyList[Char] = LazyList.unfold(this)(rand => Some(rand.nextAlphaNumeric.swap))

end Random

object Random:

  private[this] val multiplier: Long = 0x5DEECE66DL
  private[this] val addend: Long = 0xBL
  private[this] val mask: Long = (1L << 48) - 1
  private[this] val doubleUnit: Double = 1.0 / (1L << 53)
  private[this] val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

  private[this] case class LinearCongruentialRandom(seed: Long) extends Random:
    def next(bits: Int): (Random, Int) =
      val nextSeed = (seed * multiplier + addend) & mask
      val nextRandom = LinearCongruentialRandom(nextSeed)
      val n = (nextSeed >>> (48 - bits)).toInt
      (nextRandom, n)

  private[this] def initialScramble(seed: Long): Long = (seed ^ multiplier) & mask

  def apply(seed: Long): Random = LinearCongruentialRandom(initialScramble(seed))

  case class RandomSupplier(seedUniquifier: Long = 8682522807148012L):
    def nanoTime(nanoTime: Long): (RandomSupplier, Random) =
      val next = seedUniquifier * 1181783497276652981L
      (RandomSupplier(next), LinearCongruentialRandom(initialScramble(next ^ nanoTime)))
  end RandomSupplier

end Random
