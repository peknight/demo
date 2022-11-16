package com.peknight.demo.frontend.apache.echarts.chart.lines

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.chart.helper.LineDrawEffectOption
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait LinesSeriesOptionInner extends js.Object
  with SeriesOption[LinesStateOption[CallbackDataParams], DefaultStatesMixinEmphasis, js.Any, js.Any]
  with LinesStateOption[CallbackDataParams] with SeriesOnCartesianOptionMixin with SeriesOnGeoOptionMixin
  with SeriesOnPolarOptionMixin with SeriesOnCalendarOptionMixin with SeriesLargeOptionMixin:
  type Type = "lines"
  type CoordinateSystemType = String
  type DataType = js.Array[LinesDataItemOption] | js.Array[Number]
  val symbol: js.UndefOr[js.Array[String] | String] = js.undefined
  val symbolSize: js.UndefOr[js.Array[Number] | Number] = js.undefined
  val effect: js.UndefOr[LineDrawEffectOption] = js.undefined
  /**
   * If lines are polyline
   * polyline not support curveness, label, animation
   */
  val polyline: js.UndefOr[Boolean] = js.undefined
  /**
   * If clip the overflow.
   * Available when coordinateSystem is cartesian or polar.
   */
  val clip: js.UndefOr[Boolean] = js.undefined
  val dimensions: js.UndefOr[DimensionDefinitionLoose | js.Array[DimensionDefinitionLoose]] = js.undefined
