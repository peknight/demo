package com.peknight.demo.frontend.heima.pink.echarts

import com.peknight.demo.frontend.apache.echarts.*
import com.peknight.demo.frontend.apache.echarts.`export`.{EChartsOption, ToolboxComponentOption, ToolboxFeatureOption}
import com.peknight.demo.frontend.apache.echarts.chart.bar.BarSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.line.LineSeriesOption
import com.peknight.demo.frontend.apache.echarts.chart.pie.{PieDataItemOption, PieEmphasisOption, PieItemStyleOption, PieSeriesOption}
import com.peknight.demo.frontend.apache.echarts.component.legend.LegendOption
import com.peknight.demo.frontend.apache.echarts.component.title.TitleOption
import com.peknight.demo.frontend.apache.echarts.component.toolbox.feature.ToolboxSaveAsImageFeatureOption
import com.peknight.demo.frontend.apache.echarts.component.tooltip.TooltipOption
import com.peknight.demo.frontend.apache.echarts.coord.cartesian.{GridOption, XAXisOption, YAXisOption}
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
        title = TitleOption(text = "某站点用户访问来源", subtext = "纯属虚构", left = "center"),
        tooltip = TooltipOption(trigger = "item"),
        legend = LegendOption(orient = "vertical", left = "left"),
        series = js.Array(PieSeriesOption(
          name = "访问来源",
          data = js.Array(
            PieDataItemOption(name = "搜索引擎", value = 1048),
            PieDataItemOption(name = "直接访问", value = 735),
            PieDataItemOption(name = "邮件营销", value = 580),
            PieDataItemOption(name = "联盟广告", value = 1048),
            PieDataItemOption(name = "视频广告", value = 300)
          ),
          emphasis = PieEmphasisOption(PieItemStyleOption(shadowBlur = 10, shadowOffsetX = 0, shadowColor = "rgba(0,0,0,0.5)"))
        ))
      ))

    ECharts.init(dom.document.getElementById("line-simple").asInstanceOf[dom.HTMLElement])
      .setOption(EChartsOption(
        color = js.Array("pink", "blue", "green", "skyblue", "red"),
        title = TitleOption(text = "Stacked Line"),
        tooltip = TooltipOption(trigger = "axis"),
        legend = LegendOption(data = js.Array("Email", "Union Ads", "Video Ads", "Direct", "Search Engine")),
        grid = GridOption(left = "3%", right = "4%", bottom = "3%", containLabel = true),
        toolbox = ToolboxComponentOption(feature = ToolboxFeatureOption(saveAsImage = ToolboxSaveAsImageFeatureOption())),
        xAxis = XAXisOption.category(boundaryGap = false, data = js.Array("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")),
        yAxis = YAXisOption.value(),
        series = js.Array(
          LineSeriesOption(name = "Email", stack = "Total", data = js.Array(120, 132, 101, 134, 90, 230, 210)),
          LineSeriesOption(name = "Union Ads", stack = "Total", data = js.Array(220, 182, 191, 234, 290, 330, 310)),
          LineSeriesOption(name = "Video Ads", stack = "Total", data = js.Array(150, 232, 201, 154, 190, 330, 410)),
          LineSeriesOption(name = "Direct", stack = "Total", data = js.Array(320, 332, 301, 334, 390, 330, 320)),
          LineSeriesOption(name = "Search Engine", stack = "Total", data = js.Array(820, 932, 901, 934, 1290, 1330, 1320)),
        )
      ))
