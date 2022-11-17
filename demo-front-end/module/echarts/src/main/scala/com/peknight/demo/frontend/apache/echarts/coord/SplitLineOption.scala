package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{LineStyleOption, ZRColor}

import scala.scalajs.js

trait SplitLineOption extends js.Object:
  val show: js.UndefOr[Boolean] = js.undefined
  val interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = js.undefined
  val lineStyle: js.UndefOr[LineStyleOption[ZRColor | js.Array[ZRColor]]] = js.undefined

object SplitLineOption:
  def apply(show: js.UndefOr[Boolean] = js.undefined,
            interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = js.undefined,
            lineStyle: js.UndefOr[LineStyleOption[ZRColor | js.Array[ZRColor]]] = js.undefined): SplitLineOption =
    val _show: js.UndefOr[Boolean] = show
    val _interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = interval
    val _lineStyle: js.UndefOr[LineStyleOption[ZRColor | js.Array[ZRColor]]] = lineStyle
    new SplitLineOption:
      override val show: js.UndefOr[Boolean] = _show
      override val interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = _interval
      override val lineStyle: js.UndefOr[LineStyleOption[ZRColor | js.Array[ZRColor]]] = _lineStyle
