package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{ColorString, ItemStyleOption}

import scala.scalajs.js

trait PieItemStyleOption[TCbParams] extends ItemStyleOption[TCbParams]:
  val borderRadius: js.UndefOr[js.Array[Number | String] | Number | String] = js.undefined

object PieItemStyleOption:
  def apply[TCbParams](shadowBlur: js.UndefOr[Number] = js.undefined,
                       shadowColor: js.UndefOr[ColorString] = js.undefined,
                       shadowOffsetX: js.UndefOr[Number] = js.undefined,
                       shadowOffsetY: js.UndefOr[Number] = js.undefined): PieItemStyleOption[TCbParams] =
    val _shadowBlur: js.UndefOr[Number] = shadowBlur
    val _shadowColor: js.UndefOr[ColorString] = shadowColor
    val _shadowOffsetX: js.UndefOr[Number] = shadowOffsetX
    val _shadowOffsetY: js.UndefOr[Number] = shadowOffsetY
    new PieItemStyleOption[TCbParams]:
      override val shadowBlur: js.UndefOr[Number] = _shadowBlur
      override val shadowColor: js.UndefOr[ColorString] = _shadowColor
      override val shadowOffsetX: js.UndefOr[Number] = _shadowOffsetX
      override val shadowOffsetY: js.UndefOr[Number] = _shadowOffsetY
