package com.peknight.demo.frontend.apache.echarts.coord.cartesian

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.coord.*
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.ecomfe.zrender.animation.AnimationEasing

import scala.scalajs.js

object XAXisOption:
  def category(id: js.UndefOr[OptionId] = js.undefined,
               name: js.UndefOr[String] = js.undefined,
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
               show: js.UndefOr[Boolean] = js.undefined,
               inverse: js.UndefOr[Boolean] = js.undefined,
               nameLocation: js.UndefOr["start" | "middle" | "end"] = js.undefined,
               nameRotate: js.UndefOr[Number] = js.undefined,
               nameTruncate: js.UndefOr[TruncateMixin] = js.undefined,
               nameTextStyle: js.UndefOr[AxisNameTextStyleOption] = js.undefined,
               nameGap: js.UndefOr[Number] = js.undefined,
               silent: js.UndefOr[Boolean] = js.undefined,
               triggerEvent: js.UndefOr[Boolean] = js.undefined,
               tooltip: js.UndefOr[ShowMixin] = js.undefined,
               axisLabel: js.UndefOr[AxisLabelOption["category"] & IntervalMixin] = js.undefined,
               axisPointer: js.UndefOr[CommonAxisPointerOption] = js.undefined,
               axisLine: js.UndefOr[AxisLineOption] = js.undefined,
               axisTick: js.UndefOr[AxisTickOption & AlignWithLabelMixin & IntervalMixin] = js.undefined,
               minorTick: js.UndefOr[MinorTickOption] = js.undefined,
               splitLine: js.UndefOr[SplitLineOption] = js.undefined,
               minorSplitLine: js.UndefOr[MinorSplitLineOption] = js.undefined,
               splitArea: js.UndefOr[SplitAreaOption] = js.undefined,
               min: js.UndefOr[ScaleDataValue | "dataMin" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = js.undefined,
               max: js.UndefOr[ScaleDataValue | "dataMax" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = js.undefined,
               boundaryGap: js.UndefOr[Boolean] = js.undefined,
               data: js.UndefOr[js.Array[OrdinalRawValue | DataMixin]] = js.undefined,
               deduplication: js.UndefOr[Boolean] = js.undefined,
               gridIndex: js.UndefOr[Number] = js.undefined,
               gridId: js.UndefOr[String] = js.undefined,
               position: js.UndefOr[CartesianAxisPosition] = js.undefined,
               offset: js.UndefOr[Number] = js.undefined,
               categorySortInfo: js.UndefOr[OrdinalSortInfo] = js.undefined): XAXisOption =
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[String] = name
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
    val _show: js.UndefOr[Boolean] = show
    val _inverse: js.UndefOr[Boolean] = inverse
    val _nameLocation: js.UndefOr["start" | "middle" | "end"] = nameLocation
    val _nameRotate: js.UndefOr[Number] = nameRotate
    val _nameTruncate: js.UndefOr[TruncateMixin] = nameTruncate
    val _nameTextStyle: js.UndefOr[AxisNameTextStyleOption] = nameTextStyle
    val _nameGap: js.UndefOr[Number] = nameGap
    val _silent: js.UndefOr[Boolean] = silent
    val _triggerEvent: js.UndefOr[Boolean] = triggerEvent
    val _tooltip: js.UndefOr[ShowMixin] = tooltip
    val _axisLabel: js.UndefOr[AxisLabelOption["category"] & IntervalMixin] = axisLabel
    val _axisPointer: js.UndefOr[CommonAxisPointerOption] = axisPointer
    val _axisLine: js.UndefOr[AxisLineOption] = axisLine
    val _axisTick: js.UndefOr[AxisTickOption & AlignWithLabelMixin & IntervalMixin] = axisTick
    val _minorTick: js.UndefOr[MinorTickOption] = minorTick
    val _splitLine: js.UndefOr[SplitLineOption] = splitLine
    val _minorSplitLine: js.UndefOr[MinorSplitLineOption] = minorSplitLine
    val _splitArea: js.UndefOr[SplitAreaOption] = splitArea
    val _min: js.UndefOr[ScaleDataValue | "dataMin" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = min
    val _max: js.UndefOr[ScaleDataValue | "dataMax" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = max
    val _boundaryGap: js.UndefOr[Boolean] = boundaryGap
    val _data: js.UndefOr[js.Array[OrdinalRawValue | DataMixin]] = data
    val _deduplication: js.UndefOr[Boolean] = deduplication
    val _gridIndex: js.UndefOr[Number] = gridIndex
    val _gridId: js.UndefOr[String] = gridId
    val _position: js.UndefOr[CartesianAxisPosition] = position
    val _offset: js.UndefOr[Number] = offset
    val _categorySortInfo: js.UndefOr[OrdinalSortInfo] = categorySortInfo
    new CategoryAxisBaseOption with CartesianAxisMixin with XAXisMainTypeMixin:
      override val mainType: js.UndefOr[MainType] = "xAxis"
      override val `type`: js.UndefOr[Type] = "category"
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
      override val show: js.UndefOr[Boolean] = _show
      override val inverse: js.UndefOr[Boolean] = _inverse
      override val nameLocation: js.UndefOr["start" | "middle" | "end"] = _nameLocation
      override val nameRotate: js.UndefOr[Number] = _nameRotate
      override val nameTruncate: js.UndefOr[TruncateMixin] = _nameTruncate
      override val nameTextStyle: js.UndefOr[AxisNameTextStyleOption] = _nameTextStyle
      override val nameGap: js.UndefOr[Number] = _nameGap
      override val silent: js.UndefOr[Boolean] = _silent
      override val triggerEvent: js.UndefOr[Boolean] = _triggerEvent
      override val tooltip: js.UndefOr[ShowMixin] = _tooltip
      override val axisLabel: js.UndefOr[AxisLabelType] = _axisLabel
      override val axisPointer: js.UndefOr[CommonAxisPointerOption] = _axisPointer
      override val axisLine: js.UndefOr[AxisLineOption] = _axisLine
      override val axisTick: js.UndefOr[AxisTickType] = _axisTick
      override val minorTick: js.UndefOr[MinorTickOption] = _minorTick
      override val splitLine: js.UndefOr[SplitLineOption] = _splitLine
      override val minorSplitLine: js.UndefOr[MinorSplitLineOption] = _minorSplitLine
      override val splitArea: js.UndefOr[SplitAreaOption] = _splitArea
      override val min: js.UndefOr[ScaleDataValue | "dataMin" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = _min
      override val max: js.UndefOr[ScaleDataValue | "dataMax" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = _max
      override val boundaryGap: js.UndefOr[Boolean] = _boundaryGap
      override val data: js.UndefOr[js.Array[OrdinalRawValue | DataMixin]] = _data
      override val deduplication: js.UndefOr[Boolean] = _deduplication
      override val gridIndex: js.UndefOr[Number] = _gridIndex
      override val gridId: js.UndefOr[String] = _gridId
      override val position: js.UndefOr[CartesianAxisPosition] = _position
      override val offset: js.UndefOr[Number] = _offset
      override val categorySortInfo: js.UndefOr[OrdinalSortInfo] = _categorySortInfo

  def value(id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[String] = js.undefined,
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
            show: js.UndefOr[Boolean] = js.undefined,
            inverse: js.UndefOr[Boolean] = js.undefined,
            nameLocation: js.UndefOr["start" | "middle" | "end"] = js.undefined,
            nameRotate: js.UndefOr[Number] = js.undefined,
            nameTruncate: js.UndefOr[TruncateMixin] = js.undefined,
            nameTextStyle: js.UndefOr[AxisNameTextStyleOption] = js.undefined,
            nameGap: js.UndefOr[Number] = js.undefined,
            silent: js.UndefOr[Boolean] = js.undefined,
            triggerEvent: js.UndefOr[Boolean] = js.undefined,
            tooltip: js.UndefOr[ShowMixin] = js.undefined,
            axisLabel: js.UndefOr[AxisLabelOption["value"] & IntervalMixin] = js.undefined,
            axisPointer: js.UndefOr[CommonAxisPointerOption] = js.undefined,
            axisLine: js.UndefOr[AxisLineOption] = js.undefined,
            axisTick: js.UndefOr[AxisTickOption & AlignWithLabelMixin & IntervalMixin] = js.undefined,
            minorTick: js.UndefOr[MinorTickOption] = js.undefined,
            splitLine: js.UndefOr[SplitLineOption] = js.undefined,
            minorSplitLine: js.UndefOr[MinorSplitLineOption] = js.undefined,
            splitArea: js.UndefOr[SplitAreaOption] = js.undefined,
            min: js.UndefOr[ScaleDataValue | "dataMin" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = js.undefined,
            max: js.UndefOr[ScaleDataValue | "dataMax" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = js.undefined,
            boundaryGap: js.UndefOr[(Number | String, Number | String)] = js.undefined,
            splitNumber: js.UndefOr[Number] = js.undefined,
            interval: js.UndefOr[Number] = js.undefined,
            minInterval: js.UndefOr[Number] = js.undefined,
            maxInterval: js.UndefOr[Number] = js.undefined,
            alignTicks: js.UndefOr[Boolean] = js.undefined,
            scale: js.UndefOr[Boolean] = js.undefined,
            gridIndex: js.UndefOr[Number] = js.undefined,
            gridId: js.UndefOr[String] = js.undefined,
            position: js.UndefOr[CartesianAxisPosition] = js.undefined,
            offset: js.UndefOr[Number] = js.undefined,
            categorySortInfo: js.UndefOr[OrdinalSortInfo] = js.undefined): YAXisOption =
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[String] = name
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
    val _show: js.UndefOr[Boolean] = show
    val _inverse: js.UndefOr[Boolean] = inverse
    val _nameLocation: js.UndefOr["start" | "middle" | "end"] = nameLocation
    val _nameRotate: js.UndefOr[Number] = nameRotate
    val _nameTruncate: js.UndefOr[TruncateMixin] = nameTruncate
    val _nameTextStyle: js.UndefOr[AxisNameTextStyleOption] = nameTextStyle
    val _nameGap: js.UndefOr[Number] = nameGap
    val _silent: js.UndefOr[Boolean] = silent
    val _triggerEvent: js.UndefOr[Boolean] = triggerEvent
    val _tooltip: js.UndefOr[ShowMixin] = tooltip
    val _axisLabel: js.UndefOr[AxisLabelOption["value"] & IntervalMixin] = axisLabel
    val _axisPointer: js.UndefOr[CommonAxisPointerOption] = axisPointer
    val _axisLine: js.UndefOr[AxisLineOption] = axisLine
    val _axisTick: js.UndefOr[AxisTickOption & AlignWithLabelMixin & IntervalMixin] = axisTick
    val _minorTick: js.UndefOr[MinorTickOption] = minorTick
    val _splitLine: js.UndefOr[SplitLineOption] = splitLine
    val _minorSplitLine: js.UndefOr[MinorSplitLineOption] = minorSplitLine
    val _splitArea: js.UndefOr[SplitAreaOption] = splitArea
    val _min: js.UndefOr[ScaleDataValue | "dataMin" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = min
    val _max: js.UndefOr[ScaleDataValue | "dataMax" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = max
    val _boundaryGap: js.UndefOr[(Number | String, Number | String)] = boundaryGap
    val _splitNumber: js.UndefOr[Number] = splitNumber
    val _interval: js.UndefOr[Number] = interval
    val _minInterval: js.UndefOr[Number] = minInterval
    val _maxInterval: js.UndefOr[Number] = maxInterval
    val _alignTicks: js.UndefOr[Boolean] = alignTicks
    val _scale: js.UndefOr[Boolean] = scale
    val _gridIndex: js.UndefOr[Number] = gridIndex
    val _gridId: js.UndefOr[String] = gridId
    val _position: js.UndefOr[CartesianAxisPosition] = position
    val _offset: js.UndefOr[Number] = offset
    val _categorySortInfo: js.UndefOr[OrdinalSortInfo] = categorySortInfo
    new ValueAxisBaseOption with CartesianAxisMixin with YAXisMainTypeMixin:
      override val mainType: js.UndefOr[MainType] = "yAxis"
      override val `type`: js.UndefOr[Type] = "value"
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
      override val show: js.UndefOr[Boolean] = _show
      override val inverse: js.UndefOr[Boolean] = _inverse
      override val nameLocation: js.UndefOr["start" | "middle" | "end"] = _nameLocation
      override val nameRotate: js.UndefOr[Number] = _nameRotate
      override val nameTruncate: js.UndefOr[TruncateMixin] = _nameTruncate
      override val nameTextStyle: js.UndefOr[AxisNameTextStyleOption] = _nameTextStyle
      override val nameGap: js.UndefOr[Number] = _nameGap
      override val silent: js.UndefOr[Boolean] = _silent
      override val triggerEvent: js.UndefOr[Boolean] = _triggerEvent
      override val tooltip: js.UndefOr[ShowMixin] = _tooltip
      override val axisLabel: js.UndefOr[AxisLabelType] = _axisLabel
      override val axisPointer: js.UndefOr[CommonAxisPointerOption] = _axisPointer
      override val axisLine: js.UndefOr[AxisLineOption] = _axisLine
      override val axisTick: js.UndefOr[AxisTickType] = _axisTick
      override val minorTick: js.UndefOr[MinorTickOption] = _minorTick
      override val splitLine: js.UndefOr[SplitLineOption] = _splitLine
      override val minorSplitLine: js.UndefOr[MinorSplitLineOption] = _minorSplitLine
      override val splitArea: js.UndefOr[SplitAreaOption] = _splitArea
      override val min: js.UndefOr[ScaleDataValue | "dataMin" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = _min
      override val max: js.UndefOr[ScaleDataValue | "dataMax" | js.Function1[MinMaxNumberExtent, ScaleDataValue]] = _max
      override val boundaryGap: js.UndefOr[(Number | String, Number | String)] = _boundaryGap
      override val splitNumber: js.UndefOr[Number] = _splitNumber
      override val interval: js.UndefOr[Number] = _interval
      override val minInterval: js.UndefOr[Number] = _minInterval
      override val maxInterval: js.UndefOr[Number] = _maxInterval
      override val alignTicks: js.UndefOr[Boolean] = _alignTicks
      override val scale: js.UndefOr[Boolean] = _scale
      override val gridIndex: js.UndefOr[Number] = _gridIndex
      override val gridId: js.UndefOr[String] = _gridId
      override val position: js.UndefOr[CartesianAxisPosition] = _position
      override val offset: js.UndefOr[Number] = _offset
      override val categorySortInfo: js.UndefOr[OrdinalSortInfo] = _categorySortInfo
