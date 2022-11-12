package com.peknight.demo.frontend.apache.echarts.chart.bar

import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js
import scala.scalajs.js.UndefOr

trait BarSeriesOptionInner extends js.Object with BaseBarSeriesOption[BarStateOption[CallbackDataParams], BarStatesMixin]
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
  val backgroundStyle: js.UndefOr[ItemStyleOption[js.Any] & BorderRadiusMixin] = js.undefined
  val realtimeSort: js.UndefOr[Boolean] = js.undefined
  override val seriesLayoutBy: UndefOr[SeriesLayoutBy] = js.undefined
