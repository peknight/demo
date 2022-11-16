package com.peknight.demo.frontend.apache.echarts.chart.line

import com.peknight.demo.frontend.apache.echarts.util.{AreaStyleOption, ZRColor}

import scala.scalajs.js

trait LineBlurMixin extends js.Object:
  val lineStyle: js.UndefOr[LineLineStyleOption] = js.undefined
  val areaStyle: js.UndefOr[AreaStyleOption[ZRColor]] = js.undefined
