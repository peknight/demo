package com.peknight.demo.shapeless.adtandgeneric

import com.peknight.demo.shapeless.autoderiving.CsvEncoder

enum Shape derives CsvEncoder:
  case Rectangle(width: Double, height: Double)
  case Circle(radius: Double)

object Shape:
  def area(shape: Shape): Double = shape match
    case Rectangle(w, h) => w * h
    case Circle(r) => math.Pi * r * r
