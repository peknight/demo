package com.peknight.demo.frontend.apache.echarts.chart.helper

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.ZRColor

import scala.scalajs.js

trait RippleEffectOption extends js.Object:
  val period: js.UndefOr[Number] = js.undefined
  /**
   * Scale of ripple
   */
  val scale: js.UndefOr[Number] = js.undefined
  val brushType: js.UndefOr["fill" | "stroke"] = js.undefined
  val color: js.UndefOr[ZRColor] = js.undefined
  /**
   * ripple Number
   */
  val number: js.UndefOr[Number] = js.undefined

object RippleEffectOption:
  def apply(period: js.UndefOr[Number] = js.undefined,
            scale: js.UndefOr[Number] = js.undefined,
            brushType: js.UndefOr["fill" | "stroke"] = js.undefined,
            color: js.UndefOr[ZRColor] = js.undefined,
            number: js.UndefOr[Number] = js.undefined): RippleEffectOption =
    val _period: js.UndefOr[Number] = period
    val _scale: js.UndefOr[Number] = scale
    val _brushType: js.UndefOr["fill" | "stroke"] = brushType
    val _color: js.UndefOr[ZRColor] = color
    val _number: js.UndefOr[Number] = number
    new RippleEffectOption:
      override val period: js.UndefOr[Number] = _period
      override val scale: js.UndefOr[Number] = _scale
      override val brushType: js.UndefOr["fill" | "stroke"] = _brushType
      override val color: js.UndefOr[ZRColor] = _color
      override val number: js.UndefOr[Number] = _number