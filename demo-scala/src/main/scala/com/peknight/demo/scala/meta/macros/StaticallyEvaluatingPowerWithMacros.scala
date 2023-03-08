package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object StaticallyEvaluatingPowerWithMacros:
  object InlineVersion:
    inline def power(x: Double, inline n: Int): Double =
      inline if n == 0 then 1.0
      else inline if n % 2 == 1 then x * power(x, n - 1)
      else power(x * x, n / 2)
  end InlineVersion

  inline def power(inline x: Double, inline n: Int) = ${powerCode('x, 'n)}

  def pow(x: Double, n: Int): Double = if n == 0 then 1 else x * pow(x, n - 1)

  object SimpleExpressions:

    def powerCode(x: Expr[Double], n: Expr[Int])(using Quotes): Expr[Double] =
      val value: Double = pow(x.valueOrAbort, n.valueOrAbort)
      Expr(value)

  end SimpleExpressions

  def powerCode(x: Expr[Double], n: Expr[Int])(using Quotes): Expr[Double] =
    // scala.quoted.quotes.reflect.report
    import quotes.reflect.report
    (x.value, n.value) match
      case (Some(base), Some(exponent)) =>
        val value: Double = pow(base, exponent)
        Expr(value)
      case (Some(_), _) => report.errorAndAbort("Expected a known value for the exponent, but was " + n.show, n)
      case _ => report.errorAndAbort("Expected a known value for the base, but was " + x.show, x)

  object ExprUnapplyExtractor:
    def powerCode(x: Expr[Double], n: Expr[Int])(using Quotes): Expr[Double] =
      // scala.quoted.quotes.reflect.report
      import quotes.reflect.report
      (x, n) match
        case (Expr(base), Expr(exponent)) =>
          val value: Double = pow(base, exponent)
          Expr(value)
        case (Expr(_), _) => report.errorAndAbort("Expected a known value for the exponent, but was " + n.show, n)
        case _ => report.errorAndAbort("Expected a known value for the base, but was " + x.show, x)
  end ExprUnapplyExtractor


end StaticallyEvaluatingPowerWithMacros
