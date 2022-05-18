package com.peknight.demo.js.css.features.conditional

import scalacss.DevDefaults.*

object CondExample extends StyleSheet.Inline:

  import dsl.*

  val exampleStyle = style(

    // Simple pseudo selector
    &.hover(
      lineHeight(1.em)
    ),

    // Multiple pseudo selectors
    &.visited.not(_.firstChild)(
      fontWeight.bold
    ),

    // Simple media query
    media.landscape(
      margin.auto
    ),

    // Pseudo selector and media query
    (&.hover & media.landscape)(
      color.black.important
    ),

    // Multiple media queries
    (media.tv.minDeviceAspectRatio(4 :/: 3) & media.all.resolution(300.dpi))(
      margin.vertical(10.em)
    ),

    // Syntax #2: <condition> - (<styles>)
    // Scala gets confused because media.color and media.color(ColorBits).
    // Use second syntax to resolve.
    media.color - (
      padding.horizontal(500.px)
    ),

    // Everything is immutable. You can DRY repeated conditions
    modeX(
      backgroundColor(c"#700")
    ),
    modeX.hover(
      backgroundColor(c"#f00")
    )
  )

  def modeX = media.maxWidth(320.px).maxHeight(240.px) & media.handheld

