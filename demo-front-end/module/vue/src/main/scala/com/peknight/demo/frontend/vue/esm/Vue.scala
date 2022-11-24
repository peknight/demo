package com.peknight.demo.frontend.vue.esm

import com.peknight.demo.frontend.vue.runtimecore.{App, Component, Data}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("/webjars/vue/3.2.45/dist/vue.esm-browser.prod.js", JSImport.Namespace)
object Vue extends js.Object:
  def createApp[HostElement](rootComponent: Component, rootProps: js.UndefOr[Data | Null] = ???): App[HostElement] =
    js.native
