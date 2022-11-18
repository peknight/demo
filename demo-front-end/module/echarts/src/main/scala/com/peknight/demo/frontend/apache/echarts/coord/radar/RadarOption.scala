package com.peknight.demo.frontend.apache.echarts.coord.radar

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.coord.*
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait RadarOption extends js.Object with ComponentOption with CircleLayoutOptionMixin:
  type Type = String
  type MainType = "radar"
  type NameType = OptionName
  val startAngle: js.UndefOr[Number] = js.undefined
  val shape: js.UndefOr["polygon" | "circle"] = js.undefined
  val axisLine: js.UndefOr[AxisLineOption] = js.undefined
  val axisTick: js.UndefOr[AxisTickOption] = js.undefined
  val axisLabel: js.UndefOr[AxisLabelBaseOption] = js.undefined
  val splitLine: js.UndefOr[SplitLineOption] = js.undefined
  val splitArea: js.UndefOr[SplitAreaOption] = js.undefined
  val axisName: js.UndefOr[AxisNameMixin & LabelOption] = js.undefined
  val axisNameGap: js.UndefOr[Number] = js.undefined
  val triggerEvent: js.UndefOr[Boolean] = js.undefined
  val scale: js.UndefOr[Boolean] = js.undefined
  val splitNumber: js.UndefOr[Number] = js.undefined
  val boundaryGap: js.UndefOr[Boolean | (Number | String, Number | String)] = js.undefined
  val indicator: js.UndefOr[js.Array[RadarIndicatorOption]] = js.undefined

object RadarOption:
  def apply(`type`: js.UndefOr[String] = js.undefined,
            id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            z: js.UndefOr[Number] = js.undefined,
            zlevel: js.UndefOr[Number] = js.undefined,
            center: js.UndefOr[js.Array[Number | String]] = js.undefined,
            radius: js.UndefOr[js.Array[Number | String] | Number | String] = js.undefined,
            startAngle: js.UndefOr[Number] = js.undefined,
            shape: js.UndefOr["polygon" | "circle"] = js.undefined,
            axisLine: js.UndefOr[AxisLineOption] = js.undefined,
            axisTick: js.UndefOr[AxisTickOption] = js.undefined,
            axisLabel: js.UndefOr[AxisLabelBaseOption] = js.undefined,
            splitLine: js.UndefOr[SplitLineOption] = js.undefined,
            splitArea: js.UndefOr[SplitAreaOption] = js.undefined,
            axisName: js.UndefOr[AxisNameMixin & LabelOption] = js.undefined,
            axisNameGap: js.UndefOr[Number] = js.undefined,
            triggerEvent: js.UndefOr[Boolean] = js.undefined,
            scale: js.UndefOr[Boolean] = js.undefined,
            splitNumber: js.UndefOr[Number] = js.undefined,
            boundaryGap: js.UndefOr[Boolean | (Number | String, Number | String)] = js.undefined,
            indicator: js.UndefOr[js.Array[RadarIndicatorOption]] = js.undefined): RadarOption =
    val _type: js.UndefOr[String] = `type`
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _z: js.UndefOr[Number] = z
    val _zlevel: js.UndefOr[Number] = zlevel
    val _center: js.UndefOr[js.Array[Number | String]] = center
    val _radius: js.UndefOr[js.Array[Number | String] | Number | String] = radius
    val _startAngle: js.UndefOr[Number] = startAngle
    val _shape: js.UndefOr["polygon" | "circle"] = shape
    val _axisLine: js.UndefOr[AxisLineOption] = axisLine
    val _axisTick: js.UndefOr[AxisTickOption] = axisTick
    val _axisLabel: js.UndefOr[AxisLabelBaseOption] = axisLabel
    val _splitLine: js.UndefOr[SplitLineOption] = splitLine
    val _splitArea: js.UndefOr[SplitAreaOption] = splitArea
    val _axisName: js.UndefOr[AxisNameMixin & LabelOption] = axisName
    val _axisNameGap: js.UndefOr[Number] = axisNameGap
    val _triggerEvent: js.UndefOr[Boolean] = triggerEvent
    val _scale: js.UndefOr[Boolean] = scale
    val _splitNumber: js.UndefOr[Number] = splitNumber
    val _boundaryGap: js.UndefOr[Boolean | (Number | String, Number | String)] = boundaryGap
    val _indicator: js.UndefOr[js.Array[RadarIndicatorOption]] = indicator
    new RadarOption:
      override val mainType: js.UndefOr[MainType] = "radar"
      override val `type`: js.UndefOr[Type] = _type
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[NameType] = _name
      override val z: js.UndefOr[Number] = _z
      override val zlevel: js.UndefOr[Number] = _zlevel
      override val center: js.UndefOr[js.Array[Number | String]] = _center
      override val radius: js.UndefOr[js.Array[Number | String] | Number | String] = _radius
      override val startAngle: js.UndefOr[Number] = _startAngle
      override val shape: js.UndefOr["polygon" | "circle"] = _shape
      override val axisLine: js.UndefOr[AxisLineOption] = _axisLine
      override val axisTick: js.UndefOr[AxisTickOption] = _axisTick
      override val axisLabel: js.UndefOr[AxisLabelBaseOption] = _axisLabel
      override val splitLine: js.UndefOr[SplitLineOption] = _splitLine
      override val splitArea: js.UndefOr[SplitAreaOption] = _splitArea
      override val axisName: js.UndefOr[AxisNameMixin & LabelOption] = _axisName
      override val axisNameGap: js.UndefOr[Number] = _axisNameGap
      override val triggerEvent: js.UndefOr[Boolean] = _triggerEvent
      override val scale: js.UndefOr[Boolean] = _scale
      override val splitNumber: js.UndefOr[Number] = _splitNumber
      override val boundaryGap: js.UndefOr[Boolean | (Number | String, Number | String)] = _boundaryGap
      override val indicator: js.UndefOr[js.Array[RadarIndicatorOption]] = _indicator
