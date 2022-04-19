package com.peknight.demo.scala3.patternmatching

sealed trait Expr derives CanEqual

object Expr:
  case class Var(name: String) extends Expr
  case class Num(number: Double) extends Expr
  case class UnOp(operator: String, arg: Expr) extends Expr
  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

  def simplifyTop(expr: Expr): Expr = expr match
    case UnOp("-", UnOp("-", e)) => e
    case BinOp("+", e, Num(0)) => e
    case BinOp("*", e, Num(1)) => e
    case _ => expr

  def simplifyAdd(expr: Expr): Expr = expr match
    case BinOp("+", x, y) if x == y => BinOp("*", x, Num(2))
    case _ => expr

  def simplifyAll(expr: Expr): Expr = expr match
    case UnOp("-", UnOp("-", e)) => simplifyAll(e)
    case BinOp("+", e, Num(0)) => simplifyAll(e)
    case BinOp("*", e, Num(1)) => simplifyAll(e)
    case UnOp(op, e) => UnOp(op, simplifyAll(e))
    case BinOp(op, l, r) => BinOp(op, simplifyAll(l), simplifyAll(r))
    case _ => expr