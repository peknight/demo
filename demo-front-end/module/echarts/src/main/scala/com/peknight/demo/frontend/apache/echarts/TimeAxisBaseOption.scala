package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait TimeAxisBaseOption extends NumericAxisBaseOptionCommon:
  type AxisType = "time"
  type AxisLabelType = AxisLabelOption[AxisType]
  type AxisTickType = AxisTickOption
