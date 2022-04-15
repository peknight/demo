package com.peknight.demo.scala3.patternmatching

import com.peknight.demo.scala3.patternmatching.Expr.*

object PatternMatchingApp extends App:
  val v = Var("x")
  val op = BinOp("+", Num(1), v)
