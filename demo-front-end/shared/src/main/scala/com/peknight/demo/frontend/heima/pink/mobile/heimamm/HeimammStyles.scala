package com.peknight.demo.frontend.heima.pink.mobile.heimamm

import scalacss.ProdDefaults.*
import scalacss.internal.{Attr, Length}
import scalacss.internal.Dsl.*

object HeimammStyles extends StyleSheet.Standalone:
  import com.peknight.demo.frontend.style.CommonMediaStyles.no
  import dsl.*

  extension [A: Numeric] (a: A)
    def pxToRem: Length[Double] = (Numeric[A].toDouble(a) / 75).rem
  end extension

  "body" - (
    minWidth(320.px),
    maxWidth(750.px),
    margin(`0`, auto),
    backgroundColor(c"#f2f4f7"),
    fontFamily :=! "Arial,Helvetica",
  )

  "a" - (
    textDecoration := "none",
    color(c"#707070"),
  )

  "img" - (
    width(100.%%),
  )

  ".wrap" - (
    backgroundColor(c"#fff"),
    paddingBottom(43.pxToRem),
  )

  ".header" - (
    height(80.pxToRem),
    borderBottom(1.px, solid, c"#EAEAEA"),
    textAlign.center,
    lineHeight(80.pxToRem),
    fontSize(35.pxToRem),
    color(c"#1c1c1c"),
  )

  ".nav" - (
    display.flex,
    flexWrap.wrap,
    padding(45.pxToRem, `0`, 60.pxToRem, `0`),
    unsafeChild(".item")(
      display.flex,
      width(33.33.%%),
      flexDirection.column,
      alignItems.center,
      unsafeChild("img")(
        width(139.pxToRem),
        height(139.pxToRem),
      ),
      unsafeChild("span")(
        fontSize(25.pxToRem),
      ),
      &.nthOfType("-n+3")(
        marginBottom(62.pxToRem),
      ),
    ),
  )

  ".go" - (
    margin(`0`, 10.pxToRem, `0`, 18.pxToRem),
  )

  ".content" - (
    padding(40.pxToRem, 24.pxToRem),
    backgroundColor(c"#fff"),
    marginTop(10.pxToRem),
    unsafeChild(".con-hd")(
      display.flex,
      justifyContent.spaceBetween,
      height(38.pxToRem),
      lineHeight(38.pxToRem),
      marginBottom(34.pxToRem),
      unsafeChild("h4")(
        margin.`0`,
        fontSize(28.pxToRem),
        color(c"#333"),
        unsafeChild(".icon")(
          display.inlineBlock,
          width(38.pxToRem),
          height(38.pxToRem),
          verticalAlign.middle,
        ),
      ),
      unsafeChild(".more")(
        fontSize(22.pxToRem),
        color(c"#999"),
      )
    ),
  )

  ".get-job-focus" - (
    position.relative,
    unsafeChild(".swiper")(
      width(540.pxToRem),
      height(450.pxToRem),
    ),
    unsafeChild(".swiper-slide")(
      textAlign.center,
      fontSize(18.pxToRem),
      background := "#fff",
      display.flex,
      flexDirection.column,
      justifyContent.center,
      alignItems.center,
      transition := "300ms",
      transform := "scale(0.8)",
      opacity(0.4),
      unsafeChild("a")(
        width(338.pxToRem),
        height(376.pxToRem),
        unsafeChild("img")(
          height(100.%%),
        ),
      ),
      unsafeChild("p")(
        width(338.pxToRem),
        fontSize(25.pxToRem),
        marginTop(24.pxToRem),
        color(c"#333"),
      )
    ),
    // 当前选中的slide 中间那个
    unsafeChild(".swiper-slide-active, .swiper-slide-duplicate-active")(
      transform := "scale(1)",
      zIndex(1),
      opacity(1),
    ),
  )