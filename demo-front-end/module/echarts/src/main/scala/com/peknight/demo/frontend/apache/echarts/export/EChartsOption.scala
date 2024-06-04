package com.peknight.demo.frontend.apache.echarts.`export`

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.component.axispointer.AxisPointerOption
import com.peknight.demo.frontend.apache.echarts.component.brush.BrushOption
import com.peknight.demo.frontend.apache.echarts.component.dataset.DatasetOption
import com.peknight.demo.frontend.apache.echarts.component.graphic.GraphicComponentLooseOption
import com.peknight.demo.frontend.apache.echarts.component.timeline.{SliderTimelineOption, TimelineOption}
import com.peknight.demo.frontend.apache.echarts.component.title.TitleOption
import com.peknight.demo.frontend.apache.echarts.component.tooltip.TooltipOption
import com.peknight.demo.frontend.apache.echarts.coord.calendar.CalendarOption
import com.peknight.demo.frontend.apache.echarts.coord.cartesian.{GridOption, XAXisOption, YAXisOption}
import com.peknight.demo.frontend.apache.echarts.coord.geo.GeoOption
import com.peknight.demo.frontend.apache.echarts.coord.parallel.{ParallelAxisOption, ParallelCoordinateSystemOption}
import com.peknight.demo.frontend.apache.echarts.coord.polar.{AngleAxisOption, PolarOption, RadiusAxisOption}
import com.peknight.demo.frontend.apache.echarts.coord.radar.RadarOption
import com.peknight.demo.frontend.apache.echarts.coord.single.SingleAxisOption
import com.peknight.demo.frontend.apache.echarts.util.*
import com.peknight.demo.frontend.ecomfe.zrender.animation.AnimationEasing

import scala.scalajs.js

trait EChartsOption extends ECBasicOption:
  type OptionsType = js.Array[EChartsOption]
  type BaseOptionType = EChartsOption
  type TimelineType = TimelineOption | SliderTimelineOption
  val dataset: js.UndefOr[DatasetOption | js.Array[DatasetOption]] = js.undefined
  val aria: js.UndefOr[AriaOption] = js.undefined
  val title: js.UndefOr[TitleOption | js.Array[TitleOption]] = js.undefined
  val grid: js.UndefOr[GridOption | js.Array[GridOption]] = js.undefined
  val radar: js.UndefOr[RadarOption | js.Array[RadarOption]] = js.undefined
  val polar: js.UndefOr[PolarOption | js.Array[PolarOption]] = js.undefined
  val geo: js.UndefOr[GeoOption | js.Array[GeoOption]] = js.undefined
  val angleAxis: js.UndefOr[AngleAxisOption | js.Array[AngleAxisOption]] = js.undefined
  val radiusAxis: js.UndefOr[RadiusAxisOption | js.Array[RadiusAxisOption]] = js.undefined
  val xAxis: js.UndefOr[XAXisOption | js.Array[XAXisOption]] = js.undefined
  val yAxis: js.UndefOr[YAXisOption | js.Array[YAXisOption]] = js.undefined
  val singleAxis: js.UndefOr[SingleAxisOption | js.Array[SingleAxisOption]] = js.undefined
  val parallel: js.UndefOr[ParallelCoordinateSystemOption | js.Array[ParallelCoordinateSystemOption]] = js.undefined
  val parallelAxis: js.UndefOr[ParallelAxisOption | js.Array[ParallelAxisOption]] = js.undefined
  val calendar: js.UndefOr[CalendarOption | js.Array[CalendarOption]] = js.undefined
  val toolbox: js.UndefOr[ToolboxComponentOption | js.Array[ToolboxComponentOption]] = js.undefined
  val tooltip: js.UndefOr[TooltipOption | js.Array[TooltipOption]] = js.undefined
  val axisPointer: js.UndefOr[AxisPointerOption | js.Array[AxisPointerOption]] = js.undefined
  val brush: js.UndefOr[BrushOption | js.Array[BrushOption]] = js.undefined
  val legend: js.UndefOr[LegendComponentOption | js.Array[LegendComponentOption]] = js.undefined
  val dataZoom: js.UndefOr[DataZoomComponentOption | js.Array[DataZoomComponentOption]] = js.undefined
  val visualMap: js.UndefOr[VisualMapComponentOption | js.Array[VisualMapComponentOption]] = js.undefined
  val graphic: js.UndefOr[GraphicComponentLooseOption | js.Array[GraphicComponentLooseOption]] = js.undefined
  val series: js.UndefOr[SeriesOption[?, ?, ?, ?] | js.Array[SeriesOption[?, ?, ?, ?]]] = js.undefined

object EChartsOption:
  def apply(baseOption: js.UndefOr[EChartsOption] = js.undefined,
            options: js.UndefOr[js.Array[EChartsOption]] = js.undefined,
            media: js.UndefOr[js.Array[MediaUnit]] = js.undefined,
            timeline: js.UndefOr[TimelineOption | SliderTimelineOption] = js.undefined,
            backgroundColor: js.UndefOr[ZRColor] = js.undefined,
            darkMode: js.UndefOr[Boolean | "auto"] = js.undefined,
            textStyle: js.UndefOr[BaseTextStyle] = js.undefined,
            useUTC: js.UndefOr[Boolean] = js.undefined,
            stateAnimation: js.UndefOr[AnimationOption] = js.undefined,
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
            dataset: js.UndefOr[DatasetOption | js.Array[DatasetOption]] = js.undefined,
            aria: js.UndefOr[AriaOption] = js.undefined,
            title: js.UndefOr[TitleOption | js.Array[TitleOption]] = js.undefined,
            grid: js.UndefOr[GridOption | js.Array[GridOption]] = js.undefined,
            radar: js.UndefOr[RadarOption | js.Array[RadarOption]] = js.undefined,
            polar: js.UndefOr[PolarOption | js.Array[PolarOption]] = js.undefined,
            geo: js.UndefOr[GeoOption | js.Array[GeoOption]] = js.undefined,
            angleAxis: js.UndefOr[AngleAxisOption | js.Array[AngleAxisOption]] = js.undefined,
            radiusAxis: js.UndefOr[RadiusAxisOption | js.Array[RadiusAxisOption]] = js.undefined,
            xAxis: js.UndefOr[XAXisOption | js.Array[XAXisOption]] = js.undefined,
            yAxis: js.UndefOr[YAXisOption | js.Array[YAXisOption]] = js.undefined,
            singleAxis: js.UndefOr[SingleAxisOption | js.Array[SingleAxisOption]] = js.undefined,
            parallel: js.UndefOr[ParallelCoordinateSystemOption | js.Array[ParallelCoordinateSystemOption]] = js.undefined,
            parallelAxis: js.UndefOr[ParallelAxisOption | js.Array[ParallelAxisOption]] = js.undefined,
            calendar: js.UndefOr[CalendarOption | js.Array[CalendarOption]] = js.undefined,
            toolbox: js.UndefOr[ToolboxComponentOption | js.Array[ToolboxComponentOption]] = js.undefined,
            tooltip: js.UndefOr[TooltipOption | js.Array[TooltipOption]] = js.undefined,
            axisPointer: js.UndefOr[AxisPointerOption | js.Array[AxisPointerOption]] = js.undefined,
            brush: js.UndefOr[BrushOption | js.Array[BrushOption]] = js.undefined,
            legend: js.UndefOr[LegendComponentOption | js.Array[LegendComponentOption]] = js.undefined,
            dataZoom: js.UndefOr[DataZoomComponentOption | js.Array[DataZoomComponentOption]] = js.undefined,
            visualMap: js.UndefOr[VisualMapComponentOption | js.Array[VisualMapComponentOption]] = js.undefined,
            graphic: js.UndefOr[GraphicComponentLooseOption | js.Array[GraphicComponentLooseOption]] = js.undefined,
            series: js.UndefOr[SeriesOption[?, ?, ?, ?] | js.Array[SeriesOption[?, ?, ?, ?]]] = js.undefined): EChartsOption =
    val _baseOption: js.UndefOr[EChartsOption] = baseOption
    val _options: js.UndefOr[js.Array[EChartsOption]] = options
    val _media: js.UndefOr[js.Array[MediaUnit]] = media
    val _timeline: js.UndefOr[TimelineOption | SliderTimelineOption] = timeline
    val _backgroundColor: js.UndefOr[ZRColor] = backgroundColor
    val _darkMode: js.UndefOr[Boolean | "auto"] = darkMode
    val _textStyle: js.UndefOr[BaseTextStyle] = textStyle
    val _useUTC: js.UndefOr[Boolean] = useUTC
    val _stateAnimation: js.UndefOr[AnimationOption] = stateAnimation
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
    val _dataset: js.UndefOr[DatasetOption | js.Array[DatasetOption]] = dataset
    val _aria: js.UndefOr[AriaOption] = aria
    val _title: js.UndefOr[TitleOption | js.Array[TitleOption]] = title
    val _grid: js.UndefOr[GridOption | js.Array[GridOption]] = grid
    val _radar: js.UndefOr[RadarOption | js.Array[RadarOption]] = radar
    val _polar: js.UndefOr[PolarOption | js.Array[PolarOption]] = polar
    val _geo: js.UndefOr[GeoOption | js.Array[GeoOption]] = geo
    val _angleAxis: js.UndefOr[AngleAxisOption | js.Array[AngleAxisOption]] = angleAxis
    val _radiusAxis: js.UndefOr[RadiusAxisOption | js.Array[RadiusAxisOption]] = radiusAxis
    val _xAxis: js.UndefOr[XAXisOption | js.Array[XAXisOption]] = xAxis
    val _yAxis: js.UndefOr[YAXisOption | js.Array[YAXisOption]] = yAxis
    val _singleAxis: js.UndefOr[SingleAxisOption | js.Array[SingleAxisOption]] = singleAxis
    val _parallel: js.UndefOr[ParallelCoordinateSystemOption | js.Array[ParallelCoordinateSystemOption]] = parallel
    val _parallelAxis: js.UndefOr[ParallelAxisOption | js.Array[ParallelAxisOption]] = parallelAxis
    val _calendar: js.UndefOr[CalendarOption | js.Array[CalendarOption]] = calendar
    val _toolbox: js.UndefOr[ToolboxComponentOption | js.Array[ToolboxComponentOption]] = toolbox
    val _tooltip: js.UndefOr[TooltipOption | js.Array[TooltipOption]] = tooltip
    val _axisPointer: js.UndefOr[AxisPointerOption | js.Array[AxisPointerOption]] = axisPointer
    val _brush: js.UndefOr[BrushOption | js.Array[BrushOption]] = brush
    val _legend: js.UndefOr[LegendComponentOption | js.Array[LegendComponentOption]] = legend
    val _dataZoom: js.UndefOr[DataZoomComponentOption | js.Array[DataZoomComponentOption]] = dataZoom
    val _visualMap: js.UndefOr[VisualMapComponentOption | js.Array[VisualMapComponentOption]] = visualMap
    val _graphic: js.UndefOr[GraphicComponentLooseOption | js.Array[GraphicComponentLooseOption]] = graphic
    val _series: js.UndefOr[SeriesOption[?, ?, ?, ?] | js.Array[SeriesOption[?, ?, ?, ?]]] = series
    val echartsOption = new EChartsOption:
      override val baseOption: js.UndefOr[BaseOptionType] = _baseOption
      override val options: js.UndefOr[OptionsType] = _options
      override val media: js.UndefOr[MediaType] = _media
      override val timeline: js.UndefOr[TimelineType] = _timeline
      override val backgroundColor: js.UndefOr[ZRColor] = _backgroundColor
      override val darkMode: js.UndefOr[Boolean | "auto"] = _darkMode
      override val textStyle: js.UndefOr[BaseTextStyle] = _textStyle
      override val useUTC: js.UndefOr[Boolean] = _useUTC
      override val stateAnimation: js.UndefOr[AnimationOption] = _stateAnimation
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
      override val dataset: js.UndefOr[DatasetOption | js.Array[DatasetOption]] = _dataset
      override val aria: js.UndefOr[AriaOption] = _aria
      override val title: js.UndefOr[TitleOption | js.Array[TitleOption]] = _title
      override val grid: js.UndefOr[GridOption | js.Array[GridOption]] = _grid
      override val radar: js.UndefOr[RadarOption | js.Array[RadarOption]] = _radar
      override val polar: js.UndefOr[PolarOption | js.Array[PolarOption]] = _polar
      override val geo: js.UndefOr[GeoOption | js.Array[GeoOption]] = _geo
      override val angleAxis: js.UndefOr[AngleAxisOption | js.Array[AngleAxisOption]] = _angleAxis
      override val radiusAxis: js.UndefOr[RadiusAxisOption | js.Array[RadiusAxisOption]] = _radiusAxis
      override val xAxis: js.UndefOr[XAXisOption | js.Array[XAXisOption]] = _xAxis
      override val yAxis: js.UndefOr[YAXisOption | js.Array[YAXisOption]] = _yAxis
      override val singleAxis: js.UndefOr[SingleAxisOption | js.Array[SingleAxisOption]] = _singleAxis
      override val parallel: js.UndefOr[ParallelCoordinateSystemOption | js.Array[ParallelCoordinateSystemOption]] = _parallel
      override val parallelAxis: js.UndefOr[ParallelAxisOption | js.Array[ParallelAxisOption]] = _parallelAxis
      override val calendar: js.UndefOr[CalendarOption | js.Array[CalendarOption]] = _calendar
      override val toolbox: js.UndefOr[ToolboxComponentOption | js.Array[ToolboxComponentOption]] = _toolbox
      override val tooltip: js.UndefOr[TooltipOption | js.Array[TooltipOption]] = _tooltip
      override val axisPointer: js.UndefOr[AxisPointerOption | js.Array[AxisPointerOption]] = _axisPointer
      override val brush: js.UndefOr[BrushOption | js.Array[BrushOption]] = _brush
      override val legend: js.UndefOr[LegendComponentOption | js.Array[LegendComponentOption]] = _legend
      override val dataZoom: js.UndefOr[DataZoomComponentOption | js.Array[DataZoomComponentOption]] = _dataZoom
      override val visualMap: js.UndefOr[VisualMapComponentOption | js.Array[VisualMapComponentOption]] = _visualMap
      override val graphic: js.UndefOr[GraphicComponentLooseOption | js.Array[GraphicComponentLooseOption]] = _graphic
      override val series: js.UndefOr[SeriesOption[?, ?, ?, ?] | js.Array[SeriesOption[?, ?, ?, ?]]] = _series
    echartsOption.clean
    
  extension [T <: js.Any] (t: T)
    def clean: T = t match
      case func: js.Function => func
      case array if js.Array.isArray(array) =>
        array.asInstanceOf[js.Array[js.Any]].filterNot(js.isUndefined).map(_.clean).asInstanceOf[T]
      case obj: js.Object =>
        val newObj: js.Object = new js.Object()
        js.Object.keys(obj)
          .filterNot(key => js.isUndefined(obj.asInstanceOf[js.Dynamic].selectDynamic(key)))
          .map(key => key -> obj.asInstanceOf[js.Dynamic].selectDynamic(key).clean)
          .foreach { case (key, value) => newObj.asInstanceOf[js.Dynamic].updateDynamic(key)(value) }
        newObj.asInstanceOf[T]
      case other => other
  end extension
