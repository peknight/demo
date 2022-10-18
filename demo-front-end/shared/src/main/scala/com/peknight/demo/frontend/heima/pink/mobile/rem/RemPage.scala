package com.peknight.demo.frontend.heima.pink.mobile.rem

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class RemPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val rem: Frag = simplePage("rem适配方案")(style(RemMediaStyles.render[String]), style(RemStyles.render[String]))(
    div()
  )

end RemPage
object RemPage:
  object Text extends RemPage(scalatags.Text)
end RemPage

