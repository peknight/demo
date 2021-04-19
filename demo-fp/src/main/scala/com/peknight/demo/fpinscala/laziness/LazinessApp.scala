package com.peknight.demo.fpinscala.laziness

object LazinessApp extends App {
  val b1 = false && { println("!!"); true }
  println(b1)
  val b2 = true || { println("!!"); false }
  println(b2)

  val input = "input"
  val result = if (input.isEmpty) sys.error("empty input") else input
  println(result)

  def if2[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
    if (cond) onTrue else onFalse

  val a = 22
  if2(a < 22, println("a"), println("b"))
  println(if2(false, sys.error("fail"), 3))

  def maybeTwice(b: Boolean, i: => Int) = if (b) i + i else 0

  val x = maybeTwice(true, { println("hi"); 1 + 41 })
  println(x)

  def maybeTwice2(b: Boolean, i: => Int) = {
    lazy val j = i
    if (b) j + j else 0
  }

  val x2 = maybeTwice2(true, { println("hi"); 1 + 41 })
  println(x2)

  println(Stream(1, 2, 3).take(2).toList)

  println(Stream(1, 2, 3, 4)
    .map(a => {
      println(s"map $a")
      a + 10
    }).filter(a => {
      println(s"filter $a")
      a % 2 == 0
    }).toList)
}
