package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.$ as jQuery
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSGlobal}

object FullPageScrollScript:

  @js.native
  @JSGlobal
  object $ extends js.Object:
    def apply(selector: String): JQuery = js.native

  trait FullPageOptions extends js.Object:
    var sectionsColor: js.Array[String]
    var navigation: Boolean

  @js.native
  trait JQuery extends js.Object:
    def fullpage(options: FullPageOptions): js.Any = js.native

  @JSExportTopLevel("fullPageScroll")
  def fullPageScroll(): Unit = jQuery(() =>
    $("#dowebok").fullpage(new FullPageOptions {
      var sectionsColor: js.Array[String] = js.Array("pink", "#4BBFC3", "#7BAABE", "#f90")
      var navigation: Boolean = true
    })
  )
