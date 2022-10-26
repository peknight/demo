package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.SelectAllPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class SelectAllPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "全选反选"
  override def javaScriptMethod: Option[String] = Some("selectAll")
  override def bodyFrag = div(cls := "wrap")(table(
    thead(tr(
      th(input(`type` := "checkbox", id := "j-cb-all")),
      th("商品"),
      th("价钱")
    )),
    tbody(id := "j-tb")(Seq(("iPhone8", 8000), ("iPad Pro", 5000), ("iPad Air", 2000), ("Apple Watch", 2000)).map {
      case (name, price) => tr(td(input(`type` := "checkbox")), td(name), td(price))
    })
  ))

end SelectAllPage
object SelectAllPage:
  object Text extends SelectAllPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "*" - (margin.`0`, padding.`0`)
    ".wrap" - (width(300.px), margin(100.px, auto, `0`))
    "table" - (
      borderCollapse.collapse,
      borderSpacing.`0`,
      border(1.px, solid, c"#c0c0c0"),
      width(300.px),
    )
    "th, td" - (border(1.px, solid, c"#d0d0d0"), color(c"#404060"), padding(10.px))
    "th" - (backgroundColor(c"#09c"), font := "bold 16px '微软雅黑'", color.white)
    "td" - style(font := "14px '微软雅黑'")
    "tbody tr" - (
      backgroundColor(c"#f0f0f0"),
      &.hover - (
        cursor.pointer,
        backgroundColor(c"#fafafa")
      )
    )
  end Styles
end SelectAllPage
