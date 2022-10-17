package com.peknight.demo.frontend.goat.login

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class LoginPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def login: Frag = simplePage("山羊的前端小窝")(style(LoginStyles.render[String]))(
    div(cls := "login")(div(cls := "box")(
      p(cls := "table")("Login"), br(),
      input(`type` := "text", placeholder := "账号"),
      input(`type` := "password", placeholder := "密码"), br(),
      a(href := "#", cls := "go")("GO")
    )),
  )

end LoginPage
object LoginPage:
  object Text extends LoginPage(scalatags.Text)
end LoginPage
