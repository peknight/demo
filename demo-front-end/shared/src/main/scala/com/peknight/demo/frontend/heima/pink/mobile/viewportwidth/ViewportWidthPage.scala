package com.peknight.demo.frontend.heima.pink.mobile.viewportwidth

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class ViewportWidthPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag = simplePage("VW测试")(
    style(raw(ViewportWidthStyles.render[String])),
  )(
    div(
      "茶"
    )
  )

end ViewportWidthPage
object ViewportWidthPage:
  object Text extends ViewportWidthPage(scalatags.Text)
end ViewportWidthPage

