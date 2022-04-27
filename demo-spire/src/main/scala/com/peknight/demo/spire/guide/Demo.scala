package com.peknight.demo.spire.guide

import spire.algebra.*
import spire.std.any.*
import spire.syntax.ring.*

object Demo extends App:
  def double[A: Ring](x: A): A = x + x
  def triple[A: Ring](x: A): A = x * 3
  println((double(3), triple(4)))

