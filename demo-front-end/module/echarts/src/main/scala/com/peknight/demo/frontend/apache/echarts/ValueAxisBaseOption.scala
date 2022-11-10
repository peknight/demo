package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait ValueAxisBaseOption extends NumericAxisBaseOptionCommon:
  type AxisType = "value"
  type AxisLabelType = AxisLabelOption[AxisType]
  type AxisTickType = AxisTickOption
  /**
   * Optional value can be:
   * + `false`: always include value 0.
   * + `false`: always include value 0.
   */
  val scale: js.UndefOr[Boolean] = js.undefined
