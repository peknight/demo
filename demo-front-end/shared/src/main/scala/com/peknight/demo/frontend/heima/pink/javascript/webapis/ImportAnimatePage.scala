package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.ImportAnimatePage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class ImportAnimatePage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "引用animate动画函数"
  override def javaScriptMethod: Option[String] = Some("importAnimate")
  override def bodyFrag = div(cls := "slider-bar")(span("←"), div(cls := "con")("问题反馈"))

end ImportAnimatePage
object ImportAnimatePage:
  object Text extends ImportAnimatePage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    ".slider-bar" - (
      position.fixed,
      right.`0`,
      bottom(100.px),
      width(40.px),
      height(40.px),
      textAlign.center,
      lineHeight(40.px),
      cursor.pointer,
      color(c"#fff")
    )
    ".con" - (
      position.absolute,
      left.`0`,
      top.`0`,
      width(200.px),
      height(40.px),
      backgroundColor.purple,
      zIndex(-1)
    )
  end Styles
end ImportAnimatePage
