package com.peknight.demo.frontend.heima.pink.echarts

import com.peknight.demo.frontend.apache.echarts.`export`.EChartsOption
import com.peknight.demo.frontend.apache.echarts.chart.pie.{PieDataItemOption, PieItemStyleOption, PieLabelLineOption, PieLabelOption, PieSeriesOption}
import com.peknight.demo.frontend.apache.echarts.component.tooltip.TooltipOption
import com.peknight.demo.frontend.apache.echarts.core.ECharts
import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object DataVisualizationScript:

  @JSExportTopLevel("dataVisualization")
  def dataVisualization(): Unit = $(() => {
    $(".monitor .tabs").on("click", "a", null, (element: dom.Element) => {
      $(element).addClass("active").siblings("a").removeClass("active")
      $(".monitor .content").eq($(element).index()).show().siblings(".content").hide()
    })
    $(".marquee-view .marquee").each((element: dom.Element) => {
      val rows = $(element).children().clone()
      $(element).append(rows)
    })
    ECharts.init(dom.document.querySelector(".pie").asInstanceOf[dom.HTMLElement]).setOption(EChartsOption(
      tooltip = TooltipOption(trigger = "item", formatter = "{a} <br/>{b} : {c} ({d}%)"),
      color = js.Array("#006cff", "#60cda0", "#ed8884", "#ff9f7f", "#0096ff", "#9fe6b8", "#32c5e9", "#1d9dff"),
      series = js.Array(PieSeriesOption(
        name = "点位统计",
        radius = js.Array("10%", "70%"),
        center = js.Array("50%", "50%"),
        roseType = "area",
        itemStyle = PieItemStyleOption(borderRadius = 5),
        data = js.Array(
          PieDataItemOption(value = 20, name = "云南"),
          PieDataItemOption(value = 26, name = "北京"),
          PieDataItemOption(value = 24, name = "山东"),
          PieDataItemOption(value = 25, name = "河北"),
          PieDataItemOption(value = 20, name = "江苏"),
          PieDataItemOption(value = 25, name = "浙江"),
          PieDataItemOption(value = 30, name = "四川"),
          PieDataItemOption(value = 42, name = "湖北"),
        ),
        label = PieLabelOption(fontSize = 10),
        labelLine = PieLabelLineOption(length = 6, length2 = 8)
      ))
    ))
  })