package com.peknight.demo.frontend.demovue.introduction

import com.peknight.demo.frontend.vue.esm.Vue
import com.peknight.demo.frontend.vue.runtimecore.{Component, MethodOptions}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object OptionsApiScript:
  @JSExportTopLevel(name = "optionsApi", moduleID = "demo-vue")
  def introduction(): Unit =
    Vue.createApp(Component(
      data = (_, _) => js.Dynamic.literal("count" -> 0),
      methods = MethodOptions("increment" -> ((obj: js.Dynamic) => obj.count = obj.count.asInstanceOf[Int] + 1)),
      mounted = (obj: js.Dynamic) => dom.console.log(s"The initial count is ${obj.count}.")
    )).mount("#app")
