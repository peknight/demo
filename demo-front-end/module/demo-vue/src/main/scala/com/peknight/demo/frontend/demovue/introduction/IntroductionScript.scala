package com.peknight.demo.frontend.demovue.introduction

import com.peknight.demo.frontend.vue.esm.Vue
import com.peknight.demo.frontend.vue.runtimecore.Component

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object IntroductionScript:
  @JSExportTopLevel(name = "introduction", moduleID = "demo-vue")
  def introduction(): Unit =
    Vue.createApp(Component(data = (_, _) => js.Dynamic.literal("count" -> 0)))
      .mount("#app")

