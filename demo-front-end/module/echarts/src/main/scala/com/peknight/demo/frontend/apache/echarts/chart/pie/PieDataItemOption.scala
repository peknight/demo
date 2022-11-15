package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.clean
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait PieDataItemOption extends js.Object with OptionDataItemObject[OptionDataValueNumeric]
  with PieStateOption[PieCallbackDataParams]
  with StatesOptionMixin[PieStateOption[PieCallbackDataParams], PieEmphasisMixin, js.Any, js.Any]:
  val cursor: js.UndefOr[String] = js.undefined

object PieDataItemOption:
  def apply(id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            groupId: js.UndefOr[OptionId] = js.undefined,
            value: js.UndefOr[js.Array[OptionDataValueNumeric] | OptionDataValueNumeric] = js.undefined,
            selected: js.UndefOr[Boolean] = js.undefined,
            itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = js.undefined,
            label: js.UndefOr[PieLabelOption] = js.undefined,
            labelLine: js.UndefOr[PieLabelLineOption] = js.undefined,
            emphasis: js.UndefOr[PieStateOption[PieCallbackDataParams] & PieEmphasisMixin & StatesEmphasisOptionMixin] = js.undefined,
            select: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any & StatesSelectOptionMixin] = js.undefined,
            blur: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any] = js.undefined,
            cursor: js.UndefOr[String] = js.undefined
           ): PieDataItemOption =
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _groupId: js.UndefOr[OptionId] = groupId
    val _value: js.UndefOr[js.Array[OptionDataValueNumeric] | OptionDataValueNumeric] = value
    val _selected: js.UndefOr[Boolean] = selected
    val _itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = itemStyle
    val _label: js.UndefOr[PieLabelOption] = label
    val _labelLine: js.UndefOr[PieLabelLineOption] = labelLine
    val _emphasis: js.UndefOr[PieStateOption[PieCallbackDataParams] & PieEmphasisMixin & StatesEmphasisOptionMixin] = emphasis
    val _select: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any & StatesSelectOptionMixin] = select
    val _blur: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any] = blur
    val _cursor: js.UndefOr[String] = cursor
    val pieDataItemOption = new PieDataItemOption:
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[OptionName] = _name
      override val groupId: js.UndefOr[OptionId] = _groupId
      override val value: js.UndefOr[js.Array[OptionDataValueNumeric] | OptionDataValueNumeric] = _value
      override val selected: js.UndefOr[Boolean] = _selected
      override val itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[PieLabelOption] = _label
      override val labelLine: js.UndefOr[PieLabelLineOption] = _labelLine
      override val emphasis: js.UndefOr[PieStateOption[PieCallbackDataParams] & PieEmphasisMixin & StatesEmphasisOptionMixin] = _emphasis
      override val select: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any & StatesSelectOptionMixin] = _select
      override val blur: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any] = _blur
      override val cursor: js.UndefOr[ColorString] = _cursor
    pieDataItemOption.clean