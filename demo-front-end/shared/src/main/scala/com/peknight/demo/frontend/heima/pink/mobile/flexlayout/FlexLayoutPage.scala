package com.peknight.demo.frontend.heima.pink.mobile.flexlayout

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class FlexLayoutPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):

  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val flexLayoutPage: Frag = simplePage("弹性布局")(style(FlexLayoutStyles.render[String]))(
    for i <- 1 to 2 yield div(cls := s"flex-$i")(for j <- 1 to 3 yield span(j)),
    div(cls := "flex-3")(for j <- 1 to 4 yield span(j)),
    div(cls := "flex-4")(for j <- 1 to 3 yield span(j)),
    for i <- 5 to 6 yield div(cls := s"flex-$i")(for j <- 1 to 5 yield span(j)),
    for i <- 7 to 8 yield div(cls := s"flex-$i")(for j <- 1 to 3 yield span(j)),
  )

end FlexLayoutPage
object FlexLayoutPage:
  object Text extends FlexLayoutPage(scalatags.Text)
end FlexLayoutPage

