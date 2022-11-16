package com.peknight.demo.frontend.apache.echarts.util

import com.peknight.demo.frontend.apache.echarts.Number

import scala.scalajs.js

trait RoamOptionMixin extends js.Object:
  /**
   * If enable roam. can be specified "scale" or "move"
   */
  val roam: js.UndefOr[Boolean | "pan" | "move" | "zoom" | "scale"] = js.undefined
  /**
   * Current center position.
   */
  val center: js.UndefOr[js.Array[Number | String]] = js.undefined
  /**
   * Current zoom level. Default is 1
   */
  val zoom: js.UndefOr[Number] = js.undefined
  val scaleLimit: js.UndefOr[ScaleLimitMixin] = js.undefined
