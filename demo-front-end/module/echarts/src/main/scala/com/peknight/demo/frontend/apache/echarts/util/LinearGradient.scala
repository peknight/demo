package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.ecomfe.zrender.graphic.{GradientColorStop, LinearGradientObject}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("echarts.graphic.LinearGradient")
class LinearGradient(x: Number,
                     y: Number,
                     x2: Number,
                     y2: Number,
                     colorStops: js.UndefOr[js.Array[GradientColorStop]] = js.undefined,
                     globalCoord: js.UndefOr[Boolean] = js.undefined
                    ) extends LinearGradientObject
