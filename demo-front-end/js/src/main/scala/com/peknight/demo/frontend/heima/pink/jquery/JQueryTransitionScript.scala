package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object JQueryTransitionScript:

  @JSExportTopLevel("jQueryTransition")
  def jQueryTransition(): Unit = $(() => {
    $(".demo-show-hide button").eq(0).click((_: JQueryEventObject) =>
      $(".demo-show-hide div").show(1000, () => dom.window.alert("1"))
    )
    $(".demo-show-hide button").eq(1).click((_: JQueryEventObject) =>
      $(".demo-show-hide div").hide(1000, () => dom.window.alert("2"))
    )
    $(".demo-show-hide button").eq(2).click((_: JQueryEventObject) =>
      // equerki的jquery-facade没支持toggle，这玩意估计还是得自己写
      // $(".demo-show-hide div").toggle(1000)
      ()
    )
    $(".demo-slide button").eq(0).click((_: JQueryEventObject) =>
      $(".demo-slide div").slideDown()
    )
    $(".demo-slide button").eq(1).click((_: JQueryEventObject) =>
      $(".demo-slide div").slideUp(500)
    )
    $(".demo-slide button").eq(2).click((_: JQueryEventObject) =>
      $(".demo-slide div").slideToggle(500)
    )
    $(".demo-fade button").eq(0).click((_: JQueryEventObject) =>
      $(".demo-fade div").fadeIn(1000)
    )
    $(".demo-fade button").eq(1).click((_: JQueryEventObject) =>
      $(".demo-fade div").fadeOut(1000)
    )
    $(".demo-fade button").eq(2).click((_: JQueryEventObject) =>
      $(".demo-fade div").fadeToggle(1000)
    )
    $(".demo-fade button").eq(3).click((_: JQueryEventObject) =>
      $(".demo-fade div").fadeTo(1000, 0.5)
    )
    $(".demo-animate button").click(() =>
      $(".demo-animate div").animate(js.Dictionary[js.Any](
        "left" -> 500,
        "top" -> 300,
        "opacity" -> 0.4,
        "width" -> 500
      ), 500)
    )
  })
