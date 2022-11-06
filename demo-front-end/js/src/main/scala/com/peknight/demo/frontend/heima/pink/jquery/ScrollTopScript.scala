package com.peknight.demo.frontend.heima.pink.jquery

import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object ScrollTopScript:

  @JSExportTopLevel("scrollTopDemo")
  def scrollTopDemo(): Unit = $(() =>
    $(dom.document).scrollTop(100)
    val boxTop = $(".container").offset().top
    $(dom.window).scroll { () =>
      dom.console.log($(dom.document).scrollTop())
      if $(dom.document).scrollTop() >= boxTop then $(".back").fadeIn()
      else $(".back").fadeOut()
    }
    $(".back").click { () =>
      $("body, html").stop().animate(js.Dictionary[js.Any]("scrollTop" -> 0), 1000)
    }
  )
