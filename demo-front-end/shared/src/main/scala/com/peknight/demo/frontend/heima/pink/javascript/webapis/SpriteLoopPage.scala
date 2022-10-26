package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.webapis.SpriteLoopPage.Styles
import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class SpriteLoopPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "循环精灵图"
  override def javaScriptMethod: Option[String] = Some("spriteLoop")
  override def bodyFrag = div(cls := "box")(ul(for _ <- 1 to 12 yield li()))

end SpriteLoopPage
object SpriteLoopPage:
  object Text extends SpriteLoopPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "li" - style(listStyleType := "none")
    ".box" - (width(250.px), margin(100.px, auto))
    ".box li" - (
      float.left,
      width(24.px),
      height(24.px),
      margin(15.px),
      background := "url('/webapis/images/sprite.png') no-repeat"
    )
  end Styles
end SpriteLoopPage
