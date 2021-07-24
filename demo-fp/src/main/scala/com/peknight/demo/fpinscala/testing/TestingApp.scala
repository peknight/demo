package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.parallelism.Par
import com.peknight.demo.fpinscala.testing.Gen.{listOf, listOf1}
import com.peknight.demo.fpinscala.testing.Prop.{forAll, run}

import java.util.concurrent.{ExecutorService, Executors}

object TestingApp extends App {
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

  val sortedProp = forAll(listOf(smallInt)) { ns =>
    val nss = ns.sorted
    // We specify that every sorted list is either empty, has one element,
    // or has no two consecutive elements `(a, b)` such that `a` is greater than `b`.
    (nss.isEmpty || nss.tail.isEmpty || !nss.zip(nss.tail).exists {
      case (a, b) => a > b
    })
  }
  run(sortedProp)

  val ES: ExecutorService = Executors.newCachedThreadPool()
  val p1 = Prop.check(
    Par.map(Par.unit(1))(_ + 1)(ES).get == Par.unit(2)(ES).get
  )
  run(p1)
}
