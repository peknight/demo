package com.peknight.demo.js.css.quickstart.inline

import scalacss.DevDefaults.*

object MyStyles extends StyleSheet.Inline:

  import dsl.*

  val common = mixin(
    backgroundColor.green
  )

  val outer = style(
    common, // Applying our mixin
    margin(12.px, auto),
    textAlign.left,
    cursor.pointer,

    &.hover(
      cursor.zoomIn
    ),

    media.not.handheld.landscape.maxWidth(640.px)(
      width(400.px)
    )
  )

  /** Style requiring an Int when applied. */
  val indent =
    styleF.int(0 to 3)(i => styleS(
      paddingLeft(i * 2.ex)
    ))

  /** Style hooking into Bootstrap. */
  val button = style(
    addClassNames("btn", "btn-default")
  )

