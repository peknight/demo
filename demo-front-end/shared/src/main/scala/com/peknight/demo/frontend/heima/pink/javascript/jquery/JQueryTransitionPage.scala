package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.JQueryTransitionPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class JQueryTransitionPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "jQuery效果"
  override def javaScriptMethod: Option[String] = Some("jQueryTransition")
  override def bodyFrag = div(
    div(cls := "demo-show-hide")(
      Seq("显示", "隐藏", "切换(无效)").map(button(_)),
      div(),
    ),
    div(cls := "demo-slide")(
      Seq("下拉滑动", "上拉滑动", "切换滑动").map(button(_)),
      div(),
    ),
    div(cls := "demo-fade")(
      Seq("淡入效果", "淡出效果", "淡入淡出切换", "修改透明度").map(button(_)),
      div(),
    ),
    div(cls := "demo-animate")(
      button("动起来"),
      div(),
    ),
  )

end JQueryTransitionPage
object JQueryTransitionPage:
  object Text extends JQueryTransitionPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    ".demo-show-hide div" - (
      width(150.px),
      height(150.px),
      backgroundColor.pink,
    )
    ".demo-slide div, .demo-fade div" - (
      width(150.px),
      height(150.px),
      backgroundColor.pink,
      display.none
    )
    ".demo-animate" - position.relative
    ".demo-animate div" - (
      position.absolute,
      width(200.px),
      height(200.px),
      backgroundColor.pink
    )
  end Styles
end JQueryTransitionPage
