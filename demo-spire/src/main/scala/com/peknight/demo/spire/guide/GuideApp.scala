package com.peknight.demo.spire.guide

import spire.algebra.*
import spire.implicits.*

object GuideApp extends App:
  def usingSymbols[A: Ring](x: A, y: A): A = x + y
  def usingNames[A](x: A, y: A)(using r: Ring[A]): A = r.plus(x, y)
  def sqrt[A: NRoot](x: A): A = x.sqrt
  println(usingSymbols(r"1/2", r"2/3"))

