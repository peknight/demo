package com.peknight.demo.js.dom

class ColoredPoint(override val x: Double, override val y: Double, val color: String)
  extends BasePoint(x, y) with Colored
