package com.peknight.demo.frontend.vue.runtimecore

import scala.scalajs.js

// TODO
trait Component extends js.Object
object Component:
  def apply(data: js.UndefOr[js.ThisFunction1[CreateComponentPublicInstance, CreateComponentPublicInstance, js.Any]] = js.undefined): Component =
    val obj = new js.Object()
    val dynamic = obj.asInstanceOf[js.Dynamic]
    data.foreach(dynamic.data = _)
    obj.asInstanceOf[Component]
