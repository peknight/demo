package com.peknight.demo.frontend.bootstrap

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal("bootstrap.Modal")
class Modal(element: CSSSelector | dom.HTMLElement, config: ModalOptions = new ModalOptions{}) extends js.Object:
  def show(): Unit = js.native

