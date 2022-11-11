package com.peknight.demo.frontend.apache.echarts.`export`

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
import com.peknight.demo.frontend.apache.echarts.util.{AriaOption, ECBasicOption}

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
  val series: js.UndefOr[SeriesOption | js.Array[SeriesOption]] = js.undefined

object EChartsOption:
  def apply(title: js.UndefOr[TitleOption | js.Array[TitleOption]] = js.undefined,
            xAxis: js.UndefOr[XAXisOption | js.Array[XAXisOption]] = js.undefined,
            yAxis: js.UndefOr[YAXisOption | js.Array[YAXisOption]] = js.undefined,
            tooltip: js.UndefOr[TooltipOption | js.Array[TooltipOption]] = js.undefined,
            legend: js.UndefOr[LegendComponentOption | js.Array[LegendComponentOption]] = js.undefined,
            series: js.UndefOr[SeriesOption | js.Array[SeriesOption]] = js.undefined): EChartsOption =
    val _title = title
    val _xAxis = xAxis
    val _yAxis = yAxis
    val _tooltip = tooltip
    val _legend = legend
    val _series = series
    new EChartsOption {
      override val title: js.UndefOr[TitleOption | js.Array[TitleOption]] = _title
      override val xAxis: js.UndefOr[XAXisOption | js.Array[XAXisOption]] = _xAxis
      override val yAxis: js.UndefOr[YAXisOption | js.Array[YAXisOption]] = _yAxis
      override val tooltip: js.UndefOr[TooltipOption | js.Array[TooltipOption]] = _tooltip
      override val legend: js.UndefOr[LegendComponentOption | js.Array[LegendComponentOption]] = _legend
      override val series: js.UndefOr[SeriesOption | js.Array[SeriesOption]] = _series
    }
