package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object SimpleMatching:
  def value(boolExpr: Expr[Boolean])(using Quotes): Option[Boolean] =
    if boolExpr.matches(Expr(true)) then Some(true)
    else if boolExpr.matches(Expr(false)) then Some(false)
    else None
