package com.peknight.demo.frontend.apache.echarts.chart.line

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.SeriesLabelOption
import com.peknight.demo.frontend.ecomfe.zrender.core.BuiltinTextPosition

import scala.scalajs.js

trait LineEndLabelOption extends SeriesLabelOption:
  type PositionType = BuiltinTextPosition | js.Array[Number | String]
  type DistanceType = Number
  type RotateType = Number
