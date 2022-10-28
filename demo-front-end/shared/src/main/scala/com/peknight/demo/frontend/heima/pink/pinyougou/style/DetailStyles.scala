package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

/**
 * 详情页专有的css
 */
object DetailStyles extends StyleSheet.Standalone:
  import dsl.*
  ".de-container" - marginTop(20.px)
  ".crumb-wrap" - height(25.px)
  ".crumb-wrap a" - marginRight(10.px)
  ".preview-wrap" - (width(400.px), height(590.px))
  ".preview-img" - (position.relative, height(398.px), border(1.px, solid, c"#ccc"))
  ".preview-img>img" - (width(100.%%), height(100.%%))
  ".mask" - (
    display.none,
    position.absolute,
    top.`0`,
    left.`0`,
    width(300.px),
    height(300.px),
    backgroundColor(c"#fede4f"),
    opacity(0.5),
    border(1.px, solid, c"#ccc"),
    cursor.move,
  )
  ".big" - (
    display.none,
    position.absolute,
    left(410.px),
    top.`0`,
    width(500.px),
    height(500.px),
    zIndex(999),
    border(1.px, solid, c"#ccc"),
    overflow.hidden,
  )
  ".big img" - (position.absolute, top.`0`, left.`0`)
  ".preview-list" - (position.relative, height(60.px), marginTop(60.px))
  ".list-item" - (width(320.px), height(60.px), margin(`0`, auto))
  ".list-item li" - (
    float.left,
    width(56.px),
    height(56.px),
    border(2.px, solid, transparent),
    margin(`0`, 2.px)
  )
  ".list-item li img" - (width(100.%%), height(100.%%))
  ".list-item li.current" - borderColor(c"#c81623")
  ".arrow-prev, .arrow-next" - (
    position.absolute,
    top(15.px),
    width(22.px),
    height(32.px),
  )
  ".arrow-prev" - (left.`0`, background := "url('/images/arrow-prev.png') no-repeat")
  ".arrow-next" - (right.`0`, background := "url('/images/arrow-next.png') no-repeat")
  ".item-info-wrap" - width(718.px)
  ".sku-name" - (height(30.px), fontSize(16.px), fontWeight._700)
  ".news" - (height(32.px), color(c"#e12228"))
  ".summary dl" - overflow.hidden
  ".summary dt, .summary dd" - float.left
  ".summary dt" - (width(60.px), paddingLeft(10.px), lineHeight(36.px))
  ".summary-price, .summary-promotion" - (position.relative, padding(10.px, `0`), backgroundColor(c"#fee9eb"))
  ".price" - (fontSize(24.px), color(c"#e12228"))
  ".summary-price a" - color(c"#c81623")
  ".remark" - (position.absolute, right(10.px), top(20.px))
  ".summary-promotion" - paddingTop(`0`)
  ".summary-promotion dd" - (width(450.px), lineHeight(36.px))
  ".summary-promotion em" - (
    display.inlineBlock,
    width(40.px),
    height(22.px),
    backgroundColor(c"#c81623"),
    textAlign.center,
    lineHeight(22.px),
    color(c"#fff")
  )
  ".summary-support dd" - lineHeight(36.px)
  ".choose-color a" - (
    display.inlineBlock,
    width(80.px),
    height(41.px),
    backgroundColor(c"#f7f7f7"),
    border(1.px, solid, c"#ededed"),
    textAlign.center,
    lineHeight(41.px)
  )
  ".summary a.current" - borderColor(c"#c81623")
  ".choose-version" - margin(10.px, `0`)
  ".choose-version a, .choose-type a" - (
    display.inlineBlock,
    height(32.px),
    padding(`0`, 12.px),
    backgroundColor(c"#f7f7f7"),
    border(1.px, solid, c"#ededed"),
    textAlign.center,
    lineHeight(32.px)
  )
  ".choose-btns" - marginTop(20.px)
  ".choose-amount" - (
    position.relative,
    float.left,
    width(50.px),
    height(46.px),
  )
  ".choose-amount input" - (
    width(33.px),
    height(44.px),
    border(1.px, solid, c"#ccc"),
    textAlign.center
  )
  ".add, .reduce" - (
    position.absolute,
    right.`0`,
    width(15.px),
    height(22.px),
    border(1.px, solid, c"#ccc"),
    backgroundColor(c"#f1f1f1"),
    textAlign.center,
    lineHeight(22.px)
  )

  ".add" - top.`0`
  ".reduce" - (bottom.`0`, cursor.notAllowed)
  ".add-car" - (
    float.left,
    width(142.px),
    height(46.px),
    backgroundColor(c"#c81623"),
    textAlign.center,
    lineHeight(46.px),
    fontSize(18.px),
    color(c"#fff"),
    marginLeft(10.px),
    fontWeight._700
  )
  ".product-detail" - marginBottom(50.px)
  ".aside" - (width(208.px), border(1.px, solid, c"#ccc"))
  ".tab-list" - (overflow.hidden, height(34.px))
  ".tab-list li" - (
    float.left,
    backgroundColor(c"#f1f1f1"),
    borderBottom(1.px, solid, c"#ccc"),
    height(33.px),
    textAlign.center,
    lineHeight(33.px)
  )
  ".tab-list .current" - (
    backgroundColor(c"#fff"),
    borderBottom.`0`,
    color.red
  )
  ".first-tab" - width(103.px)
  ".second-tab" - (width(103.px), borderLeft(1.px, solid, c"#ccc"))
  ".tab-con" - padding(`0`, 10.px)
  ".tab-con li" - borderBottom(1.px, solid, c"#ccc")
  ".tab-con li h5" - (whiteSpace.nowrap, overflow.hidden, textOverflow := "ellipsis", fontWeight._400)
  ".aside-price" - (fontWeight._700, margin(10.px, `0`))
  ".as-add-car" - (
    display.block,
    width(88.px),
    height(26.px),
    border(1.px, solid, c"#ccc"),
    backgroundColor(c"#f7f7f7"),
    margin(10.px, auto),
    textAlign.center,
    lineHeight(26.px)
  )
  ".detail" - width(978.px)
  ".detail-tab-list" - (height(39.px), border(1.px, solid, c"#ccc"), backgroundColor(c"#f1f1f1"))
  ".detail-tab-list li" - (
    float.left,
    height(39.px),
    lineHeight(39.px),
    padding(`0`, 20.px),
    textAlign.center,
    cursor.pointer
  )
  ".detail-tab-list .current" - (backgroundColor(c"#c81623"), color(c"#fff"))
  ".item-info" - padding(20.px, `0`, `0`, 20.px)
  ".item-info li" - lineHeight(22.px)
  ".more" - (float.right, fontWeight._700, fontFamily :=! "icomoon")

