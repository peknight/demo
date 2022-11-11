package com.peknight.demo.frontend.apache.echarts.chart.bar

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.`export`.SeriesInjectedOption
import com.peknight.demo.frontend.apache.echarts.util.{ItemStyleOption, OptionDataValue, OptionId, OptionName}

import scala.scalajs.js

trait BarSeriesOption extends BarSeriesOptionInner with SeriesInjectedOption

object BarSeriesOption:

  def apply(id: js.UndefOr[OptionId] = js.undefined,
            name: js.UndefOr[OptionName] = js.undefined,
            z: js.UndefOr[Number] = js.undefined,
            zlevel: js.UndefOr[Number] = js.undefined,
            coordinateSystem: js.UndefOr["cartesian2d" | "polar"] = js.undefined,
            clip: js.UndefOr[Boolean] = js.undefined,
            roundCap: js.UndefOr[Boolean] = js.undefined,
            showBackground: js.UndefOr[Boolean] = js.undefined,
            backgroundStyle: js.UndefOr[ItemStyleOption[js.Any] & BorderRadiusMixin] = js.undefined,
            data: js.UndefOr[js.Array[BarDataItemOption | OptionDataValue | js.Array[OptionDataValue]]] = js.undefined,
            realtimeSort: js.UndefOr[Boolean] = js.undefined): BarSeriesOption =
    val _id: js.UndefOr[OptionId] = id
    val _name: js.UndefOr[OptionName] = name
    val _z: js.UndefOr[Number] = z
    val _zlevel: js.UndefOr[Number] = zlevel
    val _coordinateSystem: js.UndefOr["cartesian2d" | "polar"] = coordinateSystem
    val _clip: js.UndefOr[Boolean] = clip
    val _roundCap: js.UndefOr[Boolean] = roundCap
    val _showBackground: js.UndefOr[Boolean] = showBackground
    val _backgroundStyle: js.UndefOr[ItemStyleOption[js.Any] & BorderRadiusMixin] = backgroundStyle
    val _data: js.UndefOr[js.Array[BarDataItemOption | OptionDataValue | js.Array[OptionDataValue]]] = data
    val _realtimeSort: js.UndefOr[Boolean] = realtimeSort
    new BarSeriesOption {
      override val `type`: js.UndefOr[Type] = "bar"
      override val id: js.UndefOr[OptionId] = _id
      override val name: js.UndefOr[NameType] = _name
      override val z: js.UndefOr[Number] = _z
      override val zlevel: js.UndefOr[Number] = _zlevel
      override val coordinateSystem: js.UndefOr[CoordinateSystemType] = _coordinateSystem
      override val clip: js.UndefOr[Boolean] = _clip
      override val roundCap: js.UndefOr[Boolean] = _roundCap
      override val showBackground: js.UndefOr[Boolean] = _showBackground
      override val backgroundStyle: js.UndefOr[ItemStyleOption[js.Any] & BorderRadiusMixin] = _backgroundStyle
      override val data: js.UndefOr[DataType] = _data
      override val realtimeSort: js.UndefOr[Boolean] = _realtimeSort
    }
