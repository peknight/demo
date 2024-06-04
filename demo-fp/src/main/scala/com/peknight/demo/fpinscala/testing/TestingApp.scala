package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.parallelism.Par
import com.peknight.demo.fpinscala.parallelism.Par.Par
import com.peknight.demo.fpinscala.state.{RNG, SimpleRNG, State}
import com.peknight.demo.fpinscala.testing.Exhaustive.Prop.checkPar
import com.peknight.demo.fpinscala.testing.Gen.{listOf, listOf1}
import com.peknight.demo.fpinscala.testing.Prop.{equal, forAll, forAllPar, run}

import java.util.concurrent.{ExecutorService, Executors}

object TestingApp extends App:

  {
    import org.scalacheck.Gen
    import org.scalacheck.Prop.forAll

    val intList = Gen.listOf(Gen.choose(0, 100))
    val prop = forAll(intList)(ns => ns.reverse.reverse == ns) &&
      forAll(intList)(ns => ns.headOption == ns.reverse.lastOption)
    val failingProp = forAll(intList)(ns => ns.reverse == ns)
    prop.check()
    failingProp.check()
  }

  val smallInt = Gen.choose(-10, 10)

  // 当列表为空时会失败
  val maxProp = forAll(listOf(smallInt)) { ns =>
    val max = ns.max
    !ns.exists(_ > max)
  }

  val maxProp1 = forAll(listOf1(smallInt)) { ns =>
    val max = ns.max
    !ns.exists(_ > max) // No value greater than `max` should exist in `ns`
  }
  run(maxProp1)

  //noinspection DuplicatedCode
  val sortedProp = forAll(listOf(smallInt)) { ns =>
    val nss = ns.sorted
    // We specify that every sorted list is either empty, has one element,
    // or has no two consecutive elements `(a, b)` such that `a` is greater than `b`.
    nss.isEmpty || nss.tail.isEmpty || !nss.zip(nss.tail).exists { case (a, b) => a > b }
  }

  run(sortedProp)

  val ES: ExecutorService = Executors.newCachedThreadPool()

  val p1 = Prop.check(
    Par.map(Par.unit(1))(_ + 1)(ES).get == Par.unit(2)(ES).get
  )
  run(p1)

  val p2 = Prop.check {
    //noinspection DuplicatedCode
    val p = Par.map(Par.unit(1))(_ + 1)
    val p2 = Par.unit(2)
    p(ES).get == p2(ES).get
  }

  val p3 = Prop.check {
    equal(
      Par.map(Par.unit(1))(_ + 1),
      Par.unit(2)
    )(ES).get
  }

  val p4 = checkPar {
    equal (
      Par.map(Par.unit(1))(_ + 1),
      Par.unit(2)
    )
  }

  val pint = Gen.choose(0, 10).map(Par.unit)
  val p5 = forAllPar(pint)(n => equal(Par.map(n)(y => y), n))

  //noinspection DuplicatedCode
  /*
   * Exercise 8.16
   * A `Gen[Par[Int]]` generated from a list summation that spawns a new parallel
   * computation fro each element of the input list summed to produce the final result.
   * This is not the most compelling example,
   * but it provides at least some variation in structure to use for testing.
   */
  val pint2: Gen[Par[Int]] = Gen.choose(-100, 100)
    .listOfN(Gen.choose(0, 20))
    .map(l => l.foldLeft(Par.unit(0))((p, i) =>
      Par.fork { Par.map2(p, Par.unit(i))(_ + _) }
    ))

  // Exercise 8.17
  val forkProp = Prop.forAllPar(pint2)(i => equal(Par.fork(i), i)).tag("fork")

  val isEven = (i: Int) => i % 2 == 0
  val takeWhileProp = Prop.forAll(Gen.listOf(smallInt))(ns => ns.takeWhile(isEven).forall(isEven))

  def genStringIntFn(g: Gen[Int]): Gen[String => Int] = g.map(i => s => i)

  def genStringFn[A](g: Gen[A]): Gen[String => A] = Gen {
    State { (rng: RNG) =>
      val (seed, rng2) = rng.nextInt // we still use `rng` to produce a seed, so we get a new function each time
      val f = (s: String) => g.sample.run(SimpleRNG(seed.toLong ^ s.hashCode.toLong))._1
      (f, rng2)
    }
  }

end TestingApp
