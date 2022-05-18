package com.peknight.demo.js.css.features.typedcss

import scalacss.DevDefaults.*

object TypedDemo extends StyleSheet.Inline:

  import dsl.*

  val marginAuto = style(margin.auto)
  val marginInherit = style(margin.inherit)
  val margin0 = style(margin.`0`)
  val margin12 = style(margin(12.px))
  val margin12Auto = style(margin(12.px, auto))
  val margin12Auto164 = style(margin(12.px, auto, 16.px, 4.ex))
  val tdlOverline = style(textDecorationLine.overline)
  val tdlUnderline = style(textDecorationLine.underline)
  val tdlUnderlineOverline = style(textDecorationLine.underline.overline)
  val colorLit1 = style(color(c"#37b"))
  val colorLit2 = style(color(c"#ffcc99"))
  val colorName = style(color(red))
  val colorRgb = style(color(rgb(0, 128, 0)))
  val colorPercent = style(color(rgb(0.%%, 50.%%, 0.%%)))
  val colorRgba = style(color(rgba(0, 128, 0, 0.5)))
  val colorHsl = style(color(hsl(300, 50.%%, 10.%%)))
  val colorHsla = style(color(hsla(300, 50.%%, 10.%%, 0.5)))
  val colorGrey = style(color(grey(224)))
  val colorKeywordTransparent = style(color(transparent))
  val colorKeywordCurrentColor = style(color(currentColor))
  val colorKeywordInherit = style(color(inherit))
  // val colorLitIllegal = style(color("i break your css!"))

  backgroundColor(red) // ok

  backgroundColor(c"#ffcc99") // ok
  // backgroundColor(c"#ffcc9o") // compiler error: o is not hex
  // backgroundColor(c"#ffcc9")  // compiler error: 5 digits

  backgroundColor(c"hsl(200, 20%, 10%)") // ok
  backgroundColor(c"rgb(0%, 20%, 10%)")  // ok
  backgroundColor(c"rgba(0, 0, 0, 0.5)") // ok
  // backgroundColor(c"rgb(30, 30, 300)")   // compiler error: 300 is too large
  // backgroundColor(c"rbg(32, 128, 32)")   // compiler error: typo in rbg

  // val marginLitBypassing = style(margin := "12px auto")  // Compile warns there's a type-safe alternative
  // val marginAutoBypassing = style(margin :=! "auto\\9")    // The ! means you know what you're doing


