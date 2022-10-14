package com.peknight.demo.frontend.heima.pink.pinyougou.page

import scalatags.generic.Bundle

class ListPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends CommonPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def list: Frag =
    html(lang := "zh-CN")(
      headFrag("列表页", link(rel := "stylesheet", href := "/css/list.css")),
      body(
        shortcutFrag,
        headerFrag(secondKillFrag),
        navFrag(listNavFrag),
        secKillContainerFrag,
        footerFrag
      )
    )

  // 列表页的秒杀模块
  private[this] val secondKillFrag: Modifier =
    div(cls := "sk")(img(src := "/uploads/secKill_03.png"))

  private[this] val listNavFrag: Modifier =
    modifier(
      div(cls := "sk-list")(ul(Seq("品优秒杀", "即将售罄", "超值低价").map(s => li(a(href := "#")(s))))),
      div(cls := "sk-con")(ul(Seq(
        "女装", "女鞋", "男装", "男鞋", "母婴童装", "食品", "智能数码", "运动户外", "更多分类"
      ).zipWithIndex.map {
        case (s, index) if index == 1 => li(a(href := "#", cls := "style-red")(s))
        case (s, _)  => li(a(href := "#")(s))
      }))
    )

  // 列表页主体部分
  private[this] val secKillContainerFrag: Modifier =
    div(cls := "w sk-container")(
      div(cls := "sk-hd")(img(src := "/uploads/bg_03.png")),
      div(cls := "sk-bd")(ul(cls := "clearfix")(for _ <- 1 to 9 yield li(
        a(href := "#")(img(src := "/uploads/mobile.png")),
        div(cls := "item-info")(
          h3("Apple苹果iPhone 6s Plus (A1699) 32G 金色 移动联通电信4G手机"),
          div(cls := "price")(
            span(cls := "unit style-red")("￥"),
            span(cls := "promotion-price style-red")(6088),
            " ",
            span(cls := "origin-price")("￥6988")
          ),
          div(cls := "inventory clearfix")(
            div(cls := "fl")("已售87%"),
            div(cls := "progress-bar")(
              div(cls := "progress-bar-in"),
            ),
            div(cls := "fl")("剩余", span(cls := "style-red")(29), "件")
          )
        ),
        button(cls := "buy")("立即抢购")
      )))
    )

end ListPage
object ListPage:
  object Text extends ListPage(scalatags.Text)
end ListPage
