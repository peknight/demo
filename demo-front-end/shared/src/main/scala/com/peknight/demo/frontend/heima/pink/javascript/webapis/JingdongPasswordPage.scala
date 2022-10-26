package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.webapis.JingdongPasswordPage.Styles
import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class JingdongPasswordPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "仿京东显示隐藏密码"
  override def javaScriptMethod: Option[String] = Some("jdpwd")
  override def bodyFrag = div(cls := "box")(
    label(`for` := "")(img(src := "/webapis/images/close.png", id := "eye")),
    input(`type` := "password", id := "pwd")
  )

end JingdongPasswordPage
object JingdongPasswordPage:
  object Text extends JingdongPasswordPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    ".box" - (
      position.relative,
      width(400.px),
      borderBottom(1.px, solid, c"#ccc"),
      margin(100.px, auto),
    )

    ".box input" - (
      width(370.px),
      height(30.px),
      border.`0`,
      outline.none,
    )

    ".box img" - (
      position.absolute,
      top(2.px),
      right(2.px),
      width(24.px),
    )
  end Styles
end JingdongPasswordPage
