package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object SimpleBlocks:
  inline def test(inline ignore: Boolean, computation: => Unit): Boolean = ${ testCode('ignore, 'computation) }

  def testCode(ignore: Expr[Boolean], computation: Expr[Unit])(using Quotes): Expr[Boolean] =
    if ignore.valueOrAbort then Expr(false)
    else Expr.block(List(computation), Expr(true))
