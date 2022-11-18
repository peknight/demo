package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{AreaStyleOption, ZRColor}

import scala.scalajs.js

trait SplitAreaOption extends js.Object:
  val show: js.UndefOr[Boolean] = js.undefined
  val interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = js.undefined
  val areaStyle: js.UndefOr[AreaStyleOption[js.Array[ZRColor]]] = js.undefined

object SplitAreaOption:
  def apply(show: js.UndefOr[Boolean] = js.undefined,
            interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = js.undefined,
            areaStyle: js.UndefOr[AreaStyleOption[js.Array[ZRColor]]] = js.undefined): SplitAreaOption =
    val _show: js.UndefOr[Boolean] = show
    val _interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = interval
    val _areaStyle: js.UndefOr[AreaStyleOption[js.Array[ZRColor]]] = areaStyle
    new SplitAreaOption:
      override val show: js.UndefOr[Boolean] = _show
      override val interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = _interval
      override val areaStyle: js.UndefOr[AreaStyleOption[js.Array[ZRColor]]] = _areaStyle

