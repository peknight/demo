package com.peknight.demo.frontend.apache.echarts.chart.rader

import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, DefaultStatesMixinEmphasis, OptionDataItemObject, StatesOptionMixin, SymbolOptionMixin}

import scala.scalajs.js

trait RadarSeriesDataItemOption extends js.Object with SymbolOptionMixin[CallbackDataParams]
  with RadarSeriesStateOption[CallbackDataParams]
  with StatesOptionMixin[RadarSeriesStateOption[CallbackDataParams], DefaultStatesMixinEmphasis, js.Any, js.Any]
  with OptionDataItemObject[RadarSeriesDataValue]
