package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait TextCommonOption extends ShadowOptionMixin with BaseTextStyle:
  val align: js.UndefOr[HorizontalAlign] = js.undefined
  val verticalAlign: js.UndefOr[VerticalAlign] = js.undefined
  val baseline: js.UndefOr[VerticalAlign] = js.undefined
  val opacity: js.UndefOr[Number] = js.undefined
  val lineHeight: js.UndefOr[Number] = js.undefined
  val backgroundColor: js.UndefOr[ColorString | ImageMixin] = js.undefined
  val borderColor: js.UndefOr[String] = js.undefined
  val borderWidth: js.UndefOr[Number] = js.undefined
  val borderType: js.UndefOr[ZRLineType] = js.undefined
  val borderDashOffset: js.UndefOr[Number] = js.undefined
  val borderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined
  val padding: js.UndefOr[Number | js.Array[Number]] = js.undefined
  val width: js.UndefOr[Number | String] = js.undefined
  val height: js.UndefOr[Number] = js.undefined
  val textBorderColor: js.UndefOr[String] = js.undefined
  val textBorderWidth: js.UndefOr[Number] = js.undefined
  val textBorderType: js.UndefOr[ZRLineType] = js.undefined
  val textBorderDashOffset: js.UndefOr[Number] = js.undefined
  val textShadowBlur: js.UndefOr[Number] = js.undefined
  val textShadowColor: js.UndefOr[String] = js.undefined
  val textShadowOffsetX: js.UndefOr[Number] = js.undefined
  val textShadowOffsetY: js.UndefOr[Number] = js.undefined
  val tag: js.UndefOr[String] = js.undefined
