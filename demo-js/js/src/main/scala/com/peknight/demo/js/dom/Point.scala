package com.peknight.demo.js.dom

trait Point:
  def x: Double
  def y: Double
  def length(p: Point): Double =
    val a = x - p.x
    val b = y - p.y
    Math.sqrt(a * a + b * b)

object Point:

  private[this] class SimplePoint(val x: Double, val y: Double) extends Point

  def apply(x: Double, y: Double): Point = new SimplePoint(x, y)

  def unapply(p: Point): Option[(Double, Double)] = Some(p.x, p.y)

  private[this] class ColoredPoint(val x: Double, val y: Double, val color: String) extends Point with Colored

  def colored(x: Double, y: Double, color: String): Point & Colored = new ColoredPoint(x, y, color)

  extension (p: Point)
    def colored(color: String): Point & Colored = new ColoredPoint(p.x, p.y, color)
