package com.peknight.demo.frontend.apache.echarts.chart.helper

import com.peknight.demo.frontend.apache.echarts.util.{LineLabelOption, LineStyleOption, ZRColor}

import scala.scalajs.js

trait LineDrawStateOption extends js.Object:
  val lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = js.undefined
  val label: js.UndefOr[LineLabelOption] = js.undefined
