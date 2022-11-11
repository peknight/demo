package com.peknight.demo.frontend.apache.echarts

import com.peknight.demo.frontend.apache.echarts.chart.bar.{BarSeriesOption, PictorialBarSeriesOption}
import com.peknight.demo.frontend.apache.echarts.chart.boxplot.BoxplotSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.candlestick.CandlestickSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.custom.CustomSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.effectscatter.EffectScatterSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.funnel.FunnelSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.gauge.GaugeSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.graph.GraphSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.heatmap.HeatmapSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.line.LineSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.lines.LinesSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.map.MapSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.parallel.ParallelSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.pie.PieSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.rader.RadarSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.sankey.SankeySeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.scatter.ScatterSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.sunburst.SunburstSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.themeriver.ThemeRiverSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.tree.TreeSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.treemap.TreemapSeriesOption
import com.peknight.demo.frontend.apache.echarts.component.legend.{LegendOption, ScrollableLegendOption}

package object `export`:

  type LegendComponentOption = LegendOption | ScrollableLegendOption

  type SeriesOption = LineSeriesOption | BarSeriesOption | ScatterSeriesOption | PieSeriesOption |
    RadarSeriesOption | MapSeriesOption | TreeSeriesOption | TreemapSeriesOption | GraphSeriesOption |
    GaugeSeriesOption | FunnelSeriesOption | ParallelSeriesOption | SankeySeriesOption | BoxplotSeriesOption |
    CandlestickSeriesOption | EffectScatterSeriesOption | LinesSeriesOption | HeatmapSeriesOption |
    PictorialBarSeriesOption | ThemeRiverSeriesOption | SunburstSeriesOption | CustomSeriesOption
