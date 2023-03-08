package com.peknight.demo.scala.meta.macros

import scala.quoted.*

object MacrosAndTypeParameters:

  def loggedCode[T](x: Expr[T])(using Type[T], Quotes): Expr[T] = x

  inline def logged[T](inline x: T): T = ${ loggedCode('x) }