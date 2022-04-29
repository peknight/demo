package com.peknight.demo.js.dom

import spire.algebra.{EuclideanRing, NRoot}

class ColoredPoint[U: EuclideanRing: NRoot](override val x: U, override val y: U, val color: Color)
  extends CartesianCoordinatePoint[U](x, y) with Colored:
  override def toString: String = s"ColoredPoint($x, $y, $color)"
