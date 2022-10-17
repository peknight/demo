package com.peknight.demo.frontend.goat.login

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object LoginStyles extends StyleSheet.Standalone:
  import dsl.*

  "*" - (
    padding.`0`,
    margin.`0`,
    textDecoration := "none"
  )

  "body" - (
    display.flex,
    justifyContent.center,
    alignItems.center,
    height(100.vh),
    backgroundColor(c"#a29bfe"),
    backgroundImage := "url('/images/background.png')",
    backgroundSize := "cover"
  )

  ".login" - (
    width(550.px),
    height(400.px),
    display.flex,
    borderRadius(15.px),
    justifyContent.center,
    alignItems.center,
    background := "linear-gradient(to right bottom, rgba(255,255,255,.7), rgba(255,255,255,.5), rgba(255,255,255,.4))",
    // 使背景模糊化
    Attr.real("backdrop-filter") := "blur(10px)",
    boxShadow := "0 0 20px #a29bfe)"
  )

  ".table" - (
    font := "900 40px ''",
    textAlign.center,
    letterSpacing(5.px),
    color(c"#3d3d3d")
  )

  ".box" - overflow.hidden

  ".box input" - (
    width(100.%%),
    marginBottom(20.px),
    outline.none,
    border.`0`,
    padding(10.px),
    borderBottom(3.px, solid, rgb(150, 150, 240)),
    backgroundColor.transparent,
    font := "900 16px ''"
  )

  ".go" - (
    textAlign.center,
    display.block,
    height(24.px),
    padding(12.px),
    font := "900 20px ''",
    borderRadius(10.px),
    marginTop(20.px),
    color(c"#fff"),
    letterSpacing(3.px),
    backgroundImage := "linear-gradient(to left, #fd79a8, #a29bfe)"
  )



