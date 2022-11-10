package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait LegendOption extends js.Object with ComponentOption with LegendStyleOption with BoxLayoutOptionMixin with BorderOptionMixin:
  type Type = String
  type MainType = "legend"
  type NameType = OptionName

  val show: js.UndefOr[Boolean] = js.undefined
  val orient: js.UndefOr[LayoutOrient] = js.undefined
  val align: js.UndefOr["auto" | "left" | "right"] = js.undefined
  val backgroundColor: js.UndefOr[ColorString] = js.undefined
  /**
   * Border radius of background rect
   * @default 0
   */
  val borderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined
  /**
   * Padding between legend item and border.
   * Support to be a single Number or an array.
   * @default 5
   */
  val padding: js.UndefOr[Number | js.Array[Number]] = js.undefined
  /**
   * Gap between each legend item.
   * @default 10
   */
  val itemGap: js.UndefOr[Number] = js.undefined
  /**
   * Width of legend symbol
   */
  val itemWidth: js.UndefOr[Number] = js.undefined
  /**
   * Height of legend symbol
   */
  val itemHeight: js.UndefOr[Number] = js.undefined
  val selectedMode: js.UndefOr[Boolean | "single" | "multiple"] = js.undefined
  /**
   * selected map of each item. Default to be selected if item is not in the map
   */
  val selected: js.UndefOr[js.Dictionary[Boolean]] = js.undefined
  /**
   * Buttons for all select or inverse select.
   * @example
   *  selector: [{type: "all or inverse", title: xxx}]
   *  selector: true
   *  selector: ["all", "inverse"]
   */
  val selector: js.UndefOr[js.Array[LegendSelectorButtonOption | SelectorType] | Boolean] = js.undefined
  val selectorLabel: js.UndefOr[LabelOption] = js.undefined
  val emphasis: js.UndefOr[SelectorLabelMixin] = js.undefined
  /**
   * Position of selector buttons.
   */
  val selectorPosition: js.UndefOr["auto" | "start" | "end"] = js.undefined
  /**
   * Gap between each selector button
   */
  val selectorItemGap: js.UndefOr[Number] = js.undefined
  /**
   * Gap between selector buttons group and legend main items.
   */
  val selectorButtonGap: js.UndefOr[Number] = js.undefined
  val data: js.UndefOr[js.Array[String | DataItem]] = js.undefined
  /**
   * Tooltip option
   */
  val tooltip: js.UndefOr[CommonTooltipOption[LegendTooltipFormatterParams]] = js.undefined

object LegendOption:
  def apply(data: js.UndefOr[js.Array[String | DataItem]] = js.undefined): LegendOption =
    val _data = data
    new LegendOption {
      override val data: js.UndefOr[js.Array[String | DataItem]] = _data
    }
