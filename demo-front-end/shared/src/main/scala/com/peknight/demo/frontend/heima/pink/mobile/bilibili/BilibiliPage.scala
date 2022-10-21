package com.peknight.demo.frontend.heima.pink.mobile.bilibili

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class BilibiliPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends BasePage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val index: Frag = simplePage("哔哩哔哩")(
    style(raw(BilibiliStyles.render[String])),
    link(rel := "stylesheet", href := "/bilibili/fonts/iconfont.css"),
  )(
    div(cls := "suspension-box")(
      div(cls := "m-navbar")(
        a(href := "#", cls := "logo")(
          i(cls := "iconfont Navbar_logo")
        ),
        div(cls := "right")(
          a(href := "#")(
            i(cls := "iconfont ic_search_tab"),
          ),
          a(href := "#")(img(src := "/bilibili/images/login.png")),
          a(href := "#")(img(src := "/bilibili/images/download.png")),
        ),
      ),
      div(cls := "channel-menu")(),
    )
  )

end BilibiliPage
object BilibiliPage:
  object Text extends BilibiliPage(scalatags.Text)
end BilibiliPage

