package com.peknight.demo.fpinscala.gettingstarted

object FormatResult:

  def formatResult(name: String, n: Int, f: Int => Int) =
    val msg = "The %s of %d is %d."
    msg.format(name, n, f(n))

  def main(args: Array[String]): Unit =
    println(formatResult("absolute value", -42, MyModule.abs))
    println(formatResult("factorial", 7, Factorial.factorial))
