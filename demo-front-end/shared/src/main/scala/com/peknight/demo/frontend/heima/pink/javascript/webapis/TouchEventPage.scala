package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.TouchEventPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class TouchEventPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "触摸事件对象"
  override def javaScriptMethod: Option[String] = Some("touchEvent")
  override def bodyFrag = div()

end TouchEventPage
object TouchEventPage:
  object Text extends TouchEventPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "div" - (
      width(100.px),
      height(100.px),
      backgroundColor.pink
    )
  end Styles
end TouchEventPage
