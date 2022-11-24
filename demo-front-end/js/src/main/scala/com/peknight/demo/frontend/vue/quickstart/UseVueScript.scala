package com.peknight.demo.frontend.vue.quickstart

import com.peknight.demo.frontend.vue.global.Vue
import com.peknight.demo.frontend.vue.runtimecore.Component

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object UseVueScript:

  @JSExportTopLevel("useVue")
  def useVue(): Unit =
    Vue.createApp(Component(data = (_, _) => js.Dynamic.literal("message" -> "Hello Vue !"))).mount("#app")