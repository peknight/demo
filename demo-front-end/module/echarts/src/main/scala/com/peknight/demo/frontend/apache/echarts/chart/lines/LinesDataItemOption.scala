package com.peknight.demo.frontend.apache.echarts.chart.lines

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.chart.helper.LineDrawEffectOption
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait LinesDataItemOption extends js.Object with LinesStateOption[CallbackDataParams]
  with StatesOptionMixin[LinesStateOption[CallbackDataParams], DefaultStatesMixinEmphasis, js.Any, js.Any]:
  val name: js.UndefOr[String] = js.undefined
  val fromName: js.UndefOr[String] = js.undefined
  val toName: js.UndefOr[String] = js.undefined
  val symbol: js.UndefOr[js.Array[String] | String] = js.undefined
  val symbolSize: js.UndefOr[js.Array[Number] | Number] = js.undefined
  val coords: js.UndefOr[LinesCoords] = js.undefined
  val value: js.UndefOr[LinesValue] = js.undefined
  val effect: js.UndefOr[LineDrawEffectOption] = js.undefined

object LinesDataItemOption:
  def apply(lineStyle: js.UndefOr[LinesLineStyleOption[js.Function1[CallbackDataParams, ZRColor] | ZRColor]] = js.undefined,
            label: js.UndefOr[SeriesLineLabelOption] = js.undefined,
            emphasis: js.UndefOr[LinesStateOption[CallbackDataParams] & DefaultStatesMixinEmphasis & StatesEmphasisOptionMixin] = js.undefined,
            select: js.UndefOr[LinesStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = js.undefined,
            blur: js.UndefOr[LinesStateOption[CallbackDataParams] & js.Any] = js.undefined,
            name: js.UndefOr[String] = js.undefined,
            fromName: js.UndefOr[String] = js.undefined,
            toName: js.UndefOr[String] = js.undefined,
            symbol: js.UndefOr[js.Array[String] | String] = js.undefined,
            symbolSize: js.UndefOr[js.Array[Number] | Number] = js.undefined,
            coords: js.UndefOr[LinesCoords] = js.undefined,
            value: js.UndefOr[LinesValue] = js.undefined,
            effect: js.UndefOr[LineDrawEffectOption] = js.undefined): LinesDataItemOption =
    val _lineStyle: js.UndefOr[LinesLineStyleOption[js.Function1[CallbackDataParams, ZRColor] | ZRColor]] = lineStyle
    val _label: js.UndefOr[SeriesLineLabelOption] = label
    val _emphasis: js.UndefOr[LinesStateOption[CallbackDataParams] & DefaultStatesMixinEmphasis & StatesEmphasisOptionMixin] = emphasis
    val _select: js.UndefOr[LinesStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = select
    val _blur: js.UndefOr[LinesStateOption[CallbackDataParams] & js.Any] = blur
    val _name: js.UndefOr[String] = name
    val _fromName: js.UndefOr[String] = fromName
    val _toName: js.UndefOr[String] = toName
    val _symbol: js.UndefOr[js.Array[String] | String] = symbol
    val _symbolSize: js.UndefOr[js.Array[Number] | Number] = symbolSize
    val _coords: js.UndefOr[LinesCoords] = coords
    val _value: js.UndefOr[LinesValue] = value
    val _effect: js.UndefOr[LineDrawEffectOption] = effect
    new LinesDataItemOption:
      override val lineStyle: js.UndefOr[LinesLineStyleOption[js.Function1[CallbackDataParams, ZRColor] | ZRColor]] = _lineStyle
      override val label: js.UndefOr[SeriesLineLabelOption] = _label
      override val emphasis: js.UndefOr[LinesStateOption[CallbackDataParams] & DefaultStatesMixinEmphasis & StatesEmphasisOptionMixin] = _emphasis
      override val select: js.UndefOr[LinesStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = _select
      override val blur: js.UndefOr[LinesStateOption[CallbackDataParams] & js.Any] = _blur
      override val name: js.UndefOr[String] = _name
      override val fromName: js.UndefOr[String] = _fromName
      override val toName: js.UndefOr[String] = _toName
      override val symbol: js.UndefOr[js.Array[String] | String] = _symbol
      override val symbolSize: js.UndefOr[js.Array[Number] | Number] = _symbolSize
      override val coords: js.UndefOr[LinesCoords] = _coords
      override val value: js.UndefOr[LinesValue] = _value
      override val effect: js.UndefOr[LineDrawEffectOption] = _effect
