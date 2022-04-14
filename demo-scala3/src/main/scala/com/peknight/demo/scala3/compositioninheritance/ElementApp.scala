package com.peknight.demo.scala3.compositioninheritance

object ElementApp extends App:
  val e1: Element = VectorElement(Vector("hello", "world"))
  val ve: Element = LineElement("hello")
  val e2: Element = ve
  val e3: Element = UniformElement('x', 2, 3)
