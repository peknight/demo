package com.peknight.demo.frontend.heima.pink.mobile.flowlayout

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class FlowLayoutPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):

  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val flowLayoutPage: Frag = simplePage("流式布局")(style(FlowLayoutStyles.render[String]))(
    section(cls := "demo-1")(
      div(),
      div(),
    ),
  )

end FlowLayoutPage
object FlowLayoutPage:
  object Text extends FlowLayoutPage(scalatags.Text)
end FlowLayoutPage

