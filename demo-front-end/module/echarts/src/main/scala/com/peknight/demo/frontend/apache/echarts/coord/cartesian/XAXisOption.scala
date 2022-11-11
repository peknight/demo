package com.peknight.demo.frontend.apache.echarts.coord.cartesian

import com.peknight.demo.frontend.apache.echarts.coord.{CategoryAxisBaseOption, DataMixin}
import com.peknight.demo.frontend.apache.echarts.util.OrdinalRawValue

import scala.scalajs.js

object XAXisOption:
  def category(data: js.UndefOr[js.Array[OrdinalRawValue | DataMixin]] = js.undefined): XAXisOption =
    val _data = data
    new CategoryAxisBaseOption with CartesianAxisMixin with XAXisMainTypeMixin {
      override val data: js.UndefOr[js.Array[OrdinalRawValue | DataMixin]] = _data
    }
