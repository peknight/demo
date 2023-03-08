package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object WorkingWithVarargs:
  inline def sumNow(inline nums: Int*): Int = ${ sumCode('nums) }

  def sumCode(nums: Expr[Seq[Int]])(using Quotes): Expr[Int] =
    import quotes.reflect.report
    nums match
      // numberExprs: Seq[Expr[Int]]
      case Varargs(numberExprs) =>
        val numbers: Seq[Int] = numberExprs.map(_.valueOrAbort)
        Expr(numbers.sum)
      case _ => report.errorAndAbort("Expected explicit varargs sequence. Notation `args*` is not supported.", nums)
end WorkingWithVarargs