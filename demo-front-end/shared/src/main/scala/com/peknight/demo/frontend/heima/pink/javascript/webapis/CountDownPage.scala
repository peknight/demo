package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.CountDownPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class CountDownPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "倒计时效果"
  override def javaScriptMethod: Option[String] = Some("countDown")
  override def bodyFrag = div(
    span(cls := "hour")(1),
    span(cls := "minute")(2),
    span(cls := "second")(3),
  )

end CountDownPage
object CountDownPage:
  object Text extends CountDownPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "div" - margin(200.px)
    "span" - (
      display.inlineBlock,
      width(40.px),
      height(40.px),
      backgroundColor(c"#333"),
      fontSize(20.px),
      color(c"#fff"),
      textAlign.center,
      lineHeight(40.px),
    )
  end Styles
end CountDownPage
