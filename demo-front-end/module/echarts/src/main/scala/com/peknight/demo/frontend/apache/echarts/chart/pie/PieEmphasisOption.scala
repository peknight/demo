package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{BlurScope, DefaultEmphasisFocus, StatesEmphasisOptionMixin}

import scala.scalajs.js

trait PieEmphasisOption extends js.Object with PieStateOption[PieCallbackDataParams] with PieEmphasisMixin
  with StatesEmphasisOptionMixin

object PieEmphasisOption:
  def apply(itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = js.undefined,
            label: js.UndefOr[PieLabelOption] = js.undefined,
            labelLine: js.UndefOr[PieLabelLineOption] = js.undefined,
            focus: js.UndefOr[DefaultEmphasisFocus] = js.undefined,
            scale: js.UndefOr[Boolean] = js.undefined,
            scaleSize: js.UndefOr[Number] = js.undefined,
            blurScope: js.UndefOr[BlurScope] = js.undefined,
            disabled: js.UndefOr[Boolean] = js.undefined): PieEmphasisOption =
    val _itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = itemStyle
    val _label: js.UndefOr[PieLabelOption] = label
    val _labelLine: js.UndefOr[PieLabelLineOption] = labelLine
    val _focus: js.UndefOr[DefaultEmphasisFocus] = focus
    val _scale: js.UndefOr[Boolean] = scale
    val _scaleSize: js.UndefOr[Number] = scaleSize
    val _blurScope: js.UndefOr[BlurScope] = blurScope
    val _disabled: js.UndefOr[Boolean] = disabled
    new PieEmphasisOption:
      override val itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[PieLabelOption] = _label
      override val labelLine: js.UndefOr[PieLabelLineOption] = _labelLine
      override val focus: js.UndefOr[DefaultEmphasisFocus] = _focus
      override val scale: js.UndefOr[Boolean] = _scale
      override val scaleSize: js.UndefOr[Number] = _scaleSize
      override val blurScope: js.UndefOr[BlurScope] = _blurScope
      override val disabled: js.UndefOr[Boolean] = _disabled

