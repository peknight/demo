package com.peknight.demo.frontend.heima.pink.pinyougou.page

import scalatags.generic.Bundle

class IndexPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends CommonPage(bundle):
  import bundle.all.{title as inlineTitle, style as _, *}
  import bundle.tags2.{nav, section, style, title}

  def index: Frag =
    html(lang := "zh-CN")(
      headFrag("品优购商城",
        link(rel := "stylesheet", href := "/css/index.css"),
        script(src := "/webjars/jquery/3.6.1/jquery.min.js"),
        script(src := "/main.js"),
        script("pinyougouIndex()"),
        script("pinyougouFocus()"),
        script("pinyougouFixedTool()"),
      ),
      body(
        shortcutFrag,
        headerFrag(),
        navFrag(indexNavFrag()),
        mainFrag,
        recommendFrag,
        likeFrag,
        floorFrag,
        brandFrag,
        sidebarFrag,
        fixedToolFrag,
        footerFrag
      )
    )

  // 首页专有的模块 main
  private[this] val mainFrag: Modifier =
    div(cls := "w")(div(cls := "main")(
      div(cls := "focus fl")(
        a(href := "javascript:;", cls := "arrow-l")("‹"),
        a(href := "javascript:;", cls := "arrow-r")("›"),
        ul(for i <- 1 to 4 yield li(a(href := "#")(img(src := s"/uploads/banner$i.jpg")))),
        ol(cls := "circle"),
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
        div(cls := "bargain")(img(src := "/uploads/ad.jpg"))
      )
    ))

  // 推荐模块
  private[this] val recommendFrag: Modifier =
    div(cls := "w recom")(
      div(cls := "recom-hd")(
        img(src := "/images/clock.png"),
        h4("今日推荐")
      ),
      div(cls := "recom-bd")(ul(
        for i <- 1 to 4 yield li(cls := "white-mask")(img(src := s"/uploads/today0$i.png"))
      ))
    )

  // 猜你喜欢模块
  private[this] val likeFrag: Modifier =
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
            a(href := "#")(img(src := s"/uploads/like_0${index + 1}.png")),
            div(cls := "like-text")(
              h4(cls := "item-title")(itemTitle),
              span(cls := "item-price")(s"￥$price.00")
            )
          )
      }))
    )

  // 楼层区域制作
  private[this] def floorFrag: Modifier =
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
      floorChildFrag("furniture", "精品家具", Seq(
        "热门", "大家电", "生活电器", "厨房电器", "应季电器", "空气/净水", "高端电器"
      )),
    )

  private[this] def floorChildFrag(className: String, floorTitle: String, tabList: Seq[String]): Modifier =
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
          div(cls := "col-210")(
            ul(Seq("节能补贴", "4K电视", "空气净化器", "IH电饭煲", "滚筒洗衣机", "电热水器").map(s => li(a(href := "#")(s)))),
            a(href := "#")(img(src := "/uploads/floor-1-1.png"))
          ),
          div(cls := "col-329")(
            a(href := "#")(img(src := "/uploads/floor-1-b01.png"))
          ),
          div(cls := "col-221")(
            a(href := "#", cls := "bb")(img(src := "/uploads/floor-1-2.png")),
            a(href := "#")(img(src := "/uploads/floor-1-3.png"))
          ),
          div(cls := "col-221")(
            a(href := "#")(img(src := "/uploads/floor-1-4.png"))
          ),
          div(cls := "col-219")(
            a(href := "#", cls := "bb")(img(src := "/uploads/floor-1-5.png")),
            a(href := "#")(img(src := "/uploads/floor-1-6.png"))
          )
        )
      ))
    )

  // 商标
  private[this] val brandFrag: Modifier =
    div(cls := "w")(div(cls := "brand")(ul(
      Seq("21", "03", "05", "07", "09", "11", "13", "15", "17", "19").map(s => li(img(src := s"/uploads/brand_$s.png")))
    )))

  private[this] val sidebarFrag: Modifier =
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

  private[this] val fixedToolFrag: Modifier =
    div(cls := "fixed-tool")(ul(
      li(cls := "current")("家用电器"),
      Seq("手机通讯", "电脑办公", "精品家具").map(li(_))
    ))

end IndexPage
object IndexPage:
  object Text extends IndexPage(scalatags.Text)
end IndexPage
