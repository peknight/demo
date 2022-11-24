package com.peknight.demo.frontend.demovue.quickstart

import com.peknight.demo.frontend.vue.esm.Vue
import com.peknight.demo.frontend.vue.runtimecore.Component

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object UseVueScript:
  @JSExportTopLevel(name = "useVue", moduleID = "demo-vue")
  def useVue(): Unit =
    Vue.createApp(Component(data = (_, _) => js.Dynamic.literal("message" -> "Hello Vue ECM!")))
      .mount("#app")

