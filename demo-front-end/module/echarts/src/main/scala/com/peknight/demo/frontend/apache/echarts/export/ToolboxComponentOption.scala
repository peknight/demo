package com.peknight.demo.frontend.apache.echarts.`export`

import com.peknight.demo.frontend.apache.echarts.component.toolbox.{IconStyleMixin, ToolboxOption, ToolboxTooltipFormatterParams}
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{CanvasLineCap, CanvasLineJoin, Number}

import scala.scalajs.js

trait ToolboxComponentOption extends ToolboxOption[ToolboxFeatureOption]

object ToolboxComponentOption:
  def apply(`type`: js.UndefOr[String] = js.undefined,
            id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            z: js.UndefOr[Number] = js.undefined,
            zlevel: js.UndefOr[Number] = js.undefined,
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
            backgroundColor: js.UndefOr[ZRColor] = js.undefined,
            borderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            padding: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            itemSize: js.UndefOr[Number] = js.undefined,
            itemGap: js.UndefOr[Number] = js.undefined,
            showTitle: js.UndefOr[Boolean] = js.undefined,
            iconStyle: js.UndefOr[ItemStyleOption[ZRColor]] = js.undefined,
            emphasis: js.UndefOr[IconStyleMixin[ItemStyleOption[CallbackDataParams]]] = js.undefined,
            textStyle: js.UndefOr[LabelOption] = js.undefined,
            tooltip: js.UndefOr[CommonTooltipOption[ToolboxTooltipFormatterParams]] = js.undefined,
            feature: js.UndefOr[ToolboxFeatureOption] = js.undefined): ToolboxComponentOption =
    val _type: js.UndefOr[String] = `type`
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _z: js.UndefOr[Number] = z
    val _zlevel: js.UndefOr[Number] = zlevel
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
    val _backgroundColor: js.UndefOr[ZRColor] = backgroundColor
    val _borderRadius: js.UndefOr[Number | js.Array[Number]] = borderRadius
    val _padding: js.UndefOr[Number | js.Array[Number]] = padding
    val _itemSize: js.UndefOr[Number] = itemSize
    val _itemGap: js.UndefOr[Number] = itemGap
    val _showTitle: js.UndefOr[Boolean] = showTitle
    val _iconStyle: js.UndefOr[ItemStyleOption[ZRColor]] = iconStyle
    val _emphasis: js.UndefOr[IconStyleMixin[ItemStyleOption[CallbackDataParams]]] = emphasis
    val _textStyle: js.UndefOr[LabelOption] = textStyle
    val _tooltip: js.UndefOr[CommonTooltipOption[ToolboxTooltipFormatterParams]] = tooltip
    val _feature: js.UndefOr[ToolboxFeatureOption] = feature
    new ToolboxComponentOption:
      override val mainType: js.UndefOr[MainType] = "toolbox"
      override val `type`: js.UndefOr[Type] = _type
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[NameType] = _name
      override val z: js.UndefOr[Number] = _z
      override val zlevel: js.UndefOr[Number] = _zlevel
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
      override val backgroundColor: js.UndefOr[ZRColor] = _backgroundColor
      override val borderRadius: js.UndefOr[Number | js.Array[Number]] = _borderRadius
      override val padding: js.UndefOr[Number | js.Array[Number]] = _padding
      override val itemSize: js.UndefOr[Number] = _itemSize
      override val itemGap: js.UndefOr[Number] = _itemGap
      override val showTitle: js.UndefOr[Boolean] = _showTitle
      override val iconStyle: js.UndefOr[ItemStyleOption[ZRColor]] = _iconStyle
      override val emphasis: js.UndefOr[IconStyleMixin[ItemStyleOption[CallbackDataParams]]] = _emphasis
      override val textStyle: js.UndefOr[LabelOption] = _textStyle
      override val tooltip: js.UndefOr[CommonTooltipOption[ToolboxTooltipFormatterParams]] = _tooltip
      override val feature: js.UndefOr[ToolboxFeatureOption] = _feature
