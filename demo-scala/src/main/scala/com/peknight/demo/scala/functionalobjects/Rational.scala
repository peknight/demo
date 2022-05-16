package com.peknight.demo.scala.functionalobjects

import scala.annotation.tailrec

class Rational(n: Int, d: Int) extends Ordered[Rational] {
  require(d != 0)

  private val g = gcd(n.abs, d.abs)

  val numer: Int = n / g
  val denom: Int = d / g

  def this(n: Int) = this(n, 1)

  override def toString: String = s"$numer/$denom"

  def + (that: Rational): Rational = Rational(numer * that.denom + that.numer * denom, denom * that.denom)

  def + (i: Int): Rational = Rational(numer + i * denom, denom)

  def - (that: Rational): Rational = Rational(numer * that.denom - that.numer * denom, denom * that.denom)

  def - (i: Int): Rational = Rational(numer - i * denom, denom)

  def * (that: Rational): Rational = Rational(numer * that.numer, denom * that.denom)

  def * (i: Int): Rational = Rational(numer * i, denom)

  def / (that: Rational): Rational = Rational(numer * that.denom, denom * that.numer)

  def / (i: Int): Rational = Rational(numer, denom * i)

  def lessThan(that: Rational) = this.numer * that.denom < that.numer * this.denom

  def max(that: Rational) = if this.lessThan(that) then that else this

  def compare(that: Rational): Int = (this.numer * that.denom) - (that.numer * this.denom)

  @tailrec
  private def gcd(a: Int, b: Int): Int = if b == 0 then a else gcd(b, a % b)
}

object Rational {
  extension (x: Int)
    def + (y: Rational) = Rational(x) + y
    def - (y: Rational) = Rational(x) - y
    def * (y: Rational) = Rational(x) * y
    def / (y: Rational) = Rational(x) / y

  def apply(numer: Int, denom: Int = 1) = new Rational(numer, denom)
}
