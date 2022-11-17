package com.peknight.demo.frontend.apache.echarts.chart.bar

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.SeriesLabelOption

import scala.scalajs.js

trait BarSeriesLabelOption extends SeriesLabelOption:
  type PositionType = PolarBarLabelPosition | "outsize"
  type DistanceType = Number
  type RotateType = Number
