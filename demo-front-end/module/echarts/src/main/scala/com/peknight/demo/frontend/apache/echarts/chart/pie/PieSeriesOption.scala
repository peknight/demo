package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.`export`.SeriesInjectedOption
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{Number, clean}

import scala.scalajs.js

trait PieSeriesOption extends PieSeriesOptionInner with SeriesInjectedOption

object PieSeriesOption:
  def apply(id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            z: js.UndefOr[Number] = js.undefined,
            zlevel: js.UndefOr[Number] = js.undefined,
            coordinateSystem: js.UndefOr["cartesian2d" | "polar"] = js.undefined,
            roseType: js.UndefOr["radius" | "area"] = js.undefined,
            clockwise: js.UndefOr[Boolean] = js.undefined,
            startAngle: js.UndefOr[Number] = js.undefined,
            minAngle: js.UndefOr[Number] = js.undefined,
            minShowLabelAngle: js.UndefOr[Number] = js.undefined,
            selectedOffset: js.UndefOr[Number] = js.undefined,
            avoidLabelOverlap: js.UndefOr[Boolean] = js.undefined,
            percentPrecision: js.UndefOr[Number] = js.undefined,
            stillShowZeroSum: js.UndefOr[Boolean] = js.undefined,
            animationType: js.UndefOr["expansion" | "scale"] = js.undefined,
            animationTypeUpdate: js.UndefOr["transition" | "expansion"] = js.undefined,
            showEmptyCircle: js.UndefOr[Boolean] = js.undefined,
            emptyCircleStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = js.undefined,
            data: js.UndefOr[js.Array[OptionDataValueNumeric | js.Array[OptionDataValueNumeric] | PieDataItemOption]] = js.undefined,
            emphasis: js.UndefOr[PieEmphasisOption] = js.undefined): PieSeriesOption =
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _z: js.UndefOr[Number] = z
    val _zlevel: js.UndefOr[Number] = zlevel
    val _coordinateSystem: js.UndefOr["cartesian2d" | "polar"] = coordinateSystem
    val _roseType: js.UndefOr["radius" | "area"] = roseType
    val _clockwise: js.UndefOr[Boolean] = clockwise
    val _startAngle: js.UndefOr[Number] = startAngle
    val _minAngle: js.UndefOr[Number] = minAngle
    val _minShowLabelAngle: js.UndefOr[Number] = minShowLabelAngle
    val _selectedOffset: js.UndefOr[Number] = selectedOffset
    val _avoidLabelOverlap: js.UndefOr[Boolean] = avoidLabelOverlap
    val _percentPrecision: js.UndefOr[Number] = percentPrecision
    val _stillShowZeroSum: js.UndefOr[Boolean] = stillShowZeroSum
    val _animationType: js.UndefOr["expansion" | "scale"] = animationType
    val _animationTypeUpdate: js.UndefOr["transition" | "expansion"] = animationTypeUpdate
    val _showEmptyCircle: js.UndefOr[Boolean] = showEmptyCircle
    val _emptyCircleStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = emptyCircleStyle
    val _data: js.UndefOr[js.Array[OptionDataValueNumeric | js.Array[OptionDataValueNumeric] | PieDataItemOption]] = data
    val _emphasis: js.UndefOr[PieEmphasisOption] = emphasis
    val pieSeriesOption: PieSeriesOption = new PieSeriesOption:
      override val mainType: js.UndefOr[MainType] = "series"
      override val `type`: js.UndefOr[Type] = "pie"
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[NameType] = _name
      override val z: js.UndefOr[Number] = _z
      override val zlevel: js.UndefOr[Number] = _zlevel
      override val coordinateSystem: js.UndefOr[CoordinateSystemType] = _coordinateSystem
      override val roseType: js.UndefOr["radius" | "area"] = _roseType
      override val clockwise: js.UndefOr[Boolean] = _clockwise
      override val startAngle: js.UndefOr[Number] = _startAngle
      override val minAngle: js.UndefOr[Number] = _minAngle
      override val minShowLabelAngle: js.UndefOr[Number] = _minShowLabelAngle
      override val selectedOffset: js.UndefOr[Number] = _selectedOffset
      override val avoidLabelOverlap: js.UndefOr[Boolean] = _avoidLabelOverlap
      override val percentPrecision: js.UndefOr[Number] = _percentPrecision
      override val stillShowZeroSum: js.UndefOr[Boolean] = _stillShowZeroSum
      override val animationType: js.UndefOr["expansion" | "scale"] = _animationType
      override val animationTypeUpdate: js.UndefOr["transition" | "expansion"] = _animationTypeUpdate
      override val showEmptyCircle: js.UndefOr[Boolean] = _showEmptyCircle
      override val emptyCircleStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = _emptyCircleStyle
      override val data: js.UndefOr[DataType] = _data
      override val emphasis: js.UndefOr[PieEmphasisOption] = _emphasis
    pieSeriesOption.clean

