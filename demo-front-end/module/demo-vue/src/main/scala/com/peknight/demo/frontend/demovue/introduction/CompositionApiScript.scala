package com.peknight.demo.frontend.demovue.introduction

import com.peknight.demo.frontend.vue.esm.Vue
import com.peknight.demo.frontend.vue.runtimecore.{Component, MethodOptions}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object CompositionApiScript:
  @JSExportTopLevel(name = "compositionApi", moduleID = "demo-vue")
  def introduction(): Unit =
    Vue.createApp(Component(setup = (_, _, _) => {
      val count = Vue.ref(0)
      val increment: js.Function0[Unit] = () => count.value += 1
      Vue.onMounted(() => dom.console.log(s"The initial count is ${count.value}."))
      js.Dynamic.literal(
        "count" -> count,
        "increment" -> increment
      )
    })).mount("#app")
