package com.peknight.demo.frontend.heima.pink.pinyougou.page

import scalatags.generic.Bundle

class RegisterPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends CommonPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  // 注册页面 比较隐私，不需要SEO优化
  def register: Frag =
    html(lang := "zh-CN")(
      registerHeadFrag,
      body(
        div(cls := "w")(
          header(
            div(cls := "logo")(a(href := "index.html")(img(src := "/images/logo.png")))
          ),
          registerAreaFrag,
          footer(copyRightFrag)
        )
      )
    )

  private val registerHeadFrag: Modifier =
    head(
      metaFrag,
      title("个人注册"),
      // 引入favicon图标
      link(rel := "shortcut icon", href := "favicon.ico"),
      // 引入我们初始化的css
      link(rel := "stylesheet", href := "/css/base.css"),
      link(rel := "stylesheet", href := "/css/fonts.css"),
      // 引入我们自己的注册页面的css
      link(rel := "stylesheet", href := "/css/register.css"),
    )

  private val registerAreaFrag: Modifier =
    div(cls := "register-area")(
      h3("注册新用户", div(cls := "login")("我有账号，去", a(href := "#")("登录"))),
      div(cls := "reg-form")(form(action := "")(ul(
        li(
          label(`for` := "")("手机号："),
          input(`type` := "text", cls := "inp"),
          span(cls := "error")(" ", i(cls := "error-icon")("\uE905"), "手机号码格式不正确，请重新输入")
        ),
        li(
          label(`for` := "")("短信验证码："),
          input(`type` := "text", cls := "inp"),
          span(cls := "success")(" ", i(cls := "success-icon")("\uE906"), "短信验证码输入正确")
        ),
        li(
          label(`for` := "")("登录密码："),
          input(`type` := "password", cls := "inp"),
          span(cls := "error")(" ", i(cls := "error-icon")("\uE905"), "密码不少于6位数，请重新输入")
        ),
        li(cls := "safe")(
          "安全程度 ",
          em(cls := "weak")("弱"), " ",
          em(cls := "medium")("中"), " ",
          em(cls := "strong")("强")
        ),
        li(
          label(`for` := "")("确认密码："),
          input(`type` := "password", cls := "inp"),
          span(cls := "error")(" ", i(cls := "error-icon")("\uE905"), "密码不一致，请重新输入")
        ),
        li(cls := "agree")(
          input(`type` := "checkbox")(" 同意协议并注册 ", a(href := "#")("知晓用户协议"))
        ),
        li(
          input(`type` := "submit", value := "提交注册", cls := "btn")
        )
      )))
    )

end RegisterPage

object RegisterPage:
  object Text extends RegisterPage(scalatags.Text)
end RegisterPage

