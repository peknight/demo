package com.peknight.demo.frontend.vue.common

import com.peknight.demo.frontend.vue.reactivity.{Ref, UnwrapRef}
import com.peknight.demo.frontend.vue.runtimecore.{App, Component, Data}

import scala.scalajs.js

@js.native
trait Vue extends js.Object:
  def createApp[HostElement](rootComponent: Component, rootProps: js.UndefOr[Data | Null] = ???): App[HostElement] =
    js.native

  def ref[T](value: T): Ref[UnwrapRef[T]] = js.native

  def onMounted(func: js.Function0[Unit]): Unit = js.native
