package com.peknight.demo.frontend.heima.pink.mobile.flowlayout

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class SpecialPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):

  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val specialPage: Frag = simplePage("移动端特殊样式")(style(SpecialStyles.render[String]))(
    div(cls := "demo-1")(a(href := "#")("链接测试")),
    div(cls := "demo-2")(input(`type` := "button", value := "按钮")),
  )

end SpecialPage
object SpecialPage:
  object Text extends SpecialPage(scalatags.Text)
end SpecialPage

