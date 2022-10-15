package com.peknight.demo.frontend.heima.pink.mobile.flowlayout

import com.peknight.demo.frontend.page.BasePage
import scalacss.ProdDefaults.*
import scalatags.generic.Bundle

class Image2XPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT]) extends BasePage(bundle):

  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  val image2xPage: Frag = simplePage("2X Image")(style(Image2XStyles.render[String]))(
    div(cls := "demo-1"),
    div(cls := "demo-2")(
      // 模糊的
      img(src := "/flowlayout/images/apple50.jpg"),
      // 我们采取2倍图
      img(src := "/flowlayout/images/apple100.jpg"),
    ),
  )

end Image2XPage
object Image2XPage:
  object Text extends Image2XPage(scalatags.Text)
end Image2XPage

