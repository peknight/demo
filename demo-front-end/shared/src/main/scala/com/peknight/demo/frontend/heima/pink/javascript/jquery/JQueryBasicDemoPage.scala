package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.JQueryBasicDemoPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class JQueryBasicDemoPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "jQuery的基本使用"
  override def javaScriptMethod: Option[String] = Some("jQueryBasicDemo")
  override def bodyFrag = div()

end JQueryBasicDemoPage
object JQueryBasicDemoPage:
  object Text extends JQueryBasicDemoPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "div" - (
      width(200.px),
      height(200.px),
      backgroundColor.pink,
    )
  end Styles
end JQueryBasicDemoPage
