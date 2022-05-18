package com.peknight.demo.js.css.features.reusable

import scalacss.DevDefaults.*

object MyStyles extends StyleSheet.Inline:
  val shared = new MyStyleModule()

