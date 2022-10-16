package com.peknight.demo.frontend.heima.pink.mobile.jd

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class JingdongPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):

  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag = simplePage("京东")(style(JingdongStyles.render[String]))(
    div(),
  )

end JingdongPage
object JingdongPage:
  object Text extends JingdongPage(scalatags.Text)
end JingdongPage

