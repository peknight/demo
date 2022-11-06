package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.WeiboPublishPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class WeiboPublishPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "微博发布效果"
  override def javaScriptMethod: Option[String] = Some("weiboPublish")
  override def bodyFrag = div(cls := "box", id := "weibo")(
    span("微博发布"),
    textarea(cls := "txt", cols := "30", rows := "10"),
    button(cls := "btn")("发布"),
    ul()
  )

end WeiboPublishPage
object WeiboPublishPage:
  object Text extends WeiboPublishPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "ul" - style(listStyle := "none")
    ".box" - (
      width(600.px),
      margin(100.px, auto),
      border(1.px, solid, c"#000"),
      padding(20.px),
    )
    "textarea" - (
      width(450.px),
      height(160.px),
      outline.none,
      resize.none
    )
    "ul" - (
      width(450.px),
      paddingLeft(80.px),
    )
    "ul li" - (
      lineHeight(25.px),
      borderBottom(1.px, dashed, c"#ccc"),
      display.none
    )
    "input" - float.right
    "ul li a" - float.right
  end Styles
end WeiboPublishPage
