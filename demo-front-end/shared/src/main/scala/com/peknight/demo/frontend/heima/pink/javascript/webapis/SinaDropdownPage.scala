package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.SinaDropdownPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class SinaDropdownPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)

  override def scripts: Seq[String] = Seq("/webjars/jquery/3.7.1/jquery.min.js")
  override def headTitle: String = "新浪下拉菜单"
  override def javaScriptMethod: Option[String] = Some("sinaDropdown")
  override def bodyFrag = ul(cls := "nav")(List.fill(4)(li(
    a(href := "#")("微博"),
    ul(Seq("私信", "评论", "@我").map(s => li(a(href := "#")(s))))
  )))

end SinaDropdownPage
object SinaDropdownPage:
  object Text extends SinaDropdownPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "li" - style(listStyleType := "none")
    "a" - (textDecoration := "none", fontSize(14.px))
    ".nav" - margin(100.px)
    ".nav>li" - (
      position.relative,
      float.left,
      width(80.px),
      height(41.px),
      textAlign.center,
    )
    ".nav li a" - (
      display.block,
      width(100.%%),
      height(100.%%),
      lineHeight(41.px),
      color(c"#333"),
    )
    ".nav>li>a".hover - backgroundColor(c"#eee")
    ".nav ul" - (
      display.none,
      position.absolute,
      top(41.px),
      left.`0`,
      width(100.%%),
      borderLeft(1.px, solid, c"#fecc5b"),
      borderRight(1.px, solid, c"#fecc5b")
    )
    ".nav ul li" - borderBottom(1.px, solid, c"#fecc5b")
    ".nav ul li a".hover - backgroundColor(c"#fff5da")
  end Styles
end SinaDropdownPage
