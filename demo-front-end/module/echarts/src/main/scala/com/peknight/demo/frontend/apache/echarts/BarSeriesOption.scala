package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

// TODO
trait BarSeriesOption extends js.Object with BaseBarSeriesOption[BarStateOption[CallbackDataParams], BarStatesMixin]
  with BarStateOption[CallbackDataParams] with SeriesStackOptionMixin with SeriesSamplingOptionMixin
  with SeriesEncodeOptionMixin:
  type Type = "bar"
  val coordinateSystem: js.UndefOr["cartesian2d" | "polar"] = js.undefined
  val clip: js.UndefOr[Boolean] = js.undefined
  /**
   * If use caps on two sides of bars
   * Only available on tangential polar bar
   */
  val roundCap: js.UndefOr[Boolean] = js.undefined
  val showBackground: js.UndefOr[Boolean] = js.undefined
  val backgroundStyle: js.UndefOr[ItemStyleOption[js.Any] & BorderRadiusMixin] = js.undefined
  val data: js.UndefOr[js.Array[BarDataItemOption | OptionDataValue | js.Array[OptionDataValue]]] = js.undefined
  val realtimeSort: js.UndefOr[Boolean] = js.undefined
  