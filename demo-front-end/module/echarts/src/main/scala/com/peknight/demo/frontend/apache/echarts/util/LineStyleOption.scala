package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.{CanvasLineCap, CanvasLineJoin, Number}

import scala.scalajs.js

trait LineStyleOption[Clr] extends ShadowOptionMixin:
  type WidthType >: Number
  val width: js.UndefOr[WidthType] = js.undefined
  val color: js.UndefOr[Clr] = js.undefined
  val opacity: js.UndefOr[Number] = js.undefined
  val `type`: js.UndefOr[ZRLineType] = js.undefined
  val cap: js.UndefOr[CanvasLineCap] = js.undefined
  val join: js.UndefOr[CanvasLineJoin] = js.undefined
  val dashOffset: js.UndefOr[Number] = js.undefined
  val miterLimit: js.UndefOr[Number] = js.undefined

object LineStyleOption:
  def apply[Clr](shadowBlur: js.UndefOr[Number] = js.undefined,
                 shadowColor: js.UndefOr[ColorString] = js.undefined,
                 shadowOffsetX: js.UndefOr[Number] = js.undefined,
                 shadowOffsetY: js.UndefOr[Number] = js.undefined,
                 width: js.UndefOr[Number] = js.undefined,
                 color: js.UndefOr[Clr] = js.undefined,
                 opacity: js.UndefOr[Number] = js.undefined,
                 `type`: js.UndefOr[ZRLineType] = js.undefined,
                 cap: js.UndefOr[CanvasLineCap] = js.undefined,
                 join: js.UndefOr[CanvasLineJoin] = js.undefined,
                 dashOffset: js.UndefOr[Number] = js.undefined,
                 miterLimit: js.UndefOr[Number] = js.undefined): LineStyleOption[Clr] =
    val _shadowBlur: js.UndefOr[Number] = shadowBlur
    val _shadowColor: js.UndefOr[ColorString] = shadowColor
    val _shadowOffsetX: js.UndefOr[Number] = shadowOffsetX
    val _shadowOffsetY: js.UndefOr[Number] = shadowOffsetY
    val _width: js.UndefOr[Number] = width
    val _color: js.UndefOr[Clr] = color
    val _opacity: js.UndefOr[Number] = opacity
    val _type: js.UndefOr[ZRLineType] = `type`
    val _cap: js.UndefOr[CanvasLineCap] = cap
    val _join: js.UndefOr[CanvasLineJoin] = join
    val _dashOffset: js.UndefOr[Number] = dashOffset
    val _miterLimit: js.UndefOr[Number] = miterLimit
    new LineStyleOption[Clr]:
      type WidthType = Number
      override val shadowBlur: js.UndefOr[Number] = _shadowBlur
      override val shadowColor: js.UndefOr[ColorString] = _shadowColor
      override val shadowOffsetX: js.UndefOr[Number] = _shadowOffsetX
      override val shadowOffsetY: js.UndefOr[Number] = _shadowOffsetY
      override val width: js.UndefOr[WidthType] = _width
      override val color: js.UndefOr[Clr] = _color
      override val opacity: js.UndefOr[Number] = _opacity
      override val `type`: js.UndefOr[ZRLineType] = _type
      override val cap: js.UndefOr[CanvasLineCap] = _cap
      override val join: js.UndefOr[CanvasLineJoin] = _join
      override val dashOffset: js.UndefOr[Number] = _dashOffset
      override val miterLimit: js.UndefOr[Number] = _miterLimit
