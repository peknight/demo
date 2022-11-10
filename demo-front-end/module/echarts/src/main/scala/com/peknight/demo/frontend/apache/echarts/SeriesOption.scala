package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

// TODO
trait SeriesOption[StateOption, StatesMixin <: StatesMixinBase] extends js.Object with ComponentOption
  with AnimationOptionMixin with ColorPaletteOptionMixin with StatesOptionMixin[StateOption, StatesMixin]:
  type NameType = OptionName

object SeriesOption:
  def bar(name: js.UndefOr[OptionName] = js.undefined,
          data: js.UndefOr[js.Array[BarDataItemOption | OptionDataValue | js.Array[OptionDataValue]]] = js.undefined)
  : BarSeriesOption$1 =
    val _name = name
    val _data = data
    new BarSeriesOption with SeriesInjectedOption {
      override val name: js.UndefOr[OptionName] = _name
      override val `type`: js.UndefOr[Type] = "bar"
      override val data: js.UndefOr[js.Array[BarDataItemOption | OptionDataValue | js.Array[OptionDataValue]]] = _data
    }
