package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait CommonTooltipOption[FormatterParams] extends js.Object:
  val show: js.UndefOr[Boolean] = js.undefined
  /**
   * When to trigger
   */
  val triggerOn: js.UndefOr["mousemove" | "click" | "none" | "mousemove" | "click"] = js.undefined
  /**
   * Whether to not hide popup content automatically
   */
  val alwaysShowContent: js.UndefOr[Boolean] = js.undefined
  val formatter: js.UndefOr[String | TooltipFormatterCallback[FormatterParams]] = js.undefined
  /**
   * Formatter of value.
   *
   * Will be ignored if tooltip.formatter is specified.
   */
  val valueFormatter: js.UndefOr[js.Function1[OptionDataValue | js.Array[OptionDataValue], String]] = js.undefined
  /**
   * Absolution pixel [x, y] array. Or relative percent String [x, y] array.
   * If trigger is "item". position can be set to "inside" / "top" / "left" / "right" / "bottom",
   * which is relative to the hovered element.
   *
   * Support to be a callback
   */
  val position: js.UndefOr[js.Array[Number | String] | TooltipBuiltinPosition | TooltipPositionCallback | TooltipBoxLayoutOption] = js.undefined
  val confine: js.UndefOr[Boolean] = js.undefined
  /**
   * Consider triggered from axisPointer handle, verticalAlign should be "middle"
   */
  val align: js.UndefOr[HorizontalAlign] = js.undefined
  val verticalAlign: js.UndefOr[VerticalAlign] = js.undefined
  /**
   * Delay of show. milesecond.
   */
  val showDelay: js.UndefOr[Number] = js.undefined
  /**
   * Delay of hide. milesecond.
   */
  val hideDelay: js.UndefOr[Number] = js.undefined
  val transitionDuration: js.UndefOr[Number] = js.undefined
  /**
   * Whether mouse is allowed to enter the floating layer of tooltip
   * If you need to interact in the tooltip like with links or buttons, it can be set as true.
   */
  val enterable: js.UndefOr[Boolean] = js.undefined
  val backgroundColor: js.UndefOr[ColorString] = js.undefined
  val borderColor: js.UndefOr[ColorString] = js.undefined
  val borderRadius: js.UndefOr[Number] = js.undefined
  val borderWidth: js.UndefOr[Number] = js.undefined
  val shadowBlur: js.UndefOr[Number] = js.undefined
  val shadowColor: js.UndefOr[String] = js.undefined
  val shadowOffsetX: js.UndefOr[Number] = js.undefined
  val shadowOffsetY: js.UndefOr[Number] = js.undefined
  /**
   * Padding between tooltip content and tooltip border.
   */
  val padding: js.UndefOr[Number | js.Array[Number]] = js.undefined
  /**
   * Available when renderMode is "html"
   */
  val extraCssText: js.UndefOr[String] = js.undefined
