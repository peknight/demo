package com.peknight.demo.frontend.heima.pink.mobile.suning

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class SuningPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag = skeleton(link(rel := "stylesheet", href := "/css/common.css"))

  val flexible: Frag = skeleton(
    style(SuningFlexibleMediaStyles.render[String]),
    script(src := "/webjars/amfe-flexible/2.2.1/index.min.js")
  )

  def skeleton(heads: Modifier*): Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, user-scalable=no, initial-scale=1.0," +
          "maximum-scale=1.0, minimum-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        title("苏宁"),
        link(rel := "stylesheet", href := "/css/normalize.css"),
        heads,
        // index.css
        link(rel := "stylesheet", href := "/css/suning.css"),
      ),
      body(
        // 顶部搜索框
        div(cls := "search-content")(
          a(href := "#", cls := "classify"),
          div(cls := "search")(form(input(`type` := "search", placeholder := "厨卫保暖季 每千减百"))),
          a(href := "#", cls := "login")("登录"),
        ),
        // banner部分
        div(cls := "banner")(
          img(src := "/suning/upload/banner.gif"),
        ),
        // 广告部分
        div(cls := "ad")(
          // 不出图可能是因为被去广告插件屏蔽了
          for i <- 1 to 3 yield a(href := "#")(img(src := s"/suning/upload/ad$i.gif")),
        ),
        // nav模块
        nav(Seq("爆款手机", "苏宁超市", "生活家电", "苏宁拼购", "母婴玩具", "大聚惠", "赚钱", "领云钻", "苏宁家电", "分类").map(s =>
          a(href := "#")(img(src := "/suning/upload/nav1.png"), span(s))
        )),
      )
    )

end SuningPage
object SuningPage:
  object Text extends SuningPage(scalatags.Text)
end SuningPage

