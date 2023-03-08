package com.peknight.demo.scala.meta.quoted

import scala.math.Numeric.Implicits.infixNumericOps
import scala.quoted.*

object QuotedTypeClasses:
  inline def power[Num](x: Num, inline n: Int)(using num: Numeric[Num]) =
    ${ powerMacro('x, 'n)(using 'num) }

  def powerMacro[Num : Type](x: Expr[Num], n: Expr[Int])(using Expr[Numeric[Num]])(using Quotes): Expr[Num] =
    powerCode(x, n.valueOrAbort)

  def powerCode[Num : Type](x: Expr[Num], n: Int)(using num: Expr[Numeric[Num]])(using Quotes): Expr[Num] =
    if n == 0 then '{ $num.one }
    else if n % 2 == 0 then '{
      given Numeric[Num] = $num
      val y = $x * $x
      ${ powerCode('y, n / 2) }
    }
    else '{
      given Numeric[Num] = $num
      $x * ${ powerCode(x, n - 1) }
    }
