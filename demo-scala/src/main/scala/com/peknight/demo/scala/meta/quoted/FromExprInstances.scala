package com.peknight.demo.scala.meta.quoted

import scala.quoted.*

object FromExprInstances:
  given FromExpr[Boolean] with
    def unapply(x: Expr[Boolean])(using Quotes): Option[Boolean] =
      x match
        case '{ true } => Some(true)
        case '{ false } => Some(false)
        case _ => None
  end given

  given FromExpr[StringContext] with
    def unapply(x: Expr[StringContext])(using Quotes): Option[StringContext] =
      x match
        case '{ new StringContext(${Varargs(Exprs(args))}*) } => Some(StringContext(args*))
        case '{ StringContext(${Varargs(Exprs(args))}*) } => Some(StringContext(args*))
        case _ => None
  end given
