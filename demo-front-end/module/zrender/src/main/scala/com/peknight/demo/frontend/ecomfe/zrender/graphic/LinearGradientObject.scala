package com.peknight.demo.frontend.ecomfe.zrender.graphic

import com.peknight.demo.frontend.ecomfe.zrender.Number

import scala.scalajs.js

@js.native
trait LinearGradientObject extends GradientObject:
  type Type = "linear"
  val x: Number = js.native
  val y: Number = js.native
  val x2: Number = js.native
  val y2: Number = js.native
