package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.util.OrdinalRawValue

import scala.scalajs.js

trait CategoryAxisBaseOption extends AxisBaseOptionCommon:
  type AxisType = "category"
  type AxisLabelType = AxisLabelOption[AxisType] & IntervalMixin
  type AxisTickType = AxisTickOption & AlignWithLabelMixin & IntervalMixin
  val boundaryGap: js.UndefOr[Boolean] = js.undefined
  val data: js.UndefOr[js.Array[OrdinalRawValue | DataMixin]] = js.undefined
  val deduplication: js.UndefOr[Boolean] = js.undefined
