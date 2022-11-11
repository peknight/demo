package com.peknight.demo.frontend.apache.echarts.util

import org.scalajs.dom.HTMLElement

import scala.scalajs.js

@js.native
trait TooltipFormatterCallback[T] extends js.Object :
  def apply(params: T, asyncTicket: String): String | HTMLElement | js.Array[HTMLElement] = js.native

  def apply(params: T, asyncTicket: String,
            callback: (cbTicket: String, htmlOrDomNodes: String | HTMLElement | js.Array[HTMLElement]) => Unit)
  : String | HTMLElement | js.Array[HTMLElement] = js.native
