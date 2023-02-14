package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.SwitchTablePage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class SwitchTablePage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as inlineStyle, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def scripts: Seq[String] = Seq("/webjars/jquery/3.6.3/jquery.min.js")
  override def headTitle: String = "tab栏切换"
  override def javaScriptMethod: Option[String] = Some("switchTable")
  override def bodyFrag = div(cls := "tab")(
    div(cls := "tab-list")(ul(
      li(cls := "current")("商品介绍"),
      Seq("规格与包装", "售后保障", "商品评价（50000）", "手机社区").map(li(_))
    )),
    div(cls := "tab-con")(
      div(cls := "item", inlineStyle := "display: block;")("商品介绍模块内容"),
      Seq("规格与包装模块内容", "售后保障模块内容", "商品评价（50000）模块内容", "手机社区模块内容").map(s => div(cls := "item")(s))
    )
  )

end SwitchTablePage
object SwitchTablePage:
  object Text extends SwitchTablePage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "li" - style(listStyleType := "none")
    ".tab" - (width(978.px), margin(100.px, auto))
    ".tab-list" - (height(39.px), border(1.px, solid, c"#ccc"), backgroundColor(c"#f1f1f1"))
    ".tab-list li" - (
      float.left,
      height(39.px),
      lineHeight(39.px),
      padding(`0`, 20.px),
      textAlign.center,
      cursor.pointer,
    )
    ".tab-list .current" - (backgroundColor(c"#c81623"), color.white)
    ".item" - display.none
  end Styles
end SwitchTablePage
