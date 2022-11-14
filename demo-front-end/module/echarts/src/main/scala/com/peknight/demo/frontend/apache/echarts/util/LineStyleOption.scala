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
