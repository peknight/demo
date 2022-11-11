package com.peknight.demo.frontend.apache.echarts.coord

trait TimeAxisBaseOption extends NumericAxisBaseOptionCommon:
  type AxisType = "time"
  type AxisLabelType = AxisLabelOption[AxisType]
  type AxisTickType = AxisTickOption
