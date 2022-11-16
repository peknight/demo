package com.peknight.demo.frontend.apache.echarts.coord.geo

import com.peknight.demo.frontend.apache.echarts.Number
import com.peknight.demo.frontend.apache.echarts.util.RoamOptionMixin

import scala.scalajs.js

trait GeoCommonOptionMixin extends RoamOptionMixin:
  val map: js.UndefOr[String] = js.undefined
  val aspectScale: js.UndefOr[Number] = js.undefined
  val layoutCenter: js.UndefOr[js.Array[Number | String]] = js.undefined
  val layoutSize: js.UndefOr[Number | String] = js.undefined
  val boundingCoords: js.UndefOr[js.Array[js.Array[Number]]] = js.undefined
  val nameMap: js.UndefOr[NameMap] = js.undefined
  val nameProperty: js.UndefOr[String] = js.undefined
  /**
   * Use raw projection by default
   * Only available for GeoJSON source.
   *
   * NOTE: `center` needs to be the projected coord if projection is used.
   */
  val projection: js.UndefOr[GeoProjection] = js.undefined
