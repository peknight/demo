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
        123
      )
    )

end JingdongPage
object JingdongPage:
  object Text extends JingdongPage(scalatags.Text)
end JingdongPage

