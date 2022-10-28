package com.peknight.demo.frontend.heima.pink.pinyougou.page

import scalatags.generic.Bundle

class DetailPage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends CommonPage(bundle):
  import bundle.all.{title as inlineTitle, style as inlineStyle, *}
  import bundle.tags2.{nav, section, style, title}

  def detail: Frag =
    html(lang := "zh-CN")(
      headFrag("详情页",
        link(rel := "stylesheet", href := "/css/detail.css"),
        script(src := "/main.js"),
        script("pinyougouDetail()")
      ),
      body(
        shortcutFrag,
        headerFrag(""),
        navFrag(indexNavFrag(inlineStyle := "display: none;")),
        detailContainerFrag,
        footerFrag
      )
    )

  // 详情页内容部分
  private[this] def detailContainerFrag: Modifier =
    div(cls := "de-container w")(
      crumbFrag,
      productIntroFrag,
      productDetailFrag,
    )

  // 面包屑导航
  private[this] val crumbFrag: Modifier =
    div(cls := "crumb-wrap")(
      Seq("手机、数码、通讯", "手机", "Apple苹果", "iPhone 6S Plus系类").map(s => a(href := "#")(s)).sep[Frag]("〉")
    )

  // 产品介绍模块
  private[this] def productIntroFrag: Modifier =
    div(cls := "product-intro clearfix")(
      previewFrag,
      itemInfoFrag,
    )

  // 预览区域
  private[this] val previewFrag: Modifier =
    div(cls := "preview-wrap fl")(
      div(cls := "preview-img")(
        img(src := "/uploads/s3.png"),
        div(cls := "mask"),
        div(cls := "big")(img(src := "/uploads/big.jpg", cls := "big-img"))
      ),
      div(cls := "preview-list")(
        a(href := "#", cls := "arrow-prev"),
        a(href := "#", cls := "arrow-next"),
        ul(cls := "list-item")(
          li(img(src := "/uploads/pre.jpg")),
          li(cls := "current")(img(src := "/uploads/pre.jpg")),
          List.fill(3)(li(img(src := "/uploads/pre.jpg"))),
        )
      )
    )

  // 产品详细信息
  private[this] def itemInfoFrag: Modifier =
    div(cls := "item-info-wrap fr")(
      div(cls := "sku-name")("Apple iPhone 6s (A1700) 64G玫瑰金色 移动通信电信4G手机"),
      div(cls := "news")("推荐选择下方[移动优惠购]，手机套餐齐搞定，不用换号，每月还有花费返"),
      div(cls := "summary")(
        dl(cls := "summary-price")(dt("价格"), dd(
          i(cls := "price")("￥5299.00"),
          a(href := "#")("降价通知"),
          div(cls := "remark")("累计评价612188")
        )),
        dl(cls := "summary-promotion")(dt("促销"), dd(
          em("加购价"), "满999.00另加20.00元，或满1999.00另加30.00元，或满2999.00另加40.00元，即可在购物车换购热销商品 详情 >>"
        )),
        dl(cls := "summary-support")(dt("支持"), dd("以旧换新，闲置手机回收 4G套餐超值抢 礼品购")),
        dl(cls := "choose-color")(dt("选择颜色"), dd(toAnchors(List("玫瑰金", "金色", "白色", "土豪色")))),
        dl(cls := "choose-version")(dt("选择版本"), dd(toAnchors(List("公开版", "移动4G")))),
        dl(cls := "choose-type")(dt("购买方式"), dd(toAnchors(List("官方标配", "移动优惠购", "电信优惠购")))),
        dl(cls := "choose-btns")(
          div(cls := "choose-amount")(
            input(`type` := "text", value := "1"),
            a(href := "javascript:;", cls := "add")("+"),
            a(href := "javascript:;", cls := "reduce")("-")
          ),
          a(href := "#", cls := "add-car")("加入购物车")
        )
      )
    )

  private[this] def toAnchors(texts: List[String]): Modifier =
    texts match
      case head :: tail => modifier(
        a(href := "javascript:;", cls := "current")(head),
        tail.map(s => a(href := "javascript:;")(s))
      )
      case _ => modifier()

  // 产品细节模块
  private[this] def productDetailFrag: Modifier =
    div(cls := "product-detail clearfix")(
      div(cls := "aside fl")(
        div(cls := "tab-list")(ul(li(cls := "first-tab")("相关分类"), li(cls := "second-tab current")("推荐品牌"))),
        div(cls := "tab-con")(ul(List.fill(6)(li(
          img(src := "/uploads/aside_img.jpg"),
          h5("华为 HUAWEI P20 Pro 全面屏徕卡"),
          div(cls := "aside-price")("￥19"),
          a(href := "#", cls := "as-add-car")("加入购物车"),
        ))))
      ),
      div(cls := "detail fr")(
        div(cls := "detail-tab-list")(ul(
          li(cls := "current")("商品介绍"), Seq("规格与包装", "售后保障", "商品评价（50000）", "手机社区").map(li(_))
        )),
        div(cls := "detail-tab-con")(
          div(cls := "item")(
            ul(cls := "item-info")(Seq(
              "分辨率：1920*1080(FHD)", "后置摄像头：1200万像素", "前置摄像头：500万像素", "核 数：其他", "频 率：以官网信息为准",
              "品牌：Apple ♥关注", "商品名称：Apple iPhone 6s Plus", "商品编号：1861098", "商品毛重：0.51kg", "商品产地：中国大陆",
              "热点：指纹识别，Apple Pay，金属机身，拍照神器", "系统：苹果（IOS）", "像素：1000-1600万", "机身内存：64GB"
            ).map(li(_))),
            p(a(href := "#", cls := "more")("查看更多参数\ue900")),
            for i <- 1 to 3 yield img(src := s"/uploads/detail_img$i.jpg")
          ),
          // div(cls := "item")("规格与包装"),
          // div(cls := "item")("售后保障"),
        )
      )
    )

end DetailPage
object DetailPage:
  object Text extends DetailPage(scalatags.Text)
end DetailPage
