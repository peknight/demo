package com.peknight.demo.frontend.apache.echarts.chart.effectscatter

import com.peknight.demo.frontend.apache.echarts.util.{ItemStyleOption, SeriesLabelOption}

import scala.scalajs.js

trait EffectScatterStateOption[TCbParams] extends js.Object:
  val itemStyle: js.UndefOr[ItemStyleOption[TCbParams]] = js.undefined
  val label: js.UndefOr[SeriesLabelOption] = js.undefined
