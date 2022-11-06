package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.ScrollTopPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class ScrollTopPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "jQuery被卷去的头部"
  override def javaScriptMethod: Option[String] = Some("scrollTopDemo")
  override def bodyFrag = frag(
    div(cls := "back")("返回顶部"),
    div(cls := "container")
  )

end ScrollTopPage
object ScrollTopPage:
  object Text extends ScrollTopPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "body" - height(2000.px)
    ".back" - (
      position.fixed,
      width(50.px),
      height(50.px),
      backgroundColor.pink,
      right(30.px),
      bottom(100.px),
      display.none,
    )
    ".container" - (
      width(900.px),
      height(500.px),
      backgroundColor.skyblue,
      margin(400.px, auto)
    )
  end Styles
end ScrollTopPage
