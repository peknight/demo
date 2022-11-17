package com.peknight.demo.frontend.heima.pink.javascript.datavisualization

import cats.syntax.option.*
import scalatags.generic.Bundle

class DataVisualizationPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as inlineStyle, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag = html(lang := "en")(
    head(
      meta(charset := "UTF-8"),
      meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
      title("数据可视化项目"),
      link(rel := "stylesheet", href := "/css/data-visualization-fonts.css"),
      link(rel := "stylesheet", href := "/css/data-visualization-media.css"),
      link(rel := "stylesheet", href := "/css/data-visualization-keyframes.css"),
      link(rel := "stylesheet", href := "/css/data-visualization.css"),
      script(`type` := "text/javascript", src := "/webjars/echarts/5.4.0/dist/echarts.min.js"),
      script(src := "/webjars/amfe-flexible/2.2.1/index.min.js"),
      script(`type` := "text/javascript", src := "/webjars/jquery/3.6.1/jquery.min.js"),
      script(`type` := "text/javascript", src := "/echarts/china.js"),
      script(`type` := "text/javascript", src := "/main.js"),
      script("dataVisualization()")
    ),
    body(
      // 父容器大盒子
      div(cls := "viewport")(
        div(cls := "column")(
          // 概览区域模块制作
          div(cls := "panel overview")(div(cls := "inner")(ul(Seq(
            ("设备总数", "2,190", "#006cff", none[String]),
            ("季度新增", "190", "#6acca3", "item".some),
            ("运营设备", "3,001", "#6acca3", none[String]),
            ("异常设备", "108", "#ed3f35", none[String])
          ).map { case (text, number, color, clsOption) => li(clsOption.map(clazz => cls := clazz).getOrElse(modifier()))(
            h4(number), span(i(cls := "icon-dot", inlineStyle := s"color: $color"), text)
          )}))),
          // 监控区域模块制作
          div(cls := "panel monitor")(div(cls := "inner")(
            div(cls := "tabs")(
              a(href := "javascript:;", cls := "active")("故障设备监控"),
              a(href := "javascript:;")("异常设备监控"),
            ),
            div(cls := "content", inlineStyle := "display: block;")(monitorContent(
              Seq("故障时间", "设备地址", "异常代码"),
              Seq(
                ("20180701", "11北京市昌平西路金燕龙写字楼", "1000001"),
                ("20190601", "北京市昌平区西路金燕龙写字楼", "1000002"),
                ("20190704", "北京市昌平区建材城西路金燕龙写字楼", "1000003"),
                ("20180701", "北京市昌平区西路金燕龙写字楼", "1000004"),
                ("20190701", "北京市昌平区建材城西路金燕龙写字楼", "1000005"),
                ("20190701", "北京市昌平区建材城西路金燕龙写字楼", "1000006"),
                ("20190701", "北京市昌平区西路金燕龙写字楼", "1000007"),
                ("20190701", "北京市昌平区建材城西路金燕龙写字楼", "1000008"),
                ("20190701", "北京市昌平区建材城西路金燕龙写字楼", "1000009"),
                ("20190701", "北京市昌平区建材城西路金燕龙写字楼", "1000010")
              )
            )),
            div(cls := "content")(monitorContent(
              Seq("异常时间", "设备地址", "异常代码"),
              Seq(
                ("20190701", "北京市昌平区建材城西路金燕龙写字楼", "1000001"),
                ("20190701", "北京市昌平区建材城西路金燕龙写字楼", "1000002"),
                ("20190703", "北京市昌平区建材城西路金燕龙写字楼", "1000002"),
                ("20190704", "北京市昌平区建材城西路金燕龙写字楼", "1000002"),
                ("20190705", "北京市昌平区建材城西路金燕龙写字楼", "1000002"),
                ("20190706", "北京市昌平区建材城西路金燕龙写字楼", "1000002"),
                ("20190707", "北京市昌平区建材城西路金燕龙写字楼", "1000002"),
                ("20190708", "北京市昌平区建材城西路金燕龙写字楼", "1000002"),
                ("20190709", "北京市昌平区建材城西路金燕龙写字楼", "1000002"),
                ("20190710", "北京市昌平区建材城西路金燕龙写字楼", "1000002")
              )
            )),
          )),
          div(cls := "panel point")(div(cls := "inner")(
            h3("点位分布统计"),
            div(cls := "chart")(
              div(cls := "pie"),
              dataItem(Seq(("320,11", "#ed3f35", "点位总数"), ("418", "#eacf19", "本月新增")))
            )
          ))
        ),
        div(cls := "column")(
          // 地图模块
          div(cls := "map")(
            h3(span(cls := "icon-cube"), "设备数据统计"),
            div(cls := "chart")(div(cls := "geo"))
          ),
          // 用户统计模块
          div(cls := "panel users")(div(cls := "inner")(
            h3("全国用户总量统计"),
            div(cls := "chart")(
              div(cls := "bar"),
              dataItem(Seq(("120,899", "#ed3f35", "用户总量"), ("248", "#eacf19", "本月新增")))
            )
          ))
        ),
        div(cls := "column")(
          // 订单
          div(cls := "panel order")(div(cls := "inner")(
            // 筛选
            div(cls := "filter")(
              a(href := "javascript:;", cls := "active")("365天"),
              Seq("90天", "30天", "24小时").map(s => a(href := "javascript:;")(s))
            ),
            // 数据
            dataItem(Seq(("20,301,987", "#ed3f35", "订单量"), ("99834", "#eacf19", "销售额(万元)")))
          )),
          // 销售额
          div(cls := "panel sales")(div(cls := "inner")(
            div(cls := "caption")(
              h3("销售额统计"),
              a(href := "javascript:;", cls := "active", attr("data-type") := "year")("年"),
              a(href := "javascript:;", attr("data-type") := "quarter")("季"),
              a(href := "javascript:;", attr("data-type") := "month")("月"),
              a(href := "javascript:;", attr("data-type") := "week")("周"),
            ),
            div(cls := "chart")(
              div(cls := "label")("单位：万"),
              div(cls := "line")
            )
          )),
          // 渠道分布以及季度销售模块
          div(cls := "wrap")(
            div(cls := "panel channel")(div(cls := "inner")(
              h3("渠道分布"),
              div(cls := "data")(div(cls := "radar"))
            )),
            // 销售进度模块
            div(cls := "panel quarter")(div(cls := "inner")(
              h3("一季度销售进度"),
              div(cls := "chart")(
                div(cls := "box")(div(cls := "gauge"), div(cls := "label")(50, small(" %"))),
                dataItem(Seq(("1,321", "#6acca3", "销售额(万元)"), ("150%", "#ed3f35", "同比增长")))
              )
            ))
          ),
          // 全国热榜模块制作
          div(cls := "panel top")(div(cls := "inner")(
            div(cls := "all")(
              h3("全国热榜"),
              ul(Seq(
                ("icon-cup1", "#d93f36", "可爱多"),
                ("icon-cup2", "#68d8fe", "娃哈哈"),
                ("icon-cup3", "#4c9bfd", "喜之郎")
              ).map{ case (clazz, color, text) => li(i(cls := clazz, inlineStyle := s"color: $color;"), text) })
            ),
            div(cls := "province")(
              h3("各省热销", i(cls := "date")("// 近30日 //")),
              div(cls := "data")(ul(cls := "sup"), ul(cls := "sub"))
            )
          ))
        ),
      )
    )
  )

  def dataItem(data: Seq[(String, String, String)]): Modifier = div(cls := "data")(data.map {
    case (number, color, text) => div(cls := "item")(
      h4(number),
      span(i(cls := "icon-dot", inlineStyle := s"color: $color;"), text)
    )
  })

  def monitorContent(heads: Seq[String], marquees: Seq[(String, String, String)]): Modifier = modifier(
    div(cls := "head")(heads.map(s => span(cls := "col")(s))),
    div(cls := "marquee-view")(div(cls := "marquee")(marquees.map { case (date, addr, code) => div(cls := "row")(
      span(cls := "col")(date),
      span(cls := "col")(addr),
      span(cls := "col")(code),
      span(cls := "icon-dot"),
    )}))
  )

end DataVisualizationPage
object DataVisualizationPage:
  object Text extends DataVisualizationPage(scalatags.Text)
end DataVisualizationPage
