package com.peknight.demo.js.lihaoyi.scalatags

import scalatags.JsDom.all.*
import scalatags.stylesheet.*

/**
 * $pkg-$class-$def
 * you can override customSheetName to replace pkg-$class part with someting else
 */
object Simple extends StyleSheet:
  initStyleSheet()

  /*
   * .$pkg-Simple-x {
   *   background-color: red;
   *   height: 125px;
   * }
   */
  val x = cls(backgroundColor := "red", height := 125)
  /*
   * .$pkg-Simple-y:hover {
   *   opacity: 0.5;
   * }
   */
  val y = cls.hover(opacity := 0.5)
  /*
   * .$pkg-Simple-z {
   *   background-color: red;
   *   height: 125px;
   *   opacity: 0.5;
   * }
   */
  val z = cls(x.splice, y.splice)

  // access via Simple.styleSheetText


