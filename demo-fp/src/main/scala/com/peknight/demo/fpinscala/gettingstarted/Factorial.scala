package com.peknight.demo.fpinscala.gettingstarted

import scala.annotation.tailrec

object Factorial:

  def factorial(n: Int): Int =
    @tailrec def go(n: Int, acc: Int): Int = if n <= 0 then acc else go(n - 1, n * acc)
    go(n, 1)

  private def formatFactorial(n: Int) =
    val msg = "The factorial of %d is %d."
    msg.format(n, factorial(n))

  def main(args: Array[String]): Unit = println(formatFactorial(7))
