package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.util.SeriesLabelOption
import com.peknight.demo.frontend.ecomfe.zrender.core.BuiltinTextPosition

import scala.scalajs.js

trait PieLabelOption extends SeriesLabelOption:
  type PositionType = BuiltinTextPosition | js.Array[Number | String] | "outer" | "inner" | "center" | "outside"
  type RotateType = Number | Boolean | "radial" | "tangential"
  val alignTo: js.UndefOr["none" | "labelLine" | "edge"] = js.undefined
  val edgeDistance: js.UndefOr[String | Number] = js.undefined
  val bleedMargin: js.UndefOr[Number] = js.undefined
  val distanceToLabelLine: js.UndefOr[Number] = js.undefined
