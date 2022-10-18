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
    123,
  )

end CtripPage
object CtripPage:
  object Text extends CtripPage(scalatags.Text)
end CtripPage

