package com.peknight.demo.frontend.apache.echarts.chart.rader

import com.peknight.demo.frontend.apache.echarts.util.{AreaStyleOption, ItemStyleOption, LineStyleOption, SeriesLabelOption, ZRColor}

import scala.scalajs.js

trait RadarSeriesStateOption[TCbParams] extends js.Object:
  val lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = js.undefined
  val areaStyle: js.UndefOr[AreaStyleOption[ZRColor]] = js.undefined
  val label: js.UndefOr[SeriesLabelOption] = js.undefined
  val itemStyle: js.UndefOr[ItemStyleOption[TCbParams]] = js.undefined
