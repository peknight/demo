package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.DragElementPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class DragElementPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "移动端拖动元素"
  override def javaScriptMethod: Option[String] = Some("dragElement")
  override def bodyFrag = div()

end DragElementPage
object DragElementPage:
  object Text extends DragElementPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "div" - (
      position.absolute,
      left.`0`,
      width(100.px),
      height(100.px),
      backgroundColor.pink,
    )
  end Styles
end DragElementPage
