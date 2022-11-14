package com.peknight.demo.frontend.apache.echarts.chart.line

import com.peknight.demo.frontend.apache.echarts.util.{AreaStyleOption, DefaultEmphasisFocus, ZRColor}

import scala.scalajs.js

trait LineEmphasisMixin extends js.Object:
  val focus: js.UndefOr[DefaultEmphasisFocus] = js.undefined
  val scale: js.UndefOr[Boolean | Number] = js.undefined

  val lineStyle: js.UndefOr[LineStyleOption] = js.undefined
  val areaStyle: js.UndefOr[AreaStyleOption[ZRColor]] = js.undefined

