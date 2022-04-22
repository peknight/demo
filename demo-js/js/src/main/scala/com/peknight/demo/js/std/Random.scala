package com.peknight.demo.js.std

import cats.FlatMap
import cats.data.State
import cats.effect.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*

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
    if difference >= 0 then nextIntBounded(difference).map(_ + minInclusive)
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

  def nextLong: (Random, Long) = Random.nextLongF(this)

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
    nextRandom.nextIntBounded(bound.toInt).map(_ + offset)
  end nextLongBounded

  def between(minInclusive: Long, maxExclusive: Long): (Random, Long) =
    require(minInclusive < maxExclusive, s"Invalid bounds: $minInclusive, $maxExclusive")
    val difference = maxExclusive - minInclusive
    if difference >= 0 then nextLongBounded(difference).map(_ + minInclusive)
    else
      @tailrec def go(random: Random): (Random, Long) =
        val (nextRandom, n) = random.nextLong
        if n >= minInclusive && n < maxExclusive then (nextRandom, n)
        else go(nextRandom)
      go(this)
  end between

  def nextBoolean: (Random, Boolean) = next(1).map(_ != 0)

  def nextFloat: (Random, Float) = next(24).map(_ / (1 << 24).toFloat)

  def between(minInclusive: Float, maxExclusive: Float): (Random, Float) =
    require(minInclusive < maxExclusive, s"Invalid bounds: $minInclusive, $maxExclusive")
    nextFloat.map { (rnd: Float) =>
      val next = rnd * (maxExclusive - minInclusive) + minInclusive
      if next < maxExclusive then next
      else Math.nextAfter(maxExclusive, Float.NegativeInfinity)
    }
  end between

  def nextDouble: (Random, Double) = Random.nextDoubleF(this)

  def between(minInclusive: Double, maxExclusive: Double): (Random, Double) =
    require(minInclusive < maxExclusive, s"Invalid bounds: $minInclusive, $maxExclusive")
    nextDouble.map { (rnd: Double) =>
      val next = rnd * (maxExclusive - minInclusive) + minInclusive
      if next < maxExclusive then next
      else Math.nextAfter(maxExclusive, Double.NegativeInfinity)
    }
  end between

  def nextString(length: Int): (Random, String) =
    if length <= 0 then (this, "")
    else
      def safeChar(random: Random): (Random, Char) =
        val surrogateStart: Int = 0xD800
        random.nextIntBounded(surrogateStart - 1).map((rnd: Int) => (rnd + 1).toChar)
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
    nextIntBounded(high - low).map((rnd: Int) => (rnd + low).toChar)
  end nextPrintableChar

  def nextAlphaNumeric: (Random, Char) =
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    nextIntBounded(chars.length).map(chars.charAt)

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

  def nonNegativeInt: (Random, Int) = next(31)

end Random

object Random:

  private[this] val multiplier: Long = 0x5DEECE66DL
  private[this] val addend: Long = 0xBL
  private[this] val mask: Long = (1L << 48) - 1
  private[this] val doubleUnit: Double = 1.0 / (1L << 53)

  private[this] case class LinearCongruentialRandom(seed: Long) extends Random:
    def next(bits: Int): (Random, Int) =
      val nextSeed = (seed * multiplier + addend) & mask
      val nextRandom = LinearCongruentialRandom(nextSeed)
      val n = (nextSeed >>> (48 - bits)).toInt
      (nextRandom, n)

  def apply(seed: Long): Random = LinearCongruentialRandom(seed)

  def seedUniquifierRef[F[_]: Concurrent]: F[Ref[F, Long]] = Ref.of[F, Long](8682522807148012L)

  private[this] def initialScramble(seed: Long): Long = (seed ^ multiplier) & mask

  private[this] def seedUniquifier[F[_]](seedUniquifierR: Ref[F, Long]): F[Long] =
    seedUniquifierR.modify { uniquifier =>
      val next = uniquifier * 1181783497276652981L
      (next, next)
    }

  def javaRandom[F[_]: Clock: FlatMap](seedUniquifierR: Ref[F, Long]): F[Random] =
    for
      seedUniquifier <- seedUniquifier(seedUniquifierR)
      monotonic <- Clock[F].monotonic
    yield apply(initialScramble(seedUniquifier ^ monotonic.toNanos))

  def javaRandom(seed: Long): Random = apply(initialScramble(seed))

  extension [A](tuple: (Random, A))
    def map[B](f: A => B): (Random, B) =
      val (random, a) = tuple
      (random, f(a))
    end map
  end extension

  type RandFunction[A] = Random => (Random, A)

  extension [A](run: RandFunction[A])
    def flatMap[B](f: A => RandFunction[B]): RandFunction[B] = random =>
      val (nextRandom, a) = run(random)
      f(a)(nextRandom)
    end flatMap
    def map[B](f: A => B): RandFunction[B] = random =>
      val (nextRandom, a) = run(random)
      (nextRandom, f(a))
    end map
  end extension

  private[this] def nextF(bits: Int): RandFunction[Int] = _.next(bits)
  private[this] val nextIntF: RandFunction[Int] = _.nextInt

  private val nextLongF: RandFunction[Long] =
    for
      high <- nextIntF
      low <- nextIntF
    yield (high.toLong << 32) + low

  private val nextDoubleF: RandFunction[Double] =
    for
      high <- nextF(26)
      low <- nextF(27)
    yield ((high.toLong << 27) + low) * doubleUnit

  type Rand[A] = State[Random, A]

  def next(bits: Int): Rand[Int] = State(_.next(bits))

  def nextInt: Rand[Int] = State(_.nextInt)

  def nextIntBounded(bound: Int): Rand[Int] = State(_.nextIntBounded(bound))

  def between(minInclusive: Int, maxExclusive: Int): Rand[Int] = State(_.between(minInclusive, maxExclusive))

  def nextBytes(n: Int): Rand[Seq[Byte]] = State(_.nextBytes(n))

  def nextLong: Rand[Long] = State(_.nextLong)

  def nextLongBounded(n: Long): Rand[Long] = State(_.nextLongBounded(n))

  def between(minInclusive: Long, maxExclusive: Long): Rand[Long] = State(_.between(minInclusive, maxExclusive))

  def nextBoolean: Rand[Boolean] = State(_.nextBoolean)

  def nextFloat: Rand[Float] = State(_.nextFloat)

  def between(minInclusive: Float, maxExclusive: Float): Rand[Float] = State(_.between(minInclusive, maxExclusive))

  def nextDouble: Rand[Double] = State(_.nextDouble)

  def between(minInclusive: Double, maxExclusive: Double): Rand[Double] = State(_.between(minInclusive, maxExclusive))

  def nextString(length: Int): Rand[String] = State(_.nextString(length))

  def nextPrintableChar: Rand[Char] = State(_.nextPrintableChar)

  def nextAlphaNumeric: Rand[Char] = State(_.nextAlphaNumeric)

  def shuffle[T, C](xs: IterableOnce[T])(using bf: BuildFrom[xs.type, T, C]): Rand[C] = State(_.shuffle(xs))

  def setSeed(seed: Long): Rand[Unit] = State(random => (apply(seed), ()))

  def nonNegativeInt: Rand[Int] = State(_.nonNegativeInt)

end Random
