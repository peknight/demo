package com.peknight.demo.js.dom

import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExportTopLevel

object DomWebsocketDemo:
  @JSExportTopLevel("domWebsocketDemo")
  def domWebsocketDemo(in: html.Input, pre: html.Pre) =
    val echo = "wss://local.peknight.com:10000/"
    val socket = new dom.WebSocket(echo)
    socket.onmessage = (e: dom.MessageEvent) => pre.textContent += s"\n${e.data.toString}"
    socket.onopen = (_: dom.Event) => in.onkeyup = (_: dom.Event) => socket.send(in.value)
