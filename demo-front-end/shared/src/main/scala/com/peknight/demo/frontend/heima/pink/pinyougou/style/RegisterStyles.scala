package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

/**
 * 注册页专有的css
 */
object RegisterStyles extends StyleSheet.Standalone:
  import dsl.*

  ".w" - (width(1200.px), margin(`0`, auto))

  "header" - (
    height(84.px),
    borderBottom(2.px, solid, c"#c81623")
  )

  ".logo" - (
    paddingTop(18.px)
  )

  ".register-area" - (
    height(522.px),
    border(1.px, solid, c"#ccc"),
    marginTop(20.px),
  )

  ".register-area h3" - (
    height(42.px),
    borderBottom(1.px, solid, c"#ccc"),
    backgroundColor(c"#ececec"),
    lineHeight(42.px),
    padding(`0`, 10.px),
    fontSize(18.px),
    fontWeight._400
  )

  ".login" - (
    float.right,
    fontSize(14.px),
  )

  ".login a" - (
    color(c"#c81623")
  )

  ".reg-form" - (
    width(600.px),
    margin(50.px, auto, `0`),
  )

  ".reg-form ul li" - (
    marginBottom(20.px),
  )

  ".reg-form ul li label" - (
    display.inlineBlock,
    width(80.px),
    textAlign.right,
  )

  ".reg-form ul li input" - (
    width(242.px),
    height(37.px),
    border(1.px, solid, c"#ccc")
  )

  ".error" - (
    color(c"#c81623")
  )
  ".error-icon, .success-icon" - (
    display.inlineBlock,
    verticalAlign.middle,
    width(20.px),
    height(20.px),
    // background := "url('images/error.png') no-repeat",
    marginTop(-6.px),
    fontFamily :=! "'icomoon'",
    color(c"#c81623"),
    fontSize(18.px),
  )

  ".success" - (
    color(green),
  )

  ".success-icon" - (
    color(green),
  )
