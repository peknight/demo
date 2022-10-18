package com.peknight.demo.frontend.heima.pink.mobile.less

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class LessPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val less: Frag = simplePage("Less但是ScalaCss - 变量")(link(rel := "stylesheet", href := "/css/less.css"))(
    div("我的颜色也是粉色")
  )

  val nest: Frag = simplePage("Less但是ScalaCss - 嵌套")(style(NestStyles.render[String]))(
    div(cls := "header")(a(href := "#")("文字")),
    div(cls := "nav")(div(cls := "logo")("嵌套起来"))
  )

  val count: Frag = simplePage("Less但是ScalaCss - 运算")(style(CountStyles.render[String]))(
    div(),
  )

end LessPage
object LessPage:
  object Text extends LessPage(scalatags.Text)
end LessPage

