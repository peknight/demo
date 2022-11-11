package com.peknight.demo.frontend.apache.echarts.chart.bar

import com.peknight.demo.frontend.apache.echarts.util.{SeriesOnCartesianOptionMixin, SeriesOnPolarOptionMixin, SeriesOption, StatesMixinBase}
import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait BaseBarSeriesOption[StateOption, ExtraStateOption <: StatesMixinBase]
  extends SeriesOption[StateOption, ExtraStateOption] with SeriesOnCartesianOptionMixin with SeriesOnPolarOptionMixin:
  /**
   * Min height of bar
   */
  val barMinHeight: js.UndefOr[Number] = js.undefined
  /**
   * Min angle of bar. Avaiable on polar coordinate system
   */
  val barMinAngle: js.UndefOr[Number] = js.undefined
  /**
   * Max width of bar. Default to be 1 on cartesian coordinate system. Otherwise it's null
   */
  val barMaxWidth: js.UndefOr[Number] = js.undefined
  val barMinWidth: js.UndefOr[Number] = js.undefined
  /**
   * Bar width. Will be calculated automatically.
   * Can be pixel width or percent String.
   */
  val barWidth: js.UndefOr[Number | String] = js.undefined
  /**
   * Gap between each bar inside category. Default to be 30%. Can be an aboslute pixel value
   */
  val barGap: js.UndefOr[String | Number] = js.undefined
  /**
   * Gap between each category. Default to be 20%. can be an absolute pixel value.
   */
  val barCategoryGap: js.UndefOr[String | Number] = js.undefined
  val large: js.UndefOr[Boolean] = js.undefined
  val largeThreshold: js.UndefOr[Number] = js.undefined
