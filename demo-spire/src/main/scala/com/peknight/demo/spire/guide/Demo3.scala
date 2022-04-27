package com.peknight.demo.spire.guide

import spire.algebra.*
import spire.std.any.*
import spire.syntax.ring.*

import scala.specialized as sp

/**
 * To achieve speed on-par with direct (non-generic) code, you will need to use specialization.
 * The good news is that most of Spire's code is already specialized (and tested for proper performance).
 * The bad news is that you'll have to annotate all your generic code like so
 */
object Demo3 extends App:
  def double[@sp A: Ring](x: A): A = x + x
  def triple[@sp A: Ring](x: A): A = x * 3
  println((double(3), triple(4)))

