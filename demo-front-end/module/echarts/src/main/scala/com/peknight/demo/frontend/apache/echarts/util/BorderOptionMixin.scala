package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.{CanvasLineCap, CanvasLineJoin, Number}

import scala.scalajs.js

trait BorderOptionMixin extends js.Object:
  val borderColor: js.UndefOr[ZRColor] = js.undefined
  val borderWidth: js.UndefOr[Number] = js.undefined
  val borderType: js.UndefOr[ZRLineType] = js.undefined
  val borderCap: js.UndefOr[CanvasLineCap] = js.undefined
  val borderJoin: js.UndefOr[CanvasLineJoin] = js.undefined
  val borderDashOffset: js.UndefOr[Number] = js.undefined
  val borderMiterLimit: js.UndefOr[Number] = js.undefined
