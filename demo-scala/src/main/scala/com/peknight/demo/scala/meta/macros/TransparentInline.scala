package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object TransparentInline:
  transparent inline def defaultOf(inline str: String) =
    ${ defaultOfImpl('str) }

  def defaultOfImpl(strExpr: Expr[String])(using Quotes): Expr[Any] =
    strExpr.valueOrAbort match
      case "int" => '{ 1 }
      case "string" => '{ "a" }
