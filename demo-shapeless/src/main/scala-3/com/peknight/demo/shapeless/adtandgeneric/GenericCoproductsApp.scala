package com.peknight.demo.shapeless.adtandgeneric

import com.peknight.demo.shapeless.adtandgeneric.Shape.{Circle, Rectangle}

object GenericCoproductsApp extends App:
  type Light = Red | Amber | Green
  val red: Light = Red()
  val amber: Light = Amber()
  val green: Light = Green()
  // 有了或类型是不是不需要Coproduct了？
