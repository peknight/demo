package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.DefaultEmphasisFocus

import scala.scalajs.js

trait PieEmphasisMixin extends js.Object:
  val focus: js.UndefOr[DefaultEmphasisFocus] = js.undefined
  val scale: js.UndefOr[Boolean] = js.undefined
  val scaleSize: js.UndefOr[Number] = js.undefined
