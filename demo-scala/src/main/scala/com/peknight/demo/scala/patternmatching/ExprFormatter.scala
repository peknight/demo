package com.peknight.demo.scala.patternmatching

import com.peknight.demo.scala.compositioninheritance.Element
import com.peknight.demo.scala.compositioninheritance.Element.elem
import com.peknight.demo.scala.patternmatching.Expr.{BinOp, Num, UnOp, Var}

class ExprFormatter:
  // 包含成组的按优先级递进的操作符
  private val opGroups = Vector(
    Set("|", "||"),
    Set("&", "&&"),
    Set("^"),
    Set("==", "!="),
    Set("<", "<=", ">", ">="),
    Set("+", "-"),
    Set("*", "%")
  )

  // 从操作符到其优先级的映射
  private val precedence = {
    val assocs =
      for
        i <- 0 until opGroups.length
        op <- opGroups(i)
      yield op -> i
    assocs.toMap
  }

  private val unaryPrecedence = opGroups.length
  private val fractionPrecedence = -1

  private def format(e: Expr, enclPrec: Int): Element =
    e match
      case Var(name) => elem(name)
      case Num(number) =>
        def stripDot(s: String) = if s.endsWith(".0") then s.substring(0, s.length - 2) else s
        elem(stripDot(number.toString))
      case UnOp(op, arg) => elem(op) beside format(arg, unaryPrecedence)
      case BinOp("/", left, right) =>
        val top = format(left, fractionPrecedence)
        val bot = format(right, fractionPrecedence)
        val line = elem('-', top.width.max(bot.width), 1)
        val frac = top above line above bot
        if enclPrec != fractionPrecedence then frac
        else elem(" ") beside frac beside elem(" ")
      case BinOp(op, left, right) =>
        val opPrec = precedence(op)
        val l = format(left, opPrec)
        val r = format(right, opPrec + 1)
        val oper = l beside elem(" " + op + " ") beside r
        if enclPrec <= opPrec then oper
        else elem("(") beside oper beside elem(")")
    end match

  def format(e: Expr): Element = format(e, 0)
end ExprFormatter



