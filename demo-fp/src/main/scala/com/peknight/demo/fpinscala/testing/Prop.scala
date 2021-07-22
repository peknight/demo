package com.peknight.demo.fpinscala.testing

import com.peknight.demo.fpinscala.state.RNG
import com.peknight.demo.fpinscala.testing.Prop.TestCases
import com.peknight.demo.fpinscala.testing.Result.{Falsified, Passed}

case class Prop(run: (TestCases, RNG) => Result) {
  // Exercise 8.9
  // 这里两个prod的执行使用的是同一个rng对象，所以对于两个prop来说它们生成的值都是一模一样的
  def &&(p: Prop): Prop = Prop { (testCases, rng) =>
    run(testCases, rng) match {
      case Passed => p.run(testCases, rng)
      case x => x
    }
  }

  def ||(p: Prop): Prop = Prop { (testCases, rng) =>
    run(testCases, rng) match {
      case Falsified(msg, _) => p.tag(msg).run(testCases, rng)
      case x => x
    }
  }

  /*
   * This is rather simplistic - in the event of failure, we simply prepend
   * the given message on a newline in front of the existing message.
   */
  def tag(msg: String) = Prop { (testCases, rng) =>
    run(testCases, rng) match {
      case Falsified(e, c) => Falsified(msg + "\n" + e, c)
      case x => x
    }
  }
}
object Prop {
  // Type aliases like this can help the readability of an API
  type TestCases = Int

  def forAll[A](a: Gen[A])(f: A => Boolean): Prop = Prop { (testCases, rng) =>
    // A stream of pairs (a, i) where a is a random value and i is its index in the stream.
    randomStream(a)(rng).zip(LazyList.from(0)).take(testCases).map {
      case (a, i) => try {
        // When a test fails, record the falled cse and its index so we know how many tests succeeded before it
        if (f(a)) Passed else Falsified(a.toString, i)
        // If a test case generates an exception, record it in the result
      } catch { case e: Exception => Falsified(buildMsg(a, e), i) }
    }.find(_.isFalsified).getOrElse(Passed)
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
}
