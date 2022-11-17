package com.peknight.demo.frontend.apache.echarts.chart.bar

import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait BarEmphasisOption extends js.Object with BarStateOption[CallbackDataParams] with DefaultStatesMixinEmphasis
  with StatesEmphasisOptionMixin

object BarEmphasisOption:
  def apply(itemStyle: js.UndefOr[BarItemStyleOption[CallbackDataParams]] = js.undefined,
            label: js.UndefOr[BarSeriesLabelOption] = js.undefined,
            focus: js.UndefOr[DefaultEmphasisFocus] = js.undefined,
            blurScope: js.UndefOr[BlurScope] = js.undefined,
            disabled: js.UndefOr[Boolean] = js.undefined): BarEmphasisOption =
    val _itemStyle: js.UndefOr[BarItemStyleOption[CallbackDataParams]] = itemStyle
    val _label: js.UndefOr[BarSeriesLabelOption] = label
    val _focus: js.UndefOr[DefaultEmphasisFocus] = focus
    val _blurScope: js.UndefOr[BlurScope] = blurScope
    val _disabled: js.UndefOr[Boolean] = disabled
    new BarEmphasisOption:
      override val itemStyle: js.UndefOr[BarItemStyleOption[CallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[BarSeriesLabelOption] = _label
      override val focus: js.UndefOr[DefaultEmphasisFocus] = _focus
      override val blurScope: js.UndefOr[BlurScope] = _blurScope
      override val disabled: js.UndefOr[Boolean] = _disabled
