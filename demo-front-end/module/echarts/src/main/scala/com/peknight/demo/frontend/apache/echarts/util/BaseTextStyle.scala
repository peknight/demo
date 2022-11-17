package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

// 手工提取
trait BaseTextStyle extends js.Object:
  type ColorType >: String
  val color: js.UndefOr[ColorType] = js.undefined
  val fontStyle: js.UndefOr[ZRFontStyle] = js.undefined
  val fontWeight: js.UndefOr[ZRFontWeight] = js.undefined
  val fontFamily: js.UndefOr[String] = js.undefined
  val fontSize: js.UndefOr[Number | String] = js.undefined
