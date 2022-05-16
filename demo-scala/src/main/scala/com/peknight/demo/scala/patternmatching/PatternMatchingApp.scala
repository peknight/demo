package com.peknight.demo.scala.patternmatching

import com.peknight.demo.scala.patternmatching.Expr.*

object PatternMatchingApp extends App:
  val v = Var("x")
  val op = BinOp("+", Num(1), v)
