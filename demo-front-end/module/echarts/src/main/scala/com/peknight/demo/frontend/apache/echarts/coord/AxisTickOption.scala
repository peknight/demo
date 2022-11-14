package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{LineStyleOption, ZRColor}

import scala.scalajs.js

trait AxisTickOption extends js.Object:
  val show: js.UndefOr[Boolean | "auto"] = js.undefined
  val inside: js.UndefOr[Boolean] = js.undefined
  val length: js.UndefOr[Number] = js.undefined
  val lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = js.undefined
