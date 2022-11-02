package com.peknight.demo.frontend.heima.pink.javascript.jquery

import com.peknight.demo.frontend.heima.pink.javascript.jquery.AccordionPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class AccordionPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JQueryPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "手风琴案例"
  override def javaScriptMethod: Option[String] = Some("accordion")
  override def bodyFrag = div(cls := "king")(ul(
    li(cls := "current")(a(href := "#")(
      img(src := "/jquery/images/m1.jpg", cls := "small"),
      img(src := "/jquery/images/m.png", cls := "big"))
    ),
    Seq("l", "c", "w", "z", "h", "t").map(s => li(a(href := "#")(
      img(src := s"/jquery/images/${s}1.jpg", cls := "small"),
      img(src := s"/jquery/images/$s.png", cls := "big"))
    ))
  ))

end AccordionPage
object AccordionPage:
  object Text extends AccordionPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    "img" - display.block
    "ul" - style(listStyle := "none")
    ".king" - (
      width(852.px),
      margin(100.px, auto),
      background := "url('/jquery/images/bg.png') no-repeat",
      overflow.hidden,
      padding(10.px),
    )
    ".king ul" - overflow.hidden
    ".king li" - (
      position.relative,
      float.left,
      width(69.px),
      height(69.px),
      marginRight(10.px),
    )
    ".king li.current" - width(224.px)
    ".king li.current .big" - display.block
    ".king li.current .small" - display.none
    ".big" - (width(224.px), display.none)
    ".small" - (
      position.absolute,
      top.`0`,
      left.`0`,
      width(69.px),
      height(69.px),
      borderRadius(5.px),
    )
  end Styles
end AccordionPage
