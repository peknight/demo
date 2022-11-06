package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.MasonryLayoutPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class MasonryLayoutPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override protected def scripts: Seq[String] = Seq("/masonry/pinterest_grid.js")
  override def headTitle: String = "瀑布流插件"
  override def javaScriptMethod: Option[String] = Some("masonryLayout")
  override def bodyFrag = section(id := "gallery-wrapper")(for i <- 1 to 15 yield
    tag("article")(cls := "white-panel")(
      img(src := s"/jquery/images/P_0${"%02d".format(i)}.jpg", cls := "thumb"),
      h1(a(href := "#")(s"Title $i")),
      p(s"Description $i")
    )
  )

end MasonryLayoutPage
object MasonryLayoutPage:
  object Text extends MasonryLayoutPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "#gallery-wrapper" - (
      position.relative,
      maxWidth(75.%%),
      width(75.%%),
      margin(50.px, auto),
    )
    "img.thumb" - (
      width(100.%%),
      maxWidth(100.%%),
    )
    ".white-panel" - (
      position.absolute,
      backgroundColor.white,
      borderRadius(5.px),
      boxShadow := "0px 1px 2px rgba(0,0,0,0.3)",
      padding(10.px),
      &.hover - (
        boxShadow := "1px 1px 10px rgba(0,0,0,0.5)",
        marginTop(-5.px),
        transition := "all 0.3s ease-in-out"
      )
    )
    ".white-panel h1" - fontSize(1.em)
    ".white-panel h1 a" - color(c"#a92733")
  end Styles
end MasonryLayoutPage
