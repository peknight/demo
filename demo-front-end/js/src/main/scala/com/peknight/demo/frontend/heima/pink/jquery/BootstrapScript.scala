package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.$ as jQuery
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSGlobal}

object BootstrapScript:

  @js.native
  @JSGlobal
  object $ extends js.Object:
    def apply(selector: String): JQuery = js.native

  @js.native
  trait JQuery extends js.Object:
    def modal(option: String): js.Any = js.native

  @JSExportTopLevel("bootstrap")
  def bootstrap(): Unit = jQuery(() =>
    jQuery(".myBtn").on("click", () => $("#btn").modal("show"))
  )
