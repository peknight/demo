package com.peknight.demo.shapeless.countingtypes

import scala.Tuple.Size
import scala.compiletime.constValue
import scala.deriving.Mirror

trait SizeOf[A]:
  def value: Int

object SizeOf:
  def instance[A](value0: Int): SizeOf[A] =
    new SizeOf[A]:
      def value: Int = value0
  end instance
  inline given derived[A](using mirror: Mirror.Of[A]): SizeOf[A] =
    instance[A](constValue[Size[mirror.MirroredElemTypes]])
