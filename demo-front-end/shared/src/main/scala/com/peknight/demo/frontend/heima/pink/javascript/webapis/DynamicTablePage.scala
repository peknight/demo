package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.DynamicTablePage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class DynamicTablePage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "动态生成表格案例"
  override def javaScriptMethod: Option[String] = Some("dynamicTable")
  override def bodyFrag = table(attr("cellspacing") := "0")(
    thead(tr(Seq("姓名", "科目", "成绩", "操作").map(th(_)))),
    tbody()
  )

end DynamicTablePage
object DynamicTablePage:
  object Text extends DynamicTablePage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "table" - (
      width(500.px),
      margin(100.px, auto),
      borderCollapse.collapse,
      textAlign.center,
    )
    "td, th" - border(1.px, solid, c"#333")
    "thead tr" - (height(40.px), backgroundColor(c"#ccc"))
  end Styles
end DynamicTablePage
