package com.peknight.demo.frontend.vue.runtimecore

import scala.scalajs.js

@js.native
trait App[HostElement] extends js.Object:
  def mount(rootContainer: HostElement | String, isHydrate: js.UndefOr[Boolean] = ???, isSVG: js.UndefOr[Boolean] = ???)
  : ComponentPublicInstance = js.native
