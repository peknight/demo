package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.webapis.CloseTaobaoQRCodePage.Styles
import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class CloseTaobaoQRCodePage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "关闭淘宝二维码案例"
  override def javaScriptMethod: Option[String] = Some("closeTaobaoQRCode")
  override def bodyFrag = div(cls := "box")(
    "淘宝二维码",
    img(src := "/webapis/images/tao.png"),
    i(cls := "close-btn")("x")
  )

end CloseTaobaoQRCodePage
object CloseTaobaoQRCodePage:
  object Text extends CloseTaobaoQRCodePage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    ".box" - (
      position.relative,
      width(74.px),
      height(88.px),
      border(1.px, solid, c"#ccc"),
      margin(100.px, auto),
      fontSize(12.px),
      textAlign.center,
      color(c"#f40"),
    )
    ".box img" - (width(60.px), marginTop(5.px))
    ".close-btn" - (
      position.absolute,
      top(-1.px),
      left(-16.px),
      width(14.px),
      height(14.px),
      border(1.px, solid, c"#ccc"),
      lineHeight(14.px),
      fontFamily :=! "Arial, Helvetica, sans-serif",
      cursor.pointer,
    )
  end Styles
end CloseTaobaoQRCodePage
