package com.peknight.demo.frontend.apache.echarts.component.legend

import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{CanvasLineCap, CanvasLineJoin, Number}

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
  def apply(
            `type`: js.UndefOr[String] = js.undefined,
            id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            z: js.UndefOr[Number] = js.undefined,
            zlevel: js.UndefOr[Number] = js.undefined,
            icon: js.UndefOr[String] = js.undefined,
            inactiveColor: js.UndefOr[ColorString] = js.undefined,
            inactiveBorderColor: js.UndefOr[ColorString] = js.undefined,
            inactiveBorderWidth: js.UndefOr[Number | "auto"] = js.undefined,
            formatter: js.UndefOr[String | js.Function1[String, String]] = js.undefined,
            itemStyle: js.UndefOr[LegendItemStyleOption] = js.undefined,
            lineStyle: js.UndefOr[LegendLineStyleOption] = js.undefined,
            textStyle: js.UndefOr[LabelOption] = js.undefined,
            symbolRotate: js.UndefOr[Number | "inherit"] = js.undefined,
            width: js.UndefOr[Number | String] = js.undefined,
            height: js.UndefOr[Number | String] = js.undefined,
            top: js.UndefOr[Number | String] = js.undefined,
            right: js.UndefOr[Number | String] = js.undefined,
            bottom: js.UndefOr[Number | String] = js.undefined,
            left: js.UndefOr[Number | String] = js.undefined,
            borderColor: js.UndefOr[ZRColor] = js.undefined,
            borderWidth: js.UndefOr[Number] = js.undefined,
            borderType: js.UndefOr[ZRLineType] = js.undefined,
            borderCap: js.UndefOr[CanvasLineCap] = js.undefined,
            borderJoin: js.UndefOr[CanvasLineJoin] = js.undefined,
            borderDashOffset: js.UndefOr[Number] = js.undefined,
            borderMiterLimit: js.UndefOr[Number] = js.undefined,
            show: js.UndefOr[Boolean] = js.undefined,
            orient: js.UndefOr[LayoutOrient] = js.undefined,
            align: js.UndefOr["auto" | "left" | "right"] = js.undefined,
            backgroundColor: js.UndefOr[ColorString] = js.undefined,
            borderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            padding: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            itemGap: js.UndefOr[Number] = js.undefined,
            itemWidth: js.UndefOr[Number] = js.undefined,
            itemHeight: js.UndefOr[Number] = js.undefined,
            selectedMode: js.UndefOr[Boolean | "single" | "multiple"] = js.undefined,
            selected: js.UndefOr[js.Dictionary[Boolean]] = js.undefined,
            selector: js.UndefOr[js.Array[LegendSelectorButtonOption | SelectorType] | Boolean] = js.undefined,
            selectorLabel: js.UndefOr[LabelOption] = js.undefined,
            emphasis: js.UndefOr[SelectorLabelMixin] = js.undefined,
            selectorPosition: js.UndefOr["auto" | "start" | "end"] = js.undefined,
            selectorItemGap: js.UndefOr[Number] = js.undefined,
            selectorButtonGap: js.UndefOr[Number] = js.undefined,
            data: js.UndefOr[js.Array[String | DataItem]] = js.undefined,
            tooltip: js.UndefOr[CommonTooltipOption[LegendTooltipFormatterParams]] = js.undefined): LegendOption =
    val _type: js.UndefOr[String] = `type`
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _z: js.UndefOr[Number] = z
    val _zlevel: js.UndefOr[Number] = zlevel
    val _icon: js.UndefOr[String] = icon
    val _inactiveColor: js.UndefOr[ColorString] = inactiveColor
    val _inactiveBorderColor: js.UndefOr[ColorString] = inactiveBorderColor
    val _inactiveBorderWidth: js.UndefOr[Number | "auto"] = inactiveBorderWidth
    val _formatter: js.UndefOr[String | js.Function1[String, String]] = formatter
    val _itemStyle: js.UndefOr[LegendItemStyleOption] = itemStyle
    val _lineStyle: js.UndefOr[LegendLineStyleOption] = lineStyle
    val _textStyle: js.UndefOr[LabelOption] = textStyle
    val _symbolRotate: js.UndefOr[Number | "inherit"] = symbolRotate
    val _width: js.UndefOr[Number | String] = width
    val _height: js.UndefOr[Number | String] = height
    val _top: js.UndefOr[Number | String] = top
    val _right: js.UndefOr[Number | String] = right
    val _bottom: js.UndefOr[Number | String] = bottom
    val _left: js.UndefOr[Number | String] = left
    val _borderColor: js.UndefOr[ZRColor] = borderColor
    val _borderWidth: js.UndefOr[Number] = borderWidth
    val _borderType: js.UndefOr[ZRLineType] = borderType
    val _borderCap: js.UndefOr[CanvasLineCap] = borderCap
    val _borderJoin: js.UndefOr[CanvasLineJoin] = borderJoin
    val _borderDashOffset: js.UndefOr[Number] = borderDashOffset
    val _borderMiterLimit: js.UndefOr[Number] = borderMiterLimit
    val _show: js.UndefOr[Boolean] = show
    val _orient: js.UndefOr[LayoutOrient] = orient
    val _align: js.UndefOr["auto" | "left" | "right"] = align
    val _backgroundColor: js.UndefOr[ColorString] = backgroundColor
    val _borderRadius: js.UndefOr[Number | js.Array[Number]] = borderRadius
    val _padding: js.UndefOr[Number | js.Array[Number]] = padding
    val _itemGap: js.UndefOr[Number] = itemGap
    val _itemWidth: js.UndefOr[Number] = itemWidth
    val _itemHeight: js.UndefOr[Number] = itemHeight
    val _selectedMode: js.UndefOr[Boolean | "single" | "multiple"] = selectedMode
    val _selected: js.UndefOr[js.Dictionary[Boolean]] = selected
    val _selector: js.UndefOr[js.Array[LegendSelectorButtonOption | SelectorType] | Boolean] = selector
    val _selectorLabel: js.UndefOr[LabelOption] = selectorLabel
    val _emphasis: js.UndefOr[SelectorLabelMixin] = emphasis
    val _selectorPosition: js.UndefOr["auto" | "start" | "end"] = selectorPosition
    val _selectorItemGap: js.UndefOr[Number] = selectorItemGap
    val _selectorButtonGap: js.UndefOr[Number] = selectorButtonGap
    val _data: js.UndefOr[js.Array[String | DataItem]] = data
    val _tooltip: js.UndefOr[CommonTooltipOption[LegendTooltipFormatterParams]] = tooltip
    new LegendOption:
      override val mainType: js.UndefOr[MainType] = "legend"
      override val `type`: js.UndefOr[Type] = _type
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[NameType] = _name
      override val z: js.UndefOr[Number] = _z
      override val zlevel: js.UndefOr[Number] = _zlevel
      override val icon: js.UndefOr[String] = _icon
      override val inactiveColor: js.UndefOr[ColorString] = _inactiveColor
      override val inactiveBorderColor: js.UndefOr[ColorString] = _inactiveBorderColor
      override val inactiveBorderWidth: js.UndefOr[Number | "auto"] = _inactiveBorderWidth
      override val formatter: js.UndefOr[String | js.Function1[String, String]] = _formatter
      override val itemStyle: js.UndefOr[LegendItemStyleOption] = _itemStyle
      override val lineStyle: js.UndefOr[LegendLineStyleOption] = _lineStyle
      override val textStyle: js.UndefOr[LabelOption] = _textStyle
      override val symbolRotate: js.UndefOr[Number | "inherit"] = _symbolRotate
      override val width: js.UndefOr[Number | String] = _width
      override val height: js.UndefOr[Number | String] = _height
      override val top: js.UndefOr[Number | String] = _top
      override val right: js.UndefOr[Number | String] = _right
      override val bottom: js.UndefOr[Number | String] = _bottom
      override val left: js.UndefOr[Number | String] = _left
      override val borderColor: js.UndefOr[ZRColor] = _borderColor
      override val borderWidth: js.UndefOr[Number] = _borderWidth
      override val borderType: js.UndefOr[ZRLineType] = _borderType
      override val borderCap: js.UndefOr[CanvasLineCap] = _borderCap
      override val borderJoin: js.UndefOr[CanvasLineJoin] = _borderJoin
      override val borderDashOffset: js.UndefOr[Number] = _borderDashOffset
      override val borderMiterLimit: js.UndefOr[Number] = _borderMiterLimit
      override val show: js.UndefOr[Boolean] = _show
      override val orient: js.UndefOr[LayoutOrient] = _orient
      override val align: js.UndefOr["auto" | "left" | "right"] = _align
      override val backgroundColor: js.UndefOr[ColorString] = _backgroundColor
      override val borderRadius: js.UndefOr[Number | js.Array[Number]] = _borderRadius
      override val padding: js.UndefOr[Number | js.Array[Number]] = _padding
      override val itemGap: js.UndefOr[Number] = _itemGap
      override val itemWidth: js.UndefOr[Number] = _itemWidth
      override val itemHeight: js.UndefOr[Number] = _itemHeight
      override val selectedMode: js.UndefOr[Boolean | "single" | "multiple"] = _selectedMode
      override val selected: js.UndefOr[js.Dictionary[Boolean]] = _selected
      override val selector: js.UndefOr[js.Array[LegendSelectorButtonOption | SelectorType] | Boolean] = _selector
      override val selectorLabel: js.UndefOr[LabelOption] = _selectorLabel
      override val emphasis: js.UndefOr[SelectorLabelMixin] = _emphasis
      override val selectorPosition: js.UndefOr["auto" | "start" | "end"] = _selectorPosition
      override val selectorItemGap: js.UndefOr[Number] = _selectorItemGap
      override val selectorButtonGap: js.UndefOr[Number] = _selectorButtonGap
      override val data: js.UndefOr[js.Array[String | DataItem]] = _data
      override val tooltip: js.UndefOr[CommonTooltipOption[LegendTooltipFormatterParams]] = _tooltip
