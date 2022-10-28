package com.peknight.demo.frontend.heima.pink.javascript.webapis

import com.peknight.demo.frontend.heima.pink.javascript.page.JavaScriptPage
import com.peknight.demo.frontend.heima.pink.javascript.webapis.DragBoxPage.Styles
import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Attr, Length}
import scalatags.generic.Bundle

class DragBoxPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends JavaScriptPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  override def styles: Seq[StyleSheet.Base] = Seq(Styles)
  override def headTitle: String = "拖动的模态框"
  override def javaScriptMethod: Option[String] = Some("dragBox")
  override def bodyFrag = frag(
    div(cls := "login-header")(a(id := "link", href := "javascript:;")("点击，弹出登录框")),
    div(id := "login", cls := "login")(
      div(id := "title", cls := "login-title")(
        "登录会员",
        span(a(id := "closeBtn", href := "javascript:void(0);", cls := "close-login")("关闭"))
      ),
      div(cls := "login-input-content")(
        div(cls := "login-input")(
          label("用户名："),
          input(`type` := "text", placeholder := "请输入用户名", name := "info[username]", id := "username",
            cls := "list-input")
        ),
        div(cls := "login-input")(
          label("登录密码："),
          input(`type` := "password", placeholder := "请输入登录密码", name := "info[password]", id := "password",
            cls := "list-input")
        ),
      ),
      div(id := "loginBtn", cls := "login-button")(
        a(href := "javascript:void(0);", id := "login-button-submit")("登录会员")
      )
    ),
    // 遮盖层
    div(id := "bg", cls := "login-bg")
  )

end DragBoxPage
object DragBoxPage:
  object Text extends DragBoxPage(scalatags.Text)
  object Styles extends StyleSheet.Standalone:
    import dsl.*
    "ul,li,ol,dl,dt,dd,div,p,span,h1,h2,h3,h4,h5,h6,a" - (margin.`0`, padding.`0`)
    "a" - (textDecoration := "none", color.black)
    ".login-header" - (
      width(100.%%),
      textAlign.center,
      height(30.px),
      fontSize(24.px),
      lineHeight(30.px),
    )
    ".login" - (
      display.none,
      width(512.px),
      height(280.px),
      position.fixed,
      border(1.px, solid, c"#ebebeb"),
      left(50.%%),
      top(50.%%),
      backgroundColor.white,
      boxShadow := "0px 0px 20px #ddd",
      zIndex(9999),
      transform := "translate(-50%,-50%)",
    )
    ".login-title" - (
      width(100.%%),
      margin(10.px, `0`, `0`, `0`),
      textAlign.center,
      lineHeight(40.px),
      height(40.px),
      fontSize(18.px),
      position.relative,
      cursor.move,
    )
    ".login-title span" - (
      position.absolute,
      fontSize(12.px),
      right(-20.px),
      top(-30.px),
      backgroundColor.white,
      border(1.px, solid, c"#ebebeb"),
      width(40.px),
      height(40.px),
      borderRadius(20.px),
    )
    ".login-input-content" - marginTop(20.px)
    ".login-input" - (overflow.hidden, margin(`0`, `0`, 20.px, `0`))
    ".login-input label" - (
      float.left,
      width(90.px),
      paddingRight(10.px),
      textAlign.right,
      lineHeight(35.px),
      height(35.px),
      fontSize(14.px),
    )
    ".login-input input.list-input" - (
      float.left,
      lineHeight(35.px),
      height(35.px),
      width(350.px),
      border(1.px, solid, c"#ebebeb"),
      textIndent(5.px),
    )
    ".login-button" - (
      width(50.%%),
      margin(30.px, auto, `0`, auto),
      lineHeight(40.px),
      fontSize(14.px),
      border(1.px, solid, c"#ebebeb"),
      textAlign.center,
    )
    ".login-button a" - display.block
    ".login-bg" - (
      display.none,
      width(100.%%),
      height(100.%%),
      position.fixed,
      top.`0`,
      left.`0`,
      backgroundColor(rgba(0,0,0,.3)),
    )
  end Styles
end DragBoxPage
