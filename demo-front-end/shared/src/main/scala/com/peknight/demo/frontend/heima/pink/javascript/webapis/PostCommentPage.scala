package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.PostCommentPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class PostCommentPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "简单版发布留言案例"
  override def javaScriptMethod: Option[String] = Some("postComment")
  override def bodyFrag = frag(
    textarea(),
    button("发布"),
    ul()
  )

end PostCommentPage
object PostCommentPage:
  object Text extends PostCommentPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "body" - padding(100.px)
    "textarea" - (
      width(200.px),
      height(100.px),
      border(1.px, solid, pink),
      outline.none,
      resize.none,
    )
    "ul" - marginTop(50.px)
    "li" - (
      width(300.px),
      padding(5.px),
      backgroundColor(rgb(245, 209, 243)),
      color.red,
      fontSize(14.px),
      margin(15.px, `0`)
    )
    "li a" - float.right
  end Styles
end PostCommentPage
