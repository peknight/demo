package com.peknight.demo.frontend.apache.echarts.mixin

import scala.scalajs.js

trait MainTypeMixin extends js.Object:
  type MainType <: String
  val mainType: js.UndefOr[MainType] = js.undefined
