package com.peknight.demo.scala3.compositioninheritance

class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element:
  private val line = ch.toString * width
  def contents = Vector.fill(height)(line)