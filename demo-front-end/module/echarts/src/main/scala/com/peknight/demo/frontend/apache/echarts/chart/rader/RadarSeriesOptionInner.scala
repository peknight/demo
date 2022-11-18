package com.peknight.demo.frontend.apache.echarts.chart.rader

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, DefaultStatesMixinEmphasis, SeriesEncodeOptionMixin, SeriesLayoutBy, SeriesOption, SymbolOptionMixin}

import scala.scalajs.js
import scala.scalajs.js.UndefOr

trait RadarSeriesOptionInner extends js.Object
  with SeriesOption[RadarSeriesStateOption[CallbackDataParams], DefaultStatesMixinEmphasis, js.Any, js.Any]
  with RadarSeriesStateOption[CallbackDataParams] with SymbolOptionMixin[CallbackDataParams]
  with SeriesEncodeOptionMixin:
  type Type = "radar"
  type CoordinateSystemType = "radar"
  type DataType = js.Array[RadarSeriesDataItemOption | RadarSeriesDataValue]
  val radarIndex: js.UndefOr[Number] = js.undefined
  val radarId: js.UndefOr[String] = js.undefined
  override val seriesLayoutBy: UndefOr[SeriesLayoutBy] = js.undefined
