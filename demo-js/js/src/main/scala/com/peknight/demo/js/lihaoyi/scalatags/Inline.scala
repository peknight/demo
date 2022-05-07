package com.peknight.demo.js.lihaoyi.scalatags

import scalatags.JsDom.all.*
import scalatags.stylesheet.StyleSheet

object Inline extends StyleSheet:
  initStyleSheet()
  
  val w = cls(
    &.hover(
      backgroundColor := "red"
    ),
    &.active(
      backgroundColor := "blue"
    ),
    &.hover.active(
      backgroundColor := "yellow"
    ),
    opacity := 0.5
  )

