package com.peknight.demo.scala.meta.macros

import com.peknight.demo.scala.meta.macros.PatternMatching.{eval, showMe}
import com.peknight.demo.scala.meta.macros.TransparentInline.defaultOf

import scala.quoted.Expr

object MacrosApp extends App:
  // MacrosTreatProgramsAsValues.inspect(sys error "abort")
  MacrosTreatProgramsAsValues.inspect(println("abort"))
  val x = 1
  QuotesAndSplices.assert(x != 0)
  println(QuotesAndSplices.sum_m(Array(1, 2, 3, 4, 5)))
  val a: Int = defaultOf("int")
  val b: String = defaultOf("string")
  println(s"a=$a, b=$b")

  // gets matched by Varargs
  println(PatternMatching.sum(1, 2, 3))
  // doesn't get matched by Varargs
  val xs = List(1, 2, 3)
  println(PatternMatching.sum(xs*))

  println(showMe"${true}")

  println(eval {
    // expands to the code: (16: Int)
    val x: Int = 4
    x * x
  })
