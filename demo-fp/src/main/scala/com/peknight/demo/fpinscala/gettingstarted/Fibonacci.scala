package com.peknight.demo.fpinscala.gettingstarted

import scala.annotation.tailrec

/**
 * Exercise 2.1
 */
object Fibonacci {
  def fib(n: Int): Int = {
    @tailrec
    def go(first: Int, second: Int, n: Int): Int = {
      if (n <= 1) {
        first
      } else if (n == 2) {
        second
      } else {
        go(second, first + second, n - 1)
      }
    }
    go(0, 1, n)
  }

  def main(args: Array[String]): Unit = {
    println(fib(1))
    println(fib(2))
    println(fib(3))
    println(fib(4))
    println(fib(5))
    println(fib(6))
    println(fib(7))
  }
}
