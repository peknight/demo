package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.ecomfe.zrender.graphic.{GradientColorStop, RadialGradientObject}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("echarts.graphic.RadialGradient")
class RadialGradient(x: Number,
                     y: Number,
                     r: Number,
                     colorStops: js.UndefOr[js.Array[GradientColorStop]] = js.undefined,
                     globalCoord: js.UndefOr[Boolean] = js.undefined
                    ) extends RadialGradientObject
