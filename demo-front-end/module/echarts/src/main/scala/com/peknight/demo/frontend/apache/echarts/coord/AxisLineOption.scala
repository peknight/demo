package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{LineStyleOption, ZRColor}

import scala.scalajs.js

trait AxisLineOption extends js.Object:
  val show: js.UndefOr[Boolean | "auto"] = js.undefined
  val onZero: js.UndefOr[Boolean] = js.undefined
  val onZeroAxisIndex: js.UndefOr[Number] = js.undefined
  val symbol: js.UndefOr[String | (String, String)] = js.undefined
  val symbolSize: js.UndefOr[js.Array[Number]] = js.undefined
  val symbolOffset: js.UndefOr[String | Number | js.Array[String | Number]] = js.undefined
  val lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = js.undefined

object AxisLineOption:
  def apply(show: js.UndefOr[Boolean | "auto"] = js.undefined,
            onZero: js.UndefOr[Boolean] = js.undefined,
            onZeroAxisIndex: js.UndefOr[Number] = js.undefined,
            symbol: js.UndefOr[String | (String, String)] = js.undefined,
            symbolSize: js.UndefOr[js.Array[Number]] = js.undefined,
            symbolOffset: js.UndefOr[String | Number | js.Array[String | Number]] = js.undefined,
            lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = js.undefined): AxisLineOption =
    val _show: js.UndefOr[Boolean | "auto"] = show
    val _onZero: js.UndefOr[Boolean] = onZero
    val _onZeroAxisIndex: js.UndefOr[Number] = onZeroAxisIndex
    val _symbol: js.UndefOr[String | (String, String)] = symbol
    val _symbolSize: js.UndefOr[js.Array[Number]] = symbolSize
    val _symbolOffset: js.UndefOr[String | Number | js.Array[String | Number]] = symbolOffset
    val _lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = lineStyle
    new AxisLineOption:
      override val show: js.UndefOr[Boolean | "auto"] = _show
      override val onZero: js.UndefOr[Boolean] = _onZero
      override val onZeroAxisIndex: js.UndefOr[Number] = _onZeroAxisIndex
      override val symbol: js.UndefOr[String | (String, String)] = _symbol
      override val symbolSize: js.UndefOr[js.Array[Number]] = _symbolSize
      override val symbolOffset: js.UndefOr[String | Number | js.Array[String | Number]] = _symbolOffset
      override val lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = _lineStyle
