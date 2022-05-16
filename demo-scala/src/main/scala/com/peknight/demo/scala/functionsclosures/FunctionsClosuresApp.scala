package com.peknight.demo.scala.functionsclosures

object FunctionsClosuresApp extends App:
  var more = 1
  val addMore = (x: Int) => x + more
  println(addMore(10))
  more = 9999
  println(addMore(10))

  val someNumbers = List(-11, -10, -5, 0, 5, 10)
  var sum = 0
  someNumbers.foreach(sum += _)
  println(sum)

  def echo(args: String*) = for arg <- args do println(arg)

  echo()
  echo("one")
  echo("hello", "world!")
  val seq = Seq("What's", "up", "doc?")
  echo(seq*)
