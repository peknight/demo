package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.SinaRegisterPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class SinaRegisterPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "仿新浪注册页面"
  override def javaScriptMethod: Option[String] = Some("sinaRegister")
  override def bodyFrag = div(cls := "register")(
    input(`type` := "password", cls := "ipt"),
    p(cls := "message")("请输入6~16位密码")
  )

end SinaRegisterPage
object SinaRegisterPage:
  object Text extends SinaRegisterPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "div" - (width(600.px), margin(100.px, auto))
    ".message" - (
      display.inlineBlock,
      fontSize(12.px),
      color(c"#999"),
      background := "url('/webapis/images/mess.png') no-repeat left center",
      paddingLeft(20.px),
    )
    ".wrong" - (color.red, backgroundImage := "url('/webapis/images/wrong.png')")
    ".right" - (color.green, backgroundImage := "url('/webapis/images/right.png')")
  end Styles
end SinaRegisterPage
