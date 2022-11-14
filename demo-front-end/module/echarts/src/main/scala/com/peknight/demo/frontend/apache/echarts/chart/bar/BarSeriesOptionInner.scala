package com.peknight.demo.frontend.apache.echarts.chart.bar

import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait BarSeriesOptionInner extends js.Object
  with BaseBarSeriesOption[BarStateOption[CallbackDataParams], DefaultStatesMixinEmphasis, js.Any, js.Any]
  with BarStateOption[CallbackDataParams] with SeriesStackOptionMixin with SeriesSamplingOptionMixin
  with SeriesEncodeOptionMixin:
  type Type = "bar"
  type CoordinateSystemType = "cartesian2d" | "polar"
  type DataType = js.Array[BarDataItemOption | OptionDataValue | js.Array[OptionDataValue]]

  val clip: js.UndefOr[Boolean] = js.undefined
  /**
   * If use caps on two sides of bars
   * Only available on tangential polar bar
   */
  val roundCap: js.UndefOr[Boolean] = js.undefined
  val showBackground: js.UndefOr[Boolean] = js.undefined
  val backgroundStyle: js.UndefOr[ItemStyleOption[CallbackDataParams] & BorderRadiusMixin] = js.undefined
  val realtimeSort: js.UndefOr[Boolean] = js.undefined
  override val seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = js.undefined
