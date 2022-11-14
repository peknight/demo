package com.peknight.demo.frontend.apache.echarts.component.toolbox

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.ecomfe.zrender.core.BuiltinTextPosition

import scala.scalajs.js

trait IconTextStyleMixin extends js.Object:
  val textFill: js.UndefOr[String] = js.undefined
  val textBackgroundColor: js.UndefOr[ColorString | ImageMixin] = js.undefined
  val textPosition: js.UndefOr[BuiltinTextPosition | js.Array[Number | String]] = js.undefined
  val textAlign: js.UndefOr[HorizontalAlign] = js.undefined
  val textBorderRadius: js.UndefOr[Number | js.Array[Number]] = js.undefined
  val textPadding: js.UndefOr[Number | js.Array[Number]] = js.undefined
