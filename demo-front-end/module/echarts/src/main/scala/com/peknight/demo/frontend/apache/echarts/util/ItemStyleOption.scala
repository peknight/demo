package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.{CanvasLineCap, CanvasLineJoin, Number}

import scala.scalajs.js

trait ItemStyleOption[TCbParams] extends js.Object with ShadowOptionMixin with BorderOptionMixin:
  val color: js.UndefOr[ZRColor | TCbParams | js.Function1[TCbParams, ZRColor]] = js.undefined
  val opacity: js.UndefOr[Number] = js.undefined
  val decal: js.UndefOr[DecalObject | "none"] = js.undefined
object ItemStyleOption:
  def apply[TCbParams](shadowBlur: js.UndefOr[Number] = js.undefined,
                       shadowColor: js.UndefOr[ColorString] = js.undefined,
                       shadowOffsetX: js.UndefOr[Number] = js.undefined,
                       shadowOffsetY: js.UndefOr[Number] = js.undefined,
                       borderColor: js.UndefOr[ZRColor] = js.undefined,
                       borderWidth: js.UndefOr[Number] = js.undefined,
                       borderType: js.UndefOr[ZRLineType] = js.undefined,
                       borderCap: js.UndefOr[CanvasLineCap] = js.undefined,
                       borderJoin: js.UndefOr[CanvasLineJoin] = js.undefined,
                       borderDashOffset: js.UndefOr[Number] = js.undefined,
                       borderMiterLimit: js.UndefOr[Number] = js.undefined,
                       color: js.UndefOr[ZRColor | TCbParams | js.Function1[TCbParams, ZRColor]] = js.undefined,
                       opacity: js.UndefOr[Number] = js.undefined,
                       decal: js.UndefOr[DecalObject | "none"] = js.undefined): ItemStyleOption[TCbParams] =
    val _shadowBlur: js.UndefOr[Number] = shadowBlur
    val _shadowColor: js.UndefOr[ColorString] = shadowColor
    val _shadowOffsetX: js.UndefOr[Number] = shadowOffsetX
    val _shadowOffsetY: js.UndefOr[Number] = shadowOffsetY
    val _borderColor: js.UndefOr[ZRColor] = borderColor
    val _borderWidth: js.UndefOr[Number] = borderWidth
    val _borderType: js.UndefOr[ZRLineType] = borderType
    val _borderCap: js.UndefOr[CanvasLineCap] = borderCap
    val _borderJoin: js.UndefOr[CanvasLineJoin] = borderJoin
    val _borderDashOffset: js.UndefOr[Number] = borderDashOffset
    val _borderMiterLimit: js.UndefOr[Number] = borderMiterLimit
    val _color: js.UndefOr[ZRColor | TCbParams | js.Function1[TCbParams, ZRColor]] = color
    val _opacity: js.UndefOr[Number] = opacity
    val _decal: js.UndefOr[DecalObject | "none"] = decal
    new ItemStyleOption[TCbParams]:
      override val shadowBlur: js.UndefOr[Number] = _shadowBlur
      override val shadowColor: js.UndefOr[ColorString] = _shadowColor
      override val shadowOffsetX: js.UndefOr[Number] = _shadowOffsetX
      override val shadowOffsetY: js.UndefOr[Number] = _shadowOffsetY
      override val borderColor: js.UndefOr[ZRColor] = _borderColor
      override val borderWidth: js.UndefOr[Number] = _borderWidth
      override val borderType: js.UndefOr[ZRLineType] = _borderType
      override val borderCap: js.UndefOr[CanvasLineCap] = _borderCap
      override val borderJoin: js.UndefOr[CanvasLineJoin] = _borderJoin
      override val borderDashOffset: js.UndefOr[Number] = _borderDashOffset
      override val borderMiterLimit: js.UndefOr[Number] = _borderMiterLimit
      override val color: js.UndefOr[ZRColor | TCbParams | js.Function1[TCbParams, ZRColor]] = _color
      override val opacity: js.UndefOr[Number] = _opacity
      override val decal: js.UndefOr[DecalObject | "none"] = _decal
