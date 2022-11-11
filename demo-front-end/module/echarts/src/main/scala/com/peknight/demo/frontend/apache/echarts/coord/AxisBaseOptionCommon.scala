package com.peknight.demo.frontend.apache.echarts.coord

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.{AnimationOptionMixin, CommonAxisPointerOption, ComponentOption, ScaleDataValue}

import scala.scalajs.js

trait AxisBaseOptionCommon extends js.Object with ComponentOption with AnimationOptionMixin:
  type AxisType <: OptionAxisType
  type AxisLabelType <: AxisLabelBaseOption
  type AxisTickType <: AxisTickOption
  type Type = AxisType
  type NameType = String
  val show: js.UndefOr[Boolean] = js.undefined
  val inverse: js.UndefOr[Boolean] = js.undefined
  val nameLocation: js.UndefOr["start" | "middle" | "end"] = js.undefined
  val nameRotate: js.UndefOr[Number] = js.undefined
  val nameTruncate: js.UndefOr[TruncateMixin] = js.undefined
  val nameTextStyle: js.UndefOr[AxisNameTextStyleOption] = js.undefined
  val nameGap: js.UndefOr[Number] = js.undefined
  val silent: js.UndefOr[Boolean] = js.undefined
  val triggerEvent: js.UndefOr[Boolean] = js.undefined
  val tooltip: js.UndefOr[ShowMixin] = js.undefined
  val axisLabel: js.UndefOr[AxisLabelType] = js.undefined
  val axisPointer: js.UndefOr[CommonAxisPointerOption] = js.undefined
  val axisLine: js.UndefOr[AxisLineOption] = js.undefined
  val axisTick: js.UndefOr[AxisTickType] = js.undefined
  val minorTick: js.UndefOr[MinorTickOption] = js.undefined
  val splitLine: js.UndefOr[SplitLineOption] = js.undefined
  val minorSplitLine: js.UndefOr[MinorSplitLineOption] = js.undefined
  val splitArea: js.UndefOr[SplitAreaOption] = js.undefined
  /**
   * Min value of the axis. can be:
   * + ScaleDataValue
   * + "dataMin": use the min value in data.
   * + null/undefined: auto decide min value (consider pretty look and boundaryGap).
   */
  val min: js.UndefOr[ScaleDataValue | "dataMin" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = js.undefined
  /**
   * Max value of the axis. can be:
   * + ScaleDataValue
   * + "dataMax": use the max value in data.
   * + null/undefined: auto decide max value (consider pretty look and boundaryGap).
   */
  val max: js.UndefOr[ScaleDataValue | "dataMax" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = js.undefined
