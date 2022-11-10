package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

@js.native
trait ZREventProperties extends js.Object:
  val zrX: Number = js.native
  val zrY: Number = js.native
  val zrDelta: Number = js.native
  val zrEventControl: "no_globalout" | "only_globalout" = js.native
  val zrByTouch: Boolean = js.native
