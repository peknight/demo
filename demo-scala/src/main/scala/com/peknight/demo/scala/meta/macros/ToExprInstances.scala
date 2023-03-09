package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object ToExprInstances:
  given ToExpr[Boolean] with
    def apply(b: Boolean)(using Quotes): Expr[Boolean] =
      if b then '{ true } else '{ false }
  end given

  given ToExpr[Int] with
    def apply(n: Int)(using Quotes): Expr[Int] = n match
      case Int.MinValue => '{ Int.MinValue }
      case _ if n < 0 => '{ - ${ apply(-n) } }
      case 0 => '{ 0 }
      case _ if n % 2 == 0 => '{ ${ apply(n / 2) } * 2 }
      case _ => '{ ${ apply(n / 2) } * 2 + 1 }
  end given

  given [T : ToExpr : Type]: ToExpr[List[T]] with
    def apply(xs: List[T])(using Quotes): Expr[List[T]] = xs match
      case head :: tail => '{ ${ Expr(head) } :: ${ apply(tail) } }
      case Nil => '{ Nil: List[T] }
  end given
