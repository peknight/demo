package com.peknight.demo.frontend.ecomfe.zrender.graphic

import com.peknight.demo.frontend.ecomfe.zrender.Number

import scala.scalajs.js

@js.native
trait GradientObject extends js.Object:
  type Type <: String
  val id: js.UndefOr[Number] = js.native
  val `type`: Type = js.native
  val colorStops: js.Array[GradientColorStop] = js.native
  val global: js.UndefOr[Boolean] = js.native
