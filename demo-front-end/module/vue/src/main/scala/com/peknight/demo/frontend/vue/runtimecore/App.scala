package com.peknight.demo.frontend.vue.runtimecore

import scala.scalajs.js

@js.native
trait App[HostElement] extends js.Object:
  val version: String = js.native
  val config: AppConfig = js.native

  def component(name: String): js.UndefOr[Component] = js.native
  def component(name: String, component: Component): App[HostElement] = js.native

  def mount(rootContainer: HostElement | String, isHydrate: js.UndefOr[Boolean] = ???, isSVG: js.UndefOr[Boolean] = ???)
  : ComponentPublicInstance = js.native
