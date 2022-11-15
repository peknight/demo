package com.peknight.demo.frontend.apache.echarts.component.tooltip

import com.peknight.demo.frontend.apache.echarts.component.axispointer.AxisPointerOption
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{Number, clean}

import scala.scalajs.js

trait TooltipOption extends js.Object with CommonTooltipOption[TopLevelFormatterParams] with ComponentOption:
  type Type = String
  type MainType = "tooltip"
  type NameType = OptionName
  val axisPointer: js.UndefOr[AxisPointerOption & AxisPointerMixin] = js.undefined
  /**
   * If show popup content
   */
  val showContent: js.UndefOr[Boolean] = js.undefined
  /**
   * Trigger only works on coordinate system.
   */
  val trigger: js.UndefOr["item" | "axis" | "none"] = js.undefined
  val displayMode: js.UndefOr["single" | "multipleByCoordSys"] = js.undefined
  /**
   * "auto": use html by default, and use non-html if `document` is not defined
   * "html": use html for tooltip
   * "richText": use canvas, svg, and etc. for tooltip
   */
  val renderMode: js.UndefOr["auto" | TooltipRenderMode] = js.undefined
  /**
   * If append popup dom to document.body
   * Only available when renderMode is html
   */
  val appendToBody: js.UndefOr[Boolean] = js.undefined
  /**
   * specified class name of tooltip dom
   * Only available when renderMode is html
   */
  val className: js.UndefOr[String] = js.undefined
  val order: js.UndefOr[TooltipOrderMode] = js.undefined

object TooltipOption:
  def apply(`type`: js.UndefOr[String] = js.undefined,
            id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            z: js.UndefOr[Number] = js.undefined,
            zlevel: js.UndefOr[Number] = js.undefined,
            show: js.UndefOr[Boolean] = js.undefined,
            triggerOn: js.UndefOr["mousemove" | "click" | "none" | "mousemove" | "click"] = js.undefined,
            alwaysShowContent: js.UndefOr[Boolean] = js.undefined,
            formatter: js.UndefOr[String | TooltipFormatterCallback[TopLevelFormatterParams]] = js.undefined,
            valueFormatter: js.UndefOr[js.Function1[OptionDataValue | js.Array[OptionDataValue], String]] = js.undefined,
            position: js.UndefOr[js.Array[Number | String] | TooltipBuiltinPosition | TooltipPositionCallback | TooltipBoxLayoutOption] = js.undefined,
            confine: js.UndefOr[Boolean] = js.undefined,
            align: js.UndefOr[HorizontalAlign] = js.undefined,
            verticalAlign: js.UndefOr[VerticalAlign] = js.undefined,
            showDelay: js.UndefOr[Number] = js.undefined,
            hideDelay: js.UndefOr[Number] = js.undefined,
            transitionDuration: js.UndefOr[Number] = js.undefined,
            enterable: js.UndefOr[Boolean] = js.undefined,
            backgroundColor: js.UndefOr[ColorString] = js.undefined,
            borderColor: js.UndefOr[ColorString] = js.undefined,
            borderRadius: js.UndefOr[Number] = js.undefined,
            borderWidth: js.UndefOr[Number] = js.undefined,
            shadowBlur: js.UndefOr[Number] = js.undefined,
            shadowColor: js.UndefOr[String] = js.undefined,
            shadowOffsetX: js.UndefOr[Number] = js.undefined,
            shadowOffsetY: js.UndefOr[Number] = js.undefined,
            padding: js.UndefOr[Number | js.Array[Number]] = js.undefined,
            extraCssText: js.UndefOr[String] = js.undefined,
            axisPointer: js.UndefOr[AxisPointerOption & AxisPointerMixin] = js.undefined,
            showContent: js.UndefOr[Boolean] = js.undefined,
            trigger: js.UndefOr["item" | "axis" | "none"] = js.undefined,
            displayMode: js.UndefOr["single" | "multipleByCoordSys"] = js.undefined,
            renderMode: js.UndefOr["auto" | TooltipRenderMode] = js.undefined,
            appendToBody: js.UndefOr[Boolean] = js.undefined,
            className: js.UndefOr[String] = js.undefined,
            order: js.UndefOr[TooltipOrderMode] = js.undefined): TooltipOption =
    val _type: js.UndefOr[String] = `type`
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _z: js.UndefOr[Number] = z
    val _zlevel: js.UndefOr[Number] = zlevel
    val _show: js.UndefOr[Boolean] = show
    val _triggerOn: js.UndefOr["mousemove" | "click" | "none" | "mousemove" | "click"] = triggerOn
    val _alwaysShowContent: js.UndefOr[Boolean] = alwaysShowContent
    val _formatter: js.UndefOr[String | TooltipFormatterCallback[TopLevelFormatterParams]] = formatter
    val _valueFormatter: js.UndefOr[js.Function1[OptionDataValue | js.Array[OptionDataValue], String]] = valueFormatter
    val _position: js.UndefOr[js.Array[Number | String] | TooltipBuiltinPosition | TooltipPositionCallback | TooltipBoxLayoutOption] = position
    val _confine: js.UndefOr[Boolean] = confine
    val _align: js.UndefOr[HorizontalAlign] = align
    val _verticalAlign: js.UndefOr[VerticalAlign] = verticalAlign
    val _showDelay: js.UndefOr[Number] = showDelay
    val _hideDelay: js.UndefOr[Number] = hideDelay
    val _transitionDuration: js.UndefOr[Number] = transitionDuration
    val _enterable: js.UndefOr[Boolean] = enterable
    val _backgroundColor: js.UndefOr[ColorString] = backgroundColor
    val _borderColor: js.UndefOr[ColorString] = borderColor
    val _borderRadius: js.UndefOr[Number] = borderRadius
    val _borderWidth: js.UndefOr[Number] = borderWidth
    val _shadowBlur: js.UndefOr[Number] = shadowBlur
    val _shadowColor: js.UndefOr[String] = shadowColor
    val _shadowOffsetX: js.UndefOr[Number] = shadowOffsetX
    val _shadowOffsetY: js.UndefOr[Number] = shadowOffsetY
    val _padding: js.UndefOr[Number | js.Array[Number]] = padding
    val _extraCssText: js.UndefOr[String] = extraCssText
    val _axisPointer: js.UndefOr[AxisPointerOption & AxisPointerMixin] = axisPointer
    val _showContent: js.UndefOr[Boolean] = showContent
    val _trigger: js.UndefOr["item" | "axis" | "none"] = trigger
    val _displayMode: js.UndefOr["single" | "multipleByCoordSys"] = displayMode
    val _renderMode: js.UndefOr["auto" | TooltipRenderMode] = renderMode
    val _appendToBody: js.UndefOr[Boolean] = appendToBody
    val _className: js.UndefOr[String] = className
    val _order: js.UndefOr[TooltipOrderMode] = order
    val tooltipOption = new TooltipOption:
      override val mainType: js.UndefOr[MainType] = "tooltip"
      override val `type`: js.UndefOr[Type] = _type
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[NameType] = _name
      override val z: js.UndefOr[Number] = _z
      override val zlevel: js.UndefOr[Number] = _zlevel
      override val show: js.UndefOr[Boolean] = _show
      override val triggerOn: js.UndefOr["mousemove" | "click" | "none" | "mousemove" | "click"] = _triggerOn
      override val alwaysShowContent: js.UndefOr[Boolean] = _alwaysShowContent
      override val formatter: js.UndefOr[String | TooltipFormatterCallback[TopLevelFormatterParams]] = _formatter
      override val valueFormatter: js.UndefOr[js.Function1[OptionDataValue | js.Array[OptionDataValue], String]] = _valueFormatter
      override val position: js.UndefOr[js.Array[Number | String] | TooltipBuiltinPosition | TooltipPositionCallback | TooltipBoxLayoutOption] = _position
      override val confine: js.UndefOr[Boolean] = _confine
      override val align: js.UndefOr[HorizontalAlign] = _align
      override val verticalAlign: js.UndefOr[VerticalAlign] = _verticalAlign
      override val showDelay: js.UndefOr[Number] = _showDelay
      override val hideDelay: js.UndefOr[Number] = _hideDelay
      override val transitionDuration: js.UndefOr[Number] = _transitionDuration
      override val enterable: js.UndefOr[Boolean] = _enterable
      override val backgroundColor: js.UndefOr[ColorString] = _backgroundColor
      override val borderColor: js.UndefOr[ColorString] = _borderColor
      override val borderRadius: js.UndefOr[Number] = _borderRadius
      override val borderWidth: js.UndefOr[Number] = _borderWidth
      override val shadowBlur: js.UndefOr[Number] = _shadowBlur
      override val shadowColor: js.UndefOr[String] = _shadowColor
      override val shadowOffsetX: js.UndefOr[Number] = _shadowOffsetX
      override val shadowOffsetY: js.UndefOr[Number] = _shadowOffsetY
      override val padding: js.UndefOr[Number | js.Array[Number]] = _padding
      override val extraCssText: js.UndefOr[String] = _extraCssText
      override val axisPointer: js.UndefOr[AxisPointerOption & AxisPointerMixin] = _axisPointer
      override val showContent: js.UndefOr[Boolean] = _showContent
      override val trigger: js.UndefOr["item" | "axis" | "none"] = _trigger
      override val displayMode: js.UndefOr["single" | "multipleByCoordSys"] = _displayMode
      override val renderMode: js.UndefOr["auto" | TooltipRenderMode] = _renderMode
      override val appendToBody: js.UndefOr[Boolean] = _appendToBody
      override val className: js.UndefOr[String] = _className
      override val order: js.UndefOr[TooltipOrderMode] = _order
    tooltipOption.clean