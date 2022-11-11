package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait LogAxisBaseOption extends NumericAxisBaseOptionCommon:
  type AxisType = "log"
  type AxisLabelType = AxisLabelOption[AxisType]
  type AxisTickType = AxisTickOption
  val logBase: js.UndefOr[Number] = js.undefined
