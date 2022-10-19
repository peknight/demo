package com.peknight.demo.frontend.heima.pink.mobile.suning

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

object SuningStyles extends StyleSheet.Standalone:
  import dsl.*

  import com.peknight.demo.frontend.style.CommonMediaStyles.no

  val baseFont: Double = 75

  "body" - (
    minWidth(320.px),
    width(no.rem),
    margin(`0`, auto),
    lineHeight(1.5),
    fontFamily :=! "Arial,Helvetica",
    background := "#f2f2f2",
  )

  "a" - style(textDecoration := "none")

  ".search-content" - (
    display.flex,
    position.fixed,
    top.`0`,
    left(50.%%),
    transform := "translateX(-50%)",
    width((750 / baseFont).rem),
    height((88 / baseFont).rem),
    backgroundColor(c"#ffc001"),
    unsafeChild(".classify")(
      width((44 / baseFont).rem),
      height((70 / baseFont).rem),
      background := "url('/suning/images/classify.png') no-repeat",
      backgroundSize := s"${44 / baseFont}rem ${70 / baseFont}rem",
      margin((11 / baseFont).rem, (25 / baseFont).rem, (7 / baseFont).rem, (24 / baseFont).rem),
    ),
    unsafeChild(".search")(
      flex := "1",
      unsafeChild("input")(
        outline.none,
        width(100.%%),
        border.`0`,
        height((66 / baseFont).rem),
        borderRadius((33 / baseFont).rem),
        backgroundColor(c"#fff2cc"),
        marginTop((12 / baseFont).rem),
        fontSize((25 / baseFont).rem),
        paddingLeft((55 / baseFont).rem),
        color(c"#757575"),
      ),
    ),
    unsafeChild(".login")(
      width((75 / baseFont).rem),
      height((70 / baseFont).rem),
      lineHeight((70 / baseFont).rem),
      margin((10 / baseFont).rem),
      fontSize((25 / baseFont).rem),
      textAlign.center,
      color.white,
    )
  )

  // banner
  ".banner" - (
    width((750 / baseFont).rem),
    height((368 / baseFont).rem),
    unsafeChild("img")(
      width(100.%%),
      height(100.%%),
    ),
  )

  // ad
  ".ad" - (
    display.flex,
    unsafeChild("a")(
      flex := "1",
      unsafeChild("img")(
        width(100.%%),
      ),
    ),
  )

  // nav
  "nav" - (
    width((750 / baseFont).rem),
    unsafeChild("a")(
      float.left,
      width((150 / baseFont).rem),
      height((140 / baseFont).rem),
      textAlign.center,
      unsafeChild("img")(
        display.block,
        width((82 / baseFont).rem),
        height((82 / baseFont).rem),
        margin((10 / baseFont).rem, auto, `0`),
      ),
      unsafeChild("span")(
        fontSize((25 / baseFont).rem),
        color(c"#333"),
      )
    ),
  )
