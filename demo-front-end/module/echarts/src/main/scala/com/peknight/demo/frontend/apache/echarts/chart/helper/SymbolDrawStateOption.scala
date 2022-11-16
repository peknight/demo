package com.peknight.demo.frontend.apache.echarts.chart.helper

import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, ItemStyleOption, LabelOption}

import scala.scalajs.js

trait SymbolDrawStateOption extends js.Object:
  val itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = js.undefined
  val label: js.UndefOr[LabelOption] = js.undefined
