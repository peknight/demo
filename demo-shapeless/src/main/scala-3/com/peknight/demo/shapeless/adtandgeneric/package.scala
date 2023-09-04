package com.peknight.demo.shapeless

package object adtandgeneric:
  type Rectangle2 = (Double, Double)
  type Circle2 = Double
  type Shape2 = Rectangle2 | Circle2
  def area2(shape: Shape2): Double = shape match
    case (w: Double, h: Double) => w * h
    case r: Circle2 => math.Pi * r * r

