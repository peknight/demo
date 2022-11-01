package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.SomersaultCloudPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class SomersaultCloudPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "筋斗云导航栏"
  override def javaScriptMethod: Option[String] = Some("somersaultCloud")
  override def bodyFrag = div(id := "c-nav", cls := "c-nav")(span(cls := "cloud"), ul(
    li(cls := "current")(a(href := "#")("首页新闻")),
    Seq("师资力量", "活动策划", "企业文化", "招聘信息", "公司简介", "我是佩奇", "啥是佩奇").map(s => li(a(href := "#")(s)))
  ))

end SomersaultCloudPage
object SomersaultCloudPage:
  object Text extends SomersaultCloudPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "ul" - style(listStyle := "none")
    "body" - backgroundColor.black
    ".c-nav" - (
      width(900.px),
      height(42.px),
      background := "#fff url('/webapis/images/rss.png') no-repeat right center",
      margin(100.px, auto),
      borderRadius(5.px),
      position.relative,
    )
    ".c-nav ul" - position.absolute
    ".c-nav li" - (
      float.left,
      width(83.px),
      textAlign.center,
      lineHeight(42.px)
    )
    ".c-nav li a" - (
      color(c"#333"),
      textDecoration := "none",
      display.inlineBlock,
      height(42.px),
      &.hover - color.white
    )
    ".c-nav li.current a" - color(c"#0dff1d")
    ".cloud" - (
      position.absolute,
      left.`0`,
      top.`0`,
      width(83.px),
      height(42.px),
      background := "url('/webapis/images/cloud.gif') no-repeat"
    )
  end Styles
end SomersaultCloudPage
