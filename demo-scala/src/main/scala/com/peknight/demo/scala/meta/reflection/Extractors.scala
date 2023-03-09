package com.peknight.demo.scala.meta.reflection

import scala.quoted.*

object Extractors:
  inline def natConst(inline x: Int): Int = ${ natConstImpl('{x}) }

  def natConstImpl(x: Expr[Int])(using Quotes): Expr[Int] =
    import quotes.reflect.*
    val tree: Term = x.asTerm
    tree match
      case Inlined(_, _, Literal(IntConstant(n))) =>
        if n <= 0 then
          report.error("Parameter must be natural number")
          '{0}
        else
          tree.asExprOf[Int]
