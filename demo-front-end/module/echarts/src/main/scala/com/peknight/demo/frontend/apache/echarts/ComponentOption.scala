package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait ComponentOption extends js.Object with MainTypeMixin:
  type Type <: String
  type NameType <: OptionName
  val `type`: js.UndefOr[Type] = js.undefined
  val id: js.UndefOr[OptionId] = js.undefined
  val name: js.UndefOr[NameType] = js.undefined
  val z: js.UndefOr[Number] = js.undefined
  val zlevel: js.UndefOr[Number] = js.undefined
