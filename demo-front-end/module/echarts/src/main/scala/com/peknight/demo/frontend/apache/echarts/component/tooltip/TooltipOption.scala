package com.peknight.demo.frontend.apache.echarts.component.tooltip

import com.peknight.demo.frontend.apache.echarts.component.axispointer.AxisPointerOption
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js
import scala.scalajs.js.UndefOr

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
  def apply(axisPointer: js.UndefOr[AxisPointerOption & AxisPointerMixin] = js.undefined,
            showContent: js.UndefOr[Boolean] = js.undefined,
            trigger: js.UndefOr["item" | "axis" | "none"] = js.undefined,
            displayMode: js.UndefOr["single" | "multipleByCoordSys"] = js.undefined,
            renderMode: js.UndefOr["auto" | TooltipRenderMode] = js.undefined,
            appendToBody: js.UndefOr[Boolean] = js.undefined,
            className: js.UndefOr[String] = js.undefined,
            order: js.UndefOr[TooltipOrderMode] = js.undefined): TooltipOption =
    val _axisPointer: js.UndefOr[AxisPointerOption & AxisPointerMixin] = axisPointer
    val _showContent: js.UndefOr[Boolean] = showContent
    val _trigger: js.UndefOr["item" | "axis" | "none"] = trigger
    val _displayMode: js.UndefOr["single" | "multipleByCoordSys"] = displayMode
    val _renderMode: js.UndefOr["auto" | TooltipRenderMode] = renderMode
    val _appendToBody: js.UndefOr[Boolean] = appendToBody
    val _className: js.UndefOr[String] = className
    val _order: js.UndefOr[TooltipOrderMode] = order
    new TooltipOption {
      override val mainType: UndefOr[MainType] = "tooltip"
      override val axisPointer: js.UndefOr[AxisPointerOption & AxisPointerMixin] = _axisPointer
      override val showContent: js.UndefOr[Boolean] = _showContent
      override val trigger: js.UndefOr["item" | "axis" | "none"] = _trigger
      override val displayMode: js.UndefOr["single" | "multipleByCoordSys"] = _displayMode
      override val renderMode: js.UndefOr["auto" | TooltipRenderMode] = _renderMode
      override val appendToBody: js.UndefOr[Boolean] = _appendToBody
      override val className: js.UndefOr[String] = _className
      override val order: js.UndefOr[TooltipOrderMode] = _order
    }
