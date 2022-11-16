package com.peknight.demo.frontend.apache.echarts.chart.lines

import com.peknight.demo.frontend.apache.echarts.util.{SeriesLineLabelOption, ZRColor}

import scala.scalajs.js

trait LinesStateOption[TCbParams] extends js.Object:
  val lineStyle: js.UndefOr[LinesLineStyleOption[js.Function1[TCbParams, ZRColor] | ZRColor]] = js.undefined
  val label: js.UndefOr[SeriesLineLabelOption] = js.undefined
