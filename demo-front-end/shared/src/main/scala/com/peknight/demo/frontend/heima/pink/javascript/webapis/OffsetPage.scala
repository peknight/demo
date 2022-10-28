package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.OffsetPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class OffsetPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "Offset系列属性"
  override def javaScriptMethod: Option[String] = Some("offset")
  override def bodyFrag = frag(
    div(cls := "father")(div(cls := "son")),
    div(cls := "w"),
  )

end OffsetPage
object OffsetPage:
  object Text extends OffsetPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    ".father" - (
      width(200.px),
      height(200.px),
      backgroundColor(pink),
      margin(150.px),
      padding(30.px),
      border(5.px, solid, black)
    )
    ".son" - (
      boxSizing.borderBox,
      width(100.px),
      height(100.px),
      backgroundColor(purple),
      margin(45.px),
      padding(15.px),
      border(3.px, solid, black)
    )
    ".w" - (
      height(200.px),
      backgroundColor.skyblue,
      margin(`0`, auto, 200.px),
      padding(10.px),
      border(15.px, solid, red)
    )
  end Styles
end OffsetPage
