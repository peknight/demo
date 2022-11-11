package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait NumericAxisBaseOptionCommon extends AxisBaseOptionCommon:
  val boundaryGap: js.UndefOr[(Number | String, Number | String)] = js.undefined
  /**
   * AxisTick and axisLabel and splitLine are caculated based on splitNumber.
   */
  val splitNumber: js.UndefOr[Number] = js.undefined
  /**
   * Interval specifies the span of the ticks is mandatorily.
   */
  val interval: js.UndefOr[Number] = js.undefined
  /**
   * Specify min interval when auto calculate tick interval.
   */
  val minInterval: js.UndefOr[Number] = js.undefined
  /**
   * Specify max interval when auto calculate tick interval.
   */
  val maxInterval: js.UndefOr[Number] = js.undefined
  /**
   * If align ticks to the first axis that is not use alignTicks
   * If all axes has alignTicks: true. The first one will be applied.
   *
   * Will be ignored if interval is set.
   */
  val alignTicks: js.UndefOr[Boolean] = js.undefined
