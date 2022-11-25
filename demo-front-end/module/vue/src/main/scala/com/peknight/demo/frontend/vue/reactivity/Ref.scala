package com.peknight.demo.frontend.vue.reactivity

import scala.scalajs.js

@js.native
trait Ref[T] extends js.Object:
  var value: T = js.native