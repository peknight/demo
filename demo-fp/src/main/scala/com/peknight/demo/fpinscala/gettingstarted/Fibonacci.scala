package com.peknight.demo.fpinscala.gettingstarted

import scala.annotation.tailrec

/**
 * Exercise 2.1
 */
object Fibonacci:

  def fib(n: Int): Int =
    @tailrec def loop(n: Int, prev: Int, cur: Int): Int = if n == 0 then prev else loop(n - 1, cur, prev + cur)
    loop(n, 0, 1)

  def main(args: Array[String]): Unit =
    println("Expected: 0, 1, 1, 2, 3, 5, 8")
    println("Actual:   %d, %d, %d, %d, %d, %d, %d".format(fib(0), fib(1), fib(2), fib(3), fib(4), fib(5), fib(6)))
