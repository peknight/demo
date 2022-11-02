package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.HighLightShowPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class HighLightShowPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "高亮显示案例"
  override def javaScriptMethod: Option[String] = Some("highLightShow")
  override def bodyFrag = div(cls := "wrap")(ul(for i <- 1 to 6 yield
    li(a(href := "#")(img(src := s"/jquery/images/0$i.jpg")))
  ))

end HighLightShowPage
object HighLightShowPage:
  object Text extends HighLightShowPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "ul" - style(listStyle := "none")
    "body" - style(background := "#000")
    ".wrap" - (
      margin(100.px, auto, `0`),
      width(630.px),
      height(394.px),
      padding(10.px, `0`, `0`, 10.px),
      background := "#000",
      overflow.hidden,
      border(1.px, solid, c"#fff")
    )
    ".wrap li" - (
      float.left,
      margin(`0`, 10.px, 10.px, `0`)
    )
    ".wrap img" - (display.block, border.`0`)
  end Styles
end HighLightShowPage
