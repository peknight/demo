package com.peknight.demo.frontend.apache.echarts.chart.effectscatter

import com.peknight.demo.frontend.apache.echarts.chart.helper.{DefaultEmphasisFocusScaleMixin, RippleEffectOption}
import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, ItemStyleOption, SeriesLabelOption, StatesEmphasisOptionMixin, StatesOptionMixin, StatesSelectOptionMixin, SymbolCallback, SymbolOffsetCallback, SymbolOptionMixin, SymbolRotateCallback, SymbolSizeCallback}
import com.peknight.demo.frontend.apache.echarts.{Number, clean}

import scala.scalajs.js

trait EffectScatterDataItemOption extends js.Object with SymbolOptionMixin[CallbackDataParams]
  with EffectScatterStateOption[CallbackDataParams]
  with StatesOptionMixin[EffectScatterStateOption[CallbackDataParams], DefaultEmphasisFocusScaleMixin, js.Any, js.Any]:
  val name: js.UndefOr[String] = js.undefined
  val value: js.UndefOr[ScatterDataValue] = js.undefined
  val rippleEffect: js.UndefOr[RippleEffectOption] = js.undefined

object EffectScatterDataItemOption:
  def apply(
            symbol: js.UndefOr[String | SymbolCallback[CallbackDataParams]] = js.undefined,
            symbolSize: js.UndefOr[Number | js.Array[Number] | SymbolSizeCallback[CallbackDataParams]] = js.undefined,
            symbolRotate: js.UndefOr[Number | SymbolRotateCallback[CallbackDataParams]] = js.undefined,
            symbolKeepAspect: js.UndefOr[Boolean] = js.undefined,
            symbolOffset: js.UndefOr[String | Number | js.Array[String | Number] | SymbolOffsetCallback[CallbackDataParams]] = js.undefined,
            itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = js.undefined,
            label: js.UndefOr[SeriesLabelOption] = js.undefined,
            emphasis: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & DefaultEmphasisFocusScaleMixin & StatesEmphasisOptionMixin] = js.undefined,
            select: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = js.undefined,
            blur: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any] = js.undefined,
            name: js.UndefOr[String] = js.undefined,
            value: js.UndefOr[ScatterDataValue] = js.undefined,
            rippleEffect: js.UndefOr[RippleEffectOption] = js.undefined,
           ): EffectScatterDataItemOption =
    val _symbol: js.UndefOr[String | SymbolCallback[CallbackDataParams]] = symbol
    val _symbolSize: js.UndefOr[Number | js.Array[Number] | SymbolSizeCallback[CallbackDataParams]] = symbolSize
    val _symbolRotate: js.UndefOr[Number | SymbolRotateCallback[CallbackDataParams]] = symbolRotate
    val _symbolKeepAspect: js.UndefOr[Boolean] = symbolKeepAspect
    val _symbolOffset: js.UndefOr[String | Number | js.Array[String | Number] | SymbolOffsetCallback[CallbackDataParams]] = symbolOffset
    val _itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = itemStyle
    val _label: js.UndefOr[SeriesLabelOption] = label
    val _emphasis: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & DefaultEmphasisFocusScaleMixin & StatesEmphasisOptionMixin] = emphasis
    val _select: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = select
    val _blur: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any] = blur
    val _name: js.UndefOr[String] = name
    val _value: js.UndefOr[ScatterDataValue] = value
    val _rippleEffect: js.UndefOr[RippleEffectOption] = rippleEffect
    val effectScatterDataItemOption: EffectScatterDataItemOption = new EffectScatterDataItemOption:
      override val symbol: js.UndefOr[String | SymbolCallback[CallbackDataParams]] = _symbol
      override val symbolSize: js.UndefOr[Number | js.Array[Number] | SymbolSizeCallback[CallbackDataParams]] = _symbolSize
      override val symbolRotate: js.UndefOr[Number | SymbolRotateCallback[CallbackDataParams]] = _symbolRotate
      override val symbolKeepAspect: js.UndefOr[Boolean] = _symbolKeepAspect
      override val symbolOffset: js.UndefOr[String | Number | js.Array[String | Number] | SymbolOffsetCallback[CallbackDataParams]] = _symbolOffset
      override val itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[SeriesLabelOption] = _label
      override val emphasis: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & DefaultEmphasisFocusScaleMixin & StatesEmphasisOptionMixin] = _emphasis
      override val select: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = _select
      override val blur: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any] = _blur
      override val name: js.UndefOr[String] = _name
      override val value: js.UndefOr[ScatterDataValue] = _value
      override val rippleEffect: js.UndefOr[RippleEffectOption] = _rippleEffect
    effectScatterDataItemOption.clean