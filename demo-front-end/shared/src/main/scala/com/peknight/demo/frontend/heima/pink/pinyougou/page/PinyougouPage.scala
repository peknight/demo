package com.peknight.demo.frontend.heima.pink.pinyougou.page

import com.peknight.demo.frontend.heima.pink.pinyougou.style.*
import org.http4s.Uri
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalatags.generic.Bundle
import scalatags.text.Builder

/**
 * 常用模块类名命名
 * 快捷导航栏 shortcut
 * 头部 header
 * 标志 logo
 * 购物车 shopcar
 * 搜索 search
 * 热点词 hotwords
 * 导航 nav
 * 导航左侧 dropdown 包含 .dd .dt
 * 导航右侧 navitems
 * 页面底部 footer
 * 页面底部服务模块 mod_service
 * 页面底部帮助模块 mod_help
 * 页面底部版权模块 mod_copyright
 */
class PinyougouPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def index: Frag =
    html(lang := "zh-CN")(
      headFrag,
      body(
        shortcutFrag,
        headerFrag,
        navFrag,
        mainFrag,
        footerFrag
      )
    )

  val headFrag: Modifier = head(
    meta(charset := "UTF-8"),
    meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
    meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
    // TDK三大标签SEO优化: title description keywords
    title("品优购商城-综合网购首选-正品低价、品质保障、配送及时、轻松购物！"),
    // 网站说明
    meta(name := "description", content := "品优购商城-专业的综合网上购物商城，销售家电、数码通讯、电脑、家居百货、服装服饰、" +
      "母婴、图书、食品等数万个品牌优质商品。便捷、诚信的服务，为您提供愉悦的网上购物体验！"),
    // 关键字
    meta(name := "keywords", content := "网上购物,网上商城,手机,笔记本,电脑,MP3,CD,VCD,DV,相机,数码,配件,手表,存储卡,品优购"),
    // 引入favicon图标
    link(rel := "shortcut icon", href := "favicon.ico"),
    // 引入我们初始化样式文件
    // link(rel := "stylesheet", href := "css/base.css"),
    style(BaseStyles.render[String]),
    // 引入我们公共的样式文件
    // link(rel := "stylesheet", href := "css/common.css"),
    style(FontsStyles.render[String]),
    style(CommonStyles.render[String]),
    style(IndexStyles.render[String])
  )

  // 快捷导航模块
  val shortcutFrag: Modifier =
    section(cls := "shortcut")(div(cls := "w")(
      div(cls := "fl")(ul(
        li("品优购欢迎您！ "),
        // 这里也用的li，而不是直接用a
        li(a(href := "#")("请登录"), " ", a(href := "#", cls := "style_red")("免费注册"))
      )),
      div(cls := "fr")(ul(
        li("我的订单"), li(),
        li(cls := "arrow-icon")("我的品优购"), li(),
        li("品优购会员"), li(),
        li("企业采购"), li(),
        li(cls := "arrow-icon")("关注品优购"), li(),
        li(cls := "arrow-icon")("客户服务"), li(),
        li(cls := "arrow-icon")("网站导航")
      ))
    ))

  // header头部模块制作
  def headerFrag: Modifier =
    header(cls := "header w")(
      logoFrag,
      // search搜索模块
      div(cls := "search")(
        input(`type` := "search", name := "", id := "", placeholder := "语言开发"),
        button("搜索")
      ),
      // hotwords模块制作
      div(cls := "hotwords")(
        a(href := "#", cls := "style_red")("品优购首发"),
        Seq("亿元优惠", "9.9元团购", "每满99减30", "办公用品", "电脑", "通信").map(a(href := "#")(_))
      ),
      // 购物车模块
      div(cls := "shopcar")(
        "我的购物车",
        i(cls := "count")(8)
      )
    )

  /**
   * Logo模块
   *
   * LOGO SEO优化
   * 1. logo里面首先放一个`h1`标签，目的是为了提权，告诉搜索引擎，这个地方很重要。
   * 2. `h1`里面再放一个链接，可以返回首页的，把logo的背景图片给链接即可。
   * 3. 为了搜索引擎收录我们，我们链接里面要放文字（网站名称），但是文字不要显示出来。
   *   * 方法1: `text-indent`移到盒子外面(`text-indent: -9999px`)，然后`overflow:hidden`，淘宝的做法。
   *   * 方法2: 直接给`font-size:0;` 就看不到文字了，京东的做法。
   * 4. 最后给链接一个`title`属性，这样鼠标放到logo上就可以看到提示文字了。
   */
  val logoFrag: Modifier =
    div(cls := "logo")(
      h1(a(href := "index.html", inlineTitle := "品优购商城")("品优购商城"))
    )

  // nav模块制作
  val navFrag: Modifier =
    nav(cls := "nav")(div(cls := "w")(
      div(cls := "dropdown")(
        div(cls := "dt")("全部商品分类"),
        div(cls := "dd")(ul(
          li(a(href := "#")("家用电器")),
          li(a(href := "#")("手机"), "、", a(href := "#")("数码"), "、", a(href := "#")("通信")),
          Seq(
            "电脑、办公", "家居、家具、家装、厨具", "男装、女装、童装、内衣", "个护化妆、清洁用品、宠物", "鞋靴、箱包、珠宝、奢侈品",
            "运动户外、钟表", "汽车、汽车用品", "母婴、玩具乐器", "食品、酒类、生鲜、特产", "医药保健", "图书、音像、电子书",
            "彩票、旅行、充值、票务", "理财、众筹、白条、保险"
          ).map(s => li(a(href := "#")(s)))
        )),
      ),
      div(cls := "navitems")(ul(
        Seq("服装城", "美妆馆", "传智超市", "全球购", "闪购", "团购", "拍卖", "有趣").map(s => li(a(href := "#")(s)))
      ))
    ))

  // 底部模块的制作
  val footerFrag: Modifier =
    footer(cls := "footer")(div(cls := "w")(
      div(cls := "mod_service")(ul(
        Seq(
          ("正品保障", "正品保障，提供发票"),
          ("极速物流", "极速物流，极速送达"),
          ("无忧售后", "1天无理由退换货"),
          ("特色服务", "私人定制家电套餐"),
          ("帮助中心", "您的购物指南")
        ).zipWithIndex.map {
          case ((serviceTitle, serviceTxt), index) =>
            li(cls := s"intro${index + 1}")(h5(), div(cls := "service_txt")(h4(serviceTitle), p(serviceTxt)))
        }
      )),
      div(cls := "mod_help")(
        Seq(
          ("服务指南", Seq("购物流程", "会员介绍", "生活旅行/团购", "常见问题", "大家电", "联系客服")),
          ("配送方式", Seq("上门自提", "211限时达", "配送服务查询", "配送费收取标准", "海外配送")),
          ("支付方式", Seq("货到付款", "在线支付", "分期付款", "邮局汇款", "公司转账")),
          ("售后服务", Seq("售后政策", "价格保护", "退款说明", "返修/退换货", "取消订单")),
          ("特色服务", Seq("夺宝岛", "DIY装机", "延保服务", "品优购E卡", "品优购通信"))
        ).map {
          case (helpTitle, helpSeq) =>
            dl(dt(helpTitle), helpSeq.map(s => dd(a(href := "#")(s))))
        },
        dl(dt("帮助中心"), dd(img(src := "images/wx_cz.jpg", alt := ""), "品优购客户端"))
      ),
      div(cls := "mod_copyright")(
        div(cls := "links")(
          Seq("关于我们", "联系我们", "联系客服", "商家入驻", "营销中心", "手机品优购", "友情链接", "销售联盟", "品优购社区",
            "品优购公益", "English Site", "Contact Us").map(s => a(href := "#")(s)).sep[Frag]("|")
        ),
        div(cls := "copyright")(
          "地址：北京市昌平区建材城西路金燕龙办公楼一层 邮编：100096 电话：400-618-4000 传真：010-82935100",
          br(),
          "京ICP备08001421号京公网安备110108007702"
        )
      )
    ))

  // 首页专有的模块 main
  val mainFrag: Modifier =
    div(cls := "w")(div(cls := "main")(
      div(cls := "focus")("焦点图"),
      div(cls := "newsflash")("快报模块")
    ))

  extension [A] (seq: Seq[A])
    def sep[B](b: B): Seq[A | B] =
      seq.foldRight(Seq.empty[A | B])((a, acc) =>
        if acc.isEmpty then Seq(a)
        else a +: b +: acc
      )
  end extension

  val history: Frag =
    frag(
      div(cls := "top_container")(div(cls := "w")(
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

end PinyougouPage

object PinyougouPage:
  object Text extends PinyougouPage(scalatags.Text)
end PinyougouPage

