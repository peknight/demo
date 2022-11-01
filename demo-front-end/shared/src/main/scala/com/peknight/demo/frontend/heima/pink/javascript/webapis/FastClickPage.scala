package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.FastClickPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class FastClickPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def scripts: Seq[String] = Seq("/webjars/fastclick/1.0.6/lib/fastclick.js")
  override def headTitle: String = "fastclick插件使用"
  override def javaScriptMethod: Option[String] = Some("fastClick")
  override def bodyFrag = div()

end FastClickPage
object FastClickPage:
  object Text extends FastClickPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "div" - (
      width(50.px),
      height(50.px),
      backgroundColor.pink,
    )
  end Styles
end FastClickPage
