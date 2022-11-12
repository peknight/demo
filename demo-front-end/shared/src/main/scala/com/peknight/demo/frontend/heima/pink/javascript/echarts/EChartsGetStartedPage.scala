package com.peknight.demo.frontend.heima.pink.javascript.echarts

import com.peknight.demo.frontend.heima.pink.javascript.echarts.EChartsGetStartedPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class EChartsGetStartedPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends EChartsPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "ECharts - Get Started"
  override def javaScriptMethod: Option[String] = Some("echartsGetStarted")
  override def bodyFrag = frag(
    div(id := "main"),
    div(id := "pie-simple"),
  )
end EChartsGetStartedPage
object EChartsGetStartedPage:
  object Text extends EChartsGetStartedPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "#main" - (
      width(400.px),
      height(400.px),
      backgroundColor.pink,
    )
    "#pie-simple" - (
      width(400.px),
      height(400.px),
      backgroundColor.pink,
    )
  end Styles
end EChartsGetStartedPage
