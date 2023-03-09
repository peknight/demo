package com.peknight.demo.scala.meta.reflection

import scala.quoted.*

object Positions:
  def macroImpl()(using quotes: Quotes): Expr[Unit] =
    import quotes.reflect.*
    val pos = Position.ofMacroExpansion
    val path = pos.sourceFile.getJPath.toString
    val start = pos.start
    val end = pos.end
    val startLine = pos.startLine
    val endLine = pos.endLine
    val startColumn = pos.startColumn
    val endColumn = pos.endColumn
    val sourceCode = pos.sourceCode
    '{ () }
