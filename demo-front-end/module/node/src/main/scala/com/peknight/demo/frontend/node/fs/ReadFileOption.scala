package com.peknight.demo.frontend.node.fs

import org.scalajs.dom

import scala.scalajs.js

trait ReadFileOption extends js.Object:
  val encoding: js.UndefOr[String] = js.undefined
  val flag: js.UndefOr[String] = js.undefined
  val signal: js.UndefOr[dom.AbortSignal] = js.undefined
