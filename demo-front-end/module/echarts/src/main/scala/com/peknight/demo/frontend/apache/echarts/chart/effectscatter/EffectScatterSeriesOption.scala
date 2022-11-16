package com.peknight.demo.frontend.apache.echarts.chart.effectscatter

import com.peknight.demo.frontend.apache.echarts.`export`.SeriesInjectedOption
import com.peknight.demo.frontend.apache.echarts.chart.helper.{DefaultEmphasisFocusScaleMixin, RippleEffectOption}
import com.peknight.demo.frontend.apache.echarts.component.marker.{MarkAreaOption, MarkLineOption, MarkPointOption}
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.apache.echarts.{Number, clean}
import com.peknight.demo.frontend.ecomfe.zrender.animation.AnimationEasing

import scala.scalajs.js

trait EffectScatterSeriesOption extends EffectScatterSeriesOptionInner with SeriesInjectedOption

object EffectScatterSeriesOption:
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
            emphasis: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & DefaultEmphasisFocusScaleMixin & StatesEmphasisOptionMixin] = js.undefined,
            select: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = js.undefined,
            blur: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any] = js.undefined,
            silent: js.UndefOr[Boolean] = js.undefined,
            blendMode: js.UndefOr[String] = js.undefined,
            cursor: js.UndefOr[String] = js.undefined,
            dataGroupId: js.UndefOr[OptionId] = js.undefined,
            data: js.UndefOr[js.Array[EffectScatterDataItemOption | ScatterDataValue]] = js.undefined,
            colorBy: js.UndefOr[ColorBy] = js.undefined,
            legendHoverLink: js.UndefOr[Boolean] = js.undefined,
            progressive: js.UndefOr[Number | false] = js.undefined,
            progressiveThreshold: js.UndefOr[Number] = js.undefined,
            progressiveChunkMode: js.UndefOr["mod"] = js.undefined,
            coordinateSystem: js.UndefOr[String] = js.undefined,
            hoverLayerThreshold: js.UndefOr[Number] = js.undefined,
            seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = js.undefined,
            labelLine: js.UndefOr[LabelLineOption] = js.undefined,
            labelLayout: js.UndefOr[LabelLayoutOption | LabelLayoutOptionCallback] = js.undefined,
            stateAnimation: js.UndefOr[AnimationOption] = js.undefined,
            universalTransition: js.UndefOr[Boolean | UniversalTransitionOption] = js.undefined,
            selectedMap: js.UndefOr[js.Dictionary[Boolean] | "all"] = js.undefined,
            selectedMode: js.UndefOr["single" | "multiple" | "series" | Boolean] = js.undefined,
            itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = js.undefined,
            label: js.UndefOr[SeriesLabelOption] = js.undefined,
            xAxisIndex: js.UndefOr[Number] = js.undefined,
            yAxisIndex: js.UndefOr[Number] = js.undefined,
            xAxisId: js.UndefOr[String] = js.undefined,
            yAxisId: js.UndefOr[String] = js.undefined,
            polarIndex: js.UndefOr[Number] = js.undefined,
            polarId: js.UndefOr[String] = js.undefined,
            calendarIndex: js.UndefOr[Number] = js.undefined,
            calendarId: js.UndefOr[String] = js.undefined,
            geoIndex: js.UndefOr[Number] = js.undefined,
            geoId: js.UndefOr[String] = js.undefined,
            singleAxisIndex: js.UndefOr[Number] = js.undefined,
            singleAxisId: js.UndefOr[String] = js.undefined,
            symbol: js.UndefOr[String | SymbolCallback[CallbackDataParams]] = js.undefined,
            symbolSize: js.UndefOr[Number | js.Array[Number] | SymbolSizeCallback[CallbackDataParams]] = js.undefined,
            symbolRotate: js.UndefOr[Number | SymbolRotateCallback[CallbackDataParams]] = js.undefined,
            symbolKeepAspect: js.UndefOr[Boolean] = js.undefined,
            symbolOffset: js.UndefOr[String | Number | js.Array[String | Number] | SymbolOffsetCallback[CallbackDataParams]] = js.undefined,
            datasetIndex: js.UndefOr[Number] = js.undefined,
            datasetId: js.UndefOr[String | Number] = js.undefined,
            sourceHeader: js.UndefOr[OptionSourceHeader] = js.undefined,
            dimensions: js.UndefOr[js.Array[DimensionDefinitionLoose]] = js.undefined,
            encode: js.UndefOr[OptionEncode] = js.undefined,
            effectType: js.UndefOr["ripple"] = js.undefined,
            showEffectOn: js.UndefOr["render" | "emphasis"] = js.undefined,
            clip: js.UndefOr[Boolean] = js.undefined,
            rippleEffect: js.UndefOr[RippleEffectOption] = js.undefined,
            markArea: js.UndefOr[MarkAreaOption] = js.undefined,
            markLine: js.UndefOr[MarkLineOption] = js.undefined,
            markPoint: js.UndefOr[MarkPointOption] = js.undefined,
            tooltip: js.UndefOr[SeriesTooltipOption] = js.undefined): EffectScatterSeriesOption =
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
    val _emphasis: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & DefaultEmphasisFocusScaleMixin & StatesEmphasisOptionMixin] = emphasis
    val _select: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = select
    val _blur: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any] = blur
    val _silent: js.UndefOr[Boolean] = silent
    val _blendMode: js.UndefOr[String] = blendMode
    val _cursor: js.UndefOr[String] = cursor
    val _dataGroupId: js.UndefOr[OptionId] = dataGroupId
    val _data: js.UndefOr[js.Array[EffectScatterDataItemOption | ScatterDataValue]] = data
    val _colorBy: js.UndefOr[ColorBy] = colorBy
    val _legendHoverLink: js.UndefOr[Boolean] = legendHoverLink
    val _progressive: js.UndefOr[Number | false] = progressive
    val _progressiveThreshold: js.UndefOr[Number] = progressiveThreshold
    val _progressiveChunkMode: js.UndefOr["mod"] = progressiveChunkMode
    val _coordinateSystem: js.UndefOr[String] = coordinateSystem
    val _hoverLayerThreshold: js.UndefOr[Number] = hoverLayerThreshold
    val _seriesLayoutBy: js.UndefOr[SeriesLayoutBy] = seriesLayoutBy
    val _labelLine: js.UndefOr[LabelLineOption] = labelLine
    val _labelLayout: js.UndefOr[LabelLayoutOption | LabelLayoutOptionCallback] = labelLayout
    val _stateAnimation: js.UndefOr[AnimationOption] = stateAnimation
    val _universalTransition: js.UndefOr[Boolean | UniversalTransitionOption] = universalTransition
    val _selectedMap: js.UndefOr[js.Dictionary[Boolean] | "all"] = selectedMap
    val _selectedMode: js.UndefOr["single" | "multiple" | "series" | Boolean] = selectedMode
    val _itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = itemStyle
    val _label: js.UndefOr[SeriesLabelOption] = label
    val _xAxisIndex: js.UndefOr[Number] = xAxisIndex
    val _yAxisIndex: js.UndefOr[Number] = yAxisIndex
    val _xAxisId: js.UndefOr[String] = xAxisId
    val _yAxisId: js.UndefOr[String] = yAxisId
    val _polarIndex: js.UndefOr[Number] = polarIndex
    val _polarId: js.UndefOr[String] = polarId
    val _calendarIndex: js.UndefOr[Number] = calendarIndex
    val _calendarId: js.UndefOr[String] = calendarId
    val _geoIndex: js.UndefOr[Number] = geoIndex
    val _geoId: js.UndefOr[String] = geoId
    val _singleAxisIndex: js.UndefOr[Number] = singleAxisIndex
    val _singleAxisId: js.UndefOr[String] = singleAxisId
    val _symbol: js.UndefOr[String | SymbolCallback[CallbackDataParams]] = symbol
    val _symbolSize: js.UndefOr[Number | js.Array[Number] | SymbolSizeCallback[CallbackDataParams]] = symbolSize
    val _symbolRotate: js.UndefOr[Number | SymbolRotateCallback[CallbackDataParams]] = symbolRotate
    val _symbolKeepAspect: js.UndefOr[Boolean] = symbolKeepAspect
    val _symbolOffset: js.UndefOr[String | Number | js.Array[String | Number] | SymbolOffsetCallback[CallbackDataParams]] = symbolOffset
    val _datasetIndex: js.UndefOr[Number] = datasetIndex
    val _datasetId: js.UndefOr[String | Number] = datasetId
    val _sourceHeader: js.UndefOr[OptionSourceHeader] = sourceHeader
    val _dimensions: js.UndefOr[js.Array[DimensionDefinitionLoose]] = dimensions
    val _encode: js.UndefOr[OptionEncode] = encode
    val _effectType: js.UndefOr["ripple"] = effectType
    val _showEffectOn: js.UndefOr["render" | "emphasis"] = showEffectOn
    val _clip: js.UndefOr[Boolean] = clip
    val _rippleEffect: js.UndefOr[RippleEffectOption] = rippleEffect
    val _markArea: js.UndefOr[MarkAreaOption] = markArea
    val _markLine: js.UndefOr[MarkLineOption] = markLine
    val _markPoint: js.UndefOr[MarkPointOption] = markPoint
    val _tooltip: js.UndefOr[SeriesTooltipOption] = tooltip
    val effectScatterSeriesOption: EffectScatterSeriesOption = new EffectScatterSeriesOption:
      override val mainType: js.UndefOr[MainType] = "series"
      override val `type`: js.UndefOr[Type] = "effectScatter"
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
      override val emphasis: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & DefaultEmphasisFocusScaleMixin & StatesEmphasisOptionMixin] = _emphasis
      override val select: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any & StatesSelectOptionMixin] = _select
      override val blur: js.UndefOr[EffectScatterStateOption[CallbackDataParams] & js.Any] = _blur
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
      override val labelLine: js.UndefOr[LabelLineOption] = _labelLine
      override val labelLayout: js.UndefOr[LabelLayoutOption | LabelLayoutOptionCallback] = _labelLayout
      override val stateAnimation: js.UndefOr[AnimationOption] = _stateAnimation
      override val universalTransition: js.UndefOr[Boolean | UniversalTransitionOption] = _universalTransition
      override val selectedMap: js.UndefOr[js.Dictionary[Boolean] | "all"] = _selectedMap
      override val selectedMode: js.UndefOr["single" | "multiple" | "series" | Boolean] = _selectedMode
      override val itemStyle: js.UndefOr[ItemStyleOption[CallbackDataParams]] = _itemStyle
      override val label: js.UndefOr[SeriesLabelOption] = _label
      override val xAxisIndex: js.UndefOr[Number] = _xAxisIndex
      override val yAxisIndex: js.UndefOr[Number] = _yAxisIndex
      override val xAxisId: js.UndefOr[String] = _xAxisId
      override val yAxisId: js.UndefOr[String] = _yAxisId
      override val polarIndex: js.UndefOr[Number] = _polarIndex
      override val polarId: js.UndefOr[String] = _polarId
      override val calendarIndex: js.UndefOr[Number] = _calendarIndex
      override val calendarId: js.UndefOr[String] = _calendarId
      override val geoIndex: js.UndefOr[Number] = _geoIndex
      override val geoId: js.UndefOr[String] = _geoId
      override val singleAxisIndex: js.UndefOr[Number] = _singleAxisIndex
      override val singleAxisId: js.UndefOr[String] = _singleAxisId
      override val symbol: js.UndefOr[String | SymbolCallback[CallbackDataParams]] = _symbol
      override val symbolSize: js.UndefOr[Number | js.Array[Number] | SymbolSizeCallback[CallbackDataParams]] = _symbolSize
      override val symbolRotate: js.UndefOr[Number | SymbolRotateCallback[CallbackDataParams]] = _symbolRotate
      override val symbolKeepAspect: js.UndefOr[Boolean] = _symbolKeepAspect
      override val symbolOffset: js.UndefOr[String | Number | js.Array[String | Number] | SymbolOffsetCallback[CallbackDataParams]] = _symbolOffset
      override val datasetIndex: js.UndefOr[Number] = _datasetIndex
      override val datasetId: js.UndefOr[String | Number] = _datasetId
      override val sourceHeader: js.UndefOr[OptionSourceHeader] = _sourceHeader
      override val dimensions: js.UndefOr[js.Array[DimensionDefinitionLoose]] = _dimensions
      override val encode: js.UndefOr[OptionEncode] = _encode
      override val effectType: js.UndefOr["ripple"] = _effectType
      override val showEffectOn: js.UndefOr["render" | "emphasis"] = _showEffectOn
      override val clip: js.UndefOr[Boolean] = _clip
      override val rippleEffect: js.UndefOr[RippleEffectOption] = _rippleEffect
      override val markArea: js.UndefOr[MarkAreaOption] = _markArea
      override val markLine: js.UndefOr[MarkLineOption] = _markLine
      override val markPoint: js.UndefOr[MarkPointOption] = _markPoint
      override val tooltip: js.UndefOr[SeriesTooltipOption] = _tooltip
    effectScatterSeriesOption.clean