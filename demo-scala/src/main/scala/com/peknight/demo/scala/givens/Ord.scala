package com.peknight.demo.scala.givens

import com.peknight.demo.scala.functionalobjects.Rational

/**
 * Use `Ordering` instead.
 */
trait Ord[T]:
  def compare(x: T, y: T): Int
  def lt(x: T, y: T): Boolean = compare(x, y) < 0
  def lteq(x: T, y: T): Boolean = compare(x, y) <= 0
  def gt(x: T, y: T): Boolean = compare(x, y) > 0
  def gteq(x: T, y: T): Boolean = compare(x, y) >= 0

  extension (lhs: T)
    def < (rhs: T): Boolean = lt(lhs, rhs)
    def <= (rhs: T): Boolean = lteq(lhs, rhs)
    def > (rhs: T): Boolean = gt(lhs, rhs)
    def >= (rhs: T): Boolean = gteq(lhs, rhs)

object Ord:
  given intOrd: Ord[Int] with
    def compare(x: Int, y: Int) = if x == y then 0 else if x > y then 1 else -1

  // 匿名上下文参数
  given Ord[String] with
    def compare(x: String, y: String): Int = x.compareTo(y)

  given rationalOrd: Ord[Rational] with
    def compare(x: Rational, y: Rational) =
      val a = x.numer * y.denom
      val b = x.denom * y.numer
      if a == b then 0 else if a > b then 1 else -1
