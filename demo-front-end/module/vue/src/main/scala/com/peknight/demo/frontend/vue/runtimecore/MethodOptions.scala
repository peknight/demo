package com.peknight.demo.frontend.vue.runtimecore

import scala.scalajs.js

// TODO
trait MethodOptions extends js.Object
object MethodOptions:
  def apply(methods: (String, js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit])*): MethodOptions =
    val obj = new js.Object().asInstanceOf[js.Dynamic]
    methods.foreach((k, v) => obj.updateDynamic(k)(v))
    obj.asInstanceOf[MethodOptions]
