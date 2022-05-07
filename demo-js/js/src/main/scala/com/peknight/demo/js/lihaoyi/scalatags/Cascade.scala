package com.peknight.demo.js.lihaoyi.scalatags

import scalatags.JsDom.all.*
import scalatags.stylesheet.CascadingStyleSheet

/**
 * 关注 Scala-CSS https://japgolly.github.io/scalacss/book/ 提供更多css相关的能力
 */
object Cascade extends CascadingStyleSheet:
  initStyleSheet()

  val y = cls()
  val x = cls(
    a(backgroundColor := "red", textDecoration.none),
    a(backgroundColor := "blue", textDecoration.underline),
    // .$pkg-Cascade-x a:hover div .$pkg-Cascade-y { ... }
    (a.hover ~ div ~ y)(opacity := 0),
    // .$pkg-Cascade-x div:hover div .$pkg-Cascade-y { ... }
    div.hover(div(y(opacity := 0)))
  )

