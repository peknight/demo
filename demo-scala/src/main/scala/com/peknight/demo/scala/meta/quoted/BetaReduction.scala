package com.peknight.demo.scala.meta.quoted

import scala.quoted.*

object BetaReduction:
  def betaReduceDouble(y: Expr[Int])(using Quotes): Expr[Int] =
    // returns '{ val x = $y; x + x }
    Expr.betaReduce('{ ((x: Int) => x + x)($y) })
