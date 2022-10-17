package com.peknight.demo.frontend.heima.pink.mobile.ctrip

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class CtripPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, user-scalable=no, initial-scale=1.0," +
          "maximum-scale=1.0, minimum-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        // 引入我们的css初始化文件
        link(rel := "stylesheet", href := "/css/normalize.css"),
        // 引入我们首页的css
        link(rel := "stylesheet", href := "/css/ctrip.css"),
        title("携程在手，说走就走"),
      ),
      body(
        123,
      )
    )

end CtripPage
object CtripPage:
  object Text extends CtripPage(scalatags.Text)
end CtripPage

