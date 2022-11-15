package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.util.{LabelLineOption, LineStyleOption, ZRColor}
import com.peknight.demo.frontend.apache.echarts.{Number, clean}

import scala.scalajs.js

trait PieLabelLineOption extends LabelLineOption:
  /**
   * Max angle between labelLine and surface normal.
   * 0 - 180
   */
  val maxSurfaceAngle: js.UndefOr[Number] = js.undefined

object PieLabelLineOption:
  def apply(show: js.UndefOr[Boolean] = js.undefined,
            showAbove: js.UndefOr[Boolean] = js.undefined,
            length: js.UndefOr[Number] = js.undefined,
            length2: js.UndefOr[Number] = js.undefined,
            smooth: js.UndefOr[Boolean | Number] = js.undefined,
            minTurnAngle: js.UndefOr[Number] = js.undefined,
            lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = js.undefined,
            maxSurfaceAngle: js.UndefOr[Number] = js.undefined): PieLabelLineOption =
    val _show: js.UndefOr[Boolean] = show
    val _showAbove: js.UndefOr[Boolean] = showAbove
    val _length: js.UndefOr[Number] = length
    val _length2: js.UndefOr[Number] = length2
    val _smooth: js.UndefOr[Boolean | Number] = smooth
    val _minTurnAngle: js.UndefOr[Number] = minTurnAngle
    val _lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = lineStyle
    val _maxSurfaceAngle: js.UndefOr[Number] = maxSurfaceAngle
    val pieLabelLineOption: PieLabelLineOption = new PieLabelLineOption:
      override val show: js.UndefOr[Boolean] = _show
      override val showAbove: js.UndefOr[Boolean] = _showAbove
      override val length: js.UndefOr[Number] = _length
      override val length2: js.UndefOr[Number] = _length2
      override val smooth: js.UndefOr[Boolean | Number] = _smooth
      override val minTurnAngle: js.UndefOr[Number] = _minTurnAngle
      override val lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = _lineStyle
      override val maxSurfaceAngle: js.UndefOr[Number] = _maxSurfaceAngle
    pieLabelLineOption.clean
