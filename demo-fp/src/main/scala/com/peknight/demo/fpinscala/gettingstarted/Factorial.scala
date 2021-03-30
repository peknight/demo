package com.peknight.demo.fpinscala.gettingstarted

import scala.annotation.tailrec

object Factorial {
  def factorial(n: Int): Int = {
    @tailrec
    def go(n: Int, acc: Int): Int =
      if (n <= 0) acc
      else go(n - 1, n * acc)

    go(n, 1)
  }
}
