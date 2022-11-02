package com.peknight.demo.frontend.heima.pink.pinyougou.page

import scalatags.generic.Bundle

class ShoppingCartPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends CommonPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def cart: Frag =
    html(lang := "zh-CN")(
      headFrag("我的购物车", link(rel := "stylesheet", href := "/css/cart.css")),
      body(
        shortcutFrag,
        headerFrag(),
        footerFrag
      )
    )

end ShoppingCartPage
object ShoppingCartPage:
  object Text extends ShoppingCartPage(scalatags.Text)
end ShoppingCartPage
