package com.peknight.demo.scala.meta.reflection

import scala.quoted.*

object HowToUseTheApi:
  def f(x: Expr[Int])(using Quotes): Expr[Int] =
    import quotes.reflect.*
    def g(tpe: TypeRepr) =
      tpe match
        case TypeBounds(l, u) => ???
    def h(tpe: TypeRepr) =
      tpe match
        case tpe: TypeBounds =>
          val low = tpe.low
          val hi = tpe.hi
    val tree: Term = x.asTerm
    val expr: Expr[Int] = tree.asExprOf[Int]
    expr

  def g[T : Type](using Quotes) =
    import quotes.reflect.*
    val tpe: TypeRepr = TypeRepr.of[T]
    tpe.asType match
      case '[t] => '{ val x: t = ${???} }
    ???