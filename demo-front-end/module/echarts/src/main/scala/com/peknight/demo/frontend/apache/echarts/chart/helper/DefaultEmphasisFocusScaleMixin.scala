package com.peknight.demo.frontend.apache.echarts.chart.helper

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.DefaultEmphasisFocus

import scala.scalajs.js

trait DefaultEmphasisFocusScaleMixin extends js.Object :
  val focus: js.UndefOr[DefaultEmphasisFocus] = js.undefined
  val scale: js.UndefOr[Boolean | Number] = js.undefined
