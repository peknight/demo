package com.peknight.demo.scala.patternmatching

import com.peknight.demo.scala.patternmatching.Expr.{BinOp, Num, Var}

object Express extends App:

  val f = new ExprFormatter

  val expr = BinOp("+",
    BinOp("*",
      BinOp("+", Var("x"), Var("y")), Var("z")
    ),
    Num(1)
  )

  val e1 = BinOp("*", BinOp("/", Num(1), Num(2)), BinOp("+", Var("x"), Num(1)))
  val e2 = BinOp("+", BinOp("/", Var("x"), Num(2)), BinOp("/", Num(1.5), Var("x")))
  val e3 = BinOp("/", e1, e2)

  def show(e: Expr) = println(s"${f.format(e)}\n\n")

  for e <- Vector(expr, e1, e2, e3) do show(e)

