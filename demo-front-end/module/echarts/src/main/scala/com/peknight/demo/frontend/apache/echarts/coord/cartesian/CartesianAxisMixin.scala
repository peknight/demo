package com.peknight.demo.frontend.apache.echarts.coord.cartesian

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.OrdinalSortInfo

import scala.scalajs.js

trait CartesianAxisMixin extends js.Object:
  val gridIndex: js.UndefOr[Number] = js.undefined
  val gridId: js.UndefOr[String] = js.undefined
  val position: js.UndefOr[CartesianAxisPosition] = js.undefined
  val offset: js.UndefOr[Number] = js.undefined
  val categorySortInfo: js.UndefOr[OrdinalSortInfo] = js.undefined
