package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.util.StatesEmphasisOptionMixin
import com.peknight.demo.frontend.apache.echarts.{Number, clean}

import scala.scalajs.js

trait PieEmphasisOption extends js.Object with PieStateOption[PieCallbackDataParams] with PieEmphasisMixin
  with StatesEmphasisOptionMixin

object PieEmphasisOption:
  def apply(itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = js.undefined): PieEmphasisOption =
    val _itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = itemStyle
    val pieEmphasisOption = new PieEmphasisOption:
      override val itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = _itemStyle
    pieEmphasisOption.clean

