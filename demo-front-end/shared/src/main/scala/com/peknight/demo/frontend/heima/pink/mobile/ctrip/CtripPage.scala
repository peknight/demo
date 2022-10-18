package com.peknight.demo.frontend.heima.pink.mobile.ctrip

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class CtripPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag = simplePage("携程在手，说走就走")(
    // 引入我们的css初始化文件
    link(rel := "stylesheet", href := "/css/normalize.css"),
    // 引入我们首页的css
    link(rel := "stylesheet", href := "/css/ctrip.css")
  )(
    // 顶部搜索
    div(cls := "search-index")(
      div(cls := "search")("搜索：目的地/酒店/景点/航班号"),
      a(href := "#", cls := "user")("我 的"),
    ),
    // 焦点图模块
    div(cls := "focus")(
      img(src := "/flexlayout/upload/focus.jpg"),
    ),
    // 局部导航栏
    ul(cls := "local-nav")(
      Seq("景点·玩乐", "周边游", "美食林", "一日游", "当地攻略").zipWithIndex.map { case (s, index) =>
        li(a(href := "#", inlineTitle := s)(span(cls := s"local-nav-icon-icon${index + 1}"), span(s)))
      }
    ),
    // 主导航栏
    nav(Seq(
      ("酒店", "海外酒店", "特价酒店", "团购", "民宿·客栈"),
      ("机票", "火车票", "特价机票", "汽车票·船票", "专车·租车"),
      ("旅游", "门票", "目的地攻略", "邮轮旅行", "定制旅行")
    ).map(tuple =>
      div(cls := "nav-common")(
        div(cls := "nav-items")(
          a(href := "#", inlineTitle := tuple._1)(tuple._1),
        ),
        div(cls := "nav-items")(
          a(href := "#", inlineTitle := tuple._2)(tuple._2),
          a(href := "#", inlineTitle := tuple._3)(tuple._3),
        ),
        div(cls := "nav-items")(
          a(href := "#", inlineTitle := tuple._4)(tuple._4),
          a(href := "#", inlineTitle := tuple._5)(tuple._5),
        ),
      ),
    )),
    // 侧导航栏
    ul(cls := "subnav-entry")(Seq(
      "WIFI电话卡", "保险·签证", "外币兑换", "购物", "当地向导", "自由行", "境外玩乐", "礼品卡", "信用卡", "更多"
    ).map(s => li(a(href := "#")(span(cls := "subnav-entry-icon"), span(s))))),
    // 销售模块
    div(cls := "sales-box")(
      div(cls := "sales-hd")(
        h2("热门活动"),
        a(href := "#", cls := "more")("获取更多福利"),
      ),
      div(cls := "sales-bd")(for i <- 0 to 2 yield div(cls := "row")(
        for j <- 1 to 2 yield a(href := "#")(img(src := s"/flexlayout/upload/pic${i * 2 + j}.jpg"))
      )),
    ),
  )

end CtripPage
object CtripPage:
  object Text extends CtripPage(scalatags.Text)
end CtripPage

