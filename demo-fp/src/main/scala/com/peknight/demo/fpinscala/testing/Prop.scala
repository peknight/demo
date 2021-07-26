package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.parallelism.Par
import com.peknight.demo.fpinscala.parallelism.Par.Par
import com.peknight.demo.fpinscala.state.{RNG, SimpleRNG}
import com.peknight.demo.fpinscala.testing.Gen.{**, weighted}
import com.peknight.demo.fpinscala.testing.Prop.{MaxSize, TestCases}
import com.peknight.demo.fpinscala.testing.Result.{Falsified, Passed, Proved}

import java.util.concurrent.Executors

case class Prop(run: (MaxSize, TestCases, RNG) => Result) {
  // Exercise 8.9
  // 这里两个prod的执行使用的是同一个rng对象，所以对于两个prop来说它们生成的值都是一模一样的
  def &&(p: Prop): Prop = Prop { (max, n, rng) =>
    run(max, n, rng) match {
      case Passed => p.run(max, n, rng)
      case Proved => p.run(max, n, rng)
      case x => x
    }
  }

  def ||(p: Prop): Prop = Prop { (max, n, rng) =>
    run(max, n, rng) match {
      case Falsified(msg, _) => p.tag(msg).run(max, n, rng)
      case x => x
    }
  }

  /*
   * This is rather simplistic - in the event of failure, we simply prepend
   * the given message on a newline in front of the existing message.
   */
  def tag(msg: String) = Prop { (max, n, rng) =>
    run(max, n, rng) match {
      case Falsified(e, c) => Falsified(msg + "\n" + e, c)
      case x => x
    }
  }
}
object Prop {
  // Type aliases like this can help the readability of an API
  // 每个测试用例的list最大长度是多少
  type MaxSize = Int
  // 生成几个测试用例
  type TestCases = Int

  def forAll[A](a: Gen[A])(f: A => Boolean): Prop = Prop { (max, n, rng) =>
    // A stream of pairs (a, i) where a is a random value and i is its index in the stream.
    randomStream(a)(rng).zip(LazyList.from(0)).take(n).map {
      case (a, i) => try {
        // When a test fails, record the falled cse and its index so we know how many tests succeeded before it
        if (f(a)) Passed else Falsified(a.toString, i)
        // If a test case generates an exception, record it in the result
      } catch { case e: Exception => Falsified(buildMsg(a, e), i) }
    }.find(_.isFalsified).getOrElse(Passed)
  }

  def forAll[A](g: SGen[A])(f: A => Boolean): Prop = forAll(g(_))(f)

  // 这个方法真掉头发
  def forAll[A](g: Int => Gen[A])(f: A => Boolean): Prop = Prop { (max, n, rng) =>
    // For each size generate this many random cases.
    val casesPerSize = (n + (max - 1)) / max
    // Make one property per size, but no more than n properties
    val props: LazyList[Prop] = LazyList.from(0).take(n min max).map(i => forAll(g(i))(f))
    val prop: Prop = props.map(p => Prop { (max, _, rng) =>
      p.run(max, casesPerSize, rng)
      // Combine them all into one property.
    }).toList.reduce(_ && _)
    prop.run(max, n, rng)
  }

  def randomStream[A](g: Gen[A])(rng: RNG): LazyList[A] = {
    // Generates an infinite stream of A values by repeatedly sampling a generator.
    LazyList.unfold(rng)(rng => Some(g.sample.run(rng)))
  }

  def buildMsg[A](s: A, e: Exception): String = {
    // String interpolation syntax. A string starting with s" can refer to a Scala value v
    // as $v or ${v} in the string. The Scala compiler will expand this to v.toString.
    s"test case: $s\n" +
      s"generated an exception: ${e.getMessage}\n" +
      s"stack trace:\n ${e.getStackTrace.mkString("\n")}"
  }

  def run(p: Prop, maxSize: Int = 100, testCases: Int = 100, rng: RNG = SimpleRNG(System.currentTimeMillis())): Unit =
    p.run(maxSize, testCases, rng) match {
      case Falsified(msg, n) => println(s"! Falsified after $n passed tests:\n $msg")
      case Passed => println(s"+ OK, passed $testCases tests.")
      case Proved => println(s"+ OK, proved property.")
    }

  // Note that we are non-strict here.
  def check(p: => Boolean): Prop = Prop { (_, _, _) =>
    if (p) Proved else Falsified("()", 0)
  }

  def equal[A](p: Par[A], p2: Par[A]): Par[Boolean] = Par.map2(p, p2)(_ == _)

  val S = weighted(
    // This generator creates a fixed thread pool executor 75% of the time and an unbounded one 25% of the time.
    Gen.choose(1, 4).map(Executors.newFixedThreadPool) -> .75,
    // a -> b is syntactic sugar for (a, b).
    Gen.unit(Executors.newCachedThreadPool) -> .25
  )

  def forAllPar[A](g: Gen[A])(f: A => Par[Boolean]): Prop =
    forAll(S ** g) { case s ** a => f(a)(s).get }


}
