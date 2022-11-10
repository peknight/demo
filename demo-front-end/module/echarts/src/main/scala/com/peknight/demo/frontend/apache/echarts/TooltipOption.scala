package com.peknight.demo.frontend.apache.echarts

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
  def apply(): TooltipOption = new TooltipOption {}
