package com.peknight.demo.scala.meta.quoted

import scala.quoted.*

object Generics:
  def evalAndUse[T](x: Expr[T])(using Type[T], Quotes) = '{
    val x2: T = $x
    ???
  }

