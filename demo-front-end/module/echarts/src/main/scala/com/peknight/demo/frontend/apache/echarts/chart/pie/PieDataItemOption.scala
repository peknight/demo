package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait PieDataItemOption extends js.Object with OptionDataItemObject[OptionDataValueNumeric] with PieStateOption[_]
  with StatesOptionMixin[PieStateOption[_], ExtraStateOption]:
  val cursor: js.UndefOr[String] = js.undefined

object PieDataItemOption:
  def apply(id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            groupId: js.UndefOr[OptionId] = js.undefined,
            value: js.UndefOr[js.Array[OptionDataValueNumeric] | OptionDataValueNumeric] = js.undefined,
            selected: js.UndefOr[Boolean] = js.undefined,
            cursor: js.UndefOr[String] = js.undefined
           ): PieDataItemOption =
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _groupId: js.UndefOr[OptionId] = groupId
    val _value: js.UndefOr[js.Array[OptionDataValueNumeric] | OptionDataValueNumeric] = value
    val _selected: js.UndefOr[Boolean] = selected
    val _cursor: js.UndefOr[String] = cursor
    new PieDataItemOption:
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[OptionName] = _name
      override val groupId: js.UndefOr[OptionId] = _groupId
      override val value: js.UndefOr[js.Array[OptionDataValueNumeric] | OptionDataValueNumeric] = _value
      override val selected: js.UndefOr[Boolean] = _selected
      override val cursor: js.UndefOr[ColorString] = _cursor
