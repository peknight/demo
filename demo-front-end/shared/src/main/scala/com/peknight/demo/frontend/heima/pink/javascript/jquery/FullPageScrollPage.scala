package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.FullPageScrollPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class FullPageScrollPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def links: Seq[String] = Seq("/fullpage/fullpage.min.css")
  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def scripts: Seq[String] = Seq("/fullpage/fullpage.min.js")
  override def headTitle: String = "全屏滚动插件"
  override def javaScriptMethod: Option[String] = Some("fullPageScroll")
  override def bodyFrag = div(id := "dowebok")(
    div(cls := "section section1")(
      h3("第一屏里面的内容")
    ),
    div(cls := "section section2")(
      h3("第二屏")
    ),
    div(cls := "section section3")(
      h3("第三屏")
    ),
    div(cls := "section section4")(
      h3("第四屏")
    ),
  )

end FullPageScrollPage
object FullPageScrollPage:
  object Text extends FullPageScrollPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "#fp-nav ul li a.active span, #fp-nav ul li a span" - backgroundColor.red.important
    for i <- 1 to 4 yield s".section$i" - style(background := s"url('http://idowebok.u.qiniudn.com/77/$i.jpg') 50%")
  end Styles
end FullPageScrollPage
