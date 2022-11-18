package com.peknight.demo.frontend.apache.echarts.coord.radar

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.ColorString

import scala.scalajs.js

trait RadarIndicatorOption extends js.Object:
  val name: js.UndefOr[String] = js.undefined
  // @deprecated Use `name` instead.
  // val text: js.UndefOr[String] = js.undefined
  val min: js.UndefOr[Number] = js.undefined
  val max: js.UndefOr[Number] = js.undefined
  val color: js.UndefOr[ColorString] = js.undefined
  val axisType: js.UndefOr["value" | "log"] = js.undefined

object RadarIndicatorOption:
  def apply(name: js.UndefOr[String] = js.undefined,
            min: js.UndefOr[Number] = js.undefined,
            max: js.UndefOr[Number] = js.undefined,
            color: js.UndefOr[ColorString] = js.undefined,
            axisType: js.UndefOr["value" | "log"] = js.undefined): RadarIndicatorOption =
    val _name: js.UndefOr[String] = name
    val _min: js.UndefOr[Number] = min
    val _max: js.UndefOr[Number] = max
    val _color: js.UndefOr[ColorString] = color
    val _axisType: js.UndefOr["value" | "log"] = axisType
    new RadarIndicatorOption:
      override val name: js.UndefOr[String] = _name
      override val min: js.UndefOr[Number] = _min
      override val max: js.UndefOr[Number] = _max
      override val color: js.UndefOr[ColorString] = _color
      override val axisType: js.UndefOr["value" | "log"] = _axisType

