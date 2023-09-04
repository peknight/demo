package com.peknight.demo.frontend.heima.pink.pinyougou.page

import com.peknight.demo.frontend.heima.pink.pinyougou.domain.CartItem
import scalatags.generic.Bundle

class ShoppingCartPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends CommonPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def cart: Frag =
    html(lang := "zh-CN")(
      headFrag(
        "我的购物车",
        link(rel := "stylesheet", href := "/css/cart.css"),
        script(src := "/webjars/jquery/3.7.1/jquery.min.js"),
        script(src := "/main.js"),
        script("pinyougouCart()")
      ),
      body(
        shortcutFrag,
        div(cls := "cart-header")(div(cls := "w")(div(cls := "cart-logo")(img(src := "/images/logo.png"), b("购物车")))),
        div(cls := "c-container")(div(cls := "w")(
          div(cls := "cart-filter-bar")(em("全部商品")),
          div(cls := "cart-wrap")(
            // 头部全选模块
            div(cls := "cart-thead")(
              div(cls := "t-checkbox")(input(`type` := "checkbox", cls := "check-all"), " 全选"),
              Seq(("goods", "商品"), ("price", "单价"), ("num", "数量"), ("sum", "小计"), ("action", "操作")).map {
                case (c, t) => div(cls := s"t-$c")(t)
              },
            ),
            // 商品详细模块
            div(cls := "cart-item-list")(cartItems.map(cartItem)),
            // 结算模块
            div(cls := "cart-float-bar")(
              div(cls := "select-all")(input(`type` := "checkbox", cls := "check-all")("全选")),
              div(cls := "operation")(
                a(href := "javascript:;", cls := "remove-batch")("删除选中的商品"),
                a(href := "javascript:;", cls := "clear-all")("清理购物车"),
              ),
              div(cls := "toolbar-right")(
                div(cls := "amount-sum")("已经选", em(1), "件商品"),
                div(cls := "price-sum")("总价：", em("￥12.60")),
                div(cls := "btn-area")("去结算")
              )
            ),
          )
        )),
        footerFrag
      )
    )

  private[this] val cartItems: Seq[CartItem] = Seq(
    CartItem("【5本26.8元】经典儿童文学彩图青少版八十天环游地球中学生语言教学大纲", "p1", 12.60, 1, true),
    CartItem("【2000张贴纸】贴纸书 3-6岁 贴画儿童 贴画书全套12册 贴画 贴纸儿童 汽", "p2", 24.80, 1, false),
    CartItem("唐诗三百首+成语故事全2册 一年级课外书 精装注音儿童版 小学生二三年级课外阅读书籍", "p3", 29.80, 1, false),
  )

  private[this] def cartItem(item: CartItem): Modifier =
    div(cls := s"cart-item${if item.checked then " check-cart-item" else ""}")(
      div(cls := "p-checkbox")(
        input(`type` := "checkbox", if item.checked then checked := true else modifier(), cls := "j-checkbox")
      ),
      div(cls := "p-goods")(
        div(cls := "p-img")(img(src := s"/uploads/${item.image}.jpg")),
        div(cls := "p-msg")(item.title)
      ),
      div(cls := "p-price")("￥%.2f".format(item.price)),
      div(cls := "p-num")(div(cls := "quantity-form")(
        a(href := "javascript:;", cls := "decrement")("-"),
        input(`type` := "text", cls := "i-txt", value := item.num),
        a(href := "javascript:;", cls := "increment")("+"),
      )),
      div(cls := "p-sum")("￥%.2f".format(item.price * item.num)),
      div(cls := "p-action")(a(href := "javascript:;")("删除")),
    )

end ShoppingCartPage
object ShoppingCartPage:
  object Text extends ShoppingCartPage(scalatags.Text)
end ShoppingCartPage
