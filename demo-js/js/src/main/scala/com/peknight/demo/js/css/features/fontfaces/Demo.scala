package com.peknight.demo.js.css.features.fontfaces

import scalacss.DevDefaults.*

object Demo extends StyleSheet.Inline:

  import dsl.*

  val ff = fontFace("myFont")(
    _.src("url(font.woff)")
      .fontStretch.expanded
      .fontStyle.italic
      .unicodeRange(0, 5)
  )

  val ff2 = fontFace("myFont2")(
    _.src("url(font2.woff)")
      .fontStyle.oblique
      .fontWeight._200
  )

  val ff3 = fontFace("myFont3")(
    _.src("local(HelveticaNeue)", "url(font2.woff)")
      .fontStretch.ultraCondensed
      .fontWeight._200
  )

  val myFontText = style(fontFamily(ff))

