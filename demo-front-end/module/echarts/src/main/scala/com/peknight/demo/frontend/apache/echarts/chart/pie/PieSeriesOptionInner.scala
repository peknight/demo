package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.*

import scala.scalajs.js

trait PieSeriesOptionInner extends js.Object
  with SeriesOption[PieStateOption[PieCallbackDataParams], ExtraStateOption] with PieStateOption[PieCallbackDataParams]
  with CircleLayoutOptionMixin with BoxLayoutOptionMixin with SeriesEncodeOptionMixin:
  type Type = "pie"
  type CoordinateSystemType = String
  type DataType = js.Array[OptionDataValueNumeric | js.Array[OptionDataValueNumeric] | PieDataItemOption]
  val roseType: js.UndefOr["radius" | "area"] = js.undefined
  val clockwise: js.UndefOr[Boolean] = js.undefined
  val startAngle: js.UndefOr[Number] = js.undefined
  val minAngle: js.UndefOr[Number] = js.undefined
  val minShowLabelAngle: js.UndefOr[Number] = js.undefined
  val selectedOffset: js.UndefOr[Number] = js.undefined
  val avoidLabelOverlap: js.UndefOr[Boolean] = js.undefined
  val percentPrecision: js.UndefOr[Number] = js.undefined
  val stillShowZeroSum: js.UndefOr[Boolean] = js.undefined
  val animationType: js.UndefOr["expansion" | "scale"] = js.undefined
  val animationTypeUpdate: js.UndefOr["transition" | "expansion"] = js.undefined
  val showEmptyCircle: js.UndefOr[Boolean] = js.undefined
  val emptyCircleStyle: js.UndefOr[PieItemStyleOption[_]] = js.undefined
  val emphasis: js.UndefOr[EmphasisMixin] = js.undefined
  override val seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = js.undefined
  override val labelLine: js.UndefOr[PieLabelLineOption] = js.undefined
