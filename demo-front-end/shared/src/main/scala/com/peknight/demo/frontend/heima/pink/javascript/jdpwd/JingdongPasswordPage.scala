package com.peknight.demo.frontend.heima.pink.javascript.jdpwd

import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class JingdongPasswordPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
        style(JingdongPasswordStyles.render[String]),
        script(`type` := "text/javascript", src := "/main.js"),
        title("仿京东显示隐藏密码"),
      ),
      body(
        div(cls := "box")(
          label(`for` := "")(img(src := "/day1/images/close.png")),
          input(`type` := "password"),
        ),
        script("jdpwd()"),
      )
    )

end JingdongPasswordPage
object JingdongPasswordPage:
  object Text extends JingdongPasswordPage(scalatags.Text)
end JingdongPasswordPage
