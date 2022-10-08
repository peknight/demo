package com.peknight.demo.frontend.heima.pink.pinyougou.page

import com.peknight.demo.frontend.heima.pink.pinyougou.style.{BaseStyles, FontsStyles, PinyougouStyles}
import org.http4s.Uri
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalatags.generic.Bundle
import scalatags.text.Builder

class PinyougouPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{style as _, title as inlineTitle, *}
  import bundle.tags2.{nav, style, title}

  val index: Frag =
    html(lang := "zh-CN")(
      head(
        meta(charset := "UTF-8"),
        title("品优购 - 优质！优质！"),
        link(rel := "shortcut icon", href := "favicon.ico"),
        // link(rel := "stylesheet", href := "css/base.css"),
        style(BaseStyles.render[String]),
        style(FontsStyles.render[String]),
        style(PinyougouStyles.render[String]),
      ),
      body(
        header(
          div(cls := "top_bar")(div(cls := "w")(
            div(cls := "welcome")(
              span("品优购欢迎您！请"),
              a(cls := "login", href := "#")("登录"),
              a(cls := "register", href := "#")("免费注册")
            ),
            div(cls := "top_nav")(ul(
              li(a(href := "#")("我的订单")),
              li(cls := "top_menu")(a(href := "#")("我的品优购")),
              li(a(href := "#")("品优购会员")),
              li(a(href := "#")("企业采购")),
              li(cls := "top_menu")(a(href := "#")("关注品优购")),
              li(cls := "top_menu")(a(href := "#")("客户服务")),
              li(cls := "top_menu")(a(href := "#")("网站导航")),
            ))
          )),
          div(cls := "logo_search_cart_area")(div(cls := "w")(
            div(cls := "logo_area")(a(cls := "logo_pic", inlineTitle := "品优购", href := "index.html")),
            div(cls := "search_area")(
              div(cls := "search_bar clearfix")(
                input(`type` := "text"),
                button("搜索")
              ),
              div(cls := "search_nav")(ul(Seq(
                "品优购首发", "亿元优惠", "9.9元团购", "每满99减30", "亿元优惠", "9.9元团购", "办公用品"
              ).map(li(_))))
            ),
            div(cls := "cart_area")(button(
              span(cls := "cart_icon"),
              "我的购物车",
              div(cls := "badge")("0")
            ))
          )),
          div(cls := "head_nav_area")(div(cls := "w")(
            div(cls := "category_head")("全部商品分类"),
            div(cls := "head_nav")(ul(Seq(
              "服装城", "美妆馆", "传智超市", "全球购", "闪购", "团购", "拍卖", "有趣"
            ).map(li(_))))
          ))
        ),
        div(cls := "top_container")(div(cls := "w")(
          div(cls := "item_category")(ul(Seq(
            "图像、音像、数字商品", "家用电器", "手机、数码", "电脑、办公", "家居、家具、家装、厨具", "服饰内衣", "个护化妆", "运动健康",
            "汽车用品", "彩票、旅行", "理财、众筹", "母婴、玩具", "箱包", "运动户外", "箱包"
          ).map(s => li(a(href := "#")(s))))),
          div(cls := "banner_area white_mask")(
            a(href := "#")(img(src := "uploads/banner1.jpg")),
            a(href := "#", cls := "prev")(""),
            a(href := "#", cls := "next")(""),
            ul(cls := "banner_nav")(for _ <- 1 to 4 yield li())
          ),
          div(cls := "news_nav_banner_area")(
            div(cls := "news_area")(
              div(cls := "news_header")(
                h4("品优购快报"),
                a(href := "#")("更多")
              ),
              div(cls := "news_body")(ul(Seq(
                "【特惠】备战开学季 全民半价购数码",
                "【公告】备战开学季 全民半价购数码",
                "【特惠】备战开学季 全民半价购数码",
                "【公告】备战开学季 全民半价购数码",
                "【特惠】备战开学季 全民半价购数码"
              ).map(s => li(a(href := "#")(s)))))
            ),
            div(cls := "nav_area")(
              ul(cls := "nav_line nav_line1 clearfix")(Seq("话费", "机票", "电影票", "游戏").map(li(_))),
              ul(cls := "nav_line nav_line2 clearfix")(Seq("彩票", "加油站", "酒店", "火车票").map(li(_))),
              ul(cls := "nav_line nav_line3 clearfix")(Seq("众筹", "理财", "礼品卡", "白条").map(li(_)))
            ),
            div(cls := "little_banner_area")(img(src := "uploads/ad.jpg"))
          )
        )),
        div(cls := "middle_banner")(div(cls := "w")(dl(
          dt(
            img(src := "images/clock.png"),
            h4("今日推荐")
          ),
          for i <- 1 to 4 yield dd(cls := "white_mask")(img(src := s"uploads/today0$i.png"))
        ))),
        div(cls := "recommend_area")(div(cls := "w")(
          div(cls := "recommend_head clearfix")(
            h2("猜你喜欢"),
            button(cls := "change")("换一换")
          ),
          ul(cls := "clearfix")(
            li(cls := "recommend_item")(
              a(href := "#", cls := "item_pic")(img(src := "uploads/like_01.png")),
              h4(cls := "item-title")("时光美包新款单肩女包时尚子母包四件套女"),
              span(cls := "item_price")("￥116.00")
            ),
            li(cls := "recommend_item")(
              a(href := "#", cls := "item_pic")(img(src := "uploads/like_02.png")),
              h4(cls := "item-title")("爱仕达 30CM炒锅不粘锅电磁炉炒"),
              span(cls := "item_price")("￥116.00")
            ),
            li(cls := "recommend_item")(
              a(href := "#", cls := "item_pic")(img(src := "uploads/like_03.png")),
              h4(cls := "item-title")("捷波朗", br(), "（jabra）BOSSI劲步"),
              span(cls := "item_price")("￥236.00")
            ),
            li(cls := "recommend_item")(
              a(href := "#", cls := "item_pic")(img(src := "uploads/like_04.png")),
              h4(cls := "item-title")("阳光美包新款单肩包女包时尚子母包四件套女"),
              span(cls := "item_price")("￥116.00")
            ),
            li(cls := "recommend_item")(
              a(href := "#", cls := "item_pic")(img(src := "uploads/like_05.png")),
              h4(cls := "item-title")("捷波朗", br(), "（jabra）BOSSI劲步"),
              span(cls := "item_price")("￥236.00")
            ),
            li(cls := "recommend_item")(
              a(href := "#", cls := "item_pic")(img(src := "uploads/like_06.png")),
              h4(cls := "item-title")("三星（G5500）", br(), "移动联通双网通"),
              span(cls := "item_price")("￥566.00")
            ),
          )
        )),
        div(cls := "sidebar_area")(
          div(cls := "right_bar"),
          div(cls := "middle_area")(
            div(cls := "sidebar_icon cart_icon")(div(cls := "badge")("0")),
            div(cls := "sidebar_icon favorite_icon"),
            div(cls := "sidebar_icon history_icon")
          ),
          div(cls := "bottom_area")(
            div(cls := "sidebar_icon top_icon"),
            div(cls := "sidebar_icon comment_icon")
          )
        )
      )
    )
end PinyougouPage

object PinyougouPage:
  object Text extends PinyougouPage(scalatags.Text)
end PinyougouPage

