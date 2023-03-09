package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object QuotesAndSplices:
  inline def assert(inline expr: Boolean): Unit =
    ${ assertImpl('expr) }

  def assertImpl(expr: Expr[Boolean])(using Quotes) =
    val failMsg: Expr[String] = Expr("failed assertion: " + expr.show)
    '{ if !$expr then throw AssertionError($failMsg) }

  def showExpr(expr: Expr[Boolean])(using Quotes): Expr[String] =
    '{ ??? }

  def foreach[T](arr: Expr[Array[T]], f: Expr[T] => Expr[Unit])(using Type[T], Quotes): Expr[Unit] = '{
    var i: Int = 0
    while i < ($arr).length do
      val element: T = ($arr)(i)
      ${ f('element) }
      i += 1
  }

  def sum(arr: Expr[Array[Int]])(using Quotes): Expr[Int] = '{
    var sum = 0
    ${ foreach(arr, x => '{ sum += $x }) }
    sum
  }

  inline def sum_m(arr: Array[Int]): Int = ${ sum('arr) }