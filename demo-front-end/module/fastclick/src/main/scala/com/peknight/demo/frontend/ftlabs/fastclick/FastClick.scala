package com.peknight.demo.frontend.ftlabs.fastclick

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
class FastClick(layer: dom.Element, options: js.UndefOr[FastClickOptions]) extends js.Object

@js.native
@JSGlobal
object FastClick extends js.Object:
  def attach(layer: dom.Element, options: js.UndefOr[FastClickOptions] = js.undefined): FastClick = js.native

