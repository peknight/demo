package com.peknight.demo.frontend.apache.echarts

import scala.scalajs.js

trait AnimationOptionMixin extends js.Object:
  val animation: js.UndefOr[Boolean] = js.undefined
  val animationThreshold: js.UndefOr[Number] = js.undefined
  val animationDuration: js.UndefOr[Number | AnimationDurationCallback] = js.undefined
  val animationEasing: js.UndefOr[AnimationEasing] = js.undefined
  val animationDelay: js.UndefOr[Number | AnimationDelayCallback] = js.undefined
  val animationDurationUpdate: js.UndefOr[Number | AnimationDurationCallback] = js.undefined
  val animationEasingUpdate: js.UndefOr[AnimationEasing] = js.undefined
  val animationDelayUpdate: js.UndefOr[Number | AnimationDelayCallback] = js.undefined
