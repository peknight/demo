package com.peknight.demo.frontend.apache.echarts.chart.pie

import com.peknight.demo.frontend.apache.echarts.`export`.SeriesInjectedOption
import com.peknight.demo.frontend.apache.echarts.component.marker.{MarkAreaOption, MarkLineOption, MarkPointOption}
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{Number, clean}
import com.peknight.demo.frontend.ecomfe.zrender.animation.AnimationEasing

import scala.scalajs.js

trait PieSeriesOption extends PieSeriesOptionInner with SeriesInjectedOption

object PieSeriesOption:
  def apply(id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            z: js.UndefOr[Number] = js.undefined,
            zlevel: js.UndefOr[Number] = js.undefined,
            animation: js.UndefOr[Boolean] = js.undefined,
            animationThreshold: js.UndefOr[Number] = js.undefined,
            animationDuration: js.UndefOr[Number | AnimationDurationCallback] = js.undefined,
            animationEasing: js.UndefOr[AnimationEasing] = js.undefined,
            animationDelay: js.UndefOr[Number | AnimationDelayCallback] = js.undefined,
            animationDurationUpdate: js.UndefOr[Number | AnimationDurationCallback] = js.undefined,
            animationEasingUpdate: js.UndefOr[AnimationEasing] = js.undefined,
            animationDelayUpdate: js.UndefOr[Number | AnimationDelayCallback] = js.undefined,
            color: js.UndefOr[ZRColor | js.Array[ZRColor]] = js.undefined,
            colorLayer: js.UndefOr[js.Array[js.Array[ZRColor]]] = js.undefined,
            emphasis: js.UndefOr[PieStateOption[PieCallbackDataParams] & PieEmphasisMixin & StatesEmphasisOptionMixin] = js.undefined,
            select: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any & StatesSelectOptionMixin] = js.undefined,
            blur: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any] = js.undefined,
            silent: js.UndefOr[Boolean] = js.undefined,
            blendMode: js.UndefOr[String] = js.undefined,
            cursor: js.UndefOr[String] = js.undefined,
            dataGroupId: js.UndefOr[OptionId] = js.undefined,
            data: js.UndefOr[js.Array[OptionDataValueNumeric | js.Array[OptionDataValueNumeric] | PieDataItemOption]] = js.undefined,
            colorBy: js.UndefOr[ColorBy] = js.undefined,
            legendHoverLink: js.UndefOr[Boolean] = js.undefined,
            progressive: js.UndefOr[Number | false] = js.undefined,
            progressiveThreshold: js.UndefOr[Number] = js.undefined,
            progressiveChunkMode: js.UndefOr["mod"] = js.undefined,
            coordinateSystem: js.UndefOr[String] = js.undefined,
            hoverLayerThreshold: js.UndefOr[Number] = js.undefined,
            seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = js.undefined,
            labelLine: js.UndefOr[PieLabelLineOption] = js.undefined,
            labelLayout: js.UndefOr[LabelLayoutOption | LabelLayoutOptionCallback] = js.undefined,
            stateAnimation: js.UndefOr[AnimationOption] = js.undefined,
            universalTransition: js.UndefOr[Boolean | UniversalTransitionOption] = js.undefined,
            selectedMap: js.UndefOr[js.Dictionary[Boolean] | "all"] = js.undefined,
            selectedMode: js.UndefOr["single" | "multiple" | "series" | Boolean] = js.undefined,
            itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = js.undefined,
            label: js.UndefOr[PieLabelOption] = js.undefined,
            center: js.UndefOr[js.Array[Number | String]] = js.undefined,
            radius: js.UndefOr[js.Array[Number | String] | Number | String] = js.undefined,
            width: js.UndefOr[Number | String] = js.undefined,
            height: js.UndefOr[Number | String] = js.undefined,
            top: js.UndefOr[Number | String] = js.undefined,
            right: js.UndefOr[Number | String] = js.undefined,
            bottom: js.UndefOr[Number | String] = js.undefined,
            left: js.UndefOr[Number | String] = js.undefined,
            datasetIndex: js.UndefOr[Number] = js.undefined,
            datasetId: js.UndefOr[String | Number] = js.undefined,
            sourceHeader: js.UndefOr[OptionSourceHeader] = js.undefined,
            dimensions: js.UndefOr[js.Array[DimensionDefinitionLoose]] = js.undefined,
            encode: js.UndefOr[OptionEncode] = js.undefined,
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
            markArea: js.UndefOr[MarkAreaOption] = js.undefined,
            markLine: js.UndefOr[MarkLineOption] = js.undefined,
            markPoint: js.UndefOr[MarkPointOption] = js.undefined,
            tooltip: js.UndefOr[SeriesTooltipOption] = js.undefined): PieSeriesOption =
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _z: js.UndefOr[Number] = z
    val _zlevel: js.UndefOr[Number] = zlevel
    val _animation: js.UndefOr[Boolean] = animation
    val _animationThreshold: js.UndefOr[Number] = animationThreshold
    val _animationDuration: js.UndefOr[Number | AnimationDurationCallback] = animationDuration
    val _animationEasing: js.UndefOr[AnimationEasing] = animationEasing
    val _animationDelay: js.UndefOr[Number | AnimationDelayCallback] = animationDelay
    val _animationDurationUpdate: js.UndefOr[Number | AnimationDurationCallback] = animationDurationUpdate
    val _animationEasingUpdate: js.UndefOr[AnimationEasing] = animationEasingUpdate
    val _animationDelayUpdate: js.UndefOr[Number | AnimationDelayCallback] = animationDelayUpdate
    val _color: js.UndefOr[ZRColor | js.Array[ZRColor]] = color
    val _colorLayer: js.UndefOr[js.Array[js.Array[ZRColor]]] = colorLayer
    val _emphasis: js.UndefOr[PieStateOption[PieCallbackDataParams] & PieEmphasisMixin & StatesEmphasisOptionMixin] = emphasis
    val _select: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any & StatesSelectOptionMixin] = select
    val _blur: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any] = blur
    val _silent: js.UndefOr[Boolean] = silent
    val _blendMode: js.UndefOr[String] = blendMode
    val _cursor: js.UndefOr[String] = cursor
    val _dataGroupId: js.UndefOr[OptionId] = dataGroupId
    val _data: js.UndefOr[js.Array[OptionDataValueNumeric | js.Array[OptionDataValueNumeric] | PieDataItemOption]] = data
    val _colorBy: js.UndefOr[ColorBy] = colorBy
    val _legendHoverLink: js.UndefOr[Boolean] = legendHoverLink
    val _progressive: js.UndefOr[Number | false] = progressive
    val _progressiveThreshold: js.UndefOr[Number] = progressiveThreshold
    val _progressiveChunkMode: js.UndefOr["mod"] = progressiveChunkMode
    val _coordinateSystem: js.UndefOr[String] = coordinateSystem
    val _hoverLayerThreshold: js.UndefOr[Number] = hoverLayerThreshold
    val _seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = seriesLayoutBy
    val _labelLine: js.UndefOr[PieLabelLineOption] = labelLine
    val _labelLayout: js.UndefOr[LabelLayoutOption | LabelLayoutOptionCallback] = labelLayout
    val _stateAnimation: js.UndefOr[AnimationOption] = stateAnimation
    val _universalTransition: js.UndefOr[Boolean | UniversalTransitionOption] = universalTransition
    val _selectedMap: js.UndefOr[js.Dictionary[Boolean] | "all"] = selectedMap
    val _selectedMode: js.UndefOr["single" | "multiple" | "series" | Boolean] = selectedMode
    val _itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = itemStyle
    val _label: js.UndefOr[PieLabelOption] = label
    val _center: js.UndefOr[js.Array[Number | String]] = center
    val _radius: js.UndefOr[js.Array[Number | String] | Number | String] = radius
    val _width: js.UndefOr[Number | String] = width
    val _height: js.UndefOr[Number | String] = height
    val _top: js.UndefOr[Number | String] = top
    val _right: js.UndefOr[Number | String] = right
    val _bottom: js.UndefOr[Number | String] = bottom
    val _left: js.UndefOr[Number | String] = left
    val _datasetIndex: js.UndefOr[Number] = datasetIndex
    val _datasetId: js.UndefOr[String | Number] = datasetId
    val _sourceHeader: js.UndefOr[OptionSourceHeader] = sourceHeader
    val _dimensions: js.UndefOr[js.Array[DimensionDefinitionLoose]] = dimensions
    val _encode: js.UndefOr[OptionEncode] = encode
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
    val _markArea: js.UndefOr[MarkAreaOption] = markArea
    val _markLine: js.UndefOr[MarkLineOption] = markLine
    val _markPoint: js.UndefOr[MarkPointOption] = markPoint
    val _tooltip: js.UndefOr[SeriesTooltipOption] = tooltip
    val pieSeriesOption: PieSeriesOption = new PieSeriesOption:
      override val mainType: js.UndefOr[MainType] = "series"
      override val `type`: js.UndefOr[Type] = "pie"
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[NameType] = _name
      override val z: js.UndefOr[Number] = _z
      override val zlevel: js.UndefOr[Number] = _zlevel
      override val animation: js.UndefOr[Boolean] = _animation
      override val animationThreshold: js.UndefOr[Number] = _animationThreshold
      override val animationDuration: js.UndefOr[Number | AnimationDurationCallback] = _animationDuration
      override val animationEasing: js.UndefOr[AnimationEasing] = _animationEasing
      override val animationDelay: js.UndefOr[Number | AnimationDelayCallback] = _animationDelay
      override val animationDurationUpdate: js.UndefOr[Number | AnimationDurationCallback] = _animationDurationUpdate
      override val animationEasingUpdate: js.UndefOr[AnimationEasing] = _animationEasingUpdate
      override val animationDelayUpdate: js.UndefOr[Number | AnimationDelayCallback] = _animationDelayUpdate
      override val color: js.UndefOr[ZRColor | js.Array[ZRColor]] = _color
      override val colorLayer: js.UndefOr[js.Array[js.Array[ZRColor]]] = _colorLayer
      override val emphasis: js.UndefOr[PieStateOption[PieCallbackDataParams] & PieEmphasisMixin & StatesEmphasisOptionMixin] = _emphasis
      override val select: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any & StatesSelectOptionMixin] = _select
      override val blur: js.UndefOr[PieStateOption[PieCallbackDataParams] & js.Any] = _blur
      override val silent: js.UndefOr[Boolean] = _silent
      override val blendMode: js.UndefOr[String] = _blendMode
      override val cursor: js.UndefOr[String] = _cursor
      override val dataGroupId: js.UndefOr[OptionId] = _dataGroupId
      override val data: js.UndefOr[DataType] = _data
      override val colorBy: js.UndefOr[ColorBy] = _colorBy
      override val legendHoverLink: js.UndefOr[Boolean] = _legendHoverLink
      override val progressive: js.UndefOr[Number | false] = _progressive
      override val progressiveThreshold: js.UndefOr[Number] = _progressiveThreshold
      override val progressiveChunkMode: js.UndefOr["mod"] = _progressiveChunkMode
      override val coordinateSystem: js.UndefOr[CoordinateSystemType] = _coordinateSystem
      override val hoverLayerThreshold: js.UndefOr[Number] = _hoverLayerThreshold
      override val seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = _seriesLayoutBy
      override val labelLine: js.UndefOr[PieLabelLineOption] = _labelLine
      override val labelLayout: js.UndefOr[LabelLayoutOption | LabelLayoutOptionCallback] = _labelLayout
      override val stateAnimation: js.UndefOr[AnimationOption] = _stateAnimation
      override val universalTransition: js.UndefOr[Boolean | UniversalTransitionOption] = _universalTransition
      override val selectedMap: js.UndefOr[js.Dictionary[Boolean] | "all"] = _selectedMap
      override val selectedMode: js.UndefOr["single" | "multiple" | "series" | Boolean] = _selectedMode
      override val itemStyle: js.UndefOr[PieItemStyleOption[PieCallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[PieLabelOption] = _label
      override val center: js.UndefOr[js.Array[Number | String]] = _center
      override val radius: js.UndefOr[js.Array[Number | String] | Number | String] = _radius
      override val width: js.UndefOr[Number | String] = _width
      override val height: js.UndefOr[Number | String] = _height
      override val top: js.UndefOr[Number | String] = _top
      override val right: js.UndefOr[Number | String] = _right
      override val bottom: js.UndefOr[Number | String] = _bottom
      override val left: js.UndefOr[Number | String] = _left
      override val datasetIndex: js.UndefOr[Number] = _datasetIndex
      override val datasetId: js.UndefOr[String | Number] = _datasetId
      override val sourceHeader: js.UndefOr[OptionSourceHeader] = _sourceHeader
      override val dimensions: js.UndefOr[js.Array[DimensionDefinitionLoose]] = _dimensions
      override val encode: js.UndefOr[OptionEncode] = _encode
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
      override val markArea: js.UndefOr[MarkAreaOption] = _markArea
      override val markLine: js.UndefOr[MarkLineOption] = _markLine
      override val markPoint: js.UndefOr[MarkPointOption] = _markPoint
      override val tooltip: js.UndefOr[SeriesTooltipOption] = _tooltip
    pieSeriesOption.clean

