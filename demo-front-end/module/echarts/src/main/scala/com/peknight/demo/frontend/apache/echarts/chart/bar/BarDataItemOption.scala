package com.peknight.demo.frontend.apache.echarts.chart.bar

import com.peknight.demo.frontend.apache.echarts.component.tooltip.TooltipOption
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait BarDataItemOption extends js.Object with BarStateOption[CallbackDataParams]
  with StatesOptionMixin[BarStateOption[CallbackDataParams], DefaultStatesMixinEmphasis, js.Any, js.Any]
  with OptionDataItemObject[OptionDataValue]:
  val cursor: js.UndefOr[String] = js.undefined
  val tooltip: js.UndefOr[TooltipOption] = js.undefined

object BarDataItemOption:
  def apply(itemStyle: js.UndefOr[BarItemStyleOption[CallbackDataParams]] = js.undefined,
            label: js.UndefOr[BarSeriesLabelOption] = js.undefined,
            emphasis: js.UndefOr[BarStateOption[CallbackDataParams] & DefaultStatesMixinEmphasis & StatesEmphasisOptionMixin] = js.undefined,
            select: js.UndefOr[BarStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = js.undefined,
            blur: js.UndefOr[BarStateOption[CallbackDataParams] & js.Any] = js.undefined,
            id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            groupId: js.UndefOr[OptionId] = js.undefined,
            value: js.UndefOr[js.Array[OptionDataValue] | OptionDataValue] = js.undefined,
            selected: js.UndefOr[Boolean] = js.undefined,
            cursor: js.UndefOr[String] = js.undefined,
            tooltip: js.UndefOr[TooltipOption] = js.undefined): BarDataItemOption =
    val _itemStyle: js.UndefOr[BarItemStyleOption[CallbackDataParams]] = itemStyle
    val _label: js.UndefOr[BarSeriesLabelOption] = label
    val _emphasis: js.UndefOr[BarStateOption[CallbackDataParams] & DefaultStatesMixinEmphasis & StatesEmphasisOptionMixin] = emphasis
    val _select: js.UndefOr[BarStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = select
    val _blur: js.UndefOr[BarStateOption[CallbackDataParams] & js.Any] = blur
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _groupId: js.UndefOr[OptionId] = groupId
    val _value: js.UndefOr[js.Array[OptionDataValue] | OptionDataValue] = value
    val _selected: js.UndefOr[Boolean] = selected
    val _cursor: js.UndefOr[String] = cursor
    val _tooltip: js.UndefOr[TooltipOption] = tooltip
    new BarDataItemOption:
      override val itemStyle: js.UndefOr[BarItemStyleOption[CallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[BarSeriesLabelOption] = _label
      override val emphasis: js.UndefOr[BarStateOption[CallbackDataParams] & DefaultStatesMixinEmphasis & StatesEmphasisOptionMixin] = _emphasis
      override val select: js.UndefOr[BarStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = _select
      override val blur: js.UndefOr[BarStateOption[CallbackDataParams] & js.Any] = _blur
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[OptionName] = _name
      override val groupId: js.UndefOr[OptionId] = _groupId
      override val value: js.UndefOr[js.Array[OptionDataValue] | OptionDataValue] = _value
      override val selected: js.UndefOr[Boolean] = _selected
      override val cursor: js.UndefOr[String] = _cursor
      override val tooltip: js.UndefOr[TooltipOption] = _tooltip
