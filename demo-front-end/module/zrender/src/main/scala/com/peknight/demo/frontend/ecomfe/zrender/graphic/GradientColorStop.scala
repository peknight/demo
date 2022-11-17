package com.peknight.demo.frontend.ecomfe.zrender.graphic

import com.peknight.demo.frontend.ecomfe.zrender.Number

import scala.scalajs.js

@js.native
trait GradientColorStop extends js.Object:
  val offset: Number = js.native
  val color: String = js.native

object GradientColorStop:
  def apply(offset: js.UndefOr[Number] = js.undefined, color: js.UndefOr[String] = js.undefined): GradientColorStop =
    val gradientColorStop: js.Object with js.Dynamic = js.Dynamic.literal()
    offset.foreach(o => gradientColorStop.offset = o.asInstanceOf[js.Any])
    color.foreach(gradientColorStop.color = _)
    gradientColorStop.asInstanceOf[GradientColorStop]
