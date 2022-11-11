package com.peknight.demo.frontend.apache.echarts.coord

import scala.scalajs.js

trait AxisLabelOption[TType <: OptionAxisType] extends AxisLabelBaseOption:
  val formatter: js.UndefOr[LabelFormatters[TType]] = js.undefined
