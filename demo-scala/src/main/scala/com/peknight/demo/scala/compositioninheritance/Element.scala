package com.peknight.demo.scala.compositioninheritance

import com.peknight.demo.scala.compositioninheritance.Element.elem

abstract class Element:
  def contents: Vector[String]
  def height: Int = contents.length
  def width: Int = if height == 0 then 0 else contents(0).length
  def above(that: Element): Element = elem(this.widen(that.width).contents ++ that.widen(this.width).contents)
  def beside(that: Element): Element = elem(
    this.heighten(that.height).contents.zip(that.heighten(this.height).contents).map(_ + _)
  )

  def widen(w: Int): Element =
    if w <= width then this
    else {
      val left = elem(' ', (w - width) / 2, height)
      val right = elem(' ', w - width - left.width, height)
      left beside this beside right
    } ensuring (w <= _.width)

  def heighten(h: Int): Element =
    if h <= height then this
    else
      val top = elem(' ', width, (h - height) / 2)
      val bot = elem(' ', width, h - height - top.height)
      top above this above bot

  override def toString: String = contents.mkString("\n")
end Element

object Element:
  private class VectorElement(val contents: Vector[String]) extends Element

  private class LineElement(s: String) extends Element:
    val contents = Vector(s)
    override def width: Int = s.length
    override def height: Int = 1

  private class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element:
    private val line = ch.toString * width
    def contents = Vector.fill(height)(line)

  def elem(contents: Vector[String]): Element = VectorElement(contents)
  def elem(chr: Char, width: Int, height: Int): Element = UniformElement(chr, width, height)
  def elem(line: String): Element = LineElement(line)
end Element
