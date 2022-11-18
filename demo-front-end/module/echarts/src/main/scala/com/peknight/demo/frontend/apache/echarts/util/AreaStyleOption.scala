package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait AreaStyleOption[Clr] extends ShadowOptionMixin:
  val color: js.UndefOr[Clr] = js.undefined
  val opacity: js.UndefOr[Number] = js.undefined

object AreaStyleOption:
  def apply[Clr](shadowBlur: js.UndefOr[Number] = js.undefined,
            shadowColor: js.UndefOr[ColorString] = js.undefined,
            shadowOffsetX: js.UndefOr[Number] = js.undefined,
            shadowOffsetY: js.UndefOr[Number] = js.undefined,
            color: js.UndefOr[Clr] = js.undefined,
            opacity: js.UndefOr[Number] = js.undefined): AreaStyleOption[Clr] =
    val _shadowBlur: js.UndefOr[Number] = shadowBlur
    val _shadowColor: js.UndefOr[ColorString] = shadowColor
    val _shadowOffsetX: js.UndefOr[Number] = shadowOffsetX
    val _shadowOffsetY: js.UndefOr[Number] = shadowOffsetY
    val _color: js.UndefOr[Clr] = color
    val _opacity: js.UndefOr[Number] = opacity
    new AreaStyleOption[Clr]:
      override val shadowBlur: js.UndefOr[Number] = _shadowBlur
      override val shadowColor: js.UndefOr[ColorString] = _shadowColor
      override val shadowOffsetX: js.UndefOr[Number] = _shadowOffsetX
      override val shadowOffsetY: js.UndefOr[Number] = _shadowOffsetY
      override val color: js.UndefOr[Clr] = _color
      override val opacity: js.UndefOr[Number] = _opacity
