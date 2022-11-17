package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{LineStyleOption, ZRColor}

import scala.scalajs.js

trait AxisTickOption extends js.Object:
  val show: js.UndefOr[Boolean | "auto"] = js.undefined
  val inside: js.UndefOr[Boolean] = js.undefined
  val length: js.UndefOr[Number] = js.undefined
  val lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = js.undefined

object AxisTickOption:
  def apply(show: js.UndefOr[Boolean | "auto"] = js.undefined,
            inside: js.UndefOr[Boolean] = js.undefined,
            length: js.UndefOr[Number] = js.undefined,
            lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = js.undefined,
            alignWithLabel: js.UndefOr[Boolean] = js.undefined,
            interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = js.undefined
           ): AxisTickOption & AlignWithLabelMixin & IntervalMixin =
    val _show: js.UndefOr[Boolean | "auto"] = show
    val _inside: js.UndefOr[Boolean] = inside
    val _length: js.UndefOr[Number] = length
    val _lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = lineStyle
    val _alignWithLabel: js.UndefOr[Boolean] = alignWithLabel
    val _interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = interval
    new AxisTickOption with AlignWithLabelMixin with IntervalMixin:
      override val show: js.UndefOr[Boolean | "auto"] = _show
      override val inside: js.UndefOr[Boolean] = _inside
      override val length: js.UndefOr[Number] = _length
      override val lineStyle: js.UndefOr[LineStyleOption[ZRColor]] = _lineStyle
      override val alignWithLabel: js.UndefOr[Boolean] = _alignWithLabel
      override val interval: js.UndefOr["auto" | Number | js.Function2[Number, String, Boolean]] = _interval

