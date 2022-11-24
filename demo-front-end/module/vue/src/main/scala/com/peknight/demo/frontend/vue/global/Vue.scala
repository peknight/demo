package com.peknight.demo.frontend.vue.global

import com.peknight.demo.frontend.vue.runtimecore.{App, Component, Data}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
object Vue extends js.Object:
  def createApp[HostElement](rootComponent: Component, rootProps: js.UndefOr[Data | Null] = ???): App[HostElement] =
    js.native
