package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait LogAxisBaseOption extends NumericAxisBaseOptionCommon:
  type AxisType = "log"
  type AxisLabelType = AxisLabelOption[AxisType]
  type AxisTickType = AxisTickOption
  val logBase: js.UndefOr[Number] = js.undefined
