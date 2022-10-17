package com.peknight.demo.frontend.heima.pink.mobile.jd

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class JingdongPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, user-scalable=no, initial-scale=1.0," +
          "maximum-scale=1.0, minimum-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        title("京东"),
        // 引入我们的css初始化文件
        link(rel := "stylesheet", href := "/css/normalize.css"),
        // 引入我们首页的css
        link(rel := "stylesheet", href := "/css/index.css"),
      ),
      body(
        // 顶部
        header(cls := "open-app")(ul(
          li(img(src := "/jd/images/close.png")),
          li(img(src := "/jd/images/logo.png")),
          li("打开京东App，购物更轻松"),
          li("立即打开"),
        )),
        // 搜索
        div(cls := "search-wrap")(
          div(cls := "search-btn"),
          div(cls := "search")(
            div(cls := "jd-icon"),
            div(cls := "magnifier-icon"),
            input(`type` := "text", placeholder := "好奇x蔡康永新书《薄学》免费送"),
          ),
          div(cls := "search-login")("登录"),
        ),
        // 主体内部部分
        div(cls := "main-content")(
          // 滑动图
          div(cls := "slider")(
            img(src := "/jd/upload/banner.dpg")
          ),
          // 小家电品牌日
          div(cls := "brand")(
            div(a(href := "#")(img(src := "/jd/upload/pic11.dpg"))),
            div(a(href := "#")(img(src := "/jd/upload/pic22.dpg"))),
            div(a(href := "#")(img(src := "/jd/upload/pic33.dpg"))),
          ),
          // nav部分
          nav(cls := "clearfix")(for i <- 0 to 9 yield a(href := "#")(
            img(src := s"/jd/upload/nav${i % 3 + 1}.webp"),
            span("京东超市")
          )),
          // 新闻模块
          div(cls := "news")(
            a(href := "#")(img(src := "/jd/upload/new1.dpg")),
            a(href := "#")(img(src := "/jd/upload/new2.dpg")),
            a(href := "#")(img(src := "/jd/upload/new3.dpg")),
          )
        ),
      )
    )

end JingdongPage
object JingdongPage:
  object Text extends JingdongPage(scalatags.Text)
end JingdongPage

