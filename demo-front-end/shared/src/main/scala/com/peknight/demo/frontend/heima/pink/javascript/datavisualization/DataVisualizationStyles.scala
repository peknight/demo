package com.peknight.demo.frontend.heima.pink.javascript.datavisualization

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*
import scalacss.internal.{Attr, Length}

object DataVisualizationStyles extends StyleSheet.Standalone:
  import com.peknight.demo.frontend.style.CommonMediaStyles.no
  import dsl.*

  extension [A: Numeric] (a: A)
    def pxToRem: Length[Double] = (Numeric[A].toDouble(a) / 192).rem
  end extension

  // 清除元素默认的内外边距
  "*" - (margin.`0`, padding.`0`, boxSizing.borderBox)
  // 让所有斜体 不倾斜
  "em, i" - fontStyle.normal
  // 去掉列表前面的小点
  "li" - style(listStyle := "none")
  // 图片没有边框 去掉图片底侧的空白缝隙
  "img" - (border.`0`, verticalAlign.middle)
  // 让button按钮 变成小手
  "button" - cursor.pointer
  // 取消链接的下划线
  "a" - (color(c"#666"), textDecoration := "none", &.hover - color(c"#e33333"))
  "h4" - fontWeight._400
  "body" - style(background := "url('/echarts/images/bg.jpg') no-repeat 0 0 / cover")
  ".viewport" - (
    display.flex,
    minWidth(1024.px),
    maxWidth(1920.px),
    margin(`0`, auto),
    padding(88.pxToRem, 20.pxToRem, `0`),
    background := "url('/echarts/images/logo.png') no-repeat 0 0 / contain",
    minHeight(780.px),
  )
  ".viewport .column" - (
    flex := "3",
    &.nthOfType(2) - (
      flex := "4",
      margin(32.pxToRem, 20.pxToRem, `0`),
    )
  )
  // 公共面板样式
  ".panel" - (
    position.relative,
    border(15.px, solid, transparent),
    borderWidth(51.pxToRem, 38.pxToRem, 20.pxToRem, 132.pxToRem),
    borderImageSource := "url('/echarts/images/border.png')",
    borderImageSlice := "51 38 20 132",
    marginBottom(20.pxToRem),
  )
  ".inner" - (
    position.absolute,
    top(-51.pxToRem),
    left(-132.pxToRem),
    right(-38.pxToRem),
    bottom(-20.pxToRem),
    padding(24.pxToRem, 36.pxToRem),
  )
  ".panel h3" - (fontSize(20.pxToRem), color(c"#fff"), fontWeight._400)
  ".overview" - height(110.pxToRem)
  ".overview ul" - (
    display.flex,
    justifyContent.spaceBetween
  )
  ".overview ul li h4" - (
    fontSize(28.pxToRem),
    color(c"#fff"),
    margin(`0`, `0`, 8.pxToRem, 4.8.pxToRem)
  )

  ".overview ul li span" - (
    fontSize(16.pxToRem),
    color(c"#4c9bfd"),
  )

  ".monitor" - height(480.pxToRem)
  ".monitor .inner" - (padding(24.pxToRem, `0`), display.flex, flexDirection.column)
  ".monitor .tabs" - (padding(`0`, 36.pxToRem), marginBottom(18.pxToRem), display.flex)
  ".monitor .tabs a" - (
    color(c"#1950c4"),
    fontSize(18.pxToRem),
    padding(`0`, 27.pxToRem),
    &.firstOfType - (
      paddingLeft.`0`,
      borderRight(2.pxToRem, solid, c"#00f2f1")
    ),
  )
  ".monitor .tabs a.active" - color(c"#fff")
  ".monitor .content" - (flex := "1", position.relative, display.none)
  ".monitor .head" - (
    display.flex,
    justifyContent.spaceBetween,
    lineHeight(1.05),
    backgroundColor(rgba(255, 255, 255, 0.1)),
    padding(12.pxToRem, 36.pxToRem),
    color(c"#68d8fe"),
    fontSize(14.pxToRem)
  )
  ".monitor .marquee-view" - (
    position.absolute,
    top(40.pxToRem),
    bottom.`0`,
    width(100.%%),
    overflow.hidden
  )
  ".monitor .row" - (
    display.flex,
    justifyContent.spaceBetween,
    lineHeight(1.05),
    fontSize(12.pxToRem),
    color(c"#61a8ff"),
    padding(12.pxToRem, 36.pxToRem)
  )
  ".monitor .row .icon-dot" - (position.absolute, left(16.pxToRem), opacity(0))
  ".monitor .row".hover - (backgroundColor(rgba(255, 255, 255, 0.1)), color(c"#68d8fe"))
  ".monitor .row:hover .icon-dot" - opacity(1)
  ".monitor .col".firstOfType - width(80.pxToRem)
  ".monitor .col".nthOfType(2) - (width(200.pxToRem), whiteSpace.nowrap, textOverflow := "ellipsis", overflow.hidden)
  ".monitor .col".nthOfType(3) - width(80.pxToRem)
  // 通过CSS3动画滚动marquee
  ".marquee-view .marquee" - (
    animation := s"${DataVisualizationKeyFrameStyles.move.name.value} 15s linear infinite",
    &.hover - animationPlayState.paused
  )
  // 点位分布统计模块制作
  ".point" - height(340.pxToRem)
  ".point .chart" - (display.flex, marginTop(24.pxToRem), justifyContent.spaceBetween)
  ".point .pie" - (width(312.pxToRem), height(240.pxToRem), marginLeft(-10.pxToRem))
  ".point .data" - (
    display.flex,
    flexDirection.column,
    justifyContent.spaceBetween,
    width(168.pxToRem),
    padding(36.pxToRem, 30.pxToRem),
    boxSizing.borderBox,
    backgroundImage := "url('/echarts/images/rect.png')",
    backgroundSize := "cover"
  )
  ".point h4" - (marginBottom(12.pxToRem), fontSize(28.pxToRem), color(c"#fff"))
  ".point span" - (display.block, color(c"#4c9bfd"), fontSize(16.pxToRem))
  // 地图模块制作
  ".map" - (height(578.pxToRem), marginBottom(20.pxToRem), display.flex, flexDirection.column)
  ".map h3" - (
    lineHeight(1),
    padding(16.pxToRem, `0`),
    margin.`0`,
    fontSize(20.pxToRem),
    color(c"#fff"),
    fontWeight._400
  )
  ".map .icon-cube" - color(c"#68d8fe")
  ".map .chart" - (flex := "1", backgroundColor(rgba(255, 255, 255, 0.05)))
  ".map .geo" - (width(100.%%), height(100.%%))
  // 用户统计模块
  ".users" - (height(340.pxToRem), display.flex)
  ".users .chart" - (display.flex, marginTop(24.pxToRem))
  ".users .bar" - (width(588.pxToRem), height(240.pxToRem))
  ".users .data" - (
    display.flex,
    flexDirection.column,
    justifyContent.spaceBetween,
    width(168.pxToRem),
    padding(36.pxToRem, 30.pxToRem),
    boxSizing.borderBox,
    backgroundImage := "url('/echarts/images/rect.png')",
    backgroundSize := "cover"
  )
  ".users h4" - (marginBottom(12.pxToRem), fontSize(28.pxToRem), color(c"#fff"))
  ".users span" - (display.block, color(c"#4c9bfd"), fontSize(16.pxToRem))
  // 订单模块制作
  ".order" - height(150.pxToRem)
  ".order .filter" - display.flex
  ".order .filter a" - (
    display.block,
    height(18.pxToRem),
    lineHeight(1),
    padding(`0`, 18.pxToRem),
    color(c"#1950c4"),
    fontSize(18.pxToRem),
    borderRight(2.pxToRem, solid, c"#00f2f1"),
    &.firstOfType - paddingLeft.`0`,
    &.lastOfType - borderRight.`0`,
  )
  ".order .filter a.active" - (color(c"#fff"), fontSize(20.pxToRem))
  ".order .data" - (display.flex, marginTop(20.pxToRem))
  ".order .item" - width(50.%%)
  ".order h4" - (fontSize(28.pxToRem), color(c"#fff"), marginBottom(10.pxToRem))
  ".order span" - (display.block, color(c"#4c9bfd"), fontSize(16.pxToRem))
  // 销售区域
  ".sales" - height(248.pxToRem)
  ".sales .caption" - (display.flex, lineHeight(1))
  ".sales h3" - (height(18.pxToRem), paddingRight(18.pxToRem), borderRight(2.pxToRem, solid, c"#00f2f1"))
  ".sales a" - (
    padding(4.pxToRem),
    fontSize(16.pxToRem),
    margin(-3.pxToRem, `0`, `0`, 21.pxToRem),
    borderRadius(3.pxToRem),
    color(c"#0bace6"),
  )
  ".sales a.active" - (backgroundColor(c"#4c9bfd"), color(c"#fff"))
  ".sales .inner" - (display.flex, flexDirection.column)
  ".sales .chart" - (flex := "1", paddingTop(15.pxToRem), position.relative)
  ".sales .label" - (
    position.absolute,
    left(42.pxToRem),
    top(18.pxToRem),
    color(c"#4996f5"),
    fontSize(14.pxToRem)
  )
  ".sales .line" - (width(100.%%), height(100.%%))
  // 渠道分布以及季度销售模块
  ".wrap" - display.flex
  ".channel, .quarter" - (flex := "1", height(232.pxToRem))
  ".channel" - marginRight(20.pxToRem)
  ".channel .data" - overflow.hidden
  ".channel .data .radar" - (height(168.pxToRem), width(100.%%))
  ".channel h4" - (color(c"#fff"), fontSize(32.pxToRem), marginBottom(5.pxToRem))
  ".channel small" - fontSize(50.%%)
  ".channel span" - (display.block, color(c"#4c9bfd"), fontSize(14.pxToRem))
  // 季度区块
  ".quarter .inner" - (display.flex, flexDirection.column, margin(`0`, -6.pxToRem))
  ".quarter .chart" - (flex := "1", paddingTop(18.pxToRem))
  ".quarter .box" - position.relative
  ".quarter .label" - (
    transform := "translate(-50%,-30%)",
    color(c"#fff"),
    fontSize(30.pxToRem),
    position.absolute,
    left(50.%%),
    top(50.%%)
  )
  ".quarter .label small" - fontSize(50.%%)
  ".quarter .gauge" - height(84.pxToRem)
  ".quarter .data" - (display.flex, justifyContent.spaceBetween)
  ".quarter .item" - width(50.%%)
  ".quarter h4" - (color(c"#fff"), fontSize(24.pxToRem), marginBottom(10.pxToRem))
  ".quarter span" - (
    display.block,
    width(100.%%),
    whiteSpace.nowrap,
    textOverflow := "ellipsis",
    overflow.hidden,
    color(c"#4c9bfd"),
    fontSize(14.pxToRem)
  )
  // 全国热榜模块制作
  // 排行榜
  ".top" - height(280.pxToRem)
  ".top .inner" - display.flex
  ".top .all" - (
    display.flex,
    flexDirection.column,
    width(168.pxToRem),
    color(c"#4c9bfd"),
    fontSize(14.pxToRem),
    verticalAlign.middle
  )
  ".top .all ul" - (
    paddingLeft(12.pxToRem),
    marginTop(12.pxToRem),
    flex := "1",
    display.flex,
    flexDirection.column,
    justifyContent.spaceAround
  )
  ".top .all li" - overflow.hidden
  ".top .all ".attrStartsWith("class", "icon") - (fontSize(36.pxToRem), verticalAlign.middle, marginRight(12.pxToRem))
  ".top .province" - (flex := "1", display.flex, flexDirection.column, color(c"#fff"))
  ".top .province i" - (
    padding(`0`, 12.pxToRem),
    marginTop(5.pxToRem),
    float.right,
    fontStyle.normal,
    fontSize(14.pxToRem),
    color(c"#0bace6")
  )
  ".top .province s" - (
    display.inlineBlock,
    transform := "scale(0.8)",
    textDecoration := "none"
  )
  ".top .province .icon-up" - color(c"#dc3c33")
  ".top .province .icon-down" - color(c"#36be90")
  ".top .province .data" - (flex := "1", display.flex, marginTop(14.pxToRem))
  ".top .province ul" - (flex := "1", lineHeight(1), marginBottom(14.pxToRem))
  ".top .province ul li" - (display.flex, justifyContent.spaceBetween)
  ".top .province ul span" - (display.block, overflow.hidden, whiteSpace.nowrap, textOverflow := "ellipsis")
  ".top .province ul.sup" - fontSize(14.pxToRem)
  ".top .province ul.sup li" - (
    color(c"#4995f4"),
    padding(12.pxToRem),
  )
  ".top .province ul.sup li.active" - (color(c"#a3c6f2"), backgroundColor(rgba(10, 67, 188, 0.2)))
  ".top .province ul.sub" - (
    display.flex,
    flexDirection.column,
    justifyContent.spaceAround,
    fontSize(12.pxToRem),
    backgroundColor(rgba(10, 67, 188, 0.2))
  )
  ".top .province ul.sub li" - (color(c"#52ffff"), padding(10.pxToRem, 14.pxToRem))
  ".clock" - (position.absolute, top(-36.pxToRem), right(40.pxToRem), fontSize(20.pxToRem), color(c"#0bace6"))
  ".clock i" - (marginRight(5.pxToRem), fontSize(20.pxToRem))




