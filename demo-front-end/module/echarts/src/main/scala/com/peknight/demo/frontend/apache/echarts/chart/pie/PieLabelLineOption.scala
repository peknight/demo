package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.util.LabelLineOption

import scala.scalajs.js

trait PieLabelLineOption extends LabelLineOption:
  /**
   * Max angle between labelLine and surface normal.
   * 0 - 180
   */
  val maxSurfaceAngle: js.UndefOr[Number] = js.undefined
