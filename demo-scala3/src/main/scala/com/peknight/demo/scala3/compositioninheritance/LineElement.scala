package com.peknight.demo.scala3.compositioninheritance

class LineElement(s: String) extends Element:
  val contents = Vector(s)
  override def width: Int = s.length
  override def height: Int = 1
