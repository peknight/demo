package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

/**
 * 购物车页专有的css
 */
object ShoppingCartStyles extends StyleSheet.Standalone:
  import dsl.*

  ".cart-header" - padding(20.px, `0`)
  ".cart-logo img" - verticalAlign.middle
  ".cart-logo b" - (fontSize(20.px), marginTop(20.px), marginLeft(10.px))
  ".cart-filter-bar" - (fontSize(16.px), color(c"#e2231a"), fontWeight._700)
  ".cart-filter-bar em" - (padding(5.px), borderBottom(1.px, solid, c"#e2231a"))
  ".cart-thead" - (
    height(44.px),
    lineHeight(32.px),
    margin(5.px, `0`, 10.px),
    padding(5.px, `0`),
    background := "#f3f3f3",
    border(1.px, solid, c"#e9e9e9"),
    borderTop.`0`,
    position.relative,
  )
  ".cart-thead>div, .cart-item>div" - float.left
  ".t-checkbox, .p-checkbox" - (
    height(25.px),
    lineHeight(18.px),
    paddingTop(7.px),
    width(122.px),
    paddingLeft(11.px),
  )
  ".t-goods" - width(400.px)
  ".t-price" - (width(160.px), paddingRight(40.px), textAlign.right)
  ".t-num" - (width(150.px), textAlign.center)
  ".t-sum" - (width(100.px), textAlign.right)
  ".t-action" - (width(130.px), textAlign.right)
  ".cart-item" - (
    height(177.px),
    borderStyle(solid),
    borderWidth(2.px, 1.px, 1.px),
    borderColor(c"#aaa", c"#f1f1f1", c"#f1f1f1"),
    background := "#fff",
    paddingTop(14.px),
    margin(15.px, `0`)
  )
  ".check-cart-item" - style(background := "#fff4e8")
  ".p-checkbox" - width(50.px)
  ".p-goods" - (marginTop(8.px), width(565.px))
  ".p-img" - (float.left, border(1.px, solid, c"#ccc"), padding(5.px))
  ".p-msg" - (float.left, width(210.px), margin(`0`, 10.px))
  ".p-price" - width(110.px)
  ".quantity-form" - (width(80.px), height(22.px))
  ".p-num" - width(170.px)
  ".decrement, .increment" - (
    float.left,
    border(1.px, solid, c"#cacbcb"),
    height(22.px),
    lineHeight(18.px),
    padding(1.px, `0`),
    width(18.px),
    textAlign.center,
    color(c"#666"),
    margin.`0`,
    background := "#fff",
    marginLeft(-1.px)
  )
  ".i-txt" - (
    float.left,
    border(1.px, solid, c"#cacbcb"),
    width(46.px),
    height(22.px),
    lineHeight(18.px),
    textAlign.center,
    padding(1.px),
    margin.`0`,
    marginLeft(-1.px),
    fontSize(12.px),
    fontFamily :=! "verdana",
    color(c"#333"),
    Attr.real("-webkit-appearance") := "none"
  )
  ".p-sum" - (fontWeight._700, width(145.px))
  ".cart-float-bar" - (
    height(52.px),
    border(1.px, solid, c"#f0f0f0"),
    background := "#fff",
    position.relative,
    marginBottom(50.px),
    lineHeight(50.px)
  )
  ".select-all" - (
    float.left,
    height(40.px),
    lineHeight(18.px),
    padding(16.px, `0`, 16.px, 9.px),
    whiteSpace.nowrap
  )
  ".select-all input" - (verticalAlign.middle, display.inlineBlock, marginRight(5.px))
  ".operation" - (float.left, width(200.px), marginLeft(40.px))
  ".clear-all" - (fontWeight._700, margin(`0`, 20.px))
  ".toolbar-right" - float.right
  ".amount-sum" - float.left
  ".amount-sum em" - (fontWeight._700, color(c"#e2231a"), padding(`0`, 3.px))
  ".price-sum" - (float.left, margin(`0`, 15.px))
  ".price-sum em" - (fontSize(16.px), color(c"#e2231a"), fontWeight._700)
  ".btn-area" - (
    fontWeight._700,
    width(74.px),
    height(52.px),
    lineHeight(52.px),
    color(c"#fff"),
    textAlign.center,
    fontSize(18.px),
    fontFamily :=! "Microsoft YaHei",
    background := "#e54346",
    overflow.hidden
  )