package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.BaiduChangeSkinPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class BaiduChangeSkinPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "百度换肤效果"
  override def javaScriptMethod: Option[String] = Some("baiduChangeSkin")
  override def bodyFrag = ul(cls := "baidu")(for i <- 1 to 4 yield li(img(src := s"/webapis/images/$i.jpg")))

end BaiduChangeSkinPage
object BaiduChangeSkinPage:
  object Text extends BaiduChangeSkinPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "body" - style(background := "url('/webapis/images/1.jpg') no-repeat center top")
    "li" - style(listStyle := "none")
    ".baidu" - (
      overflow.hidden,
      margin(100.px, auto),
      backgroundColor.white,
      width(410.px),
      paddingTop(3.px),
    )
    ".baidu li" - (float.left, margin(`0`, 1.px), cursor.pointer)
    ".baidu img" - width(100.px)
  end Styles
end BaiduChangeSkinPage
