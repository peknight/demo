package com.peknight.demo.frontend.heima.pink.pinyougou.page

import com.peknight.demo.frontend.heima.pink.pinyougou.style.*
import org.http4s.Uri
import scalacss.ProdDefaults.{cssEnv, cssStringRenderer}
import scalatags.generic.Bundle
import scalatags.text.Builder
import scalacss.internal.mutable.StyleSheet

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
 * 页面底部服务模块 mod-service
 * 页面底部帮助模块 mod-help
 * 页面底部版权模块 mod-copyright
 */
class PinyougouPage[Builder, Output <: FragT, FragT](val bundle: Bundle[Builder, Output, FragT]):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def index: Frag =
    html(lang := "zh-CN")(
      headFrag("品优购商城", IndexStyles),
      body(
        shortcutFrag,
        headerFrag(""),
        navFrag(indexNavFrag),
        mainFrag,
        recommendFrag,
        likeFrag,
        floorFrag,
        brandFrag,
        sidebarFrag,
        footerFrag
      )
    )

  def list: Frag =
    html(lang := "zh-CN")(
      headFrag("列表页", ListStyles),
      body(
        shortcutFrag,
        headerFrag(secondKillFrag),
        navFrag(listNavFrag),
        footerFrag
      )
    )

  def headFrag(headTitle: String, styleSheet: StyleSheet.Base): Modifier = head(
    meta(charset := "UTF-8"),
    meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
    meta(httpEquiv := "X-UA-Compatible", content := "ie=edge"),
    // TDK三大标签SEO优化: title description keywords
    title(s"$headTitle-综合网购首选-正品低价、品质保障、配送及时、轻松购物！"),
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
    style(styleSheet.render[String])
  )

  // 快捷导航模块
  val shortcutFrag: Modifier =
    section(cls := "shortcut")(div(cls := "w")(
      div(cls := "fl")(ul(
        li("品优购欢迎您！ "),
        // 这里也用的li，而不是直接用a
        li(a(href := "#")("请登录"), " ", a(href := "#", cls := "style-red")("免费注册"))
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
  def headerFrag(afterLogo: Modifier): Modifier =
    header(cls := "header w")(
      logoFrag,
      afterLogo,
      // search搜索模块
      div(cls := "search")(
        input(`type` := "search", name := "", id := "", placeholder := "语言开发"),
        button("搜索")
      ),
      // hotwords模块制作
      div(cls := "hotwords")(
        a(href := "#", cls := "style-red")("品优购首发"),
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
  def navFrag(navModifier: Modifier): Modifier =
    nav(cls := "nav")(div(cls := "w")(navModifier))

  val indexNavFrag: Modifier =
    modifier(
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
    )


  // 底部模块的制作
  val footerFrag: Modifier =
    footer(cls := "footer")(div(cls := "w")(
      div(cls := "mod-service")(ul(
        Seq(
          ("正品保障", "正品保障，提供发票"),
          ("极速物流", "极速物流，极速送达"),
          ("无忧售后", "1天无理由退换货"),
          ("特色服务", "私人定制家电套餐"),
          ("帮助中心", "您的购物指南")
        ).zipWithIndex.map {
          case ((serviceTitle, serviceTxt), index) =>
            li(cls := s"intro${index + 1}")(h5(), div(cls := "service-txt")(h4(serviceTitle), p(serviceTxt)))
        }
      )),
      div(cls := "mod-help")(
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
        dl(dt("帮助中心"), dd(img(src := "images/wx_cz.jpg"), "品优购客户端"))
      ),
      div(cls := "mod-copyright")(
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
      div(cls := "focus white-mask")(
        ol(for _ <- 1 to 4 yield li()),
        ul(for i <- 1 to 4 yield li(img(src := s"uploads/banner$i.jpg"))),
        a(href := "#", cls := "prev")("‹"),
        a(href := "#", cls := "next")("›"),
      ),
      div(cls := "newsflash")(
        div(cls := "news")(
          div(cls := "news-hd")(
            h5("品优购快报"),
            a(href := "#", cls := "more")("更多")
          ),
          div(cls := "news-bd")(ul(Seq(
            ("特惠", "备战开学季 全民半价购数码，超长文本测试"),
            ("公告", "品优稳占家电网购六成份额"),
            ("特惠", "百元中秋全品类礼券限量领"),
            ("公告", "上品优生鲜 享阳澄湖大闸蟹"),
            ("特惠", "每日享折扣品优品质游，超长文本测试")
          ).map {
            case (l, r) => li(a(href := "#")(strong(s"[$l]"), s" $r"))
          }))
        ),
        div(cls := "lifeservice")(ul(Seq(
          "话费", "机票", "电影票", "游戏", "彩票", "加油卡", "酒店", "火车票", "众筹", "理财", "礼品卡", "白条"
        ).zipWithIndex.map {
          case (s, index) if index == 1 => li(i(), p(s), div(cls := "promotion")("减"))
          case (s, _) => li(i(), p(s))
        })),
        div(cls := "bargain")(img(src := "uploads/ad.jpg"))
      )
    ))

  // 推荐模块
  val recommendFrag: Modifier =
    div(cls := "w recom")(
      div(cls := "recom-hd")(
        img(src := "images/clock.png"),
        h4("今日推荐")
      ),
      div(cls := "recom-bd")(ul(
        for i <- 1 to 4 yield li(cls := "white-mask")(img(src := s"uploads/today0$i.png"))
      ))
    )

  // 猜你喜欢模块
  val likeFrag: Modifier =
    div(cls := "w like")(
      div(cls := "like-hd")(
        h3(cls := "fl")("猜你喜欢"),
        div(cls := "fr change-btn")(a(href := "#")("换一换"))
      ),
      div(cls := "like-bd")(ul(Seq[(Frag, Int)](
        ("时光美包新款单肩女包时尚子母包四件套女", 116),
        ("爱仕达 30CM炒锅不粘锅电磁炉炒", 116),
        (frag("捷波朗", br(), "（jabra）BOSSI劲步"), 236),
        ("阳光美包新款单肩包女包时尚子母包四件套女", 116),
        (frag("捷波朗", br(), "（jabra）BOSSI劲步"), 236),
        (frag("三星（G5500）", br(), "移动联通双网通"), 566)
      ).zipWithIndex.map {
        case ((itemTitle, price), index) =>
          li(
            a(href := "#")(img(src := s"uploads/like_0${index + 1}.png")),
            div(cls := "like-text")(
              h4(cls := "item-title")(itemTitle),
              span(cls := "item-price")(s"￥$price.00")
            )
          )
      }))
    )

  // 楼层区域制作
  def floorFrag: Modifier =
    div(cls := "floor")(
      // 1楼家用电器楼层
      floorChildFrag("appliance", "家用电器", Seq(
        "热门", "大家电", "生活电器", "厨房电器", "个护健康", "应季电器", "空气/净水", "新奇特", "高端电器"
      )),
      // 2楼手机楼层
      floorChildFrag("cellphone", "手机通讯", Seq(
        "热门", "品质优选", "新机尝鲜", "高性价比", "口碑推荐", "合约机", "手机卡", "店铺精选", "手机配件"
      )),
      floorChildFrag("computer", "电脑办公", Seq(
        "热门", "大家电", "生活电器", "厨房电器", "应季电器", "空气/净水", "高端电器"
      )),
    )

  def floorChildFrag(className: String, floorTitle: String, tabList: Seq[String]): Modifier =
    div(cls := s"w $className")(
      div(cls := "box-hd")(
        h3(floorTitle),
        div(cls := "tab-list")(ul(tabList.zipWithIndex.map {
          case (s, index) if index == 0 => li(a(href := "#", cls := "style-red")(s))
          case (s, _) => li(a(href := "#")(s))
        }))
      ),
      div(cls := "box-bd")(div(cls := "tab-content")(
        div(cls := "tab-list-item")(
          div(cls := "container col-210")(
            ul(Seq("节能补贴", "4K电视", "空气净化器", "IH电饭煲", "滚筒洗衣机", "电热水器").map(s => li(a(href := "#")(s)))),
            a(href := "#")(img(src := "uploads/floor-1-1.png"))
          ),
          div(cls := "container col-329")(
            a(href := "#")(img(src := "uploads/floor-1-b01.png"))
          ),
          div(cls := "container col-221")(
            a(href := "#", cls := "bb")(img(src := "uploads/floor-1-2.png")),
            a(href := "#")(img(src := "uploads/floor-1-3.png"))
          ),
          div(cls := "container col-221")(
            a(href := "#")(img(src := "uploads/floor-1-4.png"))
          ),
          div(cls := "container col-219")(
            a(href := "#", cls := "bb")(img(src := "uploads/floor-1-5.png")),
            a(href := "#")(img(src := "uploads/floor-1-6.png"))
          )
        )
      ))
    )

  // 商标
  val brandFrag: Modifier =
    div(cls := "w")(div(cls := "brand")(ul(
      Seq("21", "03", "05", "07", "09", "11", "13", "15", "17", "19").map(s => li(img(src := s"uploads/brand_$s.png")))
    )))

  val sidebarFrag: Modifier =
    div(cls := "sidebar")(
      div(cls := "right-bar"),
      div(cls := "middle")(
        div(cls := "sidebar-icon cart-icon")(i(cls := "badge")(8)),
        div(cls := "sidebar-icon favorite-icon"),
        div(cls := "sidebar-icon history-icon")
      ),
      div(cls := "bottom")(
        div(cls := "sidebar-icon top-icon"),
        div(cls := "sidebar-icon comment-icon")
      )
    )

  // 列表页的秒杀模块
  val secondKillFrag: Modifier =
    div(cls := "sk")(img(src := "uploads/secKill_03.png"))

  val listNavFrag: Modifier =
    modifier(
      div(cls := "sk-list")(ul(Seq("品优秒杀", "即将售罄", "超值低价").map(s => li(a(href := "#")(s))))),
      div(cls := "sk-con")(ul(Seq(
        "女装", "女鞋", "男装", "男鞋", "母婴童装", "食品", "智能数码", "运动户外", "更多分类"
      ).zipWithIndex.map {
        case (s, index) if index == 1 => li(a(href := "#", cls := "style-red")(s))
        case (s, _)  => li(a(href := "#")(s))
      }))
    )

  extension [A] (seq: Seq[A])
    def sep[B](b: B): Seq[A | B] =
      seq.foldRight(Seq.empty[A | B])((a, acc) =>
        if acc.isEmpty then Seq(a)
        else a +: b +: acc
      )
  end extension

end PinyougouPage

object PinyougouPage:
  object Text extends PinyougouPage(scalatags.Text)
end PinyougouPage

