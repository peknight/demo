package com.peknight.demo.js.dom

trait Point:
  def x: Double
  def y: Double

object Point:

  private[this] class SimplePoint(val x: Double, val y: Double) extends Point

  def apply(x: Double, y: Double): Point = new SimplePoint(x, y)

  def unapply(p: Point): Option[(Double, Double)] = Some(p.x, p.y)

  private[this] class ColoredPoint(val x: Double, val y: Double, val color: String) extends Point with Colored

  def colored(x: Double, y: Double, color: String): Point & Colored = new ColoredPoint(x, y, color)
