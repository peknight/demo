package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.JingdongTrackingNumPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class JingdongTrackingNumPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "模拟京东快递单号查询案例"
  override def javaScriptMethod: Option[String] = Some("jdTrackNum")
  override def bodyFrag = div(cls := "search")(
    div(cls := "con")(123),
    input(`type` := "text", placeholder := "请输入您的快递单号", cls := "jd")
  )

end JingdongTrackingNumPage
object JingdongTrackingNumPage:
  object Text extends JingdongTrackingNumPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    ".search" - (position.relative, width(178.px), margin(100.px))
    ".con" - (
      display.none,
      position.absolute,
      top(-40.px),
      width(171.px),
      border(1.px, solid, rgba(0, 0, 0, .2)),
      boxShadow := "0 2px 4px rgba(0,0,0,.2)",
      padding(5.px, `0`),
      fontSize(18.px),
      lineHeight(20.px),
      color(c"#333"),
      &.before - (
        content.string(""),
        width.`0`,
        height.`0`,
        position.absolute,
        top(28.px),
        left(18.px),
        border(8.px, solid, c"#000"),
        borderStyle(solid, dashed, dashed),
        borderColor(c"#fff", transparent, transparent),
      )
    )
  end Styles
end JingdongTrackingNumPage
