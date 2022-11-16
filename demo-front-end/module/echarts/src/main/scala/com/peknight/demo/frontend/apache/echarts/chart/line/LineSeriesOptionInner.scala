package com.peknight.demo.frontend.apache.echarts.chart.line

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{LineStyleOption as _, *}

import scala.scalajs.js

trait LineSeriesOptionInner extends js.Object
  with SeriesOption[LineStateOption[CallbackDataParams], LineEmphasisMixin, js.Any, LineBlurMixin]
  with LineStateOption[CallbackDataParams] with SeriesOnCartesianOptionMixin with SeriesOnPolarOptionMixin
  with SeriesStackOptionMixin with SeriesSamplingOptionMixin with SymbolOptionMixin[CallbackDataParams]
  with SeriesEncodeOptionMixin:
  type Type = "line"
  type CoordinateSystemType = "cartesian2d" | "polar"
  type DataType = js.Array[LineDataValue | LineDataItemOption]
  val clip: js.UndefOr[Boolean] = js.undefined
  val lineStyle: js.UndefOr[LineLineStyleOption] = js.undefined
  val areaStyle: js.UndefOr[AreaStyleOption[ZRColor] & OriginMixin] = js.undefined
  val step: js.UndefOr[false | "start" | "end" | "middle"] = js.undefined
  val smooth: js.UndefOr[Boolean | Number] = js.undefined
  val smoothMonotone: js.UndefOr["x" | "y" | "none"] = js.undefined
  val connectNulls: js.UndefOr[Boolean] = js.undefined
  val showSymbol: js.UndefOr[Boolean] = js.undefined
  val showAllSymbol: js.UndefOr["auto" | Boolean] = js.undefined
  val triggerLineEvent: js.UndefOr[Boolean] = js.undefined
  override val seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = js.undefined
