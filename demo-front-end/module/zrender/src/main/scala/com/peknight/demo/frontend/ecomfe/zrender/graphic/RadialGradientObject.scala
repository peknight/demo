package com.peknight.demo.frontend.ecomfe.zrender.graphic

import scala.scalajs.js

@js.native
trait RadialGradientObject extends GradientObject:
  type Type = "radial"
  val x: Number = js.native
  val y: Number = js.native
  val r: Number = js.native
