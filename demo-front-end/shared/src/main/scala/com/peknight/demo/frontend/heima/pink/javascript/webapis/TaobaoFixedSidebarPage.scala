package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.TaobaoFixedSidebarPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class TaobaoFixedSidebarPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "仿淘宝固定侧边栏"
  override def javaScriptMethod: Option[String] = Some("taobaoFixedSidebar")
  override def bodyFrag = frag(
    div(cls := "slider-bar")(span(cls := "go-back")("返回顶部")),
    div(cls := "header w")("头部区域"),
    div(cls := "banner w")("banner区域"),
    div(cls := "main w")("主体部分"),
  )

end TaobaoFixedSidebarPage
object TaobaoFixedSidebarPage:
  object Text extends TaobaoFixedSidebarPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    ".slider-bar" - (
      position.absolute,
      left(50.%%),
      top(300.px),
      marginLeft(600.px),
      width(45.px),
      height(130.px),
      backgroundColor.pink
    )
    ".w" - (width(1200.px), margin(10.px, auto))
    ".header" - (height(150.px), backgroundColor.purple)
    ".banner" - (height(250.px), backgroundColor.skyblue)
    ".main" - (height(1000.px), backgroundColor.yellowgreen)
    "span" - (display.none, position.absolute, bottom.`0`)

  end Styles
end TaobaoFixedSidebarPage
