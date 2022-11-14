package com.peknight.demo.frontend.apache.echarts.chart.line

import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, StatesOptionMixin, SymbolOptionMixin}

import scala.scalajs.js

trait LineDataItemOption extends js.Object with SymbolOptionMixin[CallbackDataParams]
  with LineStateOption[CallbackDataParams]
  with StatesOptionMixin[LineStateOption[CallbackDataParams], LineEmphasisMixin, js.Any, js.Any]:
  val name: js.UndefOr[String] = js.undefined
  val value: js.UndefOr[LineDataValue] = js.undefined
