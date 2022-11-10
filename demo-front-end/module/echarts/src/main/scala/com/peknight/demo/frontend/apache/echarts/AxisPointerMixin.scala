package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait AxisPointerMixin extends js.Object:
  val axis: js.UndefOr["auto" | "x" | "y" | "angle" | "radius"] = js.undefined
  val crossStyle: js.UndefOr[LineStyleOption & TextStyleMixin] = js.undefined
