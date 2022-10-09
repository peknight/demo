package com.peknight.demo.shapeless.countingtypes

import scala.Tuple.Size
import scala.compiletime.constValue
import scala.deriving.Mirror

trait SizeOf[A]:
  def value: Int

object SizeOf:
  inline given derived[A](using mirror: Mirror.Of[A]): SizeOf[A] = new SizeOf[A]:
    def value: Int = constValue[Size[mirror.MirroredElemTypes]]
