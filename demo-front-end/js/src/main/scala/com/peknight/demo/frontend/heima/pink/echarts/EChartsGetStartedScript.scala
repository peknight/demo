package com.peknight.demo.frontend.heima.pink.echarts

import com.peknight.demo.frontend.apache.echarts.*
import com.peknight.demo.frontend.apache.echarts.`export`.EChartsOption
import com.peknight.demo.frontend.apache.echarts.chart.bar.BarSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.pie.{EmphasisMixin, PieDataItemOption, PieItemStyleOption, PieSeriesOption}
import com.peknight.demo.frontend.apache.echarts.component.legend.LegendOption
import com.peknight.demo.frontend.apache.echarts.component.title.TitleOption
import com.peknight.demo.frontend.apache.echarts.component.tooltip.TooltipOption
import com.peknight.demo.frontend.apache.echarts.coord.cartesian.{XAXisOption, YAXisOption}
import com.peknight.demo.frontend.apache.echarts.core.ECharts
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object EChartsGetStartedScript:

  @JSExportTopLevel("echartsGetStarted")
  def echartsGetStarted(): Unit =
    // 基于准备好的dom，初始化echarts实例
    val myChart = ECharts.init(dom.document.getElementById("main").asInstanceOf[dom.HTMLElement])
    // 指定图表的配置项和数据
    val option = EChartsOption(
      title = TitleOption(text = "ECharts 入门示例"),
      tooltip = TooltipOption(),
      legend = LegendOption(data = js.Array("销量")),
      xAxis = XAXisOption.category(data = js.Array("衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子")),
      yAxis = YAXisOption.category(),
      series = js.Array(BarSeriesOption(name = "销量", data = js.Array(5, 20, 36, 10, 10, 20)))
    )
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option)
    
    ECharts.init(dom.document.getElementById("pie-simple").asInstanceOf[dom.HTMLElement])
      .setOption(EChartsOption(
        title = TitleOption(text = "Referer of a website", subtext = "Fake Data", left = "center"),
        tooltip = TooltipOption(trigger = "item"),
        legend = LegendOption(orient = "vertical", left = "left"),
        series = js.Array(PieSeriesOption(
          name = "Access From",
          data = js.Array(
            PieDataItemOption(name = "Search Engine", value = 1048),
            PieDataItemOption(name = "Direct", value = 735),
            PieDataItemOption(name = "Email", value = 580),
            PieDataItemOption(name = "Union Ads", value = 1048),
            PieDataItemOption(name = "Video Ads", value = 300)
          ),
          emphasis = EmphasisMixin(PieItemStyleOption(shadowBlur = 10, shadowOffsetX = 0, shadowColor = "rgba(0,0,0,0.5)"))
        ))
      ))