package com.peknight.demo.frontend.apache.echarts.chart.effectscatter

import com.peknight.demo.frontend.apache.echarts.chart.helper.DefaultEmphasisFocusScaleMixin
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{Number, clean}

import scala.scalajs.js

trait EffectScatterEmphasisOption extends js.Object with EffectScatterStateOption[CallbackDataParams]
  with DefaultEmphasisFocusScaleMixin with StatesEmphasisOptionMixin

object EffectScatterEmphasisOption:
  def apply(itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = js.undefined,
            label: js.UndefOr[SeriesLabelOption] = js.undefined,
            focus: js.UndefOr[DefaultEmphasisFocus] = js.undefined,
            scale: js.UndefOr[Boolean | Number] = js.undefined,
            blurScope: js.UndefOr[BlurScope] = js.undefined,
            disabled: js.UndefOr[Boolean] = js.undefined): EffectScatterEmphasisOption =
    val _itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = itemStyle
    val _label: js.UndefOr[SeriesLabelOption] = label
    val _focus: js.UndefOr[DefaultEmphasisFocus] = focus
    val _scale: js.UndefOr[Boolean | Number] = scale
    val _blurScope: js.UndefOr[BlurScope] = blurScope
    val _disabled: js.UndefOr[Boolean] = disabled
    val effectScatterEmphasisOption: EffectScatterEmphasisOption = new EffectScatterEmphasisOption:
      override val itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[SeriesLabelOption] = _label
      override val focus: js.UndefOr[DefaultEmphasisFocus] = _focus
      override val scale: js.UndefOr[Boolean | Number] = _scale
      override val blurScope: js.UndefOr[BlurScope] = _blurScope
      override val disabled: js.UndefOr[Boolean] = _disabled
    effectScatterEmphasisOption.clean
