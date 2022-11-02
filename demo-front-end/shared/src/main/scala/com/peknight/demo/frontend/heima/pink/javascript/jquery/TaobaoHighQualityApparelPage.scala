package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.TaobaoHighQualityApparelPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class TaobaoHighQualityApparelPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "淘宝精品服饰案例"
  override def javaScriptMethod: Option[String] = Some("taobaoApparel")

  private[this] val linksAndImages: (Seq[Frag], Seq[Frag]) =
    Seq("女靴", "雪地靴", "冬裙", "呢大衣", "毛衣", "棉服", "女裤", "羽绒服", "牛仔裤").map[(Frag, Frag)](s => (
      li(a(href := "#")(s)),
      div(a(href := "#")(img(src := s"/jquery/images/$s.jpg", width := 200, height := 250)))
    )).unzip[Frag, Frag]

  override def bodyFrag = div(cls := "wrapper")(
    ul(id := "left")(linksAndImages._1),
    div(id := "content")(linksAndImages._2)
  )

end TaobaoHighQualityApparelPage
object TaobaoHighQualityApparelPage:
  object Text extends TaobaoHighQualityApparelPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`, fontSize(12.px))
    "ul" - style(listStyle := "none")
    "a" - style(textDecoration := "none")
    ".wrapper" - (
      width(250.px),
      height(248.px),
      margin(100.px, auto, `0`),
      border(1.px, solid, pink),
      borderRight.`0`,
      overflow.hidden
    )
    "#left, #content" - float.left
    "#left li" - style(background := "url('/jquery/images/lili.jpg') repeat-x")
    "#left li a" - (
      display.block,
      width(48.px),
      height(27.px),
      borderBottom(1.px, solid, pink),
      lineHeight(27.px),
      textAlign.center,
      color.black,
      &.hover - style(backgroundImage := "url('/jquery/images/abg.gif')")
    )
    "#content" - (borderLeft(1.px, solid, pink), borderRight(1.px, solid, pink))
  end Styles
end TaobaoHighQualityApparelPage
