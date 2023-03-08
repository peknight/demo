package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object MacrosTreatProgramsAsValues:

  def inspectCode(x: Expr[Any])(using Quotes): Expr[Any] =
    println(x.show)
    x

  inline def inspect(inline x: Any): Any = ${ inspectCode('x) }

