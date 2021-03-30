package com.peknight.demo.fpinscala.gettingstarted

import scala.annotation.tailrec

/** A documentation comment */
object MyModule {
  def abs(n: Int): Int =
    if (n < 0) -n
    else n

  private def formatAbs(x: Int) = {
    val msg = "The absolute value of %d is %d."
    msg.format(x, abs(x))
  }

  def findFirstString(ss: Array[String], key: String): Int = {
    @tailrec
    def loop(n: Int): Int = {
      if (n >= ss.length) -1
      else if (ss(n) == key) n
      else loop(n + 1)
    }
    loop(0)
  }

  def findFirst[A](as: Array[A], p: A => Boolean): Int = {
    @tailrec
    def loop(n: Int): Int = {
      if (n >= as.length) -1
      else if (p(as(n))) n
      else loop(n + 1)
    }
    loop(0)
  }

  // Exercise 2.2
  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean = {
    @tailrec
    def go(n: Int): Boolean = {
      if (n >= as.length - 1) true
      else if (gt(as(n), as(n + 1))) false
      else go(n + 1)
    }
    go(0)
  }

  val lessThan = (a: Int, b: Int) => a < b

  def partial[A, B, C](a: A, f: (A, B) => C): B => C = f(a, _)

  // Exercise 2.3
  // => associates to the right: A => (B => C) can be written as A => B => C
  def curry[A, B, C](f: (A, B) => C): A => (B => C) = a => b => f(a, b)

  // Exercise 2.4
  def uncurry[A, B, C](f: A => B => C): (A, B) => C = (a, b) =>f(a)(b)

  // Exercise 2.5
  def compose[A, B, C](f: B => C, g: A => B): A => C = a => f(g(a))

  def main(args: Array[String]): Unit = {
    println(formatAbs(-42))
    println(findFirst[Int](Array(7, 9, 13), _ == 9))
    println(lessThan(10, 20))

    val f = (x: Double) => math.Pi / 2 - x
    val sin = math.sin _
    val cos = f andThen sin
    println(cos(math.Pi / 2))
    val cosV2 = sin compose f
    println(cosV2(math.Pi / 2))
  }
}
