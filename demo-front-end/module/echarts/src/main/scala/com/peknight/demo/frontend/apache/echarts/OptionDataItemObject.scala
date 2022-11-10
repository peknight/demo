package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait OptionDataItemObject[T] extends js.Object:
  val id: js.UndefOr[OptionId] = js.undefined
  val name: js.UndefOr[OptionName] = js.undefined
  val groupId: js.UndefOr[OptionId] = js.undefined
  val value: js.UndefOr[js.Array[T] | T] = js.undefined
  val selected: js.UndefOr[Boolean] = js.undefined

