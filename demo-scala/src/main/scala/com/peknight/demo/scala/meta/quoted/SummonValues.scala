package com.peknight.demo.scala.meta.quoted

import scala.collection.immutable.{HashSet, TreeSet}
import scala.quoted.*

object SummonValues:
  inline def setOf[T](using ord: Ordering[T]): Set[T] =
    ${ setOfCode[T]('ord) }

  def setOfCode[T : Type](ord: Expr[Ordering[T]])(using Quotes): Expr[Set[T]] =
    '{ TreeSet.empty[T](using $ord) }

  def setOfCodeSummonVersion[T : Type](using Quotes): Expr[Set[T]] =
    Expr.summon[Ordering[T]] match
      case Some(ord) => '{ TreeSet.empty[T](using $ord) }
      case _ => '{ HashSet.empty[T] }