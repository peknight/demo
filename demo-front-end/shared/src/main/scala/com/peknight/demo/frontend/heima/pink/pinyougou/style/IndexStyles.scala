package com.peknight.demo.frontend.heima.pink.pinyougou.style

import scalacss.ProdDefaults.*
import scalacss.internal.Dsl.*

object IndexStyles extends StyleSheet.Standalone:
  import dsl.*
  ".main" - (
    width(980.px),
    height(455.px),
    marginLeft(220.px),
    marginTop(10.px)
  )

  ".focus" - (
    position.relative,
    float.left,
    width(721.px),
    height(455.px),
    overflow.hidden
  )

  ".focus ol" - (
    position.absolute,
    left(50.%%),
    bottom(12.px),
    marginLeft(-28.px)
  )

  ".focus ol li" - (
    float.left,
    width(10.px),
    height(10.px),
    margin(0.px, 2.px),
    borderRadius(5.px),
    background := rgba(255, 255, 255, .5),
    &.firstOfType - style(background := rgba(255, 255, 255, 1))
  )

  ".focus ul li img" - (
    width(100.%%),
    height(100.%%)
  )

  ".focus a" - (
    position.absolute,
    top(50.%%),
    height(50.px),
    marginTop(-25.px),
    background := rgba(0, 0, 0, .3),
    color(white),
    fontSize(30.px),
    lineHeight(50.px)
  )

  ".focus .prev" - left(0.px)
  ".focus .next" - right(0.px)

  ".newsflash" - (
    float.right,
    width(250.px),
    height(455.px),
  )

  ".news" - (
    height(165.px),
    border(1.px, solid, c"#e4e4e4")
  )

  ".news-hd" - (
    height(33.px),
    lineHeight(33.px),
    borderBottom(1.px, dotted, c"#e4e4d4"),
    padding(`0`, 15.px)
  )

  ".news-hd h5" - (
    float.left,
    fontSize(14.px)
  )
  ".news-hd .more" - (
    float.right,
    &.after - (
      content :=! "'\\e902'",
      fontFamily :=! "'icomoon'"
    )
  )

  ".news-bd" - padding(5.px, 15.px, `0`)

  ".news-bd ul li" - (
    height(24.px),
    lineHeight(24.px)
  )

  ".lifeservice" - (
    overflow.hidden,
    height(209.px),
    border(1.px, solid, c"#e4e4e4"),
    borderTop.`0`
  )

  ".lifeservice ul" - width(252.px)

  ".lifeservice ul li" - (
    float.left,
    width(63.px),
    height(71.px),
    borderRight(1.px, solid, c"#e4e4e4"),
    borderBottom(1.px, solid, c"#e4e4e4"),
    textAlign.center
  )

  ".lifeservice ul li i" - (
    display.inlineBlock,
    width(24.px),
    height(28.px),
    marginTop(12.px),
    background := "url('images/icons.png') no-repeat"
  )

  ".lifeservice ul li".nthOfType(2) - (
    position.relative,
    &.after - (
      position.absolute,
      top.`0`,
      right.`0`,
      content :=! "'减'",
      fontSize(12.px),
      color(white),
      backgroundColor(green),
    )
  )

  ".lifeservice ul li:nth-of-type(1) i" - style(backgroundPosition := "-19px -15px")
  ".lifeservice ul li:nth-of-type(2) i" - style(backgroundPosition := "-79px -17px")
  ".lifeservice ul li:nth-of-type(3) i" - style(backgroundPosition := "-142px -17px")
  ".lifeservice ul li:nth-of-type(4) i" - style(backgroundPosition := "-205px -16px")
  ".lifeservice ul li:nth-of-type(5) i" - style(backgroundPosition := "-17px -85px")
  ".lifeservice ul li:nth-of-type(6) i" - style(backgroundPosition := "-79px -87px")
  ".lifeservice ul li:nth-of-type(7) i" - style(backgroundPosition := "-142px -87px")
  ".lifeservice ul li:nth-of-type(8) i" - style(backgroundPosition := "-206px -86px")
  ".lifeservice ul li:nth-of-type(9) i" - style(backgroundPosition := "-17px -155px")
  ".lifeservice ul li:nth-of-type(10) i" - style(backgroundPosition := "-79px -157px")
  ".lifeservice ul li:nth-of-type(11) i" - style(backgroundPosition := "-142px -152px")
  ".lifeservice ul li:nth-of-type(12) i" - style(backgroundPosition := "-205px -156px")

  ".bargain" - marginTop(6.px)
