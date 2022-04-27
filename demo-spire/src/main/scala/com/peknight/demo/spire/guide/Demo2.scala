package com.peknight.demo.spire.guide

import spire.algebra.*
import spire.std.any.*
import spire.syntax.ring.*

object Demo2 extends App:
  def double[A](x: A)(using ev: Ring[A]): A = ev.plus(x, x)
  def triple[A](x: A)(using ev: Ring[A]): A = ev.times(x, ev.fromInt(3))
  println((double(3), triple(4)))
