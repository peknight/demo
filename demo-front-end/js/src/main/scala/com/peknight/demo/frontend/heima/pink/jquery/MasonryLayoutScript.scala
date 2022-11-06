package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.$ as jQuery
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSGlobal}

object MasonryLayoutScript:

  @js.native
  @JSGlobal
  object $ extends js.Object:
    def apply(selector: String): JQuery = js.native

  @js.native
  trait JQuery extends js.Object:
    def pinterest_grid(config: js.Dictionary[js.Any]): js.Any = js.native

  @JSExportTopLevel("masonryLayout")
  def masonryLayout(): Unit = jQuery(() =>
    $("#gallery-wrapper").pinterest_grid(js.Dictionary[js.Any](
      "no_columns" -> 5,
      "padding_x" -> 15,
      "padding_y" -> 10,
      "margin_bottom" -> 50,
      "single_column_breakpoint" -> 700
    ))
  )
