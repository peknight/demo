package com.peknight.demo.frontend.heima.pink.webapis

import org.scalajs.dom

import scala.scalajs.js.Date
import scala.scalajs.js.annotation.JSExportTopLevel

object CountDownScript:

  @JSExportTopLevel("countDown")
  def countDownScript(): Unit =
    val hour = dom.document.querySelector(".hour").asInstanceOf[dom.HTMLElement]
    val minute = dom.document.querySelector(".minute").asInstanceOf[dom.HTMLElement]
    val second = dom.document.querySelector(".second").asInstanceOf[dom.HTMLElement]
    val inputTime = (System.currentTimeMillis() / 1000).toInt + (24 * 60 * 60)
    countDown()
    dom.window.setInterval(() => countDown(), 1000)
    def countDown(): Unit =
      val nowTime = (System.currentTimeMillis() / 1000).toInt
      val times = inputTime - nowTime
      val h = times / 60 / 60 % 24
      hour.innerHTML = format(h)
      val m = times / 60 % 60
      minute.innerHTML = format(m)
      val s = times % 60
      second.innerHTML = format(s)
    def format(i: Int): String = "%02d".format(i)




