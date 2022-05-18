package com.peknight.demo.js.css.quickstart.standalone

import scalacss.DevDefaults.*

object MyStyles extends StyleSheet.Standalone:

  import dsl.*

  "div.std" - (
    margin(12.px, auto),
    textAlign.left,
    cursor.pointer,

    &.hover -
      cursor.zoomIn,

    media.not.handheld.landscape.maxWidth(640.px) -
      width(400.px),

    &("span") -
      color.red
  )

  "h1".firstChild -
    fontWeight.bold

  for i <- 0 to 3 yield
    s".indent-$i" -
      paddingLeft(i * 2.ex)
