package com.peknight.demo.js.css.features.reusable

import scalacss.StyleSheet

class MyStyleModule(using r: StyleSheet.Register) extends StyleSheet.Inline()(r)

