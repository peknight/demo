package com.peknight.demo.frontend.apache.echarts.chart.effectscatter

import com.peknight.demo.frontend.apache.echarts.chart.helper.{DefaultEmphasisFocusScaleMixin, RippleEffectOption}
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait EffectScatterSeriesOptionInner extends js.Object
  with SeriesOption[EffectScatterStateOption[CallbackDataParams], DefaultEmphasisFocusScaleMixin, js.Any, js.Any]
  with EffectScatterStateOption[CallbackDataParams] with SeriesOnCartesianOptionMixin with SeriesOnPolarOptionMixin
  with SeriesOnCalendarOptionMixin with SeriesOnGeoOptionMixin with SeriesOnSingleOptionMixin
  with SymbolOptionMixin[CallbackDataParams] with SeriesEncodeOptionMixin:
  type Type = "effectScatter"
  type CoordinateSystemType = String
  type DataType = js.Array[EffectScatterDataItemOption | ScatterDataValue]
  val effectType: js.UndefOr["ripple"] = js.undefined
  /**
   * When to show the effect
   */
  val showEffectOn: js.UndefOr["render" | "emphasis"] = js.undefined
  val clip: js.UndefOr[Boolean] = js.undefined
  /**
   * Ripple effect config
   */
  val rippleEffect: js.UndefOr[RippleEffectOption] = js.undefined
  override val seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = js.undefined
